package github.com.suitelhy.dingding.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
 */
@Configuration
@EnableWebSecurity
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
    @Qualifier("ssoUserDetailsService"/*"dingDingUserDetailsService"*/)
    private UserDetailsService userDetailsService;

    /**
     * AuthenticationManager
     *
     * @return
     * @throws Exception
     * @Description <a href="https://github.com/jgrandja/spring-security-oauth-2-4-migrate/blob/master/auth-server/src/main/java/org/springframework/security/oauth/samples/config/SecurityConfig.java">
     * ->     spring-security-oauth-2-4-migrate/SecurityConfig.java at master · jgrandja/spring-security-oauth-2-4-migrate</a>
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()
            throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 加密器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
		/*auth.inMemoryAuthentication()
				.withUser("user")
				.password(passwordEncoder().encode("123456"))
				.authorities(Collections.emptyList());*/
    }

    /**
     * 认证授权服务器访问规则
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http)
            throws Exception {
        http/*.formLogin()*/.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()/* 所有请求都需要认证 */
                .and()
                .csrf().disable()/* 关跨域保护 (临时策略) */;
    }

}
