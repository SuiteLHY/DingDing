package github.com.suitelhy.dingding.sso.authorization.infrastructure.web.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 匿名用户过滤器
 *
 * @Description 主要是设置定制化的默认角色标识.
 *
 * @Reference
 *-> {@link <a href="http://blog.joylau.cn/2019/08/19/SpringBoot-SpringSecurity-Anonymous/">Spring Security 禁用匿名用户（anonymous().disable()）后无限重定向到登录页的问题解决 - JoyLau's Blog | JoyLau</a>}
 *-> {@link <a href="https://bbs.csdn.net/topics/390167935">An Authentication object was not found in the SecurityContex 异常-CSDN论坛</a>}
 *-> {@link <a href="https://stackoverflow.com/questions/39683987/spring-security-define-a-custom-anonymous-filter">java - Spring Security define a custom anonymous filter - Stack Overflow</a>}
 *
 * @see AnonymousAuthenticationFilter
 * @see Security.RoleVo
 */
@Component
@Slf4j
public class DingDingAnonymousAuthenticationFilter
        extends AnonymousAuthenticationFilter {

    //===== Static fields/initializers =====//

    public static final String DEFAULT_KEY = Security.RoleVo.ROLE_ANONYMOUS.name();

    //===== Instance fields =====//

    //===== Constructor =====//

    public DingDingAnonymousAuthenticationFilter() {
        super(DEFAULT_KEY);
    }

    /**
     * Creates a filter with a principal named "anonymousUser" and the single authority
     * "ROLE_ANONYMOUS".
     *
     * @param key the key to identify tokens created by this filter
     */
    public DingDingAnonymousAuthenticationFilter(String key) {
        super(key);
    }

    /**
     * @param key         key the key to identify tokens created by this filter
     * @param principal   the principal which will be used to represent anonymous users
     * @param authorities the authority list for anonymous users
     */
    public DingDingAnonymousAuthenticationFilter(String key
            , Object principal
            , List<GrantedAuthority> authorities) {
        super(key, principal, authorities);
    }

}
