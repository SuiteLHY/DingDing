package github.com.suitelhy.dingding.sso.authorization.infrastructure.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2AuthenticationFailureEvent;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetailsSource;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.Assert;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 *
 * @Description 自定义鉴权 Filter.
 *->    FilterSecurityInterceptor 是 filterchain 中比较复杂，也是比较核心的过滤器，主要负责web应用安全授权的工作。
 *
 * @Reference
 *->    {@link org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter}
 *->    {@link <a href="https://blog.csdn.net/Little_fxc/article/details/92763518">Spring Security Oauth2 添加自定义过滤器和oauth2认证后API权限控制</a>}
 *->    {@link <a href="https://www.jianshu.com/p/68885d0e1cd9">Spring Secutity  添加过滤器实现自定义登录认证 - 简书</a>}
 *
 */
//@Slf4j
//public class OAuth2UserUrlFilter
//        extends AbstractAuthenticationProcessingFilter {
//
//    public OAuth2RestOperations restTemplate;
//
//    private ResourceServerTokenServices tokenServices;
//
//    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new OAuth2AuthenticationDetailsSource();
//
//    private ApplicationEventPublisher eventPublisher;
//
//    /**
//     * (Constructor)
//     *
//     * @param defaultFilterProcessesUrl
//     */
//    public OAuth2UserUrlFilter(String defaultFilterProcessesUrl) {
//        super(defaultFilterProcessesUrl);
//        setAuthenticationManager(new OAuth2UserUrlFilter.NoopAuthenticationManager());
//        setAuthenticationDetailsSource(authenticationDetailsSource);
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        Assert.state(null != restTemplate, "Supply a rest-template");
//        super.afterPropertiesSet();
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException, IOException, ServletException {
//        OAuth2AccessToken accessToken;
//        try {
//            accessToken = restTemplate.getAccessToken();
//        } catch (OAuth2Exception e) {
//            BadCredentialsException bad = new BadCredentialsException("Could not obtain access token", e);
//            publish(new OAuth2AuthenticationFailureEvent(bad));
//            throw bad;
//        }
//
//        try {
//            OAuth2Authentication result = tokenServices.loadAuthentication(accessToken.getValue());
//            if (authenticationDetailsSource!=null) {
//                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, accessToken.getValue());
//                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, accessToken.getTokenType());
//                result.setDetails(authenticationDetailsSource.buildDetails(request));
//            }
//
//            publish(new AuthenticationSuccessEvent(result));
//
//            return result;
//        } catch (InvalidTokenException e) {
//            BadCredentialsException bad = new BadCredentialsException("Could not obtain user details from token", e);
//            publish(new OAuth2AuthenticationFailureEvent(bad));
//            throw bad;
//        }
//    }
//
//    private void publish(ApplicationEvent event) {
//        if (null != eventPublisher) {
//            eventPublisher.publishEvent(event);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request
//            , HttpServletResponse response
//            , FilterChain chain, Authentication authResult)
//            throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);
//
//        // Nearly a no-op, but if there is a ClientTokenServices then the token will now be stored
//        restTemplate.getAccessToken();
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request
//            , HttpServletResponse response
//            , AuthenticationException failed)
//            throws IOException, ServletException {
//        if (failed instanceof AccessTokenRequiredException) {
//            // Need to force a redirect via the OAuth client filter, so rethrow here
//            throw failed;
//        } else {
//            // If the exception is not a Spring Security exception this will result in a default error page
//            super.unsuccessfulAuthentication(request, response, failed);
//        }
//    }
//
//    private static class NoopAuthenticationManager
//            implements AuthenticationManager {
//        @Override
//        public Authentication authenticate(Authentication authentication)
//                throws AuthenticationException {
//            throw new UnsupportedOperationException("No authentication should be done with this AuthenticationManager");
//        }
//    }
//
//    //===== Getter And Setter =====//
//
//    @Override
//    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
//        this.eventPublisher = eventPublisher;
//        super.setApplicationEventPublisher(eventPublisher);
//    }
//
//    /**
//     * A rest template to be used to obtain an access token.
//     *
//     * @param restTemplate a rest template
//     */
//    public void setRestTemplate(OAuth2RestOperations restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    /**
//     * Reference to a CheckTokenServices that can validate an OAuth2AccessToken
//     *
//     * @param tokenServices
//     */
//    public void setTokenServices(ResourceServerTokenServices tokenServices) {
//        this.tokenServices = tokenServices;
//    }
//
//}
