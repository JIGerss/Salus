package team.glhf.salus.entity.relation;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * user_like_article database mapper
 *
 * @author Steveny
 * @since 2023/10/31
 */
@Data
@Builder
@TableName("user_like_article")
public class UserLikeArticle implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("article_id")
    private String articleId;

    @TableLogic()
    private String exist;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
