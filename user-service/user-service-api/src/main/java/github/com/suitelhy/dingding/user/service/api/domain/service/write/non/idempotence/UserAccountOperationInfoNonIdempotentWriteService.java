package github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 用户 -> 账户操作记录 - 业务接口
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see UserAccountOperationInfo
 * @see Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
public interface UserAccountOperationInfoNonIdempotentWriteService {

    //===== 添加操作业务 =====//

    /**
     * 新增一条记录
     *
     * @param user      {@linkplain UserAccountOperationInfo [用户 -> 账户操作基础记录]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean insert(@NotNull UserAccountOperationInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 修改操作业务 =====//

    /**
     * 更新指定的用户 -> [用户 -> 账户操作基础记录]
     *
     * @param old_userAccountOperationInfo      {@linkplain UserAccountOperationInfo 被修改的[用户 -> 账户操作基础记录] <- 原始版本业务全量数据}
     * @param new_userAccountOperationInfo_data {@linkplain Map 被修改的[用户 -> 账户操作基础记录] <- 需要更新的数据}
     * · 数据结构:
     * {
     *  "ip": [最后登陆 IP],
     *  "lastLoginTime": 最后登录时间
     * }
     * @param operator                          {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean update(@NotNull UserAccountOperationInfo old_userAccountOperationInfo, @NotNull Map<String, Object> new_userAccountOperationInfo_data, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除操作业务 =====//

    /**
     * 删除指定的记录
     *
     * @param user      {@linkplain UserAccountOperationInfo [用户 -> 账户操作基础记录]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean delete(@NotNull UserAccountOperationInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
