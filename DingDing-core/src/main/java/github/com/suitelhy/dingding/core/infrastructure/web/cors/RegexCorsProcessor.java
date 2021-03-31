package github.com.suitelhy.dingding.core.infrastructure.web.cors;

import github.com.suitelhy.dingding.core.infrastructure.util.Toolbox;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.DefaultCorsProcessor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 自定义跨域处理器，使用正则的方式来校验请求源是否和 @CrossOrigin 中指定的源匹配
 *
 * @see <a href="https://segmentfault.com/a/1190000021429409">@CrossOrigin 支持正则表达式 - SegmentFault 思否</a>
 */
public class RegexCorsProcessor
        extends DefaultCorsProcessor {

    /**
     * 跨域请求，会通过此方法检测请求源是否被允许
     *
     * @param config        CORS 配置
     * @param requestOrigin 请求源
     *
     * @return 如果 <tt>requestOrigin</tt> 被允许, 返回 <tt>requestOrigin</tt>; 否则返回 {@literal null}
     */
    @Override
    protected String checkOrigin(CorsConfiguration config, String requestOrigin) {
        // 先调用父类的 checkOrigin 方法，保证原来的方式继续支持
        String result = super.checkOrigin(config, requestOrigin);
        if (null != result) {
            return result;
        }

        //===== 自定义处理 =====//
        // 获取 @CrossOrigin 中配置的 origins
        List<String> allowedOrigins = config.getAllowedOrigins();
        if (CollectionUtils.isEmpty(allowedOrigins)) {
            return null;
        }

        return checkOriginWithRegex(allowedOrigins, requestOrigin);
    }

    /**
     * 用正则的方式来校验 <tt>requestOrigin</tt>
     *
     * @param allowedOrigins
     * @param requestOrigin
     *
     * @return 如果 <tt>requestOrigin</tt> 被允许, 返回 <tt>requestOrigin</tt>; 否则返回 {@literal null}
     */
    private String checkOriginWithRegex(@NotNull List<String> allowedOrigins, @NotNull String requestOrigin) {
        for (String allowedOrigin : allowedOrigins) {
            if (Boolean.TRUE.equals(Toolbox.RegexUtil.getInstance().matchers(allowedOrigin, requestOrigin))) {
                return requestOrigin;
            }
        }
        return null;
    }

}
