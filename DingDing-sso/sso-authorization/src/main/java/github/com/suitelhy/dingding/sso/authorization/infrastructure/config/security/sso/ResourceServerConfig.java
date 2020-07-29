package github.com.suitelhy.dingding.sso.authorization.infrastructure.config.security.sso;

import github.com.suitelhy.dingding.sso.authorization.domain.service.impl.DingDingUserInfoTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * 资源服务器配置
 *
 * @author Suite
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig
        extends ResourceServerConfigurerAdapter {

    @Value("${dingding.security.client-id}")
    private String resourceId;

    @Autowired
    @Qualifier("userInfoTokenServices")
    private ResourceServerTokenServices tokenServices;

    /**
     * 安全资源的访问规则配置
     *
     * @param http
     *
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http)
            throws Exception {
        http.authorizeRequests()
                /*.antMatchers("/messages/**"
                        , "user/**")
                        .access("#oauth2.hasScope('all')")*/
                .anyRequest()
                    .authenticated();
    }

    /**
     * 资源服务器的属性配置
     *
     * @param resources
     *
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .resourceId(this.resourceId)
                .tokenServices(this.tokenServices);
    }

}

/**
 * Token 配置
 *
 * @Description 弃用 JWT.
 *-> 当前情景不适合 JWT, 继续使用 Session + Cookie 的 Token 认证方式.
 *-> {@link <a href="https://juejin.im/entry/59748def518825592c4f9ac0">不要用 JWT 来做 Web 应用的会话管理 - 后端 - 掘金</a>}
 */
@Configuration
class TokenStoreAccess {

//    @Value("${dingding.security.signing-key}")
//    private String signingKey;
//
//    @Bean
//    @Primary
//    public TokenStore jwtTokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
//
//    @Bean
//    @Primary
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey(this.signingKey);
//        /*// 验证用的密钥
//        converter.setVerifierKey(this.signingKey);*/
//        return converter;
//    }

    @Value("${dingding.security.client-id}")
    private String clientId;

    @Value("${dingding.security.client-secret}")
    private String clientSecret;

    @Value("${dingding.sso.auth-server.endpoint.check_token}")
    private String checkTokenEndpointUrl;

    @Bean
    @Primary
    public DingDingUserInfoTokenServices userInfoTokenServices() {
        DingDingUserInfoTokenServices tokenServices = new DingDingUserInfoTokenServices(checkTokenEndpointUrl, clientId);
        return tokenServices;
    }

}
