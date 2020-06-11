package github.com.suitelhy.dingding.core.domain.aggregate.security.user;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRoleRepository;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractAggregateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 用户 - 角色
 *
 * @Description 用户 - 角色 关联.
 */
public class UserRoleAggregate
        extends AbstractAggregateModel<UserRoleAggregate, SecurityUser> {

    @Autowired
    private transient SecurityUserRoleRepository repository;

//    @NotNull
//    public final /*transient */SecurityUser securityUser;

    @NotNull
    public final Set<SecurityRole> roles;

    /*public final String userId;

    public final Account.StatusVo status;

    public final String username;*/

    /**
     * (构造方法)
     *
     * @param root
     */
    public UserRoleAggregate(@NotNull SecurityUser root) {
        super(UserRoleAggregate.class, root);

        /*this.securityUser = securityUser;*/
        this.roles = new LinkedHashSet<SecurityRole>(1) {
            @Override
            public boolean add(@NotNull SecurityRole role) {
                if (null != role && !role.isEmpty()) {
                    if (repository.existsByUsernameAndRoleCode(UserRoleAggregate.this.root().getUsername(), role.getCode())
                            || !repository
                                    .saveAndFlush(SecurityUserRole.Factory.USER_ROLE.create(UserRoleAggregate.this.root().getUsername(), role.getCode()))
                                    .isEmpty()) {
                        return super.add(role);
                    }
                    return false;
                }
                return false;
            }

            /*@Override
            public boolean addAll(@NotNull Collection<? extends SecurityRole> c) {
                if (null != c && !c.isEmpty()) {
                    for (SecurityRole each : c) {
                        *//*if (null == each || each.isEmpty()) {
                            return false;
                        }
                        return super.addAll(c);*//*
                        this.add(each);
                    }
                }
                return false;
            }*/

            @Override
            public boolean remove(@NotNull Object o) {
                if (super.remove(o) && o instanceof SecurityRole) {
                    return !repository.existsByUsernameAndRoleCode(/*securityUser*/UserRoleAggregate.this.root().getUsername(), ((SecurityRole) o).getCode())
                            || repository.removeByUsernameAndRoleCode(/*securityUser*/UserRoleAggregate.this.root().getUsername(), ((SecurityRole) o).getCode()) > 0;
                }
                return false;
            }
        };

        /*this.userId = this.securityUser.getUserId();
        this.status = this.securityUser.getStatus();
        this.username = this.securityUser.getUsername();*/
    }

    /*@Override
    public boolean equals(Object obj) {
        if (obj instanceof UserRoleAggregate) {
            return equals((UserRoleAggregate) obj);
        }
        return false;
    }*/

    @Override
    public boolean equals(UserRoleAggregate aggregate) {
        return null != aggregate
                && this.equalsRoot(aggregate)
                && this.roles.equals(aggregate.roles);
    }

    @Override
    public int hashCode() {
        return /*root.hashCode() * roles.hashCode()*/
                ObjectUtils.nullSafeHashCode(new Object[] {this.root(), this.roles});
    }

}
