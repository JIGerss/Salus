package team.glhf.salus.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.glhf.salus.annotation.JwtVerify;
import team.glhf.salus.dto.place.CommentReq;
import team.glhf.salus.dto.place.CreateReq;
import team.glhf.salus.dto.place.CreateRes;
import team.glhf.salus.dto.place.PointReq;
import team.glhf.salus.result.Result;
import team.glhf.salus.service.PlaceService;

/**
 * Place controller for web application
 *
 * @author Felix
 * @since 2023/11/21
 */
@RestController
@RequestMapping("/place")
public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @PostMapping("/create")
    public Result<CreateRes> createPlace(CreateReq createReq) {
        return Result.okResult(placeService.createPlace(createReq));
    }

    @JwtVerify
    @PostMapping("/point")
    public Result<Object> pointPlace(@RequestBody @Validated PointReq pointReq) {
        placeService.pointPlace(pointReq,true);
        return Result.okResult();
    }

    @JwtVerify
    @PostMapping("/unpoint")
    public Result<Object> unpointPlace(@RequestBody @Validated PointReq pointReq) {
        placeService.pointPlace(pointReq,false);
        return Result.okResult();
    }

    @JwtVerify
    @PostMapping("/comment")
    public Result<Object> commentPlace(@RequestBody @Validated CommentReq commentReq) {
        placeService.commentPlace(commentReq,true);
        return Result.okResult();
    }

    @JwtVerify
    @PostMapping("/uncomment")
    public Result<Object> uncommentPlace(@RequestBody @Validated CommentReq commentReq) {
        placeService.commentPlace(commentReq,false);
        return Result.okResult();
    }

}
