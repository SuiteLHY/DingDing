package github.com.suitelhy.dingding.sso.authorization.domain.event.security;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.*;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityResourceEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashSet;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import github.com.suitelhy.dingding.core.infrastructure.web.vo.HTTP;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 资源 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 * @see SecurityResourceEvent
 */
@SpringBootTest
public class SecurityResourceEventTests {

    @Autowired
    private SecurityResourceService service;

    @Autowired
    private SecurityRoleService roleService;

    @Autowired
    private SecurityResourceService resourceService;

    @Autowired
    private SecurityUserService securityUserService;

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
    private @NotNull
    SecurityUser operator() {
        return securityUserService.selectByUsername("admin");
    }

    private @NotNull
    String getClientId() {
        return this.clientId;
    }

    private @NotNull
    SecurityResource getEntityForTest() {
        return getEntityForTest(null);
    }

    private @NotNull
    SecurityResource getEntityForTest(@Nullable Integer seed) {
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

    private @NotNull
    SecurityRole getRoleForTest() {
        return getRoleForTest(null);
    }

    private @NotNull
    SecurityRole getRoleForTest(@Nullable Integer seed) {
        return SecurityRole.Factory.ROLE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
                                .concat(null == seed ? "" : Integer.toString(seed)))
                , "测试角色".concat(null == seed ? "" : Integer.toString(seed))
                , "测试用数据");
    }

    private @NotNull
    SecurityUser getSecurityUserForTest(@NotNull User user) {
        if (securityUserService.existByUsername(user.getUsername())) {
            return securityUserService.selectByUsername(user.getUsername());
        }
        return SecurityUser.Factory.USER.create(user);
    }

    private @NotNull
    String getUrlForTest() {
        return getUrlForTest(null);
    }

    private @NotNull
    String getUrlForTest(@Nullable Integer seed) {
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
    private @NotNull
    Map<String, EntityModel> getUserForTest() {
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
    Map<String, EntityModel> getUserForTest(Integer seed) {
        final @NotNull Map<String, EntityModel> result = new HashMap<>(3);

        final @NotNull User newUser = User.Factory.USER.create(
                "测试用户".concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , "test123"
        );

        @NotNull String currentTime = new CalendarController().toString();
        final @NotNull UserAccountOperationInfo userAccountOperationInfo = UserAccountOperationInfo.Factory.USER.create(
                newUser.getUsername()
                , "127.0.0.1"
                , currentTime
                , currentTime
        );

        final @NotNull UserPersonInfo userPersonInfo = UserPersonInfo.Factory.USER.create(
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
    String getUrlHttpMethodNameForTest() {
        return HTTP.MethodVo.GET.name();
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
     * @throws BusinessAtomicException
     * @see SecurityResourceEvent#existResourceOnRoleByRoleCode(String)
     */
    @Test
    @Transactional
    public void existResourceOnRoleByRoleCode()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator().getUsername());

        // 添加测试数据
        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityRole newRole = getRoleForTest();

        Assert.isTrue(roleService.insert(newRole, operator, operator_userAccountOperationInfo)
                , String.format("===== 添加测试数据【%s】失败!", newRole));
        Assert.isTrue(resourceService.insert(newEntity, operator, operator_userAccountOperationInfo)
                , String.format("===== 添加测试数据【%s】失败!", newEntity));
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newRole, newEntity, operator)
                , String.format("===== 添加测试数据【%s <-> %s】失败!", newRole, newEntity));

        // existResourceOnRoleByRoleCode(String)
        Assert.isTrue(securityResourceEvent.existResourceOnRoleByRoleCode(newRole.getCode())
                , "校验不通过!");
    }

    /**
     * @see SecurityResourceEvent#selectResourceOnRoleByRoleCode(String)
     */
    @Test
    @Transactional
    public void selectResourceOnRoleByRoleCode()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator().getUsername());

        // 添加测试数据
        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityRole newRole = getRoleForTest();

        Assert.isTrue(roleService.insert(newRole, operator, operator_userAccountOperationInfo)
                , String.format("===== 添加测试数据【%s】失败!", newRole));
        Assert.isTrue(resourceService.insert(newEntity, operator, operator_userAccountOperationInfo)
                , String.format("===== 添加测试数据【%s】失败!", newEntity));
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newRole, newEntity, operator)
                , String.format("===== 添加测试数据【%s <-> %s】失败!", newRole, newEntity));

        // selectResourceOnRoleByRoleCode(String)
        final @NotNull List<SecurityResource> existedResources = securityResourceEvent.selectResourceOnRoleByRoleCode(newRole.getCode());
        boolean existedResourceFlag = false;
        if (!existedResources.isEmpty()) {
            for (@NotNull SecurityResource eachResource : existedResources) {
                if (newEntity.equals(eachResource)) {
                    existedResourceFlag = true;
                    break;
                }
            }
        }

        Assert.isTrue(existedResourceFlag, "校验不通过!");
    }

    /**
     * @see SecurityResourceEvent#selectRoleOnResourceByResourceCode(String)
     */
    @Test
    @Transactional
    public void selectRoleOnResourceByResourceCode()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(service.insert(newEntity1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(service.insert(newEntity2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        final int resource_baseRole_number = securityResourceEvent.selectRoleOnResourceByResourceCode(newEntity.getCode()).size();

        final @NotNull SecurityRole newRole = getRoleForTest();
        final @NotNull SecurityRole newRole1 = getRoleForTest(1);
        final @NotNull SecurityRole newRole2 = getRoleForTest(1);

        Assert.isTrue(roleService.insert(newRole, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(roleService.insert(newRole2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        final @NotNull Set<SecurityResource> resources = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
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

        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(newRole, newEntity, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(newRole, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(roles, newEntity, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityResourceEvent.insertRoleResourceRelationship(roles, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");

        //===== selectRoleOnResourceByResourceCode(String)

        int existRoleNum = 0;
        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityRole> eachResource_roles = securityResourceEvent.selectRoleOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue((null != eachResource_roles && !eachResource_roles.isEmpty())
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += eachResource_roles.size();
        }

        Assert.isTrue((existRoleNum == (4 + resource_baseRole_number * existRoleNum)
                        || existRoleNum == (5 + resource_baseRole_number * existRoleNum)
                        || existRoleNum == (6 + resource_baseRole_number * existRoleNum))
                , String.format("===== selectRoleByCode(...) -> %s <- 操作失败, 非预期结果!", existRoleNum));
    }

    /**
     * @see SecurityResourceEvent#selectUrlInfoOnResourceByResourceCode(String)
     */
    @Test
    @Transactional
    public void selectUrlInfoOnResourceByResourceCode()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(service.insert(newEntity1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(service.insert(newEntity2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        final @NotNull String[] newUrl = new String[]{getClientId(), getUrlForTest(), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl1 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl2 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};

        final @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        final @NotNull ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(3);
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl[1])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlMethod(newUrl[2])) {
            urls.add(newUrl);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl1[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl1[1])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlMethod(newUrl1[2])) {
            urls.add(newUrl1);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl2[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl2[1])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlMethod(newUrl2[2])) {
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

            Assert.isTrue(null != eachResource_urlInfo && !eachResource_urlInfo.isEmpty()
                    , "===== selectUrlInfoOnResourceByResourceCode(..) <- 非预期结果!");

            existResourceNum += eachResource_urlInfo.size();
        }

        Assert.isTrue((3 < existResourceNum && existResourceNum < 7)
                , String.format("===== insertResource(...) -> %s <- 操作失败, 非预期结果!", existResourceNum));
    }

    /**
     * @see SecurityResourceEvent#insertResourceUrlRelationship(SecurityResourceUrl, SecurityUser)
     */
    @Test
    @Transactional
    public void insertResourceUrlRelationship()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据
        final @NotNull SecurityResource newEntity = getEntityForTest();
        final SecurityResource newEntity1 = getEntityForTest(1);
        final SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity1.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity2.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        //===== insertResourceUrlRelationship(...)
        final @NotNull String[] newUrl = new String[]{getClientId(), getUrlForTest(), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl1 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl2 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};

        final @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        final @NotNull ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(3);
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl[1])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlMethod(newUrl[2])) {
            urls.add(newUrl);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl1[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl1[1])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlMethod(newUrl1[2])) {
            urls.add(newUrl1);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl2[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl2[1])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlMethod(newUrl2[2])) {
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

            System.out.println(eachResource_roles);
        }
        Assert.isTrue(3 < existResourceNum && existResourceNum < 10
                , "===== insertResource(...) -> "
                        .concat(Integer.toString(existResourceNum))
                        .concat(" <- 操作失败, 非预期结果!"));
    }

    /**
     * @see SecurityResourceEvent#updateResource(SecurityResource, SecurityUser)
     */
    @Test
    @Transactional
    public void updateResource()
            throws BusinessAtomicException {
        final @NotNull SecurityResource result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据失败!");

        //=== updateResource(SecurityResource, SecurityUser)

        result = newEntity;

        Assert.isTrue(result.setName(String.format("%s%s", newEntity.getName(), "new"))
                , "===== 更新数据失败!");

        Assert.isTrue(securityResourceEvent.updateResource(result, operator)
                , "===== updateResource(SecurityResource, SecurityUser) -> false");

        //=== 校验数据

        final @NotNull SecurityResource existedResource = service.selectResourceByCode(result.getCode());

        Assert.isTrue((!existedResource.isEmpty() && result.getName().equals(existedResource.getName()))
                , String.format("===== 校验数据 -> 【%s】&【%s】 <- 执行失败!", result.getName(), existedResource.getName()));
    }

    /**
     * @see SecurityResourceEvent#deleteResource(SecurityResource, SecurityUser)
     */
    @Test
    @Transactional
    public void deleteResource()
            throws BusinessAtomicException {
        final @NotNull SecurityResource result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据失败!");

        //=== delete(..)

        result = newEntity;

        Assert.isTrue(securityResourceEvent.deleteResource(result, operator)
                , "===== deleteResource(...) -> false");

        //=== 校验数据

        Assert.isTrue(service.selectResourceByCode(result.getCode()).isEmpty()
                , "===== 校验数据 => deleteResource(...) -> false");

        System.out.println(result);
    }

    /**
     * @see SecurityResourceEvent#deleteResourceUrlRelationship(SecurityResource, String[], SecurityUser)
     */
    @Test
    @Transactional
    public void deleteResourceUrlRelationship__SecurityResourceUrl()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据
        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(service.insert(newEntity1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(service.insert(newEntity2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        //===== insertResourceUrlRelationship(...)

        final @NotNull String[] newUrl = new String[]{getClientId(), getUrlForTest(), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl1 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};
        final @NotNull String[] newUrl2 = new String[]{getClientId(), getUrlForTest(1), getUrlHttpMethodNameForTest()};

        final @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        final @NotNull ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(3);
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl[1])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlMethod(newUrl[2])) {
            urls.add(newUrl);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl1[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl1[1])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlMethod(newUrl1[2])) {
            urls.add(newUrl1);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.clientId(newUrl2[0])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(newUrl2[1])
                && SecurityResourceUrl.Validator.RESOURCE_URL.urlMethod(newUrl2[2])) {
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

        Assert.isTrue((3 < existResourceNum && existResourceNum < 10)
                , "===== 校验测试数据 -> "
                        .concat(Integer.toString(existResourceNum))
                        .concat(" <- 操作失败, 非预期结果!"));

        //===== deleteResourceUrlRelationship(...)

        Assert.isTrue(securityResourceEvent.deleteResourceUrlRelationship(newEntity, newUrl, operator)
                , "===== deleteResourceUrlRelationship(SecurityResource, String[], SecurityUser) -> false");
        for (@NotNull SecurityResource eachResource : resources) {
            Assert.isTrue(securityResourceEvent.deleteResourceUrlRelationship(eachResource, newUrl, operator)
                    , "===== deleteResourceUrlRelationship(SecurityResource, String[], SecurityUser) -> false");
        }
        for (@NotNull String[] eachUrlInfo : urls) {
            Assert.isTrue(securityResourceEvent.deleteResourceUrlRelationship(newEntity, eachUrlInfo, operator)
                    , "===== deleteResourceUrlRelationship(SecurityResource, String[], SecurityUser) -> false");
        }
        for (@NotNull SecurityResource eachResource : resources) {
            for (@NotNull String[] eachUrlInfo : urls) {
                Assert.isTrue(securityResourceEvent.deleteResourceUrlRelationship(eachResource, eachUrlInfo, operator)
                        , "===== deleteResourceUrlRelationship(SecurityResource, String[], SecurityUser) -> false");
            }
        }

        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityResourceUrl> eachResource_roles = securityResourceEvent.selectUrlInfoOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue((null == eachResource_roles || eachResource_roles.isEmpty())
                    , String.format("===== deleteResourceUrlRelationship(SecurityResource, String[], SecurityUser) -> %s <- 非预期结果!", eachResource_roles));
        }
    }

    /**
     * @see SecurityResourceEvent#deleteRoleResourceRelationship(SecurityRole, SecurityResource, SecurityUser)
     */
    @Test
    @Transactional
    public void deleteRoleResourceRelationship()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(service.insert(newEntity1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(service.insert(newEntity2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        final @NotNull List<SecurityRole> resource_baseRoles = securityResourceEvent.selectRoleOnResourceByResourceCode(newEntity.getCode());

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

        final @NotNull Set<SecurityResource> resources = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
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
                , String.format("===== 校验测试数据 -> %s <- 操作失败, 非预期结果!", existRoleNum));

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

            Assert.isTrue(/*(eachResource_roles.isEmpty()
                            || (eachResource_role.size() == 1 && eachResource_role.get(0).equals(Security.RoleVo.USER)))*/
                    resource_baseRoles.equals(eachResource_roles)
                    , String.format("===== deleteRoleResourceRelationship(...) -> %s <- 非预期结果!", eachResource_roles));
        }
    }

}
