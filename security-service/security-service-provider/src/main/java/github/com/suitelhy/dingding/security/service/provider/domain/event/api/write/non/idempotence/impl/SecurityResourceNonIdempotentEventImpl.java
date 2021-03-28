package github.com.suitelhy.dingding.security.service.provider.domain.event.api.write.non.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.*;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.non.idempotence.SecurityResourceNonIdempotentEvent;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityResourceEvent;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * (安全) 资源 - 复杂业务接口
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see SecurityResource
 * @see SecurityRole
 * @see SecurityResourceUrl
 * @see SecurityRoleResource
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class SecurityResourceNonIdempotentEventImpl
        implements SecurityResourceNonIdempotentEvent {

    @Autowired
    private SecurityResourceEvent securityResourceEvent;

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param urlInfo  {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean insertResourceUrlRelationship(@NotNull SecurityResource resource, @NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.insertResourceUrlRelationship(resource, urlInfo, operator);
    }

    /**
     * 新增[角色 - 资源]关联
     *
     * @Description 完整的业务流程.
     *
     * @param roles     {@linkplain SecurityRole [（安全认证）角色]}, 必须全部合法且已持久化.
     * @param resources {@linkplain SecurityResource [（安全认证）资源]}, 必须全部合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean insertRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.insertRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 删除指定的资源
     *
     * @Description · 完整的业务流程.
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.deleteResource(resource, operator);
    }

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description · 完整的业务流程.
     *
     * @param resourceUrl {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     * @param operator    {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.deleteResourceUrlRelationship(resourceUrl, operator);
    }

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param urlInfo  {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteResourceUrlRelationship(@NotNull SecurityResource resource, @NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.deleteResourceUrlRelationship(resource, urlInfo, operator);
    }

    /**
     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteResourceUrlRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.deleteResourceUrlRelationshipOnResource(resource, operator);
    }

    /**
     * 删除指定的[URL 信息]关联的所有[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程
     *
     * @param urlInfo  {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 信息]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteResourceUrlRelationshipOnUrlInfo(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.deleteResourceUrlRelationshipOnUrlInfo(urlInfo, operator);
    }

    /**
     * 删除[角色 - 资源]关联
     *
     * @Description
     * · 完整的业务流程
     *
     * @param roles     {@linkplain SecurityRole [（安全认证）角色]}, 必须全部合法且已持久化.
     * @param resources {@linkplain SecurityResource [（安全认证）资源]}, 必须全部合法且已持久化.
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteRoleResourceRelationship(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.deleteRoleResourceRelationship(roles, resources, operator);
    }

    /**
     * 删除指定的[（安全认证）角色]关联的所有[（安全认证）角色 ←→ 资源]关联关系
     *
     * @Description
     * · 完整的业务流程
     *
     * @param role     {@linkplain SecurityRole [（安全认证）角色]}, 必须合法且已持久化.
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteRoleResourceRelationshipOnRole(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.deleteRoleResourceRelationshipOnRole(role, operator);
    }

    /**
     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）角色 ←→ 资源]关联关系
     *
     * @Description
     * · 完整的业务流程
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteRoleResourceRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityResourceEvent.deleteRoleResourceRelationshipOnResource(resource, operator);
    }

}
