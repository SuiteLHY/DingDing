package github.com.suitelhy.dingding.sso.authorization.infrastructure.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * 鉴权决策管理器
 *
 * @Description 自定义鉴权管理器，根据 URL资源权限 和 用户角色权限 进行鉴权.
 * 被鉴权决策管理器 {@link AbstractSecurityInterceptor} 调用进行鉴权;
 * 框架默认实现是 {@link UnanimousBased}.
 * @Reference {@link <a href="https://www.shuzhiduo.com/A/qVdeW1wrJP/">[权限管理系统篇] (五)-Spring security（授权过程分析）</a>}
 * {@link <a href="https://github.com/ygsama/ipa/blob/master/oauth2-server/src/main/java/io/github/ygsama/oauth2server/config/LoginSecurityInterceptor.java">ipa/LoginSecurityInterceptor.java at master · ygsama/ipa</a>}
 * {@link AccessDecisionManager}
 * {@link DingDingAccessSecurityMetadataSource}
 */
@Slf4j
@Component
public class DingDingAccessDecisionManager
        implements AccessDecisionManager {

    /**
     * 权限鉴定
     *
     * @param authentication   {@link SecurityContextHolder#getContext()} -> {@link UserDetails#getAuthorities()}
     * @param object           {@link FilterInvocation}
     * @param configAttributes 非空 {@link DingDingAccessSecurityMetadataSource#getAttributes(Object)}
     */
    @Override
    public void decide(@NotNull Authentication authentication
            , Object object
            , @NotNull Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {

//        log.info("[资源权限]: {}", configAttributes);
//        log.info("[用户权限]: {}", authentication.getAuthorities());

        // 获取已定义的资源权限配置
        for (ConfigAttribute resourceAttr : configAttributes) {
            // 资源的权限
            final @NotNull String resourceRole = resourceAttr.getAttribute();

            // 用户的权限
            for (GrantedAuthority userAuth : authentication.getAuthorities()) {
                log.info("[资源角色==用户角色] ? {} == {}"
                        , resourceRole.trim()
                        , userAuth.getAuthority().trim());

                if (resourceRole.trim().equals(userAuth.getAuthority().trim())) {
                    return;
                }
            }
        }

        throw new AccessDeniedException("权限不足");
    }

    /**
     * @Description 被 {@link AbstractSecurityInterceptor} 调用, 遍历 {@link ConfigAttribute} 集合, 筛选出不支持的 attribute.
     */
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    /**
     * @Description 被 {@link AbstractSecurityInterceptor} 调用, 验证 {@link AccessDecisionManager} 是否支持这个安全对象的类型.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
