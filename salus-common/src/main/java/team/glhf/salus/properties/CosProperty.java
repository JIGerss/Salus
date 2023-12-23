package team.glhf.salus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Steveny
 * @since 2023/11/5
 */
@Data
@Component
@ConfigurationProperties(prefix = "salus.cos")
public class CosProperty {
    public String secretId;

    public String secretKey;

    public String region;

    public String bucketName;

    public String url;
}
