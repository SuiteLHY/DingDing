package github.com.suitelhy.dingding.sso.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 *
 * @author Suite
 */
@SpringBootApplication(scanBasePackages = {
        "github.com.suitelhy.dingding.core"
        , "github.com.suitelhy.dingding.sso.authorization"
})
// (该注解在版本迁移后有设计调整，详见
//-> <a href="https://juejin.im/post/5d78ac225188257fed0a9ba6">
//-> 		解决Spring Boot 从1.x升级到 2.x 后 单点登陆(SSO)问题 - 掘金</a>
//-> , <a href="https://github.com/jgrandja/spring-security-oauth-2-4-migrate/blob/master/pom.xml">
//-> 		spring-security-oauth-2-4-migrate/pom.xml at master · jgrandja/spring-security-oauth-2-4-migrate</a>
/*@EnableOAuth2Sso // 废弃注解, 不可用*/
// 解决问题, 详见 <a href="https://stackoverflow.com/questions/50073124/spring-boot-userredirectrequiredexception-a-redirect-is-required-to-get-the-use">
//-> 		mysql - Spring Boot UserRedirectRequiredException- A redirect is required to get the users approval - Stack Overflow</a>
/*@EnableOAuth2Client*/
@EnableResourceServer
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

}
