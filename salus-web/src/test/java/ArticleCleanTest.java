import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import team.glhf.salus.entity.Article;
import team.glhf.salus.entity.relation.UserOwnArticle;
import team.glhf.salus.mapper.ArticleMapper;
import team.glhf.salus.mapper.UserOwnArticleMapper;
import team.glhf.salus.service.ArticleService;

import java.util.List;

/**
 * @author Steveny
 * @since 2023/11/3
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = {team.glhf.salus.SalusApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        , properties = {"mybatis-plus.configuration.log-impl = "})
public class ArticleCleanTest {

    @Test
    void cleanArticles(@Autowired JdbcTemplate jdbcTemplate, @Autowired ArticleMapper articleMapper) {
        List<Article> articles = articleMapper.selectList(null);
        for (Article article : articles) {
            if ("[]".equals(article.getImages())) {
                jdbcTemplate.update("delete from tb_article where id = ?", article.getId());
                jdbcTemplate.update("delete from article_own_comment where article_id = ?", article.getId());
                jdbcTemplate.update("delete from article_own_tag where article_id = ?", article.getId());
                jdbcTemplate.update("delete from user_like_article where article_id = ?", article.getId());
                jdbcTemplate.update("delete from user_own_article where article_id = ?", article.getId());
            }
        }
    }

    @Test
    void clean(@Autowired JdbcTemplate jdbcTemplate, @Autowired ArticleService articleService, @Autowired UserOwnArticleMapper userOwnArticleMapper) {
        List<UserOwnArticle> ownArticles = userOwnArticleMapper.selectList(null);
        System.out.println(ownArticles);
        for (UserOwnArticle ownArticle : ownArticles) {
            if (!articleService.checkHasArticle(ownArticle.getArticleId(), false)) {
                jdbcTemplate.update("delete from user_own_article where id = ?", ownArticle.getId());
            }
        }
    }
}
