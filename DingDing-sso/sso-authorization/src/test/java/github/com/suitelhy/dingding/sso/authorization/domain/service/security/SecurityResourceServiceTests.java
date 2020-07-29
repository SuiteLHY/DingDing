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
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashMap;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashSet;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Human;
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
 * (安全) 资源 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 *
 * @see SecurityResourceService
 */
@SpringBootTest
public class SecurityResourceServiceTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private SecurityResourceService service;

    @Autowired
    private SecurityRoleService roleService;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private UserService userService;

    @Value("${dingding.security.client-id}")
    private String clientId;

    private String getClientId() {
        return this.clientId;
    }

    @NotNull
    private SecurityResource getEntityForTest() {
        return getEntityForTest(null);
    }

    @NotNull
    private SecurityResource getEntityForTest(@Nullable Integer seed) {
        return SecurityResource.Factory.RESOURCE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , null
                , null
                , "test"
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
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
    private SecurityUser getSecurityUserForTest(@NotNull User user) {
        return SecurityUser.Factory.USER.create(user);
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
        final Page<SecurityResource> result;

        // 添加测试数据
        final SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据失败!");

        // selectAll(..)
        Assert.isTrue(!(result = service.selectAll(0, 10)).isEmpty()
                , "The result is empty");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectAllUrlRoleMap() {
        final ContainArrayHashMap<String, List<Object>> result;

        //===== 添加测试数据
        final SecurityResource newEntity = getEntityForTest();
        final SecurityResource newEntity1 = getEntityForTest(1);
        final SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity1.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity2.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        final String[] newUrl = new String[] {getClientId(), getUrlForTest()};
        final String[] newUrl1 = new String[] {getClientId(), getUrlForTest(1)};
        final String[] newUrl2 = new String[] {getClientId(), getUrlForTest(1)};

        final Set<SecurityResource> resources = new HashSet<>(1);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        final ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(3);
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl[1])) {
            urls.add(newUrl);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl1[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl1[1])) {
            urls.add(newUrl1);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl2[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl2[1])) {
            urls.add(newUrl2);
        }

        Assert.isTrue(service.insertUrl(newEntity, newUrl)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(resources, newUrl)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(newEntity, urls)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(resources, urls)
                , "===== insertUrl(...) -> false");

        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole2)
                , "===== 添加测试数据 -> false");

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
        Assert.isTrue(service.insertRole(resources, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(newEntity, roles)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(resources, roles)
                , "===== insertRole(...) -> false");

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

        final SecurityUser newSecurityUser = getSecurityUserForTest(newUser);
        final SecurityUser newSecurityUser1 = getSecurityUserForTest(newUser1);
        final SecurityUser newSecurityUser2 = getSecurityUserForTest(newUser2);

        Assert.isTrue(securityUserService.insert(newSecurityUser)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityUserService.insert(newSecurityUser1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityUserService.insert(newSecurityUser2)
                , "===== 添加测试数据 -> false");

        final Set<SecurityUser> users = new HashSet<>(2);
        if (!newSecurityUser.isEmpty()) {
            users.add(newSecurityUser);
        }
        if (!newSecurityUser1.isEmpty()) {
            users.add(newSecurityUser1);
        }
        if (!newSecurityUser2.isEmpty()) {
            users.add(newSecurityUser2);
        }

        Assert.isTrue(securityUserService.insertRole(newSecurityUser, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(securityUserService.insertRole(users, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(securityUserService.insertRole(newSecurityUser, roles)
                , "===== insertRole(...) -> false");
        Assert.isTrue(securityUserService.insertRole(users, roles)
                , "===== insertRole(...) -> false");

        //===== selectAllUrlRoleMap()
        result = service.selectAllUrlRoleMap();

        final Set<String> rolesIds = new HashSet<>(roles.size());
        for (SecurityRole role : roles) {
            rolesIds.add(role.id());
        }

        for (String[] url : urls) {
            boolean exist = false;
            for (Map.Entry<String[], List<Object>> entry : result.entrySet()) {
                if (Arrays.equals(url, entry.getKey())) {
                    if (null != entry.getValue()
                            && entry.getValue().containsAll(rolesIds)) {
                        exist = true;
                        break;
                    }
                }
            }
            Assert.isTrue(exist, "===== selectAllUrlRoleMap() <- 非预期结果!");
        }

        Assert.isTrue(!urls.isEmpty()
                , "===== The result is empty!");
    }

    @Test
    @Transactional
    public void selectCount() {
        final long result;

        // 添加测试数据
        final SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据失败!");

        // selectCount(..)
        Assert.isTrue((result = service.selectCount(10)) > 0
                , "The result equaled to or less than 0");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectResourceByCode() {
        final SecurityResource result;

        //===== 添加测试数据
        final SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据失败!");

        //===== selectResourceByCode(..)
        result = service.selectResourceByCode(newEntity.getCode());

        Assert.isTrue((null != result && !result.isEmpty())
                , "===== The result is empty");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectRoleByCode() {
        //=== 添加测试数据
        final SecurityResource newEntity = getEntityForTest();
        final SecurityResource newEntity1 = getEntityForTest(1);
        final SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity1.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity2.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole1)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole2)
                , "===== 添加测试数据 -> false");

        final Set<SecurityResource> resources = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
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
        Assert.isTrue(service.insertRole(resources, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(newEntity, roles)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(resources, roles)
                , "===== insertRole(...) -> false");

        //===== selectRoleByCode(..)
        int existRoleNum = 0;

        for (SecurityResource each : resources) {
            final List<Map<String, Object>> roleDataSet = service.selectRoleByCode(each.getCode());

            Assert.isTrue(null != roleDataSet && !roleDataSet.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += roleDataSet.size();
        }

        Assert.isTrue(existRoleNum == 4 || existRoleNum == 5 || existRoleNum == 6
                , "===== selectRoleByCode(...) -> "
                        .concat(Integer.toString(existRoleNum))
                        .concat(" <- 操作失败, 非预期结果!"));
    }

    @Test
    @Transactional
    public void selectUrlByCode()
            throws JsonProcessingException {
        //===== 添加测试数据
        final SecurityResource newEntity = getEntityForTest();
        final SecurityResource newEntity1 = getEntityForTest(1);
        final SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity1.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity2.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        final String[] newUrl = new String[] {getClientId(), getUrlForTest()};
        final String[] newUrl1 = new String[] {getClientId(), getUrlForTest(1)};
        final String[] newUrl2 = new String[] {getClientId(), getUrlForTest(1)};

        final Set<SecurityResource> resources = new HashSet<>(1);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        final ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(3);
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl[1])) {
            urls.add(newUrl);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl1[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl1[1])) {
            urls.add(newUrl1);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl2[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl2[1])) {
            urls.add(newUrl2);
        }

        Assert.isTrue(service.insertUrl(newEntity, newUrl)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(resources, newUrl)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(newEntity, urls)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(resources, urls)
                , "===== insertUrl(...) -> false");

        //===== selectUrlByCode(..)
        int existResourceNum = 0;
        for (SecurityResource each : resources) {
            final List<Map<String, Object>> resourceDataSet = service.selectUrlByCode(each.getCode());

            Assert.isTrue(null != resourceDataSet && !resourceDataSet.isEmpty()
                    , "===== selectUrlByCode(..) <- 非预期结果!");

            existResourceNum += resourceDataSet.size();

            System.out.println(toJSONString.writeValueAsString(resourceDataSet));
        }
        Assert.isTrue(3 < existResourceNum && existResourceNum < 7
                , "===== insertResource(...) -> "
                        .concat(Integer.toString(existResourceNum))
                        .concat(" <- 操作失败, 非预期结果!"));
    }

    @Test
    @Transactional
    public void insert() {
        final SecurityResource newEntity = getEntityForTest();

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
    public void insertRole()
            throws JsonProcessingException {
        //===== 添加测试数据
        final SecurityResource newEntity = getEntityForTest();
        final SecurityResource newEntity1 = getEntityForTest(1);
        final SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity1.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity2.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

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

        final Set<SecurityResource> resources = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
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
        Assert.isTrue(service.insertRole(resources, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(newEntity, roles)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(resources, roles)
                , "===== insertRole(...) -> false");

        //===== 校验结果
        int existRoleNum = 0;

        for (SecurityResource each : resources) {
            final List<Map<String, Object>> roleDataSet = service.selectRoleByCode(each.getCode());

            Assert.isTrue(null != roleDataSet && !roleDataSet.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += roleDataSet.size();
        }

        Assert.isTrue(existRoleNum == 4 || existRoleNum == 5 || existRoleNum == 6
                , "===== insertRole(...) -> "
                        .concat(Integer.toString(existRoleNum))
                        .concat(" <- 操作失败, 非预期结果!"));
    }

    @Test
    @Transactional
    public void insertUrl()
            throws JsonProcessingException {
        //===== 添加测试数据
        final SecurityResource newEntity = getEntityForTest();
        final SecurityResource newEntity1 = getEntityForTest(1);
        final SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity1.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity2.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        //===== insertUrl(...)
        final String[] newUrl = new String[] {getClientId(), getUrlForTest()};
        final String[] newUrl1 = new String[] {getClientId(), getUrlForTest(1)};
        final String[] newUrl2 = new String[] {getClientId(), getUrlForTest(1)};

        final Set<SecurityResource> resources = new HashSet<>(1);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        final ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(3);
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl[1])) {
            urls.add(newUrl);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl1[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl1[1])) {
            urls.add(newUrl1);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl2[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl2[1])) {
            urls.add(newUrl2);
        }

        Assert.isTrue(service.insertUrl(newEntity, newUrl)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(resources, newUrl)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(newEntity, urls)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(resources, urls)
                , "===== insertUrl(...) -> false");

        //===== 校验结果
        int existResourceNum = 0;
        for (SecurityResource each : resources) {
            final List<Map<String, Object>> resourceDataSet = service.selectUrlByCode(each.getCode());

            Assert.isTrue(null != resourceDataSet && !resourceDataSet.isEmpty()
                    , "===== insertUrl(...) <- 非预期结果!");

            existResourceNum += resourceDataSet.size();

            System.out.println(toJSONString.writeValueAsString(resourceDataSet));
        }
        Assert.isTrue(3 < existResourceNum && existResourceNum < 10
                , "===== insertResource(...) -> "
                        .concat(Integer.toString(existResourceNum))
                        .concat(" <- 操作失败, 非预期结果!"));
    }

    @Test
    @Transactional
    public void update() {
        final SecurityResource result;

        // 添加测试数据
        final @NotNull SecurityResource newEntity = getEntityForTest();

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
        final SecurityResource result;

        // 添加测试数据
        final SecurityResource newEntity = getEntityForTest();

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
    public void deleteRole()
            throws JsonProcessingException {
        //===== 添加测试数据
        final SecurityResource newEntity = getEntityForTest();
        final SecurityResource newEntity1 = getEntityForTest(1);
        final SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity1.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity2.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

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

        final Set<SecurityResource> resources = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
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
        Assert.isTrue(service.insertRole(resources, newRole)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(newEntity, roles)
                , "===== insertRole(...) -> false");
        Assert.isTrue(service.insertRole(resources, roles)
                , "===== insertRole(...) -> false");

        //===== 校验测试数据
        int existRoleNum = 0;

        for (SecurityResource each : resources) {
            final List<Map<String, Object>> roleDataSet = service.selectRoleByCode(each.getCode());

            Assert.isTrue(null != roleDataSet && !roleDataSet.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += roleDataSet.size();
        }

        Assert.isTrue(existRoleNum == 4 || existRoleNum == 5 || existRoleNum == 6
                , "===== 校验测试数据 -> "
                        .concat(Integer.toString(existRoleNum))
                        .concat(" <- 操作失败, 非预期结果!"));

        //===== deleteRole(...)
        Assert.isTrue(service.deleteRole(newEntity, newRole)
                , "===== deleteRole(...) -> false");
        Assert.isTrue(service.deleteRole(resources, newRole)
                , "===== deleteRole(...) -> false");
        Assert.isTrue(service.deleteRole(newEntity, roles)
                , "===== deleteRole(...) -> false");
        Assert.isTrue(service.deleteRole(resources, roles)
                , "===== deleteRole(...) -> false");

        for (SecurityResource each : resources) {
            final List<Map<String, Object>> roleDataSet = service.selectRoleByCode(each.getCode());

            Assert.isTrue(null == roleDataSet
                            || roleDataSet.isEmpty()
                            || (roleDataSet.size() == 1 && Security.RoleVo.USER.name().equals(roleDataSet.get(0).get("code")))
                    , "===== deleteRole(...) -> "
                            .concat(toJSONString.writeValueAsString(roleDataSet))
                            .concat(" <- 非预期结果!"));
        }
    }

    @Test
    @Transactional
    public void deleteUrl()
            throws JsonProcessingException {
        //===== 添加测试数据
        final SecurityResource newEntity = getEntityForTest();
        final SecurityResource newEntity1 = getEntityForTest(1);
        final SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity1.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity1)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity2.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity2)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        //===== insertUrl(...)
        final String[] newUrl = new String[] {getClientId(), getUrlForTest()};
        final String[] newUrl1 = new String[] {getClientId(), getUrlForTest(1)};
        final String[] newUrl2 = new String[] {getClientId(), getUrlForTest(1)};

        final Set<SecurityResource> resources = new HashSet<>(1);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        final ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(3);
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl[1])) {
            urls.add(newUrl);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl1[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl1[1])) {
            urls.add(newUrl1);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl2[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl2[1])) {
            urls.add(newUrl2);
        }

        Assert.isTrue(service.insertUrl(newEntity, newUrl)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(resources, newUrl)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(newEntity, urls)
                , "===== insertUrl(...) -> false");
        Assert.isTrue(service.insertUrl(resources, urls)
                , "===== insertUrl(...) -> false");

        //===== 校验测试数据
        int existResourceNum = 0;

        for (SecurityResource each : resources) {
            final List<Map<String, Object>> resourceDataSet = service.selectUrlByCode(each.getCode());

            Assert.isTrue(null != resourceDataSet && !resourceDataSet.isEmpty()
                    , "===== insertUrl(...) <- 非预期结果!");

            existResourceNum += resourceDataSet.size();
        }

        Assert.isTrue(3 < existResourceNum && existResourceNum < 10
                , "===== 校验测试数据 -> "
                        .concat(Integer.toString(existResourceNum))
                        .concat(" <- 操作失败, 非预期结果!"));

        //===== deleteUrl(...)
        Assert.isTrue(service.deleteUrl(newEntity, newUrl)
                , "===== deleteUrl(...) -> false");
        Assert.isTrue(service.deleteUrl(resources, newUrl)
                , "===== deleteUrl(...) -> false");
        Assert.isTrue(service.deleteUrl(newEntity, urls)
                , "===== deleteUrl(...) -> false");
        Assert.isTrue(service.deleteUrl(resources, urls)
                , "===== deleteUrl(...) -> false");

        for (SecurityResource each : resources) {
            final List<Map<String, Object>> urlDataSet = service.selectUrlByCode(each.getCode());

            Assert.isTrue(null == urlDataSet
                            || urlDataSet.isEmpty()
                    , "===== deleteUrl(...) -> "
                            .concat(toJSONString.writeValueAsString(urlDataSet))
                            .concat(" <- 非预期结果!"));
        }
    }

}
