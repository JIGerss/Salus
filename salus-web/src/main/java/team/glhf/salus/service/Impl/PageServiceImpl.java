package team.glhf.salus.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.stereotype.Service;
import team.glhf.salus.dto.common.LocationRes;
import team.glhf.salus.dto.page.*;
import team.glhf.salus.entity.User;
import team.glhf.salus.service.*;
import team.glhf.salus.utils.SalusUtil;
import team.glhf.salus.vo.*;
import team.glhf.salus.vo.detail.DetailCommentUserVo;
import team.glhf.salus.vo.detail.DetailCommentVo;
import team.glhf.salus.vo.detail.DetailUserVo;
import team.glhf.salus.vo.detail.DetailVo;
import team.glhf.salus.vo.dynamic.DynamicArticleUserVo;
import team.glhf.salus.vo.dynamic.DynamicArticleVo;
import team.glhf.salus.vo.dynamic.DynamicCommentVo;
import team.glhf.salus.vo.dynamic.DynamicVo;
import team.glhf.salus.vo.gym.PlaceDetailCommentVo;
import team.glhf.salus.vo.gym.PlaceDetailVo;
import team.glhf.salus.vo.gym.PlacePageGymVo;
import team.glhf.salus.vo.gym.PlacePageVo;
import team.glhf.salus.vo.index.*;
import team.glhf.salus.vo.user.UserArticleVo;
import team.glhf.salus.vo.user.UserPageVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steveny
 * @since 2023/11/12
 */
@Service
public class PageServiceImpl implements PageService {
    private final UserService userService;
    private final ArticleService articleService;
    private final CommonService commonService;
    private final PlaceService placeService;

    public PageServiceImpl(UserService userService, ArticleService articleService, CommonService commonService, PlaceService placeService) {
        this.userService = userService;
        this.articleService = articleService;
        this.commonService = commonService;
        this.placeService = placeService;
    }

    @Override
    public IndexVo getIndexPage(IndexReq indexReq) {
        // 装配推荐标签
        List<TagVo> recommendTags = articleService.getRecommendTags();
        List<IndexTagVo> indexTagVos = recommendTags.stream()
                .map(tagVo -> IndexTagVo.builder()
                        .hot(tagVo.getHot())
                        .image(tagVo.getImage())
                        .tag(tagVo.getTag())
                        .build())
                .toList();
        // 装配文章
        IndexListArticleVo articles = getIndexArticles(IndexListArticleReq.builder()
                .userId(indexReq.getUserId())
                .articleIds(null)
                .build()
        );
        // 装配推荐场所
        List<PlaceVo> nearbyPlaces = placeService.getNearbyPlaces(indexReq.getPosition());
        List<IndexPlaceVo> placeVos = new ArrayList<>();
        for (PlaceVo placeVo : nearbyPlaces) {
            IndexPlaceVo indexPlaceVo = BeanUtil.copyProperties(placeVo, IndexPlaceVo.class);
            // 装配封面
            if (!placeVo.getImages().isEmpty()) {
                indexPlaceVo.setCover(placeVo.getImages().get(0));
            }
            // 计算距离
            indexPlaceVo.setDistance(placeService.countDistance(placeVo.getPosition(), indexReq.getPosition()));
            placeVos.add(indexPlaceVo);
        }
        return IndexVo.builder()
                .topics(indexTagVos)
                .articles(articles.getArticles())
                .places(placeVos)
                .build();
    }

    @Override
    public IndexListArticleVo getIndexArticles(IndexListArticleReq indexReq) {
        List<ArticleVo> recommendArticles = articleService.getRecommendArticles(indexReq.getArticleIds());
        List<IndexArticleVo> indexArticleVoList = new ArrayList<>();
        for (ArticleVo article : recommendArticles) {
            String hostId = articleService.whoOwnArticle(article.getId());
            User user = userService.getUserById(hostId);
            boolean isLiked = articleService.checkLikeArticle(indexReq.getUserId(), article.getId());
            IndexArticleUserVo indexArticleUserVo = IndexArticleUserVo.builder()
                    .avatar(user.getAvatar())
                    .nickname(user.getNickname())
                    .build();
            IndexArticleVo indexArticleVo = IndexArticleVo.builder()
                    .articleId(article.getId())
                    .title(article.getTitle())
                    .isLiked(isLiked)
                    .host(indexArticleUserVo)
                    .build();
            if (!article.getImages().isEmpty()) {
                indexArticleVo.setCover(article.getImages().get(0));
            }
            indexArticleVoList.add(indexArticleVo);
        }
        return IndexListArticleVo.builder().articles(indexArticleVoList).build();
    }

