package github.com.suitelhy.dingding.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 认证服务器配置
 *
 * @author zhailiang
 * @Editor Suite
 */
@Configuration
// (认证授权服务器配置注解)
@EnableAuthorizationServer
// 排坑: <a href="https://github.com/spring-projects/spring-security-oauth/issues/993">ResourceServerProperties DEFAULT filterOrder is not 0. #993</a>
//-> , <a href="https://github.com/spring-projects/spring-boot/issues/5072">
//-> 		non-sensitive actuator endpoints require full authentication when @EnableResourceServer is used (oauth2) #5072</a>
//-> , <description>最后一个雷: 通过@Order设置的优先级，如果不恰当，会导致必要的过滤器不被调用.</description>
//-> <solution><a href="https://hacpai.com/article/1579503779901#%E7%B3%BB%E5%88%97%E6%96%87%E7%AB%A0">
//-> 		Spring Security Oauth2 从零到一完整实践（六）踩坑记录 - 黑客派</a></solution>
@Order(/*Ordered.HIGHEST_PRECEDENCE*//*0*/3)
public class SsoAuthorizationServerConfig
		extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("ssoUserDetailsService"/*"dingDingUserDetailsService"*/)
    private UserDetailsService userDetailsService;

	@Bean
	public ApprovalStore approvalStore() {
		return new InMemoryApprovalStore();
	}

	/**
	 * JWT 的 token 存储对象
	 *
	 * @Description 一个具有解码 JWT 并验证其签名的唯一能力的 JwkTokenStore.
	 * 		-> 用于发行 JWT.
	 * @return
	 */
	@Bean
	public TokenStore jwtTokenStore() {
		/*return new JwtTokenStore(jwtAccessTokenConverter());*/
		JwtTokenStore tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
		tokenStore.setApprovalStore(approvalStore());
		return tokenStore;
	}

	/**
	 * JWT 访问 token 转换器
	 *
	 * @Description 基于 JWT 的 access_token 转换器; 在JwtTokenStore实例中使用它. 用于发行 JWT.
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		// 签名用的密钥; 资源服务器需要配置此选项方能解密 JWT 的 token
		converter.setSigningKey("dingding");
		return converter;
	}

	@Bean
	public UserApprovalHandler userApprovalHandler() {
		ApprovalStoreUserApprovalHandler userApprovalHandler = new ApprovalStoreUserApprovalHandler();
		userApprovalHandler.setApprovalStore(approvalStore());
		userApprovalHandler.setClientDetailsService(this.clientDetailsService);
		userApprovalHandler.setRequestFactory(new DefaultOAuth2RequestFactory(this.clientDetailsService));
		return userApprovalHandler;
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
				.refreshTokenValiditySeconds(600)
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
				.accessTokenConverter(jwtAccessTokenConverter()) // 设置access_token转换器
				.authenticationManager(this.authenticationManager)
				.tokenStore(jwtTokenStore())// 设置 jwtToken 为 tokenStore
				/*.userApprovalHandler(userApprovalHandler())*/
                .userDetailsService(userDetailsService);
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
				.checkTokenAccess(/*"isAuthenticated()"*/"permitAll()")// <a href="https://stackoverflow.com/questions/26750999/spring-security-oauth2-check-token-endpoint">Spring Security OAuth2 check_token端点</a>
				.tokenKeyAccess("isAuthenticated()")/* 设置: 获取令牌的校验密钥时, 需要经过身份认证 */;
	}

}
