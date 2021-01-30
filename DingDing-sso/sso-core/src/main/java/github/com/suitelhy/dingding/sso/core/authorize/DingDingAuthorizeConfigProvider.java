package github.com.suitelhy.dingding.sso.core.authorize;

import github.com.suitelhy.dingding.sso.core.properties.SecurityConstants;
import github.com.suitelhy.dingding.sso.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 核心模块的授权配置提供器
 *
 * @Description 安全模块涉及的url的授权配置在这里。
 * @Editor Suite
 */
@Component
@Order(Integer.MIN_VALUE)
public class DingDingAuthorizeConfigProvider
        implements AuthorizeConfigProvider {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATED_URL
                , SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE
                , SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_OPENID
                , SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*"
                , securityProperties.getBrowser().getSignInPage()
                , securityProperties.getBrowser().getSignUpUrl()
                , securityProperties.getBrowser().getSession().getSessionInvalidUrl()
        ).permitAll();

        if (StringUtils.isNotBlank(securityProperties.getBrowser().getSignOutUrl())) {
            config.antMatchers(securityProperties.getBrowser().getSignOutUrl()).permitAll();
        }
        return false;
    }

}
