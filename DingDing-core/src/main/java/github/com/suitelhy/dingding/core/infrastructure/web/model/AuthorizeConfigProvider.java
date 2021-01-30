package github.com.suitelhy.dingding.core.infrastructure.web.model;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 授权配置提供器
 *
 * @Description 各个业务系统及其模块可以通过实现此接口向系统添加授权配置.
 * @Editor Suite
 */
public interface AuthorizeConfigProvider {

    /**
     * 配置方法
     *
     * @param config {@link ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry}
     * @return 配置中是否有针对 <code>anyRequest</code> 的配置.
     * @Description 在整个授权配置中, 应该有且仅有一个针对 <code>anyRequest</code> 的配置.
     * -> 如果所有的实现都没有针对anyRequest的配置, 系统会自动增加一个 <code>anyRequest().authenticated()</code> 等效的配置;
     * -> 如果有多个针对 <code>anyRequest</code> 的配置, 则会抛出异常.
     */
    boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}
