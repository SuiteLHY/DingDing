package github.com.suitelhy.dingding.sso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

/**
 * Spring Security 配置
 *
 * @Editor Suite
 *
 */
@Configuration
@EnableOAuth2Sso  // SSo自动配置引用
/*@EnableWebSecurity*/
// 排坑: <a href="https://github.com/spring-projects/spring-security-oauth/issues/993">ResourceServerProperties DEFAULT filterOrder is not 0. #993</a>
//-> , <a href="https://github.com/spring-projects/spring-boot/issues/5072">
//-> 		non-sensitive actuator endpoints require full authentication when @EnableResourceServer is used (oauth2) #5072</a>
//-> , <description>最后一个雷: 通过@Order设置的优先级，如果不恰当，会导致必要的过滤器不被调用.</description>
//-> <solution><a href="https://hacpai.com/article/1579503779901#%E7%B3%BB%E5%88%97%E6%96%87%E7%AB%A0">
//-> 		Spring Security Oauth2 从零到一完整实践（六）踩坑记录 - 黑客派</a></solution>
@Order(/*Ordered.HIGHEST_PRECEDENCE*/100)
public class SsoSecurityConfig
		extends WebSecurityConfigurerAdapter {

	@Autowired
	public OAuth2ClientProperties oAuth2ClientProperties;

	/**
	 *
	 * @param http
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http)
			throws Exception {
		System.err.println(oAuth2ClientProperties.getRegistration());

		http/*.formLogin()*//*.httpBasic()*/
				/*.and()*/.antMatcher("/**")
				.authorizeRequests()
					.antMatchers("/"
                            , "/error*"
                            , "/login*").permitAll()
					.anyRequest().authenticated()
                .and().csrf().disable();
	}

}
