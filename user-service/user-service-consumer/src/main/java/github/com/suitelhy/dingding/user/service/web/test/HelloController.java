package github.com.suitelhy.dingding.user.service.web.test;

import github.com.suitelhy.dingding.core.infrastructure.web.model.WebResult;
import github.com.suitelhy.dingding.user.service.api.domain.service.HelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RequestMapping("/hello")
@RestController
public class HelloController {

    @Reference
    private HelloService helloService;

    @Value("${test.nacos.config.hello}")
    private String helloContent;

    @GetMapping("/say")
    public String sayHello() {
        return helloService.sayHello("aaa");
    }

    @GetMapping("/testHello")
    public String testHello() {
        return WebResult.Factory.DEFAULT.create(WebResult.Vo.StatusVo.SUCCESS
                , null
                , helloContent
                , "")
                .toString();
    }

}
