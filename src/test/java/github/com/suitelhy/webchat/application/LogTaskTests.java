package github.com.suitelhy.webchat.application;

import github.com.suitelhy.webchat.application.task.LogTask;
import github.com.suitelhy.webchat.application.task.UserTask;
import github.com.suitelhy.webchat.domain.entity.Log;
import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.domain.vo.HandleTypeVo;
import github.com.suitelhy.webchat.domain.vo.HumanVo;
import github.com.suitelhy.webchat.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
public class LogTaskTests {

    @Autowired
    private LogTask logTask;

    @Autowired
    private UserTask userTask;

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
        final User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.0"
                , new CalendarController().toString()
                , "测试用户"
                , "test123"
                , "测试数据"
                , null
                , ("测试" + new CalendarController())
                , HumanVo.Sex.MALE);
        Assert.notNull(newUser
                , "User.Factory.USER -> create(..) -> null");
        Assert.isTrue(newUser.isLegal()
                , "User.Factory.USER -> create(..) -> illegal");
        Assert.isTrue(userTask.insert(newUser)
                , "insert(User user) -> false");

        //===== logTask =====//
        //=== insert(Log log)
        Log newLog = Log.Factory.USER_LOG.create(null
                , newUser.getIp()
                , new CalendarController().toString()
                , HandleTypeVo.Log.USER_REGISTRATION
                , newUser.getUserid()
        );
        Assert.isTrue(logTask.insert(newLog)
                , "===== insert(Log log) -> false");

        //=== selectLogByUserid(String userid, int pageCount, int pageSize)
        List<Log> result = logTask.selectLogByUserid(newUser.getUserid()
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
        final User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.0"
                , new CalendarController().toString()
                , "测试用户"
                , "test123"
                , "测试数据"
                , null
                , ("测试" + new CalendarController())
                , HumanVo.Sex.MALE);
        Assert.notNull(newUser
                , "User.Factory.USER -> create(..) -> null");
        Assert.isTrue(newUser.isLegal()
                , "User.Factory.USER -> create(..) -> illegal");
        Assert.isTrue(userTask.insert(newUser)
                , "insert(User user) -> false");

        //===== logTask =====//
        //=== insert(Log log)
        Log newLog = Log.Factory.USER_LOG.create(null
                , newUser.getIp()
                , new CalendarController().toString()
                , HandleTypeVo.Log.USER_REGISTRATION
                , newUser.getUserid()
        );
        Assert.isTrue(logTask.insert(newLog)
                , "===== insert(Log log) -> false");

        //=== selectCountByUserid(String userid, int pageSize)
        Long result = logTask.selectCountByUserid(newUser.getUserid()
                , 10);
        Assert.isTrue(result > 0
                , "===== selectCountByUserid(String userid, int pageSize) -> equals or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void insert() {
        //===== userTask =====//
        final User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.0"
                , new CalendarController().toString()
                , "测试用户"
                , "test123"
                , "测试数据"
                , null
                , ("测试" + new CalendarController())
                , HumanVo.Sex.MALE);
        Assert.notNull(newUser
                , "User.Factory.USER -> create(..) -> null");
        Assert.isTrue(newUser.isLegal()
                , "User.Factory.USER -> create(..) -> illegal");
        Assert.isTrue(userTask.insert(newUser)
                , "insert(User user) -> false");

        //===== logTask =====//
        Log newLog = Log.Factory.USER_LOG.create(null
                , newUser.getIp()
                , new CalendarController().toString()
                , HandleTypeVo.Log.USER_REGISTRATION
                , newUser.getUserid()
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
        final User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.0"
                , new CalendarController().toString()
                , "测试用户"
                , "test123"
                , "测试数据"
                , null
                , ("测试" + new CalendarController())
                , HumanVo.Sex.MALE);
        Assert.notNull(newUser
                , "User.Factory.USER -> create(..) -> null");
        Assert.isTrue(newUser.isLegal()
                , "User.Factory.USER -> create(..) -> illegal");
        Assert.isTrue(userTask.insert(newUser)
                , "insert(User user) -> false");

        //===== logTask =====//
        //=== insert(Log log)
        Log newLog = Log.Factory.USER_LOG.create(null
                , newUser.getIp()
                , new CalendarController().toString()
                , HandleTypeVo.Log.USER_REGISTRATION
                , newUser.getUserid()
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
