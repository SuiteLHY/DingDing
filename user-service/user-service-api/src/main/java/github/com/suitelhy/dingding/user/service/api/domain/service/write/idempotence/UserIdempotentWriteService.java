package github.com.suitelhy.dingding.user.service.api.domain.service.write.idempotence;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;

import javax.validation.constraints.NotNull;

/**
 * 用户 - 业务接口
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see User
 * @see Dubbo.Strategy.ClusterVo#FORKING
 */
public interface UserIdempotentWriteService {

    //===== 修改操作 =====//

    /**
     * 更新指定的用户
     *
     * @param user                   被修改的用户
     * @param operator               操作者
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean update(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除操作 =====//

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user                              被删除的用户
     * @param operator                          操作者 (与{@param user}一致, 或拥有足够高的权限)
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定数据是否已被删除}
     */
    boolean deleteAndValidate(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
