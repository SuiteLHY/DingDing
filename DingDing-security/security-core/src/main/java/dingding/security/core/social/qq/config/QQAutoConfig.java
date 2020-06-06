package dingding.security.core.social.qq.config;

import dingding.security.core.properties.QQProperties;
import dingding.security.core.properties.SecurityProperties;
import dingding.security.core.social.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

import dingding.security.core.social.qq.connet.QQConnectionFactory;

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
