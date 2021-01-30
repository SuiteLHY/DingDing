package github.com.suitelhy.dingding.sso.authorization.domain.event.security;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 角色 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 * @see SecurityRoleEvent
 */
@SpringBootTest
public class SecurityRoleEventTests {

    @Autowired
    private SecurityRoleService service;

    @Autowired
    private SecurityResourceService resourceService;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private UserEvent userEvent;

    @Autowired
    private SecurityRoleEvent securityRoleEvent;

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
    SecurityRole getEntityForTest() {
        return getEntityForTest(null);
    }

    private @NotNull
    SecurityRole getEntityForTest(@Nullable Integer seed) {
        return SecurityRole.Factory.ROLE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
                                .concat(null == seed ? "" : Integer.toString(seed)))
                , "测试角色".concat(null == seed ? "" : Integer.toString(seed))
                , "测试用数据");
    }

    private @NotNull
    SecurityResource getResourceForTest() {
        return getResourceForTest(null);
    }

    private @NotNull
    SecurityResource getResourceForTest(@Nullable Integer seed) {
        @NotNull String seedString = (null == seed)
                ? ""
                : Integer.toString(seed);
        return SecurityResource.Factory.RESOURCE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
                                .concat(seedString))
                , null
                , null
                , "test".concat(seedString)
                , null
                , 0
                , Resource.TypeVo.MENU);
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @return {@link this#getUserForTest(Integer)}
     * · 数据结构:
     * {
     * "user": {@link User},
     * "userAccountOperationInfo": {@link UserAccountOperationInfo},
     * "userPersonInfo": {@link UserPersonInfo}
     * }
     */
    private @NotNull
    Map<String, EntityModel<?>> getUserForTest() {
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

    @Test
    public void contextLoads() {
        Assert.notNull(service, "获取测试单元失败");
        Assert.notNull(resourceService, "获取测试单元失败");
        Assert.notNull(securityUserService, "获取测试单元失败");
        Assert.notNull(userEvent, "获取测试单元失败");
        Assert.notNull(securityRoleEvent, "获取测试单元失败");
    }

    /**
     * @see SecurityRoleEvent#existRoleOnUserByUsername(String)
     */
    @Test
    @Transactional
    public void existRoleOnUserByUsername()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");

        final @NotNull Map<String, EntityModel<?>> newUserInfoMap = getUserForTest();

        Assert.isTrue(userEvent.registerUser((User) newUserInfoMap.get("user")
                , (UserAccountOperationInfo) newUserInfoMap.get("userAccountOperationInfo")
                , (UserPersonInfo) newUserInfoMap.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newUserInfoMap_securityUser = userEvent.selectSecurityUserByUsername(((User) newUserInfoMap.get("user")).getUsername());
        Assert.isTrue(securityRoleEvent.insertUserRoleRelationship(newUserInfoMap_securityUser, newEntity, operator)
                , "===== 添加测试数据失败!");

        // existRoleOnUserByUsername(String)

        Assert.isTrue(securityRoleEvent.existRoleOnUserByUsername(((User) newUserInfoMap.get("user")).getUsername())
                , "===== existRoleOnUserByUsername(String) -> false");
    }

    /**
     * @see SecurityRoleEvent#selectResourceOnRoleByRoleCode(String)
     */
    @Test
    @Transactional
    public void selectResourceOnRoleByRoleCode()
            throws BusinessAtomicException {
        final @NotNull Page<SecurityRole> result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");

        // selectAll(..)

        Assert.isTrue(!(result = service.selectAll(0, 10)).isEmpty()
                , "The result is empty");

        System.out.println(result);
    }

    /**
     * @see SecurityRoleEvent#selectRoleOnResourceByResourceCode(String)
     */
    @Test
    @Transactional
    public void selectRoleOnResourceByResourceCode()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityResource newResource = getResourceForTest();

        Assert.isTrue(resourceService.insert(newResource, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newEntity, newResource, operator)
                , "===== 添加测试数据失败!");

        // selectRoleOnResourceByResourceCode(String)

        final @NotNull List<SecurityRole> resource_roles = securityRoleEvent.selectRoleOnResourceByResourceCode(newResource.getCode());

        boolean resultFlag = false;
        if (!resource_roles.isEmpty()) {
            for (@NotNull SecurityRole eachRole : resource_roles) {
                if (newEntity.equals(eachRole)) {
                    resultFlag = true;
                    break;
                }
            }
        }

        Assert.isTrue(resultFlag, "===== existRoleOnUserByUsername(String) -> false");
    }

    /**
     * @see SecurityRoleEvent#insertUserRoleRelationship(SecurityUser, SecurityRole, SecurityUser)
     */
    @Test
    @Transactional
    public void insertUserRoleRelationship()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据

        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        final @NotNull Map<String, EntityModel<?>> newEntity_userInfoMap = getUserForTest();

        Assert.isTrue(userEvent.registerUser((User) newEntity_userInfoMap.get("user")
                , (UserAccountOperationInfo) newEntity_userInfoMap.get("userAccountOperationInfo")
                , (UserPersonInfo) newEntity_userInfoMap.get("userPersonInfo")
                , operator)
                , "===== 添加测试数据 -> false");

        final @NotNull SecurityUser newEntity_securityUser = userEvent.selectSecurityUserByUsername(((User) newEntity_userInfoMap.get("user")).getUsername());

        Assert.isTrue(securityRoleEvent.insertUserRoleRelationship(newEntity_securityUser, newEntity, operator)
                , "===== insertUserRoleRelationship(SecurityUser, SecurityRole, SecurityUser) -> false");

        //=== 校验结果

        final @NotNull List<SecurityRole> existedUser_roles = userEvent.selectRoleOnUserByUsername(newEntity_securityUser.getUsername());
        boolean resultFlag = false;
        if (!existedUser_roles.isEmpty()) {
            for (@NotNull SecurityRole eachRole : existedUser_roles) {
                if (newEntity.equals(eachRole)) {
                    resultFlag = true;
                    break;
                }
            }
        }

        Assert.isTrue(resultFlag, "===== 添加测试数据 -> false");
    }

    /**
     * @see SecurityRoleEvent#insertRoleResourceRelationship(SecurityRole, SecurityResource, SecurityUser)
     */
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE
            , timeout = 20)
    public void insertRoleResourceRelationship()
            throws Exception {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据

        final @NotNull SecurityRole newEntity = getEntityForTest();
        final @NotNull SecurityRole newEntity1 = getEntityForTest(1);
        final @NotNull SecurityRole newEntity2 = getEntityForTest(1);

        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(service.insert(newEntity1, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(service.insert(newEntity2, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        final @NotNull List<SecurityResource> role_baseResources = securityRoleEvent.selectResourceOnRoleByRoleCode(newEntity.getCode());

        final @NotNull SecurityResource newResource = getResourceForTest();
        final @NotNull SecurityResource newResource1 = getResourceForTest(1);
        final @NotNull SecurityResource newResource2 = getResourceForTest(1);

        Assert.isTrue(resourceService.insert(newResource, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(resourceService.insert(newResource1, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(resourceService.insert(newResource2, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        final @NotNull Set<SecurityRole> roles = new HashSet<>(2);
        if (!newEntity.isEmpty()) {
            roles.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            roles.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            roles.add(newEntity2);
        }
        System.out.printf("===> roles:【%s】%n", roles);

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

        //=== insertRoleResourceRelationship(...)

        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newEntity, newResource, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(roles, newResource, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newEntity, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(roles, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");

        //===== 校验结果

        int existResourceNum = 0;
        for (@NotNull SecurityRole each : roles) {
            final @NotNull List<SecurityResource> eachRole_resources = securityRoleEvent.selectResourceOnRoleByRoleCode(each.getCode());

            Assert.isTrue(!eachRole_resources.isEmpty()
                    , String.format("===== 校验结果 -> 【%s】 <- 非预期结果! ", each));

            Assert.isTrue(eachRole_resources.containsAll(role_baseResources)
                    , String.format("===== 校验结果 -> 【%s】 <- 非预期结果! ", each));

            existResourceNum += eachRole_resources.size();

            System.out.printf("当前关联的资源:【%s】%n", eachRole_resources);
        }

        Assert.isTrue((existResourceNum == (4 + role_baseResources.size() * roles.size())
                        || existResourceNum == (6 + role_baseResources.size() * roles.size())
                        || existResourceNum == (9 + role_baseResources.size() * roles.size()))
                , String.format("===== 校验结果为【%s】 <- 非预期结果!", existResourceNum));
    }

    /**
     * @see SecurityRoleEvent#updateRole(SecurityRole, SecurityUser)
     */
    @Test
    @Transactional
    public void updateRole()
            throws BusinessAtomicException {
        final @NotNull SecurityRole result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");

        //=== updateRole(SecurityRole, SecurityUser)

        result = newEntity;
        result.setName(String.format("%s%s", result.getName(), "new"));

        Assert.isTrue(securityRoleEvent.updateRole(result, operator)
                , "===== updateRole(SecurityRole, SecurityUser) -> false");

        //=== 校验数据

        Assert.isTrue(result.getName().equals(service.selectRoleByCode(newEntity.getCode()).getName())
                , "===== 校验数据 -> false");
    }

    /**
     * @see SecurityRoleEvent#deleteRole(SecurityRole, SecurityUser)
     */
    @Test
    @Transactional
    public void deleteRole()
            throws BusinessAtomicException {
        final @NotNull SecurityRole result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");

        //=== deleteRole(SecurityRole, SecurityUser)

        result = newEntity;

        Assert.isTrue(securityRoleEvent.deleteRole(result, operator)
                , "===== deleteRole(SecurityRole, SecurityUser) -> false");

        //=== 校验数据

        Assert.isTrue(service.selectRoleByCode(result.getCode()).isEmpty()
                , String.format("===== 校验数据【%s】 => deleteRole(SecurityRole, SecurityUser) -> false", result));
    }

    /**
     * @see SecurityRoleEvent#deleteRoleResourceRelationship(SecurityRole, SecurityResource, SecurityUser)
     */
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE
            , timeout = 20)
    public void deleteRoleResourceRelationship()
            throws Exception {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据

        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        final @NotNull SecurityResource newResource = getResourceForTest();
        final @NotNull SecurityResource newResource1 = getResourceForTest(1);
        final @NotNull SecurityResource newResource2 = getResourceForTest(1);

        Assert.isTrue(resourceService.insert(newResource, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(resourceService.insert(newResource1, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(resourceService.insert(newResource2, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");

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

        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newEntity, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");

        //===== 校验测试数据

        final @NotNull List<SecurityResource> newEntity_resources = securityRoleEvent.selectResourceOnRoleByRoleCode(newEntity.getCode());

        Assert.isTrue(newEntity_resources.containsAll(resources)
                , String.format("===== 校验测试数据【%s】 => 【%s】&【%s】 <- 非预期结果! ", newEntity, newEntity_resources, resources));

        //===== deleteRoleResourceRelationship(...)

        Assert.isTrue(securityRoleEvent.deleteRoleResourceRelationship(newEntity, resources, operator)
                , "===== deleteRoleResourceRelationship(...) -> false");

        //=== 校验结果

        final @NotNull List<SecurityResource> newEntity_resources_1 = securityRoleEvent.selectResourceOnRoleByRoleCode(newEntity.getCode());

        Assert.isTrue(!newEntity_resources_1.containsAll(resources)
                , String.format("===== 校验结果 => deleteResource(...) -> 【%s】&【%s】 <- 非预期结果!", newEntity, resources));
    }

}
