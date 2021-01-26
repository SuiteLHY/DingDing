package github.com.suitelhy.dingding.sso.authorization.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志记录 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 *
 * @see LogService
 */
@SpringBootTest
public class LogServiceTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private LogService service;

    @Autowired
    private UserService userService;

    /*@Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;*/

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private UserEvent userEvent;

    /**
     * 获取(测试用的)操作者信息
     *
     * @return {@link SecurityUser}
     */
    private @NotNull SecurityUser operator() {
        return securityUserService.selectByUsername("admin");
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @return {@link this#getUserForTest(Integer)}
     */
    private @NotNull Map<String, EntityModel> getUserForTest() {
        return getUserForTest(null);
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @param seed
     *
     * @return {@link Map}
     * · 数据结构:
     * {
     *    "user": {@link User},
     *    "userAccountOperationInfo": {@link UserAccountOperationInfo},
     *    "userPersonInfo": {@link UserPersonInfo}
     * }
     */
    private @NotNull Map<String, EntityModel> getUserForTest(Integer seed) {
        @NotNull Map<String, EntityModel> result = new HashMap<>(3);

        @NotNull User newUser = User.Factory.USER.create(
                "测试用户".concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , "test123"
        );

        @NotNull String currentTime = new CalendarController().toString();
        @NotNull UserAccountOperationInfo userAccountOperationInfo = UserAccountOperationInfo.Factory.USER.create(
                newUser.getUsername()
                , "127.0.0.1"
                , currentTime
                , currentTime);

        @NotNull UserPersonInfo userPersonInfo = UserPersonInfo.Factory.USER.create(
                newUser.getUsername()
                , "测试用户"
                , null
                , null
                , null
                , null);

        result.put("user", newUser);
        result.put("userAccountOperationInfo", userAccountOperationInfo);
        result.put("userPersonInfo", userPersonInfo);

        return result;
    }

    /*@NotNull
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
    }*/

    /**
     * 获取测试用的 {@link Log} 对象
     *
     * @param user                      {@link User}
     * @param operator                  {@link SecurityUser}
     * @param logVo                     {@link HandleType.LogVo}
     *
     * @return {@link Log}
     */
    private @NotNull Log getEntityForTest(@NotNull User user, @NotNull SecurityUser operator, @NotNull HandleType.LogVo logVo)
            throws IllegalArgumentException
    {
        if (null == user) {
            throw new IllegalArgumentException("非法参数: <param>user</param>");
        }
        if (null == operator || operator.isEmpty()) {
            throw new IllegalArgumentException("非法参数: <param>operator</param>");
        }
        if (null == logVo) {
            throw new IllegalArgumentException("非法参数: <param>logVo</param>");
        }
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());
        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无有效的账户操作记录
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无有效的账户操作记录"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return Log.Factory.User.LOG.create(null
                , null
                , logVo
                , user
                , operator
                , operator_userAccountOperationInfo);
    }

    @NotNull
    private String ip() {
        return "127.0.0.0";
    }

    @Test
    public void contextLoads() {
        Assert.notNull(service, "获取测试单元失败");
        Assert.notNull(securityUserService, "获取测试单元失败");
        Assert.notNull(userEvent, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAll()
            throws JsonProcessingException
    {
        final @NotNull Page<Log> result = service.selectAll(0, 10);

        Assert.isTrue(! result.isEmpty()
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
    public void selectCountByUsername()
            throws BusinessAtomicException
    {
        final Long result;

        System.err.println("===== "
                .concat(this.getClass().getName())
                .concat(" => ")
                .concat(Thread.currentThread().getStackTrace()[1].getMethodName())
                .concat(" ====="));

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        //===== 添加测试数据 =====//

        final @NotNull Log newEntity;

        // 添加测试数据
        final @NotNull Map<String, EntityModel> newUserInfo = getUserForTest();
        final @NotNull User newUser = (User) newUserInfo.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserInfo.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserInfo.get("userPersonInfo");

        Assert.isTrue(! userService.existUserByUsername(newUser.getUsername())
                , "===== 数据异常: 测试预期添加的用户数据" + newUser + "已存在!");

        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== insert(User, UserAccountOperationInfo, UserPersonInfo, SecurityUser) -> false!");
        Assert.isTrue(! newUser.isEmpty()
                , "===== insert(User, UserAccountOperationInfo, UserPersonInfo, SecurityUser) -> 无效的 User!");
        System.out.println("newUser: ".concat(newUser.toString()));

        newEntity = getEntityForTest(newUser, operator, HandleType.LogVo.USER__USER__ADD);
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest(...) -> 无效的 Entity!");
        Assert.isTrue(service.insert(newEntity)
                , "===== insert(...) -> false!");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(...) -> 无效的 Entity!");
        System.out.println("newEntity: ".concat(newEntity.toString()));

        //===== selectCountByUsername(...) =====//

        result = service.selectCountByUsername(newUser.getUsername(), 10);

        Assert.isTrue((null != result && result > 0)
                , "===== The result -> null or not enough data!");
        System.out.println(result);

        System.err.println("==========");
    }

    @Test
    @Transactional
    public void selectLogByUsername()
            throws BusinessAtomicException
    {
        final List<Log> result;

        System.err.println("===== "
                .concat(this.getClass().getName())
                .concat(" => ")
                .concat(Thread.currentThread().getStackTrace()[1].getMethodName())
                .concat(" ====="));

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        //===== 添加测试数据 =====//
        final @NotNull Log newEntity;

        // 添加测试数据
        final @NotNull Map<String, EntityModel> newUserInfo = getUserForTest();
        final @NotNull User newUser = (User) newUserInfo.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserInfo.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserInfo.get("userPersonInfo");

        Assert.isTrue(! userService.existUserByUsername(newUser.getUsername())
                , "===== 数据异常: 测试预期添加的用户数据" + newUser + "已存在!");

        Assert.isTrue(newUser.isEntityLegal()
                , "getUserForTest() -> 无效的 User!");
        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== insert(User, UserAccountOperationInfo, UserPersonInfo, SecurityUser) -> false!");
        Assert.isTrue(! newUser.isEmpty()
                , "===== insert(User, UserAccountOperationInfo, UserPersonInfo, SecurityUser) -> 无效的 User!");
        System.out.println("newUser: ".concat(newUser.toString()));

        newEntity = getEntityForTest(newUser, operator, HandleType.LogVo.USER__USER__ADD);
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest(...) -> 无效的 Entity!");
        Assert.isTrue(service.insert(newEntity)
                , "===== insert(...) -> false!");
        Assert.isTrue(! newEntity.isEmpty()
                , "===== insert(...) -> 无效的 Entity!");
        System.out.println("newEntity: " + newEntity);

        //=== selectLogByUsername(...)

        result = service.selectLogByUsername(newUser.getUsername(), 0, 10);

        System.err.println("【调试】selectLogByUsername(...) => " + result);
        Assert.isTrue((null != result && result.contains(newEntity))
                , "===== The result -> null or not enough data!");
        System.out.println(result);

        System.err.println("==========");
    }

    @Test
    @Transactional
    public void insert()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        final @NotNull Log newEntity;

        final @NotNull Map<String, EntityModel> newUserInfo = getUserForTest();
        final @NotNull User newUser = (User) newUserInfo.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserInfo.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserInfo.get("userPersonInfo");

        Assert.isTrue(newUser.isEntityLegal()
                , "getUserForTest() -> 无效的 User!");
        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== insert(User, UserAccountOperationInfo, UserPersonInfo, operator) -> false!");
        Assert.isTrue(! newUser.isEmpty()
                , "===== insert(User, UserAccountOperationInfo, UserPersonInfo, operator) -> 无效的 User!");
        System.out.println("newUser: ".concat(newUser.toString()));

        newEntity = getEntityForTest(newUser, operator, HandleType.LogVo.USER__USER__ADD);
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest(...) -> 无效的 Entity!");
        Assert.isTrue(service.insert(newEntity)
                , "===== insert(...) -> false!");
        Assert.isTrue(! newEntity.isEmpty()
                , "===== insert(...) -> 无效的 Entity!");
        System.out.println("newEntity: ".concat(newEntity.toString()));

        System.err.println("==========");
    }

    @Test
    @Transactional
    public void deleteById()
            throws BusinessAtomicException
    {
        final boolean result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        //===== 添加测试数据 =====//
        final @NotNull Log newEntity;

        // 添加测试数据
        final @NotNull Map<String, EntityModel> newUserInfo = getUserForTest();
        final @NotNull User newUser = (User) newUserInfo.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserInfo.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserInfo.get("userPersonInfo");

        Assert.isTrue(newUser.isEntityLegal()
                , "getUserForTest() -> 无效的 User!");
        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== insert(User, UserAccountOperationInfo, UserPersonInfo, operator) -> false!");
        Assert.isTrue(! newUser.isEmpty()
                , "===== insert(User, UserAccountOperationInfo, UserPersonInfo, operator) -> 无效的 User!");
        System.out.println("newUser: ".concat(newUser.toString()));

        newEntity = getEntityForTest(newUser, operator, HandleType.LogVo.USER__USER__ADD);
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest(...) -> 无效的 Entity!");
        Assert.isTrue(service.insert(newEntity)
                , "===== insert(...) -> false!");
        Assert.isTrue(! newEntity.isEmpty()
                , "===== insert(...) -> 无效的 Entity!");
        System.out.println("newEntity: " + newEntity);

        //=== deleteById(...)
        Assert.isTrue(result = service.deleteById(newEntity.id())
                , "deleteById(...) -> false");
        System.out.println(result);

        System.err.println("==========");
    }

}
