package github.com.suitelhy.dingding.log.service.client.infrastructure.config.security.sso;

import github.com.suitelhy.dingding.core.infrastructure.web.vo.HTTP;
import github.com.suitelhy.dingding.log.service.client.infrastructure.web.security.DingDingAccessSecurityMetadataSource;
import github.com.suitelhy.dingding.log.service.client.web.security.DingDingLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

/**
 * 资源服务器 - 安全配置
 *
 * @Reference
 * {@link <a href="https://www.shuzhiduo.com/A/qVdeW1wrJP/">[权限管理系统篇] (五)-Spring security（授权过程分析）</a>}
 * {@link <a href="https://github.com/ygsama/ipa/blob/master/oauth2-server/src/main/java/io/github/ygsama/oauth2server/config/LoginSecurityInterceptor.java">ipa/LoginSecurityInterceptor.java at master · ygsama/ipa</a>}
 *
 * @author Suite
 */
@Configuration
@EnableWebSecurity
/*@EnableGlobalMethodSecurity(prePostEnabled = true)*/
@Order(100)
public class WebSecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("dingDingFilterSecurityInterceptor")
    private FilterSecurityInterceptor filterSecurityInterceptor;

    @Autowired
    @Qualifier("dingDingAnonymousAuthenticationFilter")
    private AnonymousAuthenticationFilter anonymousAuthenticationFilter;

    @Autowired
    private DingDingLogoutSuccessHandler logoutSuccessHandler;

    @Override
    public AuthenticationManager authenticationManager() {
        OAuth2AuthenticationManager authManager = new OAuth2AuthenticationManager();
        return authManager;
    }

    @Override
    protected void configure(HttpSecurity http)
            throws Exception {
        http
                // 认证请求
                .authorizeRequests()
                //===
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                //=== 权限认证
                .anyRequest()
                .authenticated()
                //===== 退出登录
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")/* 从 Cookie 中删除 Session ID */
                .invalidateHttpSession(true)
                .logoutSuccessHandler(logoutSuccessHandler);

        http
                //=== 自定义权限 Filter
                .addFilterAt(this.filterSecurityInterceptor, FilterSecurityInterceptor.class)
                //=== 自定义匿名认证 Filter
                /**
                 * @see DingDingAccessSecurityMetadataSource
                 */
                .addFilterBefore(this.anonymousAuthenticationFilter, FilterSecurityInterceptor.class);

        http
                // (跨域)...
                .cors()
                .and()
                .csrf().disable()/* 关闭 CSRF 跨域保护 (临时策略) */;
    }

    /**
     * Spring Security ...
     *
     * @Description Web 策略配置.
     * 由于 Spring Security 2.x 框架的一些问题, 此处配置不生效; 具体配置移步 {@link DingDingAccessSecurityMetadataSource}.
     *
     * @Reference
     * {@link <a href="https://stackoverflow.com/questions/21696592/disable-spring-security-for-options-http-method">java-为选项Http方法禁用Spring Security-代码日志</a>}
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(HTTP.MethodVo.OPTIONS.httpMethod, "/**");
    }

}
