package github.com.suitelhy.dingding.userservice.web;

import github.com.suitelhy.dingding.core.application.task.UserTask;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.core.infrastructure.web.model.WebResult;
import github.com.suitelhy.dingding.userservice.infrastructure.web.security.util.OAuth2AuthenticationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * 用户业务
 *
 * @Description 用户基础增删查改业务.
 */
@RequestMapping("/user")
@RestController
@Slf4j
public class UserCRUDController {

    /*@Autowired
    private ObjectMapper toJSONString;*/

    @Autowired
    private UserTask userTask;

    /**
     * 查询用户 - 非安全认证基础信息
     *
     * @param authentication
     * @return
     */
    @GetMapping("/userDetail")
    public String getUserDetail(@AuthenticationPrincipal OAuth2Authentication authentication) {
        try {
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

            return WebResult.Factory.DEFAULT.create(userTask.selectUserByUsername(userAuthenticationDetails.getUserName(), userAuthenticationDetails)).toString();
        } catch (Exception e) {
            log.error("UserCRUDController#getUserDetail", e);
            e.printStackTrace();
        }
        return WebResult.Factory.DEFAULT.createDefault().toString();
    }

    /**
     * 添加一个用户
     *
     * @param authentication
     * @return
     */
    @GetMapping("/addUser")
    public String registerUser(@AuthenticationPrincipal OAuth2Authentication authentication, HttpServletRequest httpServletRequest) {
        try {
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

//            @NotNull UserDto newUser = UserDto.Factory.USER_DTO.create();
//
//            return WebResult.Factory.DEFAULT.create(userTask.insert(userAuthenticationDetails.getUserName(), userAuthenticationDetails)).toString();
        } catch (Exception e) {
            log.error("UserCRUDController#getUserDetail", e);
            e.printStackTrace();
        }
        return WebResult.Factory.DEFAULT.createDefault().toString();
    }


}
