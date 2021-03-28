package github.com.suitelhy.dingding.sso.authorization.infrastructure.web.security;

import github.com.suitelhy.dingding.core.infrastructure.web.vo.HTTP;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.event.read.SecurityRoleReadEvent;
import github.com.suitelhy.dingding.security.service.api.domain.event.read.SecurityUrlReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 权限资源映射的数据源
 *
 * @Description 自定义权限数据源，提供所有[URL资源]与对应角色权限的映射集合.
 * 这里重写并实现了基于数据库的权限数据源, 实现了 {@link FilterInvocationSecurityMetadataSource} 接口;
 * 框架的默认实现是 {@link DefaultFilterInvocationSecurityMetadataSource}.
 *
 * @Reference
 * {@link <a href="https://www.shuzhiduo.com/A/qVdeW1wrJP/">[权限管理系统篇] (五)-Spring security（授权过程分析）</a>}
 * {@link <a href="https://github.com/ygsama/ipa/blob/master/oauth2-server/src/main/java/io/github/ygsama/oauth2server/config/LoginSecurityInterceptor.java">ipa/LoginSecurityInterceptor.java at master · ygsama/ipa</a>}
 */
@Slf4j
@Service
public class DingDingAccessSecurityMetadataSource
        implements FilterInvocationSecurityMetadataSource {

    /**
     * @Description {[url + method] : [对应 url 资源的角色列表]}
     */
    private static @NotNull Map<RequestMatcher, Collection<ConfigAttribute>> permissionMap;

    /*private final SecurityResourceReadService resourceService;*/

    @Reference
    private SecurityRoleReadEvent securityRoleReadEvent;

    @Reference
    private SecurityUrlReadEvent securityUrlReadEvent;

    @Value("${dingding.security.client-id}")
    private String clientId;

    //===== Constructor =====//

//    /**
//     * (Constructor)
//     *
//     * @param resourceService {@link SecurityResourceService}
//     */
//    DingDingAccessSecurityMetadataSource(@Autowired SecurityResourceService resourceService) {
//        this.resourceService = resourceService;
//    }

//    /**
//     * (Constructor)
//     *
//     * @param securityRoleEvent {@link SecurityRoleEvent}
//     * @param securityUrlEvent {@link SecurityUrlEvent}
//     */
//    DingDingAccessSecurityMetadataSource(@Autowired SecurityRoleEvent securityRoleEvent, @Autowired SecurityUrlEvent securityUrlEvent) {
//        this.securityRoleEvent = securityRoleEvent;
//        this.securityUrlEvent = securityUrlEvent;
//    }

    /**
     * (Constructor)
     */
    private DingDingAccessSecurityMetadataSource() {
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
        /*final @NotNull ContainArrayHashMap<String, List<Object>> permissionMap = resourceService.selectUrlRoleMap(this.clientId);
        for (Map.Entry<String[], List<Object>> permission : permissionMap.entrySet()) {
            final @NotNull String[] urlInfo = permission.getKey();
            final List<Object> roles = permission.getValue();

            *//*final String clientId = urlInfo[0];*//*
            final String url = urlInfo[1];
            final String httpMethod = urlInfo[2];

            final @NotNull AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(url, httpMethod);
            final @NotNull Collection<ConfigAttribute> attributes = new ArrayList<>(1);

            if (null != roles) {
                for (Object role : roles) {
                    attributes.add(new SecurityConfig((String) role));
                }
            }
            // 占位符，需要权限才能访问的资源 都需要添加一个占位符，保证value不是空的
            attributes.add(new SecurityConfig("@needAuth"));

            DingDingAccessSecurityMetadataSource.permissionMap.put(requestMatcher, attributes);
        }*/
        final @NotNull List<SecurityResourceUrl> permissionMap_urls = securityUrlReadEvent.selectUrlInfoByClientId(this.clientId);
        for (@NotNull SecurityResourceUrl eachUrl : permissionMap_urls) {
            if (eachUrl.isEmpty()) {
                continue;
            }

            final @NotNull List<SecurityRole> eachUrl_roles = securityRoleReadEvent.selectRoleOnResourceByResourceCode(eachUrl.getCode());

            final @NotNull String url = eachUrl.getUrlPath();
            final String httpMethod = eachUrl.getUrlMethod();

            final @NotNull AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(url, httpMethod);
            final @NotNull Collection<ConfigAttribute> attributes = new ArrayList<>(1);

            if (null != eachUrl_roles) {
                for (@NotNull SecurityRole eachRole : eachUrl_roles) {
                    attributes.add(new SecurityConfig(eachRole.getCode()));
                }
            }
            // 占位符，需要权限才能访问的资源 都需要添加一个占位符，保证value不是空的
            attributes.add(new SecurityConfig("@needAuth"));

            DingDingAccessSecurityMetadataSource.permissionMap.put(requestMatcher, attributes);
        }

        //===== 匿名访问 =====//
        final @NotNull AntPathRequestMatcher anonymousRequestMatcher = new AntPathRequestMatcher("/**", HTTP.MethodVo.OPTIONS.name());

        final @NotNull Collection<ConfigAttribute> anonymousAttributes = new ArrayList<>(1);
        anonymousAttributes.add(new SecurityConfig(DingDingAnonymousAuthenticationFilter.DEFAULT_KEY));

        DingDingAccessSecurityMetadataSource.permissionMap.put(anonymousRequestMatcher, anonymousAttributes);

        //===== 剩余的url资源， @noAuth，所有人都无法访问 =====//
        final @NotNull AntPathRequestMatcher[] noAccessRequestMatchers = new AntPathRequestMatcher[] {
                new AntPathRequestMatcher("/**", HTTP.MethodVo.GET.name())
                , new AntPathRequestMatcher("/**", HTTP.MethodVo.HEAD.name())
                , new AntPathRequestMatcher("/**", HTTP.MethodVo.POST.name())
                , new AntPathRequestMatcher("/**", HTTP.MethodVo.PUT.name())
                , new AntPathRequestMatcher("/**", HTTP.MethodVo.PATCH.name())
                , new AntPathRequestMatcher("/**", HTTP.MethodVo.DELETE.name())
                , new AntPathRequestMatcher("/**", HTTP.MethodVo.TRACE.name())
        };

        final @NotNull Collection<ConfigAttribute> noAccessAttributes = new ArrayList<>(1);
        noAccessAttributes.add(new SecurityConfig("@noAuth"));

        /*DingDingAccessSecurityMetadataSource.permissionMap.put(new AntPathRequestMatcher("/**", null)
                , attributes);*/
        for (AntPathRequestMatcher pathRequestMatcher : noAccessRequestMatchers) {
            DingDingAccessSecurityMetadataSource.permissionMap.put(pathRequestMatcher, noAccessAttributes);
        }

        //==========//
        log.info("[全局权限映射集合初始化]: {}", DingDingAccessSecurityMetadataSource.permissionMap.toString());
    }

    /**
     * @Description 鉴权时会被 {@link AbstractSecurityInterceptor#beforeInvocation(Object)} 调用, 根据 URL 找到对应需要的权限.
     *
     * @param object 安全对象类型 {@link org.springframework.security.web.FilterInvocation}
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException
    {
        if (null == permissionMap) {
            loadResourceDefine();
        }

        final HttpServletRequest request = ((FilterInvocation) object).getRequest();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : permissionMap.entrySet()) {
            if (entry.getKey().matches(request) && ! entry.getValue().isEmpty()) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * @Description 用于被 {@link AbstractSecurityInterceptor} 的实现调用, 返回所有的 {@link Collection<ConfigAttribute>}, 以筛选出不符合要求的 attribute.
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<>(1);
    }

    /**
     * @Description 用于被 {@link AbstractSecurityInterceptor} 的实现调用, 验证指定的安全对象类型是否被 MetadataSource 支持.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}