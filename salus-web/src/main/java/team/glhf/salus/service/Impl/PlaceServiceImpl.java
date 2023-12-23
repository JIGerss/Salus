package team.glhf.salus.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import team.glhf.salus.dto.common.LocationRes;
import team.glhf.salus.dto.place.CommentReq;
import team.glhf.salus.dto.place.CreateReq;
import team.glhf.salus.dto.place.CreateRes;
import team.glhf.salus.dto.place.PointReq;
import team.glhf.salus.entity.Place;
import team.glhf.salus.entity.relation.PlaceOwnComment;
import team.glhf.salus.entity.relation.UserPointPlace;
import team.glhf.salus.enumeration.FilePathEnum;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.exception.PlaceException;
import team.glhf.salus.mapper.PlaceMapper;
import team.glhf.salus.mapper.PlaceOwnCommentMapper;
import team.glhf.salus.mapper.UserPointPlaceMapper;
import team.glhf.salus.service.CommonService;
import team.glhf.salus.service.PlaceService;
import team.glhf.salus.vo.PlaceCommentVo;
import team.glhf.salus.vo.PlaceVo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Steveny
 * @since 2023/11/21
 */
@Service
public class PlaceServiceImpl extends ServiceImpl<PlaceMapper, Place> implements PlaceService {
    private final CommonService commonService;
    private final TransactionTemplate transactionTemplate;
    private final UserPointPlaceMapper userPointPlaceMapper;
    private final PlaceOwnCommentMapper placeOwnCommentMapper;

    public PlaceServiceImpl(CommonService commonService, TransactionTemplate transactionTemplate, UserPointPlaceMapper userPointPlaceMapper, PlaceOwnCommentMapper placeOwnCommentMapper) {
        this.commonService = commonService;
        this.transactionTemplate = transactionTemplate;
        this.userPointPlaceMapper = userPointPlaceMapper;
        this.placeOwnCommentMapper = placeOwnCommentMapper;
    }

    @Override
    public CreateRes createPlace(CreateReq createReq) {
        Place place = Place.builder().build();
        BeanUtil.copyProperties(createReq, place, "images", "setCityCode");
        // 更新位置并上传图片
        LocationRes location = commonService.getLocation(createReq.getPosition());
        List<String> urlList = commonService.uploadAllImages(createReq.getImages(), FilePathEnum.PLACE_IMAGE);
        place.setCityCode(location.getCityCode());
        place.setImages(JSONUtil.toJsonStr(urlList));
        save(place);
        return CreateRes.builder()
                .placeId(place.getId())
                .build();
    }

    @Override
    public PlaceVo getPlaceById(String placeId) {
        return place2PlaceVo(getById(placeId));
    }

    @Override
    public List<PlaceVo> getNearbyPlaces(String position) {
        LocationRes location = commonService.getLocation(position);
        if (null == location) {
            return new ArrayList<>();
        }
        // 查出同城的所有场所
        List<Place> placeList = list(Wrappers.lambdaQuery(Place.class)
                .eq(Place::getCityCode, location.getCityCode())
        );
        return placeList.stream()
                .sorted((a, b) ->
                        Double.compare(countDistance(a.getPosition(), position),
                                countDistance(b.getPosition(), position))
                )
                .limit(10)
                .map(this::place2PlaceVo)
                .toList();
    }

    @Override
    public double countDistance(String positionA, String positionB) {
        double[] posA = Arrays.stream(positionA.split(",")).mapToDouble(Double::parseDouble).toArray();
        double[] posB = Arrays.stream(positionB.split(",")).mapToDouble(Double::parseDouble).toArray();
        return countDistance(posA[0], posA[1], posB[0], posB[1]);
    }

