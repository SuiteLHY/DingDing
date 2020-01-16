package github.com.suitelhy.webchat.domain.service.security;

import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.domain.entity.security.SecurityUser;
import github.com.suitelhy.webchat.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * Spring Security <- 项目定制化 <interface>UserDetailsService</interface> 实现
 *
 */
@Service
public class WebchatUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @NotNull
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userService.selectUserByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("获取不到指定的合法用户");
        }
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("获取到的指定的用户非法");
        }
        return new SecurityUser(user);
    }

}
