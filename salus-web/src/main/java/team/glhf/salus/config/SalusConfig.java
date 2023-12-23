package team.glhf.salus.config;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Project java bean configuration
 *
 * @author Steveny
 * @since 2023/9/18
 */
@Configuration
public class SalusConfig {
    /**
     * MybatisPlus page interceptor
     */
    @Bean
    public MybatisPlusInterceptor mpInterceptor() {
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mpInterceptor;
    }

    /**
     * MybatisPlus OptimisticLockerInnerInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * Mail template generator
     */
    @Bean
    public List<String> mailTemplate() throws IOException {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("/static/mailTemplate.html");
        InputStream inputStream = resources[0].getInputStream();
        BufferedReader reader = IoUtil.getReader(inputStream, StandardCharsets.UTF_8);
        while (reader.ready()) {
            char c = (char) reader.read();
            if (c == '$') {
                list.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        list.add(sb.toString());
        return list;
    }


    /**
     * Redis datasource
     */
    @Bean
    @ConditionalOnMissingBean(
            name = {"redisTemplate"}
    )
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
