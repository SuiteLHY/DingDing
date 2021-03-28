package github.com.suitelhy.dingding.log.service.client.infrastructure.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * HTTP 权限过滤器
 *
 * @Description 自定义过滤器.
 *
 * @Reference ->  {@link <a href="https://www.shuzhiduo.com/A/qVdeW1wrJP/">[权限管理系统篇] (五)-Spring security（授权过程分析）</a>}
 * {@link <a href="https://github.com/ygsama/ipa/blob/master/oauth2-server/src/main/java/io/github/ygsama/oauth2server/config/LoginSecurityInterceptor.java">ipa/LoginSecurityInterceptor.java at master · ygsama/ipa</a>}
 * {@link org.springframework.security.web.access.intercept.FilterSecurityInterceptor}
 *
 * @see FilterSecurityInterceptor
 */
@Component
@Slf4j
public class DingDingFilterSecurityInterceptor
        extends FilterSecurityInterceptor {

    //===== Static fields/initializers =====//

    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";

    //===== Instance fields =====//

    private final AccessDecisionManager accessDecisionManager;

    private final FilterInvocationSecurityMetadataSource securityMetadataSource;

    private boolean observeOncePerRequest = true;

    //===== Constructor =====//

    @Autowired
    public DingDingFilterSecurityInterceptor(DingDingAccessSecurityMetadataSource securityMetadataSource
            , DingDingAccessDecisionManager accessDecisionManager) {
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
     * Method that is actually called by the filter chain. Simply delegates to the {@link #invoke(FilterInvocation)} method.
     *
     * @param request  the servlet request
     * @param response the servlet response
     * @param chain    the filter chain
     * @throws IOException      if the filter chain fails
     * @throws ServletException if the filter chain fails
     * @see {@link FilterSecurityInterceptor#doFilter(ServletRequest, ServletResponse, FilterChain)}
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
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    //===== Getter And Setter =====//

    /**
     * Indicates whether once-per-request handling will be observed. By default this is
     * <code>true</code>, meaning the <code>FilterSecurityInterceptor</code> will only
     * execute once-per-request. Sometimes users may wish it to execute more than once per
     * request, such as when JSP forwards are being used and filter security is desired on
     * each included fragment of the HTTP request.
     *
     * @return <code>true</code> (the default) if once-per-request is honoured, otherwise
     * <code>false</code> if <code>FilterSecurityInterceptor</code> will enforce
     * authorizations for each and every fragment of the HTTP request.
     */
    public boolean isObserveOncePerRequest() {
        return observeOncePerRequest;
    }

    public void setObserveOncePerRequest(boolean observeOncePerRequest) {
        this.observeOncePerRequest = observeOncePerRequest;
    }

}