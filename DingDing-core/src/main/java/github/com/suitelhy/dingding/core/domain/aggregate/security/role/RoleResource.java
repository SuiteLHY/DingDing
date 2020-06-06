package github.com.suitelhy.dingding.core.domain.aggregate.security.role;

import github.com.suitelhy.dingding.core.domain.entity.security.Resource;
import github.com.suitelhy.dingding.core.domain.entity.security.Role;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 角色 - 资源
 *
 * @Description 角色 - 资源 关联.
 */
public class RoleResource {

    @NotNull
    public final transient Role role;

    @NotNull
    public final Set<Resource> resources;

    public final Long id;

    public final String code;

    public final String name;

    /**
     * (构造方法)
     *
     * @param role
     */
    public RoleResource(@NotNull Role role) {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: 角色
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 角色");
        }

        this.role = role;
        this.resources = new LinkedHashSet<Resource>(1) {
            @Override
            public boolean add(@NotNull Resource resource) {
                if (null != resource && !resource.isEmpty()) {
                    return super.add(resource);
                }
                return false;
            }

            @Override
            public boolean addAll(@NotNull Collection<? extends Resource> c) {
                if (null != c && !c.isEmpty()) {
                    for (Resource each : c) {
                        if (null == each || each.isEmpty()) {
                            return false;
                        }
                        return super.addAll(c);
                    }
                }
                return false;
            }
        };

        this.id = this.role.getId();
        this.code = this.role.getCode();
        this.name = this.role.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RoleResource) {
            return equals((RoleResource) obj);
        }
        return false;
    }

    public boolean equals(RoleResource roleResource) {
        if (null != roleResource
                && this.role.equals(roleResource.role)
                && this.resources.equals(roleResource.resources)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return role.hashCode() * resources.hashCode();
    }

}
