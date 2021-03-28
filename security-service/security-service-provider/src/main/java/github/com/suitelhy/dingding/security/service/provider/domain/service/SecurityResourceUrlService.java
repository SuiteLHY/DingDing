package github.com.suitelhy.dingding.security.service.provider.domain.service;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.service.impl.SecurityResourceUrlServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * [（安全认证）资源 ←→ URL]
 *
 * @Description [（安全认证）资源 ←→ 资源]关联关系 - 业务接口.
 *
 * @see SecurityResourceUrl
 * @see SecurityResourceUrlServiceImpl
 */
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public interface SecurityResourceUrlService
        extends EntityService {

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
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
    boolean existResourceByUrlInfo(@NotNull String[] urlInfo);

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @param resourceCode  {@linkplain SecurityResourceUrl.Validator#code(String) 资源编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    boolean existUrlByResourceCode(@NotNull String resourceCode);

    /**
     * 判断是否存在 (指定的) [（安全认证）资源 ←→ URL]
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    boolean existResourceUrlByResourceUrl(@NotNull SecurityResourceUrl resourceUrl);

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link Page}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull Page<SecurityResourceUrl> selectAll(int pageIndex, int pageSize);

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull Long selectCount(int pageSize);

    /**
     * 查询
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     *
     * @return {@link SecurityResourceUrl}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull SecurityResourceUrl selectByResourceUrl(@NotNull SecurityResourceUrl resourceUrl);

    /**
     * 查询
     *
     * @param resourceCode  {@linkplain SecurityResourceUrl.Validator#code(String) 资源编码}
     *
     * @return {@link SecurityResourceUrl}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityResourceUrl> selectByResourceCode(@NotNull String resourceCode);

    /**
     * 查询
     *
     * @param clientId  {@linkplain SecurityResourceUrl.Validator#clientId(String) [资源服务器 ID] }
     *
     * @return {@link SecurityResourceUrl}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityResourceUrl> selectByClientId(@NotNull String clientId);

    /**
     * 查询
     *
     * @param clientId  {@linkplain SecurityResourceUrl.Validator#clientId(String) [资源服务器 ID] }
     * @param urlPath   {@linkplain SecurityResourceUrl.Validator#urlPath(String) [资源对应的 URL (Path部分)]}
     *
     * @return {@link SecurityResourceUrl}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<SecurityResourceUrl> selectByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath);

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    boolean insert(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean delete(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    /**
     * 删除指定的[URL 信息]所有的[（安全认证）资源 ←→ URL]关联关系
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整的业务流程.
     *
     * @param urlInfo   {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean delete(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
