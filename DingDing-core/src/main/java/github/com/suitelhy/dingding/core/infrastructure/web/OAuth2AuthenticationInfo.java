package github.com.suitelhy.dingding.core.infrastructure.web;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;

/**
 * 身份验证令牌信息
 *
 * @see org.springframework.security.oauth2.provider.OAuth2Authentication
 */
public final class OAuth2AuthenticationInfo {

    /**
     * 用户认证凭据
     *
     * @see org.springframework.security.oauth2.provider.OAuth2Authentication#getUserAuthentication()
     */
    public static abstract class AbstractUserAuthentication {

        /**
         * 详细信息
         *
         * @see org.springframework.security.core.Authentication#getDetails()
         */
        public static abstract class AbstractDetails {

            /**
             * (Constructor)
             *
             * @param active      是否处于活动状态
             * @param authorities 权限集合
             * @param clientId    (凭证对应的)客户端编号
             * @param scope       可操作范围
             * @param userName    (凭证对应的)用户名称
             * @throws BadCredentialsException             非法的凭证参数
             * @throws InsufficientAuthenticationException 不满足构建[用户认证凭据 - 详细信息]的必要条件
             * @Description 限制 {@link OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails} 实现类的构造.
             * @see this#isActive()
             * @see this#getAuthorities()
             * @see this#getClientId()
             * @see this#getScope()
             * @see this#getUserName()
             */
            protected AbstractDetails(Boolean active, @NotNull Collection<String> authorities, @NotNull String clientId
                    , @NotNull Collection<String> scope, @NotNull String userName)
                    throws BadCredentialsException, InsufficientAuthenticationException, IllegalArgumentException {
                if (null == authorities) {
                    //-- 非法参数: [权限集合]
                    throw new BadCredentialsException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[权限集合]"
                            , authorities
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber())) {
                    };
                }
                if (!SecurityResourceUrl.Validator.RESOURCE_URL.clientId(clientId)) {
                    //-- 非法参数: [(凭证对应的)客户端编号]
                    throw new InsufficientAuthenticationException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[(凭证对应的)客户端编号]"
                            , clientId
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber())) {
                    };
                }
                if (null == scope) {
                    //-- 非法参数: [可操作范围]
                    throw new BadCredentialsException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[可操作范围]"
                            , scope
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber())) {
                    };
                }
                if (!SecurityUser.Validator.USER.username(userName)) {
                    //-- 非法参数: [(凭证对应的)用户名称]
                    throw new InsufficientAuthenticationException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[(凭证对应的)用户名称]"
                            , userName
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber())) {
                    };
                }
            }

            //===== Getter And Is Methods =====//

            public abstract Boolean isActive();

            public abstract @NotNull
            Collection<String> getAuthorities();

            public abstract @NotNull
            String getClientId();

            public abstract @NotNull
            Collection<String> getScope();

            public abstract @NotNull
            String getUserName();

            //==========//

        }

        /**
         * 获取[用户认证凭据 - 详细信息]
         *
         * @param userAuthentication 用户认证凭据
         * @return [用户认证凭据 - 详细信息]
         * @throws AuthenticationCredentialsNotFoundException 此时 {@param userAuthentication} 非法
         * @throws BadCredentialsException                    此时 {@param userAuthentication} 无效, 或无法从 {@param userAuthentication} 中提取有效的 {@link AbstractDetails}
         * @throws IllegalArgumentException                   此时 {@param userAuthentication} 无效, 或无法从 {@param userAuthentication} 中提取有效的 {@link AbstractDetails}
         * @see AbstractDetails
         */
        public static @NotNull
        AbstractDetails getDetails(@NotNull org.springframework.security.core.Authentication userAuthentication)
                throws AuthenticationCredentialsNotFoundException, BadCredentialsException, IllegalArgumentException {
            if (null == userAuthentication) {
                //-- 非法参数: 无效的[用户认证凭据]
                throw new AuthenticationCredentialsNotFoundException("非法参数: 找不到[用户认证凭据]");
            }
            if (!userAuthentication.isAuthenticated()) {
                //-- 非法参数: 无效的[用户认证凭据]
                throw new BadCredentialsException("非法参数: 无效的[用户认证凭据]");
            }
            if (!(userAuthentication.getDetails() instanceof Map)) {
                //-- 非法参数: 无效的[用户认证凭据]
                throw new BadCredentialsException("非法参数: 无效的[用户认证凭据]");
            }
            if (!(((Map<?, ?>) userAuthentication.getDetails()).get("oAuth2AuthenticationInfo_AbstractUserAuthentication_AbstractDetails") instanceof AbstractDetails)) {
                //-- 非法参数: 无效的[用户认证凭据] <- 无效的[用户认证凭据 - 详细信息]
                throw new BadCredentialsException("非法参数: 无效的[用户认证凭据] <- 无效的[用户认证凭据 - 详细信息]");
            }

            return (AbstractDetails) ((Map<?, ?>) userAuthentication.getDetails())
                    .get("oAuth2AuthenticationInfo_AbstractUserAuthentication_AbstractDetails");
        }

    }

//    /**
//     * 获取[用户认证凭据]
//     *
//     * @Description 从[身份验证信息]中获取[用户认证凭据].
//     *
//     * @param oAuth2Authentication  基于令牌 (Token) 的[身份验证信息]
//     *
//     * @return [身份验证信息 - 用户认证凭据]
//     */
//    public static @NotNull Authentication getUserAuthentication(AbstractAuthenticationToken oAuth2Authentication)
//            throws AuthenticationCredentialsNotFoundException, BadCredentialsException, IllegalArgumentException
//    {
//        if (null == oAuth2Authentication) {
//            //-- 非法参数: [身份验证信息]
//            throw new AuthenticationCredentialsNotFoundException("非法参数: 找不到[身份验证信息]");
//        }
//        if (!oAuth2Authentication.isAuthenticated()) {
//            //-- 非法参数: 无效的[身份验证信息]
//            throw new BadCredentialsException("非法参数: 无效的[身份验证信息]");
//        }
//
//        return (Authentication) oAuth2Authentication.getPrincipal();
//    }

}
