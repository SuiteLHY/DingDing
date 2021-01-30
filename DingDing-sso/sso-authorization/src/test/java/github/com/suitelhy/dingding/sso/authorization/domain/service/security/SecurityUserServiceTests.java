package github.com.suitelhy.dingding.sso.authorization.domain.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityResourceEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserRoleService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashSet;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.core.infrastructure.web.vo.HTTP;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
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
 * @see SecurityUserService
 */
@SpringBootTest
public class SecurityUserServiceTests {

    /*@Autowired
    private ObjectMapper toJSONString;*/

    @Autowired
    private SecurityUserService service;

    @Autowired
    private SecurityResourceService resourceService;

    @Autowired
    private SecurityRoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUserService securityUserService;

//    @Autowired
//    private SecurityUserRoleService securityUserRoleService;

    @Autowired
    private UserEvent userEvent;

    @Autowired
    private SecurityRoleEvent securityRoleEvent;

    @Autowired
    private SecurityResourceEvent securityResourceEvent;

    @Value("${dingding.security.client-id}")
    private String clientId;

    /**
     * 获取(测试用的)操作者信息
     *
     * @return {@link SecurityUser}
     */
    private SecurityUser operator() {
        return securityUserService.selectByUsername("admin");
    }

    private String getClientId() {
        return this.clientId;
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

    @NotNull
    private SecurityUser getEntityForTest(@NotNull User user) {
        if (securityUserService.existByUsername(user.getUsername())) {
            return securityUserService.selectByUsername(user.getUsername());
        }
        return SecurityUser.Factory.USER.create(user);
    }

    @NotNull
    private SecurityResource getResourceForTest() {
        return getResourceForTest(null);
    }

    @NotNull
    private SecurityResource getResourceForTest(@Nullable Integer seed) {
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

    @NotNull
    private SecurityRole getRoleForTest() {
        return getRoleForTest(null);
    }

    @NotNull
    private SecurityRole getRoleForTest(@Nullable Integer seed) {
        return SecurityRole.Factory.ROLE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
                                .concat(null == seed ? "" : Integer.toString(seed)))
                , "测试角色".concat(null == seed ? "" : Integer.toString(seed))
                , "测试用数据");
    }

    @NotNull
    private String getUrlForTest() {
        return getUrlForTest(null);
    }

