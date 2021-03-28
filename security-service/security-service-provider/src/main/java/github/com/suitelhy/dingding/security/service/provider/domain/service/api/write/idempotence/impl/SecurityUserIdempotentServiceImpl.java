package github.com.suitelhy.dingding.security.service.provider.domain.service.api.write.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence.SecurityRoleIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence.SecurityUserIdempotentService;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
@Service(cluster = "forking")
public class SecurityUserIdempotentServiceImpl
        implements SecurityUserIdempotentService {

    @Autowired
    private SecurityUserService securityUserService;

    /**
     * 新增一个用户
     *
     * @Description
     * · 完整业务流程的一部分
     *
     * @param user     {@linkplain SecurityUser [（安全认证）用户]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insert(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityUserService.insert(user, operator);
    }

    /**
     * 更新指定的用户
     *
     * @param user     {@linkplain SecurityUser [（安全认证）用户]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityUserService.update(user, operator);
    }

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分
     *
     * @param user     {@linkplain SecurityUser [（安全认证）用户]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定的用户是否已被删除}
     */
    @Override
    public boolean delete(@NotNull SecurityUser user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityUserService.delete(user, operator);
    }

}
