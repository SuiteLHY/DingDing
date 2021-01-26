package github.com.suitelhy.dingding.sso.authorization.web.security;

import github.com.suitelhy.dingding.core.application.task.security.ResourceTask;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.security.ResourceDto;
import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.core.infrastructure.web.model.WebResult;
import github.com.suitelhy.dingding.sso.authorization.infrastructure.web.security.util.OAuth2AuthenticationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * (安全) 资源业务
 *
 * @Description (安全) 资源相关业务.
 */
@RestController
@RequestMapping("/security/resource")
@Slf4j
public class SecurityResourceController {

//    @Autowired
//    private ObjectMapper toJSONString;

    @Autowired
    private ResourceTask resourceTask;

    /**
     * 查询用户关联的资源（集）
     *
     * @param authentication
     * @return
     */
    @GetMapping("/resourceList")
    public String getResourceList(@AuthenticationPrincipal OAuth2Authentication authentication) {
        try {
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

            @NotNull TaskResult<List<ResourceDto>> taskResult_selectResourceByUsername = resourceTask.selectResourceByUsername(userAuthenticationDetails.getUserName(), userAuthenticationDetails);

            return WebResult.Factory.DEFAULT.create(taskResult_selectResourceByUsername).toString();
        } catch (Exception e) {
            log.error("SecurityResourceController#getResourceList", e);
            e.printStackTrace();
        }
        return WebResult.Factory.DEFAULT.createDefault().toString();
    }

}
