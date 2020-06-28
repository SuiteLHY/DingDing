package github.com.suitelhy.dingding.sso.authorization.infrastructure.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

/**
 * HTTP 权限过滤器
 *
 * @Description 自定义过滤器.
 *
 * @Reference
 *->  {@link org.springframework.security.web.access.intercept.FilterSecurityInterceptor}
 *
 * @see FilterSecurityInterceptor
 */
@Component
@Slf4j
public class DingDingFilterSecurityInterceptor
        extends FilterSecurityInterceptor {

    //===== Static fields/initializers =====//

//    /**
//     * @see {@link org.springframework.security.web.access.intercept.FilterSecurityInterceptor#FILTER_APPLIED}
//     */
//    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";

    //===== Instance fields =====//

    private final AccessDecisionManager accessDecisionManager;

    private final FilterInvocationSecurityMetadataSource securityMetadataSource;

    //===== Constructor =====//

    @Autowired
    public DingDingFilterSecurityInterceptor(DingDingAccessSecurityMetadataSource securityMetadataSource, DingDingAccessDecisionManager accessDecisionManager) {
        this.securityMetadataSource = securityMetadataSource;
        this.accessDecisionManager = accessDecisionManager;
    }

    //===== Methods =====//

    @PostConstruct
    public void initSetManager() {
        super.setAccessDecisionManager(accessDecisionManager);
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    /**
     * Method that is actually called by the filter chain. Simply delegates to the
     * {@link #invoke(FilterInvocation)} method.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the filter chain
     *
     * @see {@link org.springframework.security.web.access.intercept.FilterSecurityInterceptor#doFilter(ServletRequest,ServletResponse,FilterChain)}
     *
     * @throws IOException if the filter chain fails
     * @throws ServletException if the filter chain fails
     */
    @Override
    public void doFilter(ServletRequest request
            , ServletResponse response
            , FilterChain chain)
            throws IOException, ServletException {

        FilterInvocation fi = new FilterInvocation(request, response, chain);

        invoke(fi);
    }

    /**
     * 鉴权操作
     *
     * @param fi
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void invoke(FilterInvocation fi)
            throws IOException, ServletException {
        /*boolean observeOncePerRequest = true;
        if ((null != fi.getRequest())
                && (null != fi.getRequest().getAttribute(FILTER_APPLIED))
                && observeOncePerRequest) {
            //--- 过滤器已应用于此请求, 用户希望我们观察每个请求一次的处理, 因此请勿重新进行安全检查
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } else {
            //--- 第一次调用此请求, 因此请执行安全检查
            if (null != fi.getRequest() && observeOncePerRequest) {
                fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
            }

            InterceptorStatusToken token = super.beforeInvocation(fi);

            try {
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            } finally {
                super.finallyInvocation(token);
            }

            super.afterInvocation(token, null);
        }*/

        log.info("[自定义过滤器]: {}", " doFilter");

        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

}