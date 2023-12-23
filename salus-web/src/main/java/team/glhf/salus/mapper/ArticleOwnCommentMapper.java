package team.glhf.salus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.glhf.salus.entity.relation.ArticleOwnComment;

/**
 * ArticleOwnCommentMapper
 *
 * @author Felix
 * @since 2023/11/09
 */
@Mapper
public interface ArticleOwnCommentMapper extends BaseMapper<ArticleOwnComment> {
}
