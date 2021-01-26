package github.com.suitelhy.dingding.sso.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 安全配置
 *
 * @Description 基于 Spring Security 的安全配置整合.
 *
 * @author zhailiang
 *
 */
@ConfigurationProperties(prefix = "dingding.security")
public class SecurityProperties {
	
	/**
	 * 浏览器环境配置
	 */
	private BrowserProperties browser = new BrowserProperties();

	/**
	 * 验证码配置
	 */
	private ValidateCodeProperties code = new ValidateCodeProperties();

	/**
	 * 社交登录配置
	 */
	private SocialProperties social = new SocialProperties();

	/**
	 * OAuth2认证服务器配置
	 */
	private OAuth2Properties oauth2 = new OAuth2Properties();

	//===== Getter and Setter =====//

	public BrowserProperties getBrowser() {
		return browser;
	}

	public void setBrowser(BrowserProperties browser) {
		this.browser = browser;
	}

	public ValidateCodeProperties getCode() {
		return code;
	}

	public void setCode(ValidateCodeProperties code) {
		this.code = code;
	}

	public SocialProperties getSocial() {
		return social;
	}

	public void setSocial(SocialProperties social) {
		this.social = social;
	}

	public OAuth2Properties getOauth2() {
		return oauth2;
	}

	public void setOauth2(OAuth2Properties oauth2) {
		this.oauth2 = oauth2;
	}
	
}

