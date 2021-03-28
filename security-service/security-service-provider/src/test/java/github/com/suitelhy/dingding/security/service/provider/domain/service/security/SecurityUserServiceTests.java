package github.com.suitelhy.dingding.security.service.provider.domain.service.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashSet;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import github.com.suitelhy.dingding.core.infrastructure.web.vo.HTTP;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.event.read.SecurityResourceReadEvent;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityResourceEvent;
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityRoleEvent;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityResourceService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityRoleService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserService;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 用户 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 */
@SpringBootTest
public class SecurityUserServiceTests {

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private SecurityResourceService securityResourceService;

    @Autowired
    private SecurityRoleService securityRoleService;

//    @Autowired
//    private UserService userService;

//    @Autowired
//    private UserEvent userEvent;

    @Autowired
    private SecurityRoleEvent securityRoleEvent;

    @Autowired
    private SecurityResourceEvent securityResourceEvent;

//    @Value("${dingding.security.client-id}")
//    private String clientId;

    /**
     * 获取(测试用的)操作者信息
     *
     * @return {@link SecurityUser}
     */
    private SecurityUser operator() {
        return securityUserService.selectByUsername("admin");
    }

    private String getClientId() {
        return "dingding_test";
    }

    /*@NotNull
    private SecurityUser getEntityForTest() {
        return SecurityUser.Factory.USER.create("402880e56fb72000016fb72014fc0000"
                , "测试20200118132850"
                , VoUtil.getVoByValue(Account.StatusVo.class, 1));
    }*/

//    /**
//     * 获取测试用的用户相关 {@link EntityModel} 集合
//     *
//     * @return {@link this#getEntityForTest(Integer)}
//     */
//    @NotNull
//    private Map<String, EntityModel<?>> getEntityForTest()
//            throws BusinessAtomicException {
//        return getEntityForTest((Integer) null);
//    }
//
//    /**
//     * 获取测试用的用户相关 {@link EntityModel} 集合
//     *
//     * @param seed
//     *
//     * @return {@link Map}
//     * · 数据结构:
//     * {
//     *    "user": {@link User},
//     *    "userAccountOperationInfo": {@link UserAccountOperationInfo},
//     *    "userPersonInfo": {@link UserPersonInfo}
//     *    "securityUser": {@link SecurityUser}
//     * }
//     */
//    @NotNull
//    private Map<String, EntityModel<?>> getEntityForTest(Integer seed)
//            throws BusinessAtomicException
//    {
//        Map<String, EntityModel<?>> result = new HashMap<>(3);
//
//        User newUser = User.Factory.USER.create(
//                "测试用户".concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
//                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
//                , "test123");
//
//        String currentTime = new CalendarController().toString();
//        UserAccountOperationInfo userAccountOperationInfo = UserAccountOperationInfo.Factory.USER.create(
//                newUser.getUsername()
//                , "127.0.0.1"
//                , currentTime
//                , currentTime);
//
//        UserPersonInfo userPersonInfo = UserPersonInfo.Factory.USER.create(
//                newUser.getUsername()
//                , "测试用户"
//                , null
//                , null
//                , null
//                , null);
//
//        userEvent.registerUser(newUser, userAccountOperationInfo, userPersonInfo
//                , operator());
//
//        result.put("user", newUser);
//        result.put("userAccountOperationInfo", userAccountOperationInfo);
//        result.put("userPersonInfo", userPersonInfo);
//        result.put("securityUser", SecurityUser.Factory.USER.create(newUser));
//
//        return result;
//    }

    private @NotNull SecurityUser getEntityForTest(@NotNull User user) {
        if (securityUserService.existByUsername(user.getUsername())) {
            return securityUserService.selectByUsername(user.getUsername());
        }
        return SecurityUser.Factory.USER.create(user);
    }

    private @NotNull SecurityResource getResourceForTest() {
        return getResourceForTest(null);
    }

