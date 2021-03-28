package github.com.suitelhy.dingding.log.service.client.infrastructure.config.security;

import github.com.suitelhy.dingding.core.infrastructure.web.vo.HTTP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局跨域访问配置
 *
 * @Description -> {@link <a href="https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Access_control_CORS">HTTP访问控制（CORS） - HTTP | MDN</a>}
 * @Reference -> {@link <a href="https://blog.csdn.net/niugang0920/article/details/79817763">Spring、Spring-Boot、Spring-Security中对CORS（跨域资源共享）的支持_盲目的拾荒者的博客-CSDN博客_springsecurity cors</a>}
 */
@Configuration
@ConfigurationProperties(prefix = "dingding.security.cross")
@Slf4j
public class GlobalCorsConfig {

    private static final List<String> allowedMethods = Arrays.asList(HTTP.MethodVo.GET.name()
            , HTTP.MethodVo.POST.name()
            , HTTP.MethodVo.PUT.name()
            , HTTP.MethodVo.PATCH.name()
            , HTTP.MethodVo.DELETE.name()
            , HTTP.MethodVo.OPTIONS.name());

    private static final List<String> allowOrigins = new ArrayList<>(1);

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 是否允许证书
        corsConfiguration.setAllowCredentials(true);
        // 设置允许跨域请求的域名
        corsConfiguration.setAllowedOrigins(allowOrigins);
        // 设置允许跨域请求的请求头
        corsConfiguration.addAllowedHeader("*");
        // 设置允许跨域请求的 HTTP 方法
        corsConfiguration.setAllowedMethods(allowedMethods);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    //===== Getter And Setter =====//

    /**
     * 配置允许跨域的域名
     *
     * @param allow
     */
    public void setAllowOriginHost(String allow) {
        if (null != allow) {
            allowOrigins.addAll(Arrays.asList(allow.split(",")));
        }
    }

    public static @NotNull List<String> getAllowedMethods() {
        return new ArrayList<>(allowedMethods);
    }

}
