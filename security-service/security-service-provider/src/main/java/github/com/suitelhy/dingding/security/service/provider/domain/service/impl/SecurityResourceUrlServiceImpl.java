package github.com.suitelhy.dingding.security.service.provider.domain.service.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.repository.SecurityResourceUrlRepository;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityResourceUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * [（安全认证）资源 ←→ URL]
 *
 * @Description [（安全认证）资源 ←→ 资源]关联关系 - 业务实现.
 *
 * @see SecurityResourceUrl
 * @see SecurityResourceUrlRepository
 * @see SecurityResourceUrlService
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Service("securityResourceUrlService")
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public class SecurityResourceUrlServiceImpl
        implements SecurityResourceUrlService {

    @Autowired
    private SecurityResourceUrlRepository resourceUrlRepository;

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @param urlInfo   {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public boolean existResourceByUrlInfo(@NotNull String[] urlInfo) {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
            //-- 非法输入: [URL 相关信息]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[URL 相关信息]"
                    , Arrays.toString(urlInfo)
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return resourceUrlRepository.existsAllByClientIdAndUrlPathAndUrlMethod(urlInfo[0], urlInfo[1], urlInfo[2]);
    }

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @param resourceCode 资源编码    {@link SecurityResourceUrl.Validator#code(String)}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public boolean existUrlByResourceCode(@NotNull String resourceCode) {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.code(resourceCode)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "资源编码"
                    , resourceCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return resourceUrlRepository.existsAllByCode(resourceCode);
    }

    /**
     * 判断是否存在 (指定的) [（安全认证）资源 ←→ URL]
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public boolean existResourceUrlByResourceUrl(@NotNull SecurityResourceUrl resourceUrl) {
        if (null == resourceUrl || ! resourceUrl.isEntityLegal()) {
            //-- 非法输入: [（安全认证）资源 ←→ URL]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源 ←→ URL]"
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return resourceUrlRepository.existsAllByCodeAndClientIdAndUrlPathAndUrlMethod(resourceUrl.getCode()
                , resourceUrl.getClientId()
                , resourceUrl.getUrlPath()
                , resourceUrl.getUrlMethod());
    }

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link Page}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public Page<SecurityResourceUrl> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            //-- 非法输入: <param>pageIndex</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "pageIndex"
                    , pageIndex
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        Sort.TypedSort<SecurityResourceUrl> typedSort = Sort.sort(SecurityResourceUrl.class);
        @NotNull Sort sort = typedSort.by(SecurityResourceUrl::getCode).ascending()
                .and(typedSort.by(SecurityResourceUrl::getClientId).ascending());
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return resourceUrlRepository.findAll(page);
    }

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull Long selectCount(int pageSize) {
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        long dataNumber = resourceUrlRepository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     *
     * @return {@link SecurityResourceUrl}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull SecurityResourceUrl selectByResourceUrl(@NotNull SecurityResourceUrl resourceUrl) {
        if (null == resourceUrl || ! resourceUrl.isEntityLegal()) {
            //-- 非法输入: [（安全认证）资源 ←→ URL]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源 ←→ URL]"
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull Optional<SecurityResourceUrl> result = resourceUrlRepository.findSecurityResourceUrlByCodeAndClientIdAndUrlPathAndUrlMethod(resourceUrl.getCode()
                , resourceUrl.getClientId()
                , resourceUrl.getUrlPath()
                , resourceUrl.getUrlMethod());
        return result
                .orElseGet(SecurityResourceUrl.Factory.RESOURCE_URL::createDefault);
    }

    /**
     * 查询
     *
     * @param resourceCode  {@linkplain SecurityResourceUrl.Validator#code(String) 资源编码}
     *
     * @return {@link SecurityResourceUrl}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull List<SecurityResourceUrl> selectByResourceCode(@NotNull String resourceCode) {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.code(resourceCode)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "资源编码"
                    , resourceCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return resourceUrlRepository.findAllByCode(resourceCode);
    }

    /**
     * 查询
     *
     * @param clientId {@linkplain SecurityResourceUrl.Validator#clientId(String) [资源服务器 ID] }
     *
     * @return {@link SecurityResourceUrl}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull List<SecurityResourceUrl> selectByClientId(@NotNull String clientId) {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.clientId(clientId)) {
            //-- 非法输入: [资源服务器 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[资源服务器 ID]"
                    , clientId
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return resourceUrlRepository.findAllByClientId(clientId);
    }

    /**
     * 查询
     *
     * @param clientId  {@linkplain SecurityResourceUrl.Validator#clientId(String) [资源服务器 ID] }
     * @param urlPath   {@linkplain SecurityResourceUrl.Validator#urlPath(String) [资源对应的 URL (Path部分)]}
     *
     * @return {@link SecurityResourceUrl}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull List<SecurityResourceUrl> selectByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath) {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.clientId(clientId)) {
            //-- 非法输入: [资源服务器 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[资源服务器 ID]"
                    , clientId
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(urlPath)) {
            //-- 非法输入: [资源对应的 URL (Path部分)]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[资源对应的 URL (Path部分)]"
                    , urlPath
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return resourceUrlRepository.findAllByClientIdAndUrlPath(clientId, urlPath);
    }

    /**
     * 新增一个[（安全认证）资源 ←→ URL]关联关系
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insert(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == resourceUrl || ! resourceUrl.isEntityLegal()) {
            //-- 非法输入: [（安全认证）资源 ←→ URL]关联关系
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源 ←→ URL]关联关系"
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
        /*if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || !operator_userAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        if (resourceUrlRepository.existsAllByCodeAndClientIdAndUrlPathAndUrlMethod(resourceUrl.getCode()
                , resourceUrl.getClientId()
                , resourceUrl.getUrlPath()
                , resourceUrl.getUrlMethod())) {
            //--- 已存在相同数据 (根据 EntityID) 的情况
            return ! resourceUrlRepository.findSecurityResourceUrlByCodeAndClientIdAndUrlPathAndUrlMethod(resourceUrl.getCode()
                    , resourceUrl.getClientId()
                    , resourceUrl.getUrlPath()
                    , resourceUrl.getUrlMethod())
                    .orElseGet(SecurityResourceUrl.Factory.RESOURCE_URL::createDefault)
                    .isEmpty();
        }

        if (resourceUrlRepository.saveAndFlush(resourceUrl).isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
                    , "执行后数据异常"
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        /*//=== 操作日志记录 ===//
        final @NotNull Log newLog_ResourceUrl = Log.Factory.SecurityResourceUrl.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD
                , resourceUrl
                , operator
                , operator_userAccountOperationInfo);
        if (!logService.insert(newLog_ResourceUrl)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
                    , "生成操作日志记录"
                    , newLog_ResourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//*/

        return true;
    }

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
    @Override
    public boolean delete(@NotNull SecurityResourceUrl resourceUrl, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == resourceUrl || resourceUrl.isEmpty()) {
            //-- 非法输入: [（安全认证）资源 ←→ URL]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源 ←→ URL]"
                    , resourceUrl
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
        /*if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        if (! resourceUrlRepository.existsAllByCodeAndClientIdAndUrlPathAndUrlMethod(resourceUrl.getCode()
                , resourceUrl.getClientId()
                , resourceUrl.getUrlPath()
                , resourceUrl.getUrlMethod())) {
            return true;
        } else {
            resourceUrlRepository.removeByCodeAndClientIdAndUrlPathAndUrlMethod(resourceUrl.getCode()
                    , resourceUrl.getClientId()
                    , resourceUrl.getUrlPath()
                    , resourceUrl.getUrlMethod());
            if (resourceUrlRepository.existsAllByCodeAndClientIdAndUrlPathAndUrlMethod(resourceUrl.getCode()
                    , resourceUrl.getClientId()
                    , resourceUrl.getUrlPath()
                    , resourceUrl.getUrlMethod())) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                        , "执行后数据异常"
                        , resourceUrl
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            /*//=== 操作日志记录 ===//
            final @NotNull Log newLog_ResourceUrl = Log.Factory.SecurityResourceUrl.LOG.create(null
                    , null
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION
                    , resourceUrl
                    , operator
                    , operator_userAccountOperationInfo);
            if (!logService.insert(newLog_ResourceUrl)) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                        , "生成操作日志记录"
                        , newLog_ResourceUrl
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            //======//*/
        }

        return true;
    }

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
    @Override
    public boolean delete(@NotNull String[] urlInfo, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(urlInfo)) {
            //-- 非法输入: [URL 信息]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[URL 信息]"
                    , Arrays.toString(urlInfo)
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
        /*if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || !operator_userAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- 【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        if (! resourceUrlRepository.existsAllByClientIdAndUrlPathAndUrlMethod(urlInfo[0], urlInfo[1], urlInfo[2])) {
            return true;
        } else {
            final List<SecurityResourceUrl> resourceUrls = resourceUrlRepository.findAllByClientIdAndUrlPathAndUrlMethod(urlInfo[0], urlInfo[1], urlInfo[2]);
            if (null == resourceUrls || resourceUrls.isEmpty()) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                        , "预期删除的数据不存在"
                        , Arrays.toString(urlInfo)
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            resourceUrlRepository.removeByClientIdAndUrlPathAndUrlMethod(urlInfo[0], urlInfo[1], urlInfo[2]);
            if (resourceUrlRepository.existsAllByClientIdAndUrlPathAndUrlMethod(urlInfo[0], urlInfo[1], urlInfo[2])) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                        , "执行后数据异常"
                        , Arrays.toString(urlInfo)
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }/* else {
                //=== 操作日志记录 ===//
                for (@NotNull SecurityResourceUrl resourceUrl : resourceUrls) {
                    final @NotNull Log newLog_ResourceUrl = Log.Factory.SecurityResourceUrl.LOG.create(null
                            , null
                            , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION
                            , resourceUrl
                            , operator
                            , operator_userAccountOperationInfo);
                    if (!logService.insert(newLog_ResourceUrl)) {
                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- 【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                                , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__DELETION.name
                                , "生成操作日志记录"
                                , newLog_ResourceUrl
                                , this.getClass().getName()
                                , Thread.currentThread().getStackTrace()[1].getMethodName()
                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                    }
                }
                //======//
            }*/
        }

        return true;
    }

}
