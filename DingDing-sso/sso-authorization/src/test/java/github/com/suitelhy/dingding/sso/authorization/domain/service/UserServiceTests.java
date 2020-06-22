package github.com.suitelhy.dingding.sso.authorization.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Human;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;

/**
 * 用户 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 *
 * @see github.com.suitelhy.dingding.core.domain.service.UserService
 */
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private UserService service;

    @NotNull
    private User getEntityForTest() {
        return getEntityForTest(null);
    }

    @NotNull
    private User getEntityForTest(Integer seed) {
        return User.Factory.USER.create(20
                , new CalendarController().toString()
                , ip()
                , new CalendarController().toString()
                , "测试用户"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , "test123"
                , "测试数据"
                , null
                , "测试"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , Human.SexVo.MALE);
    }

    @NotNull
    private String ip() {
        return "127.0.0.0";
    }

    @Test
    public void contextLoads() {
        Assert.notNull(service, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAll() {
        final Page<User> result;
        Assert.isTrue(!(result = service.selectAll(0, 10)).isEmpty()
                , "The result is empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectCount() {
        final long result;
        Assert.isTrue((result = service.selectCount(10)) > 0
                , "The result equaled to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectUserByUserid() {
        final User result;

        // 添加测试数据
        final User newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据失败!");

        // selectUserByUserid(...)
        result = service.selectUserByUserid(newEntity.getUserid());
        Assert.isTrue((null != result && !result.isEmpty())
                , "===== The result -> null");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void insert() {
        final User newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== insert(Entity) -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void update() {
        final User result;

        // 添加测试数据
        final User newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据失败!");

        //=== update(..)
        result = newEntity;
        System.out.println(result.getIntroduction());
        result.setIntroduction("最新测试数据");

        Assert.isTrue(service.update(result)
                , "===== update - update(Entity) -> false");
        Assert.isTrue(!result.isEmpty()
                , "===== update - update(Entity) -> 无效的 Entity");

        System.out.println(result.getIntroduction());
        System.out.println(result);
    }

    @Test
    @Transactional
    public void delete() {
        final User result;

        // 添加测试数据
        final User newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据失败!");

        //=== delete(..)
        result = newEntity;
        Assert.isTrue(service.delete(result)
                , "===== delete(Entity) -> false");
        System.out.println(result);
    }

}
