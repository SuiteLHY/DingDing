package github.com.suitelhy.dingding.sso.core.authentication;

import github.com.suitelhy.dingding.sso.core.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 表单登录配置
 * 
 * @author zhailiang
 *
 */
@Component
public class FormAuthenticationConfig {

	@Autowired
	protected AuthenticationSuccessHandler dingdingAuthenticationSuccessHandler;
	
	@Autowired
	protected AuthenticationFailureHandler dingdingAuthenticationFailureHandler;
	
	public void configure(HttpSecurity http)
			throws Exception {
		http.formLogin()
			.loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATED_URL)
			.loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
			.successHandler(dingdingAuthenticationSuccessHandler)
			.failureHandler(dingdingAuthenticationFailureHandler);
	}
	
}
