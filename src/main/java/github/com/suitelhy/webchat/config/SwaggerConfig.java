package github.com.suitelhy.webchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SpringFox - Swagger 配置类
 *
 * @Reference <a href="https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api">
 *-> 使用Spring REST API设置Swagger 2</a>
 * @Author Suite
 * @CreateDate 2018-12-19 11:38
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * SpringFox 为我们提供了一个 Docket（摘要的意思）类，我们需要把它做成一个Bean注入到spring中；
     * -> 显然，我们需要一个配置文件，通过 @Configuration 声明为配置文件的 Bean。
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(/*RequestHandlerSelectors.basePackage("github.com.suitelhy.webchat")*/
                        RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * SpringFox 允许我们将信息组合成一个 ApiInfo 对象，作为构造参数传给Docket
     * 	（当然也可以不构造这个类，而直接使用null；但是这个API就没有足够的信息，太low了）。
     * @return
     */
    private ApiInfo apiInfo() {
        Contact contact = new Contact("Suite"
                , "https://github.com/SuiteLHY"
                ,"a471486944@gmail.com");
        ApiInfo result = new ApiInfoBuilder()
                .title("Spring Boot 中使用 Swagger2 构建 RESTful API")
                .description("WebChat API 文档")
                .contact(contact)
                .version("1.0")
                .build();
        return result;
    }

}
