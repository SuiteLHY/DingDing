package github.com.suitelhy.dingding.security.service.api.domain.service.read;

import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
//import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

/**
 * (安全) 资源
 *
 * @Description (安全) 资源 - 业务接口.
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
public interface SecurityResourceReadService {

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从 0 开始.
     * @param pageSize
     *
     * @return {@link PageImpl}
     */
    @NotNull PageImpl<SecurityResource> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数.
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询指定的资源
     *
     * @param code  {@linkplain SecurityResource#getCode() 资源编码}
     *
     * @return {@linkplain SecurityResource 指定的资源}
     */
    @NotNull SecurityResource selectResourceByCode(@NotNull String code)
            throws IllegalArgumentException;

}
