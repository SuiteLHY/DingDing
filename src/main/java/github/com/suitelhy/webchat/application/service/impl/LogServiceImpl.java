package github.com.suitelhy.webchat.application.service.impl;

import github.com.suitelhy.webchat.domain.entity.Log;
import github.com.suitelhy.webchat.domain.repository.LogRepository;
import github.com.suitelhy.webchat.application.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志记录相关业务
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Resource
    private LogRepository logRepository;

    @Override
    public List<Log> selectAll(int page, int pageSize) {
        return logRepository.selectAll(page, pageSize);
    }

    @Override
    public List<Log> selectLogByUserid(String userid, int page, int pageSize) {
        int start = 1;
        int end = pageSize;
        if (page != 1) {
            start = pageSize * (page - 1) + 1;
            end = pageSize * page;
        }
        return logRepository.selectLogByUserid(userid, start, end);
    }

    @Override
    public Integer selectCount(int pageSize) {
        int dataNumber = Integer.parseInt(logRepository.selectCount().getUserid());
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    public Integer selectCountByUserid(String userid, int pageSize) {
        int dataNumber = Integer.parseInt(logRepository.selectCountByUserid(userid).getUserid());
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    public boolean insert(Log log) {
        return logRepository.insert(log);
    }

    @Override
    public boolean delete(String id) {
        return logRepository.delete(id);
    }

    /*@Override
    public boolean deleteThisUser(String userid) {
        return logRepository.deleteThisUser(userid);
    }*/

    /*@Override
    public boolean deleteAll() {
        return logMapper.deleteAll();
    }*/

}
