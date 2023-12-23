package team.glhf.salus.entity.relation;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * article_own_comment database mapper
 *
 * @author Felix
 * @since 2023/11/09
 */
@Data
@Builder
@TableName("article_own_comment")
public class ArticleOwnComment implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("article_id")
    private String articleId;

    @TableField("user_id")
    private String userId;

    @TableField("comment")
    private String comment;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @TableLogic()
    private String exist;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
