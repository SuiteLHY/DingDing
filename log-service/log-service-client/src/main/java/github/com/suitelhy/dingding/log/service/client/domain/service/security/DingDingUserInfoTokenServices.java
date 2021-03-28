package github.com.suitelhy.dingding.log.service.client.domain.service.security;

import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.*;

/**
 * Token - 用户认证业务
 *
 * @Description 自定义设计, 解决一些 Spring Security OAuth 2.0 框架的 BUG.
 *
 * @Solution · Spring Security OAuth 2.0 实现的设计更改后, 相关的策略没有及时更新.
 * {@link <a href="https://github.com/spring-projects/spring-security-oauth/issues/1710">CheckTokenEndpoint should restrict to POST method only · Issue #1710 · spring-projects/spring-security-oauth</a>}
 *
 * @see org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices
 * @see ResourceServerTokenServices
 */
@Slf4j
public class DingDingUserInfoTokenServices
        implements ResourceServerTokenServices {

    private static class AuthenticationDetails
            extends OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails {

        private final Boolean active;

        private final @NotNull Collection<String> authorities;

        private final @NotNull String clientId;

        private final @NotNull Collection<String> scope;

        private final @NotNull String userName;

        /**
         * (Constructor)
         *
         * @Description 限制 {@link OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails} 实现类的构造.
         *
         * @param active      是否处于活动状态
         * @param authorities 权限集合
         * @param clientId    (凭证对应的)客户端编号
         * @param scope       可操作范围
         * @param userName    (凭证对应的)用户名称
         *
         * @throws InsufficientAuthenticationException 不满足构建[用户认证凭据 - 详细信息]的必要条件
         *
         * @see this#isActive()
         * @see this#getAuthorities()
         * @see this#getClientId()
         * @see this#getScope()
         * @see this#getUserName()
         */
        private AuthenticationDetails(Boolean active, Collection<String> authorities, @NotNull String clientId
                , Collection<String> scope, @NotNull String userName)
        {
            super(active
                    , (null != authorities) ? authorities : (authorities = new ArrayList<>(0))
                    , clientId
                    , (null != scope) ? scope : (scope = new ArrayList<>(0))
                    , userName);

            this.active = active;
            this.authorities = authorities;
            this.clientId = clientId;
            this.scope = scope;
            this.userName = userName;
        }

        @Override
        public Boolean isActive() {
            return this.active;
        }

        @Override
        public @NotNull
        Collection<String> getAuthorities() {
            return this.authorities;
        }

        @Override
        public @NotNull
        String getClientId() {
            return this.clientId;
        }

        @Override
        public @NotNull
        Collection<String> getScope() {
            return this.scope;
        }

        @Override
        public @NotNull
        String getUserName() {
            return this.userName;
        }

        @Override
        public @NotNull
        String toString() {
            return String.format("{active=%s, user_name=%s, authorities=%s, client_id=%s, scope=%s}"
                    , this.active
                    , this.userName
                    , this.authorities
                    , this.clientId
                    , this.scope);
        }

    }

    private final String userInfoEndpointUrl;

    private final String clientId;

    private OAuth2RestOperations restTemplate;

    private String tokenType = DefaultOAuth2AccessToken.BEARER_TYPE;

    private String tokenName = "token";

    private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();

    private PrincipalExtractor principalExtractor = new FixedPrincipalExtractor();

    //===== Constructor =====//

    /**
     * (Constructor)
     *
     * @param userInfoEndpointUrl
     * @param clientId
     */
    public DingDingUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        this.userInfoEndpointUrl = userInfoEndpointUrl;
        this.clientId = clientId;
    }

    //==========//

    /**
     * Load the credentials for the specified access token.
     *
     * @param accessToken
     * @return {@link OAuth2Authentication}
     * @throws AuthenticationException
     * @throws InvalidTokenException
     * @Description 获取身份凭证信息.【重要方法】
     * @see ResourceServerTokenServices#loadAuthentication(String)
     * @see this#getMap(String, String)
     * @see this#extractAuthentication(Map)
     */
    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException
    {
        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);

        if (map.containsKey("error")) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("userinfo returned error: %s", map.get("error")));
            }
            throw new InvalidTokenException(accessToken);
        }

        return extractAuthentication(map, accessToken);
    }

    /**
     * 提取身份凭证
     *
     * @param map
     *
     * @return {@link OAuth2Authentication}
     *
     * @see this#loadAuthentication(String)
     * @see this#getPrincipal(Map)
     */
    private OAuth2Authentication extractAuthentication(Map<String, Object> map, @NotNull String accessToken) {
        Object principal = getPrincipal(map);
        List<GrantedAuthority> authorities = this.authoritiesExtractor.extractAuthorities(map);
        OAuth2Request request = new OAuth2Request(null, this.clientId, null
                , true, null, null
                , null, null, null);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);

        try {
            map.put("oAuth2AuthenticationInfo_AbstractUserAuthentication_AbstractDetails", new AuthenticationDetails((Boolean) map.get("active")
                    , (Collection<String>) map.get("authorities")
                    , (String) map.get("client_id")
                    , (Collection<String>) map.get("scope")
                    , (String) map.get("user_name")));
        } catch (Exception e) {
            throw new InvalidTokenException(accessToken);
        }
        token.setDetails(map);

        return new OAuth2Authentication(request, token);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    //===== Getter And Setter =====//

    /**
     * Return the principal that should be used for the token. The default implementation
     * delegates to the {@link PrincipalExtractor}.
     *
     * @param map the source map
     * @return the principal or {@literal "unknown"}
     */
    protected Object getPrincipal(Map<String, Object> map) {
        Object principal = this.principalExtractor.extractPrincipal(map);
        return (null == principal ? "unknown" : principal);
    }

    /**
     * 从 {@param path} 对应的 API 获取基于 {@param accessToken} 的身份凭证信息
     *
     * @Description 重点: 此处修改请求方式.
     *
     * @param path        远程请求路径
     * @param accessToken 远程请求所需的令牌
     *
     * @return 基于 {@param accessToken} 的身份凭证信息
     *
     * @see this#loadAuthentication(String)
     * @see RestTemplate#postForEntity(URI, Object, Class)
     */
    @SuppressWarnings({"unchecked"})
    private Map<String, Object> getMap(String path, String accessToken) {
        if (log.isDebugEnabled()) {
            log.debug("Getting user info from: " + path);
        }
        try {
            OAuth2RestOperations restTemplate = this.restTemplate;
            if (null == restTemplate) {
                BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();

                resource.setClientId(this.clientId);
                //=== 使用自定义设计 ===//
                resource.setAuthenticationScheme(AuthenticationScheme.query);
                resource.setClientAuthenticationScheme(AuthenticationScheme.query);
                resource.setTokenName(this.tokenName);
                //======//

                restTemplate = new OAuth2RestTemplate(resource);
            }

            OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext().getAccessToken();
            if (null == existingToken
                    || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);

                token.setTokenType(this.tokenType);

                restTemplate.getOAuth2ClientContext().setAccessToken(token);
            }

            /*return restTemplate.getForEntity(path, Map.class).getBody();*/
            /**
             * @see RestTemplate#postForEntity(URI, Object, Class)
             */
            return restTemplate.postForEntity(path, null, Map.class).getBody();
        } catch (Exception ex) {
            log.warn(String.format("Could not fetch user details: %s, %s"
                    , ex.getClass()
                    , ex.getMessage()));

            return Collections.singletonMap("error", "Could not fetch user details");
        }
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setRestTemplate(OAuth2RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
        Assert.notNull(authoritiesExtractor, "AuthoritiesExtractor must not be null");

        this.authoritiesExtractor = authoritiesExtractor;
    }

    public void setPrincipalExtractor(PrincipalExtractor principalExtractor) {
        Assert.notNull(principalExtractor, "PrincipalExtractor must not be null");

        this.principalExtractor = principalExtractor;
    }

}
