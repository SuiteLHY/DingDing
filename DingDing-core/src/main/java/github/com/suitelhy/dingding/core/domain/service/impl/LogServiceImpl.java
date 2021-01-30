package github.com.suitelhy.dingding.core.domain.service.impl;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.repository.LogRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
@Service("logService")
@Order(Ordered.LOWEST_PRECEDENCE)
public class LogServiceImpl
        implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public Page<Log> selectAll(int pageIndex, int pageSize)
            throws IllegalArgumentException {
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

        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        return logRepository.findAll(pageable);
    }

    @Override
    public Long selectCount(int pageSize)
            throws IllegalArgumentException {
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
     * 查询指定用户对应的日志记录数量
     *
     * @param username {@link Log.Validator#operatorUsername(String)}
     * @param pageSize 分页 - 每页容量
     * @return {@link Long}
     */
    @Override
    public Long selectCountByUsername(@NotNull String username, int pageSize)
            throws IllegalArgumentException {
        if (!Log.Validator.USER.operatorUsername(username)) {
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
     * @return {@link Log}
     */
    @Override
    public Log selectLogById(@NotNull String id)
            throws IllegalArgumentException {
        if (null == id || !Log.Validator.LOG.id(id)) {
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
     * 查询指定用户对应的日志记录 (分页)
     *
     * @param username  {@link Log.Validator#operatorUsername(String)}
     * @param pageIndex 分页索引, 从 0 开始
     * @param pageSize  分页 - 每页容量
     * @return {@link List}
     */
    @Override
    public List<Log> selectLogByUsername(@NotNull String username, int pageIndex, int pageSize)
            throws IllegalArgumentException {
        if (!Log.Validator.USER.operatorUsername(username)) {
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

        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        return logRepository.findByTargetId(username, pageable);
    }

    /**
     * 新增日志记录
     *
     * @param log {@link Log}
     * @return 操作是否成功 / 是否已存在有效数据
     */
    @Override
    @Transactional
    public boolean insert(@NotNull Log log)
            throws IllegalArgumentException {
        if (null == log || !log.isEntityLegal()) {
            //-- 非法输入: 日志记录
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "日志记录"
                    , log
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        logRepository.saveAndFlush(log);
        return !log.isEmpty()
                && logRepository.existsById(log.id());
    }

    /**
     * 删除日志记录
     *
     * @param id [日志记录 - 编号] {@link Log.Validator#id(String)}
     * @return {@link Long}
     */
    @Override
    @Transactional
    public boolean deleteById(@NotNull Long id)
            throws IllegalArgumentException {
        if (!Log.Validator.LOG.id(id)) {
            //-- 非法输入: [日志记录 - 编号]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[日志记录 - 编号]"
                    , id
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        logRepository.deleteById(id);
        return !logRepository.existsById(id);
    }

}
