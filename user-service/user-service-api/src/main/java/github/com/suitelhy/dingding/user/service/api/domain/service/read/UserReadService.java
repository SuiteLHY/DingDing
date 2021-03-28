package github.com.suitelhy.dingding.user.service.api.domain.service.read;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;

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
public interface UserReadService {

    //===== 查询操作 =====//

    /**
     * 判断存在 -> 指定的用户
     *
     * @param username {@linkplain User.Validator#username(String) 用户名称}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     *
     * @throws IllegalArgumentException
     */
    boolean existUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

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
    @NotNull PageImpl<User> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询用户列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return {@linkplain Long 分页 - 总页数}
     *
     * @throws IllegalArgumentException
     */
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询指定的用户
     *
     * @param userid    {@linkplain User.Validator#userid(String) [用户 ID]}
     *
     * @return {@linkplain User 指定的用户}
     *
     * @throws IllegalArgumentException
     */
    @NotNull User selectUserByUserid(@NotNull String userid)
            throws IllegalArgumentException;

    /**
     * 查询指定的用户
     *
     * @param username  {@linkplain User.Validator#userid(String) 用户名称}
     *
     * @return {@linkplain User 指定的用户}
     *
     * @throws IllegalArgumentException
     */
    @NotNull User selectUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

}
