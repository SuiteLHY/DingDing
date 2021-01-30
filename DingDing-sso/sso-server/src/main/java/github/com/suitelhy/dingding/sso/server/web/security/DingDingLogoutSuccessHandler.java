package github.com.suitelhy.dingding.sso.server.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.infrastructure.web.model.DingDingResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * DingDing - 注销成功处理器
 *
 * @Description 项目自定义处理器.
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

    /**
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     * @Description 成功退出时调用.
     * @see LogoutSuccessHandler#onLogoutSuccess(HttpServletRequest, HttpServletResponse, Authentication)
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication)
            throws IOException, ServletException {
        final String accessToken = request.getParameter("access_token");
        final OAuth2AccessToken oAuth2AccessToken;
        final String cancelled_access_token;

        response.setContentType("application/json;charset=UTF-8");

        if (StringUtils.isNotBlank(accessToken)
                && null != (oAuth2AccessToken = tokenStore.readAccessToken(accessToken))
                && null != (cancelled_access_token = oAuth2AccessToken.getValue())) {
            final OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();

            tokenStore.removeAccessToken(oAuth2AccessToken);
            tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
            tokenStore.removeRefreshToken(oAuth2RefreshToken);

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
