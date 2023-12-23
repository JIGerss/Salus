package team.glhf.salus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Steveny
 * @since 2023/10/3
 */
@Data
@Component
@ConfigurationProperties(prefix = "salus.email")
public class EmailCodeProperty {
    public int timeout;
    public int refresh;
    public String timeunit;
    public String subject;
    public String from;
}
