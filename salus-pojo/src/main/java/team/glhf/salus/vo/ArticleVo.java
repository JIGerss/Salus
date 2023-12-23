package team.glhf.salus.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Steveny
 * @since 2023/11/12
 */
@Data
@Builder
public class ArticleVo implements Serializable {

    private String id;

    private List<String> images;

    private String title;

    private String content;

    private List<String> tags;

    private List<CommentVo> commentList;

    private String position;

    private Long likes;

    private Long comments;

    private String createTime;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
