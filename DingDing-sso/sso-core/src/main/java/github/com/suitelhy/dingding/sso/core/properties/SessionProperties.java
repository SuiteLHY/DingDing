package github.com.suitelhy.dingding.sso.core.properties;

/**
 * Session管理配置项
 * 
 * @author zhailiang
 * @Editor Suite
 *
 */
public class SessionProperties {
	
	/**
	 * 同一个用户在系统中的最大session数
	 *
	 * @Description 默认1
	 */
	private int maximumSessions = 1;

	/**
	 * 达到最大session时是否阻止新的登录请求
	 *
	 * @Description 默认为 false, 不阻止, 新的登录会使老的登录失效掉.
	 */
	private boolean maxSessionsPreventsLogin;

	/**
	 * session失效时跳转的地址
	 */
	private String sessionInvalidUrl = SecurityConstants.DEFAULT_SESSION_INVALID_URL;

	//===== Getter and Setter =====//

	public int getMaximumSessions() {
		return maximumSessions;
	}

	public void setMaximumSessions(int maximumSessions) {
		this.maximumSessions = maximumSessions;
	}

	public boolean isMaxSessionsPreventsLogin() {
		return maxSessionsPreventsLogin;
	}

	public void setMaxSessionsPreventsLogin(boolean maxSessionsPreventsLogin) {
		this.maxSessionsPreventsLogin = maxSessionsPreventsLogin;
	}

	public String getSessionInvalidUrl() {
		return sessionInvalidUrl;
	}

	public void setSessionInvalidUrl(String sessionInvalidUrl) {
		this.sessionInvalidUrl = sessionInvalidUrl;
	}
	
}
