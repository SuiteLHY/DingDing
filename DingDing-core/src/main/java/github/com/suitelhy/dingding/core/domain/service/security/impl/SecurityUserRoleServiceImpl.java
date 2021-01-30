package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRoleRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserRoleService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
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
import java.util.*;

/**
 * （安全认证）用户 ←→ 角色
 *
 * @Description [（安全认证）用户 ←→ 角色]关联关系 - 业务接口.
 * @see SecurityUserRole
 * @see SecurityUserRoleRepository
 * @see SecurityUserRoleServiceImpl
 * @see LogService
 */
@Service("securityUserRoleService")
@Order(Ordered.LOWEST_PRECEDENCE)
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public class SecurityUserRoleServiceImpl
        implements SecurityUserRoleService {

    @Autowired
    private SecurityUserRoleRepository repository;

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    @Autowired
    private LogService logService;

//    /**
//     * 判断是否存在 (关联的) [（安全认证）角色]
//     *
//     * @param username 用户名称    {@link SecurityUserRole.Validator#username(String)}
//     *
//     * @return {@link boolean}
//     */
//    @Override
//    public boolean existRoleByUsername(@NotNull String username) {
//        if (! SecurityUserRole.Validator.USER_ROLE.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final List<Map<String, Object>> roleList = repository.selectRoleByUsername(username);
//        if (null != roleList && ! roleList.isEmpty()) {
//            for (@NotNull Map<String, Object> each : roleList) {
//                final SecurityRole eachRole = SecurityRole.Factory.ROLE.update(
//                        Long.parseLong(String.valueOf(each.get("role_id")))
//                        , (String) each.get("role_code")
//                        , (String) each.get("role_name")
//                        , (String) each.get("role_description"));
//                if (! eachRole.isEmpty()) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @return {@link Page)
     */
    @Override
    public Page<SecurityUserRole> selectAll(int pageIndex, int pageSize) {
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

        Sort.TypedSort<SecurityUserRole> typedSort = Sort.sort(SecurityUserRole.class);
        Sort sort = typedSort.by(SecurityUserRole::getRoleCode).ascending();
        Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return repository.findAll(page);
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

        long dataNumber = repository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询
     *
     * @param username
     * @return {@link SecurityUserRole}
     */
    @Override
    public @NotNull
    List<SecurityUserRole> selectByUsername(@NotNull String username) {
        if (!SecurityUserRole.Validator.USER_ROLE.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.findAllByUsername(username);
    }

    /**
     * 查询
     *
     * @param roleCode
     * @return {@link SecurityUserRole}
     */
    @Override
    public @NotNull
    List<SecurityUserRole> selectByRoleCode(@NotNull String roleCode) {
        if (!SecurityUserRole.Validator.USER_ROLE.roleCode(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "角色编码"
                    , roleCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.findAllByRoleCode(roleCode);
    }

    /**
     * 查询
     *
     * @param username 用户名称    {@link SecurityUserRole.Validator#username(String)}
     * @param roleCode 角色编码    {@link SecurityUserRole.Validator#roleCode(String)}
     * @return {@link SecurityUserRole}
     */
    @Override
    public @NotNull
    SecurityUserRole selectByUsernameAndRoleCode(@NotNull String username, @NotNull String roleCode) {
        if (!SecurityUserRole.Validator.USER_ROLE.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (!SecurityUserRole.Validator.USER_ROLE.roleCode(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "角色编码"
                    , roleCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.findByUsernameAndRoleCode(username, roleCode)
                .orElse(SecurityUserRole.Factory.USER_ROLE.createDefault());
    }

//    /**
//     * 查询 (关联的) [（安全认证）角色]
//     *
//     * @param username 用户名称    {@link SecurityUserRole.Validator#username(String)}
//     *
//     * @return {@link SecurityRole}
//     */
//    @Override
//    public @NotNull List<SecurityRole> selectRoleByUsername(@NotNull String username) {
//        if (! SecurityUserRole.Validator.USER_ROLE.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityRole> result;
//
//        final List<Map<String, Object>> roleList = repository.selectRoleByUsername(username);
//        if (null != roleList && !roleList.isEmpty()) {
//            result = new ArrayList<>(roleList.size());
//
//            for (@NotNull Map<String, Object> each : roleList) {
//                final @NotNull SecurityRole eachRole = SecurityRole.Factory.ROLE.update(
//                        Long.parseLong(String.valueOf(each.get("role_id")))
//                        , (String) each.get("role_code")
//                        , (String) each.get("role_name")
//                        , (String) each.get("role_description"));
//                if (! eachRole.isEmpty()) {
//                    result.add(eachRole);
//                }
//            }
//        } else {
//            result = new ArrayList<>(0);
//        }
//
//        return result;
//    }

    /**
     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param userRole                          [（安全认证）用户 ←→ 角色]    {@link SecurityUserRole}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean insert(@NotNull SecurityUserRole userRole, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == userRole || !userRole.isEntityLegal()) {
            //-- 非法输入: [（安全认证）用户 ←→ 角色]关联关系
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）用户 ←→ 角色]关联关系"
                    , userRole
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
        if (null == operator_userAccountOperationInfo
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
        }

        if (repository.existsByUsernameAndRoleCode(userRole.getUsername(), userRole.getRoleCode())) {
            //--- 已存在相同数据 (根据 EntityID) 的情况
            return !repository.findByUsernameAndRoleCode(userRole.getUsername(), userRole.getRoleCode())
                    .orElse(SecurityUserRole.Factory.USER_ROLE.createDefault())
                    .isEmpty();
        }

        if (repository.saveAndFlush(userRole).isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
                    , "执行后数据异常"
                    , userRole
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final @NotNull Log newLog_UserRole = Log.Factory.SecurityUserRole.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD
                , userRole
                , operator
                , operator_userAccountOperationInfo);
        if (!logService.insert(newLog_UserRole)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
                    , "生成操作日志记录"
                    , newLog_UserRole
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

//    /**
//     * 新增多个[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user      （安全认证）用户
//     * @param roles     （安全认证）角色
//     * @param operator  操作者
//     *
//     * @return 操作是否成功 / 是否已存在完全相同的有效数据集合
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if ((null == user || user.isEmpty())) {
//            //-- 非法输入: [（安全认证）用户]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）用户]"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == roles || roles.isEmpty()) {
//            //-- 非法输入: [（安全认证）角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）角色]"
//                    , roles
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        } else {
//            for (SecurityRole each : roles) {
//                if ((null == each || each.isEmpty())) {
//                    //-- 非法输入: [（安全认证）角色]
//                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , "[（安全认证）角色]"
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
//        if (!repository.existsAllByUsername(user.getUsername())) {
//            //--- 角色不存在绑定的资源的情况
//            for (SecurityRole each : roles) {
//                final SecurityUserRole eachUserRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), each.getCode());
//                if (repository/*.saveAndFlush*/.save(eachUserRole)
//                        .isEmpty()) {
//                    //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
//                    throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                            , eachUserRole
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                } else {
//                    //=== 操作日志记录 ===//
//                    final Log newLog_UserRole = Log.Factory.SecurityUserRole.LOG.create(null
//                            , null
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD
//                            , eachUserRole
//                            , operator
//                            , operatorOperationInfo);
//                    if (!logService.insert(newLog_UserRole)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                                , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
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
//            final List<SecurityUserRole> existedUserRoles = repository.findAllByUsername(user.getUsername());
//
//            for (SecurityRole eachRole : roles) {
//                boolean exist = false;
//                for (SecurityUserRole existedUserRole : existedUserRoles) {
//                    if (existedUserRole.equals(user, eachRole)) {
//                        exist = true;
//                        break;
//                    }
//                }
//                if (exist) continue;
//
//                final SecurityUserRole eachUserRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), eachRole.getCode());
//                if (repository/*.saveAndFlush*/.save(eachUserRole)
//                        .isEmpty()) {
//                    //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
//                    throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                            , eachUserRole
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                } else {
//                    //=== 操作日志记录 ===//
//                    final Log newLog_UserRole = Log.Factory.SecurityUserRole.LOG.create(null
//                            , null
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD
//                            , eachUserRole
//                            , operator
//                            , operatorOperationInfo);
//                    if (!logService.insert(newLog_UserRole)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                                , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                                , "生成操作日志记录"
//                                , newLog_UserRole
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
//
//    /**
//     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user     （安全认证）用户
//     * @param roleVo   [(安全) 用户 -> 角色] {@link Security.RoleVo}, 仅需保证合法性, 不需要保证持久化.
//     * @param operator 操作者
//     *
//     * @return 操作是否成功 / 是否已存在相同的有效数据
//     */
//    @Override
//    public boolean insertUserRoleRelationship(@NotNull SecurityUser user, @NotNull Security.RoleVo roleVo, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if ((null == user || user.isEmpty())) {
//            //-- 非法输入: [（安全认证）用户]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）用户]"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == roleVo) {
//            //-- 非法输入: [(安全) 用户 -> 角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[(安全) 用户 -> 角色]"
//                    , roleVo
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
//        if (!repository.existsAllByUsername(user.getUsername())) {
//            //--- 角色不存在绑定的资源的情况
//            SecurityRole role = SecurityRole.Factory.ROLE.create(roleVo);
//
//            final SecurityUserRole userRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), role.getCode());
//            if (repository/*.saveAndFlush*/.save(userRole).isEmpty()) {
//                //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
//                throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                        , userRole
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            } else {
//                //=== 操作日志记录 ===//
//                final Log newLog_UserRole = Log.Factory.SecurityUserRole.LOG.create(null
//                        , null
//                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD
//                        , userRole
//                        , operator
//                        , operatorOperationInfo);
//                if (!logService.insert(newLog_UserRole)) {
//                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                            , "生成操作日志记录"
//                            , newLog_UserRole
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//                //======//
//            }
//        } else {
//            //--- 角色存在绑定的资源的情况
//            List<SecurityUserRole> existedUserRoles = repository.findAllByUsername(user.getUsername());
//
//            SecurityRole role = SecurityRole.Factory.ROLE.create(roleVo);
//
//            boolean exist = false;
//            for (SecurityUserRole existedUserRole : existedUserRoles) {
//                if (existedUserRole.equals(user, role)) {
//                    exist = true;
//                    break;
//                }
//            }
//            if (exist) {
//                return true;
//            } else {
//                SecurityUserRole userRole = SecurityUserRole.Factory.USER_ROLE.create(user.getUsername(), role.getCode());
//                if (repository/*.saveAndFlush*/.save(userRole).isEmpty()) {
//                    //-- 操作失败: 新增[（安全认证）用户 ←→ 角色]关联关系
//                    throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                            , userRole
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                } else {
//                    //=== 操作日志记录 ===//
//                    final Log newLog_UserRole = Log.Factory.SecurityUserRole.LOG.create(null
//                            , null
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD
//                            , userRole
//                            , operator
//                            , operatorOperationInfo);
//                    if (!logService.insert(newLog_UserRole)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                                , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
//                                , "生成操作日志记录"
//                                , newLog_UserRole
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
     * 删除指定的[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param userRole                          [（安全认证）用户 ←→ 角色]    {@link SecurityUserRole}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 -> 账户操作基础记录]
     * @return 操作是否成功
     * @Description 删除成功后校验持久化数据;
     * 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean delete(@NotNull SecurityUserRole userRole, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == userRole || userRole.isEmpty()) {
            //-- 非法输入: [（安全认证）用户 ←→ 角色]关联关系
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[（安全认证）用户 ←→ 角色]关联关系"
                    , userRole
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
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
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

        if (!repository.existsById(userRole.getId())) {
            return true;
        } else {
            repository.deleteById(userRole.getId());
            if (repository.existsById(userRole.getId())) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
                        , "执行后数据异常"
                        , userRole
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            //=== 操作日志记录 ===//
            final @NotNull Log newLog_UserRole = Log.Factory.SecurityUserRole.LOG.create(null
                    , null
                    , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION
                    , userRole
                    , operator
                    , operator_userAccountOperationInfo);
            if (!logService.insert(newLog_UserRole)) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
                        , "生成操作日志记录"
                        , newLog_UserRole
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            //======//
        }

        return true;
    }

//    /**
//     * 删除指定的 [（安全认证）用户] 的所有[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user      [（安全认证）用户], 必须合法且已持久化.  {@link SecurityUser}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    public boolean delete(@NotNull SecurityUser user, @NotNull SecurityUser operator)
//            throws BusinessAtomicException {
//        if (null == user || user.isEmpty()) {
//            //-- 非法输入: [（安全认证）用户]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）用户]"
//                    , user
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
//        final List<SecurityUserRole> userRoles = repository.findAllByUsername(user.getUsername());
//        if (null == userRoles || userRoles.isEmpty()) {
//            return true;
//        } else {
//            repository.removeByUsername(user.getUsername());
//            if (repository.existsAllByUsername(user.getUsername())) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
//                        , "执行后数据异常"
//                        , user
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            //=== 操作日志记录 ===//
//            for (SecurityUserRole userRole : userRoles) {
//                final Log newLog_UserRole = Log.Factory.SecurityUserRole.LOG.create(null
//                        , null
//                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION
//                        , userRole
//                        , operator
//                        , operatorOperationInfo);
//                if (!logService.insert(newLog_UserRole)) {
//                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
//                            , "生成操作日志记录"
//                            , newLog_UserRole
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
//     * 删除指定的 [（安全认证）角色] 的所有[（安全认证）用户 ←→ 角色]关联关系
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
//            throws BusinessAtomicException {
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
//        final List<SecurityUserRole> userRoles = repository.findAllByRoleCode(role.getCode());
//        if (null == userRoles || userRoles.isEmpty()) {
//            return true;
//        } else {
//            repository.removeByRoleCode(role.getCode());
//            if (repository.existsAllByRoleCode(role.getCode())) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
//                        , "执行后数据异常"
//                        , role
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            //=== 操作日志记录 ===//
//            for (SecurityUserRole userRole : userRoles) {
//                final Log newLog_UserRole = Log.Factory.SecurityUserRole.LOG.create(null
//                        , null
//                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION
//                        , userRole
//                        , operator
//                        , operatorOperationInfo);
//                if (!logService.insert(newLog_UserRole)) {
//                    throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                            , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
//                            , "生成操作日志记录"
//                            , newLog_UserRole
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
//     * 删除[（安全认证）用户 ←→ 角色]关联关系
//     *
//     * @param user      [（安全认证）用户], 必须合法且已持久化.  {@link SecurityUser}
//     * @param role      [（安全认证）角色], 必须合法且已持久化.  {@link SecurityRole}
//     * @param operator  操作者
//     *
//     * @return 操作是否成功
//     */
//    @Override
//    public boolean delete(@NotNull SecurityUser user, @NotNull SecurityRole role, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == user || user.isEmpty()) {
//            //-- 非法输入: [（安全认证）用户]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[（安全认证）用户]"
//                    , user
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
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
//        final SecurityUserRole userRole = repository.findByUsernameAndRoleCode(user.getUsername(), role.getCode()).orElse(null);
//        if (null == userRole) {
//            return true;
//        } else {
//            repository.removeByUsernameAndRoleCode(user.getUsername(), role.getCode());
//            if (repository.existsByUsernameAndRoleCode(user.getUsername(), role.getCode())) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
//                        , "执行后数据异常"
//                        , user
//                        , role
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            //=== 操作日志记录 ===//
//            final Log newLog_UserRole = Log.Factory.SecurityUserRole.LOG.create(null
//                    , null
//                    , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION
//                    , userRole
//                    , operator
//                    , operatorOperationInfo);
//            if (!logService.insert(newLog_UserRole)) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__DELETION.name
//                        , "生成操作日志记录"
//                        , newLog_UserRole
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
