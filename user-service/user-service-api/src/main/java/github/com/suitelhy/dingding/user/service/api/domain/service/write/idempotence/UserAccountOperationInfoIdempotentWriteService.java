package github.com.suitelhy.dingding.user.service.api.domain.service.write.idempotence;

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
 * · 幂等性
 *
 * @see UserAccountOperationInfo
 * @see Dubbo.Strategy.ClusterVo#FORKING
 */
public interface UserAccountOperationInfoIdempotentWriteService {

    //===== 修改操作业务 =====//

    /**
     * 更新指定的记录
     *
     * @param userAccountOperationInfo  {@linkplain UserAccountOperationInfo [用户 -> 账户操作基础记录]}
     * @param operator                  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean update(@NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除操作业务 =====//

    /**
     * 删除指定的记录
     *
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user      {@linkplain UserAccountOperationInfo [用户 -> 账户操作基础记录]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定数据是否已被删除}
     */
    boolean deleteAndValidate(@NotNull UserAccountOperationInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
