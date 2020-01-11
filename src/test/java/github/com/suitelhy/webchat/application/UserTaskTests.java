package github.com.suitelhy.webchat.application;

import github.com.suitelhy.webchat.application.task.UserTask;
import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.domain.vo.HumanVo;
import github.com.suitelhy.webchat.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
public class UserTaskTests {

    @Autowired
    private UserTask userTask;

    @Test
    @Transactional
    public void contextLoads() {
        Assert.notNull(userTask, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAll() {
        List<User> result;
        Assert.notEmpty(result = userTask.selectAll(0, 10)
                , "selectAll(int dataIndex, int pageSize) -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectUserByUserid() {
        User result;
        //=== insert
        final User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.0"
                , new CalendarController().toString()
                , "测试用户"
                , "test123"
                , "测试数据"
                , null
                , HumanVo.Sex.MALE);
        Assert.notNull(newUser
                , "insert - User.Factory.USER -> create(..) -> null");
        Assert.isTrue(newUser.isLegal()
                , "insert - User.Factory.USER -> create(..) -> illegal");
        Assert.isTrue(userTask.insert(newUser)
                , "insert - insert(User user) -> false");
        //=== select
        Assert.notNull(result = userTask.selectUserByUserid(newUser.id())
                , "select - selectUserByUserid(Sting) -> null");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectCount() {
        Integer result;
        Assert.notNull(result = userTask.selectCount(10)
                , "selectCount(int) -> null");
        Assert.isTrue(result > 0
                , "selectCount(int) -> equals or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void insert() {
        final User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.0"
                , new CalendarController().toString()
                , "测试用户"
                , "test123"
                , "测试数据"
                , null
                , HumanVo.Sex.MALE);
        Assert.notNull(newUser
                , "User.Factory.USER -> create(..) -> null");
        Assert.isTrue(newUser.isLegal()
                , "User.Factory.USER -> create(..) -> illegal");
        Assert.isTrue(userTask.insert(newUser)
                , "insert(User user) -> false");
    }

    @Test
    @Transactional
    public void update() {
        //=== insert
        final User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.0"
                , new CalendarController().toString()
                , "测试用户"
                , "test123"
                , "测试数据"
                , null
                , HumanVo.Sex.MALE);
        Assert.notNull(newUser
                , "insert - User.Factory.USER -> create(..) -> null");
        Assert.isTrue(newUser.isLegal()
                , "insert - User.Factory.USER -> create(..) -> illegal");
        Assert.isTrue(userTask.insert(newUser)
                , "insert - insert(User user) -> false");
        //=== update
        newUser.setAge(18);
        newUser.setLasttime(new CalendarController().toString());
        newUser.setProfile("最新_" + newUser.getProfile());
        Assert.isTrue(userTask.update(newUser)
                , "update - update(User user) -> false");
        System.out.println(newUser);
    }

    @Test
    @Transactional
    public void delete() {
        User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.1"
                , new CalendarController().toString()
                , "测试"
                , "a12345678"
                , "测试数据"
                , null
                , HumanVo.Sex.MALE);
        Assert.isTrue(newUser.isLegal()
                , "===== User.Factory.USER.create(..) -> 无效的 User");
        //=== insert
        Assert.isTrue(userTask.insert(newUser)
                , "===== insert - insert(User) -> null");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert - insert(User) -> 无效的 User");
        //=== delete
        Assert.isTrue(userTask.delete(newUser)
                , "delete - delete(User user) -> false");
        System.out.println(newUser);
    }

}
