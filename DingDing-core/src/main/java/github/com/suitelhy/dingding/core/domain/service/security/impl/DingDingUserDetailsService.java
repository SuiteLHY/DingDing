package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.BasicUserDto;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.infrastructure.web.AbstractSecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
 * @see AbstractSecurityUser
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DingDingUserDetailsService
        implements UserDetailsService {

    /**
     * 用户（安全认证）基本信息
     *
     * @Description {@link AbstractSecurityUser} 的项目定制化实现.
     *
     * @Design {@link AbstractSecurityUser}
     * · 【安全设计】使用[静态的嵌套类]能够有效地限制[对 {@link SecurityUser} 的滥用].
     *
     * @see AbstractSecurityUser
     */
    public static class SecurityUser
            extends AbstractSecurityUser
    {

        /**
         * (Constructor)
         *
         * @param username
         * @param password
         * @param authorities
         * @param accountNonExpired     {@link this#isAccountNonExpired()}
         * @param accountNonLocked      {@link this#isAccountNonLocked()}
         * @param credentialsNonExpired {@link this#isCredentialsNonExpired()}
         * @param enabled               {@link this#isEnabled()}
         *
         * @throws AccountStatusException
         * @throws IllegalArgumentException
         */
        protected SecurityUser(@NotNull String username
                , @NotNull String password
                , @NotNull Collection<? extends GrantedAuthority> authorities
                , boolean accountNonExpired
                , boolean accountNonLocked
                , boolean credentialsNonExpired
                , boolean enabled)
                throws AccountStatusException, IllegalArgumentException
        {
            super(username, password, authorities
                    , accountNonExpired, accountNonLocked, credentialsNonExpired
                    , enabled);
        }

    }

    /*@Autowired
    private UserService userService;

    @Autowired
    private SecurityUserService securityUserService;*/

    @Autowired
    private UserEvent userEvent;

    @Override
    public @NotNull UserDetails loadUserByUsername(String username)
            throws AuthenticationException
    {
        /*final @NotNull User user = userService.selectUserByUsername(username);*/
        final @NotNull User user = userEvent.selectUserByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("获取不到指定的用户");
        }

//        return new SecurityUser(user
//                , /*passwordEncoder*/passwordEncoder.encode(user.getPassword())
//                , securityUserService.selectRoleByUsername(user.getUsername()));

        // 自定义认证用户 - 权限
        final Collection<GrantedAuthority> authorities = new HashSet<>(1);
        final @NotNull List<SecurityRole> roleList = userEvent.selectRoleOnUserByUsername(user.getUsername());
        for (@NotNull SecurityRole role : roleList) {
            authorities.add(new SimpleGrantedAuthority(role.getCode()));
        }

        return new SecurityUser(user.getUsername()
                , /*passwordEncoder.encode(user.getPassword())*/user.getPassword()
                , authorities
                , !Account.StatusVo.DESTRUCTION.equals(user.getStatus())
                , !Account.StatusVo.LOCKED.equals(user.getStatus())
                , Account.StatusVo.NORMAL.equals(user.getStatus())
                , !user.isEmpty());
    }

    public @Nullable BasicUserDto getUserInfo(@NotNull String username) {
        final @NotNull User user = User.Validator.USER.username(username)
                ? userEvent.selectUserByUsername(username)
                : null;

        return (null != user && ! user.isEmpty())
                ? BasicUserDto.Factory.USER_DTO.create(user)
                : null;
    }

    public @Nullable BasicUserDto getUserInfo(@NotNull UserDetails user) {
        return (null != user)
                ? getUserInfo(user.getUsername())
                : null;
    }

}
