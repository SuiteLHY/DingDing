package github.com.suitelhy.dingding.security.service.provider.domain.service.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.repository.SecurityUserRoleRepository;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserRoleService;
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
 * （安全认证）用户 ←→ 角色
 *
 * @Description [（安全认证）用户 ←→ 角色]关联关系 - 业务接口.
 *
 * @see SecurityUserRole
 * @see SecurityUserRoleRepository
 * @see SecurityUserRoleServiceImpl
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Service("securityUserRoleService")
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public class SecurityUserRoleServiceImpl
        implements SecurityUserRoleService {

    @Autowired
    private SecurityUserRoleRepository repository;

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
    public @NotNull Page<SecurityUserRole> selectAll(int pageIndex, int pageSize) {
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
        @NotNull Sort sort = typedSort.by(SecurityUserRole::getRoleCode).ascending();
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return repository.findAll(page);
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

        long dataNumber = repository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询
     *
     * @param username 用户名称    {@link SecurityUserRole.Validator#username(String)}
     *
     * @return {@link SecurityUserRole}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull List<SecurityUserRole> selectByUsername(@NotNull String username) {
        if (! SecurityUserRole.Validator.USER_ROLE.username(username)) {
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
     * @param username  用户名称    {@link SecurityUserRole.Validator#username(String)}
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link SecurityUserRole}
     */
    @Override
    public @NotNull Page<SecurityUserRole> selectPageByUsername(@NotNull String username, int pageIndex, int pageSize)
            throws IllegalArgumentException
    {
        if (! SecurityUserRole.Validator.USER_ROLE.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        Sort.TypedSort<SecurityUserRole> typedSort = Sort.sort(SecurityUserRole.class);
        @NotNull Sort sort = typedSort.by(SecurityUserRole::getRoleCode).ascending();
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return repository.findAllByUsername(username, page);
    }

    /**
     * 查询
     *
     * @param roleCode  {@linkplain SecurityUserRole.Validator#roleCode(String) 角色编码}
     *
     * @return {@link SecurityUserRole}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull List<SecurityUserRole> selectByRoleCode(@NotNull String roleCode) {
        if (! SecurityUserRole.Validator.USER_ROLE.roleCode(roleCode)) {
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
     * @param roleCode  {@linkplain SecurityUserRole.Validator#roleCode(String) 角色编码}
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link SecurityUserRole}
     */
    @Override
    public @NotNull Page<SecurityUserRole> selectPageByRoleCode(@NotNull String roleCode, int pageIndex, int pageSize)
            throws IllegalArgumentException
    {
        if (! SecurityUserRole.Validator.USER_ROLE.roleCode(roleCode)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "角色编码"
                    , roleCode
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        Sort.TypedSort<SecurityUserRole> typedSort = Sort.sort(SecurityUserRole.class);
        @NotNull Sort sort = typedSort.by(SecurityUserRole::getRoleCode).ascending();
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return repository.findAllByRoleCode(roleCode, page);
    }

    /**
     * 查询
     *
     * @param username  {@linkplain SecurityUserRole.Validator#username(String) 用户名称}
     * @param roleCode  {@linkplain SecurityUserRole.Validator#roleCode(String) 角色编码}
     *
     * @return {@link SecurityUserRole}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull SecurityUserRole selectByUsernameAndRoleCode(@NotNull String username, @NotNull String roleCode) {
        if (! SecurityUserRole.Validator.USER_ROLE.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! SecurityUserRole.Validator.USER_ROLE.roleCode(roleCode)) {
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

    /**
     * 新增一个[（安全认证）用户 ←→ 角色]关联关系
     *
     * @param userRole  {@linkplain SecurityUserRole [（安全认证）用户 ←→ 角色]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insert(@NotNull SecurityUserRole userRole, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == userRole || ! userRole.isEntityLegal()) {
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

        if (repository.existsByUsernameAndRoleCode(userRole.getUsername(), userRole.getRoleCode())) {
            //--- 已存在相同数据 (根据 EntityID) 的情况
            return ! repository.findByUsernameAndRoleCode(userRole.getUsername(), userRole.getRoleCode())
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

        /*//=== 操作日志记录 ===//
        final @NotNull Log newLog_UserRole = Log.Factory.SecurityUserRole.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD
                , userRole
                , operator
                , operator_userAccountOperationInfo);
        if (! logService.insert(newLog_UserRole)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
                    , "生成操作日志记录"
                    , newLog_UserRole
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//*/

        return true;
    }

    /**
     * 删除指定的[（安全认证）用户 ←→ 角色]关联关系
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param userRole  {@linkplain SecurityUserRole [（安全认证）用户 ←→ 角色]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定数据是否已被删除}
     */
    @Override
    public boolean delete(@NotNull SecurityUserRole userRole, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
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
        /*if (null == operator_userAccountOperationInfo
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
        }*/

        if (! repository.existsById(userRole.getId())) {
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

            /*//=== 操作日志记录 ===//
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
            //======//*/
        }

        return true;
    }

}
