package github.com.suitelhy.dingding.core.domain.aggregate.security.user;

import github.com.suitelhy.dingding.core.domain.entity.security.Role;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 用户 - 角色
 *
 * @Description 用户 - 角色 关联.
 */
public class UserRole {

    @NotNull
    public final transient SecurityUser securityUser;

    @NotNull
    public final Set<Role> roles;

    public final String userId;

    public final Account.StatusVo status;

    public final String username;

    /**
     * (构造方法)
     *
     * @param securityUser
     */
    public UserRole(@NotNull SecurityUser securityUser) {
        if (null == securityUser || securityUser.isEmpty()) {
            //-- 非法输入: 用户
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 用户");
        }

        this.securityUser = securityUser;
        this.roles = new LinkedHashSet<Role>(1) {
            @Override
            public boolean add(@NotNull Role role) {
                if (null != role && !role.isEmpty()) {
                    return super.add(role);
                }
                return false;
            }

            @Override
            public boolean addAll(@NotNull Collection<? extends Role> c) {
                if (null != c && !c.isEmpty()) {
                    for (Role each : c) {
                        if (null == each || each.isEmpty()) {
                            return false;
                        }
                        return super.addAll(c);
                    }
                }
                return false;
            }
        };

        this.userId = this.securityUser.getUserId();
        this.status = this.securityUser.getStatus();
        this.username = this.securityUser.getUsername();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserRole) {
            return equals((UserRole) obj);
        }
        return false;
    }

    public boolean equals(UserRole userRole) {
        if (null != userRole
                && this.securityUser.equals(userRole.securityUser)
                && this.roles.equals(userRole.roles)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return securityUser.hashCode() * roles.hashCode();
    }

}
