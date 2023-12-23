package team.glhf.salus.service;

import team.glhf.salus.dto.place.CommentReq;
import team.glhf.salus.dto.place.CreateReq;
import team.glhf.salus.dto.place.CreateRes;
import team.glhf.salus.dto.place.PointReq;
import team.glhf.salus.vo.PlaceVo;

import java.util.List;

/**
 * @author Steveny
 * @since 2023/11/21
 */
public interface PlaceService {
    /**
     * 创建健身场所
     */
    CreateRes createPlace(CreateReq createReq);

    /**
     * 根据ID获取健身场所
     */
    PlaceVo getPlaceById(String placeId);

    /**
     * 获取最近的健身场所
     */
    List<PlaceVo> getNearbyPlaces(String position);

    /**
     * 使用半正矢函数公式（Haversine公式）计算两地距离
     */
    double countDistance(String positionA, String positionB);

    /**
     * 使用半正矢函数公式（Haversine公式）计算两地距离
     */
    double countDistance(double jA, double wA, double jB, double wB);

    /**
     * 对健身场所评分
     */
    void pointPlace(PointReq pointReq,boolean isPoint);

    /**
     * 评论场所
     */
    void commentPlace(CommentReq commentReq, boolean isComment);

    /**
     * 查询该场所是否存在，若orElseThrow为true则在不存在时抛出异常
     */
    boolean checkHasPlace(String placeId, boolean orElseThrow);

    /**
     * 查询用户是否评分该场所，若没评分时抛出异常
     */
    boolean checkPointPlace(String userId, String placeId);
}
