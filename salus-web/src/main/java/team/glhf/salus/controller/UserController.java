package team.glhf.salus.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.glhf.salus.annotation.JwtVerify;
import team.glhf.salus.dto.user.*;
import team.glhf.salus.result.Result;
import team.glhf.salus.service.UserService;

/**
 * User controller for web application
 *
 * @author Steveny
 * @since 2023/9/22
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<LoginRes> loginUser(@RequestBody @Validated LoginReq loginDto) {
        return Result.okResult(userService.loginUser(loginDto));
    }

    @PostMapping("/register")
    public Result<RegisterRes> registerUser(@RequestBody @Validated RegisterReq registerDto) {
        return Result.okResult(userService.registerUser(registerDto));
    }

    @PostMapping("/forget")
    public Result<ForgetRes> forgetUser(@RequestBody @Validated ForgetReq forgetDto) {
        return Result.okResult(userService.forgetUser(forgetDto));
    }

    @JwtVerify
    @PostMapping("/update")
    public Result<Object> updateUser(@Validated UpdateReq updateDto) {
        userService.updateUser(updateDto);
        return Result.okResult();
    }

    @JwtVerify
    @PostMapping("/subscribe")
    public Result<Object> subscribeUser(@RequestBody @Validated SubscribeReq subscribeReq) {
        userService.subscribeUser(subscribeReq, true);
        return Result.okResult();
    }

    @JwtVerify
    @PostMapping("/unsubscribe")
    public Result<Object> unsubscribeUser(@RequestBody @Validated SubscribeReq subscribeReq) {
        userService.subscribeUser(subscribeReq, false);
        return Result.okResult();
    }
}
