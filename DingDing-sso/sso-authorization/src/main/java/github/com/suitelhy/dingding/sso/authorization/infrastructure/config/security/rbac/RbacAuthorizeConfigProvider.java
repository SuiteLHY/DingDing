package github.com.suitelhy.dingding.sso.authorization.infrastructure.config.security.rbac;

import github.com.suitelhy.dingding.core.infrastructure.web.model.AuthorizeConfigProvider;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Order(Integer.MAX_VALUE)
public class RbacAuthorizeConfigProvider
        implements AuthorizeConfigProvider {

    /**
     * 配置方法
     *
     * @Description 在整个授权配置中，
     *-> 应该有且仅有一个针对anyRequest的配置，如果所有的实现都没有针对anyRequest的配置，
     *-> 系统会自动增加一个anyRequest().authenticated()的配置。如果有多个针对anyRequest
     *-> 的配置，则会抛出异常。
     * @param config
     * @return 返回的boolean表示配置中是否有针对anyRequest的配置。
     */
    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config
                .antMatchers(HttpMethod.GET
                        , "/fonts/**").permitAll()
                .antMatchers(HttpMethod.GET
                        , "/**/*.html"
                        , "/authorization").authenticated()
                .anyRequest()
                        .access("@rbacService.hasPermission(request, authentication)");
        return true;
    }

}
