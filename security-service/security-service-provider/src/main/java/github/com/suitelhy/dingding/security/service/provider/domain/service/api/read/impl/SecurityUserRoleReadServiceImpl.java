package github.com.suitelhy.dingding.security.service.provider.domain.service.api.read.impl;

import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageRequest;
import github.com.suitelhy.dingding.core.infrastructure.domain.Pageable;
import github.com.suitelhy.dingding.core.infrastructure.domain.Sort;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserRoleReadService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserRoleService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * （安全认证）用户 ←→ 角色
 *
 * @Description [（安全认证）用户 ←→ 角色]关联关系 - 业务接口.
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see SecurityUserRole
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
@Service(cluster = "failover")
public class SecurityUserRoleReadServiceImpl
        implements SecurityUserRoleReadService {

    @Autowired
    private SecurityUserRoleService securityUserRoleService;

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @Override
    public @NotNull PageImpl<SecurityUserRole> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException
    {
        final @NotNull Page<SecurityUserRole> result = securityUserRoleService.selectAll(pageIndex, pageSize);

        if (result instanceof org.springframework.data.domain.PageImpl) {
            return PageImpl.Factory.DEFAULT.create((org.springframework.data.domain.PageImpl) result);
        }

        Sort.TypedSort<SecurityUserRole> typedSort = Sort.sort(SecurityUserRole.class);
        @NotNull Sort sort = typedSort.by(SecurityUserRole::getRoleCode).ascending();
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
    public @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException
    {
        return securityUserRoleService.selectCount(pageSize);
    }

    /**
     * 查询
     *
     * @param username  {@linkplain SecurityUserRole.Validator#username(String) 用户名称}
     *
     * @return {@link SecurityUserRole}
     */
    @Override
    public @NotNull List<SecurityUserRole> selectByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return securityUserRoleService.selectByUsername(username);
    }

    /**
     * 查询
     *
     * @param roleCode {@linkplain SecurityUserRole.Validator#roleCode(String) 角色编码}
     *
     * @return {@link SecurityUserRole}
     */
    @Override
    public @NotNull List<SecurityUserRole> selectByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException
    {
        return securityUserRoleService.selectByRoleCode(roleCode);
    }

    /**
     * 查询
     *
     * @param username {@linkplain SecurityUserRole.Validator#username(String) 用户名称}
     * @param roleCode {@linkplain SecurityUserRole.Validator#roleCode(String) 角色编码}
     *
     * @return {@link SecurityUserRole}
     */
    @Override
    public @NotNull SecurityUserRole selectByUsernameAndRoleCode(@NotNull String username, @NotNull String roleCode)
            throws IllegalArgumentException
    {
        return securityUserRoleService.selectByUsernameAndRoleCode(username, roleCode);
    }

}
