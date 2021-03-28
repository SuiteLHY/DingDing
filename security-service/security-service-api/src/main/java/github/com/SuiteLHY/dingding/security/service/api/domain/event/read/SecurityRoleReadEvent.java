package github.com.suitelhy.dingding.security.service.api.domain.event.read;

import github.com.suitelhy.dingding.core.infrastructure.domain.Page;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * （安全认证）角色 - 复杂业务接口
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see SecurityRole
 * @see SecurityResource
 * @see SecurityUser
 * @see SecurityRoleResource
 * @see SecurityUserRole
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
public interface SecurityRoleReadEvent {

    //===== 查询操作业务 =====//

    /**
     * 判断是否存在 (关联的) [（安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    boolean existRoleOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityRoleResource.Validator#resourceCode(String) 资源编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    boolean existRoleOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @Description 完整的业务流程.
     *
     * @param roleCode  {@linkplain SecurityRoleResource.Validator#roleCode(String) 角色编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    boolean existResourceOnRoleByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException;

    /**
     * 查询 (指定角色关联的) 资源
     *
     * @Description 完整的业务流程.
     *
     * @param roleCode  {@linkplain SecurityRole.Validator#code(String) 角色编码}
     *
     * @return {@linkplain SecurityResource (指定角色关联的) 资源}
     */
    @NotNull List<SecurityResource> selectResourceOnRoleByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException;

    /**
     * 查询 (指定资源关联的) 角色
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityResource#getCode() 资源编码}
     *
     * @return {@linkplain SecurityRole (指定资源关联的) 角色}
     */
    @NotNull List<SecurityRole> selectRoleOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 查询 (关联的) [（安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain SecurityRole (关联的) [（安全认证）角色]}
     */
    @NotNull List<SecurityRole> selectRoleOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询 (关联的) [（安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@linkplain SecurityRole (关联的) [（安全认证）角色]}
     */
    @NotNull Page<SecurityRole> selectPageRoleOnUserByUsername(@NotNull String username, int pageIndex, int pageSize)
            throws IllegalArgumentException;

}
