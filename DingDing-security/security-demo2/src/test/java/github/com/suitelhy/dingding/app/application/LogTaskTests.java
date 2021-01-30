package github.com.suitelhy.dingding.app.application;

import github.com.suitelhy.dingding.app.application.task.LogTask;
import github.com.suitelhy.dingding.app.application.task.UserTask;
import github.com.suitelhy.dingding.app.domain.entity.Log;
import github.com.suitelhy.dingding.app.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.app.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.app.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.app.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @NotNull
    private SecurityUser getUserForTest() {
        /*final User newUser = User.Factory.USER.update(id()
                , 20
                , new CalendarController().toString()
                , ip()
                , new CalendarController().toString()
                , "测试用户"
                , "test123"
                , "测试数据"
                , null
                , ("测试" + new CalendarController().toString().replaceAll("[-:\\s]", ""))
                , HumanVo.Sex.MALE);*/
        final UserDto newUser = userTask.selectUserByUserid(id());
        return new SecurityUser(newUser.dtoId(username(), password()));
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
        final UserDto newUserDto = UserDto.Factory.USER_DTO.create(newUser);
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
        final UserDto newUserDto = UserDto.Factory.USER_DTO.create(newUser);
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
        final UserDto newUserDto = UserDto.Factory.USER_DTO.create(newUser);
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
        final UserDto newUserDto = UserDto.Factory.USER_DTO.create(newUser);
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
