package github.com.suitelhy.dingding.security.service.provider.domain.event;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.AggregateEvent;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.event.impl.SecurityUrlEventImpl;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityResourceService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityResourceUrlService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * (安全) 资源 - 复杂业务接口
 *
 * @see SecurityResource
 * @see SecurityResourceUrl
 * @see SecurityUrlEventImpl
 * @see SecurityResourceService
 * @see SecurityResourceUrlService
 */
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public interface SecurityUrlEvent
        extends AggregateEvent {

    //===== 查询操作业务 =====//

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @Description 完整的业务流程.
     *
     * @param urlInfo   {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    boolean existResourceOnUrlInfo(@NotNull String[] urlInfo)
            throws IllegalArgumentException;

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityResource.Validator#code(String) 资源编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    boolean existUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 查询[URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityResourceUrl.Validator#code(String) 资源编码}
     *
     * @return {@linkplain SecurityResourceUrl [URL 相关信息]}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityResourceUrl> selectUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain SecurityResourceUrl [用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityResourceUrl> selectUrlInfoOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     * @param clientId  {@linkplain SecurityResourceUrl#getClientId() [资源服务器 ID]}
     *
     * @return {@linkplain SecurityResourceUrl [用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityResourceUrl> selectUrlInfoOnUserByUsernameAndClientId(@NotNull String username, @NotNull String clientId)
            throws IllegalArgumentException;

    /**
     * 查询[URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param clientId  {@linkplain SecurityResourceUrl.Validator#clientId(String) [资源服务器 ID]}
     *
     * @return {@linkplain SecurityResourceUrl [URL 相关信息]}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityResourceUrl> selectUrlInfoByClientId(@NotNull String clientId)
            throws IllegalArgumentException;

    /**
     * 查询[URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param clientId  {@linkplain SecurityResourceUrl.Validator#clientId(String) [资源服务器 ID]}
     * @param urlPath   {@linkplain SecurityResourceUrl.Validator#urlPath(String) [资源对应的 URL (Path部分)]}
     *
     * @return {@linkplain SecurityResourceUrl [URL 相关信息]}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityResourceUrl> selectUrlInfoByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath)
            throws IllegalArgumentException;

    /**
     * 查询[（安全认证）资源 ←→ URL]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityResourceUrl.Validator#code(String) 资源编码}
     *
     * @return {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityResourceUrl> selectResourceUrlRelationshipOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 查询[（安全认证）资源 ←→ URL]
     *
     * @Description 完整的业务流程.
     *
     * @param clientId  {@linkplain SecurityResourceUrl.Validator#clientId(String) [资源服务器 ID]}
     * @param urlPath   {@linkplain SecurityResourceUrl.Validator#urlPath(String) [资源对应的 URL](Path部分)}
     *
     * @return {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityResourceUrl> selectResourceUrlRelationshipOnUrlInfoByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath)
            throws IllegalArgumentException;

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
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    boolean insertResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
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
