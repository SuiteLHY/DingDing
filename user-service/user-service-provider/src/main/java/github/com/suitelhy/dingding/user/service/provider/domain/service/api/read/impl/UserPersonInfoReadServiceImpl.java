package github.com.suitelhy.dingding.user.service.provider.domain.service.api.read.impl;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageRequest;
import github.com.suitelhy.dingding.core.infrastructure.domain.Pageable;
import github.com.suitelhy.dingding.core.infrastructure.domain.Sort;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.user.service.api.domain.service.read.UserPersonInfoReadService;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserPersonInfoService;
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
 * @see UserPersonInfo
 * @see Dubbo.Strategy.ClusterVo#FAILOVER
 */
@Service(cluster = "failover")
public class UserPersonInfoReadServiceImpl
        implements UserPersonInfoReadService {

    @Autowired
    private UserPersonInfoService userPersonInfoService;

    /**
     * 查询记录列表
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @Override
    public @NotNull PageImpl<UserPersonInfo> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException
    {
        final @NotNull Page<UserPersonInfo> result = userPersonInfoService.selectAll(pageIndex, pageSize);

        if (result instanceof org.springframework.data.domain.PageImpl) {
            return PageImpl.Factory.DEFAULT.create((org.springframework.data.domain.PageImpl) result);
        }

        Sort.TypedSort<UserPersonInfo> typedSort = Sort.sort(UserPersonInfo.class);
        @NotNull Sort sort = typedSort.by(UserPersonInfo::getUsername).ascending();
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
    public @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException
    {
        return userPersonInfoService.selectCount(pageSize);
    }

    /**
     * 查询指定的记录
     *
     * @param id    {@linkplain UserPersonInfo.Validator#id(String) 数据 ID}
     *
     * @return {@link UserPersonInfo}
     */
    @Override
    public @NotNull UserPersonInfo selectById(@NotNull String id)
            throws IllegalArgumentException
    {
        return userPersonInfoService.selectById(id);
    }

    /**
     * 查询指定的记录
     *
     * @param username  {@linkplain User.Validator#username(String) 用户名称}
     *
     * @return {@link UserPersonInfo}
     */
    @Override
    public @NotNull UserPersonInfo selectByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return userPersonInfoService.selectByUsername(username);
    }

    /**
     * 查询指定的记录
     *
     * @param nickname  {@linkplain UserPersonInfo.Validator#nickname(String) 用户 - 昵称}
     *
     * @return {@link UserPersonInfo}
     */
    @Override
    public @NotNull UserPersonInfo selectByNickname(@NotNull String nickname)
            throws IllegalArgumentException
    {
        return userPersonInfoService.selectByNickname(nickname);
    }

}
