package github.com.suitelhy.dingding.sso.authorization.application.task;

import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.domain.Page;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.AbstractSecurityUser;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.sso.authorization.application.task.impl.ResourceTaskImpl;
import github.com.suitelhy.dingding.sso.authorization.infrastructure.application.dto.ResourceDto;
import github.com.suitelhy.dingding.sso.authorization.infrastructure.application.dto.RoleDto;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 资源 - 任务调度接口
 *
 * @Description (安全) 权限 - 资源.
 *
 * @see ResourceDto
 * @see ResourceTaskImpl
 */
public interface ResourceTask {

    //===== 查询操作 =====//

    /**
     * 查询所有资源
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @param operator  {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<Page<ResourceDto>> selectAllResource(int pageIndex, int pageSize, @NotNull AbstractSecurityUser operator);

    /**
     * 查询所有资源
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @param operator  操作者
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<Page<ResourceDto>> selectAllResource(int pageIndex, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询 (指定的) 资源
     *
     * @param resourceCode 资源编码
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<ResourceDto> selectResourceByCode(@NotNull String resourceCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询 (关联的) 资源
     *
     * @param roleCode  角色编码
     * @param operator  {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link ResourceDto} 集合
     */
    @NotNull TaskResult<List<ResourceDto>> selectResourceByRoleCode(@NotNull String roleCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询 (关联的) 资源
     *
     * @param username  用户名称
     * @param operator  {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<List<ResourceDto>> selectResourceByUsername(@NotNull String username, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询 (关联的) [URL 信息]
     *
     * @param resourceCode  资源编码
     * @param operator      {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<ResourceDto> selectUrlInfoByResourceCode(@NotNull String resourceCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    //===== 添加操作 =====//

    /**
     * 添加资源
     *
     * @Description 添加单个资源.
     *
     * @param resourceCode          {@linkplain ResourceDto#getCode() 资源编码}
     * @param resourceIcon          {@linkplain ResourceDto#getIcon() 图标}
     * @param resourceLink          {@linkplain ResourceDto#getLink() 资源链接}
     * @param resourceName          {@linkplain ResourceDto#getName() 资源名称}
     * @param resourceParentCode    {@linkplain ResourceDto#getParentCode() [父节点 <- 资源编码]}
     * @param resourceSort          {@linkplain ResourceDto#getSort() 序号}
     * @param resourceType_Value    {@linkplain ResourceDto#getType() [资源类型 -> VO 的值]}
     * @param operator              {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<ResourceDto> addResource(@NotNull String resourceCode
            , @Nullable String resourceIcon
            , @Nullable String resourceLink
            , @NotNull String resourceName
            , @Nullable String resourceParentCode
            , @NotNull Integer resourceSort
            , @Nullable Integer resourceType_Value
            , @NotNull AbstractSecurityUser operator);

    /**
     * 添加资源
     *
     * @Description 添加单个资源.
     *
     * @param resourceCode          {@linkplain ResourceDto#getCode() 资源编码}
     * @param resourceIcon          {@linkplain ResourceDto#getIcon() 图标}
     * @param resourceLink          {@linkplain ResourceDto#getLink() 资源链接}
     * @param resourceName          {@linkplain ResourceDto#getName() 资源名称}
     * @param resourceParentCode    {@linkplain ResourceDto#getParentCode() [父节点 <- 资源编码]}
     * @param resourceSort          {@linkplain ResourceDto#getSort() 序号}
     * @param resourceType_Value    {@linkplain ResourceDto#getType() [资源类型 -> VO 的值]}
     * @param operator              {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<ResourceDto> addResource(@NotNull String resourceCode
            , @Nullable String resourceIcon
            , @Nullable String resourceLink
            , @NotNull String resourceName
            , @Nullable String resourceParentCode
            , @NotNull Integer resourceSort
            , @Nullable Integer resourceType_Value
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 添加[资源 - 角色]关联
     *
     * @param existedResource   {@linkplain ResourceDto [(安全) 资源]}
     * @param existedRole       {@linkplain RoleDto [(安全) 角色]}
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<Boolean> addResourceToRole(@NotNull ResourceDto existedResource
            , @NotNull RoleDto existedRole
            , @NotNull AbstractSecurityUser operator);

    /**
     * 添加[资源 - 角色]关联
     *
     * @param existedResource   {@linkplain ResourceDto [(安全) 资源]}
     * @param existedRole       {@linkplain RoleDto [(安全) 角色]}
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<Boolean> addResourceToRole(@NotNull ResourceDto existedResource
            , @NotNull RoleDto existedRole
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 添加[资源 - URL]关联
     *
     * @param existedResource   {@linkplain ResourceDto [(安全) 资源]}
     * @param urlInfo           {@linkplain RoleDto [URL 信息]}
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<Boolean> addUrlToResource(@NotNull ResourceDto existedResource
            , @NotNull String[] urlInfo
            , @NotNull AbstractSecurityUser operator);

    /**
     * 添加[资源 - URL]关联
     *
     * @param existedResource   {@linkplain ResourceDto [(安全) 资源]}
     * @param urlInfo           {@linkplain RoleDto [URL 信息]}
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<Boolean> addUrlToResource(@NotNull ResourceDto existedResource
            , @NotNull String[] urlInfo
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    //===== 更新操作 =====//

    /**
     * 更新指定资源
     *
     * @Description 局部更新.
     *
     * @param old_resource_data {@linkplain Map<String, Object> 被替换的资源数据}
     *                          · 数据格式:
     *                          {
     *                          "resource_code" : [资源编码],
     *                          "resource_icon" : [图标],
     *                          "resource_link" : [资源链接],
     *                          "resource_name" : [资源名称],
     *                          "resource_parentCode" : [父节点 <- 资源编码],
     *                          "resource_sort" : [序号],
     *                          "resource_type_value" : [资源类型 -> VO 的值]
     *                          }
     * @param new_resource_data {@linkplain Map<String, Object> 替换的资源数据}
     *                          · 数据格式:
     *                          {
     *                          "resource_icon" : [图标],
     *                          "resource_link" : [资源链接],
     *                          "resource_name" : [资源名称],
     *                          "resource_parentCode" : [父节点 <- 资源编码],
     *                          "resource_sort" : [序号],
     *                          "resource_type_value" : [资源类型 -> VO 的值]
     *                          }
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<ResourceDto> updateResource(@NotNull @NotNull Map<String, Object> old_resource_data
            , @NotNull Map<String, Object> new_resource_data
            , @NotNull AbstractSecurityUser operator);

    /**
     * 更新指定资源
     *
     * @Description 局部更新.
     *
     * @param old_resource_data {@linkplain Map<String, Object> 被替换的资源数据}
     *                          · 数据格式:
     *                          {
     *                          "resource_code" : [资源编码],
     *                          "resource_icon" : [图标],
     *                          "resource_link" : [资源链接],
     *                          "resource_name" : [资源名称],
     *                          "resource_parentCode" : [父节点 <- 资源编码],
     *                          "resource_sort" : [序号],
     *                          "resource_type_value" : [资源类型 -> VO 的值]
     *                          }
     * @param new_resource_data {@linkplain Map<String, Object> 替换的资源数据}
     *                          · 数据格式:
     *                          {
     *                          "resource_icon" : [图标],
     *                          "resource_link" : [资源链接],
     *                          "resource_name" : [资源名称],
     *                          "resource_parentCode" : [父节点 <- 资源编码],
     *                          "resource_sort" : [序号],
     *                          "resource_type_value" : [资源类型 -> VO 的值]
     *                          }
     * @param operator          {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<ResourceDto> updateResource(@NotNull @NotNull Map<String, Object> old_resource_data
            , @NotNull Map<String, Object> new_resource_data
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    //===== 删除操作 =====//

    /**
     * 删除指定资源
     *
     * @param resourceCode  {@linkplain SecurityResource#getCode() 资源编码}
     * @param operator      {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<ResourceDto> deleteResource(@NotNull String resourceCode, @NotNull AbstractSecurityUser operator);

    /**
     * 删除指定资源
     *
     * @param resourceCode  {@linkplain SecurityResource#getCode() 资源编码}
     * @param operator      {@linkplain AbstractSecurityUser 操作者}
     *
     * @return {@link TaskResult}
     */
    @NotNull TaskResult<ResourceDto> deleteResource(@NotNull String resourceCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

}
