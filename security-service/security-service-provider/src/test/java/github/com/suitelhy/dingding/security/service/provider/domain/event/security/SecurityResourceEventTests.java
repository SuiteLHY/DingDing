package github.com.suitelhy.dingding.security.service.provider.domain.event.security;

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
import github.com.suitelhy.dingding.security.service.api.domain.event.write.idempotence.SecurityResourceIdempotentEvent;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.non.idempotence.SecurityResourceNonIdempotentEvent;
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
public class SecurityResourceEventTests {

    @Autowired
    private SecurityResourceService securityResourceService;

    @Autowired
    private SecurityRoleService securityRoleService;

    @Autowired
    private SecurityUserService securityUserService;

//    @Autowired
//    private UserReadEvent userReadEvent;

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

//    private @NotNull String getClientId() {
//        return this.clientId;
//    }

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
     *  "user": {@link User},
     *  "userAccountOperationInfo": {@link UserAccountOperationInfo},
     *  "userPersonInfo": {@link UserPersonInfo}
     * }
     */
    private @NotNull Map<String, EntityModel> getUserForTest(Integer seed) {
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

    private @NotNull String getUrlHttpMethodNameForTest() {
        return HTTP.MethodVo.GET.name();
    }

    private @NotNull String ip() {
        return "127.0.0.0";
    }

    @Test
    public void contextLoads() {
        Assert.notNull(securityResourceEvent, "获取测试单元失败");
    }

    /**
     * @throws BusinessAtomicException
     * @see SecurityResourceReadEvent#existResourceOnRoleByRoleCode(String)
     */
    @Test
    @Transactional
    public void existResourceOnRoleByRoleCode()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userReadEvent.selectUserAccountOperationInfoByUsername(operator().getUsername());

        // 添加测试数据
        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityRole newRole = getRoleForTest();

        Assert.isTrue(securityRoleService.insert(newRole, operator)
                , String.format("===== 添加测试数据【%s】失败!", newRole));
        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , String.format("===== 添加测试数据【%s】失败!", newEntity));
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newRole, newEntity, operator)
                , String.format("===== 添加测试数据【%s <-> %s】失败!", newRole, newEntity));

        // existResourceOnRoleByRoleCode(String)
        Assert.isTrue(securityResourceEvent.existResourceOnRoleByRoleCode(newRole.getCode())
                , "校验不通过!");
    }

    /**
     * @see SecurityResourceReadEvent#selectResourceOnRoleByRoleCode(String)
     */
    @Test
    @Transactional
    public void selectResourceOnRoleByRoleCode()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userReadEvent.selectUserAccountOperationInfoByUsername(operator().getUsername());

        // 添加测试数据
        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityRole newRole = getRoleForTest();

        Assert.isTrue(securityRoleService.insert(newRole, operator)
                , String.format("===== 添加测试数据【%s】失败!", newRole));
        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , String.format("===== 添加测试数据【%s】失败!", newEntity));
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newRole, newEntity, operator)
                , String.format("===== 添加测试数据【%s <-> %s】失败!", newRole, newEntity));

        // selectResourceOnRoleByRoleCode(String)
        final @NotNull List<SecurityResource> existedResources = securityResourceEvent.selectResourceOnRoleByRoleCode(newRole.getCode());
        boolean existedResourceFlag = false;
        if (! existedResources.isEmpty()) {
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
     * @see SecurityResourceReadEvent#selectRoleOnResourceByResourceCode(String)
     */
    @Test
    @Transactional
    public void selectRoleOnResourceByResourceCode()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userReadEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(! newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(securityResourceService.insert(newEntity1, operator)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(! newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(securityResourceService.insert(newEntity2, operator)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(! newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        final int resource_baseRole_number = securityResourceEvent.selectRoleOnResourceByResourceCode(newEntity.getCode()).size();

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

        //===== selectRoleOnResourceByResourceCode(String)

        int existRoleNum = 0;
        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityRole> eachResource_roles = securityResourceEvent.selectRoleOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue((null != eachResource_roles && ! eachResource_roles.isEmpty())
                    , "===== 校验结果 <- 非预期结果!");

            existRoleNum += eachResource_roles.size();
        }

        Assert.isTrue((existRoleNum == (4 + resource_baseRole_number * existRoleNum)
                        || existRoleNum == (5 + resource_baseRole_number * existRoleNum)
                        || existRoleNum == (6 + resource_baseRole_number * existRoleNum))
                , String.format("===== selectRoleByCode(...) -> %s <- 操作失败, 非预期结果!", existRoleNum));
    }

    /**
     * @see SecurityResourceReadEvent#selectUrlInfoOnResourceByResourceCode(String)
     */
    @Test
    @Transactional
    public void selectUrlInfoOnResourceByResourceCode()
            throws BusinessAtomicException
    {
        /*// 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userReadEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(securityResourceService.insert(newEntity, operator)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(! newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(securityResourceService.insert(newEntity1, operator)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(! newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(securityResourceService.insert(newEntity2, operator)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(! newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

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
                , String.format("===== insertResource(...) -> %s <- 操作失败, 非预期结果!", existResourceNum));*/

        // (不报错就行)
        securityResourceEvent.selectUrlInfoOnResourceByResourceCode("aaa");
    }

}
