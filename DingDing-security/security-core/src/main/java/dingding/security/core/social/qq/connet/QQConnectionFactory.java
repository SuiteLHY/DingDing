package dingding.security.core.social.qq.connet;

import dingding.security.core.social.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * 针对QQ的 ConnectionFactory
 *
 * @author zhailiang
 */
public class QQConnectionFactory
        extends OAuth2ConnectionFactory<QQ> {

    /**
     * Constructor
     *
     * @param providerId
     * @param appId
     * @param appSecret
     */
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }

}
