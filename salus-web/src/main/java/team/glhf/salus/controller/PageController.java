package team.glhf.salus.controller;

import cn.hutool.core.lang.Opt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.glhf.salus.annotation.JwtVerify;
import team.glhf.salus.dto.page.*;
import team.glhf.salus.result.Result;
import team.glhf.salus.service.PageService;
import team.glhf.salus.vo.detail.DetailVo;
import team.glhf.salus.vo.dynamic.DynamicVo;
import team.glhf.salus.vo.gym.PlaceDetailVo;
import team.glhf.salus.vo.gym.PlacePageVo;
import team.glhf.salus.vo.index.IndexListArticleVo;
import team.glhf.salus.vo.index.IndexVo;
import team.glhf.salus.vo.user.UserPageVo;

/**
 * Page controller for web application
 *
 * @author Steveny
 * @since 2023/10/30
 */
@RestController
@RequestMapping("/page")
public class PageController {
    private final PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @JwtVerify
    @GetMapping("/index")
    public Result<IndexVo> getIndexPage(@Validated IndexReq indexReq) {
        return Result.okResult(pageService.getIndexPage(indexReq));
    }

    @JwtVerify
    @GetMapping("/getIndexArticles")
    public Result<IndexListArticleVo> getRecommendArticles(@Validated IndexListArticleReq indexReq) {
        return Result.okResult(pageService.getIndexArticles(indexReq));
    }

    @JwtVerify
    @GetMapping("/getUserPage")
    public Result<UserPageVo> getUserPage(@RequestParam(required = false) String userId) {
        return Result.okResult(pageService.getUserPage(userId));
    }

    @JwtVerify
    @GetMapping("/getDynamicPage")
    public Result<DynamicVo> getDynamicPage(@RequestParam(required = false) String userId, Integer page) {
        return Result.okResult(pageService.getDynamicPage(userId, Opt.ofNullable(page).orElse(1)));
    }

    @JwtVerify
    @GetMapping("/getDetailPage")
    public Result<DetailVo> getDetailPage(@Validated DetailReq detailReq) {
        return Result.okResult(pageService.getDetailPage(detailReq));
    }

    @GetMapping("/getGymListPage")
    public Result<PlacePageVo> getGymList(@Validated PlaceListReq placeListReq) {
        return Result.okResult(pageService.getGymListPage(placeListReq));
    }

    @GetMapping("/getGymDetailPage")
    public Result<PlaceDetailVo> getDymDetail(@Validated PlaceReq placeReq) {
        return Result.okResult(pageService.getGymDetailPage(placeReq));
    }
}
