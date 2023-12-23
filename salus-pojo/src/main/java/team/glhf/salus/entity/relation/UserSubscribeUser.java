package team.glhf.salus.entity.relation;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * user_subscribe_user database mapper
 *
 * @author Steveny
 * @since 2023/11/14
 */
@Data
@Builder
@TableName("user_subscribe_user")
public class UserSubscribeUser implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("from_user_id")
    private String fromUserId;

    @TableField("to_user_id")
    private String toUserId;

    @TableLogic()
    private String exist;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
