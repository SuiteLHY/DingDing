//package github.com.suitelhy.dingding.core.domain.aggregate.security.role;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleResourceRepository;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractAggregateModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.ObjectUtils;
//
//import javax.validation.constraints.NotNull;
//import java.util.LinkedHashSet;
//import java.util.Set;
//
///**
// * 角色 - 资源
// *
// * @Description 角色 - 资源 关联.
// */
//public class RoleResourceAggregate
//        extends AbstractAggregateModel<RoleResourceAggregate, SecurityRole> {
//
//    @Autowired
//    private transient SecurityRoleResourceRepository repository;
//
//    @NotNull
//    public final Set<SecurityResource> resources;
//
//    /**
//     * (构造方法)
//     *
//     * @param root
//     */
//    public RoleResourceAggregate(@NotNull SecurityRole root) {
//        super(RoleResourceAggregate.class, root);
//
//        this.resources = new LinkedHashSet<SecurityResource>(1) {
//            @Override
//            public boolean add(@NotNull SecurityResource resource) {
//                if (null != resource && !resource.isEmpty()) {
//                    if (repository.existsByRoleCodeAndResourceCode(RoleResourceAggregate.this.root().getCode(), resource.getCode())
//                            || !repository
//                            .saveAndFlush(SecurityRoleResource.Factory.ROLE_RESOURCE.create(RoleResourceAggregate.this.root().getCode(), resource.getCode()))
//                            .isEmpty()) {
//                        return super.add(resource);
//                    }
//                    return false;
//                }
//                return false;
//            }
//
//            @Override
//            public boolean remove(@NotNull Object o) {
//                if (super.remove(o) && o instanceof SecurityResource) {
//                    return !repository.existsByRoleCodeAndResourceCode(RoleResourceAggregate.this.root().getCode(), ((SecurityResource) o).getCode())
//                            || repository.removeByRoleCodeAndResourceCode(RoleResourceAggregate.this.root().getCode(), ((SecurityResource) o).getCode()) > 0;
//                }
//                return false;
//            }
//        };
//
//    }
//
//    @Override
//    public boolean equals(RoleResourceAggregate aggregate) {
//        return null != aggregate
//                && this.equalsRoot(aggregate)
//                && this.resources.equals(aggregate.resources);
//    }
//
//    @Override
//    public int hashCode() {
//        return /*root.hashCode() * resources.hashCode()*/
//                ObjectUtils.nullSafeHashCode(new Object[]{this.root(), this.resources});
//    }
//
//}