    @Override
    public UserPageVo getUserPage(String userId) {
        User user = userService.getUserById(userId);
        List<User> subscribers = userService.getSubscribers(userId);
        List<ArticleVo> likedArticles = articleService.getLikedArticles(userId);
        List<ArticleVo> ownedArticles = articleService.getOwnedArticles(userId);
        List<UserArticleVo> likedVo = likedArticles.stream()
                .map(likedArticle -> {
                    UserArticleVo one = BeanUtil.copyProperties(likedArticle, UserArticleVo.class);
                    one.setArticleId(likedArticle.getId());
                    if (!likedArticle.getImages().isEmpty()) {
                        one.setCover(likedArticle.getImages().get(0));
                    }
                    return one;
                }).toList();
        List<UserArticleVo> ownedVo = ownedArticles.stream()
                .map(ownArticle -> {
                    UserArticleVo one = BeanUtil.copyProperties(ownArticle, UserArticleVo.class);
                    one.setArticleId(ownArticle.getId());
                    one.setArticleId(ownArticle.getId());
                    if (!ownArticle.getImages().isEmpty()) {
                        one.setCover(ownArticle.getImages().get(0));
                    }
                    return one;
                }).toList();
        return UserPageVo.builder()
                .userId(userId)
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .fans(user.getFans())
                .subscriptions((long) subscribers.size())
                .likedArticles(likedVo)
                .ownedArticles(ownedVo)
                .build();
    }

    @Override
    public DynamicVo getDynamicPage(String userId, Integer page) {
        int currentPage = (page - 1) * 10;
        List<User> subscribers = userService.getSubscribers(userId);
        List<String> idList = subscribers.stream().map(User::getId).toList();
        List<DynamicArticleVo> articles = new ArrayList<>();
        for (String subId : idList) {
            List<ArticleVo> ownedArticles = articleService.getOwnedArticles(subId);
            List<DynamicArticleVo> ownDynamicArticles = new ArrayList<>();
            for (ArticleVo ownedArticle : ownedArticles) {
                boolean isLiked = articleService.checkLikeArticle(userId, ownedArticle.getId());
                DynamicArticleVo dynamicArticleVo = BeanUtil.copyProperties(ownedArticle,
                        DynamicArticleVo.class, "commentList");
                dynamicArticleVo.setArticleId(ownedArticle.getId());
                dynamicArticleVo.setIsLiked(isLiked);
                // 装配评论
                List<DynamicCommentVo> commentList = new ArrayList<>();
                for (CommentVo commentVo : ownedArticle.getCommentList()) {
                    User commentOwner = userService.getUserById(commentVo.getUserId());
                    commentList.add(DynamicCommentVo.builder()
                            .nickname(commentOwner.getNickname())
                            .comment(commentVo.getComment())
                            .build()
                    );
                }
                dynamicArticleVo.setCommentList(commentList);
                // 装配用户
                User articleOwner = userService.getUserById(subId);
                dynamicArticleVo.setHost(DynamicArticleUserVo.builder()
                        .avatar(articleOwner.getAvatar())
                        .nickname(articleOwner.getNickname())
                        .userId(articleOwner.getId())
                        .build()
                );
                ownDynamicArticles.add(dynamicArticleVo);
            }
            articles.addAll(ownDynamicArticles);
        }
        List<DynamicArticleVo> sortedList = articles.stream()
                .sorted((a, b) -> SalusUtil.compareTime(a.getCreateTime(), b.getCreateTime()))
                .skip(currentPage)
                .limit(10)
                .toList();
        // 装配位置
        for (DynamicArticleVo dynamicArticleVo : sortedList) {
            if (null != dynamicArticleVo.getPosition()) {
                LocationRes location = commonService.getLocation(dynamicArticleVo.getPosition());
                dynamicArticleVo.setFormattedAddress(location.getFormattedAddress());
            }
        }
        return DynamicVo.builder()
                .articles(sortedList)
                .build();
    }

