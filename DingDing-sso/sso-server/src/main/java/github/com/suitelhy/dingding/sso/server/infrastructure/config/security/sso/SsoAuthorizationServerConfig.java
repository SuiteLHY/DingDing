package github.com.suitelhy.dingding.sso.server.infrastructure.config.security.sso;

import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 认证服务器配置
 *
 * @Description · 排坑:
 * -> {@link <a href="https://github.com/spring-projects/spring-security-oauth/issues/993">ResourceServerProperties DEFAULT filterOrder is not 0. #993</a>}
 * -> {@link <a href="https://github.com/spring-projects/spring-boot/issues/5072">non-sensitive actuator endpoints require full authentication when @EnableResourceServer is used (oauth2) #5072</a>}
 * -> {@Description 最后一个雷: 通过 {@link Order} 设置的优先级, 如果不恰当, 会导致必要的过滤器不被调用.}
 * -> {@link <solution><a href="https://hacpai.com/article/1579503779901#%E7%B3%BB%E5%88%97%E6%96%87%E7%AB%A0">Spring Security Oauth2 从零到一完整实践（六）踩坑记录 - 黑客派</a></solution>}
 * @Editor Suite
 */
@Configuration
// (认证授权服务器配置注解)
@EnableAuthorizationServer
@Order(/*Ordered.HIGHEST_PRECEDENCE*//*0*/3)
public class SsoAuthorizationServerConfig
        extends AuthorizationServerConfigurerAdapter {

	/*@Autowired
	@Qualifier("jwtAccessTokenConverter")
	private AccessTokenConverter accessTokenConverter;*/

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientDetailsService clientDetailsService;

	/*@Autowired
	private PasswordEncoder passwordEncoder;*/

    @Autowired
    /*@Qualifier("jwtTokenStore")*/
    private TokenStore tokenStore;

    @Autowired
    @Qualifier(/*"ssoUserDetailsService"*/"dingDingUserDetailsService")
    private UserDetailsService userDetailsService;

    @Bean
    public ApprovalStore approvalStore() {
        return new InMemoryApprovalStore();
    }

    /**
     * 客户端服务配置
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        clients.inMemory()
                //=== 客户端1 ===//
                .withClient("dingding1")
                .authorizedGrantTypes("authorization_code"/* 授权码模式 */
                        , "password"/* 密码模式 */
                        , "refresh_token"/* 刷新令牌; 不是授权模式, 但是可以使用它来刷新 access_token (在它的有效期内) */) // (授权方式)
                .redirectUris("https://www.baidu.com"
                        , "http://127.0.0.1:8080/client1/index.html")
                .scopes("all") // 允许的授权范围
                /*.secret(passwordEncoder.encode("dingding_secret1"))*/
                .secret(Security.PasswordEncoderVo.BCrypt.encoder.encode("dingding_secret1"))// 客户端密码 (应该是加密后的密文)
                .accessTokenValiditySeconds(60 * 60)
                .refreshTokenValiditySeconds(3600 * 24 * 5)
                //=== 客户端2 ===//
                .and()
                .withClient("dingding2")
                .authorizedGrantTypes("authorization_code"
                        , "password"
                        , "refresh_token") // (授权方式)
                .redirectUris("https://www.baidu.com"
                        , "http://127.0.0.1:8060/client2/index.html")
                .scopes("all")
                .secret(Security.PasswordEncoderVo.BCrypt.encoder.encode("dingding_secret2"))
                //=== 客户端3 - 用户基础 CRUD 服务 ===//
                .and()
                .withClient("dingding_user")
                .authorizedGrantTypes("authorization_code"
                        , "password"
                        , "refresh_token") // (授权方式)
                .redirectUris("https://www.baidu.com"
                        , "http://127.0.0.1:8060/client2/index.html")
                .scopes("all")
                .secret(Security.PasswordEncoderVo.BCrypt.encoder.encode("dingding_user_secrect1"));
    }

    /**
     * 端点配置
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		/*endpoints
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);*/
        endpoints
                /*.accessTokenConverter(this.accessTokenConverter) // 设置令牌转换器*/
                .authenticationManager(this.authenticationManager)
                .tokenStore(this.tokenStore)// 设置令牌存储
                /*.userApprovalHandler(userApprovalHandler())*/
                .userDetailsService(this.userDetailsService);
    }

    /**
     * 认证服务器安全配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .allowFormAuthenticationForClients()
                .checkTokenAccess("permitAll()")// <a href="https://stackoverflow.com/questions/26750999/spring-security-oauth2-check-token-endpoint">Spring Security OAuth2 check_token端点</a>
                .tokenKeyAccess("isAuthenticated()")/* 设置: 获取令牌的校验密钥时, 需要经过身份认证 */;
    }

}

/**
 * Token 配置
 *
 * @author Suite
 * @Description 弃用 JWT.
 * -> 当前情景不适合 JWT, 继续使用 Session + Cookie 的 Token 认证方式.
 * -> {@link <a href="https://juejin.im/entry/59748def518825592c4f9ac0">不要用 JWT 来做 Web 应用的会话管理 - 后端 - 掘金</a>}
 * @Reference -> {@link <a href="https://juejin.im/post/5a45aa44f265da43133d770e">Spring Security TokenStore实现3+1详解 - 掘金</a>}
 */
@Configuration
class TokenStoreAccess {

	/*@Value("${dingding.security.signing-key}")
	private String signingKey;*/

//	/**
//	 * JWT 的 token 存储对象
//	 *
//	 * @Description 一个具有解码 JWT 并验证其签名的唯一能力的 JwkTokenStore.
//	 * @return
//	 */
//	@Bean
//	@Primary
//	public TokenStore jwtTokenStore() {
//		/*return new JwtTokenStore(jwtAccessTokenConverter());*/
//		JwtTokenStore tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
//		/*tokenStore.setApprovalStore(approvalStore());*/
//		return tokenStore;
//	}
//
//	/**
//	 * JWT 访问 token 转换器
//	 *
//	 * @Description 基于 JWT 的 access_token 转换器; 在JwtTokenStore实例中使用它. 用于发行 JWT.
//	 * @return
//	 */
//	@Bean
//	@Primary
//	public JwtAccessTokenConverter jwtAccessTokenConverter() {
//		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//		// 签名用的密钥; 资源服务器需要配置此选项方能解密 JWT 的 token
//		converter.setSigningKey(signingKey);
//		return converter;
//	}

    /**
     * @Description Redis 连接工厂.
     */
    @Autowired
    public RedisConnectionFactory redisConnectionFactory;

    @Bean
    @Primary
    public TokenStore tokenStore() {
        // 使用 redis 存储 token
        final RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);

        // 设置 redis token 存储中的前缀
        redisTokenStore.setPrefix("auth-token:");

        return redisTokenStore;
        /*return new InMemoryTokenStore();*/
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();

        // 配置token存储
        tokenServices.setTokenStore(this.tokenStore());
        // 开启支持refresh_token，此处如果之前没有配置，启动服务后再配置重启服务，可能会导致不返回token的问题，解决方式：清除redis对应token存储
        tokenServices.setSupportRefreshToken(true);
        // 复用refresh_token
        tokenServices.setReuseRefreshToken(true);
        // token有效期，设置12小时
        tokenServices.setAccessTokenValiditySeconds(2 * 60 * 60);
        // refresh_token有效期，设置五天
        tokenServices.setRefreshTokenValiditySeconds(5 * 24 * 60 * 60);

        return tokenServices;
    }

}
