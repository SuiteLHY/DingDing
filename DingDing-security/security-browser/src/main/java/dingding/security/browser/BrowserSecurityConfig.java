package dingding.security.browser;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import dingding.security.core.authentication.FormAuthenticationConfig;
import dingding.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import dingding.security.core.authorize.AuthorizeConfigManager;
import dingding.security.core.properties.SecurityProperties;
import dingding.security.core.validate.code.ValidateCodeSecurityConfig;

/**
 * 浏览器环境下安全配置主类
 *
 * @author zhailiang
 * @Editor Suite
 */
@Configuration
public class BrowserSecurityConfig
        extends WebSecurityConfigurerAdapter {

    // 基础安全配置
    @Autowired
    private SecurityProperties securityProperties;

    // 数据源
    @Autowired
    private DataSource dataSource;

    // 用户信息业务
    @Autowired
    private UserDetailsService userDetailsService;

    // 短信认证安全配置
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    // 校验码 - 基础安全配置
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    // SpringSocial - (第三方社交服务商) 配置
    @Autowired
    private SpringSocialConfigurer dingDingSocialSecurityConfig;

    // Session 过期处理策略
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    // Session 失效处理策略
    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    // 登出成功处理器
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    // 授权配置管理器
    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    // 表单认证配置
    @Autowired
    private FormAuthenticationConfig formAuthenticationConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        formAuthenticationConfig.configure(http);

        http
                //=== 验证码校验配置 ===//
                .apply(validateCodeSecurityConfig)
                .and()
                //=== 短信验证码校验配置 ===//
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                //=== 第三方社交软件关联校验配置 ===//
                .apply(dingDingSocialSecurityConfig)
                .and()
                //=== "记住我"功能配置 ===//
                //-> 如果想在'记住我'登录时记录日志，可以注册一个InteractiveAuthenticationSuccessEvent事件的监听器
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()
                //=== Session 配置 ===//
                .sessionManagement()
                // Session 过期处理策略
                .invalidSessionStrategy(invalidSessionStrategy)
                // Session 最大存在数量
                .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                // Session - 是否在达到最大存在数量后阻止新的 Session 产生
                .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
                //===== 登出配置 =====//
                .logout()
                .logoutUrl("/signOut")
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .and()
                //===== 其他配置 =====//
                .csrf().disable();

        authorizeConfigManager.config(http.authorizeRequests());
    }

    /**
     * 记住我功能的token存取器配置
     *
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
//		tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

}
