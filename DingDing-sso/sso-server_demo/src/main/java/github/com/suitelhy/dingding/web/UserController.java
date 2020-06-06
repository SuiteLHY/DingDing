package github.com.suitelhy.dingding.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 用户业务
 *
 * @Description 用户相关业务.
 * @Issue · 关于 $ref 的 Swagger UI 前端页面输出的问题描述:
 * -> <Docs><a href="https://swagger.io/docs/specification/using-ref/">
 * ->     Using $ref | Swagger</a></Docs>
 * -> , <Solution><a href="https://github.com/springfox/springfox/issues/2563">
 * ->     无法解析指针：/ definitions /列表在文档中不存在·问题＃2563·springfox / springfox</a></Solution>
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private ObjectMapper toJSONString;

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
    public /*UserDto*/String getUser(Principal principal) {
        try {
            return toJSONString.writeValueAsString(principal);
        } catch (JsonProcessingException e) {
            log.error("UserController#getUser", e);
            e.printStackTrace();
        }
        return "{}";
    }

}