package github.com.suitelhy.dingding.sso.core.social;

import github.com.suitelhy.dingding.sso.core.properties.SecurityProperties;
import github.com.suitelhy.dingding.sso.core.social.support.DingDingSpringSocialConfigurer;
import github.com.suitelhy.dingding.sso.core.social.support.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 社交登录配置主类
 *
 * @author zhailiang
 */
@Configuration
@EnableSocial
public class SocialConfig
        extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    @Autowired(required = false)
    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    /* (non-Javadoc)
     * @see org.springframework.social.config.annotation.SocialConfigurerAdapter#getUsersConnectionRepository(org.springframework.social.connect.ConnectionFactoryLocator)
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource
                , connectionFactoryLocator
                , Encryptors.noOpText());
        // 设置 UserConnection 对应表的表名前缀
        repository.setTablePrefix("dingding_");
        if (null != connectionSignUp) {
            repository.setConnectionSignUp(connectionSignUp);
        }
        return repository;
    }

    /**
     * 社交登录配置类
     *
     * @return
     * @Description 供浏览器或app模块引入设计登录配置用。
     */
    @Bean
    public SpringSocialConfigurer dingDingSocialSecurityConfig() {
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        DingDingSpringSocialConfigurer configurer = new DingDingSpringSocialConfigurer(filterProcessesUrl);
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
        return configurer;
    }

    /**
     * 用来处理注册流程的工具类
     *
     * @param connectionFactoryLocator
     * @return
     * @Description 主要用于解决两个问题:
     * -> (1) 在注册过程中, 如何获取 SpringSocial 的信息;
     * -> (2) 在注册完成后, 如何把业务系统的 ID 传给 SpringSocial.
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator
                , getUsersConnectionRepository(connectionFactoryLocator)) {
        };
    }

}
