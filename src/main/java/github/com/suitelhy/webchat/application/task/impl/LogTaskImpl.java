package github.com.suitelhy.webchat.application.task.impl;

import github.com.suitelhy.webchat.domain.entity.Log;
import github.com.suitelhy.webchat.application.task.LogTask;
import github.com.suitelhy.webchat.domain.service.LogService;
import github.com.suitelhy.webchat.infrastructure.domain.policy.DBPolicy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志记录相关业务
 */
@Service("logTask")
public class LogTaskImpl implements LogTask {

    @Resource
    private LogService logService;

    @Override
    public List<Log> selectAll(int page, int pageSize) {
        return logService.selectAll(page, pageSize).toList();
    }

    @Override
    public List<Log> selectLogByUserid(String userid, int page, int pageSize) {
        if (null == userid || !DBPolicy.MYSQL.validateUuid(userid)) {
            throw new RuntimeException("非法输入: 用户ID");
        }
        if (page < 1) {
            throw new RuntimeException("非法输入: <param>page</param>");
        }
        if (pageSize < 1) {
            throw new RuntimeException("非法输入: <param>pageSize</param>");
        }
        return logService.selectLogByUserid(userid, page, pageSize);
    }

    @Override
    public Integer selectCount(int pageSize) {
        int dataNumber = logService.selectCount(pageSize);
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    public Integer selectCountByUserid(String userid, int pageSize) {
        if (null == userid || !DBPolicy.MYSQL.validateUuid(userid)) {
            throw new RuntimeException("非法输入: 用户ID");
        }
        int dataNumber = logService.selectCountByUserid(userid, pageSize);
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    public boolean insert(Log log) {
        if (null == log || (null != log.id() && log.isEmpty())) {
            throw new RuntimeException("非法输入: <param>log</param>");
        }
        return logService.insert(log);
    }

    @Override
    public boolean delete(String id) {
        if (null == id || !DBPolicy.MYSQL.validateUuid(id)) {
            throw new RuntimeException("非法输入: 日志记录ID");
        }
        return logService.delete(id);
    }

}
