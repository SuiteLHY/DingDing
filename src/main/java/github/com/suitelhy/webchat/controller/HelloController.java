package github.com.suitelhy.webchat.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @ApiOperation(value = "跳转到 hello world 展示页面", httpMethod = "GET")
    @GetMapping("/helloPage")
    public ModelAndView helloPage(Model model) {
        model.addAttribute("message", "Hello World!");
        return new ModelAndView("example/hello");
    }

    @ApiOperation(value = "获取 hello 消息", httpMethod = "POST")
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

}
