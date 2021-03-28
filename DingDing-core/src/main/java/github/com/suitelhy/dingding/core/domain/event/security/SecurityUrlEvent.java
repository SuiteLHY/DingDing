//package github.com.suitelhy.dingding.core.domain.event.security;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.*;
//import github.com.suitelhy.dingding.core.domain.event.security.impl.SecurityUrlEventImpl;
//import github.com.suitelhy.dingding.core.domain.service.LogService;
//import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
//import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
//import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceUrlService;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.AggregateEvent;
//import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.validation.constraints.NotNull;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * (安全) 资源 - 复杂业务接口
// *
// * @see SecurityResource
// * @see SecurityResourceUrl
// * @see SecurityUrlEventImpl
// * @see SecurityResourceService
// * @see SecurityResourceUrlService
// * @see UserAccountOperationInfoService
// * @see LogService
// */
//@Transactional(isolation = Isolation.READ_COMMITTED
//        , propagation = Propagation.REQUIRED
//        , readOnly = true
//        , rollbackFor = Exception.class
//        , timeout = 15)
//public interface SecurityUrlEvent
//        extends AggregateEvent {
//
//    //===== 查询操作业务 =====//
//
//    /**
//     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
//     *
//     * @param urlInfo URL相关信息.    {@link SecurityResourceUrl.Validator#urlInfo(String[])}
//     * @return {@link Boolean#TYPE}
//     * @Description 完整的业务流程.
//     */
//    boolean existResourceOnUrlInfo(@NotNull String[] urlInfo)
//            throws IllegalArgumentException;
//
//    /**
//     * 判断[（安全认证）资源]是否存在 (关联的) [URL 相关信息]
//     *
//     * @param resourceCode 资源编码    {@link SecurityResource.Validator#code(String)}
//     * @return {@link Boolean#TYPE}
//     * @Description 完整的业务流程.
//     */
//    boolean existUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询[URL 相关信息]
//     *
//     * @param resourceCode 资源编码    {@link SecurityResourceUrl.Validator#code(String)}
//     * @return {@link SecurityResourceUrl}
//     * @Description 完整的业务流程.
//     * @see github.com.suitelhy.dingding.core.domain.event.security.SecurityResourceEvent#selectUrlInfoOnResourceByResourceCode(String)
//     */
//    @NotNull
//    List<SecurityResourceUrl> selectUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
//     *
//     * @param username 用户名称    {@link SecurityUser.Validator#username(String)}
//     * @return {@link SecurityResourceUrl}
//     * @Description 完整的业务流程.
//     * @see github.com.suitelhy.dingding.core.domain.event.UserEvent#selectUrlInfoOnUserByUsername(String)
//     */
//    @NotNull
//    List<SecurityResourceUrl> selectUrlInfoOnUserByUsername(@NotNull String username)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
//     *
//     * @param username 用户名称         {@link SecurityUser.Validator#username(String)}
//     * @param clientId [资源服务器 ID]  {@link SecurityResourceUrl#getClientId()}
//     * @return {@link SecurityResourceUrl}
//     * @Description 完整的业务流程.
//     * @see github.com.suitelhy.dingding.core.domain.event.UserEvent#selectUrlInfoOnUserByUsernameAndClientId(String, String)
//     */
//    @NotNull
//    List<SecurityResourceUrl> selectUrlInfoOnUserByUsernameAndClientId(@NotNull String username, @NotNull String clientId)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询[URL 相关信息]
//     *
//     * @param clientId [资源服务器 ID]             {@link SecurityResourceUrl.Validator#clientId(String)}
//     * @param urlPath  [资源对应的 URL (Path部分)] {@link SecurityResourceUrl.Validator#urlPath(String)}
//     * @return {@link SecurityResourceUrl}
//     * @Description 完整的业务流程.
//     */
//    @NotNull
//    List<SecurityResourceUrl> selectUrlInfoByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询[（安全认证）资源 ←→ URL]
//     *
//     * @param resourceCode 资源编码    {@link SecurityResourceUrl.Validator#code(String)}
//     * @return {@link SecurityResourceUrl}
//     * @Description 完整的业务流程.
//     */
//    @NotNull
//    List<SecurityResourceUrl> selectResourceUrlRelationshipOnResourceByResourceCode(@NotNull String resourceCode)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询[（安全认证）资源 ←→ URL]
//     *
//     * @param clientId 资源服务器 ID             {@link SecurityResourceUrl.Validator#clientId(String)}
//     * @param urlPath  资源对应的 URL (Path部分)  {@link SecurityResourceUrl.Validator#urlPath(String)}
//     * @return {@link SecurityResourceUrl}
//     * @Description 完整的业务流程.
//     */
//    @NotNull
//    List<SecurityResourceUrl> selectResourceUrlRelationshipOnUrlInfoByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath)
//            throws IllegalArgumentException;
//
//    //===== 添加操作业务 =====//
//
//    /**
//     * 新增一个[URL 相关信息]
//     *
//     * @param resource [（安全认证）资源], 必须合法且已持久化.   {@link SecurityResource}
//     * @param urlInfo  [URL 相关信息]                       {@link SecurityResourceUrl.Validator#urlInfo(String[])}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean insertUrlInfo(@NotNull SecurityResource resource
//            , @NotNull String[] urlInfo
//            , @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 新增一个[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param resourceUrl [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
//     * @param operator    操作者
//     * @return 操作是否成功 / 是否已存在相同的有效数据
//     * @Description 完整的业务流程.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean insertResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 新增一个[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param resource [（安全认证）资源], 必须合法且已持久化.   {@link SecurityResource}
//     * @param urlInfo  [URL 相关信息]                       {@link SecurityResourceUrl.Validator#urlInfo(String[])}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description 完整的业务流程.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean insertResourceUrlRelationship(@NotNull SecurityResource resource
//            , @NotNull String[] urlInfo
//            , @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    //===== 修改操作业务 =====//
//
//    //===== 删除操作业务 =====//
//
//    /**
//     * 删除指定的[URL 相关信息]
//     *
//     * @param urlInfo  [URL 相关信息]   {@link SecurityResourceUrl.Validator#urlInfo(String[])}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description · 完整的业务流程.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean deleteUrlInfo(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 删除指定的[（安全认证）资源]关联的所有[URL 相关信息]
//     *
//     * @param resource [（安全认证）资源]  {@link SecurityResource}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description · 完整的业务流程.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean deleteUrlInfoOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param resourceUrl [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
//     * @param operator    操作者
//     * @return 操作是否成功
//     * @Description · 完整的业务流程.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean deleteResourceUrlRelationship(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param resource [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
//     * @param urlInfo  [URL 相关信息]                      {@link SecurityResourceUrl.Validator#urlInfo(String[])}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description · 完整的业务流程.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean deleteResourceUrlRelationship(@NotNull SecurityResource resource
//            , @NotNull String[] urlInfo
//            , @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 删除指定的[（安全认证）资源]关联的所有[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param resource [（安全认证）资源]  {@link SecurityResource}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description · 完整的业务流程.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean deleteResourceUrlRelationshipOnResource(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 删除指定的[URL 信息]关联的所有[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @param urlInfo  [URL 信息]    {@link SecurityResourceUrl.Validator#urlInfo(String[])}
//     * @param operator 操作者
//     * @return 操作是否成功
//     * @Description · 完整的业务流程.
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean deleteResourceUrlRelationshipOnUrlInfo(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//}
