package github.com.suitelhy.dingding.security.service.provider.domain.service.api.read.impl;

import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageRequest;
import github.com.suitelhy.dingding.core.infrastructure.domain.Pageable;
import github.com.suitelhy.dingding.core.infrastructure.domain.Sort;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRoleResource;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityRoleResourceReadService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityRoleResourceService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * （安全认证）角色 ←→ 资源
 *
 * @Description [（安全认证）角色 ←→ 资源]关联关系 - 业务接口.
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see SecurityRoleResource
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
@Service(cluster = "failover")
public class SecurityRoleResourceReadServiceImpl
        implements SecurityRoleResourceReadService {

    @Autowired
    private SecurityRoleResourceService securityRoleResourceService;

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @param resourceCode {@linkplain SecurityRoleResource.Validator#resourceCode(String) 资源编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    public boolean existRoleByResourceCode(@NotNull String resourceCode) {
        return securityRoleResourceService.existRoleByResourceCode(resourceCode);
    }

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @param roleCode {@linkplain SecurityRoleResource.Validator#roleCode(String) 角色编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    public boolean existResourceByRoleCode(@NotNull String roleCode) {
        return securityRoleResourceService.existResourceByRoleCode(roleCode);
    }

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @Override
    public @NotNull PageImpl<SecurityRoleResource> selectAll(int pageIndex, int pageSize) {
        final @NotNull Page<SecurityRoleResource> result = securityRoleResourceService.selectAll(pageIndex, pageSize);

        if (result instanceof org.springframework.data.domain.PageImpl) {
            return PageImpl.Factory.DEFAULT.create((org.springframework.data.domain.PageImpl) result);
        }

        Sort.TypedSort<SecurityRoleResource> typedSort = Sort.sort(SecurityRoleResource.class);
        @NotNull Sort sort = typedSort.by(SecurityRoleResource::getRoleCode).ascending()
                .and(typedSort.by(SecurityRoleResource::getResourceCode).ascending());
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return PageImpl.Factory.DEFAULT.create(result.getContent(), page, result.getTotalElements());
    }

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @Override
    public @NotNull Long selectCount(int pageSize) {
        return securityRoleResourceService.selectCount(pageSize);
    }

    /**
     * 查询
     *
     * @param resourceCode {@linkplain SecurityRoleResource.Validator#resourceCode(String) 资源编码}
     *
     * @return {@link SecurityRoleResource}
     */
    @Override
    public @NotNull List<SecurityRoleResource> selectByResourceCode(@NotNull String resourceCode) {
        return securityRoleResourceService.selectByResourceCode(resourceCode);
    }

    /**
     * 查询
     *
     * @param roleCode {@linkplain SecurityRoleResource.Validator#roleCode(String) 角色编码}
     *
     * @return {@link SecurityRoleResource}
     */
    @Override
    public @NotNull List<SecurityRoleResource> selectByRoleCode(@NotNull String roleCode) {
        return securityRoleResourceService.selectByRoleCode(roleCode);
    }

}
