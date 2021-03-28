//package github.com.suitelhy.dingding.sso.server.web.security;
//
//import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
//import github.com.suitelhy.dingding.log.service.api.domain.entity.Log;
//import github.com.suitelhy.dingding.log.service.api.domain.service.write.idempotence.LogIdempotentWriteService;
//import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
//import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
//import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
//import github.com.suitelhy.dingding.security.service.api.infrastructure.web.util.OAuth2AuthenticationUtil;
//import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
//import github.com.suitelhy.dingding.user.service.api.domain.event.read.UserReadEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.config.annotation.Reference;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.InsufficientAuthenticationException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.constraints.NotNull;
//import java.io.IOException;
//
///**
// * DingDing - 登录成功处理器
// *
// * @Description 项目自定义处理器.
// *
// * @see AuthenticationSuccessHandler
// */
//@Component
//@Slf4j
//public class DingDingLoginSuccessHandler
//        implements AuthenticationSuccessHandler {
//
//    @Reference
//    private LogIdempotentWriteService logNonIdempotentWriteService;
//
//    @Reference
//    private UserReadEvent userReadEvent;
//
//    /**
//     * Called when a user has been successfully authenticated.
//     *
//     * @param request        the request which caused the successful authentication
//     * @param response       the response
//     * @param authentication the <tt>Authentication</tt> object which was created during
//     */
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//            throws IOException, ServletException
//    {
//        if (! (authentication instanceof OAuth2Authentication)) {
//            throw new InsufficientAuthenticationException("非[OAuth 2 认证标准的安全凭据]");
//        }
//
//        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails((OAuth2Authentication) authentication);
//
//        if (userAuthenticationDetails.isActive()) {
//            //=== 操作日志记录 ===//
//
//            final @NotNull User thisUser = userReadEvent.selectUserByUsername(userAuthenticationDetails.getUserName());
//            final @NotNull SecurityUser operator;
//            final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo;
//
//            if (thisUser.isEmpty()) {
//                throw new BadCredentialsException(String.format("[OAuth 2 认证标准的安全凭据%s]无效", thisUser));
//            }
//            operator = userReadEvent.selectSecurityUserByUsername(thisUser.getUsername());
//            if (operator.isEmpty()) {
//                throw new BadCredentialsException(String.format("[OAuth 2 认证标准的安全凭据%s]无效", operator));
//            }
//            operator_UserAccountOperationInfo = userReadEvent.selectUserAccountOperationInfoByUsername(thisUser.getUsername());
//            if (operator_UserAccountOperationInfo.isEmpty()) {
//                throw new BadCredentialsException(String.format("[OAuth 2 认证标准的安全凭据%s]无效", operator_UserAccountOperationInfo));
//            }
//
//            final @NotNull Log newLog_LoginSuccess = Log.Factory.User.LOG.create(null
//                    , null
//                    , HandleType.LogVo.USER__LOGIN
//                    , thisUser
//                    , operator
//                    , operator_UserAccountOperationInfo);
//            if (! logNonIdempotentWriteService.insert(newLog_LoginSuccess)) {
//                throw new AuthenticationServiceException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , HandleType.LogVo.USER__LOGIN.name
//                        , "生成操作日志记录"
//                        , newLog_LoginSuccess
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            //======//
//        } else {
//            throw new BadCredentialsException("[OAuth 2 认证标准的安全凭据]无效");
//        }
//    }
//
//}