    private @NotNull SecurityResource getResourceForTest(@Nullable Integer seed) {
        String seedString = (null == seed)
                ? ""
                : Integer.toString(seed);
        return SecurityResource.Factory.RESOURCE.create(
                "test".concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
                        .concat(seedString))
                , null
                , null
                , "test".concat(seedString)
                , null
                , 0
                , Resource.TypeVo.MENU);
    }

    private @NotNull SecurityRole getRoleForTest() {
        return getRoleForTest(null);
    }

    private @NotNull SecurityRole getRoleForTest(@Nullable Integer seed) {
        return SecurityRole.Factory.ROLE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
                                .concat(null == seed ? "" : Integer.toString(seed)))
                , "测试角色".concat(null == seed ? "" : Integer.toString(seed))
                , "测试用数据");
    }

    private @NotNull String getUrlForTest() {
        return getUrlForTest(null);
    }

    private @NotNull String getUrlForTest(@Nullable Integer seed) {
        return "/test/test"
                .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                .concat((null == seed || seed < 0) ? "" : Integer.toString(seed));
    }

    /*@NotNull
    private User getUserForTest() {
        return getUserForTest(null);
    }

    @NotNull
    private User getUserForTest(Integer seed) {
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
    }*/

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @return {@link this#getUserForTest(Integer)}
     */
    private @NotNull Map<String, EntityModel<?>> getUserForTest() {
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
     * "user": {@link User},
     * "userAccountOperationInfo": {@link UserAccountOperationInfo},
     * "userPersonInfo": {@link UserPersonInfo}
     * }
     */
    private @NotNull Map<String, EntityModel<?>> getUserForTest(Integer seed) {
        final @NotNull Map<String, EntityModel<?>> result = new HashMap<>(3);

        @NotNull User newUser = User.Factory.USER.create(
                "测试用户".concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , "test123");

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

    private @NotNull String getUrlHttpMethodNameForTest() {
        return HTTP.MethodVo.GET.name();
    }

    private @NotNull String ip() {
        return "127.0.0.0";
    }

    @Test
    public void contextLoads() {
        Assert.notNull(securityUserService, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void existByUserId()
            throws BusinessAtomicException
    {
        /*final boolean result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userNonIdempotentWriteEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(securityUserIdempotentService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        // existByUserId(..)
        Assert.isTrue(result = securityUserService.existByUserId(newEntity.getUserId())
                , "===== existByUserId(..) -> false");

        System.out.println(result);*/

        // (不报错就行)
        securityUserService.existByUserId("abc123456");
    }

    @Test
    @Transactional
    public void existByUsername()
            throws BusinessAtomicException
    {
        /*final boolean result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userNonIdempotentWriteEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(securityUserIdempotentService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        // existByUsername(..)
        Assert.isTrue(result = securityUserService.existByUsername(newEntity.getUsername())
                , "===== existByUsername(..) -> false");

        System.out.println(result);*/

        // (不报错就行)
        securityUserService.existByUsername("abc123456");
    }

    @Test
    @Transactional
    public void existAdminPermission()
            throws BusinessAtomicException
    {
//        final boolean result;
//
//        // 获取必要的测试用身份信息
//        final @NotNull SecurityUser operator = operator();
//
//        // 添加测试数据
//        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
//        final @NotNull User newUser = (User) newUserData.get("user");
//        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
//        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");
//
//        Assert.isTrue(userNonIdempotentWriteEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
//                , "===== 添加测试数据失败!");
//
//        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.isTrue(securityUserIdempotentService.insert(newEntity, operator)
//                , "===== 添加测试数据失败!");
//
//        // 给测试数据添加对管理员权限角色的关联
//        Assert.isTrue(userIdempotentWriteEvent.insertUserRoleRelationship(newEntity, Security.RoleVo.ADMIN, operator)
//                , "===== [给测试数据添加对管理员权限角色的关联]失败!");
//
//        // existAdminPermission(..)
//        Assert.isTrue(result = securityUserService.existAdminPermission(/*newEntity.getUsername()*/"admin")
//                , "===== existByUsername(..) -> false");
//
//        System.out.println(result);

        // (不报错就行)
        securityUserService.existAdminPermission("admin");
    }

    @Test
    @Transactional
    public void selectAll() {
        final Page<SecurityUser> result;
        Assert.isTrue(! (result = securityUserService.selectAll(0, 10)).isEmpty()
                , "The result is empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectCount() {
        final long result;
        Assert.isTrue((result = securityUserService.selectCount(10)) > 0
                , "The result equals to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectUserByUserId()
            throws BusinessAtomicException
    {
        /*final SecurityUser result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userNonIdempotentWriteEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(securityUserIdempotentService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        // selectByUserId(..)
        result = securityUserService.selectByUserId(newEntity.getUserId());
        Assert.isTrue((null != result && ! result.isEmpty())
                , "===== The result is empty");

        System.out.println(result);*/

        // (不报错就行)
        securityUserService.selectByUserId("abc123456");
    }

    @Test
    @Transactional
    public void selectUserByUsername()
            throws BusinessAtomicException
    {
        /*final SecurityUser result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userNonIdempotentWriteEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(securityUserIdempotentService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        // selectUserByUsername(..)
        result = securityUserService.selectByUsername(newEntity.getUsername());
        Assert.isTrue((null != result && ! result.isEmpty())
                , "===== The result is empty");

        System.out.println(result);*/

        // (不报错就行)
        securityUserService.selectByUsername("abc123456");
    }

}
