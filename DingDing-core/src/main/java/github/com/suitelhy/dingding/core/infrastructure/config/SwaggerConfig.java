package github.com.suitelhy.dingding.core.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import com.fasterxml.classmate.TypeResolver;

/**
 * SpringFox - Swagger 配置类
 *
 * @Issue · 集成 Spring Security 之后, Swagger ui 的 Controller 无法注册到 Web MVC 中.
 * -> {@Solution <a href="https://stackoverflow.com/questions/43545540/swagger-ui-no-mapping-found-for-http-request">
 * ->     java - swagger-ui No mapping found for HTTP request - Stack Overflow</a>}
 * · 关于 $ref 的 Swagger UI 前端页面输出的问题描述:
 * -> <Solution><a href="https://github.com/springfox/springfox/issues/2563">
 * ->     无法解析指针：/ definitions /列表在文档中不存在·问题＃2563·springfox / springfox</a></Solution>
 * -> , <Solution><a href="https://github.com/springfox/springfox/blob/d6c72bf8623b3efaebb3e276154cf98f5fdf8af6/springfox-spring-config/src/main/java/springfox/springconfig/Swagger2SpringBoot.java#L85">
 * ->     springfox/Swagger2SpringBoot.java at d6c72bf8623b3efaebb3e276154cf98f5fdf8af6 · springfox/springfox</a></Solution>
 * @Reference -> <a href="https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api">
 * ->     使用Spring REST API设置Swagger 2</a>
 * @Author Suite
 * @CreateDate 2018-12-19 11:38
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig
        implements WebMvcConfigurer {

    /**
     * 添加 View Controller
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/api/v2/api-docs"
                , "/v2/api-docs");
        registry.addRedirectViewController("/api/swagger-resources/configuration/ui"
                , "/swagger-resources/configuration/ui");
        registry.addRedirectViewController("/api/swagger-resources/configuration/security"
                , "/swagger-resources/configuration/security");
        registry.addRedirectViewController("/api/swagger-resources"
                , "/swagger-resources");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/swagger-ui.html**")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/api/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * SpringFox 为我们提供了一个 Docket（摘要）类，我们需要把它做成一个Bean注入到spring中；
     * -> 显然，我们需要一个配置文件，通过 @Configuration 声明为配置文件的 Bean。
     *
     * @return
     */
    @Bean
    public Docket apiDocket() {
        TypeResolver typeResolver = new TypeResolver();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(/*RequestHandlerSelectors.basePackage("github.com.suitelhy.dingding")*/
                        RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * SpringFox 允许我们将信息组合成一个 ApiInfo 对象，作为构造参数传给Docket
     * （当然也可以不构造这个类，而直接使用null；但是这个API就没有足够的信息，太low了）。
     *
     * @return
     */
    private ApiInfo getApiInfo() {
        Contact contact = new Contact("Suite"
                , "https://github.com/SuiteLHY"
                , "a471486944@gmail.com");
        ApiInfo result = new ApiInfoBuilder()
                .title("基于 Spring Boot 的项目中使用 Swagger2 构建 RESTful API")
                .description("WebChat API 文档")
                .contact(contact)
                .version("1.0")
                .build();
        return result;
    }

}
