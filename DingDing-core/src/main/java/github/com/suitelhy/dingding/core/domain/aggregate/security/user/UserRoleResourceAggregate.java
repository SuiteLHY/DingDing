package github.com.suitelhy.dingding.core.domain.aggregate.security.user;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRoleRepository;
import github.com.suitelhy.dingding.core.domain.aggregate.security.role.RoleResourceAggregate;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractAggregateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 用户 - 角色
 *
 * @Description 用户 - 角色 关联.
 */
public class UserRoleResourceAggregate
        extends AbstractAggregateModel<UserRoleResourceAggregate, SecurityUser> {

    @Autowired
    private transient SecurityUserRoleRepository repository;

    @NotNull
    public final Set<RoleResourceAggregate> roleResources;

    /**
     * (构造方法)
     *
     * @param root
     */
    public UserRoleResourceAggregate(@NotNull SecurityUser root) {
        super(UserRoleResourceAggregate.class, root);

        this.roleResources = new LinkedHashSet<RoleResourceAggregate>(1) {
            @Override
            public boolean add(@NotNull RoleResourceAggregate roleResource) {
                if (null != roleResource && !roleResource.isEmpty()) {
                    String username = UserRoleResourceAggregate.this.root().getUsername();
                    String roleCode = roleResource.root().getCode();
                    if (repository.existsByUsernameAndRoleCode(username, roleCode)
                            || !repository.saveAndFlush(SecurityUserRole.Factory.USER_ROLE.create(username, roleCode))
                                    .isEmpty()) {
                        return super.add(roleResource);
                    }
                    return false;
                }
                return false;
            }

            @Override
            public boolean remove(@NotNull Object o) {
                if (super.remove(o) && o instanceof RoleResourceAggregate) {
                    String username = UserRoleResourceAggregate.this.root().getUsername();
                    String roleCode = ((RoleResourceAggregate) o).root().getCode();
                    return !repository.existsByUsernameAndRoleCode(username, roleCode)
                            || repository.removeByUsernameAndRoleCode(username, roleCode) > 0;
                }

                if (o instanceof SecurityRole) {
                    for (RoleResourceAggregate each : roleResources) {
                        if (each.equalsRoot((SecurityRole) o)) {
                            return remove(each);
                        }
                    }
                }
                return false;
            }
        };

    }

    @Override
    public boolean equals(UserRoleResourceAggregate aggregate) {
        return null != aggregate
                && this.equalsRoot(aggregate)
                && this.roleResources.equals(aggregate.roleResources);
    }

    /**
     * 根节点 <- 等效比较
     *
     * @param root
     * @return
     */
    public boolean equalsRoot(@NotNull SecurityUser root) {
        return root().equals(root);
    }

    @Override
    public int hashCode() {
        return /*root().hashCode() * roleResources.hashCode()*/
                ObjectUtils.nullSafeHashCode(new Object[] {this.root(), this.roleResources});
    }

}
