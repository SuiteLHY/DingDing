package github.com.suitelhy.dingding.sso.authorization.infrastructure.config.security.sso;

import github.com.suitelhy.dingding.sso.authorization.web.security.DingDingLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 资源服务器 - 安全配置
 *
 * @Reference
 *->  {@link <a href="https://www.shuzhiduo.com/A/qVdeW1wrJP/">[权限管理系统篇] (五)-Spring security（授权过程分析）</a>}
 *->  {@link <a href="https://github.com/ygsama/ipa/blob/master/oauth2-server/src/main/java/io/github/ygsama/oauth2server/config/LoginSecurityInterceptor.java">ipa/LoginSecurityInterceptor.java at master · ygsama/ipa</a>}
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
                        /*.addLogoutHandler(logoutHandler)*/
                        .logoutSuccessHandler(logoutSuccessHandler);

        http
                //=== 自定义权限 Filter
                .addFilterAt(this.filterSecurityInterceptor, FilterSecurityInterceptor.class);
    }

//    /**
//     * 自定义 FilterSecurityInterceptor ObjectPostProcessor
//     *
//     * @Description 自定义 FilterSecurityInterceptor ObjectPostProcessor 以替换默认配置实现动态权限管控.
//     *
//     * @Reference
//     *->  {@link <a href="https://juejin.im/post/5de0ed6ae51d4533007261d2#heading-2">Spring Security 实战干货：动态权限控制（下）实现 - 掘金</a>}
//     *
//     * @return ObjectPostProcessor
//     */
//    private ObjectPostProcessor<FilterSecurityInterceptor> filterSecurityInterceptorObjectPostProcessor() {
//        return new ObjectPostProcessor<FilterSecurityInterceptor>() {
//            @Override
//            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
//                object.setAccessDecisionManager(accessDecisionManager);
//                object.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
//
//                return object;
//            }
//        };
//    }

}