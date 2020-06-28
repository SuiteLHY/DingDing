package github.com.suitelhy.dingding.sso.server.infrastructure.config.security.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 认证服务器配置
 *
 * @Description
 *-> 排坑:
 *->  	{@link <a href="https://github.com/spring-projects/spring-security-oauth/issues/993">ResourceServerProperties DEFAULT filterOrder is not 0. #993</a>}
 *->  	{@link <a href="https://github.com/spring-projects/spring-boot/issues/5072">non-sensitive actuator endpoints require full authentication when @EnableResourceServer is used (oauth2) #5072</a>}
 *->  	{@link <description>最后一个雷: 通过 {@link Order} 设置的优先级, 如果不恰当, 会导致必要的过滤器不被调用.</description>}
 *->  	{@link <solution><a href="https://hacpai.com/article/1579503779901#%E7%B3%BB%E5%88%97%E6%96%87%E7%AB%A0">Spring Security Oauth2 从零到一完整实践（六）踩坑记录 - 黑客派</a></solution>}
 *
 * @Editor Suite
 */
@Configuration
// (认证授权服务器配置注解)
@EnableAuthorizationServer
@Order(/*Ordered.HIGHEST_PRECEDENCE*//*0*/3)
public class SsoAuthorizationServerConfig
		extends AuthorizationServerConfigurerAdapter {

	@Autowired
	@Qualifier("jwtAccessTokenConverter")
	private AccessTokenConverter accessTokenConverter;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("jwtTokenStore")
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
				.secret(passwordEncoder.encode("dingding_secret1"))// 客户端密码 (应该是加密后的密文)
				.accessTokenValiditySeconds(600)
				.refreshTokenValiditySeconds(6000)
				.and()
				//=== 客户端2 ===//
				.withClient("dingding2")
				.authorizedGrantTypes("authorization_code"
						, "password"
						, "refresh_token") // (授权方式)
				.redirectUris("https://www.baidu.com"
						, "http://127.0.0.1:8060/client2/index.html")
				.scopes("all")
				.secret(passwordEncoder.encode("dingding_secret2"));
	}

	/**
	 * 端点配置
	 *
	 * @param endpoints
	 * @throws Exception
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		endpoints
				.accessTokenConverter(this.accessTokenConverter) // 设置令牌转换器
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
	public void configure(AuthorizationServerSecurityConfigurer security)
			throws Exception {
		security.allowFormAuthenticationForClients()
				.checkTokenAccess("permitAll()")// <a href="https://stackoverflow.com/questions/26750999/spring-security-oauth2-check-token-endpoint">Spring Security OAuth2 check_token端点</a>
				.tokenKeyAccess("isAuthenticated()")/* 设置: 获取令牌的校验密钥时, 需要经过身份认证 */;
	}

}

/**
 * Token 存储配置
 *
 * @Description JWT.
 * @author Suite
 */
@Configuration
class TokenStoreAccess {

	@Value("${dingding.security.signing-key}")
	private String signingKey;

	/**
	 * JWT 的 token 存储对象
	 *
	 * @Description 一个具有解码 JWT 并验证其签名的唯一能力的 JwkTokenStore.
	 * @return
	 */
	@Bean
	@Primary
	public TokenStore jwtTokenStore() {
		/*return new JwtTokenStore(jwtAccessTokenConverter());*/
		JwtTokenStore tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
		/*tokenStore.setApprovalStore(approvalStore());*/
		return tokenStore;
	}

	/**
	 * JWT 访问 token 转换器
	 *
	 * @Description 基于 JWT 的 access_token 转换器; 在JwtTokenStore实例中使用它. 用于发行 JWT.
	 * @return
	 */
	@Bean
	@Primary
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		// 签名用的密钥; 资源服务器需要配置此选项方能解密 JWT 的 token
		converter.setSigningKey(signingKey);
		return converter;
	}

}
