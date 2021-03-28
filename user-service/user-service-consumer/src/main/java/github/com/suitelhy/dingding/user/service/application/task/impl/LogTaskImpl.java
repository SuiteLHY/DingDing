package github.com.suitelhy.dingding.user.service.application.task.impl;

import github.com.suitelhy.dingding.core.infrastructure.application.PageImpl;
import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.domain.Page;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.Log;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.user.service.api.domain.service.read.LogReadService;
import github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence.LogNonIdempotentWriteService;
import github.com.suitelhy.dingding.user.service.application.task.LogTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志记录 - 任务调度实现
 */
@Service("logTask")
@Slf4j
public class LogTaskImpl
        implements LogTask {

    @Reference
    private LogReadService logReadService;

    @Reference
    private LogNonIdempotentWriteService logNonIdempotentWriteService;

    /**
     * 查询所有日志记录 (分页)
     *
     * @param pageCount 页码, 从1开始
     * @param pageSize  每页容量, [5,20]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<Page<String>> selectAll(int pageCount, int pageSize) {
        @NotNull TaskResult<Page<String>> taskResult;
        @NotNull Page<String> resultData = Page.empty();
        final @NotNull List<String> resultData_content = new ArrayList<>(0);
        try {
            if (pageCount < 1) {
                //-- 非法输入: 页码
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "页码"
                        , pageCount
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (pageSize < 5 || pageSize > 20) {
                //-- 非法输入: 每页容量
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "每页容量"
                        , pageSize
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            final @NotNull Page<Log> logPage = logReadService.selectAll(-- pageCount, pageSize);
            if (! logPage.getContent().isEmpty()) {
                for (@NotNull Log eachLog : logPage) {
                    resultData_content.add(eachLog.toJSONString());
                }
            }
            resultData = PageImpl.Factory.DEFAULT.create(resultData_content, logPage);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                    , null
                    , resultData);
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询所有日志记录 (分页)
     *
     * @param pageCount 页码, 从1开始
     * @param pageSize  每页容量, [5,20]
     * @param operator  操作者
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Page<String>> selectAll(int pageCount, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<Page<String>> taskResult;
        final @NotNull Page<String> resultData = Page.empty();
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectAll(pageCount, pageSize);
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询日志记录数量 (分页)
     *
     * @param pageSize 每页容量, [5,20]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<Long> selectCount(int pageSize) {
        @NotNull TaskResult<Long> taskResult;
        @NotNull Long resultData = 0L;
        try {
            if (pageSize < 5 || pageSize > 20) {
                //-- 非法输入: 每页容量
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "每页容量"
                        , pageSize
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            long dataNumber = logReadService.selectCount(pageSize);
            resultData = (dataNumber % pageSize == 0)
                    ? (dataNumber / pageSize)
                    : ((dataNumber / pageSize) + 1);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                    , null
                    , resultData);
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询日志记录数量 (分页)
     *
     * @param pageSize 每页容量, [5,20]
     * @param operator 操作者
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<Long> selectCount(int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<Long> taskResult;
        @NotNull Long resultData = 0L;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectCount(pageSize);
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询指定的日志记录
     *
     * @param id [日志记录 - 编号]
     *
     * @return {@link Log}
     */
    private @NotNull TaskResult<Log> selectLogById(@NotNull String id) {
        @NotNull TaskResult<Log> taskResult;
        @Nullable Log resultData = null;
        try {
            if (! Log.Validator.LOG.id(id)) {
                //-- 非法输入: [日志记录 - 编号]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "[日志记录 - 编号]"
                        , id
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            resultData = logReadService.selectLogById(id);

            if (null != resultData && ! resultData.isEmpty()) {
                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                        , null
                        , resultData);
            }
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询指定的日志记录
     *
     * @param id       [日志记录 - 编号]
     * @param operator 操作者
     *
     * @return {@link Log}
     */
    @Override
    public @NotNull TaskResult<Log> selectLogById(@NotNull String id, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<Log> taskResult;
        @Nullable Log resultData = null;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectLogById(id);
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询指定用户对应的日志记录 (分页)
     *
     * @param username  [操作者 - 用户名称]
     * @param pageCount 页码, 从1开始
     * @param pageSize  每页容量, [5,20]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<List<Log>> selectLogByUsername(@NotNull String username, int pageCount, int pageSize) {
        @NotNull TaskResult<List<Log>> taskResult;
        final @NotNull List<Log> resultData = new ArrayList<>(0);
        try {
            if (! User.Validator.USER.username(username)) {
                //-- 非法输入: 用户名称
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户名称"
                        , username
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (pageCount < 1) {
                //-- 非法输入: pageCount
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "pageCount"
                        , pageCount
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (pageSize < 5 || pageSize > 20) {
                //-- 非法输入: 每页容量
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "每页容量"
                        , pageSize
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            @NotNull List<Log> result = logReadService.selectLogByUsername(username, --pageCount, pageSize);
            if (null != result && ! result.isEmpty()) {
                resultData.addAll(result);

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                if (null != result) {
                    resultData.addAll(result);
                }

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                        , null
                        , resultData);
            }
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询指定用户对应的日志记录 (分页)
     *
     * @param username  [操作者 - 用户名称]
     * @param pageCount 页码, 从1开始
     * @param pageSize  每页容量, [5,20]
     * @param operator  操作者
     *
     * @return {@link TaskResult}
     */
    @Override
    public @NotNull TaskResult<List<Log>> selectLogByUsername(@NotNull String username, int pageCount, int pageSize
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator)
    {
        @NotNull TaskResult<List<Log>> taskResult;
        final @NotNull List<Log> resultData = new ArrayList<>(0);
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (!Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectLogByUsername(username, pageCount, pageSize);
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询指定用户对应的日志记录数量 (分页)
     *
     * @param username [操作者 - 用户名称]
     * @param pageSize 每页容量, [5,20]
     *
     * @return {@linkplain TaskResult 页数}
     */
    private @NotNull TaskResult<Long> selectCountByUsername(@NotNull String username, int pageSize) {
        @NotNull TaskResult<Long> taskResult;
        @NotNull Long resultData = 0L;
        try {
            if (! User.Validator.USER.username(username)) {
                //-- 非法输入: 用户名称
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户名称"
                        , username
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (pageSize < 5 || pageSize > 20) {
                //-- 非法输入: 每页容量
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "每页容量"
                        , pageSize
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            long dataNumber = logReadService.selectCountByUsername(username, pageSize);
            resultData = (dataNumber % pageSize == 0)
                    ? (dataNumber / pageSize)
                    : ((dataNumber / pageSize) + 1);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                    , null
                    , resultData);
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 查询指定用户对应的日志记录数量 (分页)
     *
     * @param username [操作者 - 用户名称]
     * @param pageSize 每页容量, [5,20]
     * @param operator 操作者
     *
     * @return {@linkplain TaskResult 页数}
     */
    @Override
    public @NotNull TaskResult<Long> selectCountByUsername(@NotNull String username, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<Long> taskResult;
        @NotNull Long resultData = 0L;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    if (Security.RoleVo.USER.name().equals(authority)) {
                        isInsufficientPermissions = true;
                        break;
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return selectCountByUsername(username, pageSize);
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 新增日志记录
     *
     * @param param_log 日志记录
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<Log> insert(@NotNull Log param_log) {
        @NotNull TaskResult<Log> taskResult;
        @Nullable Log resultData = null;
        try {
            if (null == param_log || ! param_log.isEntityLegal()) {
                //-- 非法输入: 日志记录
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "日志记录"
                        , param_log
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            final boolean result = logNonIdempotentWriteService.insert(param_log);

            if (result) {
                resultData = param_log;

                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                        , null
                        , resultData);
            }
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 新增日志记录
     *
     * @param param_log 日志记录
     * @param operator  操作者
     *
     * @return {@link TaskResult}
     */
    @Override
    @Transactional
    public @NotNull TaskResult<Log> insert(@NotNull Log param_log, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<Log> taskResult;
        @Nullable Log resultData = null;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    for (Security.RoleVo eachRole : Security.RoleVo.ADMINISTRATOR_ROLE_VO__SET) {
                        if (eachRole.name().equals(authority)) {
                            isInsufficientPermissions = true;
                            break;
                        }
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return insert(param_log);
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 删除日志记录
     *
     * @param id    [日志记录 - 编号]
     *
     * @return {@link TaskResult}
     */
    private @NotNull TaskResult<Boolean> delete(@NotNull String id) {
        @NotNull TaskResult<Boolean> taskResult;
        boolean resultData = false;
        try {
            if (! Log.Validator.LOG.id(id)) {
                //-- 非法输入: [日志记录 - 编号]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "[日志记录 - 编号]"
                        , id
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            resultData = logNonIdempotentWriteService.deleteById(Long.parseLong(id));

            if (resultData) {
                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.SUCCESS
                        , null
                        , resultData);
            } else {
                taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_BUSINESS
                        , null
                        , resultData);
            }
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

    /**
     * 删除日志记录
     *
     * @param id       [日志记录 - 编号]
     * @param operator 操作者
     *
     * @return {@link TaskResult}
     */
    @Override
    @Transactional
    public @NotNull TaskResult<Boolean> delete(@NotNull String id, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator) {
        @NotNull TaskResult<Boolean> taskResult;
        boolean resultData = false;
        try {
            if (null == operator) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("非法的身份认证凭证");
            }
            if (! Boolean.TRUE.equals(operator.isActive())) {
                //-- 非法输入: 身份认证凭证
                throw new AuthenticationCredentialsNotFoundException("无效的身份认证凭证");
            }
            if (null == operator.getAuthorities()) {
                throw new AccessDeniedException("权限不足");
            } else {
                boolean isInsufficientPermissions = false;
                for (@NotNull String authority : operator.getAuthorities()) {
                    for (Security.RoleVo eachRole : Security.RoleVo.ADMINISTRATOR_ROLE_VO__SET) {
                        if (eachRole.name().equals(authority)) {
                            isInsufficientPermissions = true;
                            break;
                        }
                    }
                }
                if (! isInsufficientPermissions) {
                    throw new AccessDeniedException("权限不足");
                }
            }

            return delete(id);
        } catch (IllegalArgumentException e) {
            //--- 非法参数异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE_INPUT_PARAMETER
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        } catch (Exception e) {
            //--- 其他异常
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);

            taskResult = TaskResult.Factory.DEFAULT.create(TaskResult.Vo.StatusVo.FAILURE
                    , null
                    , resultData);
            taskResult.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());
        }
        return taskResult;
    }

}
