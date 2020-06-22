package github.com.suitelhy.dingding.core.domain.service;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 日志记录 - 业务接口
 *
 * @see github.com.suitelhy.dingding.core.domain.entity.Log
 * @see github.com.suitelhy.dingding.core.domain.repository.LogRepository
 */
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public interface LogService
        extends EntityService {

    /**
     * 查询所有日志记录 (分页)
     *
     * @param pageIndex     分页索引, 从0开始
     * @param pageSize      分页 - 每页容量
     * @return {@link org.springframework.data.domain.Page}
     */
    Page<Log> selectAll(int pageIndex, int pageSize);

    /**
     * 查询所有日志记录数量
     *
     * @param pageSize      分页 - 每页容量
     * @return
     */
    Long selectCount(int pageSize);

    /**
     * 查询指定用户对应的日志记录数量
     *
     * @param userid
     * @param pageSize      分页 - 每页容量
     * @return
     */
    Long selectCountByUserid(@NotNull String userid, int pageSize);

    /**
     * 查询指定用户对应的日志记录 (分页)
     *
     * @param userid
     * @param page          分页索引, 从 0 开始
     * @param pageSize      分页 - 每页容量
     * @return
     */
    List<Log> selectLogByUserid(@NotNull String userid, int page, int pageSize);

    /**
     * 新增日志记录
     *
     * @param log
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean insert(@NotNull Log log);

    /**
     * 删除日志记录
     *
     * @param id        日志记录 id
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , rollbackFor = Exception.class
            , timeout = 15)
    boolean deleteById(@NotNull Long id);

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteThisUser(String userid);*/

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteAll();*/

}
