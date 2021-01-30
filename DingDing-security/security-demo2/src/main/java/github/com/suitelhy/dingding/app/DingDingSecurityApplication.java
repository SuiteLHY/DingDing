package github.com.suitelhy.dingding.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class DingDingSecurityApplication
        extends SpringBootServletInitializer {

//    @Autowired
//    DataInitializer initializer;
//
//    @PostConstruct
//    public void init() {
//        CustomerId customerId = initializer.initializeCustomer();
//        initializer.initializeOrder(customerId);
//    }

    /**
     * @param application
     * @return
     * @Description Spring Boot 部署到 Tomcat 中去启动时需要在启动类添加SpringBootServletInitializer.
     * @Reference <a href="http://www.ityouknow.com/springboot/2018/06/03/favorites-spring-boot-2.0.html">
     * ->      Spring Boot 2 版的开源项目云收藏来了！ - 纯洁的微笑博客 </a>
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DingDingSecurityApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(DingDingSecurityApplication.class, args);
    }

}
