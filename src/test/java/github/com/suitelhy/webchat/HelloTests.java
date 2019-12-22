package github.com.suitelhy.webchat;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Controller层（API）单元测试
 *
 * @description 使用MockMvc实现Http请求模拟流程。</br>
 * @author Suite
 */
@SpringBootTest
public class HelloTests {

    /*@Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    }

    @Test
    public void getHello() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello")// 构造一个请求，Post请求就用.post方法
                .contentType(MediaType.APPLICATION_JSON_UTF8)// 代表发送端发送的数据格式是application/json;charset=UTF-8
                .accept(MediaType.APPLICATION_JSON_UTF8)// 代表客户端希望接受的数据类型为application/json;charset=UTF-8
        )// 执行一个请求
                .andExpect(content().string(equalTo("Hello World!"))*//* 访问响应主体断言... *//*)// 添加执行完成后的断言
                .andExpect(status().isOk()*//* 看请求的状态响应码是否为200如果不是则抛异常，测试不通过 *//*)// 添加执行完成后的断言
                .andDo(MockMvcResultHandlers.print())*//* 打印出执行结果 *//*;
    }*/

}
