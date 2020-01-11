package github.com.suitelhy.webchat.domain.service.impl;

import github.com.suitelhy.webchat.domain.entity.Log;
import github.com.suitelhy.webchat.domain.repository.LogRepository;
import github.com.suitelhy.webchat.domain.service.LogService;
import github.com.suitelhy.webchat.infrastructure.domain.policy.DBPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日志记录- 业务实现
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public Page<Log> selectAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return logRepository.findAll(pageable);
    }

    @Override
    public List<Log> selectLogByUserid(String userid, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return logRepository.findByUserid(userid, pageable);
    }

    @Override
    public Integer selectCount(int pageSize) {
        int dataNumber = (int) logRepository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    public Integer selectCountByUserid(String userid, int pageSize) {
        int dataNumber = (int) logRepository.countByUserid(userid);
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    @Transactional
    public boolean insert(Log log) {
        if (null != log && !log.isEmpty()) {
            return null != logRepository.saveAndFlush(log);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        if (null != id) {
            logRepository.deleteById(id);
            return logRepository.existsById(id);
        }
        return false;
    }

}
