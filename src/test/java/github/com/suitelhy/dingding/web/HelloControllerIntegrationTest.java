package github.com.suitelhy.dingding.web;

import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * HelloController Web 层动态测试 (启动服务)
 *
 * @Description 使用随机端口.
 * @Reference
 *-> <a href="https://blog.codeleak.pl/2019/09/spring-boot-testing-with-junit-5.html#spring-boot-test-with-mocked-web-layer">
 *->     使用JUnit 5进行Spring Boot测试</a>
 *-> , <a href="https://github.com/kolorobot/spring-boot-junit5/blob/master/src/test/java/pl/codeleak/samples/springbootjunit5/todo/TaskControllerIntegrationTest.java">
 *->     spring-boot-junit5/TaskControllerIntegrationTest.java at master · kolorobot/spring-boot-junit5</a>
 *-> , <a href="https://stackoverflow.com/questions/8297215/spring-resttemplate-get-with-parameters">
 *->     java - Spring RestTemplate GET with parameters - Stack Overflow</a>
 *
 */
/**
 * AssertJ
 *
 * @Reference <a href="https://assertj.github.io/doc/">AssertJ - fluent assertions java library</a>
 *-> , <a href="https://blog.csdn.net/neven7/article/details/51448559">
 *->     断言神器AssertJ快速入门_neven7的专栏-CSDN博客</a>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerIntegrationTest {

    @LocalServerPort
    private int port;

    /**
     * org.springframework.boot.test.web.client.TestRestTemplate
     * @Description 替代 RestTemplate 方法适用于集成测试。
     * @Reference <a href="https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/web/client/TestRestTemplate.html">
     *->     TestRestTemplate（Spring Boot Docs 2.2.2.RELEASE API）</a>
     */
    @Autowired
    private TestRestTemplate restTemplate;

    @NotNull
    private String webRequestRoot() {
        return "http://localhost:" + port;
    }

    @Test
    public void helloPage() {
        // act
        var task = restTemplate.getForObject(webRequestRoot() + "/hello/helloPage"
                , String.class);
        // assert
        assertThat(task).contains("Hello World!");
    }

    @Test
    public void hello() {
        // act
        var task = restTemplate.getForObject(webRequestRoot() + "/hello/hello"
                , String.class);
        // assert
        assertThat(task).contains("hello");
    }

    @Test
    public void select() {
        // act
        Map<String, Object> requestParams = new LinkedHashMap<>(1);
        requestParams.put("id", "402880e56f88643d016f886449e10000");
        var task = restTemplate.getForObject(webRequestRoot() + "/hello/select?"
                        + "id={id}"
                , String.class
                , /*requestParamsMap*/requestParams.get("id"));
        // assert
        assertThat(task).contains("{" + requestParams.get("id") + "}");
    }

}
