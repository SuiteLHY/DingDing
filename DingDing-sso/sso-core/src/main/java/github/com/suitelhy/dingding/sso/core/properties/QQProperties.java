package github.com.suitelhy.dingding.sso.core.properties;

/**
 * QQ登录配置项
 *
 * @author zhailiang
 *
 */
public class QQProperties {
	
	/**
	 * Application id.
	 */
	private String appId;

	/**
	 * Application secret.
	 */
	private String appSecret;

	/**
	 * 第三方id，用来决定发起第三方登录的url，默认是 qq。
	 */
	private String providerId = "qq";

	//===== Getter and Setter =====//

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return this.appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
}
