//package github.com.suitelhy.dingding.sso.authorization.infrastructure.config.security.sso;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.access.AccessDecisionManager;
//import org.springframework.security.access.AccessDecisionVoter;
//import org.springframework.security.access.vote.AffirmativeBased;
//import org.springframework.security.access.vote.RoleVoter;
//import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * 动态权限组件配置
// *
// * @Reference
// *->  {@link <a href="https://juejin.im/post/5de0ed6ae51d4533007261d2#heading-2">Spring Security 实战干货：动态权限控制（下）实现 - 掘金</a>}
// */
//@Configuration
//public class DynamicAccessControlConfiguration {
//
//    /**
//     * RequestMatcher 生成器
//     *
//     * @return RequestMatcher
//     */
//    @Bean
//    public RequestMatcherCreator requestMatcherCreator() {
//        return metaResources -> metaResources.stream()
//                .map(metaResource -> new AntPathRequestMatcher(metaResource.getPattern(), metaResource.getMethod()))
//                .collect(Collectors.toSet());
//    }
//
//    /**
//     * 元数据加载器
//     *
//     * @return dynamicFilterInvocationSecurityMetadataSource
//     */
//    @Bean
//    public FilterInvocationSecurityMetadataSource dynamicFilterInvocationSecurityMetadataSource() {
//        return new DynamicFilterInvocationSecurityMetadataSource();
//    }
//
//    /**
//     * 角色投票器
//     *
//     * @return roleVoter
//     */
//    @Bean
//    public RoleVoter roleVoter() {
//        return new RoleVoter();
//    }
//
//    /**
//     * 基于肯定的访问决策器
//     *
//     * @param decisionVoters  AccessDecisionVoter类型的 Bean 会自动注入到 decisionVoters
//     * @return affirmativeBased
//     */
//    @Bean
//    public AccessDecisionManager affirmativeBased(List<AccessDecisionVoter<?>> decisionVoters) {
//        return new AffirmativeBased(decisionVoters);
//    }
//
//}
