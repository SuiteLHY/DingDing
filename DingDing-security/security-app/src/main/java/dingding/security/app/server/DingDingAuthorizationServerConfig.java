/**
 *
 */
package dingding.security.app.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import dingding.security.core.properties.OAuth2ClientProperties;
import dingding.security.core.properties.SecurityProperties;

/**
 * 认证服务器配置
 *
 * @author zhailiang
 * @Editor Suite
 *
 */
@Configuration
@EnableAuthorizationServer
public class DingDingAuthorizationServerConfig
        extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Token 存储配置
    @Autowired
    private TokenStore tokenStore;

    // JWT 令牌转换器
    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    // JWT 增强器
    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    // Spring Security 安全配置项
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 认证及 Token 配置
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

        if (null != jwtAccessTokenConverter && null != jwtTokenEnhancer) {
            //--- 组织 JWT 的令牌转换器和增强器
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancers);
            endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
        }

    }

    /**
     * TokenKey 的访问权限表达式配置
     *
     * @param security
     * @throws Exception
     */
    public void configure(AuthorizationServerSecurityConfigurer security)
            throws Exception {
        security.tokenKeyAccess("permitAll()");
    }

    /**
     * 客户端配置
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        // (业务场景不需要做第三方接入的拓展, 直接使用内存存储模式)
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
            for (OAuth2ClientProperties client : securityProperties.getOauth2().getClients()) {
                builder.withClient(client.getClientId())
                        .secret(client.getClientSecret())
                        // 设置支持的授权模式
                        .authorizedGrantTypes("refresh_token"/* 刷新令牌 */
                                , "authorization_code"/* 授权码 */
                                , "password"/* 密码 */)
                        // Token 有效时长
                        .accessTokenValiditySeconds(client.getAccessTokenValidateSeconds())
                        // Token 有效性刷新时间间隔
                        .refreshTokenValiditySeconds(2666000)
                        // 权限控制参数
                        //-> 注意: 请求中出现的(如果有) "scope" 参数必须在此处设置的范围内.
                        .scopes("all");
            }
        }
    }

}
