package github.com.suitelhy.dingding.core.application.task.security;

import github.com.suitelhy.dingding.core.application.task.security.impl.RoleTaskImpl;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.security.RoleDto;
import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.web.AbstractSecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 角色 - 任务调度接口
 *
 * @Description (安全) 权限 - 角色.
 *
 * @see RoleDto
 * @see RoleTaskImpl
 */
public interface RoleTask {

    //===== 查询操作 =====//

    /**
     * 查询所有角色
     *
     * @param pageIndex     分页索引, 从0开始
     * @param pageSize      分页 - 每页容量
     * @param operator      操作者 {@link AbstractSecurityUser}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<List<RoleDto>> selectAllRole(int pageIndex, int pageSize, @NotNull AbstractSecurityUser operator);

    /**
     * 查询所有角色
     *
     * @param pageIndex     分页索引, 从0开始
     * @param pageSize      分页 - 每页容量
     * @param operator      操作者
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<List<RoleDto>> selectAllRole(int pageIndex, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询 (指定的) 角色
     *
     * @param roleCode  角色编码
     * @param operator  操作者
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<RoleDto> selectRoleByCode(@NotNull String roleCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询 (关联的) 角色
     *
     * @param username  用户名称
     * @param operator  操作者
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<List<RoleDto>> selectRoleByUsername(@NotNull String username, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    //===== 添加操作 =====//

    /**
     * 添加角色
     *
     * @Description 添加单个角色.
     *
     * @param roleCode          {@link SecurityRole.Validator#code(String)}
     * @param roleName          {@link SecurityRole.Validator#name(String)}
     * @param roleDescription   {@link SecurityRole.Validator#description(String)}
     * @param operator          操作者 {@link AbstractSecurityUser}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<RoleDto> addRole(@NotNull String roleCode
            , @NotNull String roleName
            , @Nullable String roleDescription
            , @NotNull AbstractSecurityUser operator);

    /**
     * 添加角色
     *
     * @Description 添加单个角色.
     *
     * @param roleCode          {@link SecurityRole.Validator#code(String)}
     * @param roleName          {@link SecurityRole.Validator#name(String)}
     * @param roleDescription   {@link SecurityRole.Validator#description(String)}
     * @param operator          操作者
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<RoleDto> addRole(@NotNull String roleCode
            , @NotNull String roleName
            , @Nullable String roleDescription
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 添加[(安全) 角色 - 用户]关联
     *
     * @param existedRole   {@link RoleDto}
     * @param existedUser   {@link UserDto}
     * @param operator      操作者 {@link AbstractSecurityUser}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<Boolean> addRoleToUser(@NotNull RoleDto existedRole
            , @NotNull UserDto existedUser
            , @NotNull AbstractSecurityUser operator);

    /**
     * 添加[(安全) 角色 - 用户]关联
     *
     * @param existedRole   {@link RoleDto}
     * @param existedUser   {@link UserDto}
     * @param operator      操作者
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<Boolean> addRoleToUser(@NotNull RoleDto existedRole
            , @NotNull UserDto existedUser
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    //===== 更新操作 =====//

    /**
     * 更新指定角色
     *
     * @Description 局部更新.
     *
     * @param old_roleCode          {@link SecurityRole.Validator#code(String)}
     * @param old_roleName          {@link SecurityRole.Validator#name(String)}
     * @param old_roleDescription   {@link SecurityRole.Validator#description(String)}
     * @param new_role_data         {@link Map<String, Object>}
     * · 数据格式:
     * {
     *    role_name : [角色名称],
     *    role_description : [角色描述]
     * }
     * @param operator  操作者 {@link AbstractSecurityUser}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<RoleDto> updateRole(@NotNull String old_roleCode
            , @NotNull String old_roleName
            , @Nullable String old_roleDescription
            , @NotNull Map<String, Object> new_role_data
            , @NotNull AbstractSecurityUser operator);

    /**
     * 更新指定角色
     *
     * @Description 局部更新.
     *
     * @param old_roleCode          {@link SecurityRole.Validator#code(String)}
     * @param old_roleName          {@link SecurityRole.Validator#name(String)}
     * @param old_roleDescription   {@link SecurityRole.Validator#description(String)}
     * @param new_role_data         {@link Map<String, Object>}
     * · 数据格式:
     * {
     *    role_name : [角色名称],
     *    role_description : [角色描述]
     * }
     * @param operator  操作者
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<RoleDto> updateRole(@NotNull String old_roleCode
            , @NotNull String old_roleName
            , @Nullable String old_roleDescription
            , @NotNull Map<String, Object> new_role_data
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    //===== 删除操作 =====//

    /**
     * 删除指定角色
     *
     * @param roleCode  {@link SecurityRole#getCode()}
     * @param operator  操作者 {@link AbstractSecurityUser}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<RoleDto> deleteRole(@NotNull String roleCode, @NotNull AbstractSecurityUser operator);

    /**
     * 删除指定角色
     *
     * @param roleCode  {@link SecurityRole#getCode()}
     * @param operator  操作者
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<RoleDto> deleteRole(@NotNull String roleCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

}
