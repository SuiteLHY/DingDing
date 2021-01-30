package github.com.suitelhy.dingding.sso.authorization.domain.event;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
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
import java.util.Map;

/**
 * 用户 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 * @see UserService
 */
@SpringBootTest
public class UserEventTests {

    @Autowired
    private UserService service;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private UserEvent userEvent;

    /**
     * 获取(测试用的)操作者信息
     *
     * @return {@link SecurityUser}
     */
    private @NotNull
    SecurityUser operator() {
        final SecurityUser securityUser = securityUserService.selectByUsername("admin");

        System.err.println("【调试用】获取(测试用的)操作者信息 => "
                .concat((null != securityUser) ? securityUser.toString() : "{}"));

        return securityUser;
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @return {@link this#getEntityForTest(Integer)}
     */
    private @NotNull /*User*/Map<String, EntityModel<?>> getEntityForTest() {
        return getEntityForTest(null);
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @param seed
     * @return {@link Map}
     * · 数据结构:
     * {
     * "user": {@link User},
     * "userAccountOperationInfo": {@link UserAccountOperationInfo},
     * "userPersonInfo": {@link UserPersonInfo}
     * }
     */
    private @NotNull /*User*/Map<String, EntityModel<?>> getEntityForTest(Integer seed) {
        /*return User.Factory.USER.create(20
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
                , Human.SexVo.MALE);*/
        @NotNull Map<String, EntityModel<?>> result = new HashMap<>(3);

        @NotNull User newUser = User.Factory.USER.create(
                "测试用户".concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , "test123");

        final @NotNull String currentTime = new CalendarController().toString();
        @NotNull UserAccountOperationInfo userAccountOperationInfo = UserAccountOperationInfo.Factory.USER.create(
                newUser.getUsername()
                , ip()
                , currentTime
                , currentTime);

        @NotNull UserPersonInfo userPersonInfo = UserPersonInfo.Factory.USER.create(
                newUser.getUsername()
                , "测试用户"
                , null
                , null
                , null
                , null
        );

        result.put("user", newUser);
        result.put("userAccountOperationInfo", userAccountOperationInfo);
        result.put("userPersonInfo", userPersonInfo);

        return result;
    }

    private @NotNull
    String ip() {
        return "127.0.0.0";
    }

    @Test
    public void contextLoads() {
        Assert.notNull(service, "获取测试单元失败");
    }

    /**
     * @see UserEvent#selectUserByUsername(String)
     */
    @Test
    @Transactional
    public void selectUserByUsername()
            throws BusinessAtomicException {
        final @NotNull User result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();

        Assert.isTrue(service.insert((User) newEntity.get("user"), operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据失败!");

        // selectUserByUsername(String)
        result = userEvent.selectUserByUsername(((User) newEntity.get("user")).getUsername());
        Assert.isTrue(!result.isEmpty()
                , "===== The result -> is empty!");

        System.out.println(result);
    }

    /**
     * @see UserEvent#selectUserAccountOperationInfoByUsername(String)
     */
    @Test
    @Transactional
    public void selectUserAccountOperationInfoByUsername()
            throws BusinessAtomicException {
        final @NotNull UserAccountOperationInfo result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();

        Assert.isTrue(userEvent.registerUser((User) newEntity.get("user")
                , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
                , (UserPersonInfo) newEntity.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据失败!");

        // selectUserAccountOperationInfoByUsername(String)
        result = userEvent.selectUserAccountOperationInfoByUsername(((User) newEntity.get("user")).getUsername());
        Assert.isTrue(!result.isEmpty()
                , "===== The result -> is empty!");

        System.out.println(result);
    }

    /**
     * @see UserEvent#selectUserPersonInfoByUsername(String)
     */
    @Test
    @Transactional
    public void selectUserPersonInfoByUsername()
            throws BusinessAtomicException {
        final @NotNull UserPersonInfo result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();

        Assert.isTrue(userEvent.registerUser((User) newEntity.get("user")
                , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
                , (UserPersonInfo) newEntity.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据失败!");

        // selectUserPersonInfoByUsername(String)
        result = userEvent.selectUserPersonInfoByUsername(((User) newEntity.get("user")).getUsername());
        Assert.isTrue(!result.isEmpty()
                , "===== The result -> is empty!");

        System.out.println(result);
    }

    /**
     * @see UserEvent#selectUserPersonInfoByUsername(String)
     */
    @Test
    @Transactional
    public void selectSecurityUserByUsername()
            throws BusinessAtomicException {
        final @NotNull SecurityUser result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();

        Assert.isTrue(userEvent.registerUser((User) newEntity.get("user")
                , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
                , (UserPersonInfo) newEntity.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据失败!");

        // selectUserPersonInfoByUsername(String)
        result = userEvent.selectSecurityUserByUsername(((User) newEntity.get("user")).getUsername());
        Assert.isTrue(!result.isEmpty()
                , "===== The result -> is empty!");

        System.out.println(result);
    }

    /**
     * @throws BusinessAtomicException
     * @see UserEvent#registerUser(User, UserAccountOperationInfo, UserPersonInfo, SecurityUser)
     */
    @Test
    @Transactional
    public void registerUser()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();

        // registerUser(...)
        Assert.isTrue(userEvent.registerUser((User) newEntity.get("user")
                , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
                , (UserPersonInfo) newEntity.get("userPersonInfo")
                , operator)
                , "===== registerUser(...) -> false");

        //=== 验证数据
        Assert.isTrue(!service.selectUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 基础信息]无效");
        Assert.isTrue(!userEvent.selectUserAccountOperationInfoByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 账户操作记录]无效");
        Assert.isTrue(!userEvent.selectUserPersonInfoByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 个人信息]无效");
        Assert.isTrue(!userEvent.selectSecurityUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [（安全认证）用户]无效");
        Assert.isTrue(!userEvent.selectRoleOnUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> (关联的) [用户 -> （安全认证）角色]不存在");

        System.out.println(newEntity);
    }

    /**
     * @throws BusinessAtomicException
     * @see UserEvent#updateUser(User, SecurityUser)
     */
    @Test
    @Transactional
    public void updateUser()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();

        Assert.isTrue(userEvent.registerUser((User) newEntity.get("user")
                , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
                , (UserPersonInfo) newEntity.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据失败!");

        //=== update(..)

        System.out.println(((UserPersonInfo) newEntity.get("userPersonInfo")).getIntroduction());

        ((UserPersonInfo) newEntity.get("userPersonInfo")).setIntroduction("最新测试数据");

        Assert.isTrue(userEvent.updateUser((User) newEntity.get("user"), operator)
                , "===== update - update(Entity) -> false");

        //=== 验证数据

        Assert.isTrue(!service.selectUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 基础信息]无效");
        Assert.isTrue(!userEvent.selectUserAccountOperationInfoByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 账户操作记录]无效");
        Assert.isTrue(!userEvent.selectUserPersonInfoByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 个人信息]无效");
        Assert.isTrue(!userEvent.selectSecurityUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [（安全认证）用户]无效");
        Assert.isTrue(!userEvent.selectRoleOnUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> (关联的) [用户 -> （安全认证）角色]不存在");
    }

    /**
     * @see UserService#delete(User, SecurityUser, UserAccountOperationInfo)
     */
    @Test
    @Transactional
    public void deleteUser()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();

        Assert.isTrue(((User) newEntity.get("user")).isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(userEvent.registerUser((User) newEntity.get("user")
                , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
                , (UserPersonInfo) newEntity.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据失败!");

        //=== deleteUser(..)
        Assert.isTrue(userEvent.deleteUser((User) newEntity.get("user"), operator)
                , "===== delete(Entity) -> false");

        //=== 验证数据
        Assert.isTrue(service.selectUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 基础信息] -> 删除失败!");
        Assert.isTrue(userEvent.selectUserAccountOperationInfoByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 账户操作记录] -> 删除失败!");
        Assert.isTrue(userEvent.selectUserPersonInfoByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 个人信息] -> 删除失败!");
        Assert.isTrue(userEvent.selectSecurityUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [（安全认证）用户] -> 删除失败!");
        Assert.isTrue(userEvent.selectRoleOnUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> (关联的) [用户 -> （安全认证）角色] -> 删除失败!");

        System.out.println(newEntity.get("user"));
    }

}
