package github.com.suitelhy.dingding.app.web;

import github.com.suitelhy.dingding.app.application.task.LogTask;
import github.com.suitelhy.dingding.app.application.task.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    // 用户业务 Task
    @Autowired
    private UserTask userTask;

    // 日志记录业务 Task
    @Autowired
    private LogTask logTask;

    


}
