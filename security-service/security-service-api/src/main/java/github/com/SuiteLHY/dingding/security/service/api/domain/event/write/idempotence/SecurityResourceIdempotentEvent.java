package github.com.suitelhy.dingding.security.service.api.domain.event.write.idempotence;

import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.*;

import javax.validation.constraints.NotNull;

/**
 * (安全) 资源 - 复杂业务接口
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see SecurityResource
 * @see SecurityRole
 * @see SecurityResourceUrl
 * @see SecurityRoleResource
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FORKING
 */
public interface SecurityResourceIdempotentEvent {

    //===== 添加操作业务 =====//

    /**
     * 新增一个[（安全认证）资源]
     *
     * @Description 完整的业务流程.
     *
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    boolean insertResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    boolean insertResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 修改操作业务 =====//

    /**
     * 更新指定的资源
     *
     * @Description 全量更新.
     * · 完整的业务流程.
     *
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean updateResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
