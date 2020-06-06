package github.com.suitelhy.dingding.core.application.task;

import github.com.suitelhy.dingding.core.domain.entity.Log;

import java.util.List;

/**
 * 日志记录 - 任务调度接口
 */
public interface LogTask {

    /**
     * 查询所有日志记录 (分页)
     *
     * @param pageCount 页码, 从1开始
     * @param pageSize
     * @return
     */
    List<Log> selectAll(int pageCount, int pageSize);

    /**
     * 查询日志记录数量 (分页)
     *
     * @param pageSize
     * @return
     */
    Long selectCount(int pageSize);

    /**
     * 查询指定用户对应的日志记录 (分页)
     *
     * @param userid
     * @param pageCount 页码, 从1开始
     * @param pageSize
     * @return
     */
    List<Log> selectLogByUserid(String userid, int pageCount, int pageSize);

    /**
     * 查询指定用户对应的日志记录数量 (分页)
     *
     * @param userid
     * @param pageSize
     * @return
     */
    Long selectCountByUserid(String userid, int pageSize);

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
    boolean delete(String id);

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteThisUser(String userid);*/

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteAll();*/

}
