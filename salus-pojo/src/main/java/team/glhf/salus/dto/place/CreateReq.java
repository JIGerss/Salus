package team.glhf.salus.dto.place;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Steveny
 * @since 2023/10/30
 */
@Data
@Builder
public class CreateReq implements Serializable {

    private String name;

    private String position;

    private String description;

    private String businessTime;

    private String telephone;

    private Double point;

    private List<MultipartFile> images;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
