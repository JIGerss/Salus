package team.glhf.salus.service.Impl;

import cn.hutool.core.date.DateUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import team.glhf.salus.dto.verify.EmailCodeReq;
import team.glhf.salus.dto.verify.EmailCodeRes;
import team.glhf.salus.dto.verify.SmsCodeReq;
import team.glhf.salus.dto.verify.SmsCodeRes;
import team.glhf.salus.entity.Code;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.exception.EmailCodeException;
import team.glhf.salus.properties.EmailCodeProperty;
import team.glhf.salus.service.CommonService;
import team.glhf.salus.service.VerifyService;
import team.glhf.salus.utils.SalusUtil;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Steveny
 * @since 2023/9/23
 */
@Service
public class VerifyServiceImpl implements VerifyService {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final CommonService commonService;
    private final EmailCodeProperty property;

    public VerifyServiceImpl(EmailCodeProperty property, RedisTemplate<Object, Object> redisTemplate, CommonService commonService) {
        this.commonService = commonService;
        this.property = property;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public EmailCodeRes getEmailVerifyCode(EmailCodeReq codeDto) {
        Code code = getOrSetCode(codeDto.getEmail(),
                newCode -> commonService.sendEmailCode(codeDto.getEmail(), newCode));
        return EmailCodeRes.builder()
                .refreshTime(code.getRefreshTime())
                .email(codeDto.getEmail())
                .build();
    }

    @Override
    public SmsCodeRes getSmsVerifyCode(SmsCodeReq codeDto) {
        Code code = getOrSetCode(codeDto.getPhone(),
                newCode -> commonService.sendSmsCode(codeDto.getPhone(), newCode));
        return SmsCodeRes.builder()
                .refreshTime(code.getRefreshTime())
                .phone(codeDto.getPhone())
                .build();
    }

    @Override
    public void checkVerifyCode(String key, String verifyCode) {
        Code code = (Code) redisTemplate.opsForValue().get(key);
        // if the code is NOT exist
        if (null == code) {
            throw new EmailCodeException(HttpCodeEnum.NUMBER_CODE_NOT_EXIST);
        }
        // if user try code > 5 times
        if (0 >= code.getChance()) {
            deleteCode(key);
        } else {
            code.setChance(code.getChance() - 1);
            redisTemplate.opsForValue().set(key, code);
        }
        // if code is NOT correct
        if (!verifyCode.equalsIgnoreCase(code.getCode())) {
            throw new EmailCodeException(HttpCodeEnum.NUMBER_CODE_NOT_EQUAL);
        }
    }

    @Override
    public void deleteCode(String key) {
        redisTemplate.delete(key);
    }

    private Code getOrSetCode(String key, Consumer<String> consumer) {
        // redis database already has a code
        Code code = (Code) redisTemplate.opsForValue().get(key);
        if (null != code) {
            // currentTime <= freshTime
            if (DateUtil.date().compareTo(DateUtil.parse(code.getRefreshTime())) <= 0) {
                return code;
            }
        }
        // set a new or replace a code into redis
        String verifyCode = SalusUtil.getVerifyCode(5, true);
        Code newCode = Code.builder()
                .refreshTime(SalusUtil.offsetTime(property.refresh, property.timeunit))
                .code(verifyCode)
                .chance(5)
                .build();
        redisTemplate.opsForValue().set(key, newCode, property.timeout, TimeUnit.valueOf(property.timeunit));
        consumer.accept(verifyCode);
        return newCode;
    }
}