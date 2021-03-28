package github.com.suitelhy.dingding.log.service.provider.domain.service.impl;

import github.com.suitelhy.dingding.log.service.api.domain.entity.Log;
import github.com.suitelhy.dingding.log.service.provider.domain.repository.LogRepository;
import github.com.suitelhy.dingding.log.service.provider.domain.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 日志记录 - 业务实现
 *
 * @see Log
 * @see LogRepository
 * @see LogService
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Service("logService")
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public class LogServiceImpl
        implements LogService {

    @Autowired
    private LogRepository logRepository;

    /**
     * 查询所有日志记录 (分页)
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link Page}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull Page<Log> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException
    {
        if (pageIndex < 0) {
            //-- 非法输入: <param>pageIndex</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageIndex"
                    , pageIndex
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize);

        return logRepository.findAll(page);
    }

    /**
     * 查询所有日志记录数量
     *
     * @param pageSize 分页 - 每页容量
     *
     * @return {@linkplain Long 日志记录数量}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull Long selectCount(int pageSize)
            throws IllegalArgumentException
    {
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        long dataNumber = logRepository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询[指定的操作者对应的日志记录数量]
     *
     * @param username {@linkplain Log.Validator#operatorUsername(String) [操作者 - 用户名称]}
     * @param pageSize 分页 - 每页容量
     *
     * @return {@linkplain Long 指定的操作者对应的日志记录数量}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull Long selectCountByUsername(@NotNull String username, int pageSize)
            throws IllegalArgumentException
    {
        if (! Log.Validator.USER.operatorUsername(username)) {
            //-- 非法输入: [用户 - 用户名称]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 - 用户名称]"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        long dataNumber = logRepository.countByTargetId(username);
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询指定的日志记录
     *
     * @param id [日志记录 - 编号]
     *
     * @return {@linkplain Log 指定的日志记录}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull Log selectLogById(@NotNull String id)
            throws IllegalArgumentException
    {
        if (null == id || ! Log.Validator.LOG.id(id)) {
            //-- 非法输入: [日志记录 - 编号]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[日志记录 - 编号]"
                    , id
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return logRepository.findLogById(Long.parseLong(id))
                .orElseGet(Log.Factory.User.LOG::createDefault);
    }

    /**
     * 查询[指定操作者对应的日志记录] (分页)
     *
     * @param username  {@linkplain Log.Validator#operatorUsername(String) [操作者 - 用户名称]}
     * @param pageIndex 分页索引, 从 0 开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@linkplain Log 指定操作者对应的日志记录}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull List<Log> selectLogByUsername(@NotNull String username, int pageIndex, int pageSize)
            throws IllegalArgumentException
    {
        if (! Log.Validator.USER.operatorUsername(username)) {
            //-- 非法输入: [用户 - 用户名称]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 - 用户名称]"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (pageIndex < 0) {
            //-- 非法输入: <param>pageIndex</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageIndex"
                    , pageIndex
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        @NotNull Pageable pageable = PageRequest.of(pageIndex, pageSize);

        return logRepository.findByTargetId(username, pageable);
    }

    /**
     * 新增日志记录
     *
     * @param log {@linkplain Log 日志记录}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在有效数据}
     */
    @Override
    public boolean insert(@NotNull Log log)
            throws IllegalArgumentException
    {
        if (null == log || ! log.isEntityLegal()) {
            //-- 非法输入: 日志记录
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "日志记录"
                    , log
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        logRepository.saveAndFlush(log);
        return ! log.isEmpty()
                && logRepository.existsById(log.id());
    }

    /**
     * 删除日志记录
     *
     * @param id    {@linkplain Log.Validator#id(String) [日志记录 - 编号]}
     *
     * @return {@link Boolean#TYPE 操作是否成功 / 指定日志记录是否已被删除}
     */
    @Override
    public boolean deleteById(@NotNull Long id)
            throws IllegalArgumentException
    {
        if (! Log.Validator.LOG.id(id)) {
            //-- 非法输入: [日志记录 - 编号]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[日志记录 - 编号]"
                    , id
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        logRepository.deleteById(id);
        return ! logRepository.existsById(id);
    }

}
