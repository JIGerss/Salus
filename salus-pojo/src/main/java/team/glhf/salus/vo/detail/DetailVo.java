package team.glhf.salus.vo.detail;

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
public class DetailVo implements Serializable {

    private DetailUserVo host;

    private String articleId;

    private String title;

    private String content;

    private Boolean isLiked;

    private Long likes;

    private Long comments;

    private List<String> tags;

    private List<String> images;

    private List<DetailCommentVo> commentList;

    private String position;

    private String formattedAddress;

    private String createTime;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
