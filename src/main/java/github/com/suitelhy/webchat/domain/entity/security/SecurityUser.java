package github.com.suitelhy.webchat.domain.entity.security;

import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.infrastructure.domain.vo.AccountVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * UserDetails 定制化实现
 *
 * @Description Spring Security 认证所需 <interface>UserDetails</interface> 实例.
 */
@Data
@Slf4j
public class SecurityUser implements UserDetails {

    /**
     * 当前用户 Entity
     */
    private final transient User user;

    public SecurityUser(@NotNull User user) {
        if (null == user || user.isEmpty()) {
            throw new IllegalArgumentException("无效的 User! -> " + user);
        }
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>(1);
        // 自定义认证用户 - 权限
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("admin");
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !AccountVo.Status.ABNORMAL.equals(user.getStatus());
    }

    @Override
    public boolean isAccountNonLocked() {
        return AccountVo.Status.NORMAL.equals(user.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 凭证未过期... 暂无业务设计
        return AccountVo.Status.NORMAL.equals(user.getStatus());
    }

    @Override
    public boolean isEnabled() {
        return AccountVo.Status.NORMAL.equals(user.getStatus());
    }

}
