package github.com.suitelhy.dingding.sso.server.infrastructure.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.log.service.api.domain.entity.Log;
import github.com.suitelhy.dingding.log.service.api.domain.service.write.idempotence.LogIdempotentWriteService;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.sso.server.domain.service.security.DingDingUserDetailsService;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.event.read.UserReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * [用户登录成功]的事件 <- 监听器
 *
 * @Description 针对[用户通过账户密码登录成功]的 {@linkplain AuthenticationSuccessEvent 安全认证事件} 的监听器.
 *
 * @Reference
 * {@linkplain <a href="https://blog.csdn.net/fsdf8sad7/article/details/100535639">spring-security-oauth2 登录或者认证成功后 做一些操作， 比如登录日志。_fsdf8sad7的博客-CSDN博客</a> 参考文章}
 *
 * @author Suite
 */
@Component
@Slf4j
public class AuthenticationSuccessEventListener
        implements ApplicationListener<AuthenticationSuccessEvent> {

    @Reference
    private LogIdempotentWriteService logNonIdempotentWriteService;

    @Reference
    private UserReadEvent userReadEvent;

    /**
     * Handle an application event.
     *
     * @Description The event maybe belong to {@linkplain org.springframework.security.authentication.UsernamePasswordAuthenticationToken the event of a username and password}
     * or {@linkplain org.springframework.security.oauth2.provider.OAuth2Authentication OAuth 2 authentication token}.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {

        final org.springframework.security.authentication.UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
        if (event.getSource() instanceof org.springframework.security.authentication.UsernamePasswordAuthenticationToken) {
            usernamePasswordAuthenticationToken = (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) event.getSource();
        } else {
            return;
        }

        final DingDingUserDetailsService.SecurityUser securityUser;
        if (usernamePasswordAuthenticationToken.getPrincipal() instanceof DingDingUserDetailsService.SecurityUser) {
            securityUser = (DingDingUserDetailsService.SecurityUser) usernamePasswordAuthenticationToken.getPrincipal();
        } else {
            return;
        }

        //=== 操作日志记录 ===//

        final @NotNull User thisUser = userReadEvent.selectUserByUsername(securityUser.getUsername());
        final @NotNull SecurityUser operator;
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo;

        if (thisUser.isEmpty()) {
            throw new BadCredentialsException(String.format("[OAuth 2 认证标准的安全凭据%s]无效", thisUser));
        }
        operator = userReadEvent.selectSecurityUserByUsername(thisUser.getUsername());
        if (operator.isEmpty()) {
            throw new BadCredentialsException(String.format("[OAuth 2 认证标准的安全凭据%s]无效", operator));
        }
        operator_UserAccountOperationInfo = userReadEvent.selectUserAccountOperationInfoByUsername(thisUser.getUsername());
        if (operator_UserAccountOperationInfo.isEmpty()) {
            throw new BadCredentialsException(String.format("[OAuth 2 认证标准的安全凭据%s]无效", operator_UserAccountOperationInfo));
        }

        final @NotNull Log newLog_LoginSuccess = Log.Factory.User.LOG.create(null
                , null
                , HandleType.LogVo.USER__LOGIN
                , thisUser
                , operator
                , operator_UserAccountOperationInfo);
        if (! logNonIdempotentWriteService.insert(newLog_LoginSuccess)) {
            throw new AuthenticationServiceException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__LOGIN.name
                    , "生成操作日志记录"
                    , newLog_LoginSuccess
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //======//

    }

}
