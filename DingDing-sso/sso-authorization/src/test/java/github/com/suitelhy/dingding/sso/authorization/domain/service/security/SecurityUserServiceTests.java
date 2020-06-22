package github.com.suitelhy.dingding.sso.authorization.domain.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Human;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * (安全) 用户 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 *
 * @see SecurityUserService
 */
@SpringBootTest
public class SecurityUserServiceTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private SecurityUserService service;

    @Autowired
    private SecurityResourceService resourceService;

    @Autowired
    private SecurityRoleService roleService;

    @Autowired
    private UserService userService;

    @NotNull
    private SecurityUser getEntityForTest() {
        return SecurityUser.Factory.USER.create("402880e56fb72000016fb72014fc0000"
                , "测试20200118132850"
                , VoUtil.getVoByValue(Account.StatusVo.class, 1));
    }

    @NotNull
    private SecurityUser getEntityForTest(@NotNull User user) {
        return SecurityUser.Factory.USER.create(user);
    }

    @NotNull
    private SecurityResource getResourceForTest() {
        return getResourceForTest(null);
    }

    @NotNull
    private SecurityResource getResourceForTest(@Nullable Integer seed) {
        return SecurityResource.Factory.RESOURCE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
                                .concat(null == seed ? "" : Integer.toString(seed)))
                , null
                , null
                , "test".concat(null == seed ? "" : Integer.toString(seed))
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

    @NotNull
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
    public void selectUserByUserId() {
        final SecurityUser result;

        // 添加测试数据
        final SecurityUser newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据失败!");

        // selectUserByUserId(..)
        result = service.selectUserByUserId(newEntity.getUserId());
        Assert.isTrue((null != result && !result.isEmpty())
                , "===== The result is empty");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectUserByUsername() {
        final SecurityUser result;

        // 添加测试数据
        final SecurityUser newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据失败!");

        // selectUserByUsername(..)
        result = service.selectUserByUsername(newEntity.getUsername());
        Assert.isTrue((null != result && !result.isEmpty())
                , "===== The result is empty");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectRoleByUsername() {
        //===== 添加测试数据
        final User newUser = getUserForTest();
        final User newUser1 = getUserForTest(1);
        User newUser2 = getUserForTest(1);

        Assert.isTrue(userService.insert(newUser)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userService.insert(newUser1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userService.insert(newUser2)
                , "===== 添加测试数据 -> false");

        if (newUser2.isEmpty() && newUser2.equals(newUser1)) {
            newUser2 = newUser1;
        }

        final SecurityUser newEntity = getEntityForTest(newUser);
        final SecurityUser newEntity1 = getEntityForTest(newUser1);
        final SecurityUser newEntity2 = getEntityForTest(newUser2);

        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");

        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole2)
                , "===== 添加测试数据 -> false");

        final Set<SecurityUser> users = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            users.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            users.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            users.add(newEntity2);
        }

        final Set<SecurityRole> roles = new HashSet<>(2);
        if (!newRole.isEmpty()) {
            roles.add(newRole);
        }
        if (!newRole1.isEmpty()) {
            roles.add(newRole1);
        }
        if (!newRole2.isEmpty()) {
            roles.add(newRole2);
        }

        Assert.isTrue(service.insertRole(newEntity, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(users, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(newEntity, roles)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(users, roles)
                , "===== insertRole(...) -> false");

        //===== selectRoleByUsername(..)
        int existRoleNum = 0;

        for (SecurityUser each : users) {
            final List<Map<String, Object>> roleDataSet = service.selectRoleByUsername(each.getUsername());

            Assert.isTrue(null != roleDataSet
                            && !roleDataSet.isEmpty()
                            && !(roleDataSet.size() == 1 && Security.RoleVo.USER.name().equals(roleDataSet.get(0).get("role_code")))
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += roleDataSet.size();
        }

        Assert.isTrue(existRoleNum == 2 * (2 + 1)
                        || existRoleNum == 2 * (3 + 1)
                        || existRoleNum == 3 * (2 + 1)
                        || existRoleNum == 3 * (3 + 1)
                , "===== selectRoleByUsername(..) -> "
                        .concat(Integer.toString(existRoleNum))
                        .concat(" <- 操作失败, 非预期结果!"));
    }

    @Test
    @Transactional
    public void selectResourceByUsername()
            throws JsonProcessingException {
        //===== 添加测试数据
        final User newUser = getUserForTest();
        final User newUser1 = getUserForTest(1);
        User newUser2 = getUserForTest(1);

        Assert.isTrue(userService.insert(newUser)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userService.insert(newUser1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userService.insert(newUser2)
                , "===== 添加测试数据 -> false");

        if (newUser2.isEmpty() && newUser2.equals(newUser1)) {
            newUser2 = newUser1;
        }

        final SecurityUser newEntity = getEntityForTest(newUser);
        final SecurityUser newEntity1 = getEntityForTest(newUser1);
        final SecurityUser newEntity2 = getEntityForTest(newUser2);

        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");

        // (关联) 角色
        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole2)
                , "===== 添加测试数据 -> false");

        // (关联) 角色 -> (关联) 资源
        final SecurityResource newResource = getResourceForTest();
        final SecurityResource newResource1 = getResourceForTest(1);
        final SecurityResource newResource2 = getResourceForTest(1);

        Assert.isTrue(resourceService.insert(newResource)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(resourceService.insert(newResource1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(resourceService.insert(newResource2)
                , "===== 添加测试数据 -> false");

        final Set<SecurityUser> users = new HashSet<>(2);
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

        final Set<SecurityRole> roles = new HashSet<>(2);
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

        final Set<SecurityResource> resources = new HashSet<>(2);
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

        Assert.isTrue(service.insertRole(newEntity, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(users, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(newEntity, roles)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(users, roles)
                , "===== insertRole(...) -> false");

        Assert.isTrue(roleService.insertResource(roles, resources)
                , "===== insertResource(...) -> false");

        //===== 校验结果
        int existedResourceNum = 0;

        for (SecurityUser each : users) {
            final List<Map<String, Object>> resourceDataSet = service.selectResourceByUsername(each.getUsername());

            Assert.isTrue(null != resourceDataSet && !resourceDataSet.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            System.err.println("=> 当前用户" + each
                    + "关联的资源:" + toJSONString.writeValueAsString(resourceDataSet));

            existedResourceNum += resourceDataSet.size();
        }

        Assert.isTrue(existedResourceNum == 4
                        || existedResourceNum == 6
                        || existedResourceNum == 8
                        || existedResourceNum == 9
                        || existedResourceNum == 12
                        || existedResourceNum == 18
                        || existedResourceNum == 27
                , "===== selectResourceByUsername(..) -> "
                        .concat(Integer.toString(existedResourceNum))
                        .concat(" <- 操作失败, 非预期结果!"));
    }

    @Test
    @Transactional
    public void selectUrlPathByUsername()
            throws JsonProcessingException {
        //===== 添加测试数据
        final User newUser = getUserForTest();
        final User newUser1 = getUserForTest(1);
        User newUser2 = getUserForTest(1);

        Assert.isTrue(userService.insert(newUser)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userService.insert(newUser1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userService.insert(newUser2)
                , "===== 添加测试数据 -> false");

        if (newUser2.isEmpty() && newUser2.equals(newUser1)) {
            newUser2 = newUser1;
        }

        final SecurityUser newEntity = getEntityForTest(newUser);
        final SecurityUser newEntity1 = getEntityForTest(newUser1);
        final SecurityUser newEntity2 = getEntityForTest(newUser2);

        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");

        // (关联) 角色
        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole2)
                , "===== 添加测试数据 -> false");

        // (关联) 角色 -> (关联) 资源
        final SecurityResource newResource = getResourceForTest();
        final SecurityResource newResource1 = getResourceForTest(1);
        final SecurityResource newResource2 = getResourceForTest(1);

        Assert.isTrue(resourceService.insert(newResource)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(resourceService.insert(newResource1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(resourceService.insert(newResource2)
                , "===== 添加测试数据 -> false");

        final Set<SecurityUser> users = new HashSet<>(2);
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

        final Set<SecurityRole> roles = new HashSet<>(2);
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

        final Set<SecurityResource> resources = new HashSet<>(2);
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

        final String newUrl = getUrlForTest();
        final String newUrl1 = getUrlForTest(1);
        final String newUrl2 = getUrlForTest(1);

        final Set<String> urls = new HashSet<>(3);
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl)) {
            urls.add(newUrl);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl1)) {
            urls.add(newUrl1);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl2)) {
            urls.add(newUrl2);
        }

        Assert.isTrue(service.insertRole(newEntity, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(users, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(newEntity, roles)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(users, roles)
                , "===== insertRole(...) -> false");

        Assert.isTrue(roleService.insertResource(roles, resources)
                , "===== insertResource(...) -> false");

        Assert.isTrue(resourceService.insertUrl(newResource, newUrl)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(resourceService.insertUrl(resources, newUrl)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(resourceService.insertUrl(newResource, urls)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(resourceService.insertUrl(resources, urls)
                , "===== insertUrl(...) -> false");

        //===== selectUrlPathByUsername(..)
        int existedUrlPathNum = 0;

        for (SecurityUser each : users) {
            final List<Map<String, Object>> urlPathDataSet = service.selectUrlPathByUsername(each.getUsername());

            Assert.isTrue(null != urlPathDataSet && !urlPathDataSet.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            System.err.println("=> 当前用户" + each
                    + "关联的 URL 路径:" + toJSONString.writeValueAsString(urlPathDataSet));

            existedUrlPathNum += urlPathDataSet.size();
        }

        Assert.isTrue(existedUrlPathNum == 2 * 2
                        || existedUrlPathNum == 2 * 3
                , "===== selectUrlPathByUsername(..) -> "
                        .concat(Integer.toString(existedUrlPathNum))
                        .concat(" <- 操作失败, 非预期结果!"));
    }

    @Test
    @Transactional
    public void insert() {
        final SecurityUser newEntity = getEntityForTest();

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
    public void insertRole() {
        //===== 添加测试数据
        final User newUser = getUserForTest();
        final User newUser1 = getUserForTest(1);
        User newUser2 = getUserForTest(1);

        Assert.isTrue(userService.insert(newUser)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userService.insert(newUser1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userService.insert(newUser2)
                , "===== 添加测试数据 -> false");

        if (newUser2.isEmpty() && newUser2.equals(newUser1)) {
            newUser2 = newUser1;
        }

        final SecurityUser newEntity = getEntityForTest(newUser);
        final SecurityUser newEntity1 = getEntityForTest(newUser1);
        final SecurityUser newEntity2 = getEntityForTest(newUser2);

        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");

        //===== insertRole(...)
        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole2)
                , "===== 添加测试数据 -> false");

        final Set<SecurityUser> users = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            users.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            users.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            users.add(newEntity2);
        }

        final Set<SecurityRole> roles = new HashSet<>(2);
        if (!newRole.isEmpty()) {
            roles.add(newRole);
        }
        if (!newRole1.isEmpty()) {
            roles.add(newRole1);
        }
        if (!newRole2.isEmpty()) {
            roles.add(newRole2);
        }

        Assert.isTrue(service.insertRole(newEntity, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(users, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(newEntity, roles)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(users, roles)
                , "===== insertRole(...) -> false");

        //===== 校验结果
        int existRoleNum = 0;

        for (SecurityUser each : users) {
            final List<Map<String, Object>> roleDataSet = service.selectRoleByUsername(each.getUsername());

            Assert.isTrue(null != roleDataSet
                            && !roleDataSet.isEmpty()
                            && !(roleDataSet.size() == 1 && Security.RoleVo.USER.name().equals(roleDataSet.get(0).get("role_code")))
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += roleDataSet.size();
        }

        Assert.isTrue(existRoleNum == 2 * (2 + 1)
                        || existRoleNum == 2 * (3 + 1)
                        || existRoleNum == 3 * (2 + 1)
                        || existRoleNum == 3 * (3 + 1)
                , "===== insertRole(...) -> "
                        .concat(Integer.toString(existRoleNum))
                        .concat(" <- 操作失败, 非预期结果!"));
    }

    @Test
    @Transactional
    public void update() {
        final SecurityUser result;

        // 添加测试数据
        final SecurityUser newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据失败!");

        //=== update(..)
        result = newEntity;

        Assert.isTrue(service.update(result)
                , "===== update(Entity) -> false");
        Assert.isTrue(!result.isEmpty()
                , "===== update(Entity) -> 无效的 Entity");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void delete() {
        final SecurityUser result;

        // 添加测试数据
        final SecurityUser newEntity = getEntityForTest();

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

    @Test
    @Transactional
    public void deleteRole() throws JsonProcessingException {
        //===== 添加测试数据
        final User newUser = getUserForTest();
        final User newUser1 = getUserForTest(1);
        User newUser2 = getUserForTest(1);

        Assert.isTrue(userService.insert(newUser)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userService.insert(newUser1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(userService.insert(newUser2)
                , "===== 添加测试数据 -> false");

        if (newUser2.isEmpty() && newUser2.equals(newUser1)) {
            newUser2 = newUser1;
        }

        final SecurityUser newEntity = getEntityForTest(newUser);
        final SecurityUser newEntity1 = getEntityForTest(newUser1);
        final SecurityUser newEntity2 = getEntityForTest(newUser2);

        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");

        //===== insertRole(...)
        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole2)
                , "===== 添加测试数据 -> false");

        final Set<SecurityUser> users = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            users.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            users.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            users.add(newEntity2);
        }

        final Set<SecurityRole> roles = new HashSet<>(2);
        if (!newRole.isEmpty()) {
            roles.add(newRole);
        }
        if (!newRole1.isEmpty()) {
            roles.add(newRole1);
        }
        if (!newRole2.isEmpty()) {
            roles.add(newRole2);
        }

        Assert.isTrue(service.insertRole(newEntity, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(users, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(newEntity, roles)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(users, roles)
                , "===== insertRole(...) -> false");

        //===== 校验测试数据
        int existRoleNum = 0;

        for (SecurityUser each : users) {
            final List<Map<String, Object>> roleDataSet = service.selectRoleByUsername(each.getUsername());

            Assert.isTrue(null != roleDataSet && !roleDataSet.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += roleDataSet.size();
        }

        Assert.isTrue(existRoleNum == 2 * (2 + 1)
                        || existRoleNum == 2 * (3 + 1)
                        || existRoleNum == 3 * (2 + 1)
                        || existRoleNum == 3 * (3 + 1)
                , "===== 校验测试数据 -> "
                        .concat(Integer.toString(existRoleNum))
                        .concat(" <- 操作失败, 非预期结果!")
        );

        //===== deleteRole(...)
        Assert.isTrue(service.deleteRole(newEntity, newRole)
                , "===== deleteRole(...) -> false");
        Assert.isTrue(service.deleteRole(users, newRole)
                , "===== deleteRole(...) -> false");
        Assert.isTrue(service.deleteRole(newEntity, roles)
                , "===== deleteRole(...) -> false");
        Assert.isTrue(service.deleteRole(users, roles)
                , "===== deleteRole(...) -> false");

        for (SecurityUser each : users) {
            final List<Map<String, Object>> roleDataSet = service.selectRoleByUsername(each.getUsername());

            Assert.isTrue(null == roleDataSet
                            || roleDataSet.isEmpty()
                            || (roleDataSet.size() == 1 && Security.RoleVo.USER.name().equals(roleDataSet.get(0).get("role_code")))
                    , "===== deleteRole(...) -> "
                            .concat(toJSONString.writeValueAsString(roleDataSet))
                            .concat(" <- 非预期结果!")
            );
        }
    }

}
