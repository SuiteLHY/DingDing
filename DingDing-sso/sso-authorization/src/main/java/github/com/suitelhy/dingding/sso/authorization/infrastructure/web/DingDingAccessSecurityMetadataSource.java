package github.com.suitelhy.dingding.sso.authorization.infrastructure.web;

import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 权限资源映射的数据源
 *
 * @Description 自定义权限数据源，提供所有URL资源与对应角色权限的映射集合.
 *->  这里重写并实现了基于数据库的权限数据源, 实现了 {@link FilterInvocationSecurityMetadataSource}接口;
 *->  框架的默认实现是 {@link DefaultFilterInvocationSecurityMetadataSource}.
 *
 * @Reference
 *->  {@link <a href="https://www.shuzhiduo.com/A/qVdeW1wrJP/">[权限管理系统篇] (五)-Spring security（授权过程分析）</a>}
 *->  {@link <a href="https://github.com/ygsama/ipa/blob/master/oauth2-server/src/main/java/io/github/ygsama/oauth2server/config/LoginSecurityInterceptor.java">ipa/LoginSecurityInterceptor.java at master · ygsama/ipa</a>}
 */
@Slf4j
@Service
public class DingDingAccessSecurityMetadataSource
        implements FilterInvocationSecurityMetadataSource {

    /**
     * @Description {[url + method] : [对应 url 资源的角色列表]}
     */
    private static Map<RequestMatcher, Collection<ConfigAttribute>> permissionMap;

    private final SecurityResourceService resourceService;

    @Value("${dingding.security.client-id}")
    private String clientId;

    //===== Constructor =====//

    DingDingAccessSecurityMetadataSource(@Autowired SecurityResourceService resourceService) {
        this.resourceService = resourceService;
    }

    //==========//

    /**
     * 在 Web 服务器启动时，缓存系统中的所有权限映射
     *
     * @Description 被 {@link PostConstruct} 修饰的方法会在服务器加载 Servlet 的时候运行 (构造器之后, init()之前).
     */
    @PostConstruct
    private void loadResourceDefine() {
        permissionMap = new LinkedHashMap<>(1);

        //===== 需要鉴权的 URL 资源，@needAuth标志 =====//
        /*ContainArrayHashMap<String, List<Object>> permissionMap = resourceService.selectAllUrlRoleMap();*/
        ContainArrayHashMap<String, List<Object>> permissionMap = resourceService.selectUrlRoleMap(this.clientId);
        for (Map.Entry<String[], List<Object>> permission : permissionMap.entrySet()) {
            final String[] urlInfo = permission.getKey();
            final List<Object> roles = permission.getValue();

            /*final String clientId = urlInfo[0];*/
            final String url = urlInfo[1];

            /*if (!ObjectUtils.nullSafeEquals(this.clientId, clientId)) {
                continue;
            }*/

            final AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(url);
            final Collection<ConfigAttribute> attributes = new ArrayList<>(1);

            for (Object role : roles) {
                attributes.add(new SecurityConfig((String) role));
            }
            // 占位符，需要权限才能访问的资源 都需要添加一个占位符，保证value不是空的
            attributes.add(new SecurityConfig("@needAuth"));

            DingDingAccessSecurityMetadataSource.permissionMap.put(requestMatcher, attributes);
        }

        //===== (目前没有这一部分设计; 保留参考资料的初始代码) =====//
        /*// 公共的url资源 & 系统接口的url资源，value为null
        List<SysPermissionDO> publicList = resourceService.queryPublicPermission();
        for (SysPermissionDO permission : publicList) {
            String url = permission.getUrl();
            String method = permission.getMethod();
            AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(url, "*".equals(method) ? null : method);
            // value为空时不做鉴权，相当于所有人都可以访问该资源URL
            permissionMap.put(requestMatcher, null);
        }*/

        //===== 多余的url资源， @noAuth，所有人都无法访问 =====//
        Collection<ConfigAttribute> attributes = new ArrayList<>(1);
        attributes.add(new SecurityConfig("@noAuth"));
        DingDingAccessSecurityMetadataSource.permissionMap.put(new AntPathRequestMatcher("/**", null), attributes);

        //==========//
        log.info("[全局权限映射集合初始化]: {}", DingDingAccessSecurityMetadataSource.permissionMap.toString());
    }

    /**
     * @Description 鉴权时会被 AbstractSecurityInterceptor.beforeInvocation() 调用, 根据 URL 找到对应需要的权限.
     *
     * @param object    安全对象类型 {@link org.springframework.security.web.FilterInvocation}
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        if (null == permissionMap) {
            loadResourceDefine();
        }

        final HttpServletRequest request = ((FilterInvocation) object).getRequest();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : permissionMap.entrySet()) {
            if (entry.getKey().matches(request)
                    && !entry.getValue().isEmpty()) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * @Description 用于被 AbstractSecurityInterceptor 调用, 返回所有的 Collection<ConfigAttribute>, 以筛选出不符合要求的 attribute.
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<>(1);
    }

    /**
     * @Description 用于被 AbstractSecurityInterceptor 调用, 验证指定的安全对象类型是否被 MetadataSource 支持.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}