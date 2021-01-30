/**
 * 权限管理系统
 *
 * @Description OAuth 2.0 - 资源服务器.
 * -> {@link <a href="https://juejin.im/post/5a3cbce05188252582279467#heading-12">Spring security OAuth2 深入解析 - 掘金</a>}
 * -> {@link <a href="https://www.oschina.net/translate/spring-security-oauth-docs-oauth2?print">Spring Security OAuth2 开发指南 - 技术翻译 - OSCHINA 社区</a>}
 * @Reference -> {@link <a href="https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/htmlsingle/#boot-features-security-oauth2-resource-server">OAuth2 Boot</a>}
 * -> {@link <a href="https://echocow.cn/articles/2019/07/20/1563611848587.html?utm_source=hacpai.com">Spring Security Oauth2 从零到一完整实践（四）资源服务器</a>}
 * -> {@link <a href="https://stackoverflow.com/questions/48084965/spring-oauth2-with-jwt-cannot-convert-access-token-to-json-when-separating-aut/48088156">Spring OAuth2 with JWT - Cannot convert access token to JSON When Separating Auth and Resource Servers</a>}
 * @Tips · 完整过滤器链顺序, 查看 {@link org.springframework.security.config.annotation.web.builders.FilterComparator}.
 * @author Suite
 */
package github.com.suitelhy.dingding.sso.authorization;