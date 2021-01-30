package github.com.suitelhy.dingding.core.domain.service.security;

import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityResourceUrlRepository;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleResourceRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityResourceUrlServiceImpl;
import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityRoleResourceServiceImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashSet;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * [（安全认证）资源 ←→ URL]
 *
 * @Description [（安全认证）资源 ←→ 资源]关联关系 - 业务接口.
 * @see SecurityResourceUrl
 * @see SecurityResourceUrlRepository
 * @see SecurityResourceUrlServiceImpl
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , timeout = 15)
public interface SecurityResourceUrlService
        extends EntityService {

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @param urlInfo URL相关信息.    {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     * @return {@link Boolean#TYPE}
     */
    boolean existResourceByUrlInfo(@NotNull String[] urlInfo);

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @param resourceCode 资源编码    {@link SecurityResourceUrl.Validator#code(String)}
     * @return {@link Boolean#TYPE}
     */
    boolean existUrlByResourceCode(@NotNull String resourceCode);

    /**
     * 判断是否存在 (指定的) [（安全认证）资源 ←→ URL]
     *
     * @param resourceUrl [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
     * @return {@link Boolean#TYPE}
     */
    boolean existResourceUrlByResourceUrl(@NotNull SecurityResourceUrl resourceUrl);

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @return {@link Page}
     */
    Page<SecurityResourceUrl> selectAll(int pageIndex, int pageSize);

    /**
     * 查询总页数
     *
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     * @Description 查询数据列表 - 分页 - 总页数
     */
    @NotNull
    Long selectCount(int pageSize);

    /**
     * 查询
     *
     * @param resourceUrl [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
     * @return {@link SecurityResourceUrl}
     */
    @NotNull
    SecurityResourceUrl selectByResourceUrl(@NotNull SecurityResourceUrl resourceUrl);

    /**
     * 查询
     *
     * @param resourceCode 资源编码    {@link SecurityResourceUrl.Validator#code(String)}
     * @return {@link SecurityResourceUrl}
     */
    @NotNull
    List<SecurityResourceUrl> selectByResourceCode(@NotNull String resourceCode);

    /**
     * 查询
     *
     * @param clientId 资源服务器 ID             {@link SecurityResourceUrl.Validator#clientId(String)}
     * @param urlPath  资源对应的 URL (Path部分)  {@link SecurityResourceUrl.Validator#urlPath(String)}
     * @return {@link SecurityResourceUrl}
     */
    @NotNull
    List<SecurityResourceUrl> selectByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath);

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @param resourceUrl                       [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    boolean insert(@NotNull SecurityResourceUrl resourceUrl
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @param resourceUrl                       [（安全认证）资源 ←→ URL]   {@link SecurityResourceUrl}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean delete(@NotNull SecurityResourceUrl resourceUrl
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

//    /**
//     * 删除指定的[（安全认证）资源]所有的[（安全认证）资源 ←→ URL]关联关系
//     *
//     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
//     *
//     * @param resource                          [（安全认证）资源]  {@link SecurityResource}
//     * @param operator                          操作者
//     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
//     *
//     * @return 操作是否成功
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean delete(@NotNull SecurityResource resource
//            , @NotNull SecurityUser operator
//            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
//            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[URL 信息]所有的[（安全认证）资源 ←→ URL]关联关系
     *
     * @param urlInfo                           [URL 信息]    {@link SecurityResourceUrl.Validator#urlInfo(String[])}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整的业务流程.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean delete(@NotNull String[] urlInfo
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException;

}
