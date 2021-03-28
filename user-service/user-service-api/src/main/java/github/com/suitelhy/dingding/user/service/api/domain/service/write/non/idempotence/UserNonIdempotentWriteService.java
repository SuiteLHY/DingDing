package github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 用户 - 业务接口
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see User
 * @see Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
public interface UserNonIdempotentWriteService {

    //===== 添加操作 =====//

    /**
     * 新增一个用户
     *
     * @Description 完整业务的一部分.
     *
     * @param user                   预期新增的用户 -> 用户基础信息
     * @param operator               操作者
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     *
     * @throws IllegalArgumentException
     * @throws BusinessAtomicException
     */
    boolean insert(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

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

    /**
     * 更新指定的用户
     *
     * @param old_user              {@linkplain User 被修改的用户 <- 原始版本业务全量数据}
     * @param new_user_data         {@linkplain Map 被修改的用户 <- 需要更新的数据}
     * · 数据结构:
     * {
     *  “password”: [用户密码_明文],
     *  “status”: [账号状态]
     * }
     * @param operator              {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean update(@NotNull User old_user, @NotNull Map<String, Object> new_user_data, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

   //===== 删除操作 =====//

   /**
     * 删除指定的用户
     *
     * @param user      被删除的用户
     * @param operator  操作者 (与{@param user}一致, 或拥有足够高的权限)
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean delete(@NotNull User user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
