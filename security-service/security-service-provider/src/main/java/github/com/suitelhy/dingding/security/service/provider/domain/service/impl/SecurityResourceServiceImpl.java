package github.com.suitelhy.dingding.security.service.provider.domain.service.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.repository.SecurityResourceRepository;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityResourceService;
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
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * (安全) 资源
 *
 * @Description (安全) 资源 - 业务实现.
 *
 * @see SecurityResourceService
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Service("securityResourceService")
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public class SecurityResourceServiceImpl
        implements SecurityResourceService {

    @Autowired
    private SecurityResourceRepository repository;

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从 0 开始.
     * @param pageSize
     *
     * @return {@link Page}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public Page<SecurityResource> selectAll(int pageIndex, int pageSize) {
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

        Sort.TypedSort<SecurityResource> typedSort = Sort.sort(SecurityResource.class);
        Sort sort = typedSort.by(SecurityResource::getSort).descending()
                .and(typedSort.by(SecurityResource::getCode).ascending());
        Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return repository.findAll(page);
    }

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数.
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

        long dataNumber = repository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询指定的资源
     *
     * @param code  {@linkplain SecurityResource#getCode() 资源编码}
     *
     * @return {@linkplain SecurityResource 指定的资源}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull SecurityResource selectResourceByCode(@NotNull String code) {
        if (! SecurityResource.Validator.RESOURCE.code(code)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "资源编码"
                    , code
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.findSecurityResourceByCode(code)
                .orElseGet(SecurityResource.Factory.RESOURCE::createDefault);
    }

    /**
     * 新增一个资源
     *
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insert(@NotNull SecurityResource resource, @NotNull SecurityUser operator) {
        if (null == resource || ! resource.isEntityLegal()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resource
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
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        if (repository.existsByCode(resource.id())) {
            //--- 已存在相同数据 (根据 EntityID) 的情况
            return ! repository.findSecurityResourceByCode(resource.id())
                    .orElseGet(SecurityResource.Factory.RESOURCE::createDefault)
                    .isEmpty();
        }

        return ! repository.save(resource).isEmpty();
    }

    /**
     * 更新指定的资源
     *
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     *
     * @throws IllegalArgumentException
     * @throws BusinessAtomicException
     */
    @Override
    public boolean update(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == resource || resource.isEmpty()) {
            //-- 非法输入: 非法角色
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "非法角色"
                    , resource
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
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        if (repository.saveAndFlush(resource).isEmpty()) {
            //-- 操作失败: 更新指定的资源
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE.name
                    , "执行后数据异常"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }/* else {
            //=== 操作日志记录 ===//
            final @NotNull Log newLog_Resource = Log.Factory.SecurityResource.LOG.create(null
                    , null
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE
                    , resource
                    , operator
                    , operator_userAccountOperationInfo);
            if (! logService.insert(newLog_Resource)) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE.name
                        , "生成操作日志记录"
                        , newLog_Resource
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            //======//
        }*/

        return true;
    }

    /**
     * 更新指定的资源
     *
     * @param old_resource      {@linkplain SecurityResource [（安全认证）资源]} <- 原始版本业务全量数据
     * @param new_resource_data [（安全认证）资源] <- 需要更新的数据
     * · 数据结构:
     * {
     *  “resource_icon”: [图标],
     *  “resource_link“: [资源链接],
     *  “resource_name“: [资源名称],
     *  “resource_parentCode“: [父节点 <- 资源编码],
     *  “resource_sort“: [序号],
     *  “resource_type“: [资源类型]
     * }
     * @param operator          {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull SecurityResource old_resource, @NotNull Map<String, Object> new_resource_data, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == old_resource || ! old_resource.isEntityLegal()) {
            //-- 非法输入: 原始版本业务全量数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "原始版本业务全量数据"
                    , old_resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == new_resource_data
                || (! new_resource_data.containsKey("resource_icon") && ! new_resource_data.containsKey("resource_link") && ! new_resource_data.containsKey("resource_name") && ! new_resource_data.containsKey("resource_parentCode") && ! new_resource_data.containsKey("resource_sort") && ! new_resource_data.containsKey("resource_type"))) {
            //-- 非法输入: 需要更新的数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_resource_data.containsKey("resource_icon")
                && ! SecurityResource.Validator.RESOURCE.icon((String) new_resource_data.get("resource_icon"))) {
            //-- 非法输入: 需要更新的数据 => 图标
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 图标"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_resource_data.containsKey("resource_link")
                && ! SecurityResource.Validator.RESOURCE.link((String) new_resource_data.get("resource_link"))) {
            //-- 非法输入: 需要更新的数据 => 资源链接
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 资源链接"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_resource_data.containsKey("resource_name")
                && ! SecurityResource.Validator.RESOURCE.name((String) new_resource_data.get("resource_name"))) {
            //-- 非法输入: 需要更新的数据 => 资源名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 资源名称"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_resource_data.containsKey("resource_parentCode")
                && ! SecurityResource.Validator.RESOURCE.parentCode((String) new_resource_data.get("resource_parentCode"))) {
            //-- 非法输入: 需要更新的数据 => [父节点 <- 资源编码]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [父节点 <- 资源编码]"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_resource_data.containsKey("resource_sort")
                && ! SecurityResource.Validator.RESOURCE.sort((Integer) new_resource_data.get("resource_sort"))) {
            //-- 非法输入: 需要更新的数据 => 序号
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 序号"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_resource_data.containsKey("resource_type_vo_value")
                && ! SecurityResource.Validator.RESOURCE.type_vo_value((Integer) new_resource_data.get("resource_type_vo_value"))) {
            //-- 非法输入: 需要更新的数据 => [资源类型 -> VO 的值]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [资源类型 -> VO 的值]"
                    , new_resource_data
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
        /*final UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        final SecurityResource latest_resource = repository.findSecurityResourceByCode(old_resource.getCode())
                .orElseGet(null);
        if (null == latest_resource) {
            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "原始版本对应数据已被删除"
                    , old_resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        } else if (latest_resource.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "原始版本对应数据异常"
                    , old_resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        old_resource = SecurityResource.Factory.RESOURCE.update(latest_resource.getId()
                , old_resource.getCode()
                , old_resource.getIcon()
                , old_resource.getLink()
                , old_resource.getName()
                , old_resource.getParentCode()
                , old_resource.getSort()
                , old_resource.getType());
        if (! latest_resource.equals(old_resource)
                || ! ObjectUtils.nullSafeEquals(old_resource.getCode(), latest_resource.getCode())
                || ! ObjectUtils.nullSafeEquals(old_resource.getIcon(), latest_resource.getIcon())
                || ! ObjectUtils.nullSafeEquals(old_resource.getLink(), latest_resource.getLink())
                || ! ObjectUtils.nullSafeEquals(old_resource.getName(), latest_resource.getName())
                || ! ObjectUtils.nullSafeEquals(old_resource.getParentCode(), latest_resource.getParentCode())
                || ! ObjectUtils.nullSafeEquals(old_resource.getSort(), latest_resource.getSort())
                || ! ObjectUtils.nullSafeEquals(old_resource.getType(), latest_resource.getType()))
        {
            //-- 非法输入: 原始版本业务全量数据 -> 已过期
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "原始版本业务全量数据"
                    , "已过期"
                    , latest_resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));

        }

        final @NotNull SecurityResource new_resource = SecurityResource.Factory.RESOURCE.update(latest_resource.getId()
                , old_resource.getCode()
                , old_resource.getIcon()
                , old_resource.getLink()
                , old_resource.getName()
                , old_resource.getParentCode()
                , old_resource.getSort()
                , old_resource.getType());
        if (new_resource_data.containsKey("resource_icon")
                && ! new_resource.setIcon((String) new_resource_data.get("resource_icon"))) {
            //-- 非法输入: 需要更新的数据 => 图标
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 图标"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_resource_data.containsKey("resource_link")
                && ! new_resource.setLink((String) new_resource_data.get("resource_link"))) {
            //-- 非法输入: 需要更新的数据 => 资源链接
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 资源链接"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_resource_data.containsKey("resource_name")
                && ! new_resource.setLink((String) new_resource_data.get("resource_name"))) {
            //-- 非法输入: 需要更新的数据 => 资源名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 资源名称"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_resource_data.containsKey("resource_parentCode")
                && ! new_resource.setParentCode((String) new_resource_data.get("resource_parentCode"))) {
            //-- 非法输入: 需要更新的数据 => [父节点 <- 资源编码]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [父节点 <- 资源编码]"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_resource_data.containsKey("resource_sort")
                && ! new_resource.setSort((Integer) new_resource_data.get("resource_sort"))) {
            //-- 非法输入: 需要更新的数据 => 序号
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 序号"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_resource_data.containsKey("resource_type_vo_value")
                && ! new_resource.setType((Integer) new_resource_data.get("resource_type_vo_value"))) {
            //-- 非法输入: 需要更新的数据 => [资源类型 -> VO 的值]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [资源类型 -> VO 的值]"
                    , new_resource_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (repository.saveAndFlush(new_resource).isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE.name
                    , "执行后数据异常"
                    , new_resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        /*//=== 操作日志记录 ===//
        final @NotNull Log newLog_SecurityRole = Log.Factory.SecurityResource.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE
                , old_resource
                , operator
                , operator_userAccountOperationInfo);
        if (! logService.insert(newLog_SecurityRole)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE.name
                    , "生成操作日志记录"
                    , newLog_SecurityRole
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//*/

        return true;
    }

    /**
     * 删除指定的资源
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分.
     *
     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean delete(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == resource || resource.isEmpty()) {
            //-- 非法输入: [（安全认证）资源]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）资源]"
                    , resource
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
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        repository.removeByCode(resource.getCode());
        if (repository.existsByCode(resource.getCode())) {
            //-- 操作失败: 删除指定的资源
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__DELETION.name
                    , "执行后数据异常"
                    , resource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }/* else {
            //=== 操作日志记录 ===//
            final @NotNull Log newLog_Resource = Log.Factory.SecurityResource.LOG.create(null
                    , null
                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__DELETION
                    , resource
                    , operator
                    , operator_userAccountOperationInfo);
            if (!logService.insert(newLog_Resource)) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__DELETION.name
                        , "生成操作日志记录"
                        , newLog_Resource
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            //======//
        }*/

        return true;
    }

}