    @NotNull
    private String getUrlForTest(@Nullable Integer seed) {
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
    @NotNull
    private Map<String, EntityModel<?>> getUserForTest() {
        return getUserForTest(null);
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
    private @NotNull
    Map<String, EntityModel<?>> getUserForTest(Integer seed) {
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

    @NotNull
    private String getUrlHttpMethodNameForTest() {
        return HTTP.MethodVo.GET.name();
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
    public void existByUserId()
            throws BusinessAtomicException {
        final boolean result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据失败!");

        // existByUserId(..)
        Assert.isTrue(result = service.existByUserId(newEntity.getUserId())
                , "===== existByUserId(..) -> false");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void existByUsername()
            throws BusinessAtomicException {
        final boolean result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据失败!");

        // existByUsername(..)
        Assert.isTrue(result = service.existByUsername(newEntity.getUsername())
                , "===== existByUsername(..) -> false");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void existAdminPermission()
            throws BusinessAtomicException {
        final boolean result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据失败!");

        // 给测试数据添加对管理员权限角色的关联
        Assert.isTrue(userEvent.insertUserRoleRelationship(newEntity, Security.RoleVo.ADMIN, operator)
                , "===== [给测试数据添加对管理员权限角色的关联]失败!");

        // existAdminPermission(..)
        Assert.isTrue(result = service.existAdminPermission(/*newEntity.getUsername()*/"admin")
                , "===== existByUsername(..) -> false");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void existRoleByUsername()
            throws BusinessAtomicException {
        final boolean result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据失败!");

        // existByUsername(..)
        Assert.isTrue(result = securityRoleEvent.existRoleOnUserByUsername(newEntity.getUsername())
                , "===== existByUsername(..) -> false");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectAll() {
        final Page<SecurityUser> result;
        Assert.isTrue(!(result = service.selectAll(0, 10)).isEmpty()
                , "The result is empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectCount() {
        final long result;
        Assert.isTrue((result = service.selectCount(10)) > 0
                , "The result equals to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectUserByUserId()
            throws BusinessAtomicException {
        final SecurityUser result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据失败!");

        // selectByUserId(..)
        result = service.selectByUserId(newEntity.getUserId());
        Assert.isTrue((null != result && !result.isEmpty())
                , "===== The result is empty");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectUserByUsername()
            throws BusinessAtomicException {
        final SecurityUser result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据失败!");

        // selectUserByUsername(..)
        result = service.selectByUsername(newEntity.getUsername());
        Assert.isTrue((null != result && !result.isEmpty())
                , "===== The result is empty");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectRoleOnUserByUsername()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        //===== 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull Map<String, EntityModel<?>> newUserData1 = getUserForTest(1);
        @NotNull Map<String, EntityModel<?>> newUserData2 = getUserForTest(1);

        Assert.isTrue(userEvent.registerUser((User) newUserData.get("user")
                , (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(userEvent.registerUser((User) newUserData1.get("user")
                , (UserAccountOperationInfo) newUserData1.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData1.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(userEvent.registerUser((User) newUserData2.get("user")
                , (UserAccountOperationInfo) newUserData2.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData2.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        if (((User) newUserData2.get("user")).isEmpty()
                && ((User) newUserData2.get("user")).equals((User) newUserData1.get("user"))) {
            newUserData2.put("user", newUserData1.get("user"));
        }

        final @NotNull SecurityUser newEntity = getEntityForTest((User) newUserData.get("user"));
        final @NotNull SecurityUser newEntity1 = getEntityForTest((User) newUserData1.get("user"));
        final @NotNull SecurityUser newEntity2 = getEntityForTest((User) newUserData2.get("user"));

        Assert.isTrue(service.insert(newEntity, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");
        Assert.isTrue(service.insert(newEntity1, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");
        Assert.isTrue(service.insert(newEntity2, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");

        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");
        Assert.isTrue(roleService.insert(newRole1, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");
        Assert.isTrue(roleService.insert(newRole2, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");

        final @NotNull Set<SecurityUser> users = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            users.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            users.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            users.add(newEntity2);
        }

        final @NotNull Set<SecurityRole> roles = new HashSet<>(2);
        if (!newRole.isEmpty()) {
            roles.add(newRole);
        }
        if (!newRole1.isEmpty()) {
            roles.add(newRole1);
        }
        if (!newRole2.isEmpty()) {
            roles.add(newRole2);
        }

        Assert.isTrue(userEvent.insertUserRoleRelationship(newEntity, newRole, operator)
                , "===== insertUserRoleRelationship(...) -> false");
        for (@NotNull SecurityUser eachUser : users) {
            Assert.isTrue(userEvent.insertUserRoleRelationship(eachUser, newRole, operator)
                    , "===== insertUserRoleRelationship(...) -> false");
        }
        Assert.isTrue(userEvent.insertUserRoleRelationship(newEntity, roles, operator)
                , "===== insertUserRoleRelationship(...) -> false");
        for (@NotNull SecurityUser eachUser : users) {
            for (@NotNull SecurityRole eachRole : roles) {
                Assert.isTrue(userEvent.insertUserRoleRelationship(eachUser, eachRole, operator)
                        , "===== insertUserRoleRelationship(...) -> false");
            }
        }

        //===== selectRoleOnUserByUsername(..)
        int existRoleNum = 0;

        for (@NotNull SecurityUser eachUser : users) {
            final @NotNull List<SecurityRole> eachUser_roles = userEvent.selectRoleOnUserByUsername(eachUser.getUsername());

            Assert.isTrue(!eachUser_roles.isEmpty()
                            && !(eachUser_roles.size() == 1 && Objects.requireNonNull(eachUser_roles.get(0)).equals(Security.RoleVo.USER))
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += eachUser_roles.size();
        }

        Assert.isTrue(existRoleNum == 2 * (2 + 1)
                        || existRoleNum == 2 * (3 + 1)
                        || existRoleNum == 3 * (2 + 1)
                        || existRoleNum == 3 * (3 + 1)
                , String.format("===== selectRoleOnUserByUsername(..) -> %s <- 操作失败, 非预期结果!", existRoleNum));
    }

    @Test
    @Transactional
    public void selectResourceByUsername()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        //===== 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull Map<String, EntityModel<?>> newUserData1 = getUserForTest(1);
        @NotNull Map<String, EntityModel<?>> newUserData2 = getUserForTest(1);

        Assert.isTrue(userEvent.registerUser((User) newUserData.get("user")
                , (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userEvent.registerUser((User) newUserData1.get("user")
                , (UserAccountOperationInfo) newUserData1.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData1.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userEvent.registerUser((User) newUserData2.get("user")
                , (UserAccountOperationInfo) newUserData2.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData2.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        /*if (newUserData2.isEmpty() && newUserData2.equals(newUserData1)) {
            newUserData2 = newUserData1;
        }*/
        if (((User) newUserData2.get("user")).isEmpty()
                && ((User) newUserData2.get("user")).equals((User) newUserData1.get("user"))) {
            newUserData2.put("user", newUserData1.get("user"));
        }

        final @NotNull SecurityUser newEntity = getEntityForTest((User) newUserData.get("user"));
        final @NotNull SecurityUser newEntity1 = getEntityForTest((User) newUserData1.get("user"));
        final @NotNull SecurityUser newEntity2 = getEntityForTest((User) newUserData2.get("user"));

        Assert.isTrue(service.insert(newEntity, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");
        Assert.isTrue(service.insert(newEntity1, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");
        Assert.isTrue(service.insert(newEntity2, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");

        final int user_baseRoles_number = userEvent.selectRoleOnUserByUsername(newEntity.getUsername()).size();

        // (关联) 角色
        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");
        Assert.isTrue(roleService.insert(newRole1, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");
        Assert.isTrue(roleService.insert(newRole2, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");

        // (关联) 角色 -> (关联) 资源
        final @NotNull SecurityResource newResource = getResourceForTest();
        final @NotNull SecurityResource newResource1 = getResourceForTest(1);
        final @NotNull SecurityResource newResource2 = getResourceForTest(1);

        Assert.isTrue(securityResourceEvent.insertResource(newResource, operator)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(securityResourceEvent.insertResource(newResource1, operator)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(securityResourceEvent.insertResource(newResource2, operator)
                , "===== 添加测试数据 -> false");

        final @NotNull Set<SecurityUser> users = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            users.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            users.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            users.add(newEntity2);
        }
        System.out.println("===> users:\n".concat(users.toString()));

        final @NotNull Set<SecurityRole> roles = new HashSet<>(2);
        if (!newRole.isEmpty()) {
            roles.add(newRole);
        }
        if (!newRole1.isEmpty()) {
            roles.add(newRole1);
        }
        if (!newRole2.isEmpty()) {
            roles.add(newRole2);
        }
        System.out.println("===> roles:\n".concat(roles.toString()));

        final @NotNull Set<SecurityResource> resources = new HashSet<>(2);
        if (!newResource.isEmpty()) {
            resources.add(newResource);
        }
        if (!newResource1.isEmpty()) {
            resources.add(newResource1);
        }
        if (!newResource2.isEmpty()) {
            resources.add(newResource2);
        }
        System.out.println("===> resources:\n" + resources);

        Assert.isTrue(userEvent.insertUserRoleRelationship(newEntity, newRole, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        for (@NotNull SecurityUser eachUser : users) {
            Assert.isTrue(userEvent.insertUserRoleRelationship(eachUser, newRole, operator)
                    , "===== insertRoleResourceRelationship(...) -> false");
        }
        Assert.isTrue(userEvent.insertUserRoleRelationship(newEntity, roles, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        for (@NotNull SecurityUser eachUser : users) {
            Assert.isTrue(userEvent.insertUserRoleRelationship(eachUser, roles, operator)
                    , "===== insertRoleResourceRelationship(...) -> false");
        }

        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(roles, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");

        //===== 校验结果
        int existedResourceNum = 0;

        for (@NotNull SecurityUser eachUser : users) {
            final @NotNull List<SecurityResource> resourceList = userEvent.selectResourceOnUserByUsername(eachUser.getUsername());

            Assert.isTrue(null != resourceList && !resourceList.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            System.err.printf("=> 当前用户%s关联的资源:%s%n", eachUser, resourceList);

            existedResourceNum += resourceList.size();
        }

        Assert.isTrue(existedResourceNum == (4 + user_baseRoles_number * users.size())
                        || existedResourceNum == (6 + user_baseRoles_number * users.size())
                        || existedResourceNum == (8 + user_baseRoles_number * users.size())
                        || existedResourceNum == (9 + user_baseRoles_number * users.size())
                        || existedResourceNum == (12 + user_baseRoles_number * users.size())
                        || existedResourceNum == (18 + user_baseRoles_number * users.size())
                        || existedResourceNum == (27 + user_baseRoles_number * users.size())
                , String.format("===== selectResourceByUsername(..) -> %s <- 操作失败, 非预期结果!", existedResourceNum));
    }

    @Test
    @Transactional
    public void selectUrlPathByUsername()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        //===== 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull Map<String, EntityModel<?>> newUserData1 = getUserForTest(1);
        @NotNull Map<String, EntityModel<?>> newUserData2 = getUserForTest(1);

        Assert.isTrue(userEvent.registerUser((User) newUserData.get("user")
                , (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userEvent.registerUser((User) newUserData1.get("user")
                , (UserAccountOperationInfo) newUserData1.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData1.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userEvent.registerUser((User) newUserData2.get("user")
                , (UserAccountOperationInfo) newUserData2.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData2.get("userPersonInfo")
                , operator)
                        || (!userEvent.selectUserByUsername(((User) newUserData2.get("user")).getUsername()).isEmpty()
                        && !userEvent.selectUserAccountOperationInfoByUsername(((UserAccountOperationInfo) newUserData2.get("userAccountOperationInfo")).getUsername()).isEmpty()
                        && !userEvent.selectUserPersonInfoByUsername(((UserPersonInfo) newUserData2.get("userPersonInfo")).getUsername()).isEmpty())
                , "===== 添加测试数据 -> false");

        if (((User) newUserData2.get("user")).isEmpty()
                && ((User) newUserData2.get("user")).equals((User) newUserData1.get("user"))) {
            newUserData2.put("user", newUserData1.get("user"));
        }

        final @NotNull SecurityUser newEntity = getEntityForTest((User) newUserData.get("user"));
        final @NotNull SecurityUser newEntity1 = getEntityForTest((User) newUserData1.get("user"));
        final @NotNull SecurityUser newEntity2 = getEntityForTest((User) newUserData2.get("user"));

        Assert.isTrue(service.insert(newEntity, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity1, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity2, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");

        // (关联) 角色
        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole1, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole2, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== 添加测试数据 -> false");

        // (关联) 角色 -> (关联) 资源
        final @NotNull SecurityResource newResource = getResourceForTest();
        final @NotNull SecurityResource newResource1 = getResourceForTest(1);
        final @NotNull SecurityResource newResource2 = getResourceForTest(1);

        Assert.isTrue(securityResourceEvent.insertResource(newResource, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceEvent.insertResource(newResource1, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceEvent.insertResource(newResource2, operator)
                , "===== 添加测试数据 -> false");

        final @NotNull Set<SecurityUser> users = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            users.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            users.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            users.add(newEntity2);
        }
        System.out.println("===> users:\n" + users);

        final @NotNull Set<SecurityRole> roles = new HashSet<>(2);
        if (!newRole.isEmpty()) {
            roles.add(newRole);
        }
        if (!newRole1.isEmpty()) {
            roles.add(newRole1);
        }
        if (!newRole2.isEmpty()) {
            roles.add(newRole2);
        }
        System.out.println("===> roles:\n" + roles);

        final @NotNull Set<SecurityResource> resources = new HashSet<>(2);
        if (!newResource.isEmpty()) {
            resources.add(newResource);
        }
        if (!newResource1.isEmpty()) {
            resources.add(newResource1);
        }
        if (!newResource2.isEmpty()) {
            resources.add(newResource2);
        }
        System.out.println("===> resources:\n" + resources);

        final @NotNull String newUrl[] = new String[]{getClientId(), getUrlForTest(), getUrlHttpMethodNameForTest()};
        final @NotNull String newUrl1[] = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};
        final @NotNull String newUrl2[] = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};

        final @NotNull ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(3);
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(newUrl)) {
            urls.add(newUrl);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(newUrl1)) {
            urls.add(newUrl1);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(newUrl2)) {
            urls.add(newUrl2);
        }

        Assert.isTrue(userEvent.insertUserRoleRelationship(newEntity, newRole, operator)
                , "===== insertUserRoleRelationship(...) -> false");
        for (@NotNull SecurityUser eachUser : users) {
            Assert.isTrue(userEvent.insertUserRoleRelationship(eachUser, newRole, operator)
                    , "===== insertUserRoleRelationship(...) -> false");
        }
        Assert.isTrue(userEvent.insertUserRoleRelationship(newEntity, roles, operator)
                , "===== insertUserRoleRelationship(...) -> false");
        for (@NotNull SecurityUser eachUser : users) {
            Assert.isTrue(userEvent.insertUserRoleRelationship(eachUser, roles, operator)
                    , "===== insertUserRoleRelationship(...) -> false");
        }

        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(roles, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");

        Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newResource, newUrl, operator)
                , "===== insertResourceUrlRelationship(...) -> false");
        for (@NotNull SecurityResource eachResource : resources) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, newUrl, operator)
                    , "===== insertResourceUrlRelationship(...) -> false");
        }
        for (@NotNull String[] eachUrlInfo : urls) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newResource, eachUrlInfo, operator)
                    , "===== insertResourceUrlRelationship(...) -> false");
        }
        for (@NotNull SecurityResource eachResource : resources) {
            for (@NotNull String[] eachUrlInfo : urls) {
                Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, eachUrlInfo, operator)
                        , "===== insertResourceUrlRelationship(...) -> false");
            }
        }

        //===== selectUrlPathByUsernameAndClientId(..)
        /*int existedUrlPathNum = 0;*/

        for (@NotNull SecurityUser eachUser : users) {
            final @NotNull List<SecurityResourceUrl> eachUser_urlInfo = userEvent.selectUrlInfoOnUserByUsernameAndClientId(eachUser.getUsername(), getClientId());

            Assert.isTrue(null != eachUser_urlInfo && !eachUser_urlInfo.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            System.err.printf("=> 当前用户%s关联的 URL 路径:%s\n", eachUser, eachUser_urlInfo);

            @NotNull ContainArrayHashSet<String> urlInfoSet = new ContainArrayHashSet<>(eachUser_urlInfo.size());
            for (@NotNull SecurityResourceUrl urlInfo : eachUser_urlInfo) {
                final @NotNull String clientId = urlInfo.getClientId();
                final @NotNull String urlPath = urlInfo.getUrlPath();
                final @NotNull String urlMethod = urlInfo.getUrlMethod();

                Assert.isTrue(SecurityResourceUrl.Validator.RESOURCE_URL.clientId(clientId)
                                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(urlPath)
                                && SecurityResourceUrl.Validator.RESOURCE_URL.urlMethod(urlMethod)
                        , String.format("===== selectUrlPathByUsername(..) -> {clientId:%s, urlPath:%s, urlMethod:%s} <- 【操作失败, 非预期结果!】"
                                , clientId
                                , urlPath
                                , urlMethod)
                );

                urlInfoSet.add(new String[]{clientId, urlPath, urlMethod});
            }

            Assert.isTrue(urlInfoSet.containsAll(urls)
                    , String.format("===== selectUrlPathByUsername(..) - {urlInfoSet:%s, urls:%s} <- 【操作失败, 非预期结果集!】"
                            , urlInfoSet
                            , urls)
            );
            /*existedUrlPathNum += eachUser_urlInfo.size();*/
        }
        /*Assert.isTrue(existedUrlPathNum == 2 * 2
                        || existedUrlPathNum == 2 * 3
                , "===== selectUrlPathByUsername(..) -> "
                        .concat(Integer.toString(existedUrlPathNum))
                        .concat(" <- 操作失败, 非预期结果!"));*/
    }

    @Test
    @Transactional
    public void insert()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo
                , operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername()))
                , "===== insert(Entity) -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void insertRole()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull Map<String, EntityModel<?>> newUserData1 = getUserForTest(1);
        @NotNull Map<String, EntityModel<?>> newUserData2 = getUserForTest(1);

        Assert.isTrue(userEvent.registerUser((User) newUserData.get("user")
                , (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userEvent.registerUser((User) newUserData1.get("user")
                , (UserAccountOperationInfo) newUserData1.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData1.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userEvent.registerUser((User) newUserData2.get("user")
                , (UserAccountOperationInfo) newUserData2.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData2.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        if (((User) newUserData2.get("user")).isEmpty()
                && ((User) newUserData2.get("user")).equals((User) newUserData1.get("user"))) {
            newUserData2.put("user", newUserData1.get("user"));
        }

        final @NotNull SecurityUser newEntity = getEntityForTest((User) newUserData.get("user"));
        final @NotNull SecurityUser newEntity1 = getEntityForTest((User) newUserData1.get("user"));
        final @NotNull SecurityUser newEntity2 = getEntityForTest((User) newUserData2.get("user"));

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        //===== insertRole(...)
        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        final @NotNull Set<SecurityUser> users = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            users.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            users.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            users.add(newEntity2);
        }

        final @NotNull Set<SecurityRole> roles = new HashSet<>(2);
        if (!newRole.isEmpty()) {
            roles.add(newRole);
        }
        if (!newRole1.isEmpty()) {
            roles.add(newRole1);
        }
        if (!newRole2.isEmpty()) {
            roles.add(newRole2);
        }

        Assert.isTrue(userEvent.insertUserRoleRelationship(newEntity, newRole, operator)
                , "===== insertUserRoleRelationship(...) -> false");
        for (@NotNull SecurityUser eachUser : users) {
            Assert.isTrue(userEvent.insertUserRoleRelationship(eachUser, newRole, operator)
                    , "===== insertUserRoleRelationship(...) -> false");
        }
        Assert.isTrue(userEvent.insertUserRoleRelationship(newEntity, roles, operator)
                , "===== insertUserRoleRelationship(...) -> false");
        for (@NotNull SecurityUser eachUser : users) {
            Assert.isTrue(userEvent.insertUserRoleRelationship(eachUser, roles, operator)
                    , "===== insertUserRoleRelationship(...) -> false");
        }

        //===== 校验结果
        int existRoleNum = 0;

        for (@NotNull SecurityUser eachUser : users) {
            final @NotNull List<SecurityRole> eachUser_roles = userEvent.selectRoleOnUserByUsername(eachUser.getUsername());

            Assert.isTrue(!eachUser_roles.isEmpty()
                            && !(eachUser_roles.size() == 1 && eachUser_roles.get(0).equals(Security.RoleVo.USER))
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += eachUser_roles.size();
        }

        Assert.isTrue(existRoleNum == 2 * (2 + 1)
                        || existRoleNum == 2 * (3 + 1)
                        || existRoleNum == 3 * (2 + 1)
                        || existRoleNum == 3 * (3 + 1)
                , String.format("===== insertRole(...) -> %s <- 操作失败, 非预期结果!", existRoleNum));
    }

    @Test
    @Transactional
    public void update()
            throws BusinessAtomicException {
        final SecurityUser result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据失败!");

        //=== update(..)
        result = newEntity;

        Assert.isTrue(service.update(result, operator, operator_userAccountOperationInfo)
                , "===== update(...) -> false");
        Assert.isTrue(!result.isEmpty()
                , "===== update(...) -> 无效的 Entity");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void delete()
            throws BusinessAtomicException {
        final SecurityUser result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull User newUser = (User) newUserData.get("user");
        final @NotNull UserAccountOperationInfo newUserAccountOperationInfo = (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo");
        final @NotNull UserPersonInfo newUserPersonInfo = (UserPersonInfo) newUserData.get("userPersonInfo");

        Assert.isTrue(userEvent.registerUser(newUser, newUserAccountOperationInfo, newUserPersonInfo, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newEntity = getEntityForTest(newUser);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据失败!");

        //=== delete(..)
        result = newEntity;

        Assert.isTrue(service.delete(result, operator, operator_userAccountOperationInfo)
                , "===== delete(Entity) -> false");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void deleteRole()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newUserData = getUserForTest();
        final @NotNull Map<String, EntityModel<?>> newUserData1 = getUserForTest(1);
        @NotNull Map<String, EntityModel<?>> newUserData2 = getUserForTest(1);

        Assert.isTrue(userEvent.registerUser((User) newUserData.get("user")
                , (UserAccountOperationInfo) newUserData.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userEvent.registerUser((User) newUserData1.get("user")
                , (UserAccountOperationInfo) newUserData1.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData1.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userEvent.registerUser((User) newUserData2.get("user")
                , (UserAccountOperationInfo) newUserData2.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserData2.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        if (((User) newUserData2.get("user")).isEmpty()
                && ((User) newUserData2.get("user")).equals((User) newUserData1.get("user"))) {
            newUserData2.put("user", newUserData1.get("user"));
        }

        final @NotNull SecurityUser newEntity = getEntityForTest((User) newUserData.get("user"));
        final @NotNull SecurityUser newEntity1 = getEntityForTest((User) newUserData1.get("user"));
        final @NotNull SecurityUser newEntity2 = getEntityForTest((User) newUserData2.get("user"));

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(service.insert(newEntity1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(service.insert(newEntity2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        //===== insertRole(...)
        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(roleService.insert(newRole1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(roleService.insert(newRole2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        final @NotNull Set<SecurityUser> users = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            users.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            users.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            users.add(newEntity2);
        }

        final @NotNull Set<SecurityRole> roles = new HashSet<>(2);
        if (!newRole.isEmpty()) {
            roles.add(newRole);
        }
        if (!newRole1.isEmpty()) {
            roles.add(newRole1);
        }
        if (!newRole2.isEmpty()) {
            roles.add(newRole2);
        }

        Assert.isTrue(userEvent.insertUserRoleRelationship(newEntity, newRole, operator)
                , "===== insertUserRoleRelationship(...) -> false");
        for (@NotNull SecurityUser eachUser : users) {
            Assert.isTrue(userEvent.insertUserRoleRelationship(eachUser, newRole, operator)
                    , "===== insertUserRoleRelationship(...) -> false");
        }
        Assert.isTrue(userEvent.insertUserRoleRelationship(newEntity, roles, operator)
                , "===== insertUserRoleRelationship(...) -> false");
        for (@NotNull SecurityUser eachUser : users) {
            Assert.isTrue(userEvent.insertUserRoleRelationship(eachUser, roles, operator)
                    , "===== insertUserRoleRelationship(...) -> false");
        }

        //===== 校验测试数据
        int existRoleNum = 0;

        for (@NotNull SecurityUser eachUser : users) {
            final @NotNull List<SecurityRole> roleList = userEvent.selectRoleOnUserByUsername(eachUser.getUsername());

            Assert.isTrue(!roleList.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += roleList.size();
        }

        Assert.isTrue(existRoleNum == 2 * (2 + 1)
                        || existRoleNum == 2 * (3 + 1)
                        || existRoleNum == 3 * (2 + 1)
                        || existRoleNum == 3 * (3 + 1)
                , String.format("===== 校验测试数据 -> %s <- 操作失败, 非预期结果!", existRoleNum)
        );

        //===== deleteRole(...)
        Assert.isTrue(userEvent.deleteUserRoleRelationship(newEntity, newRole, operator)
                , "===== deleteUserRoleRelationship(...) -> false");
        for (@NotNull SecurityUser eachUser : users) {
            Assert.isTrue(userEvent.deleteUserRoleRelationship(eachUser, newRole, operator)
                    , "===== deleteUserRoleRelationship(...) -> false");
        }
        for (@NotNull SecurityRole eachRole : roles) {
            Assert.isTrue(userEvent.deleteUserRoleRelationship(newEntity, eachRole, operator)
                    , "===== deleteUserRoleRelationship(...) -> false");
        }
        for (@NotNull SecurityUser eachUser : users) {
            for (@NotNull SecurityRole eachRole : roles) {
                Assert.isTrue(userEvent.deleteUserRoleRelationship(eachUser, eachRole, operator)
                        , "===== deleteUserRoleRelationship(...) -> false");
            }
        }

        for (@NotNull SecurityUser eachUser : users) {
            final @NotNull List<SecurityRole> roleList = userEvent.selectRoleOnUserByUsername(eachUser.getUsername());

            Assert.isTrue((roleList.isEmpty()
                            || (roleList.size() == 1 && roleList.get(0).equals(Security.RoleVo.USER)))
                    , String.format("===== deleteRole(...) -> %s <- 非预期结果!", roleList)
            );
        }
    }

}
