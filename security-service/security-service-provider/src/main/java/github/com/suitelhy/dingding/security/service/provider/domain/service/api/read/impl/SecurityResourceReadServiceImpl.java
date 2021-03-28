package github.com.suitelhy.dingding.security.service.provider.domain.service.api.read.impl;

import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageRequest;
import github.com.suitelhy.dingding.core.infrastructure.domain.Pageable;
import github.com.suitelhy.dingding.core.infrastructure.domain.Sort;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityResourceReadService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityResourceService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.*;

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
@Service(cluster = "failover")
public class SecurityResourceReadServiceImpl
        implements SecurityResourceReadService {

    @Autowired
    private SecurityResourceService securityResourceService;

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从 0 开始.
     * @param pageSize
     *
     * @return {@link PageImpl}
     */
    @Override
    public @NotNull PageImpl<SecurityResource> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException
    {
        final org.springframework.data.domain.Page<SecurityResource> result = securityResourceService.selectAll(pageIndex, pageSize);

        if (result instanceof org.springframework.data.domain.PageImpl) {
            return PageImpl.Factory.DEFAULT.create((org.springframework.data.domain.PageImpl) result);
        }

        Sort.TypedSort<SecurityResource> typedSort = Sort.sort(SecurityResource.class);
        @NotNull Sort sort = typedSort.by(SecurityResource::getSort).descending()
                .and(typedSort.by(SecurityResource::getCode).ascending());
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return PageImpl.Factory.DEFAULT.create(result.getContent()
                , page
                , result.getTotalElements());
    }

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数.
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return 分页 - 总页数
     */
    @Override
    public @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException
    {
        return securityResourceService.selectCount(pageSize);
    }

    /**
     * 查询指定的资源
     *
     * @param code {@linkplain SecurityResource#getCode() 资源编码}
     *
     * @return {@linkplain SecurityResource 指定的资源}
     */
    @Override
    public @NotNull SecurityResource selectResourceByCode(@NotNull String code)
            throws IllegalArgumentException
    {
        return securityResourceService.selectResourceByCode(code);
    }

}
