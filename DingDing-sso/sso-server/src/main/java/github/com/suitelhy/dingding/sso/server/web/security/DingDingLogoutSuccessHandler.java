package github.com.suitelhy.dingding.sso.server.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.web.model.DingDingResponse;
import github.com.suitelhy.dingding.log.service.api.domain.entity.Log;
import github.com.suitelhy.dingding.log.service.api.domain.service.write.idempotence.LogIdempotentWriteService;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.util.OAuth2AuthenticationUtil;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.event.read.UserReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;

/**
 * DingDing - 注销成功处理器
 *
 * @Description 项目自定义处理器.
 *
 * @see LogoutSuccessHandler
 */
@Component
@Slf4j
public class DingDingLogoutSuccessHandler
        implements LogoutSuccessHandler {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private TokenStore tokenStore;

    @Reference
    private LogIdempotentWriteService logNonIdempotentWriteService;

    @Reference
    private UserReadEvent userReadEvent;

    /**
     * 登出成功时执行的操作
     *
     * @Description 成功退出时调用.
     *
     * @param request
     * @param response
     * @param authentication
     *
     * @throws IOException
     * @throws ServletException
     *
     * @see LogoutSuccessHandler#onLogoutSuccess(HttpServletRequest, HttpServletResponse, Authentication)
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication)
            throws IOException, ServletException
    {
        final String accessToken = request.getParameter("access_token");
        final OAuth2AccessToken oAuth2AccessToken;
        final String cancelled_access_token;

        response.setContentType("application/json;charset=UTF-8");

        if (StringUtils.isNotBlank(accessToken)
                && null != (oAuth2AccessToken = tokenStore.readAccessToken(accessToken))
                && null != (cancelled_access_token = oAuth2AccessToken.getValue()))
        {
            final OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken);
            /*final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(oAuth2Authentication);*/
            if (! (oAuth2Authentication.getUserAuthentication().getDetails() instanceof Map)) {
                throw new InsufficientAuthenticationException("非[OAuth 2 认证标准的安全凭据]");
            }
            final @NotNull Map<String, Object> userAuthenticationDetails = (Map<String, Object>) oAuth2Authentication.getUserAuthentication().getDetails();

            final @NotNull User thisUser = userReadEvent.selectUserByUsername((String) userAuthenticationDetails.get("username"));
            final @NotNull SecurityUser operator = userReadEvent.selectSecurityUserByUsername(thisUser.getUsername());
            final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userReadEvent.selectUserAccountOperationInfoByUsername(thisUser.getUsername());

            final OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();

            tokenStore.removeAccessToken(oAuth2AccessToken);
            tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
            tokenStore.removeRefreshToken(oAuth2RefreshToken);

            //=== 操作日志记录 ===//

            final @NotNull Log newLog_LogoutSuccess = Log.Factory.User.LOG.create(null
                    , null
                    , HandleType.LogVo.USER__LOGGED_OUT
                    , thisUser
                    , operator
                    , operator_UserAccountOperationInfo);
            if (! logNonIdempotentWriteService.insert(newLog_LogoutSuccess)) {
                throw new AuthenticationServiceException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , HandleType.LogVo.USER__LOGGED_OUT.name
                        , "生成操作日志记录"
                        , newLog_LogoutSuccess
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            //======//

            response.getWriter().write(
                    toJSONString.writeValueAsString(new DingDingResponse("access_token {"
                            .concat(cancelled_access_token)
                            .concat("} 注销成功")))
            );
        } else {
            response.getWriter().write(
                    toJSONString.writeValueAsString(new DingDingResponse("注销失败"))
            );
        }
    }

}
