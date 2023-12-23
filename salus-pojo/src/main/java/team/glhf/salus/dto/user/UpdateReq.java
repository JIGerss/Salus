package team.glhf.salus.dto.user;

import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Felix
 * @since 2023/10/21
 */
@Data
@Builder
public class UpdateReq implements Serializable {

    @Null(message = "cannot use userId to access")
    private String userId;

    private String nickname;

    private String gender;

    private String birthday;

    private Long height;

    private Long weight;

    private String signature;

    private MultipartFile avatar;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;

}
