package github.com.suitelhy.dingding.sso.authorization.infrastructure.config.security.sso;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;

/**
 * 资源服务器 - 安全配置
 *
 * @author Suite
 */
@Configuration
@EnableWebSecurity
/*@EnableGlobalMethodSecurity(prePostEnabled = true)*/
@Order(100)
public class SecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Override
    public AuthenticationManager authenticationManager()
            throws Exception {
        OAuth2AuthenticationManager authManager = new OAuth2AuthenticationManager();
        return authManager;
    }

}
