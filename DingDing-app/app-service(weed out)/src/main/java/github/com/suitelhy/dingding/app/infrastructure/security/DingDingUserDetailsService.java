package github.com.suitelhy.dingding.app.infrastructure.security;

import github.com.suitelhy.dingding.app.application.task.UserTask;
import github.com.suitelhy.dingding.app.infrastructure.application.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务系统的 UserDetailsService 接口实现
 *
 * @author Suite
 */
@Component
@Slf4j
@Transactional
public class DingDingUserDetailsService
        implements UserDetailsService, SocialUserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserTask userTask;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return null;
    }

    /**
     * @param userId the user ID used to lookup the user details
     * @return the SocialUserDetails requested
     * @see UserDetailsService#loadUserByUsername(String)
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId)
            throws UsernameNotFoundException {
        return null;
    }

    /**
     * 构建用户
     *
     * @param userId
     * @return
     */
    private SocialUserDetails buildUser(String userId) {
        UserDto user = userTask.selectUserByUserid(userId);
        if (null != user) {

        }
        return null;
    }

}
