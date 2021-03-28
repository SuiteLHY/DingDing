package github.com.suitelhy.dingding.security.service.api.domain.event.write.non.idempotence;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;

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
public interface SecurityUrlNonIdempotentEvent {

    //===== 添加操作业务 =====//

    /**
     * 新增一个[URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param urlInfo   {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean insertUrlInfo(@NotNull SecurityResource resource
            , @NotNull String[] urlInfo
            , @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description 完整的业务流程.
     *
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param urlInfo   {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean insertResourceUrlRelationship(@NotNull SecurityResource resource
            , @NotNull String[] urlInfo
            , @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 修改操作业务 =====//

    //===== 删除操作业务 =====//

    /**
     * 删除指定的[URL 相关信息]
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param urlInfo   {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean deleteUrlInfo(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）资源]关联的所有[URL 相关信息]
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean deleteUrlInfoOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean deleteResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}, 必须合法且已持久化.
     * @param urlInfo   {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean deleteResourceUrlRelationship(@NotNull SecurityResource resource
            , @NotNull String[] urlInfo
            , @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean deleteResourceUrlRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[URL 信息]关联的所有[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description
     * · 完整的业务流程.
     *
     * @param urlInfo   {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean deleteResourceUrlRelationshipOnUrlInfo(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
