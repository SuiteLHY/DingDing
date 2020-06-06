package github.com.suitelhy.dingding.sso.authorization.application;

import github.com.suitelhy.dingding.core.application.task.LogTask;
import github.com.suitelhy.dingding.core.application.task.UserTask;
import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.BasicUserDto;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import github.com.suitelhy.dingding.core.infrastructure.web.SecurityUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;

@SpringBootTest
public class LogTaskTests {

    @Autowired
    private LogTask logTask;

    @Autowired
    private UserTask userTask;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @NotNull
    private SecurityUser getUserForTest() {
        final UserDto newUser = userTask.selectUserByUserid(id());
        return new SecurityUser(newUser.dtoId(username(), password()), passwordEncoder);
    }

    @NotNull
    private String ip() {
        return "127.0.0.0";
    }

    @NotNull
    public String id() {
        return "402880e56fb72000016fb72014fc0000";
    }

    @NotNull
    public String password() {
        return "test123";
    }

    @NotNull
    public String username() {
        return "测试20200118132850";
    }

    @Test
    @Transactional
    public void contextLoads() {
        Assert.notNull(logTask, "获取测试单元失败");
        Assert.notNull(userTask, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAll() {
        List<Log> result = logTask.selectAll(1, 10);
        Assert.notEmpty(result
                , "===== selectAll(int pageCount, int pageSize) -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectCount() {
        Long result = logTask.selectCount(10);
        Assert.isTrue(result > 0
                , "===== selectCount(int pageSize) -> equals or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectLogByUserid() {
        //===== userTask =====//
        final SecurityUser newUser = getUserForTest();
        final BasicUserDto newUserDto = newUser.getUserInfo();
        Assert.notNull(newUser
                , "User.Factory.USER -> create(..) -> null");
        Assert.isTrue(userTask.insert(newUserDto
                    , newUser.getPassword()
                    , ip()
                    , new CalendarController().toString())
                , "insert(User user) -> false");

        //===== logTask =====//
        //=== insert(Log log)
        Log newLog = Log.Factory.USER_LOG.create(null
                , ip()
                , new CalendarController().toString()
                , HandleType.LogVo.USER_REGISTRATION
                , newUserDto.id()
        );
        Assert.isTrue(logTask.insert(newLog)
                , "===== insert(Log log) -> false");

        //=== selectLogByUserid(String userid, int pageCount, int pageSize)
        List<Log> result = logTask.selectLogByUserid(newUserDto.id()
                , 1
                , 10);
        Assert.notEmpty(result
                , "===== selectLogByUserid(String userid, int pageCount, int pageSize) -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectCountByUserid() {
        //===== userTask =====//
        final SecurityUser newUser = getUserForTest();
        final BasicUserDto newUserDto = newUser.getUserInfo();
        Assert.notNull(newUser
                , "User.Factory.USER -> create(..) -> null");
        Assert.isTrue(userTask.insert(newUserDto
                    , newUser.getPassword()
                    , ip()
                    , new CalendarController().toString())
                , "insert(User user) -> false");

        //===== logTask =====//
        //=== insert(Log log)
        Log newLog = Log.Factory.USER_LOG.create(null
                , ip()
                , new CalendarController().toString()
                , HandleType.LogVo.USER_REGISTRATION
                , newUserDto.id()
        );
        Assert.isTrue(logTask.insert(newLog)
                , "===== insert(Log log) -> false");

        //=== selectCountByUserid(String userid, int pageSize)
        Long result = logTask.selectCountByUserid(newUserDto.id()
                , 10);
        Assert.isTrue(result > 0
                , "===== selectCountByUserid(String userid, int pageSize) -> equals or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void insert() {
        //===== userTask =====//
        final SecurityUser newUser = getUserForTest();
        final BasicUserDto newUserDto = newUser.getUserInfo();
        Assert.notNull(newUser
                , "User.Factory.USER -> create(..) -> null");
        Assert.isTrue(userTask.insert(newUserDto
                    , newUser.getPassword()
                    , ip()
                    , new CalendarController().toString())
                , "insert(User user) -> false");

        //===== logTask =====//
        Log newLog = Log.Factory.USER_LOG.create(null
                , ip()
                , new CalendarController().toString()
                , HandleType.LogVo.USER_REGISTRATION
                , newUserDto.id()
        );
        boolean result;
        Assert.isTrue(result = logTask.insert(newLog)
                , "===== insert(Log log) -> false");
        System.out.println(result);
        System.out.println(newLog);
    }

    @Test
    @Transactional
    public void delete() {
        //===== userTask =====//
        final SecurityUser newUser = getUserForTest();
        final BasicUserDto newUserDto = newUser.getUserInfo();
        Assert.notNull(newUser
                , "User.Factory.USER -> create(..) -> null");
        Assert.isTrue(userTask.insert(newUserDto
                    , newUser.getPassword()
                    , ip()
                    , new CalendarController().toString())
                , "insert(User user) -> false");

        //===== logTask =====//
        //=== insert(Log log)
        Log newLog = Log.Factory.USER_LOG.create(null
                , ip()
                , new CalendarController().toString()
                , HandleType.LogVo.USER_REGISTRATION
                , newUserDto.id()
        );
        Assert.isTrue(logTask.insert(newLog)
                , "===== insert(Log log) -> false");

        //=== delete(String id)
        boolean result;
        Assert.isTrue(result = logTask.delete(Long.toString(newLog.id()))
                , "===== delete(String id) -> false");
        System.out.println(result);
        System.out.println(newLog);
    }

}
