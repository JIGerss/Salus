package team.glhf.salus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * User database mapper
 *
 * @author Steveny
 * @since 2023/9/22
 */

@Data
@Builder
@TableName("tb_user")
public class User implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("nickname")
    private String nickname;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("password")
    private String password;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    @TableField("gender")
    private String gender;

    @TableField("birthday")
    private String birthday;

    @TableField("height")
    private Long height;

    @TableField("weight")
    private Long weight;

    @TableField("signature")
    private String signature;

    @TableField("avatar")
    private String avatar;

    @TableField("fans")
    private Long fans;

    @TableLogic()
    private String exist;

    @Version()
    private Long version;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
