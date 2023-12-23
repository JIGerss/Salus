package team.glhf.salus.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.glhf.salus.dto.verify.EmailCodeReq;
import team.glhf.salus.dto.verify.EmailCodeRes;
import team.glhf.salus.result.Result;
import team.glhf.salus.service.VerifyService;

/**
 * VerifyCode controller for web application
 *
 * @author Steveny
 * @since 2023/9/23
 */
@RestController
@RequestMapping("/verify")
public class VerifyCodeController {
    private final VerifyService verifyService;

    public VerifyCodeController(VerifyService verifyService) {
        this.verifyService = verifyService;
    }

    @GetMapping("/email")
    public Result<EmailCodeRes> getEmailVerifyCode(@Validated EmailCodeReq codeDto) {
        return Result.okResult(verifyService.getEmailVerifyCode(codeDto));
    }
}
