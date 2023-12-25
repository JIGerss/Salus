package team.glhf.salus.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import team.glhf.salus.dto.article.*;
import team.glhf.salus.entity.Article;
import team.glhf.salus.entity.relation.ArticleOwnComment;
import team.glhf.salus.entity.relation.ArticleOwnTag;
import team.glhf.salus.entity.relation.UserLikeArticle;
import team.glhf.salus.entity.relation.UserOwnArticle;
import team.glhf.salus.enumeration.FilePathEnum;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.exception.ArticleException;
import team.glhf.salus.mapper.*;
import team.glhf.salus.service.ArticleService;
import team.glhf.salus.service.CommonService;
import team.glhf.salus.vo.ArticleVo;
import team.glhf.salus.vo.CommentVo;
import team.glhf.salus.vo.TagVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steveny
 * @since 2023/10/30
 */
@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    private final CommonService commonService;
    private final TransactionTemplate transactionTemplate;
    private final UserOwnArticleMapper userOwnArticleMapper;
    private final UserLikeArticleMapper userLikeArticleMapper;
    private final ArticleOwnTagMapper articleOwnTagMapper;
    private final ArticleOwnCommentMapper articleOwnCommentMapper;

    public ArticleServiceImpl(UserOwnArticleMapper userOwnArticleMapper, TransactionTemplate transactionTemplate, UserLikeArticleMapper userLikeArticleMapper, ArticleOwnTagMapper articleOwnTagMapper, ArticleOwnCommentMapper articleOwnCommentMapper, CommonService commonService) {
        this.commonService = commonService;
        this.userOwnArticleMapper = userOwnArticleMapper;
        this.transactionTemplate = transactionTemplate;
        this.userLikeArticleMapper = userLikeArticleMapper;
        this.articleOwnTagMapper = articleOwnTagMapper;
        this.articleOwnCommentMapper = articleOwnCommentMapper;
    }

    @Override
    public CreateRes createArticle(CreateReq createReq) {
        // upload images
        List<String> urlList = new ArrayList<>();
        if (null != createReq.getImages()) {
            urlList = commonService.uploadAllImages(createReq.getImages(), FilePathEnum.ARTICLE_IMAGE);
        }
        // generate new article instance
        Article article = Article.initedBuilder()
                .title(createReq.getTitle())
                .images(JSONUtil.toJsonStr(urlList))
                .content(createReq.getContent())
                .position(createReq.getPosition())
                .build();
        // start a transaction
        transactionTemplate.execute((status) -> {
            save(article);
            UserOwnArticle userOwnArticle = UserOwnArticle.builder()
                    .userId(createReq.getUserId())
                    .articleId(article.getId())
                    .build();
            userOwnArticleMapper.insert(userOwnArticle);
            List<String> tagList = createReq.getTags().stream().distinct().toList();
            for (String tag : tagList) {
                ArticleOwnTag articleOwnTag = ArticleOwnTag.builder()
                        .articleId(article.getId())
                        .tag(tag)
                        .build();
                articleOwnTagMapper.insert(articleOwnTag);
            }
            return Boolean.TRUE;
        });
        return CreateRes.builder().articleId(article.getId()).build();
    }

    @Override
    public ArticleVo getArticleById(String articleId) {
        checkHasArticle(articleId, true);
        return article2ArticleVo(getById(articleId));
    }

    @Override
    public List<ArticleVo> getRecommendArticles(List<String> articleIds) {
        // 排除已经推荐过的文章
        articleIds = Opt.ofNullable(articleIds).orElseGet(ArrayList::new);
        LambdaQueryWrapper<Article> qw = Wrappers.lambdaQuery(Article.class)
                .notIn(!articleIds.isEmpty(), Article::getId, articleIds);
        List<ArticleVo> articleVoList = new ArrayList<>(10);
        Page<Article> page = new Page<>(1, 1);
        int count = (int) count(qw);
        for (int i = 0; i < 10 && count > 0; i++) {
            page.setCurrent(RandomUtil.randomInt(1, count + 1));
            List<Article> oneArticleList = list(page,
                    qw.notIn(!articleIds.isEmpty(), Article::getId, articleIds)
            );
            Article oneArticle = oneArticleList.get(0);
            articleVoList.add(article2ArticleVo(oneArticle));
            articleIds.add(oneArticle.getId());
            count = count - 1;
        }
        return articleVoList;
    }

    @Override
    public List<ArticleVo> getLikedArticles(String userId) {
        List<UserLikeArticle> likeArticles = userLikeArticleMapper.selectList(Wrappers.lambdaQuery(UserLikeArticle.class)
                .eq(UserLikeArticle::getUserId, userId)
        );
        List<String> idList = likeArticles.stream().map(UserLikeArticle::getArticleId).toList();
        if (idList.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Article> articles = listByIds(idList);
            return articles.stream().map(this::article2ArticleVo).toList();
        }
    }

    @Override
    public List<ArticleVo> getOwnedArticles(String userId) {
        List<UserOwnArticle> ownArticles = userOwnArticleMapper.selectList(Wrappers.lambdaQuery(UserOwnArticle.class)
                .eq(UserOwnArticle::getUserId, userId)
        );
        List<String> idList = ownArticles.stream().map(UserOwnArticle::getArticleId).toList();
        if (idList.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Article> articles = listByIds(idList);
            return articles.stream().map(this::article2ArticleVo).toList();
        }
    }

    @Override
    public List<TagVo> getRecommendTags() {
        List<TagVo> tagVoList = new ArrayList<>(3);
        List<Object> tagNameList = articleOwnTagMapper.selectObjs(Wrappers.query(ArticleOwnTag.class)
                .select("tag")
                .groupBy("tag")
                .orderByDesc("count(tag)")
        );
        for (int i = 0; i < 5 && i < tagNameList.size(); i++) {
            long hot = 0L;
            String image = null;
            String tag = tagNameList.get(i).toString();
            List<ArticleOwnTag> tagList = articleOwnTagMapper.selectList(Wrappers.lambdaQuery(ArticleOwnTag.class)
                    .eq(ArticleOwnTag::getTag, tag)
            );
            List<String> idList = tagList.stream().map(ArticleOwnTag::getArticleId).toList();
            List<Article> articleList = list(Wrappers.query(Article.class).in("id", idList));
            for (Article article : articleList) {
                if (null == image && !"[]".equals(article.getImages())) {
                    image = JSONUtil.toList(article.getImages(), String.class).get(0);
                }
                hot += article.getLikes() + article.getComments();
            }
            TagVo tagVo = TagVo.builder()
                    .tag(tag)
                    .image(image)
                    .hot(hot)
                    .build();
            tagVoList.add(tagVo);
        }
        return tagVoList.stream().sorted((a, b) -> Long.compare(b.getHot(), a.getHot())).limit(3).toList();
    }

    @Override
    public void updateArticle(ModifyReq modifyReq) {
        // check whether user own this article
        checkOwnArticle(modifyReq.getUserId(), modifyReq.getArticleId());
        Article article = Article.builder().id(modifyReq.getArticleId()).build();
        BeanUtil.copyProperties(modifyReq, article, "images");
        article.setImages(JSONUtil.toJsonStr(modifyReq.getImages()));
        updateById(article);
    }

    @Override
    public void deleteArticle(DeleteReq deleteReq) {
        // check whether user own this article
        checkOwnArticle(deleteReq.getUserId(), deleteReq.getArticleId());
        // start a transaction
        transactionTemplate.execute(status -> {
            removeById(deleteReq.getArticleId());
            userOwnArticleMapper.delete(Wrappers.lambdaQuery(UserOwnArticle.class)
                    .eq(UserOwnArticle::getArticleId, deleteReq.getArticleId())
            );
            userLikeArticleMapper.delete(Wrappers.lambdaQuery(UserLikeArticle.class)
                    .eq(UserLikeArticle::getArticleId, deleteReq.getArticleId())
            );
            articleOwnTagMapper.delete(Wrappers.lambdaQuery(ArticleOwnTag.class)
                    .eq(ArticleOwnTag::getArticleId, deleteReq.getArticleId())
            );
            articleOwnCommentMapper.delete(Wrappers.lambdaQuery(ArticleOwnComment.class)
                    .eq(ArticleOwnComment::getArticleId, deleteReq.getArticleId())
            );
            return Boolean.TRUE;
        });
    }

    @Override
    public void likeArticle(LikeReq likeReq, boolean isLike) {
        // check whether article exists
        checkHasArticle(likeReq.getArticleId(), true);
        // like or unlike the article
        if (isLike) {
            // whether is liked
            if (checkLikeArticle(likeReq.getUserId(), likeReq.getArticleId())) {
                throw new ArticleException(HttpCodeEnum.ARTICLE_ALREADY_LIKED);
            }
            // start a transaction
            transactionTemplate.execute(status -> {
                // generate new userLikeArticle instance
                UserLikeArticle userLikeArticle = UserLikeArticle.builder()
                        .userId(likeReq.getUserId())
                        .articleId(likeReq.getArticleId())
                        .build();
                userLikeArticleMapper.insert(userLikeArticle);
                // update article table
                update(Wrappers.lambdaUpdate(Article.class)
                        .eq(Article::getId, likeReq.getArticleId())
                        .setSql("likes = likes + 1")
                );
                return Boolean.TRUE;
            });
        } else {
            // whether is not liked
            if (!checkLikeArticle(likeReq.getUserId(), likeReq.getArticleId())) {
                throw new ArticleException(HttpCodeEnum.ARTICLE_NOT_LIKED);
            }
            // start a transaction
            transactionTemplate.execute(status -> {
                // remove likes row
                userLikeArticleMapper.delete(Wrappers.lambdaQuery(UserLikeArticle.class)
                        .eq(UserLikeArticle::getUserId, likeReq.getUserId())
                        .eq(UserLikeArticle::getArticleId, likeReq.getArticleId())
                );
                // update article table
                update(Wrappers.lambdaUpdate(Article.class)
                        .eq(Article::getId, likeReq.getArticleId())
                        .setSql("likes = likes - 1")
                );
                return Boolean.TRUE;
            });
        }
    }

    @Override
    public void commentArticle(CommentReq commentReq, boolean isComment) {
        // check whether article exists
        checkHasArticle(commentReq.getArticleId(), true);
        // comment or uncomment the article
        if (isComment) {
            // start a transaction
            transactionTemplate.execute(status -> {
                // generate new ArticleOwnComment instance
                ArticleOwnComment articleOwnComment = ArticleOwnComment.builder()
                        .userId(commentReq.getUserId())
                        .articleId(commentReq.getArticleId())
                        .comment(commentReq.getComment())
                        .build();
                articleOwnCommentMapper.insert(articleOwnComment);
                // update article table
                update(Wrappers.lambdaUpdate(Article.class)
                        .eq(Article::getId, commentReq.getArticleId())
                        .setSql("comments = comments + 1")
                );
                return Boolean.TRUE;
            });
        } else {
            // start a transaction
            transactionTemplate.execute(status -> {
                // remove likes row
                articleOwnCommentMapper.delete(Wrappers.lambdaQuery(ArticleOwnComment.class)
                        .eq(ArticleOwnComment::getUserId, commentReq.getUserId())
                        .eq(ArticleOwnComment::getArticleId, commentReq.getArticleId())
                        .eq(ArticleOwnComment::getComment, commentReq.getComment())
                );
                // update article table
                update(Wrappers.lambdaUpdate(Article.class)
                        .eq(Article::getId, commentReq.getArticleId())
                        .setSql("comments = comments - 1")
                );
                return Boolean.TRUE;
            });
        }
    }

    @Override
    public boolean checkHasArticle(String articleId, boolean orElseThrow) {
        Article nullableArticle = getById(articleId);
        if (orElseThrow) {
            Opt.ofNullable(nullableArticle).orElseThrow(() -> new ArticleException(HttpCodeEnum.ARTICLE_NOT_EXIST));
            return true;
        } else {
            return null != nullableArticle;
        }
    }

    @Override
    public boolean checkOwnArticle(String userId, String articleId) {
        if (whoOwnArticle(articleId).equals(userId)) {
            return true;
        }
        throw new ArticleException(HttpCodeEnum.ARTICLE_PERMISSION_ERROR);
    }

    @Override
    public boolean checkLikeArticle(String userId, String articleId) {
        return null != userLikeArticleMapper.selectOne(Wrappers.lambdaQuery(UserLikeArticle.class)
                .eq(UserLikeArticle::getUserId, userId)
                .eq(UserLikeArticle::getArticleId, articleId)
        );
    }

    @Override
    public String whoOwnArticle(String articleId) {
        checkHasArticle(articleId, true);
        UserOwnArticle ownArticle = userOwnArticleMapper.selectOne(Wrappers.lambdaQuery(UserOwnArticle.class)
                .eq(UserOwnArticle::getArticleId, articleId)
        );
        return ownArticle.getUserId();
    }

    private ArticleVo article2ArticleVo(Article article) {
        ArticleVo articleVo = ArticleVo.builder().build();
        BeanUtil.copyProperties(article, articleVo, "images");
        articleVo.setImages(JSONUtil.toList(article.getImages(), String.class));
        // 获取文章的标签
        List<ArticleOwnTag> tagList = articleOwnTagMapper.selectList(Wrappers.lambdaQuery(ArticleOwnTag.class)
                .eq(ArticleOwnTag::getArticleId, article.getId())
        );
        articleVo.setTags(tagList.stream().map(ArticleOwnTag::getTag).toList());
        // 获取文章的评论
        List<ArticleOwnComment> commentList = articleOwnCommentMapper.selectList(Wrappers.lambdaQuery(ArticleOwnComment.class)
                .eq(ArticleOwnComment::getArticleId, article.getId())
        );
        List<CommentVo> commentVos = commentList.stream().map(comment -> CommentVo.builder()
                .userId(comment.getUserId())
                .createTime(comment.getCreateTime())
                .comment(comment.getComment())
                .build()
        ).toList();
        articleVo.setCommentList(commentVos);
        return articleVo;
    }
}
