package team.glhf.salus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Article database mapper
 *
 * @author Steveny
 * @since 2023/9/22
 */

@Data
@Builder
@TableName("tb_article")
public class Article implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("images")
    private String images;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("position")
    private String position;

    @TableField("likes")
    private Long likes;

    @TableField("comments")
    private Long comments;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @TableLogic()
    private String exist;

    @Version()
    private Long version;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;

    /**
     * 已初始化的builder
     */
    public static ArticleBuilder initedBuilder() {
        return Article.builder()
                .images("[]")
                .position("{}")
                .comments(0L)
                .likes(0L);
    }
}
