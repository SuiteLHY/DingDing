package github.com.suitelhy.dingding.sso.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 资源服务器配置
 */
@Configuration
@EnableResourceServer
public class ResourceConfig
        extends ResourceServerConfigurerAdapter {

    /**
     * JWT 访问 token 转换器
     *
     * @Description 基于 JWT 的 access_token 转换器.
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 签名用的密钥; 资源服务器需要配置此选项方能解密 JWT 的 token
        converter.setSigningKey("dingding");
        return converter;
    }

    /**
     * JWT 的 token 存储对象
     *
     * @Description 一个具有解码 JWT 并验证其签名的唯一能力的 JwkTokenStore.
     * @return
     */
    @Bean
    public TokenStore jwtTokenStore() {
        JwtTokenStore tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
        return tokenStore;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * HTTP 配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)/* 设置创建session策略 */
                .and()
                    .authorizeRequests()
                    .anyRequest().authenticated()/* 所有请求必须认证 */;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .resourceId("client2")/* 设置资源服务器的 ID */
                .tokenStore(jwtTokenStore());
    }

}
