package github.com.suitelhy.dingding.user.service.provider.domain.service;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import github.com.suitelhy.dingding.user.service.api.domain.entity.Log;
import github.com.suitelhy.dingding.user.service.provider.domain.repository.LogRepository;
import github.com.suitelhy.dingding.user.service.provider.domain.service.impl.LogServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 日志记录 - 业务接口
 *
 * @see Log
 * @see LogRepository
 * @see LogServiceImpl
 */
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public interface LogService
        extends EntityService {

    /**
     * 查询所有日志记录 (分页)
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link Page}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull Page<Log> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询所有日志记录数量
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return {@linkplain Long 日志记录数量}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询指定用户对应的日志记录数量
     *
     * @param username {@link Log.Validator#operatorUsername(String)}
     * @param pageSize 分页 - 每页容量
     *
     * @return {@link Long}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull Long selectCountByUsername(@NotNull String username, int pageSize)
            throws IllegalArgumentException;

    /**
     * 查询指定的日志记录
     *
     * @param id [日志记录 - 编号]
     *
     * @return {@link Log}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull Log selectLogById(@NotNull String id)
            throws IllegalArgumentException;

    /**
     * 查询指定用户对应的日志记录 (分页)
     *
     * @param username {@link Log.Validator#operatorUsername(String)}
     * @param page     分页索引, 从 0 开始
     * @param pageSize 分页 - 每页容量
     *
     * @return {@link List}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    @NotNull List<Log> selectLogByUsername(@NotNull String username, int page, int pageSize)
            throws IllegalArgumentException;

    /**
     * 新增日志记录
     *
     * @param log {@link Log}
     *
     * @return 操作是否成功 / 是否已存在有效数据
     */
    boolean insert(@NotNull Log log)
            throws IllegalArgumentException;

    /**
     * 删除日志记录
     *
     * @param id [日志记录 - 编号] {@link Log.Validator#id(String)}
     *
     * @return {@link Long}
     */
    boolean deleteById(@NotNull Long id)
            throws IllegalArgumentException;

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteThisUser(String userid);*/

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteAll();*/

}
