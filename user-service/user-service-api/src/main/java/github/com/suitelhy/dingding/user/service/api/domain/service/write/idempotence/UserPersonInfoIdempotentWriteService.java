package github.com.suitelhy.dingding.user.service.api.domain.service.write.idempotence;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;

import javax.validation.constraints.NotNull;

/**
 * 用户 -> 账户操作记录 - 业务接口
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see UserPersonInfo
 * @see Dubbo.Strategy.ClusterVo#FORKING
 */
public interface UserPersonInfoIdempotentWriteService {

    //===== 修改操作业务 =====//

    /**
     * 更新指定的记录
     *
     * @param user      {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean update(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除操作业务 =====//

    /**
     * 删除指定的记录
     *
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user      {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定数据是否已被删除}
     */
    boolean deleteAndValidate(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
