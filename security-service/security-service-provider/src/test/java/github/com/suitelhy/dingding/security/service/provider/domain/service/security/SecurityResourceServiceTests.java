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
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityResourceEvent;
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityRoleEvent;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityResourceService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityRoleService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserService;
//import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
//import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
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
 */
@SpringBootTest
public class SecurityResourceServiceTests {

    @Autowired
    private SecurityResourceService securityResourceService;

    @Autowired
    private SecurityRoleService securityRoleService;

    @Autowired
    private SecurityUserService securityUserService;

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
    private @NotNull SecurityUser operator() {
        return securityUserService.selectByUsername("admin");
    }

    private @NotNull String getClientId() {
        return "dingding_test";
    }

    private @NotNull SecurityResource getEntityForTest() {
        return getEntityForTest(null);
    }

    private @NotNull SecurityResource getEntityForTest(@Nullable Integer seed) {
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

    private @NotNull SecurityUser getSecurityUserForTest(@NotNull User user) {
        if (securityUserService.existByUsername(user.getUsername())) {
            return securityUserService.selectByUsername(user.getUsername());
        }
        return SecurityUser.Factory.USER.create(user);
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
     * "user": {@link User}
     * }
     */
    private @NotNull Map<String, EntityModel<?>> getUserForTest(Integer seed) {
        @NotNull Map<String, EntityModel<?>> result = new HashMap<>(3);

        @NotNull User newUser = User.Factory.USER.create(
                "测试用户".concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , "test123"
        );

        @NotNull String currentTime = new CalendarController().toString();
        /*@NotNull UserAccountOperationInfo userAccountOperationInfo = UserAccountOperationInfo.Factory.USER.create(
                newUser.getUsername()
                , "127.0.0.1"
                , currentTime
                , currentTime
        );

        @NotNull UserPersonInfo userPersonInfo = UserPersonInfo.Factory.USER.create(
                newUser.getUsername()
                , "测试用户"
                , null
                , null
                , null
                , null
        );*/

        result.put("user", newUser);
        /*result.put("userAccountOperationInfo", userAccountOperationInfo);
        result.put("userPersonInfo", userPersonInfo);*/

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
        Assert.notNull(securityResourceService, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAll()
            throws BusinessAtomicException
    {
        final Page<SecurityResource> result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        // selectAll(..)
        Assert.isTrue(! (result = securityResourceService.selectAll(0, 10)).isEmpty()
                , "The result is empty");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectCount()
            throws BusinessAtomicException
    {
        final long result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        // selectCount(..)

        Assert.isTrue((result = securityResourceService.selectCount(10)) > 0
                , "The result equaled to or less than 0");
    }

    @Test
    @Transactional
    public void selectResourceByCode()
            throws BusinessAtomicException
    {
        final @NotNull SecurityResource result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        //===== selectResourceByCode(..)

        result = securityResourceService.selectResourceByCode(newEntity.getCode());

        Assert.isTrue(! result.isEmpty()
                , "===== The result is empty");
    }

    @Test
    @Transactional
    public void selectRoleByCode()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity1, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity2, operator)
                , "===== 添加测试数据 -> false");

        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(securityRoleService.insert(newRole, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityRoleService.insert(newRole1, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityRoleService.insert(newRole2, operator)
                , "===== 添加测试数据 -> false");

        final @NotNull Set<SecurityResource> resources = new HashSet<>(2);
        if (! newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (! newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (! newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        final @NotNull Set<SecurityRole> roles = new HashSet<>(2);
        if (! newRole.isEmpty()) {
            roles.add(newRole);
        }
        if (! newRole1.isEmpty()) {
            roles.add(newRole1);
        }
        if (! newRole2.isEmpty()) {
            roles.add(newRole2);
        }

        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(newRole, newEntity, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(newRole, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(roles, newEntity, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(roles, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");

        //===== selectRoleByCode(..)

        int existRoleNum = 0;
        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityRole> eachResource_roles = securityRoleEvent.selectRoleOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue(null != eachResource_roles && !eachResource_roles.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += eachResource_roles.size();
        }

        Assert.isTrue(existRoleNum == 4 || existRoleNum == 5 || existRoleNum == 6
                , String.format("===== selectRoleByCode(...) -> 【%s】 <- 操作失败, 非预期结果!", existRoleNum));
    }

    @Test
    @Transactional
    public void selectUrlByCode()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity1, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity2, operator)
                , "===== 添加测试数据 -> false");

        final @NotNull String[] newUrl = new String[]{getClientId(), getUrlForTest(), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl1 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl2 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};

        final @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        if (! newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (! newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (! newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

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

        Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newEntity, newUrl, operator)
                , "===== insertResourceUrlRelationship(...) -> false");
        for (@NotNull SecurityResource eachResource : resources) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, newUrl, operator)
                    , "===== insertResourceUrlRelationship(...) -> false");
        }
        for (@NotNull String[] urlInfo : urls) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newEntity, urlInfo, operator)
                    , "===== insertResourceUrlRelationship(...) -> false");
        }
        for (@NotNull SecurityResource eachResource : resources) {
            for (@NotNull String[] urlInfo : urls) {
                Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, urlInfo, operator)
                        , "===== insertResourceUrlRelationship(...) -> false");
            }
        }

        //===== selectUrlInfoOnResourceByResourceCode(..)

        int existResourceNum = 0;
        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityResourceUrl> eachResource_urlInfo = securityResourceEvent.selectUrlInfoOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue(null != eachResource_urlInfo && ! eachResource_urlInfo.isEmpty()
                    , "===== selectUrlInfoOnResourceByResourceCode(..) <- 非预期结果!");

            existResourceNum += eachResource_urlInfo.size();

            System.out.println(eachResource_urlInfo);
        }
        Assert.isTrue(3 < existResourceNum && existResourceNum < 7
                , "===== insertResource(...) -> "
                        .concat(Integer.toString(existResourceNum))
                        .concat(" <- 操作失败, 非预期结果!"));
    }

    @Test
    @Transactional
    public void insert()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        final @NotNull SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== insert(Entity) -> false");

        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void insertRole()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity1, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity2, operator)
                , "===== 添加测试数据 -> false");

        //===== insertRole(...)

        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(securityRoleService.insert(newRole, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityRoleService.insert(newRole1, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityRoleService.insert(newRole2, operator)
                , "===== 添加测试数据 -> false");

        final @NotNull Set<SecurityResource> resources = new HashSet<>(2);
        if (! newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (! newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (! newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        final @NotNull Set<SecurityRole> roles = new HashSet<>(2);
        if (! newRole.isEmpty()) {
            roles.add(newRole);
        }
        if (! newRole1.isEmpty()) {
            roles.add(newRole1);
        }
        if (! newRole2.isEmpty()) {
            roles.add(newRole2);
        }

        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(newRole, newEntity, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(newRole, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(roles, newEntity, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(roles, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");

        //===== 校验结果

        int existRoleNum = 0;
        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityRole> eachResource_roles = securityResourceEvent.selectRoleOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue(null != eachResource_roles && !eachResource_roles.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += eachResource_roles.size();
        }
        Assert.isTrue(existRoleNum == 4 || existRoleNum == 5 || existRoleNum == 6
                , String.format("===== insertRole(...) -> 【%s】 <- 操作失败, 非预期结果!", existRoleNum));
    }

    @Test
    @Transactional
    public void insertResourceUrlRelationship()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final SecurityResource newEntity1 = getEntityForTest(1);
        final SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity1, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity2, operator)
                , "===== 添加测试数据 -> false");

        //===== insertResourceUrlRelationship(...)

        final @NotNull String[] newUrl = new String[]{getClientId(), getUrlForTest(), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl1 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl2 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};

        final @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        if (! newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (! newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (! newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

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

        Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newEntity, newUrl, operator)
                , "===== insertResourceUrlRelationship(...) -> false");
        for (@NotNull SecurityResource eachResource : resources) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, newUrl, operator)
                    , "===== insertResourceUrlRelationship(...) -> false");
        }
        for (@NotNull String[] eachUrlInfo : urls) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newEntity, eachUrlInfo, operator)
                    , "===== insertResourceUrlRelationship(...) -> false");
        }
        for (@NotNull SecurityResource eachResource : resources) {
            for (@NotNull String[] eachUrlInfo : urls) {
                Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, eachUrlInfo, operator)
                        , "===== insertResourceUrlRelationship(...) -> false");
            }
        }

        //===== 校验结果

        int existResourceNum = 0;
        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityResourceUrl> eachResource_roles = securityResourceEvent.selectUrlInfoOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue(null != eachResource_roles && !eachResource_roles.isEmpty()
                    , "===== insertUrl(...) <- 非预期结果!");

            existResourceNum += eachResource_roles.size();
        }
        Assert.isTrue((3 < existResourceNum && existResourceNum < 10)
                , String.format("===== insertResource(...) -> 【%s】 <- 操作失败, 非预期结果!", existResourceNum));
    }

    @Test
    @Transactional
    public void update()
            throws BusinessAtomicException
    {
        final SecurityResource result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        //=== update(..)

        result = newEntity;

        Assert.isTrue(result.setName(String.format("%s%s", result.getName(), "new"))
                , "===== 准备测试数据失败!");

        Assert.isTrue(securityResourceService.update(result, operator)
                , "===== update(Entity) -> false");

        //=== 校验数据

        final @NotNull SecurityResource existedResource = securityResourceService.selectResourceByCode(result.getCode());

        Assert.isTrue(!existedResource.isEmpty() && existedResource.getName().equals(result.getName())
                , String.format("===== 校验数据 -> 【%s】&【%s】 <- 非预期结果!", existedResource, result));
    }

    @Test
    @Transactional
    public void delete()
            throws BusinessAtomicException
    {
        final @NotNull SecurityResource result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        //=== delete(...)

        result = newEntity;

        Assert.isTrue(securityResourceService.delete(result, operator)
                , "===== delete(Entity) -> false");
    }

    @Test
    @Transactional
    public void deleteRole()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity1, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity2, operator)
                , "===== 添加测试数据 -> false");

        //===== insertRole(...)

        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(securityRoleService.insert(newRole, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityRoleService.insert(newRole1, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityRoleService.insert(newRole2, operator)
                , "===== 添加测试数据 -> false");

        final @NotNull Set<SecurityResource> resources = new HashSet<>(2);
        if (! newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (! newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (! newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        final @NotNull Set<SecurityRole> roles = new HashSet<>(2);
        if (! newRole.isEmpty()) {
            roles.add(newRole);
        }
        if (! newRole1.isEmpty()) {
            roles.add(newRole1);
        }
        if (! newRole2.isEmpty()) {
            roles.add(newRole2);
        }

        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(newRole, newEntity, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        for (@NotNull SecurityResource eachResource : resources) {
            Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(newRole, eachResource, operator)
                    , "===== insertRoleResourceRelationship(...) -> false");
        }
        for (@NotNull SecurityRole eachRole : roles) {
            Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(eachRole, newEntity, operator)
                    , "===== insertRoleResourceRelationship(...) -> false");
        }
        for (@NotNull SecurityRole eachRole : roles) {
            for (@NotNull SecurityResource eachResource : resources) {
                Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(eachRole, eachResource, operator)
                        , "===== insertRoleResourceRelationship(...) -> false");
            }
        }

        //===== 校验测试数据

        int existRoleNum = 0;
        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityRole> eachResource_roles = securityResourceEvent.selectRoleOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue(null != eachResource_roles && !eachResource_roles.isEmpty()
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += eachResource_roles.size();
        }
        Assert.isTrue(existRoleNum == 4 || existRoleNum == 5 || existRoleNum == 6
                , "===== 校验测试数据 -> "
                        .concat(Integer.toString(existRoleNum))
                        .concat(" <- 操作失败, 非预期结果!"));

        //===== deleteRoleResourceRelationship(...)

        Assert.isTrue(securityResourceEvent.deleteRoleResourceRelationship(newRole, newEntity, operator)
                , "===== deleteRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.deleteRoleResourceRelationship(newRole, resources, operator)
                , "===== deleteRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.deleteRoleResourceRelationship(roles, newEntity, operator)
                , "===== deleteRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.deleteRoleResourceRelationship(roles, resources, operator)
                , "===== deleteRoleResourceRelationship(...) -> false");

        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityRole> eachResource_roles = securityResourceEvent.selectRoleOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue((eachResource_roles.isEmpty()
                            /*|| (eachResource_roles.size() == 1 && eachResource_roles.get(0).equals(Security.RoleVo.USER))*/
                            || ! eachResource_roles.containsAll(roles))
                    , String.format("===== deleteRoleResourceRelationship(...) -> %s <- 非预期结果!", eachResource_roles));
        }
    }

    @Test
    @Transactional
    public void deleteUrl()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity1, operator)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(securityResourceService.insert(newEntity2, operator)
                , "===== 添加测试数据 -> false");

        //===== insertResourceUrlRelationship(...)

        final @NotNull String[] newUrl = new String[]{getClientId(), getUrlForTest(), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl1 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl2 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};

        final @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        if (! newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (! newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (! newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

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

        Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newEntity, newUrl, operator)
                , "===== insertResourceUrlRelationship(...) -> false");
        for (@NotNull SecurityResource eachResource : resources) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, newUrl, operator)
                    , "===== insertResourceUrlRelationship(...) -> false");
        }
        for (@NotNull String[] eachUrlInfo : urls) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newEntity, eachUrlInfo, operator)
                    , "===== insertResourceUrlRelationship(...) -> false");
        }
        for (@NotNull SecurityResource eachResource : resources) {
            for (@NotNull String[] eachUrlInfo : urls) {
                Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, eachUrlInfo, operator)
                        , "===== insertResourceUrlRelationship(...) -> false");
            }
        }

        //===== 校验测试数据

        int existResourceNum = 0;
        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityResourceUrl> eachResource_roles = securityResourceEvent.selectUrlInfoOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue(null != eachResource_roles && !eachResource_roles.isEmpty()
                    , "===== insertResourceUrlRelationship(...) <- 非预期结果!");

            existResourceNum += eachResource_roles.size();
        }
        Assert.isTrue(3 < existResourceNum && existResourceNum < 10
                , String.format("===== 校验测试数据 -> 【%s】 <- 操作失败, 非预期结果!", existResourceNum));

        //===== deleteResourceUrlRelationship(...)

        Assert.isTrue(securityResourceEvent.deleteResourceUrlRelationship(newEntity, newUrl, operator)
                , "===== deleteResourceUrlRelationship(...) -> false");
        for (@NotNull SecurityResource eachResource : resources) {
            Assert.isTrue(securityResourceEvent.deleteResourceUrlRelationship(eachResource, newUrl, operator)
                    , "===== deleteResourceUrlRelationship(...) -> false");
        }
        for (@NotNull String[] eachUrlInfo : urls) {
            Assert.isTrue(securityResourceEvent.deleteResourceUrlRelationship(newEntity, eachUrlInfo, operator)
                    , "===== deleteResourceUrlRelationship(...) -> false");
        }
        for (@NotNull SecurityResource eachResource : resources) {
            for (@NotNull String[] eachUrlInfo : urls) {
                Assert.isTrue(securityResourceEvent.deleteResourceUrlRelationship(eachResource, eachUrlInfo, operator)
                        , "===== deleteResourceUrlRelationship(...) -> false");
            }
        }

        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityResourceUrl> eachResource_roles = securityResourceEvent.selectUrlInfoOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue(eachResource_roles.isEmpty()
                    , String.format("===== deleteUrl(...) -> %s <- 非预期结果!", eachResource_roles));
        }
    }

}
