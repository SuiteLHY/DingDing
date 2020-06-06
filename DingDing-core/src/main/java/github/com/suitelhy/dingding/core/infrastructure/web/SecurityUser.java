package github.com.suitelhy.dingding.core.infrastructure.web;

import github.com.suitelhy.dingding.core.infrastructure.application.dto.BasicUserDto;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * UserDetails 定制化实现
 *
 * @Description Spring Security 认证所需 <interface>UserDetails</interface> 实例.
 */
@Slf4j
public class SecurityUser
        implements UserDetails {

    @NotNull
    private final transient PasswordEncoder passwordEncoder;

    /**
     * 当前用户 Entity
     */
    @NotNull
    private final transient User user;

    public SecurityUser(@NotNull User user, @NotNull PasswordEncoder passwordEncoder)
            throws AccountStatusException, IllegalArgumentException {
        if (null == user) {
            throw new IllegalArgumentException("无效的 User -> null");
        }
        if (null == passwordEncoder) {
            throw new IllegalArgumentException("无效的 PasswordEncoder -> null");
        }
        if (user.isEmpty()) {
            if (User.Validator.USER.validateId(user)
                    && user.isEntityLegal()
                    && !Boolean.FALSE.equals(user.isEntityPersistence())) {
                if (Account.StatusVo.DESTRUCTION.equals(user.getStatus())) {
                    throw new AccountExpiredException("用户已注销");
                }
                if (Account.StatusVo.ABNORMAL.equals(user.getStatus())) {
                    throw new DisabledException("用户异常");
                }
                if (Account.StatusVo.LOCKED.equals(user.getStatus())) {
                    throw new LockedException("用户异常");
                }
            }
            throw new IllegalArgumentException("无效的 User -> " + user);
        }
        this.passwordEncoder = passwordEncoder;
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>(1);
        // 自定义认证用户 - 权限
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("Admin");
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return null == user.getPassword()
                ? null
                : passwordEncoder.encode(user.getPassword());
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /*@NotNull
    public UserDto getUserDto() {
        return UserDto.Factory.USER_DTO.create(this.user);
    }*/

    @NotNull
    public BasicUserDto getUserInfo() {
        return BasicUserDto.Factory.USER_DTO.create(this.user);
    }

    /**
     * 账户是否未过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return !Account.StatusVo.DESTRUCTION.equals(user.getStatus());
    }

    /**
     * 账户是否未被锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return !Account.StatusVo.LOCKED.equals(user.getStatus());
    }

    /**
     * 账户是否凭证未过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // 凭证未过期... 暂无业务设计
        return Account.StatusVo.NORMAL.equals(user.getStatus());
    }

    /**
     * 账户是否可用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return !user.isEmpty();
    }

}
