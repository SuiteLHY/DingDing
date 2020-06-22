package github.com.suitelhy.dingding.core.domain.service.impl;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.entity.User;
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
 * @see github.com.suitelhy.dingding.core.domain.service.LogService
 */
@Service("logService")
@Order(Ordered.LOWEST_PRECEDENCE)
public class LogServiceImpl
        implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public Page<Log> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            //-- 非法输入: <param>dataIndex</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>dataIndex</param>"));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageSize</param>"));
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        return logRepository.findAll(pageable);
    }

    @Override
    public List<Log> selectLogByUserid(@NotNull String userid, int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            //-- 非法输入: <param>dataIndex</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>dataIndex</param>"));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageSize</param>"));
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        return logRepository.findByUserid(userid, pageable);
    }

    @Override
    public Long selectCount(int pageSize) {
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageSize</param>"));
        }

        long dataNumber = logRepository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    public Long selectCountByUserid(@NotNull String userid, int pageSize) {
        if (!User.Validator.USER.userid(userid)) {
            //-- 非法输入: 用户ID
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 用户ID"));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageSize</param>"));
        }

        long dataNumber = logRepository.countByUserid(userid);
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    @Transactional
    public boolean insert(@NotNull Log log) {
        if (null != log && log.isEntityLegal()) {
            logRepository.saveAndFlush(log);
            return !log.isEmpty()
                    && logRepository.existsById(log.id());
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteById(@NotNull Long id) {
        if (Log.Validator.LOG.entity_id(id)) {
            logRepository.deleteById(id);
            return !logRepository.existsById(id);
        }
        return false;
    }

}
