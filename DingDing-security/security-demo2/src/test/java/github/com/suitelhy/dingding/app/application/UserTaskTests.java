package github.com.suitelhy.dingding.app.application;

import github.com.suitelhy.dingding.app.application.task.UserTask;
import github.com.suitelhy.dingding.app.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.app.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.app.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;

@SpringBootTest
public class UserTaskTests {

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
        Assert.notNull(userTask, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAll() {
        List<UserDto> result;
        Assert.notEmpty(result = userTask.selectAll(1, 10)
                , "selectAll(int dataIndex, int pageSize) -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectUserByUserid() {
        UserDto result;
        //=== insert
        final SecurityUser newUser = getUserForTest();
        final UserDto newUserDto = UserDto.Factory.USER_DTO.create(newUser);
        Assert.notNull(newUser
                , "insert - User.Factory.USER -> create(..) -> null");
        Assert.isTrue(userTask.insert(newUserDto
                , newUser.getPassword()
                , ip()
                , new CalendarController().toString())
                , "insert - insert(User user) -> false");
        //=== select
        Assert.notNull(result = userTask.selectUserByUserid(newUserDto.id())
                , "select - selectUserByUserid(Sting) -> null");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectCount() {
        Long result;
        Assert.notNull(result = userTask.selectCount(10)
                , "selectCount(int) -> null");
        Assert.isTrue(result > 0
                , "selectCount(int) -> equals or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void insert() {
        final SecurityUser newUser = getUserForTest();
        final UserDto newUserDto = UserDto.Factory.USER_DTO.create(newUser);
        Assert.notNull(newUser
                , "User.Factory.USER -> create(..) -> null");
        Assert.isTrue(userTask.insert(newUserDto
                , newUser.getPassword()
                , ip()
                , new CalendarController().toString())
                , "insert(User user) -> false");
    }

    @Test
    @Transactional
    public void update() {
        final SecurityUser newUser = getUserForTest();
        final UserDto newUserDto = UserDto.Factory.USER_DTO.create(newUser);
        //=== insert
        Assert.notNull(newUser
                , "insert - User.Factory.USER -> create(..) -> null");
        Assert.isTrue(userTask.insert(newUserDto
                , newUser.getPassword()
                , ip()
                , new CalendarController().toString())
                , "insert - insert(User user) -> false");
        //=== update
        newUserDto.setAge("18");
        newUserDto.setIntroduction("最新_" + newUserDto.getIntroduction());
        Assert.isTrue(userTask.update(newUserDto
                , newUser.getPassword()
                , ip()
                , new CalendarController().toString())
                , "update - update(User user) -> false");
        System.out.println(newUser);
    }

    @Test
    @Transactional
    public void delete() {
        final SecurityUser newUser = getUserForTest();
        System.err.println(newUser.getUsername());
        final UserDto newUserDto = UserDto.Factory.USER_DTO.create(newUser);
        //=== insert
        Assert.isTrue(userTask.insert(newUserDto
                , newUser.getPassword()
                , ip()
                , new CalendarController().toString())
                , "===== insert - insert(User) -> null");
        Assert.isTrue(!newUserDto.isEmpty()
                , "===== insert - insert(User) -> 无效的 User");
        //=== delete
        Assert.isTrue(userTask.delete(newUserDto, newUser.getPassword())
                , "delete - delete(User user) -> false");
        System.out.println(newUser);
    }

}
