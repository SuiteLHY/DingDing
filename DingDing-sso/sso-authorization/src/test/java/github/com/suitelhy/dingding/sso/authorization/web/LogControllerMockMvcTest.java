package github.com.suitelhy.dingding.sso.authorization.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web层 - <class>LogController</class> MockMvc测试 (静态) (未启动服务)
 *
 * @Description 使用 MockMvc 实现 Http请求 模拟 Web 交互流程。</br>
 *-> 通过使用标注在测试用例上的 @AutoConfigureMockMvc 注释来注入 MockMvc 实例.
 * @author Suite
 */
@SpringBootTest
@AutoConfigureMockMvc
public class LogControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void selectAll() throws Exception {
        Map<String, String> requsrtParams = new LinkedHashMap<>(2);
        requsrtParams.put("userid", "admin");
        requsrtParams.put("page", Integer.toString(1));

        ResultActions resultActions = this.mockMvc
                .perform(get("/log/" + requsrtParams.get("userid") + "/log")
                        .param("page", requsrtParams.get("page")))
                .andDo(print());
        // 验证返回结果
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(
                        containsString(requsrtParams.get("userid"))));
        resultActions.andReturn();
    }

}
