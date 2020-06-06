package github.com.suitelhy.dingding.app.web;

import github.com.suitelhy.dingding.app.application.task.LogTask;
import github.com.suitelhy.dingding.app.application.task.UserTask;
import github.com.suitelhy.dingding.app.infrastructure.application.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {

    // 用户业务 Task
    @Autowired
    private UserTask userTask;

    // 日志记录业务 Task
    @Autowired
    private LogTask logTask;

    @GetMapping("/userInfo/*")
    public String getAllUserInfo() {
        List<UserDto> result = userTask.selectAll(1, 10);
        return null != result ? result.toString() : "{}";
    }

    @GetMapping("/test")
    public String test() {
        return "{status:ok}";
    }

    @GetMapping("/testConfiguration")
    public String testConfiguration(@Value("${security.test}") String test) {
        return test;
    }

}
