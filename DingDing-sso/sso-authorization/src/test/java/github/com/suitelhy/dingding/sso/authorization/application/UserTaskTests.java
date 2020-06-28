package github.com.suitelhy.dingding.sso.authorization.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.application.task.UserTask;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.BasicUserDto;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.UserDto;
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
public class UserTaskTests {

    @Autowired
    private ObjectMapper toJSONObject;

    @Autowired
    private UserTask userTask;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityUserService securityUserService;

    @NotNull
    private SecurityUser getEntityForTest() {
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
        return new SecurityUser(newUser.dtoId(username(), password())
                , passwordEncoder
                , securityUserService);
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
        final UserDto result;

        //=== 添加测试数据
        final SecurityUser newEntity = getEntityForTest();
        final BasicUserDto newUserDto = newEntity.getUserInfo();

        Assert.notNull(newEntity
                , "===== getEntityForTest() -> null");
        Assert.isTrue(userTask.insert(newUserDto
                    , newEntity.getPassword()
                    , ip()
                    , new CalendarController().toString())
                , "===== 添加测试数据 -> false");

        //=== selectUserByUserid(..)
        Assert.notNull(result = userTask.selectUserByUserid(newUserDto.getUserId())
                , "selectUserByUserid(Sting) -> null");

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
        final SecurityUser newUser = getEntityForTest();
        final BasicUserDto newUserDto = newUser.getUserInfo();

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
        //=== 添加测试数据
        final SecurityUser newEntity = getEntityForTest();
        final BasicUserDto newUserDto = newEntity.getUserInfo();

        Assert.notNull(newEntity
                , "===== getEntityForTest() -> null");
        Assert.isTrue(userTask.insert(newUserDto
                    , password()
                    , ip()
                    , new CalendarController().toString())
                , "添加测试数据 -> false");

        //=== update
        User newUser = newUserDto.dtoId(username(), password());

        assert newUser != null;
        newUser.setAge(18);
        newUser.setIntroduction("最新_" + newUser.getIntroduction());

        Assert.isTrue(userTask.update(newUserDto
                    , password()
                    , ip()
                    , new CalendarController().toString())
                , "update - update(User user) -> false");

        System.out.println(newUserDto);
    }

    @Test
    @Transactional
    public void delete() {
        //=== 添加测试数据
        final SecurityUser newSecurityUser = getEntityForTest();
        final BasicUserDto newUserDto = newSecurityUser.getUserInfo();

        Assert.isTrue(userTask.insert(newUserDto
                    , password()
                    , ip()
                    , new CalendarController().toString())
                , "===== insert - insert(User) -> null");
        Assert.isTrue(!newUserDto.isEmpty()
                , "===== insert - insert(User) -> 无效的 User");

        //=== delete(...)
        Assert.isTrue(userTask.delete(newUserDto, password())
                , "delete - delete(User user) -> false");

        System.out.println(newUserDto);
    }

}
