package github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRoleResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;

import javax.validation.constraints.NotNull;

/**
 * （安全认证）角色 ←→ 资源
 *
 * @Description [（安全认证）角色 ←→ 资源]关联关系 - 业务接口.
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see SecurityRoleResource
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FORKING
 */
public interface SecurityRoleResourceIdempotentService {

    //===== 添加操作业务 =====//

    /**
     * 新增一个[（安全认证）角色 ←→ 资源]关联关系
     *
     * @param roleResource  {@linkplain SecurityRoleResource [（安全认证）角色 ←→ 资源]}
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    boolean insert(@NotNull SecurityRoleResource roleResource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除操作业务 =====//

    /**
     * 删除指定的关联关系
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分
     *
     * @param roleResource  {@linkplain SecurityRoleResource [（安全认证）角色 ←→ 资源]}
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定的[（安全认证）角色 ←→ 资源]已被删除}
     */
    boolean delete(@NotNull SecurityRoleResource roleResource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
