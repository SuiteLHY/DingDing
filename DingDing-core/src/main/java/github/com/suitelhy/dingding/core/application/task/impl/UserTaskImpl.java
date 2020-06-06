package github.com.suitelhy.dingding.core.application.task.impl;

import github.com.suitelhy.dingding.core.application.task.UserTask;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.BasicUserDto;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息相关业务的调度
 */
@Service("userTask")
// 通过 lombok 使用 Slf4j 日志框架
@Slf4j
public class UserTaskImpl
        implements UserTask {

    @Autowired
    private PasswordEncoder passwordEncoder;

    // (@Autowired 在 @Service 标识的类中可以省略)
    @Autowired
    private UserService userService;

    @Override
    public List<UserDto> selectAll(int pageCount, int pageSize) {
        List<UserDto> result = null;
        try {
            List<User> userList = userService.selectAll(--pageCount, pageSize).getContent();
            result = new ArrayList<>(userList.size());
            for (User each : userList) {
                /*result.add(UserDto.Factory.USER_DTO.create(new SecurityUser(each, passwordEncoder)));*/
                result.add(UserDto.Factory.USER_DTO.create(each));
            }
        } catch (Exception e) {
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
    public UserDto selectUserByUserid(String userid) {
        UserDto result = null;
        try {
            /*result = UserDto.Factory.USER_DTO.create(
                    new SecurityUser(userService.selectUserByUserid(userid), passwordEncoder));*/
            result = UserDto.Factory.USER_DTO.create(userService.selectUserByUserid(userid));
        } catch (Exception e) {
            log.error("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);
        } finally {
            return result;
        }
    }

    /**
     * 查询指定的用户
     *
     * @param username
     * @return
     */
    @Override
    public UserDto selectUserByUsername(String username) {
        UserDto result = null;
        try {
            /*result = UserDto.Factory.USER_DTO.create(
                    new SecurityUser(userService.selectUserByUsername(username), passwordEncoder));*/
            result = UserDto.Factory.USER_DTO.create(userService.selectUserByUsername(username));
        } catch (Exception e) {
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
        } catch (Exception e) {
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
    public boolean insert(@NotNull /*UserDto*/BasicUserDto userDto
            , @NotNull String password
            , @NotNull String ip
            , @NotNull String lasttime) {
        boolean result = false;
        try {
            result = userService.insert(
                    userDto.dtoId(userDto.getUsername(), password));
        } catch (Exception e) {
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
    public boolean update(@NotNull BasicUserDto userDto
            , @NotNull String password
            , @NotNull String ip
            , @NotNull String lasttime) {
        boolean result = false;
        try {
            User user = userDto.dtoId(userDto.getUsername(), password);
            if (null == user) {
                throw new IllegalArgumentException("越权操作");
            }
            if (user.isEmpty()) {
                throw new IllegalArgumentException("非法参数");
            }
            result = userService.update(user);
        } catch (IllegalArgumentException e) {
            log.warn("<class>{}</class> - <method>{}</method> <- 第{}行"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()
                    , e);
        } catch (Exception e) {
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
    public boolean delete(@NotNull BasicUserDto userDto, @NotNull String password) {
        boolean result = false;
        try {
            result = userService.deleteAndValidate(userDto.dtoId(userDto.getUsername()
                    , password));
        } catch (Exception e) {
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
