package team.glhf.salus.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.glhf.salus.annotation.JwtVerify;
import team.glhf.salus.dto.game.ConfigReq;
import team.glhf.salus.dto.game.SelectReq;
import team.glhf.salus.dto.game.StartGameReq;
import team.glhf.salus.result.Result;
import team.glhf.salus.service.GameService;

/**
 * VerifyCode controller for web application
 *
 * @author Steveny
 * @since 2023/9/23
 */
@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @JwtVerify
    @PostMapping("/configure")
    public Result<Object> configureGame(@RequestBody @Validated ConfigReq configReq) {
        gameService.configureGame(configReq, null);
        return Result.okResult();
    }

    @JwtVerify
    @PostMapping("/selectRole")
    public Result<Object> selectRole(@RequestBody @Validated SelectReq selectReq) {
        gameService.selectRole(selectReq, null);
        return Result.okResult();
    }

    @JwtVerify
    @PostMapping("/startGame")
    public Result<Object> startGame(@RequestBody @Validated StartGameReq startGameReq) {
        gameService.startGame(startGameReq, null);
        return Result.okResult();
    }
}
