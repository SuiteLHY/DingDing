package github.com.suitelhy.dingding.security.service.api.domain.service.read;

import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
//import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * [（安全认证）资源 ←→ URL]
 *
 * @Description [（安全认证）资源 ←→ 资源]关联关系 - 业务接口.
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see SecurityResourceUrl
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
public interface SecurityResourceUrlReadService {

    /**
     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
     *
     * @param urlInfo   {@linkplain SecurityResourceUrl.Validator#urlInfo(String[]) [URL 相关信息]}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    boolean existResourceByUrlInfo(@NotNull String[] urlInfo);

    /**
     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
     *
     * @param resourceCode  {@linkplain SecurityResourceUrl.Validator#code(String) 资源编码}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    boolean existUrlByResourceCode(@NotNull String resourceCode);

    /**
     * 判断是否存在 (指定的) [（安全认证）资源 ←→ URL]
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    boolean existResourceUrlByResourceUrl(@NotNull SecurityResourceUrl resourceUrl);

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @NotNull PageImpl<SecurityResourceUrl> selectAll(int pageIndex, int pageSize);

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @NotNull Long selectCount(int pageSize);

    /**
     * 查询
     *
     * @param resourceUrl   {@linkplain SecurityResourceUrl [（安全认证）资源 ←→ URL]}
     *
     * @return {@link SecurityResourceUrl}
     */
    @NotNull SecurityResourceUrl selectByResourceUrl(@NotNull SecurityResourceUrl resourceUrl);

    /**
     * 查询
     *
     * @param resourceCode  {@linkplain SecurityResourceUrl.Validator#code(String) 资源编码}
     *
     * @return {@link SecurityResourceUrl}
     */
    @NotNull List<SecurityResourceUrl> selectByResourceCode(@NotNull String resourceCode);

    /**
     * 查询
     *
     * @param clientId  {@linkplain SecurityResourceUrl.Validator#clientId(String) [资源服务器 ID] }
     * @param urlPath   {@linkplain SecurityResourceUrl.Validator#urlPath(String) [资源对应的 URL (Path部分)]}
     *
     * @return {@link SecurityResourceUrl}
     */
    @NotNull List<SecurityResourceUrl> selectByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath);

}
