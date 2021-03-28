package github.com.suitelhy.dingding.security.service.provider.domain.event.api.write.non.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.non.idempotence.SecurityUrlNonIdempotentEvent;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityUrlEvent;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

/**
 * (安全) 资源 - 复杂业务接口
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see SecurityResource
 * @see SecurityResourceUrl
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class SecurityUrlNonIdempotentEventImpl
        implements SecurityUrlNonIdempotentEvent {

    @Autowired
    private SecurityUrlEvent securityUrlEvent;

    /**
     * 新增一个[URL 相关信息]
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
    public boolean insertUrlInfo(@NotNull SecurityResource resource, @NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityUrlEvent.insertUrlInfo(resource, urlInfo, operator);
    }

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程
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
        return securityUrlEvent.insertResourceUrlRelationship(resource, urlInfo, operator);
    }

    /**
     * 删除指定的[URL 相关信息]
     *
     * @Description
     * · 完整的业务流程
     *
     * @param urlInfo  {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteUrlInfo(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityUrlEvent.deleteUrlInfo(urlInfo, operator);
    }

    /**
     * 删除指定的[（安全认证）资源]关联的所有[URL 相关信息]
     *
     * @Description
     * · 完整的业务流程
     *
     * @param resource {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean deleteUrlInfoOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityUrlEvent.deleteUrlInfoOnResource(resource, operator);
    }

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程
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
        return securityUrlEvent.deleteResourceUrlRelationship(resourceUrl, operator);
    }

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程
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
        return securityUrlEvent.deleteResourceUrlRelationship(resource, urlInfo, operator);
    }

    /**
     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程
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
        return securityUrlEvent.deleteResourceUrlRelationshipOnResource(resource, operator);
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
        return securityUrlEvent.deleteResourceUrlRelationshipOnUrlInfo(urlInfo, operator);
    }

}
