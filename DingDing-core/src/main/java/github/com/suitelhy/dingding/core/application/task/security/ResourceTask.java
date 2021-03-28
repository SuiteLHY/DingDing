//package github.com.suitelhy.dingding.core.application.task.security;
//
//import github.com.suitelhy.dingding.core.application.task.security.impl.ResourceTaskImpl;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
//import github.com.suitelhy.dingding.core.infrastructure.application.dto.UserDto;
//import github.com.suitelhy.dingding.core.infrastructure.application.dto.security.ResourceDto;
//import github.com.suitelhy.dingding.core.infrastructure.application.dto.security.RoleDto;
//import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
//import github.com.suitelhy.dingding.core.infrastructure.web.AbstractSecurityUser;
//import github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo;
//import org.springframework.lang.Nullable;
//
//import javax.validation.constraints.NotNull;
//import java.util.List;
//import java.util.Map;
//
///**
// * 资源 - 任务调度接口
// *
// * @Description (安全) 权限 - 资源.
// * @see ResourceDto
// * @see ResourceTaskImpl
// */
//public interface ResourceTask {
//
//    //===== 查询操作 =====//
//
//    /**
//     * 查询所有资源
//     *
//     * @param pageIndex 分页索引, 从0开始
//     * @param pageSize  分页 - 每页容量
//     * @param operator  操作者 {@link AbstractSecurityUser}
//     * @return {@link TaskResult}
//     */
//    @NotNull
//    TaskResult<List<ResourceDto>> selectAllResource(int pageIndex, int pageSize, @NotNull AbstractSecurityUser operator);
//
//    /**
//     * 查询所有资源
//     *
//     * @param pageIndex 分页索引, 从0开始
//     * @param pageSize  分页 - 每页容量
//     * @param operator  操作者
//     * @return {@link TaskResult}
//     */
//    @NotNull
//    TaskResult<List<ResourceDto>> selectAllResource(int pageIndex, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);
//
//    /**
//     * 查询 (指定的) 资源
//     *
//     * @param resourceCode 资源编码
//     * @return {@link TaskResult}
//     */
//    @NotNull
//    TaskResult<ResourceDto> selectResourceByCode(@NotNull String resourceCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);
//
//    /**
//     * 查询 (关联的) 资源
//     *
//     * @param roleCode 角色编码
//     * @param operator 操作者
//     * @return {@link ResourceDto} 集合
//     */
//    @NotNull
//    TaskResult<List<ResourceDto>> selectResourceByRoleCode(@NotNull String roleCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);
//
//    /**
//     * 查询 (关联的) 资源
//     *
//     * @param username 用户名称
//     * @param operator 操作者
//     * @return {@link TaskResult}
//     */
//    @NotNull
//    TaskResult<List<ResourceDto>> selectResourceByUsername(@NotNull String username, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);
//
//    /**
//     * 查询 (关联的) [URL 信息]
//     *
//     * @param resourceCode 资源编码
//     * @param operator     操作者
//     * @return {@link TaskResult}
//     */
//    @NotNull
//    TaskResult<ResourceDto> selectUrlInfoByResourceCode(@NotNull String resourceCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);
//
//    //===== 添加操作 =====//
//
//    /**
//     * 添加资源
//     *
//     * @param resourceCode       资源编码              {@link ResourceDto#getCode()}
//     * @param resourceIcon       图标                 {@link ResourceDto#getIcon()}
//     * @param resourceLink       资源链接              {@link ResourceDto#getLink()}
//     * @param resourceName       资源名称              {@link ResourceDto#getName()}
//     * @param resourceParentCode [父节点 <- 资源编码]   {@link ResourceDto#getParentCode()}
//     * @param resourceSort       序号                 {@link ResourceDto#getSort()}
//     * @param resourceType_Value [资源类型 -> VO 的值] {@link ResourceDto#getType()}
//     * @param operator           操作者               {@link AbstractSecurityUser}
//     * @return {@link TaskResult}
//     * @Description 添加单个资源.
//     */
//    @NotNull
//    TaskResult<ResourceDto> addResource(@NotNull String resourceCode
//            , @Nullable String resourceIcon
//            , @Nullable String resourceLink
//            , @NotNull String resourceName
//            , @Nullable String resourceParentCode
//            , @NotNull Integer resourceSort
//            , @Nullable Integer resourceType_Value
//            , @NotNull AbstractSecurityUser operator);
//
//    /**
//     * 添加资源
//     *
//     * @param resourceCode       资源编码              {@link ResourceDto#getCode()}
//     * @param resourceIcon       图标                 {@link ResourceDto#getIcon()}
//     * @param resourceLink       资源链接              {@link ResourceDto#getLink()}
//     * @param resourceName       资源名称              {@link ResourceDto#getName()}
//     * @param resourceParentCode [父节点 <- 资源编码]   {@link ResourceDto#getParentCode()}
//     * @param resourceSort       序号                 {@link ResourceDto#getSort()}
//     * @param resourceType_Value [资源类型 -> VO 的值] {@link ResourceDto#getType()}
//     * @param operator           操作者
//     * @return {@link TaskResult}
//     * @Description 添加单个资源.
//     */
//    @NotNull
//    TaskResult<ResourceDto> addResource(@NotNull String resourceCode
//            , @Nullable String resourceIcon
//            , @Nullable String resourceLink
//            , @NotNull String resourceName
//            , @Nullable String resourceParentCode
//            , @NotNull Integer resourceSort
//            , @Nullable Integer resourceType_Value
//            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);
//
//    /**
//     * 添加[资源 - 角色]关联
//     *
//     * @param existedResource (安全) 资源 {@link ResourceDto}
//     * @param existedRole     (安全) 角色 {@link RoleDto}
//     * @param operator        操作者      {@link AbstractSecurityUser}
//     * @return {@link TaskResult}
//     */
//    @NotNull
//    TaskResult<Boolean> addResourceToRole(@NotNull ResourceDto existedResource
//            , @NotNull RoleDto existedRole
//            , @NotNull AbstractSecurityUser operator);
//
//    /**
//     * 添加[资源 - 角色]关联
//     *
//     * @param existedResource (安全) 资源 {@link ResourceDto}
//     * @param existedRole     (安全) 角色 {@link RoleDto}
//     * @param operator        操作者
//     * @return {@link TaskResult}
//     */
//    @NotNull
//    TaskResult<Boolean> addResourceToRole(@NotNull ResourceDto existedResource
//            , @NotNull RoleDto existedRole
//            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);
//
//    /**
//     * 添加[资源 - URL]关联
//     *
//     * @param existedResource (安全) 资源  {@link ResourceDto}
//     * @param urlInfo         [URL 信息]  {@link RoleDto}
//     * @param operator        操作者      {@link AbstractSecurityUser}
//     * @return {@link TaskResult}
//     */
//    @NotNull
//    TaskResult<Boolean> addUrlToResource(@NotNull ResourceDto existedResource
//            , @NotNull String[] urlInfo
//            , @NotNull AbstractSecurityUser operator);
//
//    /**
//     * 添加[资源 - URL]关联
//     *
//     * @param existedResource (安全) 资源  {@link ResourceDto}
//     * @param urlInfo         [URL 信息]  {@link RoleDto}
//     * @param operator        操作者
//     * @return {@link TaskResult}
//     */
//    @NotNull
//    TaskResult<Boolean> addUrlToResource(@NotNull ResourceDto existedResource
//            , @NotNull String[] urlInfo
//            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);
//
//    //===== 更新操作 =====//
//
//    /**
//     * 更新指定资源
//     *
//     * @param old_resource_data 被替换的资源数据    {@link Map <String, Object>}
//     *                          · 数据格式:
//     *                          {
//     *                          resource_code : [资源编码],
//     *                          resource_icon : [图标],
//     *                          resource_link : [资源链接],
//     *                          resource_name : [资源名称],
//     *                          resource_parentCode : [父节点 <- 资源编码],
//     *                          resource_sort : [序号],
//     *                          resource_type_value : [资源类型 -> VO 的值]
//     *                          }
//     * @param new_resource_data 替换的资源数据     {@link Map <String, Object>}
//     *                          · 数据格式:
//     *                          {
//     *                          resource_icon : [图标],
//     *                          resource_link : [资源链接],
//     *                          resource_name : [资源名称],
//     *                          resource_parentCode : [父节点 <- 资源编码],
//     *                          resource_sort : [序号],
//     *                          resource_type_value : [资源类型 -> VO 的值]
//     *                          }
//     * @param operator          操作者 {@link AbstractSecurityUser}
//     * @return {@link TaskResult}
//     * @Description 局部更新.
//     */
//    @NotNull
//    TaskResult<ResourceDto> updateResource(@NotNull @NotNull Map<String, Object> old_resource_data
//            , @NotNull Map<String, Object> new_resource_data
//            , @NotNull AbstractSecurityUser operator);
//
//    /**
//     * 更新指定资源
//     *
//     * @param old_resource_data 被替换的资源数据    {@link Map <String, Object>}
//     *                          · 数据格式:
//     *                          {
//     *                          resource_code : [资源编码],
//     *                          resource_icon : [图标],
//     *                          resource_link : [资源链接],
//     *                          resource_name : [资源名称],
//     *                          resource_parentCode : [父节点 <- 资源编码],
//     *                          resource_sort : [序号],
//     *                          resource_type_value : [资源类型 -> VO 的值]
//     *                          }
//     * @param new_resource_data 替换的资源数据     {@link Map <String, Object>}
//     *                          · 数据格式:
//     *                          {
//     *                          resource_icon : [图标],
//     *                          resource_link : [资源链接],
//     *                          resource_name : [资源名称],
//     *                          resource_parentCode : [父节点 <- 资源编码],
//     *                          resource_sort : [序号],
//     *                          resource_type_value : [资源类型 -> VO 的值]
//     *                          }
//     * @param operator          操作者
//     * @return {@link TaskResult}
//     * @Description 局部更新.
//     */
//    @NotNull
//    TaskResult<ResourceDto> updateResource(@NotNull @NotNull Map<String, Object> old_resource_data
//            , @NotNull Map<String, Object> new_resource_data
//            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);
//
//    //===== 删除操作 =====//
//
//    /**
//     * 删除指定资源
//     *
//     * @param resourceCode 资源编码    {@link SecurityResource#getCode()}
//     * @param operator     操作者      {@link AbstractSecurityUser}
//     * @return {@link TaskResult}
//     */
//    @NotNull
//    TaskResult<ResourceDto> deleteResource(@NotNull String resourceCode, @NotNull AbstractSecurityUser operator);
//
//    /**
//     * 删除指定资源
//     *
//     * @param resourceCode 资源编码    {@link SecurityResource#getCode()}
//     * @param operator     操作者
//     * @return {@link TaskResult}
//     */
//    @NotNull
//    TaskResult<ResourceDto> deleteResource(@NotNull String resourceCode, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);
//
//}
