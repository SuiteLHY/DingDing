package github.com.suitelhy.dingding.sso.core.social.qq.config;

import github.com.suitelhy.dingding.sso.core.properties.QQProperties;
import github.com.suitelhy.dingding.sso.core.properties.SecurityProperties;
import github.com.suitelhy.dingding.sso.core.social.CurrentUserHolder;
import github.com.suitelhy.dingding.sso.core.social.qq.connet.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

/**
 *
 * @author zhailiang
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "dingding.security.social.qq", name = "app-id")
public class QQAutoConfig
		extends SocialConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;
	
	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
		configurer.addConnectionFactory(createConnectionFactory());
	}
	
	@Override
	public UserIdSource getUserIdSource() {
		return new CurrentUserHolder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter
	 * #createConnectionFactory()
	 */
	protected ConnectionFactory<?> createConnectionFactory() {
		QQProperties qqConfig = securityProperties.getSocial().getQq();
		return new QQConnectionFactory(qqConfig.getProviderId(), qqConfig.getAppId(), qqConfig.getAppSecret());
	}

}
