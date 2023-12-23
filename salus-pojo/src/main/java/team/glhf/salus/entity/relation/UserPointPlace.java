package team.glhf.salus.entity.relation;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * user_point_place database mapper
 *
 * @author Felix
 * @since 2023/12/11
 */
@Data
@Builder
@TableName("user_point_place")
public class UserPointPlace implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("place_id")
    private String placeId;

    @TableField("point")
    private Double point;

    @TableLogic()
    private String exist;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
