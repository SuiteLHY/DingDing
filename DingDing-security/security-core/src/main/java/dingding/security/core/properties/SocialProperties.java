package dingding.security.core.properties;

/**
 * 社交登录配置项
 *
 * @author zhailiang
 * @Editor Suite
 *
 */
public class SocialProperties {
	
	/**
	 * 社交登录功能拦截的url
	 */
	private String filterProcessesUrl = "/auth";

	private QQProperties qq = new QQProperties();
	
	private WeiXinProperties weiXin = new WeiXinProperties();

	//===== Getter and Setter =====//

	public QQProperties getQq() {
		return qq;
	}

	public void setQq(QQProperties qq) {
		this.qq = qq;
	}

	public String getFilterProcessesUrl() {
		return filterProcessesUrl;
	}

	public void setFilterProcessesUrl(String filterProcessesUrl) {
		this.filterProcessesUrl = filterProcessesUrl;
	}

	public WeiXinProperties getWeiXin() {
		return weiXin;
	}

	public void setWeiXin(WeiXinProperties weiXin) {
		this.weiXin = weiXin;
	}
	
}
