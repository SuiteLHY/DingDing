package github.com.suitelhy.webchat.infrastructure.web.config.security;

import github.com.suitelhy.webchat.web.security.WebchatAuthenticationSuccessHandler;
import github.com.suitelhy.webchat.web.security.WebchatAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;

/**
 * Spring Security 配置
 *
 * @Disuse
 *-> <Guide><a href="https://spring.io/guides/gs/authenticating-ldap/">
 *->     Getting Started · Authenticating a User with LDAP</a></Guide>
 *-> , <Docs><a href="https://www.docs4dev.com/docs/zh/spring-security/4.2.10.RELEASE/reference/ldap.html">
 *->     Spring Security 4.2.10.RELEASE 中文文档 - 29. LDAP 身份验证 | Docs4dev</a></Docs>
 *
 * @Reference
 *-> <Guide><a href="https://spring.io/guides/gs/securing-web/">
 *->     Getting Started · Securing a Web Application</a></Guide>
 *-> , <Blog>【☆】<a href="https://woodwhales.cn/2019/04/12/026/">
 *->     SpringBoot + Spring Security 学习笔记（一）自定义基本使用及个性化登录配置 | woodwhale's blog</a></Blog>
 *-> , <Blog><a href="https://semlinker.com/spring-security-quickstart/">
 *->     Spring Boot 集成 Spring Security | 全栈修仙之路</a></Blog>
 *-> , <Community>【☆】<a href="https://juejin.im/post/5da40403f265da5b6723eeeb">
 *->     Spring Security 自定义登录认证（二） - 掘金</a></Community>
 *-> , <Docs><a href="https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#hello-web-security-java-configuration">
 *->     Spring Security Reference</a></Docs>
 *-> , <Docs><a href="https://www.springcloud.cc/spring-security.html">
 *->     Spring Security 中文文档 参考手册 中文版</a></Docs>
 *
 */
/**
 * TIPS: 前端资源请求记得制定并实现特定的过滤策略. 不然资源请求会报<code>404</code>.
 * @Reference
 *-> <a href="https://stackoverflow.com/questions/48248832/stylesheet-not-loaded-because-of-mime-type">
 *->     html - Stylesheet not loaded because of MIME-type - Stack Overflow</a>
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义 UserDetailsService 实例
     * @Description 项目 Domain 层提供 <interface>UserDetailsService</interface> 定制化实现
     *-> , 在此处注入该实现.
     */
    @Autowired
    @Qualifier("webchatUserDetailsService")
    private UserDetailsService userDetailsService;

    /**
     * 验证失败 -> 自定义操作
     */
    @Autowired
    private WebchatAuthenticationFailureHandler failureHandler;

    /**
     * 验证成功 -> 自定义操作
     */
    @Autowired
    private WebchatAuthenticationSuccessHandler successHandler;

    /**
     * 配置 -> 端点 (Endpoint) 过滤规则
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http
                .authorizeRequests() // 定义哪些 URL 需要被保护、哪些不需要被保护
                    .antMatchers(this.getEndpointWithoutAuthentication()).permitAll() // 无需验证的 Endpoint
                    .anyRequest().authenticated() // 任何请求, 登录后可以访问
                .and()
                .formLogin() // 定义当需要提交表单进行用户登录时候, 所跳转到的登录页面
                    .loginPage("/user/login") // 自定义登录页面
                    .loginProcessingUrl("/user/login") // 自定义登录接口
//                    .defaultSuccessUrl("/user/login") // 登录成功之后, 默认跳转的页面
                    .successHandler(successHandler) // (前后端分离所需)
                    .failureHandler(failureHandler) // (前后端分离所需)
                    .permitAll()
                .and()
                .logout() // 登出...
                    .permitAll();*/

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http.antMatcher("/**").authorizeRequests();

        // 允许匿名访问的 Endpoint
        urlRegistry.antMatchers(this.getEndpointWithoutAuthentication()).permitAll();

        // 表单提交认证细节
        urlRegistry.and()
                .formLogin()
                .loginPage("/user/login") // 自定义登录页面
                .loginProcessingUrl("/user/login") // 自定义登录接口
                /*.successHandler(successHandler) // (前后端分离所需)
                .failureHandler(failureHandler) // (前后端分离所需)*/
                .permitAll();
        // 对 OPTIONS 请求放行
        //-> TIPS: 不清楚 OPTIONS 请求方式可查阅: <a href="https://www.runoob.com/http/http-methods.html">HTTP 请求方法 | 菜鸟教程</a>
        urlRegistry.antMatchers(HttpMethod.OPTIONS, "/**").denyAll();
        // 所有未指定放行的请求 <- 全都需要认证
        urlRegistry.anyRequest().authenticated();

        // 自动登录 - cookie储存方式
        urlRegistry.and().rememberMe();

        // 避免 <iframe></iframe> 的跨域问题
        //-> 详见: <a href="https://juejin.im/post/5cc4fc445188252e83434c32">iframe跨域的几种常用方法 - 掘金</a>
        urlRegistry.and().headers().frameOptions().disable();
    }

    /**
     * 配置 -> 无需验证即可访问的 Endpoint
     * @Description 自定义配置实现; 解耦.
     * @return
     */
    @NotNull
    private String[] getEndpointWithoutAuthentication() {
        return new String[]{
                //=== 登录页面 (必需, 避免无限校验) ===//
                "/user/login"
                //=== 前端静态资源 (应该做更细致地校验) ===//
                , "/static/img/**"
                , "/static/plugins/**/**"
                , "/static/source/**/*.*"
                //======//
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService) // 注入自定义 UserDetailsService 实例
                .passwordEncoder(this.getPasswordEncoder())/* 注入定制化 PasswordEncoder 实现 */;
    }

    /**
     * 定制化 <interface>PasswordEncoder</interface>(密码编码器)
     * @Description 设计目的: 解耦.
     * @return
     */
    @Bean
    protected PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
