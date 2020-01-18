package github.com.suitelhy.webchat.domain;

import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.domain.service.UserService;
import github.com.suitelhy.webchat.infrastructure.domain.vo.HumanVo;
import github.com.suitelhy.webchat.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @NotNull
    private User getUserForTest() {
        return User.Factory.USER.create(20
                , new CalendarController().toString()
                , ip()
                , new CalendarController().toString()
                , "测试用户"
                , "test123"
                , "测试数据"
                , null
                , ("测试" + new CalendarController().toString().replaceAll("[-:\\s]", ""))
                , HumanVo.Sex.MALE);
    }

    @NotNull
    private String ip() {
        return "127.0.0.0";
    }

    @Test
    public void contextLoads() {
        Assert.notNull(userService, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAll() {
        List<User> result;
        Assert.notEmpty(result = userService.selectAll(0, 10).getContent()
                , "The result of count() equaled to or less than 0");
        System.out.println(result);
        System.out.println(result.size());
    }

    @Test
    @Transactional
    public void selectCount() {
        long result;
        Assert.isTrue((result = userService.selectCount(10)) > 0
                , "The result of selectCount(int) equaled to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectUserByUserid() {
        User result;
        List<User> userList;
        Assert.notEmpty(userList = userService.selectAll(0, 10).getContent()
                , "===== Init handle -> false, cause by not enough data");
        Assert.isTrue(null != userList.get(0) && !userList.get(0).isEmpty()
                , "===== Init handle -> false, cause by empty data");
        Assert.notNull(result = userService.selectUserByUserid(userList.get(0).id())
                , "===== selectUserByUserid() -> null");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void insert() {
        User newUser = getUserForTest();
        Assert.isTrue(newUser.isEntityLegal()
                , "User.Factory.USER.create(..) -> 无效的 User");
        Assert.isTrue(userService.insert(newUser)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert(User) -> 无效的 User");
        System.out.println(newUser);
    }

    @Test
    @Transactional
    public void update() {
        User result = getUserForTest();
        Assert.isTrue(result.isEntityLegal()
                , "===== User.Factory.USER.create(..) -> 无效的 User");
        //=== insert
        Assert.isTrue(userService.insert(result)
                , "===== insert - insert(User) -> null");
        Assert.isTrue(!result.isEmpty()
                , "===== insert - insert(User) -> 无效的 User");
        //=== update
        System.out.println(result.getProfile());
        result.setProfile("最新测试数据");
        Assert.isTrue(userService.update(result)
                , "===== update - update(User) -> false");
        Assert.isTrue(!result.isEmpty()
                , "===== update - update(User) -> 无效的 User");
        System.out.println(result.getProfile());
        System.out.println(result);
    }

    @Test
    @Transactional
    public void delete() {
        final User result = getUserForTest();
        Assert.isTrue(result.isEntityLegal()
                , "===== User.Factory.USER.create(..) -> 无效的 User");
        //=== insert
        Assert.isTrue(userService.insert(result)
                , "===== insert - insert(User) -> null");
        Assert.isTrue(!result.isEmpty()
                , "===== insert - insert(User) -> 无效的 User");
        Assert.notNull(result
                , "===== selectUserByUserid() -> null");
        //=== delete
        Assert.isTrue(userService.delete(result)
                , "===== delete - delete(User user) -> false");
        System.out.println(result);
    }

}
