package github.com.suitelhy.dingding.sso.authorization.domain.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityResourceEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
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
 * @see SecurityRoleService
 */
@SpringBootTest
public class SecurityRoleServiceTests {

    /*@Autowired
    private ObjectMapper toJSONString;*/

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

    @Test
    public void contextLoads() {
        Assert.notNull(service, "获取测试单元失败");
        Assert.notNull(resourceService, "获取测试单元失败");
        Assert.notNull(securityUserService, "获取测试单元失败");
        Assert.notNull(userEvent, "获取测试单元失败");
        Assert.notNull(securityRoleEvent, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void existsByCode()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");

        // existsByCode(..)
        Assert.isTrue(service.existsByCode(newEntity.getCode())
                , "The result -> false");

        System.out.println(true);
    }

    @Test
    @Transactional
    public void selectCount()
            throws BusinessAtomicException {
        final long result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");

        // selectCount(..)
        Assert.isTrue((result = service.selectCount(10)) > 0
                , "The result equaled to or less than 0");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectRoleByCode()
            throws BusinessAtomicException {
        final @NotNull SecurityRole result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");

        // selectRoleByCode(..)
        result = service.selectRoleByCode(newEntity.getCode());
        Assert.isTrue((null != result && !result.isEmpty())
                , "===== The result is empty");

        System.out.println(result);
    }

    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE
            , timeout = 20)
    public void selectResourceByCode()
            throws Exception {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest(2);
        final @NotNull SecurityRole newEntity1 = getEntityForTest(3);
        final @NotNull SecurityRole newEntity2 = getEntityForTest(3);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity1.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity1, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity2.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity2, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        final @NotNull SecurityResource newResource = getResourceForTest(2);
        final @NotNull SecurityResource newResource1 = getResourceForTest(3);
        final @NotNull SecurityResource newResource2 = getResourceForTest(3);

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

        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newEntity, newResource, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(roles, newResource, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newEntity, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(roles, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");

        //===== selectResourceByCode(..)
        int existResourceNum = 0;
        for (@NotNull SecurityRole eachRole : roles) {
            final @NotNull List<SecurityResource> eachRole_resources = securityRoleEvent.selectResourceOnRoleByRoleCode(eachRole.getCode());

            Assert.isTrue(null != eachRole_resources && !eachRole_resources.isEmpty()
                    , "===== selectResourceByCode(..) <- 非预期结果! ".concat(eachRole.toString()));

            existResourceNum += eachRole_resources.size();

            System.out.println(eachRole_resources);
        }
        Assert.isTrue(existResourceNum == 4
                        || existResourceNum == 6
                        || existResourceNum == 9
                , "===== 校验结果为" + existResourceNum + " <- 非预期结果!");
    }

    @Test
    @Transactional
    public void insert()
            throws BusinessAtomicException {
        final @NotNull SecurityRole newEntity = getEntityForTest();

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== insert(Entity) -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        System.out.println(newEntity);
    }

    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE
            , timeout = 20)
    public void insertResource()
            throws Exception {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();
        final @NotNull SecurityRole newEntity1 = getEntityForTest(1);
        final @NotNull SecurityRole newEntity2 = getEntityForTest(1);

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity1.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity1, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity1.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        Assert.isTrue(newEntity2.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity2, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(!newEntity2.isEmpty() || newEntity2.equals(newEntity1)
                , "===== 添加测试数据 -> 无效的 Entity");

        //=== insertResource(..)
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

            Assert.isTrue(null != eachRole_resources && !eachRole_resources.isEmpty()
                    , "===== 校验结果 <- 非预期结果! ".concat(each.toString()));

            existResourceNum += eachRole_resources.size();

            System.out.println("当前关联的资源:" + eachRole_resources);
        }

        Assert.isTrue(existResourceNum == 4
                        || existResourceNum == 6
                        || existResourceNum == 9
                , "===== 校验结果为"
                        .concat(Integer.toString(existResourceNum))
                        .concat(" <- 非预期结果!"));
    }

    @Test
    @Transactional
    public void update()
            throws BusinessAtomicException {
        final @NotNull SecurityRole result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");

        //=== update(..)
        result = newEntity;

        Assert.isTrue(service.update(result, operator, operator_UserAccountOperationInfo)
                , "===== update(Entity) -> false");
        Assert.isTrue(!result.isEmpty()
                , "===== update(Entity) -> 无效的 Entity");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void delete()
            throws BusinessAtomicException {
        final @NotNull SecurityRole result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity, operator, operator_UserAccountOperationInfo)
                , "===== 添加测试数据失败!");

        //=== delete(..)
        result = newEntity;
        Assert.isTrue(service.delete(result, operator, operator_UserAccountOperationInfo)
                , "===== delete(Entity) -> false");

        System.out.println(result);
    }

    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE
            , timeout = 20)
    public void deleteResource()
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

        //=== insertResource(..)
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

        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newEntity, newResource, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(roles, newResource, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newEntity, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(roles, resources, operator)
                , "===== insertRoleResourceRelationship(...) -> false");

        //===== 校验测试数据

        int existResourceNum = 0;
        for (@NotNull SecurityRole eachRole : roles) {
            final @NotNull List<SecurityResource> eachRole_resources = securityRoleEvent.selectResourceOnRoleByRoleCode(eachRole.getCode());

            Assert.isTrue(null != eachRole_resources && !eachRole_resources.isEmpty()
                    , "===== 校验测试数据 <- 非预期结果! ".concat(eachRole.toString()));

            existResourceNum += eachRole_resources.size();

            System.out.println("当前关联的资源:" + eachRole_resources);
        }

        Assert.isTrue((existResourceNum == 2 * 2
                        || existResourceNum == 2 * 3
                        || existResourceNum == 3 * 3)
                , String.format("===== 校验测试数据:【%s】 <- 非预期结果!", existResourceNum));

        //===== deleteResource(...)

        Assert.isTrue(securityRoleEvent.deleteRoleResourceRelationship(newEntity, newResource, operator)
                , "===== deleteRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.deleteRoleResourceRelationship(roles, newResource, operator)
                , "===== deleteRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.deleteRoleResourceRelationship(newEntity, resources, operator)
                , "===== deleteRoleResourceRelationship(...) -> false");
        Assert.isTrue(securityRoleEvent.deleteRoleResourceRelationship(roles, resources, operator)
                , "===== deleteRoleResourceRelationship(...) -> false");

        for (@NotNull SecurityRole eachRole : roles) {
            final @NotNull List<SecurityResource> eachRole_resources = securityRoleEvent.selectResourceOnRoleByRoleCode(eachRole.getCode());

            Assert.isTrue((null == eachRole_resources || eachRole_resources.isEmpty())
                    , String.format("===== deleteResource(...) -> %s <- 非预期结果!", eachRole_resources));
        }
    }

}
