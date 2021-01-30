package github.com.suitelhy.dingding.core.application.task;

import github.com.suitelhy.dingding.core.application.task.impl.LogTaskImpl;
import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 日志记录 - 任务调度接口
 *
 * @see LogTaskImpl
 * @see OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails
 */
public interface LogTask {

    /**
     * 查询所有日志记录 (分页)
     *
     * @param pageCount 页码, 从1开始
     * @param pageSize  每页容量, [5,20]
     * @param operator  操作者
     * @return {@link TaskResult}
     */
    @NotNull
    TaskResult<List<Log>> selectAll(int pageCount, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询日志记录数量 (分页)
     *
     * @param pageSize 每页容量, [5,20]
     * @param operator 操作者
     * @return {@link TaskResult}
     */
    @NotNull
    TaskResult<Long> selectCount(int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询指定的日志记录
     *
     * @param id       [日志记录 - 编号]
     * @param operator 操作者
     * @return {@link TaskResult}
     */
    @NotNull
    TaskResult<Log> selectLogById(@NotNull String id, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询指定用户对应的日志记录 (分页)
     *
     * @param username  [操作者 - 用户名称]
     * @param pageCount 页码, 从1开始
     * @param pageSize  每页容量, [5,20]
     * @param operator  操作者
     * @return {@link TaskResult}
     */
    @NotNull
    TaskResult<List<Log>> selectLogByUsername(@NotNull String username, int pageCount, int pageSize
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询指定用户对应的日志记录数量 (分页)
     *
     * @param username [操作者 - 用户名称]
     * @param pageSize 每页容量, [5,20]
     * @param operator 操作者
     * @return 页数   {@link TaskResult}
     */
    @NotNull
    TaskResult<Long> selectCountByUsername(@NotNull String username, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 新增日志记录
     *
     * @param log      日志记录
     * @param operator 操作者
     * @return {@link TaskResult}
     */
    @Transactional
    @NotNull
    TaskResult<Log> insert(@NotNull Log log, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 删除日志记录
     *
     * @param id       [日志记录 - 编号]
     * @param operator 操作者
     * @return {@link TaskResult}
     */
    @Transactional
    @NotNull
    TaskResult<Boolean> delete(@NotNull String id, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteThisUser(String userid);*/

    // 屏蔽不严谨设计的业务方法
    /*boolean deleteAll();*/

}
