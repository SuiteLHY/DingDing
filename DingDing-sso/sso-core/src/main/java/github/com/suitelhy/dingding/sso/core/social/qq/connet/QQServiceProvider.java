package github.com.suitelhy.dingding.sso.core.social.qq.connet;

import github.com.suitelhy.dingding.sso.core.social.qq.api.QQ;
import github.com.suitelhy.dingding.sso.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * @author zhailiang
 */
public class QQServiceProvider
        extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;

    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    public QQServiceProvider(String appId, String appSecret) {
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.social.oauth2.AbstractOAuth2ServiceProvider#getApi(java.
     * lang.String)
     */
    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }

}
