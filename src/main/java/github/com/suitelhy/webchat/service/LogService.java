package github.com.suitelhy.webchat.service;


import github.com.suitelhy.webchat.domain.entity.Log;

import java.util.List;

/**
 * 日志记录信息业务接口
 */
public interface LogService {

    /**
     *
     * @param page
     * @param pageSize
     * @return
     */
    List<Log> selectAll(int page, int pageSize);

    /**
     *
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
     *
     * @param log
     * @return
     */
    boolean insert(Log log);

    /**
     *
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     *
     * @param userid
     * @return
     */
    boolean deleteThisUser(String userid);

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteAll();*/

}
