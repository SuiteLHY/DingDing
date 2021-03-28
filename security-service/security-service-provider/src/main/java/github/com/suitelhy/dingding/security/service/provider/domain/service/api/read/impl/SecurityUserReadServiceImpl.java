package github.com.suitelhy.dingding.security.service.provider.domain.service.api.read.impl;

import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageRequest;
import github.com.suitelhy.dingding.core.infrastructure.domain.Pageable;
import github.com.suitelhy.dingding.core.infrastructure.domain.Sort;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityRoleReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserRoleReadService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

/**
 * (安全) 用户 - 业务
 *
 * @Description (安全) 用户 - 业务接口.
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see SecurityUser
 * @see SecurityRoleReadService
 * @see SecurityUserRoleReadService
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
@Service(cluster = "failover")
public class SecurityUserReadServiceImpl
        implements SecurityUserReadService {

    @Autowired
    private SecurityUserService securityUserService;

    /**
     * 判断存在
     *
     * @param userId {@linkplain SecurityUser.Validator#userId(String)} [用户 ID]}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     *
     * @throws IllegalArgumentException
     */
    @Override
    public boolean existByUserId(@NotNull String userId) throws IllegalArgumentException {
        return securityUserService.existByUserId(userId);
    }

    /**
     * 判断存在
     *
     * @param username {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     *
     * @throws IllegalArgumentException
     */
    @Override
    public boolean existByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return securityUserService.existByUsername(username);
    }

    /**
     * 是否具有管理员权限
     *
     * @param username {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    public boolean existAdminPermission(@NotNull String username)
            throws IllegalArgumentException
    {
        return securityUserService.existAdminPermission(username);
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
    public @NotNull PageImpl<SecurityUser> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException
    {
        final @NotNull Page<SecurityUser> result = securityUserService.selectAll(pageIndex, pageSize);

        if (result instanceof org.springframework.data.domain.PageImpl) {
            return PageImpl.Factory.DEFAULT.create((org.springframework.data.domain.PageImpl) result);
        }

        Sort.TypedSort<SecurityUser> typedSort = Sort.sort(SecurityUser.class);
        @NotNull Sort sort = typedSort.by(SecurityUser::getUserId).ascending()
                .and(typedSort.by(SecurityUser::getStatus).ascending());
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
        return securityUserService.selectCount(pageSize);
    }

    /**
     * 查询
     *
     * @param userId {@linkplain SecurityUser.Validator#userId(String) [用户 ID]}
     *
     * @return {@link SecurityUser}
     */
    @Override
    public @NotNull SecurityUser selectByUserId(@NotNull String userId)
            throws IllegalArgumentException
    {
        return securityUserService.selectByUserId(userId);
    }

    /**
     * 查询
     *
     * @param username {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@link SecurityUser}
     */
    @Override
    public @NotNull SecurityUser selectByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return securityUserService.selectByUsername(username);
    }

}
