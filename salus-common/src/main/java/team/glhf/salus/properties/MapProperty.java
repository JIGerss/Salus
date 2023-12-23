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
@ConfigurationProperties(prefix = "salus.map")
public class MapProperty {
    public String key;

    public String poi;

    public String radius;
}
