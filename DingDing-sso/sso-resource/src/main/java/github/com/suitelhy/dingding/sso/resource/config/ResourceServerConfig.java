package github.com.suitelhy.dingding.sso.resource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 资源服务器配置
 *
 * @author Suite
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig
        extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "dingding";

    @Value("${dingding.security.signing-key}")
    private String signingKey;

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        /*System.err.println(this.signingKey);*/
        converter.setSigningKey(this.signingKey);
        /*// 验证用的密钥
        converter.setVerifierKey(this.signingKey);*/
        return converter;
    }

    @Bean
    public ResourceServerTokenServices tokenService() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(this.jwtTokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    /**
     * 安全资源的访问规则配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http)
            throws Exception {
        http.authorizeRequests()
                .antMatchers("/messages/**").access("#oauth2.hasScope('all')")
                .anyRequest().authenticated();
    }

    /**
     * 资源服务器的属性配置
     *
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources)
            throws Exception {
        resources
                .resourceId(RESOURCE_ID)
                .tokenServices(this.tokenService())
                .tokenStore(this.jwtTokenStore());
    }

}
