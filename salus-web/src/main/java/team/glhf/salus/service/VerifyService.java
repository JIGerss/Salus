package team.glhf.salus.service;

import team.glhf.salus.dto.verify.EmailCodeReq;
import team.glhf.salus.dto.verify.EmailCodeRes;
import team.glhf.salus.dto.verify.SmsCodeReq;
import team.glhf.salus.dto.verify.SmsCodeRes;

/**
 * @author Steveny
 * @since 2023/9/23
 */
public interface VerifyService {
    /**
     * generate a email verifyCode and send a mail or return an existed code
     */
    EmailCodeRes getEmailVerifyCode(EmailCodeReq codeDto);

    /**
     * generate a sms verifyCode and send a message or return an existed code
     */
    SmsCodeRes getSmsVerifyCode(SmsCodeReq codeDto);

    /**
     * check whether the verifyCode is correct
     */
    void checkVerifyCode(String key, String verifyCode);

    /**
     * 删除key保存的验证码
     */
    void deleteCode(String key);
}
