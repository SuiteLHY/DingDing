package github.com.suitelhy.webchat.service.impl;

import github.com.suitelhy.webchat.domain.entity.Log;
import github.com.suitelhy.webchat.domain.mapper.LogMapper;
import github.com.suitelhy.webchat.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志记录相关业务
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Resource
    private LogMapper logMapper;

    @Resource
    private Log log;

    @Override
    public List<Log> selectAll(int page, int pageSize) {
        return logMapper.selectAll(page, pageSize);
    }

    @Override
    public List<Log> selectLogByUserid(String userid, int page, int pageSize) {
        int start = 1;
        int end = pageSize;
        if (page != 1) {
            start = pageSize * (page - 1) + 1;
            end = pageSize * page;
        }
        return logMapper.selectLogByUserid(userid, start, end);
    }

    @Override
    public Integer selectCount(int pageSize) {
        int pageCount = Integer.parseInt(logMapper.selectCount().getUserid());
        return (pageCount % pageSize == 0)
                ? (pageCount / pageSize)
                : (pageCount/pageSize + 1);
    }

    @Override
    public Integer selectCountByUserid(String userid, int pageSize) {
        int pageCount = Integer.parseInt(logMapper.selectCountByUserid(userid).getUserid());
        return (pageCount % pageSize == 0)
                ? (pageCount / pageSize)
                : (pageCount / pageSize + 1);
    }


    @Override
    public boolean insert(Log log) {
        return logMapper.insert(log);
    }

    @Override
    public boolean delete(String id) {
        return logMapper.delete(id);
    }

    @Override
    public boolean deleteThisUser(String userid) {
        return logMapper.deleteThisUser(userid);
    }

    /*@Override
    public boolean deleteAll() {
        return logMapper.deleteAll();
    }*/

}
