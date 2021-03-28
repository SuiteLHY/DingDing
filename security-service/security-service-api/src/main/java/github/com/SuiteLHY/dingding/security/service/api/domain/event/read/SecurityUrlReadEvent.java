package github.com.suitelhy.dingding.security.service.api.domain.event.read;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;

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
 * @see SecurityResourceUrl
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
public interface SecurityUrlReadEvent {

    //===== 查询操作业务 =====//

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @Description 完整的业务流程.
     *
     * @param urlInfo   {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    boolean existResourceOnUrlInfo(@NotNull String[] urlInfo)
            throws IllegalArgumentException;

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityResource.Validator#code(String) 资源编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    boolean existUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 查询[URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityResourceUrl.Validator#code(String) 资源编码}
     *
     * @return {@linkplain SecurityResourceUrl [URL 相关信息]}
     */
    @NotNull List<SecurityResourceUrl> selectUrlInfoOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     *
     * @return {@linkplain SecurityResourceUrl [用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]}
     */
    @NotNull List<SecurityResourceUrl> selectUrlInfoOnUserByUsername(@NotNull String username)
            throws IllegalArgumentException;

    /**
     * 查询[用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param username  {@linkplain SecurityUser.Validator#username(String) 用户名称}
     * @param clientId  {@linkplain SecurityResourceUrl#getClientId() [资源服务器 ID]}
     *
     * @return {@linkplain SecurityResourceUrl [用户 -> (关联的) 角色 -> (关联的) 资源 -> URL 相关信息]}
     */
    @NotNull List<SecurityResourceUrl> selectUrlInfoOnUserByUsernameAndClientId(@NotNull String username, @NotNull String clientId)
            throws IllegalArgumentException;

    /**
     * 查询[URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param clientId  {@linkplain SecurityResourceUrl.Validator#clientId(String) [资源服务器 ID]}
     *
     * @return {@linkplain SecurityResourceUrl [URL 相关信息]}
     */
    @NotNull List<SecurityResourceUrl> selectUrlInfoByClientId(@NotNull String clientId)
            throws IllegalArgumentException;

    /**
     * 查询[URL 相关信息]
     *
     * @Description 完整的业务流程.
     *
     * @param clientId  {@linkplain SecurityResourceUrl.Validator#clientId(String) [资源服务器 ID]}
     * @param urlPath   {@linkplain SecurityResourceUrl.Validator#urlPath(String) [资源对应的 URL (Path部分)]}
     *
     * @return {@linkplain SecurityResourceUrl [URL 相关信息]}
     */
    @NotNull List<SecurityResourceUrl> selectUrlInfoByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath)
            throws IllegalArgumentException;

    /**
     * 查询[（安全认证）资源 ←→ URL]
     *
     * @Description 完整的业务流程.
     *
     * @param resourceCode  {@linkplain SecurityResourceUrl.Validator#code(String) 资源编码}
     *
     * @return {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     */
    @NotNull List<SecurityResourceUrl> selectResourceUrlRelationshipOnResourceByResourceCode(@NotNull String resourceCode)
            throws IllegalArgumentException;

    /**
     * 查询[（安全认证）资源 ←→ URL]
     *
     * @Description 完整的业务流程.
     *
     * @param clientId  {@linkplain SecurityResourceUrl.Validator#clientId(String) [资源服务器 ID]}
     * @param urlPath   {@linkplain SecurityResourceUrl.Validator#urlPath(String) [资源对应的 URL](Path部分)}
     *
     * @return {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     */
    @NotNull List<SecurityResourceUrl> selectResourceUrlRelationshipOnUrlInfoByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath)
            throws IllegalArgumentException;

}
