package github.com.suitelhy.dingding.sso.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

// (授权服务器)
@EnableAuthorizationServer
@SpringBootApplication(scanBasePackages = {
        "github.com.suitelhy.dingding.sso.server"
        , "github.com.suitelhy.dingding.core"})
public class SSOServerApplication
        extends SpringBootServletInitializer {

    /**
     * @Description Spring Boot 部署到 Tomcat 中去启动时需要在启动类添加SpringBootServletInitializer.
     *
     * @Reference
     * {@link <a href="http://www.ityouknow.com/springboot/2018/06/03/favorites-spring-boot-2.0.html">Spring Boot 2 版的开源项目云收藏来了！ - 纯洁的微笑博客 </a>}
     *
     * @param application
     *
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SSOServerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SSOServerApplication.class, args);
    }

}