    @Override
    public double countDistance(double jA, double wA, double jB, double wB) {
        try {
            jA *= 0.01745329251994329;
            wA *= 0.01745329251994329;
            jB *= 0.01745329251994329;
            wB *= 0.01745329251994329;
            double var1 = Math.sin(jA);
            double var2 = Math.sin(wA);
            double var3 = Math.cos(jA);
            double var4 = Math.cos(wA);
            double var5 = Math.sin(jB);
            double var6 = Math.sin(wB);
            double var7 = Math.cos(jB);
            double var8 = Math.cos(wB);
            double[] var10 = new double[3];
            double[] var20 = new double[3];
            var10[0] = var4 * var3;
            var10[1] = var4 * var1;
            var10[2] = var2;
            var20[0] = var8 * var7;
            var20[1] = var8 * var5;
            var20[2] = var6;
            double distance = 0.001 * Math.asin(Math.sqrt((var10[0] - var20[0]) * (var10[0] - var20[0])
                    + (var10[1] - var20[1]) * (var10[1] - var20[1]) + (var10[2] - var20[2]) *
                    (var10[2] - var20[2])) / 2.0) * 1.27420015798544E7;
            return new BigDecimal(distance).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (RuntimeException e) {
            throw new PlaceException(HttpCodeEnum.GET_LOCATION_ERROR);
        }
    }

    @Override
    public void pointPlace(PointReq pointReq, boolean isPoint) {
        // check whether place exists
        checkHasPlace(pointReq.getPlaceId(), true);
        // point or point the article
        if (isPoint) {
            // whether is pointed
            if (checkPointPlace(pointReq.getUserId(), pointReq.getPlaceId())) {
                throw new PlaceException(HttpCodeEnum.PLACE_ALREADY_POINTED);
            }
            // start a transaction
            transactionTemplate.execute(status -> {
                // generate new userPointPlace instance
                UserPointPlace userPointPlace = UserPointPlace.builder()
                        .userId(pointReq.getUserId())
                        .placeId(pointReq.getPlaceId())
                        .point(pointReq.getPoint())
                        .build();
                userPointPlaceMapper.insert(userPointPlace);
                // update place table
                Long count = userPointPlaceMapper.selectCount(Wrappers.lambdaQuery(UserPointPlace.class)
                        .eq(UserPointPlace::getPlaceId, pointReq.getPlaceId())
                );
                update(Wrappers.lambdaUpdate(Place.class)
                        .eq(Place::getId, pointReq.getPlaceId())
                        .setSql("point = (point + {0}) / {1}", pointReq.getPoint(), count)
                );
                return Boolean.TRUE;
            });
        } else {
            // whether is not pointed
            if (!checkPointPlace(pointReq.getUserId(), pointReq.getPlaceId())) {
                throw new PlaceException(HttpCodeEnum.PLACE_NOT_POINTED);
            }
            // start a transaction
            transactionTemplate.execute(status -> {
                // get point record
                UserPointPlace record = userPointPlaceMapper.selectOne(Wrappers.lambdaQuery(UserPointPlace.class)
                        .eq(UserPointPlace::getUserId, pointReq.getUserId())
                        .eq(UserPointPlace::getPlaceId, pointReq.getPlaceId())
                );
                // remove points row
                userPointPlaceMapper.delete(Wrappers.lambdaQuery(UserPointPlace.class)
                        .eq(UserPointPlace::getUserId, pointReq.getUserId())
                        .eq(UserPointPlace::getPlaceId, pointReq.getPlaceId())
                );
                // update place table
                Long count = userPointPlaceMapper.selectCount(Wrappers.lambdaQuery(UserPointPlace.class)
                        .eq(UserPointPlace::getPlaceId, pointReq.getPlaceId())
                );
                update(Wrappers.lambdaUpdate(Place.class)
                        .eq(Place::getId, pointReq.getPlaceId())
                        .setSql("point = (point * ({1} + 1) - {0}) / {1}", record.getPoint(), count)
                );
                return Boolean.TRUE;
            });
        }
    }

    @Override
    public void commentPlace(CommentReq commentReq, boolean isComment) {
        // check whether place exists
        checkHasPlace(commentReq.getPlaceId(), true);
        // comment or uncomment the place
        if (isComment) {
            // start a transaction
            transactionTemplate.execute(status -> {
                // generate new placeOwnComment instance
                PlaceOwnComment placeOwnComment = PlaceOwnComment.builder()
                        .userId(commentReq.getUserId())
                        .placeId(commentReq.getPlaceId())
                        .comment(commentReq.getComment())
                        .build();
                placeOwnCommentMapper.insert(placeOwnComment);
                // update place table
                update(Wrappers.lambdaUpdate(Place.class)
                        .eq(Place::getId, commentReq.getPlaceId())
                        .setSql("comments = comments + 1")
                );
                return Boolean.TRUE;
            });
        } else {
            // start a transaction
            transactionTemplate.execute(status -> {
                // remove comments row
                placeOwnCommentMapper.delete(Wrappers.lambdaQuery(PlaceOwnComment.class)
                        .eq(PlaceOwnComment::getUserId, commentReq.getUserId())
                        .eq(PlaceOwnComment::getPlaceId, commentReq.getPlaceId())
                        .eq(PlaceOwnComment::getComment, commentReq.getComment())
                );
                // update place table
                update(Wrappers.lambdaUpdate(Place.class)
                        .eq(Place::getId, commentReq.getPlaceId())
                        .setSql("comments = comments - 1")
                );
                return Boolean.TRUE;
            });
        }
    }

    @Override
    public boolean checkHasPlace(String placeId, boolean orElseThrow) {
        Place nullablePlace = getById(placeId);
        if (orElseThrow) {
            Opt.ofNullable(nullablePlace).orElseThrow(() -> new PlaceException(HttpCodeEnum.PLACE_NOT_EXIST));
            return true;
        } else {
            return null != nullablePlace;
        }
    }

    @Override
    public boolean checkPointPlace(String userId, String placeId) {
        return null != userPointPlaceMapper.selectOne(Wrappers.lambdaQuery(UserPointPlace.class)
                .eq(UserPointPlace::getUserId, userId)
                .eq(UserPointPlace::getPlaceId, placeId)
        );
    }

    private PlaceVo place2PlaceVo(Place place) {
        PlaceVo placeVo = PlaceVo.builder().build();
        BeanUtil.copyProperties(place, placeVo, "images", "comments");
        placeVo.setImages(JSONUtil.toList(place.getImages(), String.class));
        // 装配评论
        List<PlaceOwnComment> comments = placeOwnCommentMapper.selectList(Wrappers.lambdaQuery(PlaceOwnComment.class)
                .eq(PlaceOwnComment::getPlaceId, place.getId())
        );
        List<PlaceCommentVo> commentVoList = comments.stream().map(comment ->
            BeanUtil.copyProperties(comment, PlaceCommentVo.class)
        ).toList();
        placeVo.setComments(commentVoList);
        // 装配位置
        LocationRes location = commonService.getLocation(place.getPosition());
        placeVo.setFormattedAddress(location.getFormattedAddress());
        return placeVo;
    }
}
