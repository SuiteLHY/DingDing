package github.com.suitelhy.dingding.core.domain.aggregate.security.user;

import github.com.suitelhy.dingding.core.domain.aggregate.security.role.RoleResource;
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
public class UserRoleResource {

    @NotNull
    public final transient SecurityUser securityUser;

    @NotNull
    public final Set<RoleResource> roleResources;

    public final String userId;

    public final Account.StatusVo status;

    public final String username;

    /**
     * (构造方法)
     *
     * @param securityUser
     */
    public UserRoleResource(@NotNull SecurityUser securityUser) {
        if (null == securityUser || securityUser.isEmpty()) {
            //-- 非法输入: 用户
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 用户");
        }

        this.securityUser = securityUser;
        this.roleResources = new LinkedHashSet<RoleResource>(1) {
            @Override
            public boolean add(@NotNull RoleResource roleResource) {
                if (null != roleResource && !roleResource.role.isEmpty()) {
                    return super.add(roleResource);
                }
                return false;
            }

            @Override
            public boolean addAll(@NotNull Collection<? extends RoleResource> c) {
                if (null != c && !c.isEmpty()) {
                    for (RoleResource each : c) {
                        if (null == each || each.role.isEmpty()) {
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
        if (obj instanceof UserRoleResource) {
            return equals((UserRoleResource) obj);
        }
        return false;
    }

    public boolean equals(UserRoleResource userRoleResource) {
        if (null != userRoleResource
                && this.securityUser.equals(userRoleResource.securityUser)
                && this.roleResources.equals(userRoleResource.roleResources)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return securityUser.hashCode() * roleResources.hashCode();
    }

}
