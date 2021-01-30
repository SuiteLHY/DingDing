/**
 * 认证授权服务
 *
 * @Description OAuth 2.0 - 认证服务器.
 * -> {@link <a href="https://juejin.im/post/5a3cbce05188252582279467#heading-12">Spring security OAuth2 深入解析 - 掘金</a>}
 * -> {@link <a href="https://www.oschina.net/translate/spring-security-oauth-docs-oauth2?print">Spring Security OAuth2 开发指南 - 技术翻译 - OSCHINA 社区</a>}
 * @Reference -> {@link <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide · spring-projects/spring-security Wiki</a>}
 * -> {@link <a href="https://stackoverflow.com/questions/48084965/spring-oauth2-with-jwt-cannot-convert-access-token-to-json-when-separating-aut/48088156">java-带JWT的Spring OAuth2-分离身份验证和资源服务器时无法将访问令牌转换为JSON-代码日志</a>}
 * @Tips · 完整过滤器链顺序, 查看 {@link org.springframework.security.config.annotation.web.builders.FilterComparator}.
 * @author Suite
 */
package github.com.suitelhy.dingding.sso.server;