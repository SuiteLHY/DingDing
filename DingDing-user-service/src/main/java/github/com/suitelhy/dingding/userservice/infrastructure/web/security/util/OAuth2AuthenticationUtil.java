package github.com.suitelhy.dingding.userservice.infrastructure.web.security.util;

import github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.validation.constraints.NotNull;

/**
 * 基于 OAuth2 标准的[身份凭证处理工具]
 *
 * @see OAuth2AuthenticationInfo
 */
public final class OAuth2AuthenticationUtil {

    /**
     * @Design (单例模式 - 登记式)
     */
    private static class Factory {
        private static final OAuth2AuthenticationUtil SINGLETON = new OAuth2AuthenticationUtil();
    }

    private OAuth2AuthenticationUtil() {}

    public static @NotNull OAuth2AuthenticationUtil getInstance() {
        return Factory.SINGLETON;
    }

    /**
     * 获取[身份验证令牌信息 - 用户认证凭据 - 详细信息]
     *
     * @param oAuth2Authentication
     *
     * @return {@link OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails}
     */
    public OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails getDetails(OAuth2Authentication oAuth2Authentication)
            throws AuthenticationCredentialsNotFoundException, BadCredentialsException
    {
        if (null == oAuth2Authentication) {
            //-- 非法参数: [身份验证信息]
            throw new AuthenticationCredentialsNotFoundException("非法参数: 找不到[身份验证信息]");
        }
        if (!oAuth2Authentication.isAuthenticated()) {
            //-- 非法参数: 无效的[身份验证信息]
            throw new BadCredentialsException("非法参数: 无效的[身份验证信息]");
        }

        return OAuth2AuthenticationInfo.AbstractUserAuthentication.getDetails(oAuth2Authentication.getUserAuthentication());
    }

}
