package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRepository;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRoleRepository;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
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
 */
@Service("securityUserService")
@Order(Ordered.LOWEST_PRECEDENCE)
public class SecurityUserServiceImpl
        implements SecurityUserService {

    @Autowired
    private SecurityUserRepository repository;

    @Autowired
    private SecurityUserRoleRepository userRoleRepository;

    @Autowired
    private SecurityRoleService securityRoleService;

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize
     * @return
     */
    @Override
    public Page<SecurityUser> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            //-- 非法输入: <param>pageIndex</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageIndex</param>"));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageSize</param>"));
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
     * @return 分页 - 总页数
     */
    @Override
    public Long selectCount(int pageSize) {
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageSize</param>"));
        }

        long dataNumber = repository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询指定的用户
     *
     * @param userId
     * @return
     */
    @Nullable
    @Override
    public SecurityUser selectUserByUserId(@NotNull String userId) {
        if (!SecurityUser.Validator.USER.userId(userId)) {
            //-- 非法输入: 用户ID
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 用户ID"));
        }

        Optional<SecurityUser> result = repository.findById(userId);
        return result.orElse(null);
    }

    /**
     * 查询指定的用户
     *
     * @param username
     * @return
     */
    @Override
    public SecurityUser selectUserByUsername(@NotNull String username) {
        if (!SecurityUser.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 用户名称"));
        }

        Optional<SecurityUser> result = repository.findByUsername(username);
        return result.orElse(null);
    }

    /**
     * 查询 (关联的) 角色
     *
     * @param username
     * @return {@link SecurityUserRole}
     */
    @Nullable
    @Override
    public List<Map<String, Object>> selectRoleByUsername(@NotNull String username) {
        if (!SecurityUser.Validator.USER.username(username)) {
            return null;
        }

        return userRoleRepository.selectRoleByUsername(username);
    }

    /**
     * 查询 (关联的) 角色 -> (关联的) 资源
     *
     * @param username
     * @return {@link SecurityUser}
     */
    @Override
    public List<Map<String, Object>> selectResourceByUsername(@NotNull String username) {
        if (!SecurityUser.Validator.USER.username(username)) {
            return null;
        }

        /*final List<Map<String, Object>> userRoles = userRoleRepository.selectRoleByUsername(username);
        if (null == userRoles || userRoles.isEmpty()) {
            return null;
        }

        final List<Map<String, Object>> roleResources = new ArrayList<>(userRoles.size());
        for (Map<String, Object> each : userRoles) {
            List<Map<String, Object>> eachResources = roleResourceRepository.selectResourceByRoleCode((String) each.get("role_code"));

            roleResources.addAll(eachResources);
        }*/

        return repository.selectResourceByUsername(username);
    }

    /**
     * 查询 (关联的) 角色 -> (关联的) 资源
     *
     * @param username
     * @return 资源集合         {@link SecurityResource}
     */
    @Override
    public List<Map<String, Object>> selectUrlPathByUsernameAndClientId(@NotNull String username, @NotNull String clientId) {
        if (!SecurityUser.Validator.USER.username(username)
                || !SecurityResourceUrl.Validator.RESOURCE_URL.clientId(clientId)) {
            return null;
        }

        return repository.selectURLByUsernameAndClientId(username, clientId);
    }

    /**
     * 新增一个用户
     *
     * @param user
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean insert(@NotNull SecurityUser user) {
        if (null == user || !user.isEntityLegal()) {
            //-- 非法输入: 非法用户
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法用户"));
        }
        if (repository.existsByUsername(user.id())) {
            //--- 已存在相同数据 (根据 EntityID 判断) 的情况
            return !repository.findByUsername(user.id()).get().isEmpty();
        }

        final SecurityUser newUser = repository.saveAndFlush(user);
        if (newUser.isEmpty()) return false;

        if (!securityRoleService.existsByCode(Security.RoleVo.USER.name())
                && !securityRoleService.insert(SecurityRole.Factory.ROLE.create(Security.RoleVo.USER))) {
            //-- 操作失败: 新增 (安全) 角色
            throw new RuntimeException(this.getClass().getSimpleName()
                    .concat(" -> 操作失败: 新增 (安全) 角色"));
        }

        final SecurityUserRole newUserRole = userRoleRepository.saveAndFlush(
                SecurityUserRole.Factory.USER_ROLE.create(newUser.getUsername(), Security.RoleVo.USER.name())
        );
        if (null == newUserRole || newUserRole.isEmpty()) {
            //-- 操作失败: 新增 {(安全) 用户 - 角色 关联关系}
            throw new RuntimeException(this.getClass().getSimpleName()
                    .concat(" -> 操作失败: 新增 {(安全) 用户 - 角色 关联关系}"));
        }

        return true;
    }

    /**
     * 新增 角色 关联
     *
     * @param users     用户, 必须全部合法且已持久化.    {@link SecurityUser}
     * @param roles     角色, 必须全部合法且已持久化.    {@link SecurityRole}
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean insertRole(@NotNull Set<SecurityUser> users, @NotNull Set<SecurityRole> roles) {
        if ((null == users || users.isEmpty())
                || (null == roles || roles.isEmpty())) {
            return false;
        }
        for (SecurityUser each : users) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }
        for (SecurityRole each : roles) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }

        boolean result = false;

        for (SecurityUser each : users) {
            if (!userRoleRepository.existsAllByUsername(each.getUsername())) {
                //--- 用户不存在绑定的角色的情况
                for (SecurityRole eachRole : roles) {
                    if (userRoleRepository.saveAndFlush(SecurityUserRole.Factory.USER_ROLE.create(each.getUsername(), eachRole.getCode())).isEmpty()) {
                        //-- 操作失败: 新增 用户 - 角色 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 新增 用户 - 角色 关联"));
                    }

                    result = true;
                }
            } else {
                //--- 用户存在绑定的角色的情况
                final List<SecurityUserRole> existedUserRoles = userRoleRepository.findAllByUsername(each.getUsername());

                for (SecurityRole eachRole : roles) {
                    boolean exist = false;
                    for (SecurityUserRole userRole : existedUserRoles) {
                        if (userRole.equals(each, eachRole)) {
                            exist = true;
                            break;
                        }
                    }
                    if (exist) continue;

                    if (userRoleRepository.saveAndFlush(SecurityUserRole.Factory.USER_ROLE.create(each.getUsername(), eachRole.getCode())).isEmpty()) {
                        //-- 操作失败: 新增 用户 - 角色 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 新增 用户 - 角色 关联"));
                    }

                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * 更新指定的用户
     *
     * @param user
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean update(@NotNull SecurityUser user) {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 非法用户
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法用户"));
        }

        final SecurityUser newUser = repository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            return false;
        }

        if (!securityRoleService.existsByCode(Security.RoleVo.USER.name())
                && !securityRoleService.insert(SecurityRole.Factory.ROLE.create(Security.RoleVo.USER))) {
            //-- 操作失败: 新增 (安全) 角色
            throw new RuntimeException(this.getClass().getSimpleName()
                    .concat(" -> 操作失败: 新增 (安全) 角色"));
        }

        final SecurityUserRole newUserRole = userRoleRepository.saveAndFlush(
                SecurityUserRole.Factory.USER_ROLE.create(newUser.getUsername(), Security.RoleVo.USER.name())
        );
        if (null == newUserRole || newUserRole.isEmpty()) {
            //-- 操作失败: 新增 {(安全) 用户 - 角色 关联关系}
            throw new RuntimeException(this.getClass().getSimpleName()
                    .concat(" -> 操作失败: 新增 {(安全) 用户 - 角色 关联关系}"));
        }

        return true;
    }

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据;
     *->    主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean delete(@NotNull SecurityUser user) {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 非法用户
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法用户"));
        }

        if (userRoleRepository.existsAllByUsername(user.getUsername())) {
            if (userRoleRepository.removeByUsername(user.getUsername()) < 1
                    || userRoleRepository.existsAllByUsername(user.getUsername())) {
                //-- 操作失败: 删除 {(安全) 用户 - 角色 关联关系}
                throw new RuntimeException(this.getClass().getSimpleName()
                        .concat(" -> 操作失败: 删除 {(安全) 用户 - 角色 关联关系}"));
            }
        }

        repository.deleteById(user.getUserId());
        return !repository.existsById(user.getUserId());
    }

    /**
     * 删除 角色 关联
     *
     * @param users 用户, 必须全部合法且已持久化.    {@link SecurityUser}
     * @param roles 角色, 必须全部合法且已持久化.    {@link SecurityRole}
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean deleteRole(@NotNull Set<SecurityUser> users, @NotNull Set<SecurityRole> roles) {
        if ((null == users || users.isEmpty())
                || (null == roles || roles.isEmpty())) {
            return false;
        }
        for (SecurityUser each : users) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }
        for (SecurityRole each : roles) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }

        boolean result = false;

        for (SecurityUser each : users) {
            if (!userRoleRepository.existsAllByUsername(each.getUsername())) {
                //--- 用户不存在绑定的角色的情况
            } else {
                //--- 用户存在绑定的角色的情况
                final List<SecurityUserRole> existedUserRoles = userRoleRepository.findAllByUsername(each.getUsername());

                for (SecurityRole eachRole : roles) {
                    SecurityUserRole existedUserRole = null;

                    for (SecurityUserRole userRole : existedUserRoles) {
                        if (userRole.equals(each, eachRole)) {
                            existedUserRole = userRole;
                            break;
                        }
                    }
                    if (null == existedUserRole) continue;

                    if (userRoleRepository.removeByUsernameAndRoleCode(existedUserRole.getUsername(), existedUserRole.getRoleCode()) <= 0) {
                        //-- 操作失败: 删除 用户 - 角色 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 删除 用户 - 角色 关联"));
                    }
                }
            }

            result = true;
        }

        return result;
    }

}
