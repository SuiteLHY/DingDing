package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.BasicUserDto;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.infrastructure.web.SecurityUser;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.*;

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

//        return new SecurityUser(user
//                , /*passwordEncoder*/passwordEncoder.encode(user.getPassword())
//                , securityUserService.selectRoleByUsername(user.getUsername()));

        // 自定义认证用户 - 权限
        final Collection<GrantedAuthority> authorities = new HashSet<>(1);
        final List<Map<String, Object>> roleMapList = securityUserService.selectRoleByUsername(user.getUsername());
        for (Map<String, Object> roleMap : roleMapList) {
            authorities.add(new SimpleGrantedAuthority((String) roleMap.get("role_code")));
        }

        return new SecurityUser(user.getUsername()
                , passwordEncoder.encode(user.getPassword())
                , authorities
                , !Account.StatusVo.DESTRUCTION.equals(user.getStatus())
                , !Account.StatusVo.LOCKED.equals(user.getStatus())
                , Account.StatusVo.NORMAL.equals(user.getStatus())
                , !user.isEmpty());
    }

    @Nullable
    public BasicUserDto getUserInfo(@NotNull String username) {
        final User user = User.Validator.USER.username(username)
                ? userService.selectUserByUsername(username)
                : null;

        return null != user
                ? BasicUserDto.Factory.USER_DTO.create(user)
                : null;
    }

    @Nullable
    public BasicUserDto getUserInfo(@NotNull UserDetails user) {
        return null != user
                ? getUserInfo(user.getUsername())
                : null;
    }

}
