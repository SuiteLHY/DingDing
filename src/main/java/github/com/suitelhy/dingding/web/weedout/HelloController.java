package github.com.suitelhy.dingding.web.weedout;

import github.com.suitelhy.dingding.application.task.UserTask;
import github.com.suitelhy.dingding.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.infrastructure.domain.policy.DBPolicy;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        // ...
        model.addAttribute("userid", "aaa");
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
    public String select(@AuthenticationPrincipal SecurityUser currentUser
            , @RequestParam("id") String userId) {
        //=== 拦截器 ===//
        if (!DBPolicy.MYSQL.validateUuid(userId)) {
            return "{}";
        }
        //======//
        /*User result = userTask.selectUserByUserid(userId);*/
        UserDto result = UserDto.Factory.USER_DTO.create(currentUser);
        return result.toString();
    }

}
