package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.web.SecurityUser;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * DingDingUserDetailsService
 *
 * @Description Spring Security <- 项目定制化 {@link UserDetailsService} 实现.
 *
 * @author Suite
 *
 * @see UserDetailsService
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DingDingUserDetailsService
        implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUserService securityUserService;

    @NotNull
    @Override
    public UserDetails loadUserByUsername(String username)
            throws AuthenticationException {
        final User user = userService.selectUserByUsername(username);

        if (null == user) {
            throw new UsernameNotFoundException("获取不到指定的用户");
        }

        return new SecurityUser(user, passwordEncoder, securityUserService);
    }

}
