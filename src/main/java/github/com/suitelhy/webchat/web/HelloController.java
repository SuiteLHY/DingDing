package github.com.suitelhy.webchat.web;

import github.com.suitelhy.webchat.application.task.UserTask;
import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.infrastructure.domain.policy.DBPolicy;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private UserTask userTask;

    @ApiOperation(httpMethod = "GET"
            , notes = "获取 hello world 展示页面的数据"
            , value = "hello world")
    @GetMapping("/helloPage")
    public ModelAndView helloPage(Model model) {
        model.addAttribute("message", "Hello World!");
        return new ModelAndView("example/hello");
    }

    @ApiOperation(httpMethod = "POST"
            , notes = "获取 hello 消息"
            , value = "hello")
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    // 测试接口
    @ApiOperation(value = "测试接口 - 查询用户信息")
    @ApiParam(defaultValue = "admin"
            , name = "id"
            , required = true
            , value = "用户id")
    @RequestMapping(value = "/select"
            , method = {RequestMethod.GET, RequestMethod.POST})
    public String select(@RequestParam("id") String userId) {
        //=== 拦截器 ===//
        if (!DBPolicy.MYSQL.validateUuid(userId)) {
            return "{}";
        }
        //======//
        User result = userTask.selectUserByUserid(userId);
        return null != result ? result.toString() : "{}";
    }

}
