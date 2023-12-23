package team.glhf.salus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Place database mapper
 *
 * @author Steveny
 * @since 2023/11/21
 */

@Data
@Builder
@TableName("tb_place")
public class Place implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("name")
    private String name;

    @TableField("images")
    private String images;

    @TableField("position")
    private String position;

    @TableField("city_code")
    private String cityCode;

    @TableField("description")
    private String description;

    @TableField("business_time")
    private String businessTime;

    @TableField("telephone")
    private String telephone;

    @TableField("point")
    private Double point;

    @TableLogic()
    private String exist;

    @Version()
    private Long version;

    @TableField("comments")
    private Long comments;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
