package github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;

import javax.validation.constraints.NotNull;

/**
 * (安全) 用户 - 业务
 *
 * @Description (安全) 用户 - 业务接口.
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see SecurityUser
 * @see SecurityRoleIdempotentService
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FORKING
 */
public interface SecurityUserIdempotentService {

    /**
     * 新增一个用户
     *
     * @Description
     * · 完整业务流程的一部分.
     *
     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    boolean insert(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 更新指定的用户
     *
     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean update(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分.
     *
     * @param user      {@linkplain SecurityUser [（安全认证）用户]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定的用户是否已被删除}
     */
    boolean delete(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
