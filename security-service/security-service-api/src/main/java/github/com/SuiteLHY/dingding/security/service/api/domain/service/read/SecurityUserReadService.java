package github.com.suitelhy.dingding.security.service.api.domain.service.read;

import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
//import org.springframework.data.domain.Page;

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
public interface SecurityUserReadService {

    /**
     * 判断存在
     *
     * @param userId    {@linkplain SecurityUser.Validator#userId(String)} [用户 ID]}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     *
     * @throws IllegalArgumentException
     */
    boolean existByUserId(@NotNull String userId)
            throws IllegalArgumentException;

    /**
     * 判断存在
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     *
     * @throws IllegalArgumentException
     */
    boolean existByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 是否具有管理员权限
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    boolean existAdminPermission(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @NotNull PageImpl<SecurityUser> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询
     *
     * @param userId    {@linkplain SecurityUser.Validator#userId(String) [用户 ID]}
     *
     * @return {@link SecurityUser}
     */
    @NotNull SecurityUser selectByUserId(@NotNull String userId)
            throws IllegalArgumentException;

    /**
     * 查询
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@link SecurityUser}
     */
    @NotNull SecurityUser selectByUsername(@NotNull String username)
            throws IllegalArgumentException;

}
