package github.com.suitelhy.dingding.core.domain.service;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 日志记录 - 业务接口
 */
public interface LogService {

    /**
     * 查询所有日志记录 (分页)
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize
     * @return
     */
    Page<Log> selectAll(int pageIndex, int pageSize);

    /**
     * 查询所有日志记录数量
     *
     * @param pageSize
     * @return
     */
    Long selectCount(int pageSize);

    /**
     * 查询指定用户对应的日志记录数量
     *
     * @param userid
     * @param pageSize
     * @return
     */
    Long selectCountByUserid(String userid, int pageSize);

    /**
     * 查询指定用户对应的日志记录 (分页)
     *
     * @param userid
     * @param page     从0开始
     * @param pageSize
     * @return
     */
    List<Log> selectLogByUserid(String userid, int page, int pageSize);

    /**
     * 新增日志记录
     *
     * @param log
     * @return
     */
    boolean insert(Log log);

    /**
     * 删除日志记录
     *
     * @param id - 日志记录 id
     * @return
     */
    boolean deleteById(Long id);

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteThisUser(String userid);*/

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteAll();*/

}
