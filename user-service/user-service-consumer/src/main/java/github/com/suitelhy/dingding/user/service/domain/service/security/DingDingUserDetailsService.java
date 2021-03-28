package github.com.suitelhy.dingding.user.service.domain.service.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.AbstractSecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.user.service.api.domain.event.read.UserReadEvent;
import github.com.suitelhy.dingding.user.service.infrastructure.application.dto.BasicUserDto;
import org.apache.dubbo.config.annotation.Reference;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
@Order(Ordered.HIGHEST_PRECEDENCE)
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
            extends AbstractSecurityUser {

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

    @Reference
    private UserReadEvent userReadEvent;

    @Override
    public @NotNull UserDetails loadUserByUsername(String username)
            throws AuthenticationException
    {
        /*final @NotNull User user = userService.selectUserByUsername(username);*/
        final @NotNull User user = userReadEvent.selectUserByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("获取不到指定的用户");
        }

//        return new SecurityUser(user
//                , /*passwordEncoder*/passwordEncoder.encode(user.getPassword())
//                , securityUserService.selectRoleByUsername(user.getUsername()));

        // 自定义认证用户 - 权限
        final @NotNull Collection<GrantedAuthority> authorities = new HashSet<>(1);
        final @NotNull List<SecurityRole> roleList = userReadEvent.selectRoleOnUserByUsername(user.getUsername());
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
                ? userReadEvent.selectUserByUsername(username)
                : null;

        return (null != user && !user.isEmpty())
                ? BasicUserDto.Factory.USER_DTO.create(user)
                : null;
    }

    public @Nullable BasicUserDto getUserInfo(@NotNull UserDetails user) {
        return (null != user)
                ? getUserInfo(user.getUsername())
                : null;
    }

}
