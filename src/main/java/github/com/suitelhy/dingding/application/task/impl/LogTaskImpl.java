package github.com.suitelhy.dingding.application.task.impl;

import github.com.suitelhy.dingding.application.task.LogTask;
import github.com.suitelhy.dingding.domain.entity.Log;
import github.com.suitelhy.dingding.domain.entity.User;
import github.com.suitelhy.dingding.domain.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志记录 - 任务调度实现
 */
@Service("logTask")
@Slf4j
public class LogTaskImpl implements LogTask {

    @Resource
    private LogService logService;

    @Override
    public List<Log> selectAll(int pageCount, int pageSize) {
        return logService.selectAll(--pageCount, pageSize).toList();
    }

    @Override
    public List<Log> selectLogByUserid(String userid, int pageCount, int pageSize) {
        if (!User.Validator.USER.userid(userid)) {
            throw new RuntimeException("非法输入: 用户ID");
        }
        if (pageCount < 1) {
            throw new RuntimeException("非法输入: <param>pageCount</param>");
        }
        if (pageSize < 1) {
            throw new RuntimeException("非法输入: <param>pageSize</param>");
        }
        return logService.selectLogByUserid(userid, --pageCount, pageSize);
    }

    @Override
    public Long selectCount(int pageSize) {
        if (pageSize < 1) {
            throw new RuntimeException("非法输入: <param>pageSize</param>");
        }
        long dataNumber = logService.selectCount(pageSize);
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    public Long selectCountByUserid(String userid, int pageSize) {
        if (!User.Validator.USER.userid(userid)) {
            throw new RuntimeException("非法输入: 用户ID");
        }
        if (pageSize < 1) {
            throw new RuntimeException("非法输入: <param>pageSize</param>");
        }
        long dataNumber = logService.selectCountByUserid(userid, pageSize);
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    public boolean insert(Log log) {
        if (null == log || !log.isEntityLegal()) {
            throw new RuntimeException("非法输入: <param>log</param>");
        }
        return logService.insert(log);
    }

    @Override
    public boolean delete(String id) {
        if (!User.Validator.USER.id(id)) {
            throw new RuntimeException("非法输入: 日志记录ID");
        }
        return logService.deleteById(Long.parseLong(id));
    }

}
