package github.com.suitelhy.dingding.security.service.provider.domain.service.api.read.impl;

import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageRequest;
import github.com.suitelhy.dingding.core.infrastructure.domain.Pageable;
import github.com.suitelhy.dingding.core.infrastructure.domain.Sort;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityRoleReadService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityRoleService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

/**
 * (安全) 角色
 *
 * @Description (安全) 角色 - 业务接口.
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see SecurityRole
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
@Service(cluster = "failover")
public class SecurityRoleReadServiceImpl
        implements SecurityRoleReadService {

    @Autowired
    private SecurityRoleService securityRoleService;

    /**
     * 判断存在
     *
     * @param code {@linkplain SecurityRole 角色编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    public @NotNull Boolean existsByCode(@NotNull String code)
            throws IllegalArgumentException
    {
        return securityRoleService.existsByCode(code);
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
    public @NotNull PageImpl<SecurityRole> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException
    {
        final @NotNull Page<SecurityRole> result = securityRoleService.selectAll(pageIndex, pageSize);

        if (result instanceof org.springframework.data.domain.PageImpl) {
            return PageImpl.Factory.DEFAULT.create((org.springframework.data.domain.PageImpl) result);
        }

        Sort.TypedSort<SecurityRole> typedSort = Sort.sort(SecurityRole.class);
        @NotNull Sort sort = typedSort.by(SecurityRole::getCode).ascending();
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return PageImpl.Factory.DEFAULT.create(result.getContent(), page, result.getTotalElements());
    }

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize [分页 - 每页容量]
     *
     * @return [分页 - 总页数]
     */
    @Override
    public @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException
    {
        return securityRoleService.selectCount(pageSize);
    }

    /**
     * 查询指定的角色
     *
     * @param code {@linkplain SecurityRole 角色编码}
     *
     * @return {@link SecurityRole}
     */
    @Override
    public @NotNull SecurityRole selectRoleByCode(@NotNull String code)
            throws IllegalArgumentException
    {
        return securityRoleService.selectRoleByCode(code);
    }

}
