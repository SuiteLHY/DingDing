package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleResourceRepository;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRoleRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleResourceService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
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
import java.util.Set;

/**
 * （安全认证）角色 ←→ 资源
 *
 * @Description [（安全认证）角色 ←→ 资源]关联关系 - 业务接口.
 * @see SecurityUserRole
 * @see SecurityUserRoleRepository
 * @see SecurityRoleResourceServiceImpl
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Service("securityRoleResourceService")
@Order(Ordered.LOWEST_PRECEDENCE)
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public class SecurityRoleResourceServiceImpl
        implements SecurityRoleResourceService {

    @Autowired
    private SecurityRoleResourceRepository roleResourceRepository;

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    @Autowired
    private LogService logService;

    /**
     * 判断是否存在 (关联的) [（安全认证）角色]
     *
     * @param resourceCode 资源编码    {@link SecurityRoleResource.Validator#resourceCode(String)}
     * @return {@link boolean}
     */
    @Override
    public boolean existRoleByResourceCode(@NotNull String resourceCode) {
        if (!SecurityRoleResource.Validator.ROLE_RESOURCE.resourceCode(resourceCode)) {
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
     * @param roleCode 角色编码    {@link SecurityRoleResource.Validator#roleCode(String)}
     * @return {@link boolean}
     */
    @Override
    public boolean existResourceByRoleCode(@NotNull String roleCode) {
        if (!SecurityRoleResource.Validator.ROLE_RESOURCE.roleCode(roleCode)) {
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
     * @return {@link Page)
     */
    @Override
    public Page<SecurityRoleResource> selectAll(int pageIndex, int pageSize) {
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
        Sort sort = typedSort.by(SecurityRoleResource::getRoleCode).ascending()
                .and(typedSort.by(SecurityRoleResource::getResourceCode).ascending());
        Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return roleResourceRepository.findAll(page);
    }

    /**
     * 查询总页数
     *
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     * @Description 查询数据列表 - 分页 - 总页数
     */
    @Override
    public @NotNull
    Long selectCount(int pageSize) {
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
     * @param resourceCode 资源编码    {@link SecurityRoleResource.Validator#resourceCode(String)}
     * @return {@link SecurityRoleResource}
     */
    @Override
    public @NotNull
    List<SecurityRoleResource> selectByResourceCode(@NotNull String resourceCode) {
        if (!SecurityRoleResource.Validator.ROLE_RESOURCE.resourceCode(resourceCode)) {
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
     * @param roleCode 角色编码    {@link SecurityRoleResource.Validator#roleCode(String)}
     * @return {@link SecurityRoleResource}
     */
    @Override
    public @NotNull
    List<SecurityRoleResource> selectByRoleCode(@NotNull String roleCode) {
        if (!SecurityRoleResource.Validator.ROLE_RESOURCE.roleCode(roleCode)) {
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
     * @param roleResource [（安全认证）角色 ←→ 资源]    {@link SecurityRoleResource}
     * @param operator     操作者
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean insert(@NotNull SecurityRoleResource roleResource, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == roleResource || !roleResource.isEntityLegal()) {
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
        final @NotNull UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
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
        }

        if (roleResourceRepository.existsByRoleCodeAndResourceCode(roleResource.getRoleCode(), roleResource.getResourceCode())) {
            //--- 已存在相同数据 (根据 EntityID) 的情况
            return !roleResourceRepository.findByRoleCodeAndResourceCode(roleResource.getRoleCode(), roleResource.getResourceCode())
                    .get()
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

        //=== 操作日志记录 ===//
        final @NotNull Log newLog_RoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
                , roleResource
                , operator
                , operatorOperationInfo);
        if (!logService.insert(newLog_RoleResource)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
                    , "生成操作日志记录"
                    , newLog_RoleResource
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

//    /**
//     * 新增多个[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param role          [（安全认证）角色]
//     * @param resources     [（安全认证）资源]
//     * @param operator      操作者
//     *
//     * @return 操作是否成功 / 是否已存在完全相同的有效数据集合
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean insert(@NotNull SecurityRole role, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if ((null == role || role.isEmpty())) {
//            //-- 非法输入: [（安全认证）角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）角色]"
//                    , role
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == resources || resources.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）资源]"
//                    , resources
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        } else {
//            for (SecurityResource each : resources) {
//                if ((null == each || each.isEmpty())) {
//                    //-- 非法输入: [（安全认证）资源]
//                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , "[（安全认证）资源]"
//                            , each
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operator_OperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        if (!roleResourceRepository.existsAllByRoleCode(role.getCode())) {
//            //--- 角色不存在绑定的资源的情况
//            for (SecurityResource each : resources) {
//                final SecurityRoleResource eachRoleResource = SecurityRoleResource.FactoryModel.ROLE_RESOURCE.create(role.getCode(), each.getCode());
//                if (roleResourceRepository/*.saveAndFlush*/.save(eachRoleResource)
//                        .isEmpty()) {
//                    //-- 操作失败: 新增[（安全认证）角色 ←→ 资源]关联关系
//                    throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                            , eachRoleResource
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                } else {
//                    //=== 操作日志记录 ===//
//                    final Log newLog_UserRole = Log.Factory.SecurityRoleResource.LOG.create(null
//                            , null
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
//                            , eachRoleResource
//                            , operator
//                            , operator_OperationInfo);
//                    if (!logService.insert(newLog_UserRole)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                                , "生成操作日志记录"
//                                , newLog_UserRole
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                    //======//
//                }
//            }
//        } else {
//            //--- 角色存在绑定的资源的情况
//            final List<SecurityRoleResource> existedRoleResources = roleResourceRepository.findAllByRoleCode(role.getCode());
//
//            for (SecurityResource eachResource : resources) {
//                boolean exist = false;
//                for (SecurityRoleResource existedRoleResource : existedRoleResources) {
//                    if (existedRoleResource.equals(role, eachResource)) {
//                        exist = true;
//                        break;
//                    }
//                }
//                if (exist) continue;
//
//                final SecurityRoleResource eachRoleResource = SecurityRoleResource.FactoryModel.ROLE_RESOURCE.create(role.getCode(), eachResource.getCode());
//                if (roleResourceRepository/*.saveAndFlush*/.save(eachRoleResource)
//                        .isEmpty()) {
//                    //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
//                    throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                            , eachRoleResource
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                } else {
//                    //=== 操作日志记录 ===//
//                    final Log newLog_RoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
//                            , null
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
//                            , eachRoleResource
//                            , operator
//                            , operator_OperationInfo);
//                    if (!logService.insert(newLog_RoleResource)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                                , "生成操作日志记录"
//                                , newLog_RoleResource
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                    //======//
//                }
//            }
//        }
//
//        return true;
//    }

    /**
     * 删除指定的[（安全认证）角色 ←→ 资源]关联关系
     *
     * @param roleResource                      [（安全认证）角色 ←→ 资源]    {@link SecurityRoleResource}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean delete(@NotNull SecurityRoleResource roleResource, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException {
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
        if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || !operator_userAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (!roleResourceRepository.existsByRoleCodeAndResourceCode(roleResource.getRoleCode(), roleResource.getResourceCode())) {
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

            //=== 操作日志记录 ===//
            final @NotNull Log newLog_RoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
                    , null
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION
                    , roleResource
                    , operator
                    , operator_userAccountOperationInfo);
            if (!logService.insert(newLog_RoleResource)) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
                        , "生成操作日志记录"
                        , newLog_RoleResource
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            //======//
        }

        return true;
    }

//    /**
//     * 删除指定的 [（安全认证）角色] 的所有[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param role      [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean delete(@NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == role || role.isEmpty()) {
//            //-- 非法输入: [（安全认证）角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）角色]"
//                    , role
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operatorOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final List<SecurityRoleResource> roleResources = roleResourceRepository.findAllByRoleCode(role.getCode());
//        if (null == roleResources || roleResources.isEmpty()) {
//            return true;
//        } else {
//            roleResourceRepository.removeByRoleCode(role.getCode());
//            if (roleResourceRepository.existsAllByRoleCode(role.getCode())) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
//                        , "执行后数据异常"
//                        , role
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            //=== 操作日志记录 ===//
//            for (SecurityRoleResource roleResource : roleResources) {
//                final Log newLog_RoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
//                        , null
//                        , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION
//                        , roleResource
//                        , operator
//                        , operatorOperationInfo);
//                if (!logService.insert(newLog_RoleResource)) {
//                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
//                            , "生成操作日志记录"
//                            , newLog_RoleResource
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//            //======//
//        }
//
//        return true;
//    }
//
//    /**
//     * 删除指定的 [（安全认证）资源] 的所有[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param resource      [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean delete(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == resource || resource.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）资源]"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final List<SecurityRoleResource> roleResources = roleResourceRepository.findAllByResourceCode(resource.getCode());
//        if (null == roleResources || roleResources.isEmpty()) {
//            return true;
//        } else {
//            roleResourceRepository.removeByResourceCode(resource.getCode());
//            if (roleResourceRepository.existsAllByResourceCode(resource.getCode())) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
//                        , "执行后数据异常"
//                        , resource
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            //=== 操作日志记录 ===//
//            for (SecurityRoleResource roleResource : roleResources) {
//                final Log newLog_RoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
//                        , null
//                        , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION
//                        , roleResource
//                        , operator
//                        , operatorOperationInfo);
//                if (!logService.insert(newLog_RoleResource)) {
//                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
//                            , "生成操作日志记录"
//                            , newLog_RoleResource
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//            //======//
//        }
//
//        return true;
//    }

//    /**
//     * 删除[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param resource  [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
//     * @param resource  [（安全认证）资源], 必须合法且已持久化.  {@link SecurityResource}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Override
//    public boolean delete(@NotNull SecurityRole role, @NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == role || role.isEmpty()) {
//            //-- 非法输入: [（安全认证）角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）角色]"
//                    , role
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == resource || resource.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）资源]"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        final UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operatorOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final SecurityRoleResource roleResource = roleResourceRepository.findByRoleCodeAndResourceCode(role.getCode(), resource.getCode())
//                .orElse(null);
//        if (null == roleResource) {
//            return true;
//        } else {
//            roleResourceRepository.removeByRoleCodeAndResourceCode(role.getCode(), resource.getCode());
//            if (roleResourceRepository.existsByRoleCodeAndResourceCode(role.getCode(), resource.getCode())) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
//                        , "执行后数据异常"
//                        , role
//                        , resource
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            //=== 操作日志记录 ===//
//            final Log newLog_RoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
//                    , null
//                    , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION
//                    , roleResource
//                    , operator
//                    , operatorOperationInfo);
//            if (!logService.insert(newLog_RoleResource)) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
//                        , "生成操作日志记录"
//                        , newLog_RoleResource
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//            //======//
//        }
//
//        return true;
//    }

}
