package team.glhf.salus.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.glhf.salus.annotation.JwtVerify;
import team.glhf.salus.dto.article.*;
import team.glhf.salus.result.Result;
import team.glhf.salus.service.ArticleService;

/**
 * Article controller for web application
 *
 * @author Steveny
 * @since 2023/10/30
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @JwtVerify
    @PostMapping("/create")
    public Result<CreateRes> createArticle(@Validated CreateReq createReq) {
        return Result.okResult(articleService.createArticle(createReq));
    }

    @JwtVerify
    @DeleteMapping("/delete")
    public Result<Object> deleteArticle(@Validated DeleteReq deleteReq) {
        articleService.deleteArticle(deleteReq);
        return Result.okResult();
    }

    @JwtVerify
    @PostMapping("/like")
    public Result<Object> likeArticle(@RequestBody @Validated LikeReq likeReq) {
        articleService.likeArticle(likeReq, true);
        return Result.okResult();
    }

    @JwtVerify
    @PostMapping("/unlike")
    public Result<Object> unlikeArticle(@RequestBody @Validated LikeReq likeReq) {
        articleService.likeArticle(likeReq, false);
        return Result.okResult();
    }

    @JwtVerify
    @PostMapping("/comment")
    public Result<Object> commentArticle(@RequestBody @Validated CommentReq commentReq) {
        articleService.commentArticle(commentReq,true);
        return Result.okResult();
    }

    @JwtVerify
    @PostMapping("/uncomment")
    public Result<Object> uncommentArticle(@RequestBody @Validated CommentReq commentReq) {
        articleService.commentArticle(commentReq,false);
        return Result.okResult();
    }
}
