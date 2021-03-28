package github.com.suitelhy.dingding.security.service.provider.domain.event.api.write.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.*;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.idempotence.SecurityResourceIdempotentEvent;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityResourceEvent;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
@Service(cluster = "forking")
public class SecurityResourceIdempotentEventImpl
        implements SecurityResourceIdempotentEvent {

    @Autowired
    private SecurityResourceEvent securityResourceEvent;

    /**
     * 新增一个[（安全认证）资源]
     *
     * @Description 完整的业务流程.
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insertResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.insertResource(resource, operator);
    }

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param resourceUrl {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     * @param operator    {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insertResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.insertResourceUrlRelationship(resourceUrl, operator);
    }

    /**
     * 更新指定的资源
     *
     * @Description 全量更新.
     * · 完整的业务流程.
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean updateResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.updateResource(resource, operator);
    }

}
