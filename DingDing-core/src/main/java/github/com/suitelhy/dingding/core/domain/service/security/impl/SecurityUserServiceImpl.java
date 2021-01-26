package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserRoleService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 用户
 *
 * @Description (安全) 用户 - 业务实现.
 *
 * @see SecurityUser
 * @see SecurityUserRepository
 * @see SecurityUserService
 * @see SecurityRoleService
 * @see SecurityUserRoleService
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Service("securityUserService")
@Order(Ordered.LOWEST_PRECEDENCE)
public class SecurityUserServiceImpl
        implements SecurityUserService {

    @Autowired
    private SecurityUserRepository repository;

    @Autowired
    private SecurityRoleService securityRoleService;

    /*@Autowired
    private SecurityUserRoleService securityUserRoleService;

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;*/

    @Autowired
    private UserEvent userEvent;

    @Autowired
    private SecurityRoleEvent securityRoleEvent;

    @Autowired
    private LogService logService;


    /**
     * 判断存在
     *
     * @param userId {@link SecurityUser#getUserId()}
     *
     * @return 判断结果
     *
     * @throws IllegalArgumentException
     */
    @Override
    public boolean existByUserId(@NotNull String userId)
            throws IllegalArgumentException
    {
        if (!SecurityUser.Validator.USER.userId(userId)) {
            //-- 非法输入: [用户 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 ID]"
                    , userId
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.existsByUserId(userId);
    }

    /**
     * 判断存在
     *
     * @param username {@link SecurityUser#getUsername()}
     *
     * @return 判断结果
     *
     * @throws IllegalArgumentException
     */
    @Override
    public boolean existByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        if (!SecurityUser.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.existsByUsername(username);
    }

    /**
     * 是否具有管理员权限
     *
     * @param username  用户名称    {@link SecurityUser.Validator#username(String)}
     *
     * @return {@link Boolean}
     *
     * @throws IllegalArgumentException
     */
    @Override
    public boolean existAdminPermission(@NotNull String username)
            throws IllegalArgumentException
    {
        if (null == username || !SecurityUser.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        List<Map<String, Object>> roleList = repository.selectRoleByUsername(username);
        if (null != roleList && !roleList.isEmpty()) {
            for (final Map<String, Object> each : roleList) {
                SecurityRole eachRole = SecurityRole.Factory.ROLE.update(
                        Long.parseLong(String.valueOf(each.get("id")))
                        , (String) each.get("code")
                        , (String) each.get("name")
                        , (String) each.get("description"));
                if (SecurityRole.Validator.isAdminRole(eachRole)) {
                    return true;
                }
            }
        }
        return false;
    }

//    /**
//     * 判断是否存在 (关联的) [（安全认证）角色]
//     *
//     * @param username 用户名称    {@link SecurityUser.Validator#username(String)}
//     *
//     * @return {@link boolean}
//     */
//    @Override
//    public boolean existRoleByUsername(@NotNull String username) {
//        if (!SecurityUser.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return securityUserRoleService.existRoleByUsername(username);
//    }

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize
     *
     * @return
     */
    @Override
    public Page<SecurityUser> selectAll(int pageIndex, int pageSize) {
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

        Sort.TypedSort<SecurityUser> typedSort = Sort.sort(SecurityUser.class);
        Sort sort = typedSort.by(SecurityUser::getUserId).ascending()
                .and(typedSort.by(SecurityUser::getStatus).ascending());
        Pageable page = PageRequest.of(pageIndex, pageSize, sort);

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
     * 查询指定的用户
     *
     * @param userId    [用户 ID] {@link SecurityUser.Validator#userId(String)}
     *
     * @return {@link SecurityUserRole}
     */
    @Override
    public @NotNull SecurityUser selectByUserId(@NotNull String userId) {
        if (! SecurityUser.Validator.USER.userId(userId)) {
            //-- 非法输入: [用户 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 ID]"
                    , userId
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.findById(userId)
                .orElse(SecurityUser.Factory.USER.createDefault());
    }

    /**
     * 查询指定的用户
     *
     * @param username  用户名称    {@link SecurityUser.Validator#username(String)}
     *
     * @return {@link SecurityUserRole}
     */
    @Override
    public @NotNull SecurityUser selectByUsername(@NotNull String username) {
        if (! SecurityUser.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.findByUsername(username)
                .orElse(SecurityUser.Factory.USER.createDefault());
    }

//    /**
//     * 查询 (关联的) 角色
//     *
//     * @param username  用户名称    {@link SecurityUser.Validator#username(String)}
//     *
//     * @return {@link SecurityRole}
//     */
//    @Override
//    public @NotNull List<SecurityRole> selectRoleByUsername(@NotNull String username) {
//        if (!SecurityUser.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        final @NotNull List<SecurityRole> result = new ArrayList<>(0);;
//
//        final List<Map<String, Object>> roleList = repository.selectRoleByUsername(username);
//        if (null != roleList && !roleList.isEmpty()) {
//            for (Map<String, Object> each : roleList) {
//                if (null == each.get("id")) {
//                    continue;
//                }
//
//                final SecurityRole eachRole = SecurityRole.Factory.ROLE.update(Long.parseLong(String.valueOf(each.get("id")))
//                        , (String) each.get("code")
//                        , (String) each.get("name")
//                        , (String) each.get("description"));
//                if (!eachRole.isEmpty()) {
//                    result.add(eachRole);
//                }
//            }
//        }
//
//        return result;
//    }

//    /**
//     * 查询[角色 -> (关联的) 资源]
//     *
//     * @param username  用户名称    {@link SecurityUser.Validator#username(String)}
//     *
//     * @return {@link SecurityResource}
//     */
//    @Override
//    public @NotNull List<SecurityResource> selectResourceByUsername(@NotNull String username)
//            throws IllegalArgumentException
//    {
//        if (!SecurityUser.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        /*final List<Map<String, Object>> userRoles = userRoleRepository.selectRoleByUsername(username);
//        if (null == userRoles || userRoles.isEmpty()) {
//            return null;
//        }
//
//        final List<Map<String, Object>> roleResources = new ArrayList<>(userRoles.size());
//        for (Map<String, Object> each : userRoles) {
//            List<Map<String, Object>> eachResources = roleResourceRepository.selectResourceByRoleCode((String) each.get("role_code"));
//
//            roleResources.addAll(eachResources);
//        }*/
//
//        final List<SecurityResource> result;
//
//        final List<Map<String, Object>> resourceList = repository.selectResourceByUsername(username);
//        if (null != resourceList && !resourceList.isEmpty()) {
//            result = new ArrayList<>(resourceList.size());
//
//            for (Map<String, Object> each : resourceList) {
//                final SecurityResource eachResource = SecurityResource.Factory.RESOURCE.update(
//                        Long.parseLong(String.valueOf(each.get("id")))
//                        , (String) each.get("code")
//                        , (String) each.get("icon")
//                        , (String) each.get("link")
//                        , (String) each.get("name")
//                        , (String) each.get("parent_code")
//                        , Integer.parseInt(String.valueOf(each.get("sort")))
//                        , VoUtil.getInstance().getVoByValue(Resource.TypeVo.class, Integer.parseInt(String.valueOf(each.get("type"))))
//                );
//                if (!eachResource.isEmpty()) {
//                    result.add(eachResource);
//                }
//            }
//        } else {
//            result = new ArrayList<>(0);
//        }
//
//        return result;
//    }

//    /**
//     * 查询 (关联的) 角色 -> (关联的) 资源
//     *
//     * @param username  用户名称         {@link SecurityUser.Validator#username(String)}
//     * @param clientId  [资源服务器 ID]  {@link SecurityResourceUrl.Validator#clientId(String)}
//     *
//     * @return 资源集合                 {@link SecurityResource}
//     */
//    @Override
//    public List<Map<String, Object>> selectUrlPathByUsernameAndClientId(@NotNull String username, @NotNull String clientId) {
//        if (! SecurityUser.Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (! SecurityResourceUrl.Validator.RESOURCE_URL.clientId(clientId)) {
//            //-- 非法输入: [资源服务器 ID]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[资源服务器 ID]"
//                    , clientId
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return repository.selectURLByUsernameAndClientId(username, clientId);
//    }

    /**
     * 新增一个用户
     *
     * @Description
     * · 完整业务流程的一部分.
     *
     * @param user                              [（安全认证）用户]
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean insert(@NotNull SecurityUser user, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == user || !user.isEntityLegal()) {
            //-- 非法输入: 预期新增的用户
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "预期新增的用户"
                    , user
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
                || ! operator_userAccountOperationInfo.equals(operator))
        {
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

        if (repository.existsByUsername(user.getUsername())) {
            //--- 已存在相同数据 (根据 EntityID 判断) 的情况
            return ! repository.findByUsername(user.getUsername())
                    .orElse(SecurityUser.Factory.USER.createDefault())
                    .isEmpty();
        }

        final @NotNull SecurityUser newUser = repository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_USER__ADD.name
                    , "更新后的数据异常"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//

        final @NotNull Log newLog_SecurityUser = Log.Factory.SecurityUser.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_USER__ADD
                , newUser
                , operator
                , operator_userAccountOperationInfo);
        if (! logService.insert(newLog_SecurityUser)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_USER__ADD.name
                    , "生成操作日志记录"
                    , newLog_SecurityUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //======//

        return true;
    }

    /**
     * 更新指定的用户
     *
     * @param user                              [（安全认证）用户]
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean update(@NotNull SecurityUser user, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 非法用户
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "非法用户"
                    , user
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
                || ! operator_userAccountOperationInfo.equals(operator))
        {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull SecurityUser newUser = repository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            return false;
        }

        /*//===== 关联数据 =====//

        if (! securityRoleService.existsByCode(Security.RoleVo.USER.name())
                && ! securityRoleService.insert(Security.RoleVo.USER))
        {
            //-- 操作失败: 新增 (安全) 角色
            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__ADD.name
                    , Security.RoleVo.USER
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull SecurityUserRole newUserRole  = SecurityUserRole.Factory.USER_ROLE.create(newUser.getUsername()
                , Security.RoleVo.USER.name());
        if (! userEvent.insertUserRoleRelationship(newUserRole, operator)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_USER_ROLE__ADD.name
                    , newUserRole
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //==========//*/

        //=== 操作日志记录 ===//

        final @NotNull Log newLog_SecurityUser = Log.Factory.SecurityUser.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_USER__ADD
                , user
                , operator
                , operator_userAccountOperationInfo);
        if (! logService.insert(newLog_SecurityUser)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.SECURITY__SECURITY_USER__ADD.name
                    , "生成操作日志记录"
                    , newLog_SecurityUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //======//

        return true;
    }

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分.
     *
     * @param user                              [（安全认证）用户]
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     *
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean delete(@NotNull SecurityUser user
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 非法用户
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "非法用户"
                    , user
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
                || ! operator_userAccountOperationInfo.equals(operator))
        {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (! repository.existsById(user.getUserId())) {
            //--- 不存在预期删除的数据的情况
            return true;
        } else {
            //--- 存在预期删除的数据的情况
            repository.deleteById(user.getUserId());
            if (repository.existsById(user.getUserId())) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , HandleType.LogVo.SECURITY__SECURITY_USER__DELETE.name
                        , "执行后数据异常"
                        , user
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            //=== 操作日志记录 ===//

            final @NotNull Log newLog_SecurityUser = Log.Factory.SecurityUser.LOG.create(null
                    , null
                    , HandleType.LogVo.SECURITY__SECURITY_USER__DELETE
                    , user
                    , operator
                    , operator_userAccountOperationInfo);
            if (! logService.insert(newLog_SecurityUser)) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , HandleType.LogVo.SECURITY__SECURITY_USER__DELETE.name
                        , "生成操作日志记录"
                        , newLog_SecurityUser
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            //======//
        }

        return true;
    }

}
