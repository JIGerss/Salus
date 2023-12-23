package team.glhf.salus.dto.place;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Felix
 * @since 2023/12/11
 */
@Data
@Builder
public class PointReq implements Serializable {

    @Null(message = "cannot use userId to access")
    private String userId;

    @NotBlank(message = "param cannot be null")
    private String placeId;

    @Range(max = 5, min = 1, message = "评分必须在1-5星之间")
    private Double point;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
