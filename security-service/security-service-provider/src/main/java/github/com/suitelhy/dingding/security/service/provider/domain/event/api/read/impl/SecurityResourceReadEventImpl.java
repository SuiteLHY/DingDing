package github.com.suitelhy.dingding.security.service.provider.domain.event.api.read.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRoleResource;
import github.com.suitelhy.dingding.security.service.api.domain.event.read.SecurityResourceReadEvent;
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityResourceEvent;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * (安全) 资源 - 复杂业务接口
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see SecurityResource
 * @see SecurityRole
 * @see SecurityResourceUrl
 * @see SecurityRoleResource
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
@Service(cluster = "failover")
public class SecurityResourceReadEventImpl
        implements SecurityResourceReadEvent {

    @Autowired
    private SecurityResourceEvent securityResourceEvent;

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @Description 完整的业务流程.
     *
     * @param roleCode {@linkplain SecurityRoleResource.Validator#roleCode(String) 角色编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    public boolean existResourceOnRoleByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException
    {
        return securityResourceEvent.existResourceOnRoleByRoleCode(roleCode);
    }

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @Description 完整的业务流程.
     *
     * @param urlInfo {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    public boolean existResourceOnUrlInfo(@NotNull String[] urlInfo)
            throws IllegalArgumentException
    {
        return securityResourceEvent.existResourceOnUrlInfo(urlInfo);
    }

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode {@linkplain SecurityRoleResource.Validator#resourceCode(String) 资源编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    public boolean existRoleOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException
    {
        return securityResourceEvent.existRoleOnResourceByResourceCode(resourceCode);
    }

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode {@linkplain SecurityResource.Validator#code(String) 资源编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    @Override
    public boolean existUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException
    {
        return securityResourceEvent.existUrlInfoOnResourceByResourceCode(resourceCode);
    }

    /**
     * 查询 (指定角色关联的) 资源
     *
     * @Description 完整的业务流程.
     *
     * @param roleCode {@linkplain SecurityRole.Validator#code(String) 角色编码}
     *
     * @return {@linkplain SecurityResource [（安全认证）资源]}
     */
    @Override
    public @NotNull List<SecurityResource> selectResourceOnRoleByRoleCode(@NotNull String roleCode)
            throws IllegalArgumentException
    {
        return securityResourceEvent.selectResourceOnRoleByRoleCode(roleCode);
    }

    /**
     * 查询[(关联的) 角色 -> (关联的) 资源]
     *
     * @Description 完整的业务流程.
     *
     * @param username {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain SecurityResource [(关联的) 角色 -> (关联的) 资源]}
     */
    @Override
    public @NotNull List<SecurityResource> selectResourceOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException
    {
        return securityResourceEvent.selectResourceOnUserByUsername(username);
    }

    /**
     * 查询 (指定资源关联的) 角色
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode {@linkplain SecurityResource#getCode() 资源编码}
     *
     * @return {@linkplain SecurityRole [（安全认证）角色]}
     */
    @Override
    public @NotNull List<SecurityRole> selectRoleOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException
    {
        return securityResourceEvent.selectRoleOnResourceByResourceCode(resourceCode);
    }

    /**
     * 查询[URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode {@linkplain SecurityResourceUrl.Validator#code(String) 资源编码}
     *
     * @return {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     */
    @Override
    public @NotNull List<SecurityResourceUrl> selectUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException
    {
        return securityResourceEvent.selectUrlInfoOnResourceByResourceCode(resourceCode);
    }

}
