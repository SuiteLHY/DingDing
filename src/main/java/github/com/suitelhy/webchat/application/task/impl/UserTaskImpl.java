package github.com.suitelhy.webchat.application.task.impl;

import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.application.task.UserTask;
import github.com.suitelhy.webchat.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息相关业务的调度
 */
@Service("userTask")
// 通过 lombok 使用 Slf4j 日志框架
@Slf4j
public class UserTaskImpl implements UserTask {

    // (@Autowired 在 @Service 标识的类中可以省略)
    @Autowired
    private UserService userService;

    @Override
    public List<User> selectAll(int pageCount, int pageSize) {
        List<User> result = null;
        try {
            result = userService.selectAll(--pageCount, pageSize).getContent();
        }  catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);
            result = new ArrayList<>(0);
        } finally {
            return result;
        }
    }

    @Override
    public User selectUserByUserid(String userid) {
        User result = null;
        try {
            result = userService.selectUserByUserid(userid);
        }  catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);
        } finally {
            return result;
        }
    }

    @Override
    public Long selectCount(int pageSize) {
        Long result = null;
        try {
            result = userService.selectCount(pageSize);
        }  catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);
        } finally {
            return result;
        }
    }

    @Override
    public boolean insert(User user) {
        boolean result = false;
        try {
            result = userService.insert(user);
        }  catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);
        } finally {
            return result;
        }
    }

    @Override
    public boolean update(User user) {
        boolean result = false;
        try {
            result = userService.update(user);
        }  catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);
        } finally {
            return result;
        }
    }

    @Override
    public boolean delete(User user) {
        boolean result = false;
        try {
            result = userService.deleteAndValidate(user);
        }  catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);
        } finally {
            return result;
        }
    }

}
