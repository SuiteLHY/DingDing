//package github.com.suitelhy.dingding.core.domain.aggregate.security.resource;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityResourceRepository;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractAggregateModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.ObjectUtils;
//
//import javax.validation.constraints.NotNull;
//import java.util.LinkedHashSet;
//import java.util.Set;
//
///**
// * 资源树
// *
// * @Description 向下延伸一个层级.
// */
//public class ResourceTreeAggregate
//        extends AbstractAggregateModel<ResourceTreeAggregate, SecurityResource> {
//
//    @Autowired
//    private transient SecurityResourceRepository repository;
//
//    @NotNull
//    public final Set<SecurityResource> childNodes;
//
//    public ResourceTreeAggregate(@NotNull SecurityResource root) {
//        super(ResourceTreeAggregate.class, root);
//
//        this.childNodes = new LinkedHashSet<SecurityResource>(1) {
//            @Override
//            public boolean add(@NotNull SecurityResource resource) {
//                /*if (null != resource
//                        && !resource.isEmpty()
//                        && root.getCode().equals(resource.getParentCode())) {
//                    if (null != root.getParentCode()
//                            && root.getParentCode().equals(resource.getCode())) {
//                        return false;
//                    }
//                    return super.add(resource);
//                }*/
//                if (null != resource) {
//                    if (SecurityResource.Validator.RESOURCE.isChildNode(ResourceTreeAggregate.this.root, resource)) {
//                        //--- 判断为父子节点关系
//                        if (resource.isEmpty()) {
//                            repository.saveAndFlush(resource);
//                        }
//                        return repository.existsById(resource.getId())
//                                && super.add(resource);
//                    } else if (resource.isEntityLegal()) {
//                        //--- 判断为非父子节点关系, 设置为父子节点关系
//                        if (null != ResourceTreeAggregate.this.root.getParentCode()
//                                && ResourceTreeAggregate.this.root.getParentCode().equals(resource.getCode())) {
//                            return false;
//                        }
//
//                        if (resource.isEmpty()) {
//                            repository.saveAndFlush(resource);
//                        }
//                        return repository.existsById(resource.getId())
//                                && super.add(resource);
//                    }
//                }
//                return false;
//            }
//
//            @Override
//            public boolean remove(@NotNull Object o) {
//                if (super.remove(o) && o instanceof SecurityResource) {
//                    return !repository.existsByCodeAndParentCode(((SecurityResource) o).getCode(), ResourceTreeAggregate.this.root().getCode())
//                            || repository.removeByCodeAndParentCode(((SecurityResource) o).getCode(), ResourceTreeAggregate.this.root().getCode()) > 0;
//                }
//                return false;
//            }
//        };
//    }
//
//    public boolean equals(ResourceTreeAggregate aggregate) {
//        return null != aggregate
//                && this.root.equals(aggregate.root)
//                && this.childNodes.equals(aggregate.childNodes);
//    }
//
//    @Override
//    public int hashCode() {
//        return /*root.hashCode() * childNodes.hashCode()*/
//                ObjectUtils.nullSafeHashCode(new Object[]{this.root(), this.childNodes});
//    }
//
//}
