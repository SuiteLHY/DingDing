package github.com.suitelhy.dingding.sso.server.web;

import github.com.suitelhy.dingding.core.application.task.LogTask;
import github.com.suitelhy.dingding.core.application.task.UserTask;
import github.com.suitelhy.dingding.core.infrastructure.web.AbstractSecurityUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 用户业务
 *
 * @Description 用户相关业务.
 *
 * @Issue
 * · 关于 $ref 的 Swagger UI 前端页面输出的问题描述:
 * {@link <a href="https://swagger.io/docs/specification/using-ref/">Using $ref | Swagger</a>}
 * {@Solution <a href="https://github.com/springfox/springfox/issues/2563">无法解析指针：/ definitions /列表在文档中不存在·问题＃2563·springfox / springfox</a>}
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    // 用户业务 Task
    @Autowired
    private UserTask userTask;

    // 日志记录业务 Task
    @Autowired
    private LogTask logTask;

//    @Autowired
//    private ObjectMapper toJSONString;

    @ApiOperation(value = "测试接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username"
                    , value = "用户名"
                    , dataType = "String"
                    , defaultValue = "测试20200118132850"
                    , paramType = "query"
                    , required = true)
            , @ApiImplicitParam(name = "password"
            , value = "用户密码"
            , dataType = "String"
            , defaultValue = "test123"
            , paramType = "query"
            , required = true)
    })
    @GetMapping("/login")
    public /*UserDto*/String getUser(@AuthenticationPrincipal final AbstractSecurityUser securityUser, Principal principal) {
        try {
//            return toJSONString.writeValueAsString(/*securityUser.getUserDto()*/principal);
            /*Map<String, String> userInfo = new LinkedHashMap<>(1);
            userInfo.put("username", securityUser.getUsername());

            return toJSONString.writeValueAsString(userInfo);*/
            return securityUser.toString();
        } catch (Exception e) {
            log.error("UserController#getUser", e);
            e.printStackTrace();
        }
        return "{}";
    }

}
