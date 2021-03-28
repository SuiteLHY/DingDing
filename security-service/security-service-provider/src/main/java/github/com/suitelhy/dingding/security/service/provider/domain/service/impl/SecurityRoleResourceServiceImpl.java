package github.com.suitelhy.dingding.security.service.provider.domain.service.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRoleResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.repository.SecurityRoleResourceRepository;
import github.com.suitelhy.dingding.security.service.provider.domain.repository.SecurityUserRoleRepository;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityRoleResourceService;
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
import java.util.List;

/**
 * （安全认证）角色 ←→ 资源
 *
 * @Description [（安全认证）角色 ←→ 资源]关联关系 - 业务接口.
 *
 * @see SecurityUserRole
 * @see SecurityUserRoleRepository
 * @see SecurityRoleResourceServiceImpl
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Service("securityRoleResourceService")
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public class SecurityRoleResourceServiceImpl
        implements SecurityRoleResourceService {

    @Autowired
    private SecurityRoleResourceRepository roleResourceRepository;

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @param resourceCode  {@linkplain SecurityRoleResource.Validator#resourceCode(String) 资源编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public boolean existRoleByResourceCode(@NotNull String resourceCode) {
        if (! SecurityRoleResource.Validator.ROLE_RESOURCE.resourceCode(resourceCode)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "资源编码"
                    , resourceCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        /*final List<Map<String, Object>> roleList = roleResourceRepository.selectRoleByResourceCode(resourceCode);
        if (null != roleList && !roleList.isEmpty()) {
            for (Map<String, Object> each : roleList) {
                final SecurityRole eachRole = SecurityRole.Factory.ROLE.update(
                        Long.parseLong(String.valueOf(each.get("role_id")))
                        , (String) each.get("role_code")
                        , (String) each.get("role_name")
                        , (String) each.get("role_description"));
                if (!eachRole.isEmpty()) {
                    return true;
                }
            }
        }

        return false;*/

        return roleResourceRepository.existsAllByResourceCode(resourceCode);
    }

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @param roleCode  {@linkplain SecurityRoleResource.Validator#roleCode(String) 角色编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public boolean existResourceByRoleCode(@NotNull String roleCode) {
        if (! SecurityRoleResource.Validator.ROLE_RESOURCE.roleCode(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "角色编码"
                    , roleCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        /*final List<Map<String, Object>> roleList = roleResourceRepository.selectRoleByResourceCode(resourceCode);
        if (null != roleList && !roleList.isEmpty()) {
            for (Map<String, Object> each : roleList) {
                final SecurityRole eachRole = SecurityRole.Factory.ROLE.update(
                        Long.parseLong(String.valueOf(each.get("role_id")))
                        , (String) each.get("role_code")
                        , (String) each.get("role_name")
                        , (String) each.get("role_description"));
                if (!eachRole.isEmpty()) {
                    return true;
                }
            }
        }

        return false;*/

        return roleResourceRepository.existsAllByRoleCode(roleCode);
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
    public @NotNull Page<SecurityRoleResource> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            //-- 非法输入: <param>pageIndex</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageIndex"
                    , pageIndex
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        Sort.TypedSort<SecurityRoleResource> typedSort = Sort.sort(SecurityRoleResource.class);
        @NotNull Sort sort = typedSort.by(SecurityRoleResource::getRoleCode).ascending()
                .and(typedSort.by(SecurityRoleResource::getResourceCode).ascending());
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return roleResourceRepository.findAll(page);
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
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        long dataNumber = roleResourceRepository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询
     *
     * @param resourceCode  {@linkplain SecurityRoleResource.Validator#resourceCode(String) 资源编码}
     *
     * @return {@link SecurityRoleResource}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull List<SecurityRoleResource> selectByResourceCode(@NotNull String resourceCode) {
        if (! SecurityRoleResource.Validator.ROLE_RESOURCE.resourceCode(resourceCode)) {
            //-- 非法输入: 资源编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "资源编码"
                    , resourceCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return roleResourceRepository.findAllByResourceCode(resourceCode);
    }

    /**
     * 查询
     *
     * @param roleCode  {@linkplain SecurityRoleResource.Validator#roleCode(String) 角色编码}
     *
     * @return {@link SecurityRoleResource}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull List<SecurityRoleResource> selectByRoleCode(@NotNull String roleCode) {
        if (! SecurityRoleResource.Validator.ROLE_RESOURCE.roleCode(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "角色编码"
                    , roleCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return roleResourceRepository.findAllByRoleCode(roleCode);
    }

    /**
     * 新增一个[（安全认证）角色 ←→ 资源]关联关系
     *
     * @param roleResource  {@linkplain SecurityRoleResource [（安全认证）角色 ←→ 资源]}
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insert(@NotNull SecurityRoleResource roleResource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == roleResource || ! roleResource.isEntityLegal()) {
            //-- 非法输入: [（安全认证）角色 ←→ 资源]关联关系
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[（安全认证）角色 ←→ 资源]关联关系"
                    , roleResource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*final @NotNull UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operatorOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        if (roleResourceRepository.existsByRoleCodeAndResourceCode(roleResource.getRoleCode(), roleResource.getResourceCode())) {
            //--- 已存在相同数据 (根据 EntityID) 的情况
            return ! roleResourceRepository.findByRoleCodeAndResourceCode(roleResource.getRoleCode(), roleResource.getResourceCode())
                    .orElseGet(SecurityRoleResource.Factory.ROLE_RESOURCE::createDefault)
                    .isEmpty();
        }

        if (roleResourceRepository.saveAndFlush(roleResource).isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
                    , "执行后数据异常"
                    , roleResource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        /*//=== 操作日志记录 ===//
        final @NotNull Log newLog_RoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
                , roleResource
                , operator
                , operatorOperationInfo);
        if (! logService.insert(newLog_RoleResource)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
                    , "生成操作日志记录"
                    , newLog_RoleResource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//*/

        return true;
    }

    /**
     * 删除指定的关联关系
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分
     *
     * @param roleResource  {@linkplain SecurityRoleResource [（安全认证）角色 ←→ 资源]}
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定的[（安全认证）角色 ←→ 资源]已被删除}
     */
    @Override
    public boolean delete(@NotNull SecurityRoleResource roleResource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == roleResource || roleResource.isEmpty()) {
            //-- 非法输入: [（安全认证）角色 ←→ 资源]关联关系
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[（安全认证）角色 ←→ 资源]关联关系"
                    , roleResource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*final UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
        /*if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || ! operator_userAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }*/

        if (! roleResourceRepository.existsByRoleCodeAndResourceCode(roleResource.getRoleCode(), roleResource.getResourceCode())) {
            return true;
        } else {
            roleResourceRepository.removeByRoleCodeAndResourceCode(roleResource.getRoleCode(), roleResource.getResourceCode());
            if (roleResourceRepository.existsByRoleCodeAndResourceCode(roleResource.getRoleCode(), roleResource.getResourceCode())) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
                        , "执行后数据异常"
                        , roleResource
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            /*//=== 操作日志记录 ===//
            final @NotNull Log newLog_RoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
                    , null
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION
                    , roleResource
                    , operator
                    , operator_userAccountOperationInfo);
            if (! logService.insert(newLog_RoleResource)) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
                        , "生成操作日志记录"
                        , newLog_RoleResource
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            //======//*/
        }

        return true;
    }

}
