package github.com.suitelhy.dingding.sso.authorization.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.application.task.UserTask;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.core.infrastructure.web.model.WebResult;
import github.com.suitelhy.dingding.sso.authorization.infrastructure.web.security.util.OAuth2AuthenticationUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    /*@Autowired
    private ObjectMapper toJSONString;*/

    @Autowired
    private UserTask userTask;

    /**
     * 查询用户 - 安全认证基础信息
     *
     * @param authentication
     *
     * @return API 相应的主要数据
     */
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
    @GetMapping("/userInfo")
    public String getUser(@AuthenticationPrincipal /*Authentication*/OAuth2Authentication authentication) {
        @NotNull WebResult<String> result;
        try {
            /*Object oAuth2Details = authentication.getUserAuthentication().getDetails();

            if (null != oAuth2Details) {
                result = WebResult.Factory.DEFAULT.create(WebResult.Vo.StatusVo.SUCCESS
                        , null
                        , oAuth2Details.toString()
                        , "");
            } else {
                result = WebResult.Factory.DEFAULT.createFailure(WebResult.Vo.StatusVo.FAILURE
                        , null
                        , ""
                        , ""
                        , WebResult.Vo.ErrorVo.INTERNAL_SERVER_ERROR
                        , "");
            }*/
            /*return (null != oAuth2Details)
                    ? oAuth2Details.toString()
                    : "{}";*/
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

            if (null != userAuthenticationDetails) {
                result = WebResult.Factory.DEFAULT.create(WebResult.Vo.StatusVo.SUCCESS
                        , null
                        , userAuthenticationDetails.toString()
                        , "");
            } else {
                result = WebResult.Factory.DEFAULT.createFailure(WebResult.Vo.StatusVo.FAILURE
                        , null
                        , ""
                        , ""
                        , WebResult.Vo.ErrorVo.INTERNAL_SERVER_ERROR
                        , "");
            }
        } catch (Exception e) {
            log.error("UserController#getUser", e);
            e.printStackTrace();

            result = WebResult.Factory.DEFAULT.createUnknown("接口异常!"
                    , ""
                    , ""
                    , "");
        }
        return result.toString();
    }

//    /**
//     * 查询用户 - 非安全认证基础信息
//     *
//     * @param authentication
//     * @return
//     */
//    @GetMapping("/userDetail")
//    public String getUserDetail(@AuthenticationPrincipal OAuth2Authentication authentication) {
//        WebResult<String> result;
//        try {
//            final Map<String, Object> result = new LinkedHashMap<>(8);
//
//            final Map<String, Object> oAuth2Details = (Map<String, Object>) authentication.getUserAuthentication().getDetails();
//
//            final UserDto userDto = userTask.selectUserByUsername((String) oAuth2Details.get("user_name"));
//
//            result.put("username", userDto.getUsername());
//            result.put("nickname", userDto.getNickname());
//            result.put("age", userDto.getAge());
//            result.put("faceImage", userDto.getFaceImage());
//            result.put("firsttime", userDto.getRegistrationTime());
//            result.put("introduction", userDto.getIntroduction());
//            result.put("sex", userDto.getSex());
//            result.put("status", userDto.getStatus());
//
//            return toJSONString.writeValueAsString(result);
//        } catch (Exception e) {
//            log.error("UserController#getUserDetail", e);
//            e.printStackTrace();
//        }
//        return result.toString();
//    }

}
