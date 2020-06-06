package dingding.security.core.properties;

/**
 * OAuth2 相关配置
 *
 * @author zhailiang
 *
 */
public class OAuth2Properties {
	
	/**
	 * JWT 签名密钥
	 *
	 * @Description 使用 JWT 时为 Token 签名的秘钥.
	 */
	private String jwtSigningKey = "dingding";

	/**
	 * 客户端配置
	 *
	 * @Description 配置项集合.
	 */
	private OAuth2ClientProperties[] clients = {};

	//===== Getter and Setter =====//

	public OAuth2ClientProperties[] getClients() {
		return clients;
	}

	public void setClients(OAuth2ClientProperties[] clients) {
		this.clients = clients;
	}

	public String getJwtSigningKey() {
		return jwtSigningKey;
	}

	public void setJwtSigningKey(String jwtSigningKey) {
		this.jwtSigningKey = jwtSigningKey;
	}
	
}
