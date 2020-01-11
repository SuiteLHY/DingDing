package github.com.suitelhy.webchat.domain.service;

import github.com.suitelhy.webchat.domain.entity.Log;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 日志记录 - 业务接口
 */
public interface LogService {

    /**
     * 查询所有日志记录 (分页)
     * @param page
     * @param pageSize
     * @return
     */
    Page<Log> selectAll(int page, int pageSize);

    /**
     * 查询指定用户对应的日志记录 (分页)
     * @param userid
     * @param page
     * @param pageSize
     * @return
     */
    List<Log> selectLogByUserid(String userid, int page, int pageSize);

    /**
     *
     * @param pageSize
     * @return
     */
    Integer selectCount(int pageSize);

    /**
     *
     * @param userid
     * @param pageSize
     * @return
     */
    Integer selectCountByUserid(String userid, int pageSize);

    /**
     * 新增日志记录
     * @param log
     * @return
     */
    boolean insert(Log log);

    /**
     * 删除日志记录
     * @param id - 日志记录 id
     * @return
     */
    boolean delete(String id);

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteThisUser(String userid);*/

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteAll();*/

}
