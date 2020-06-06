package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.infrastructure.web.SecurityUser;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Description Spring Security <- 项目定制化 <interface>UserDetailsService</interface> 实现.
 * @author Suite
 */
@Component
public class DingDingUserDetailsService
        implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @NotNull
    @Override
    public UserDetails loadUserByUsername(String username)
            throws AuthenticationException {
        User user = userService.selectUserByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("获取不到指定的用户");
        }
        return new SecurityUser(user, passwordEncoder);
    }

}