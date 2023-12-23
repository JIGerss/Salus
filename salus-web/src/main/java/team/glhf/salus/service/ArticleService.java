package team.glhf.salus.service;

import team.glhf.salus.dto.article.*;
import team.glhf.salus.vo.ArticleVo;
import team.glhf.salus.vo.TagVo;

import java.util.List;

/**
 * @author Steveny
 * @since 2023/10/30
 */
@SuppressWarnings("UnusedReturnValue")
public interface ArticleService {
    /**
     * 创建文章
     */
    CreateRes createArticle(CreateReq createReq);

    /**
     * 获取文章
     */
    ArticleVo getArticleById(String articleId);

    /**
     * 随机获取几篇文章
     */
    List<ArticleVo> getRecommendArticles(List<String> articleIds);

    /**
     * 获取已经点赞的文章
     */
    List<ArticleVo> getLikedArticles(String userId);

    /**
     * 获取自己的文章
     */
    List<ArticleVo> getOwnedArticles(String userId);

    /**
     * 随机获取几个热门标签
     */
    List<TagVo> getRecommendTags();

    /**
     * 更新文章，除了tags
     */
    void updateArticle(ModifyReq modifyReq);

    /**
     * 删除文章
     */
    void deleteArticle(DeleteReq deleteReq);

    /**
     * 点赞文章
     */
    void likeArticle(LikeReq likeReq, boolean isLike);

    /**
     * 评论文章
     */
    void commentArticle(CommentReq commentReq, boolean isComment);

    // -------------------------------------------------------------------------------------------

    /**
     * 查询该文章是否存在，若orElseThrow为true则在不存在时抛出异常
     */
    boolean checkHasArticle(String articleId, boolean orElseThrow);

    /**
     * 查询用户是否拥有该文章，若不拥有时抛出异常
     */
    boolean checkOwnArticle(String userId, String articleId);

    /**
     * 查询用户是否点赞该文章，若不拥有时抛出异常
     */
    boolean checkLikeArticle(String userId, String articleId);

    /**
     * 查询哪个用户拥有该文章
     */
    String whoOwnArticle(String articleId);

}
