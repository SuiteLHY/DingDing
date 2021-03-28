package github.com.suitelhy.dingding.user.service.provider.domain.service.api.read.impl;

import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.domain.PageRequest;
import github.com.suitelhy.dingding.core.infrastructure.domain.Pageable;
import github.com.suitelhy.dingding.user.service.api.domain.entity.Log;
import github.com.suitelhy.dingding.user.service.api.domain.service.read.LogReadService;
import github.com.suitelhy.dingding.user.service.provider.domain.service.LogService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 日志记录 - 业务接口
 *
 * @Design
 * · 只读操作
 * · 幂等性
 *
 * @see Log
 * @see Dubbo.Strategy.ClusterVo#FAILOVER
 */
@Service(cluster = "failover")
public class LogReadServiceImpl
        implements LogReadService {

    @Autowired
    private LogService logService;

    /**
     * 查询所有日志记录 (分页)
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @Override
    public @NotNull PageImpl<Log> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException
    {
        final @NotNull Page<Log> result = logService.selectAll(pageIndex, pageSize);

        if (result instanceof org.springframework.data.domain.PageImpl) {
            return PageImpl.Factory.DEFAULT.create((org.springframework.data.domain.PageImpl) result);
        }

        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize);
        return PageImpl.Factory.DEFAULT.create(result.getContent(), page, result.getTotalElements());
    }

    /**
     * 查询所有日志记录数量
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return
     */
    @Override
    public @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException
    {
        return logService.selectCount(pageSize);
    }

    /**
     * 查询指定用户对应的日志记录数量
     *
     * @param username {@link Log.Validator#operatorUsername(String)}
     * @param pageSize 分页 - 每页容量
     *
     * @return {@link Long}
     */
    @Override
    public @NotNull Long selectCountByUsername(@NotNull String username, int pageSize)
            throws IllegalArgumentException
    {
        return logService.selectCountByUsername(username, pageSize);
    }

    /**
     * 查询指定的日志记录
     *
     * @param id [日志记录 - 编号]
     *
     * @return {@link Log}
     */
    @Override
    public @NotNull Log selectLogById(@NotNull String id)
            throws IllegalArgumentException
    {
        return logService.selectLogById(id);
    }

    /**
     * 查询指定用户对应的日志记录 (分页)
     *
     * @param username {@link Log.Validator#operatorUsername(String)}
     * @param page     分页索引, 从 0 开始
     * @param pageSize 分页 - 每页容量
     *
     * @return {@link List}
     */
    @Override
    public @NotNull List<Log> selectLogByUsername(@NotNull String username, int page, int pageSize)
            throws IllegalArgumentException
    {
        return logService.selectLogByUsername(username, page, pageSize);
    }

}
