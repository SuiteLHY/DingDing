package github.com.suitelhy.webchat.web;

import github.com.suitelhy.webchat.application.service.UserService;
import github.com.suitelhy.webchat.domain.entity.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
@RequestMapping("/hello")
public class HelloController {

    // 测试接口使用
    @Resource
    private UserService userService;

    @ApiOperation(httpMethod = "GET"
            , notes = "跳转到 hello world 展示页面"
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
        User user = userService.selectUserByUserid(userId);
        return user.toString();
    }

}
