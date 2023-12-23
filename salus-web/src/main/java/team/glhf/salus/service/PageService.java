package team.glhf.salus.service;

import team.glhf.salus.dto.page.*;
import team.glhf.salus.vo.detail.DetailVo;
import team.glhf.salus.vo.dynamic.DynamicVo;
import team.glhf.salus.vo.gym.PlaceDetailVo;
import team.glhf.salus.vo.gym.PlacePageVo;
import team.glhf.salus.vo.index.IndexListArticleVo;
import team.glhf.salus.vo.index.IndexVo;
import team.glhf.salus.vo.user.UserPageVo;

/**
 * @author Steveny
 * @since 2023/11/12
 */
public interface PageService {

    /**
     * 请求首页
     */
    IndexVo getIndexPage(IndexReq indexReq);

    /**
     * 请求额外的文章
     */
    IndexListArticleVo getIndexArticles(IndexListArticleReq indexReq);

    /**
     * 请求用户页面
     */
    UserPageVo getUserPage(String userId);

    /**
     * 请求动态页面
     */
    DynamicVo getDynamicPage(String userId, Integer page);

    /**
     * 请求文章详情页面
     */
    DetailVo getDetailPage(DetailReq detailReq);

    /**
     * 请求健身房列表页面
     */
    PlacePageVo getGymListPage(PlaceListReq placeListReq);

    /**
     * 请求健身房详情页面
     */
    PlaceDetailVo getGymDetailPage(PlaceReq placeReq);
}
