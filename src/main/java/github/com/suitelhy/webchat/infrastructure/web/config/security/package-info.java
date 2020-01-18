/**
 * Web 层 - 安全配置
 */
/**
 * 关于 Spring Security:
 * · Web 层获取会话中当前登录用户的策略选择及其实现:
 *-> <Forum><a href="https://stackoverflow.com/questions/248562/when-using-spring-security-what-is-the-proper-way-to-obtain-current-username-i">
 *->     java-使用Spring Security时，在bean中获取当前用户名（即SecurityContext）信息的正确方法是什么？ - 堆栈溢出</a></Forum>
 *-> , <Docs><a href="https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#mvc-authentication-principal">
 *->     34.2 @AuthenticationPrincipal</a></Docs>
 *
 */
package github.com.suitelhy.webchat.infrastructure.web.config.security;