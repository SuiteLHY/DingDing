package github.com.suitelhy.dingding.user.service.provider.domain.event;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserRoleReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence.SecurityUserIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityUserNonIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.user.service.api.domain.event.read.UserReadEvent;
import github.com.suitelhy.dingding.user.service.api.domain.event.write.idempotence.UserIdempotentWriteEvent;
import github.com.suitelhy.dingding.user.service.api.domain.event.write.non.idempotence.UserNonIdempotentWriteEvent;
import github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence.UserNonIdempotentWriteService;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 */
@SpringBootTest
public class UserEventTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserEvent userEvent;

    @Reference
    private SecurityUserReadService securityUserReadService;

    @Reference
    private SecurityUserIdempotentService securityUserIdempotentService;

    @Reference
    private SecurityUserNonIdempotentService securityUserNonIdempotentService;

    @Reference
    private SecurityUserRoleReadService securityUserRoleReadService;

    /**
     * 获取(测试用的)操作者信息
     *
     * @return {@link SecurityUser}
     */
    private @NotNull SecurityUser operator() {
        final SecurityUser securityUser = securityUserReadService.selectByUsername("admin");

        System.err.println("【调试用】获取(测试用的)操作者信息 => "
                .concat((null != securityUser) ? securityUser.toString() : "{}"));

        return securityUser;
    }

    /**
     * 获取(测试用的)操作者信息 -> 拥有管理权的[（安全认证）角色]
     *
     * @Description 若没有拥有管理权的[（安全认证）角色], 则返回已被标准化的[（安全认证）角色].
     *
     * @return {@link SecurityRole}
     */
    private @NotNull SecurityRole operator_role() {
        @NotNull SecurityRole result = SecurityRole.Factory.ROLE.createDefault();

        final @NotNull List<SecurityRole> operator_roles = userEvent.selectRoleOnUserByUsername(operator().getUsername());

        for (@NotNull SecurityRole eachRole : operator_roles) {
            final Security.RoleVo eachRoleVo;
            if (null != eachRole
                    && ! eachRole.isEmpty()
                    && null != (eachRoleVo = SecurityRole.changeIntoRoleVo(eachRole)))
            {
                if (eachRoleVo.isAdministrator()) {
                    result = eachRole;
                    break;
                } else if (result.isEmpty()) {
                    result = eachRole;
                }
            }
        }

        return result;
    }

    /**
     * 获取(测试用的)操作者信息 -> 拥有管理权的[（安全认证）用户 ←→ 角色]
     *
     * @Description 若没有拥有管理权的[（安全认证）用户 ←→ 角色], 则返回已被标准化的[（安全认证）用户 ←→ 角色].
     *
     * @return {@link SecurityUserRole}
     */
    private @NotNull SecurityUserRole operator_userRole() {
        return securityUserRoleReadService.selectByUsernameAndRoleCode(operator().getUsername(), operator_role().getCode());
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @return {@link Map}
     * · 数据结构:
     * {
     * "user": {@link User},
     * "userAccountOperationInfo": {@link UserAccountOperationInfo},
     * "userPersonInfo": {@link UserPersonInfo}
     * }
     *
     * @see this#getEntityForTest(Integer)
     */
    private @NotNull /*User*/Map<String, EntityModel<?>> getEntityForTest() {
        return getEntityForTest(null);
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @param seed
     *
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
                , null);

        result.put("user", newUser);
        result.put("userAccountOperationInfo", userAccountOperationInfo);
        result.put("userPersonInfo", userPersonInfo);

        return result;
    }

    private @NotNull String ip() {
        return "127.0.0.0";
    }

    @Test
    public void contextLoads() {
        Assert.notNull(userService, "获取测试单元失败");
    }

    /**
     * @see UserReadEvent#selectUserByUsername(String)
     */
    @Test
    @Transactional
    public void selectUserByUsername()
            throws BusinessAtomicException
    {
        final @NotNull User result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userReadEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();

        @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());
        Assert.isTrue(userService.insert((User) newEntity.get("user"), operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");

        // selectUserByUsername(String)
        result = userEvent.selectUserByUsername(((User) newEntity.get("user")).getUsername());
        Assert.isTrue(! result.isEmpty()
                , "===== The result -> is empty!");

        System.out.println(result);
    }

    /**
     * @see UserReadEvent#selectUserAccountOperationInfoByUsername(String)
     */
    @Test
    @Transactional
    public void selectUserAccountOperationInfoByUsername()
            throws BusinessAtomicException
    {
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
        Assert.isTrue(! result.isEmpty()
                , "===== The result -> is empty!");

        System.out.println(result);
    }

    /**
     * @see UserReadEvent#selectUserPersonInfoByUsername(String)
     */
    @Test
    @Transactional
    public void selectUserPersonInfoByUsername()
            throws BusinessAtomicException
    {
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
     * @see UserReadEvent#selectUserPersonInfoByUsername(String)
     */
    @Test
    @Transactional
    public void selectSecurityUserByUsername()
            throws BusinessAtomicException
    {
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
     * @see UserNonIdempotentWriteEvent#registerUser(User, UserAccountOperationInfo, UserPersonInfo, SecurityUser)
     */
    @Test
    @Transactional
    public void registerUser()
            throws BusinessAtomicException
    {
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
        Assert.isTrue(! userService.selectUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 基础信息]无效");
        Assert.isTrue(! userEvent.selectUserAccountOperationInfoByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 账户操作记录]无效");
        Assert.isTrue(! userEvent.selectUserPersonInfoByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 个人信息]无效");
        Assert.isTrue(! userEvent.selectSecurityUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [（安全认证）用户]无效");
        Assert.isTrue(! userEvent.selectRoleOnUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> (关联的) [用户 -> （安全认证）角色]不存在");

        System.out.println(newEntity);
    }

    /**
     * @throws BusinessAtomicException
     * @see UserIdempotentWriteEvent#updateUser(User, SecurityUser)
     */
    @Test
    @Transactional
    public void updateUser()
            throws BusinessAtomicException
    {
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

        ((UserPersonInfo) newEntity.get("userPersonInfo")).setIntroduction("最新测试数据", operator(), operator_userRole()
                , operator_role());

        Assert.isTrue(userEvent.updateUser((User) newEntity.get("user"), operator)
                , "===== update - update(Entity) -> false");

        //=== 验证数据

        Assert.isTrue(! userService.selectUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 基础信息]无效");
        Assert.isTrue(! userEvent.selectUserAccountOperationInfoByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 账户操作记录]无效");
        Assert.isTrue(! userEvent.selectUserPersonInfoByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [用户 - 个人信息]无效");
        Assert.isTrue(! userEvent.selectSecurityUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> [（安全认证）用户]无效");
        Assert.isTrue(! userEvent.selectRoleOnUserByUsername(((User) newEntity.get("user")).getUsername())
                        .isEmpty()
                , "===== registerUser(...) -> (关联的) [用户 -> （安全认证）角色]不存在");
    }

    /**
     * @see UserNonIdempotentWriteService#delete(User, SecurityUser)
     */
    @Test
    @Transactional
    public void deleteUser()
            throws BusinessAtomicException
    {
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
        Assert.isTrue(userService.selectUserByUsername(((User) newEntity.get("user")).getUsername())
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
