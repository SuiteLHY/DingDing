package github.com.suitelhy.dingding.sso.authorization.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Human;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 日志记录 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 *
 * @see github.com.suitelhy.dingding.core.domain.service.LogService
 */
@SpringBootTest
public class LogServiceTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private LogService service;

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
                , ("测试".concat(new CalendarController().toString().replaceAll("[-:\\s]", "")))
                , Human.SexVo.MALE);
    }

    @NotNull
    private Log getEntityForTest(@NotNull User user) {
        return Log.Factory.USER_LOG.create(null
                , user.getIp()
                , new CalendarController().toString()
                , HandleType.LogVo.USER_REGISTRATION
                , user.getUserid()
        );
    }

    @NotNull
    private String ip() {
        return "127.0.0.0";
    }

    @Test
    public void contextLoads() {
        Assert.notNull(service, "获取测试单元失败");
        Assert.notNull(userService, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAll()
            throws JsonProcessingException {
        final Page<Log> result = service.selectAll(0, 10);
        Assert.isTrue(!result.isEmpty()
                , "The result -> empty");
        System.out.println(toJSONString.writeValueAsString(result));
        System.out.println(result.getContent());
        System.out.println(result.getNumber());
        System.out.println(result.getTotalPages());
        System.out.println(result.getTotalElements());
    }

    @Test
    @Transactional
    public void selectCount() {
        long result = service.selectCount(10);
        Assert.isTrue(result > 0
                , "The result of selectCount(int) equaled to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectCountByUserid() {
        final Long result;

        //===== 添加测试数据 =====//
        final Log newEntity;

        final User newUser = getUserForTest();
        Assert.isTrue(newUser.isEntityLegal()
                , "getUserForTest() -> 无效的 User!");
        Assert.isTrue(userService.insert(newUser)
                , "===== insert(User) -> false!");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert(User) -> 无效的 User!");
        System.out.println("newUser: " + newUser);

        newEntity = getEntityForTest(newUser);
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest(User) -> 无效的 Entity!");
        Assert.isTrue(service.insert(newEntity)
                , "===== insert(...) -> false!");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(...) -> 无效的 Entity!");
        System.out.println("newEntity: " + newEntity);

        //===== selectCountByUserid(...) =====//
        result = service.selectCountByUserid(newUser.getUserid()
                , 10);
        Assert.isTrue((null != result && result > 0)
                , "===== The result -> null or not enough data!");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectLogByUserid() {
        final List<Log> result;

        //===== 添加测试数据 =====//
        final Log newEntity;

        final User newUser = getUserForTest();
        Assert.isTrue(newUser.isEntityLegal()
                , "getUserForTest() -> 无效的 User!");
        Assert.isTrue(userService.insert(newUser)
                , "===== insert(User) -> false!");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert(User) -> 无效的 User!");
        System.out.println("newUser: " + newUser);

        newEntity = getEntityForTest(newUser);
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest(User) -> 无效的 Entity!");
        Assert.isTrue(service.insert(newEntity)
                , "===== insert(...) -> false!");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(...) -> 无效的 Entity!");
        System.out.println("newEntity: " + newEntity);

        //=== selectLogByUserid(...)
        result = service.selectLogByUserid(newUser.getUserid()
                , 0
                , 10);
        Assert.isTrue((null != result && result.contains(newEntity))
                , "===== The result -> null or not enough data!");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void insert() {
        final Log newEntity;

        final User newUser = getUserForTest();
        Assert.isTrue(newUser.isEntityLegal()
                , "getUserForTest() -> 无效的 User!");
        Assert.isTrue(userService.insert(newUser)
                , "===== insert(User) -> false!");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert(User) -> 无效的 User!");
        System.out.println("newUser: " + newUser);

        newEntity = getEntityForTest(newUser);
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest(User) -> 无效的 Entity!");
        Assert.isTrue(service.insert(newEntity)
                , "===== insert(...) -> false!");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(...) -> 无效的 Entity!");
        System.out.println("newEntity: " + newEntity);
    }

    @Test
    @Transactional
    public void deleteById() {
        final boolean result;

        //===== 添加测试数据 =====//
        final Log newEntity;

        final User newUser = getUserForTest();
        Assert.isTrue(newUser.isEntityLegal()
                , "getUserForTest() -> 无效的 User!");
        Assert.isTrue(userService.insert(newUser)
                , "===== insert(User) -> false!");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert(User) -> 无效的 User!");
        System.out.println("newUser: " + newUser);

        newEntity = getEntityForTest(newUser);
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest(User) -> 无效的 Entity!");
        Assert.isTrue(service.insert(newEntity)
                , "===== insert(...) -> false!");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(...) -> 无效的 Entity!");
        System.out.println("newEntity: " + newEntity);

        //=== deleteById(...)
        Assert.isTrue(result = service.deleteById(newEntity.id())
                , "deleteById(...) -> false");
        System.out.println(result);
    }

}
