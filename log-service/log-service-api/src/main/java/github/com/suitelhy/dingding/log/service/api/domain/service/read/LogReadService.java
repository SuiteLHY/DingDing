package github.com.suitelhy.dingding.log.service.api.domain.service.read;

import github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl;
import github.com.suitelhy.dingding.log.service.api.domain.entity.Log;

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
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAILOVER
 */
public interface LogReadService {

    /**
     * 查询所有日志记录 (分页)
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link PageImpl}
     */
    @NotNull PageImpl<Log> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询所有日志记录数量
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return {@linkplain Long 日志记录数量}
     */
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询[指定的操作者对应的日志记录数量]
     *
     * @param username {@linkplain Log.Validator#operatorUsername(String) [操作者 - 用户名称]}
     * @param pageSize 分页 - 每页容量
     *
     * @return {@linkplain Long 指定的操作者对应的日志记录数量}
     */
    @NotNull Long selectCountByUsername(@NotNull String username, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询指定的日志记录
     *
     * @param id [日志记录 - 编号]
     *
     * @return {@linkplain Log 指定的日志记录}
     */
    @NotNull Log selectLogById(@NotNull String id)
            throws IllegalArgumentException;

    /**
     * 查询[指定操作者对应的日志记录] (分页)
     *
     * @param username  {@linkplain Log.Validator#operatorUsername(String) [操作者 - 用户名称]}
     * @param pageIndex 分页索引, 从 0 开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@linkplain Log 指定操作者对应的日志记录}
     */
    @NotNull List<Log> selectLogByUsername(@NotNull String username, int pageIndex, int pageSize)
            throws IllegalArgumentException;

}