    @Override
    public DetailVo getDetailPage(DetailReq detailReq) {
        ArticleVo article = articleService.getArticleById(detailReq.getArticleId());
        DetailVo detailVo = BeanUtil.copyProperties(article, DetailVo.class, "commentList");
        detailVo.setArticleId(detailReq.getArticleId());
        // 装配点赞
        boolean isLiked = articleService.checkLikeArticle(detailReq.getUserId(), detailReq.getArticleId());
        detailVo.setIsLiked(isLiked);
        // 装配评论
        List<DetailCommentVo> commentVoList = new ArrayList<>();
        for (CommentVo commentVo : article.getCommentList()) {
            User user = userService.getUserById(commentVo.getUserId());
            DetailCommentUserVo detailCommentUserVo = DetailCommentUserVo.builder()
                    .avatar(user.getAvatar())
                    .nickname(user.getNickname())
                    .build();
            DetailCommentVo detailCommentVo = DetailCommentVo.builder()
                    .commentUser(detailCommentUserVo)
                    .comment(commentVo.getComment())
                    .createTime(commentVo.getCreateTime())
                    .build();
            commentVoList.add(detailCommentVo);
        }
        commentVoList.sort((a, b) -> SalusUtil.compareTime(a.getCreateTime(), b.getCreateTime()));
        detailVo.setCommentList(commentVoList);
        // 装配用户
        String hostId = articleService.whoOwnArticle(detailReq.getArticleId());
        User host = userService.getUserById(hostId);
        boolean hasSubscribeUser = userService.checkHasSubscribeUser(detailReq.getUserId(), hostId);
        DetailUserVo detailUserVo = DetailUserVo.builder()
                .isSubscribed(hasSubscribeUser)
                .avatar(host.getAvatar())
                .nickname(host.getNickname())
                .build();
        detailVo.setHost(detailUserVo);
        // 装配位置
        if (null != detailVo.getPosition()) {
            LocationRes location = commonService.getLocation(detailVo.getPosition());
            detailVo.setFormattedAddress(location.getFormattedAddress());
        }
        return detailVo;
    }

    @Override
    public PlacePageVo getGymListPage(PlaceListReq placeListReq) {
        List<PlacePageGymVo> gyms = new ArrayList<>();
        List<PlaceVo> nearbyPlaces = placeService.getNearbyPlaces(placeListReq.getPosition());
        for (PlaceVo place : nearbyPlaces) {
            PlacePageGymVo placePageGymVo = PlacePageGymVo.builder()
                    .cover(place.getImages().get(0))
                    .id(place.getId())
                    .comments(place.getComments().size())
                    .distance(placeService.countDistance(placeListReq.getPosition(), place.getPosition()))
                    .point(place.getPoint())
                    .name(place.getName())
                    .build();
            // 装配位置
            if (null != place.getPosition()) {
                LocationRes location = commonService.getLocation(place.getPosition());
                placePageGymVo.setFormattedAddress(location.getFormattedAddress());
            }
            gyms.add(placePageGymVo);
        }
        return PlacePageVo.builder()
                .gyms(gyms)
                .build();
    }

    @Override
    public PlaceDetailVo getGymDetailPage(PlaceReq placeReq) {
        PlaceVo place = placeService.getPlaceById(placeReq.getPlaceId());
        PlaceDetailVo detailVo = BeanUtil.copyProperties(place, PlaceDetailVo.class, "comments");
        // 装配评论
        List<PlaceDetailCommentVo> commentVoList = new ArrayList<>();
        for (PlaceCommentVo comment : place.getComments()) {
            PlaceDetailCommentVo detailCommentVo = BeanUtil.copyProperties(comment, PlaceDetailCommentVo.class);
            User user = userService.getUserById(comment.getUserId());
            detailCommentVo.setNickname(user.getNickname());
            commentVoList.add(detailCommentVo);
        }
        detailVo.setComments(commentVoList);
        return detailVo;
    }
}
