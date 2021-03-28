package github.com.suitelhy.dingding.user.service.provider.domain.service.api.read.impl;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageRequest;
import github.com.suitelhy.dingding.core.infrastructure.domain.Pageable;
import github.com.suitelhy.dingding.core.infrastructure.domain.Sort;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.service.read.UserAccountOperationInfoReadService;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserAccountOperationInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

/**
 * 用户 -> 账户操作记录 - 业务接口
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see UserAccountOperationInfo
 * @see Dubbo.Strategy.ClusterVo#FAILOVER
 */
@Service(cluster = "failover")
public class UserAccountOperationInfoReadServiceImpl
        implements UserAccountOperationInfoReadService {

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    /**
     * 查询记录列表
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @Override
    public @NotNull PageImpl<UserAccountOperationInfo> selectAll(int pageIndex, int pageSize) {
        final @NotNull Page<UserAccountOperationInfo> result = userAccountOperationInfoService.selectAll(pageIndex, pageSize);

        if (result instanceof org.springframework.data.domain.PageImpl) {
            return PageImpl.Factory.DEFAULT.create((org.springframework.data.domain.PageImpl) result);
        }

        Sort.TypedSort<UserAccountOperationInfo> typedSort = Sort.sort(UserAccountOperationInfo.class);
        @NotNull Sort sort = typedSort.by(UserAccountOperationInfo::getRegistrationTime).ascending()
                .and(typedSort.by(UserAccountOperationInfo::getLastLoginTime).ascending());
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return PageImpl.Factory.DEFAULT.create(result.getContent(), page, result.getTotalElements());
    }

    /**
     * 查询记录列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @Override
    public @NotNull Long selectCount(int pageSize) {
        return userAccountOperationInfoService.selectCount(pageSize);
    }

    /**
     * 查询指定的记录
     *
     * @param id
     *
     * @return {@link UserAccountOperationInfo}
     */
    @Override
    public @NotNull UserAccountOperationInfo selectById(@NotNull String id) {
        return userAccountOperationInfoService.selectById(id);
    }

    /**
     * 查询指定的记录
     *
     * @param username
     *
     * @return {@link UserAccountOperationInfo}
     */
    @Override
    public @NotNull UserAccountOperationInfo selectByUsername(@NotNull String username) {
        return userAccountOperationInfoService.selectByUsername(username);
    }

}
