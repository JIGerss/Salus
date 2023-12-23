import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import team.glhf.salus.result.Result;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;

/**
 * @author Steveny
 * @since 2023/11/3
 */
@SuppressWarnings("all")
@AutoConfigureMockMvc
@SpringBootTest(classes = {team.glhf.salus.SalusApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        , properties = {"mybatis-plus.configuration.log-impl = "})
public class MockTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Test
    void testArticleOperation() {

    }

    @Test
    void testPlaceOperation() {

    }

    @Test
    void testGameOperation() {

    }

    @Test
    void testUserOperation() {

    }

    private <T> Result<T> request(MockHttpServletRequestBuilder request, Object requestDto, Class<T> tClass, String token) throws Exception {
        request.content(JSONUtil.toJsonStr(requestDto));
        request.header("Authorization", token);
        return perform(request, tClass);
    }

    private <T> Result<T> getRequest(MockHttpServletRequestBuilder request, Object requestDto, Class<T> tClass, String token) throws Exception {
        if (null != requestDto) {
            Class<?> aClass = requestDto.getClass();
            for (Field field : aClass.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    request.param(field.getName(), field.get(requestDto).toString());
                }
            }
        }
        request.header("Authorization", token);
        return perform(request, tClass);
    }

    private <T> Result<T> perform(MockHttpServletRequestBuilder request, Class<T> tClass) throws Exception {
        request.contentType("application/json");
        ResultActions action = mockMvc.perform(request);
        action.andExpect(MockMvcResultMatchers.status().isOk());
        String content = action.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject = JSONUtil.parseObj(content);
        Integer code = jsonObject.get("code", Integer.class);
        String message = jsonObject.get("message", String.class);
        T data = jsonObject.get("data", tClass);
        return Result.errorResult(code, message, data);
    }
}
