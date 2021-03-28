package github.com.suitelhy.dingding.user.service.provider.domain.service.api.write.idempotence.impl;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.service.write.idempotence.UserAccountOperationInfoIdempotentWriteService;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserAccountOperationInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

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
@Service(cluster = "forking")
public class UserAccountOperationInfoIdempotentWriteServiceImpl
        implements UserAccountOperationInfoIdempotentWriteService {

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    /**
     * 更新指定的记录
     *
     * @param userAccountOperationInfo [用户 -> 账户操作基础记录]
     * @param operator                 操作者
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userAccountOperationInfoService.update(userAccountOperationInfo, operator);
    }

    /**
     * 删除指定的记录
     *
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user     {@linkplain UserAccountOperationInfo [用户 -> 账户操作基础记录]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定数据是否已被删除}
     */
    @Override
    public boolean deleteAndValidate(@NotNull UserAccountOperationInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return userAccountOperationInfoService.delete(user, operator);
    }

}
