package team.glhf.salus.dto.place;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/10/30
 */
@Data
@Builder
public class CreateRes implements Serializable {
    private String placeId;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
