package team.glhf.salus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Steveny
 * @since 2023/10/13
 */
@Data
@Component
@ConfigurationProperties(prefix = "salus.sms")
public class SmsCodeProperty {
    public String secretId;
    public String secretKey;
    public String appId;
    public String signName;
    public String templateId;
}
