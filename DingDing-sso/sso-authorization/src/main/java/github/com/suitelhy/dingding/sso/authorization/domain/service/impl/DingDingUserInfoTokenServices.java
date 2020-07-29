package github.com.suitelhy.dingding.sso.authorization.domain.service.impl;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
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

/**
 * Token - 用户认证业务
 *
 * @Description 自定义设计, 解决一些 Spring Security OAuth 2.0 框架的 BUG.
 *
 * @Solution
 *-> · 已解决的问题: Spring Security OAuth 2.0 实现的设计更改后, 相关的策略没有及时更新.
 *->    {@link <a href="https://github.com/spring-projects/spring-security-oauth/issues/1710">CheckTokenEndpoint should restrict to POST method only · Issue #1710 · spring-projects/spring-security-oauth</a>}
 *
 *
 * @see org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices
 */
@Slf4j
public class DingDingUserInfoTokenServices
        implements ResourceServerTokenServices {

    private final String userInfoEndpointUrl;

    private final String clientId;

    private OAuth2RestOperations restTemplate;

    private String tokenType = DefaultOAuth2AccessToken.BEARER_TYPE;

    private String tokenName = "token";

    private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();

    private PrincipalExtractor principalExtractor = new FixedPrincipalExtractor();

    //===== Constructor =====//

    public DingDingUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        this.userInfoEndpointUrl = userInfoEndpointUrl;
        this.clientId = clientId;
    }

    //==========//

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);

        if (map.containsKey("error")) {
            if (log.isDebugEnabled()) {
                log.debug("userinfo returned error: ".concat((String) map.get("error")));
            }
            throw new InvalidTokenException(accessToken);
        }

        return extractAuthentication(map);
    }

    /**
     *
     * @param map
     *
     * @return
     *
     * @see this#loadAuthentication(String)
     */
    private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
        Object principal = getPrincipal(map);
        List<GrantedAuthority> authorities = this.authoritiesExtractor.extractAuthorities(map);
        OAuth2Request request = new OAuth2Request(null, this.clientId, null
                , true, null, null
                , null, null, null);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);

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
     *
     * @return the principal or {@literal "unknown"}
     */
    protected Object getPrincipal(Map<String, Object> map) {
        Object principal = this.principalExtractor.extractPrincipal(map);
        return (principal == null ? "unknown" : principal);
    }

    /**
     * @Description 重点: 此处修改请求方式.
     *
     * @param path
     * @param accessToken
     *
     * @return
     *
     * @see this#loadAuthentication(String)
     * @see RestTemplate#postForEntity(URI, Object, Class)
     */
    @SuppressWarnings({ "unchecked" })
    private Map<String, Object> getMap(String path, String accessToken) {
        if (log.isDebugEnabled()) {
            log.debug("Getting user info from: " + path);
        }
        try {
            OAuth2RestOperations restTemplate = this.restTemplate;
            if (restTemplate == null) {
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
            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
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
            log.warn("Could not fetch user details: " + ex.getClass() + ", " + ex.getMessage());
            return Collections.<String, Object>singletonMap("error", "Could not fetch user details");
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
