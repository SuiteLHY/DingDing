package github.com.suitelhy.webchat.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Web层（API） MockMvc测试 (静态) (未启动服务)
 *
 * @description 使用 MockMvc 实现 Http请求 模拟 Web 交互流程。</br>
 *-> 通过使用标注在测试用例上的 @AutoConfigureMockMvc 注释来注入 MockMvc 实例.
 * @Reference
 *-> <a href="https://github.com/kolorobot/spring-boot-junit5/blob/master/src/test/java/pl/codeleak/samples/springbootjunit5/todo/TaskControllerMockMvcTest.java">
 *     spring-boot-junit5/TaskControllerMockMvcTest.java at master · kolorobot/spring-boot-junit5</a>
 *-> , <a href="https://blog.codeleak.pl/2019/09/spring-boot-testing-with-junit-5.html#spring-boot-test-with-mocked-web-layer">
 *->     使用JUnit 5进行Spring Boot测试</a>
 *-> , <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#spring-mvc-test-framework">
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
@SpringBootTest
// 自动注入 MockMvc
@AutoConfigureMockMvc
public class HelloControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void helloPage() throws Exception {
        this.mockMvc.perform(get("/hello/helloPage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello World!")));
    }

    @Test
    public void hello() throws Exception {
        this.mockMvc.perform(post("/hello/hello"))
                .andDo(print())
                // 验证返回结果
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("hello")));
    }

    @Test
    public void select() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(get("/hello/select")
                        .param("id", "402880e56f88643d016f886449e10000"))
                .andDo(print())
                /*// 验证返回结果
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{admin}")))*/;
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(containsString("{402880e56f88643d016f886449e10000}")));
        resultActions.andReturn();
    }

}
