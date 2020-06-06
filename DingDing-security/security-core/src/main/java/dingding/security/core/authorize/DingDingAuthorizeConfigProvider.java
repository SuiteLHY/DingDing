package dingding.security.core.authorize;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import dingding.security.core.properties.SecurityConstants;
import dingding.security.core.properties.SecurityProperties;

/**
 * 核心模块的授权配置提供器
 *
 * @Description 安全模块涉及的url的授权配置在这里。
 * 
 * @author zhailiang
 * @Editor Suite
 *
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
