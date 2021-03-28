package github.com.suitelhy.dingding.user.service.provider.domain.service.api.read.impl;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageRequest;
import github.com.suitelhy.dingding.core.infrastructure.domain.Pageable;
import github.com.suitelhy.dingding.core.infrastructure.domain.Sort;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.service.read.UserReadService;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

/**
 * 用户 - 业务接口
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see User
 * @see UserAccountOperationInfo
 * @see Dubbo.Strategy.ClusterVo#FAILOVER
 */
@Service(cluster = "failover")
public class UserReadServiceImpl
        implements UserReadService {

    @Autowired
    private UserService userService;

    /**
     * 判断存在 -> 指定的用户
     *
     * @param username {@linkplain User.Validator#username(String) 用户名称}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     *
     * @throws IllegalArgumentException
     */
    @Override
    public boolean existUserByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return userService.existUserByUsername(username);
    }

    /**
     * 查询用户列表
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     *
     * @throws IllegalArgumentException
     */
    @Override
    public @NotNull PageImpl<User> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException
    {
        final @NotNull Page<User> result = userService.selectAll(pageIndex, pageSize);

        if (result instanceof org.springframework.data.domain.PageImpl) {
            return PageImpl.Factory.DEFAULT.create((org.springframework.data.domain.PageImpl) result);
        }

        Sort.TypedSort<User> typedSort = Sort.sort(User.class);
        @NotNull Sort sort = typedSort.by(User::getUserid).ascending()
                .and(typedSort.by(User::getStatus).ascending());
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return PageImpl.Factory.DEFAULT.create(result.getContent(), page, result.getTotalElements());
    }

    /**
     * 查询用户列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return {@linkplain Long 分页 - 总页数}
     *
     * @throws IllegalArgumentException
     */
    @Override
    public @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException
    {
        return userService.selectCount(pageSize);
    }

    /**
     * 查询指定的用户
     *
     * @param userid {@linkplain User.Validator#userid(String) [用户 ID]}
     *
     * @return {@linkplain User 指定的用户}
     *
     * @throws IllegalArgumentException
     */
    @Override
    public @NotNull User selectUserByUserid(@NotNull String userid)
            throws IllegalArgumentException
    {
        return userService.selectUserByUserid(userid);
    }

    /**
     * 查询指定的用户
     *
     * @param username {@linkplain User.Validator#userid(String) 用户名称}
     *
     * @return {@linkplain User 指定的用户}
     *
     * @throws IllegalArgumentException
     */
    @Override
    public @NotNull User selectUserByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return userService.selectUserByUsername(username);
    }

}
