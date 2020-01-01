package github.com.suitelhy.webchat.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Web层（API）单元测试
 *
 * @description 使用 MockMvc 实现 Http请求 模拟 Web 交互流程。</br>
 *-> 通过使用标注在测试用例上的 @AutoConfigureMockMvc 注释来注入 MockMvc 实例.
 * @Reference
 *-> <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#spring-mvc-test-framework">
 *->     spring-mvc-test-framework</a>
 *-> , <a href="http://www.ityouknow.com/springboot/2017/05/09/spring-boot-deploy.html">
 *->     Spring Boot(十二)：Spring Boot 如何测试打包部署 - 纯洁的微笑博客 </a>
 *-> , <a href="https://spring.io/guides/gs/testing-web/">
 *->     Getting Started · Testing the Web Layer</a>
 *-> , <a href="https://stackoverflow.com/questions/17972428/mock-mvc-add-request-parameter-to-test/17985015">
 *->     spring - Mock MVC - Add Request Parameter to test - Stack Overflow</a>
 * @author Suite
 */
/**
 * Spring Mockmvc API:
 *-> <a href="https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/MockMvc.html">
 *->     MockMvc (Spring Framework 5.2.2.RELEASE API)</a>
 *
 * Spring Http API:
 *-> <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/package-summary.html">
 *->     org.springframework.http (Spring Framework 5.2.2.RELEASE API)</a>
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
// 自动注入 MockMvc
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    }

    @Test
    public void helloPage() throws Exception {
        this.mockMvc.perform(get("/hello/helloPage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello World")));
    }

    @Test
    public void hello() throws Exception {
        this.mockMvc.perform(get("/hello/hello"))
                .andDo(print())
                // 验证返回结果
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("hello")));
    }

    @Test
    public void select() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(get("/hello/select")
                        .param("id", "admin"))
                .andDo(print())
                /*// 验证返回结果
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{admin}")))*/;
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(containsString("{admin}")));
        resultActions.andReturn();
    }

}
