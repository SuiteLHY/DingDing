package github.com.suitelhy.dingding.sso.authorization.domain.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 角色 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 *
 * @see SecurityRoleService
 */
@SpringBootTest
public class SecurityRoleServiceTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private SecurityRoleService service;

    @Autowired
    private SecurityResourceService resourceService;

    @NotNull
    private SecurityRole getEntityForTest() {
        return getEntityForTest(null);
    }

    @NotNull
    private SecurityRole getEntityForTest(@Nullable Integer seed) {
        return SecurityRole.Factory.ROLE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
                        .concat(null == seed ? "" : Integer.toString(seed)))
                , "测试角色".concat(null == seed ? "" : Integer.toString(seed))
                , "测试用数据");
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

    @Test
    public void contextLoads() {
        Assert.notNull(service, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void existsByCode() {
        // 添加测试数据
        final SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
                , "===== 添加测试数据失败!");

        // existsByCode(..)
        Assert.isTrue(service.existsByCode(newEntity.getCode())
                , "The result -> false");

        System.out.println(true);
    }

    @Test
    @Transactional
    public void selectAll() {
        final Page<SecurityRole> result;

        // 添加测试数据
        final SecurityRole newEntity = getEntityForTest();

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
    public void selectCount() {
        final long result;

        // 添加测试数据
        final SecurityRole newEntity = getEntityForTest();

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
    public void selectRoleByCode() {
        final SecurityRole result;

        // 添加测试数据
        final SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
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
        //=== 添加测试数据
        final SecurityRole newEntity = getEntityForTest(2);
        final SecurityRole newEntity1 = getEntityForTest(3);
        final SecurityRole newEntity2 = getEntityForTest(3);

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

        final SecurityResource newResource = getResourceForTest(2);
        final SecurityResource newResource1 = getResourceForTest(3);
        final SecurityResource newResource2 = getResourceForTest(3);

        Assert.isTrue(resourceService.insert(newResource)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(resourceService.insert(newResource1)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(resourceService.insert(newResource2)
                , "===== 添加测试数据 -> false");

        final Set<SecurityRole> roles = new HashSet<>(2);
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

        Assert.isTrue(service.insertResource(newEntity, newResource)
                , "===== insertResource(...) -> false");
        Assert.isTrue(service.insertResource(roles, newResource)
                , "===== insertResource(...) -> false");
        Assert.isTrue(service.insertResource(newEntity, resources)
                , "===== insertResource(...) -> false");
        Assert.isTrue(service.insertResource(roles, resources)
                , "===== insertResource(...) -> false");

        //===== selectResourceByCode(..)
        int existResourceNum = 0;
        for (SecurityRole each : roles) {
            final List<Map<String, Object>> resourceDataSet = service.selectResourceByCode(each.getCode());

            Assert.isTrue(null != resourceDataSet && !resourceDataSet.isEmpty()
                    , "===== selectResourceByCode(..) <- 非预期结果! ".concat(each.toString()));

            existResourceNum += resourceDataSet.size();

            System.out.println(toJSONString.writeValueAsString(resourceDataSet));
        }
        Assert.isTrue(existResourceNum == 4
                        || existResourceNum == 6
                        || existResourceNum == 9
                , "===== 校验结果为" + existResourceNum + " <- 非预期结果!");
    }

    @Test
    @Transactional
    public void insert() {
        final SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(service.insert(newEntity)
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
        //=== 添加测试数据
        final SecurityRole newEntity = getEntityForTest();
        final SecurityRole newEntity1 = getEntityForTest(1);
        final SecurityRole newEntity2 = getEntityForTest(1);

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

        //=== insertResource(..)
        final SecurityResource newResource = getResourceForTest();
        final SecurityResource newResource1 = getResourceForTest(1);
        final SecurityResource newResource2 = getResourceForTest(1);

        Assert.isTrue(resourceService.insert(newResource)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(resourceService.insert(newResource1)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(resourceService.insert(newResource2)
                , "===== 添加测试数据 -> false");

        final Set<SecurityRole> roles = new HashSet<>(2);
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

        Assert.isTrue(service.insertResource(newEntity, newResource)
                , "===== insertResource(...) -> false");
        Assert.isTrue(service.insertResource(roles, newResource)
                , "===== insertResource(...) -> false");
        Assert.isTrue(service.insertResource(newEntity, resources)
                , "===== insertResource(...) -> false");
        Assert.isTrue(service.insertResource(roles, resources)
                , "===== insertResource(...) -> false");

        //===== 校验结果
        int existResourceNum = 0;

        for (SecurityRole each : roles) {
            final List<Map<String, Object>> resourceDataSet = service.selectResourceByCode(each.getCode());

            Assert.isTrue(null != resourceDataSet && !resourceDataSet.isEmpty()
                    , "===== 校验结果 <- 非预期结果! ".concat(each.toString()));

            existResourceNum += resourceDataSet.size();

            System.out.println("当前关联的资源:" + toJSONString.writeValueAsString(resourceDataSet));
        }

        Assert.isTrue(existResourceNum == 4
                        || existResourceNum == 6
                        || existResourceNum == 9
                , "===== 校验结果为" + existResourceNum + " <- 非预期结果!");
    }

    @Test
    @Transactional
    public void update() {
        final SecurityRole result;

        // 添加测试数据
        final SecurityRole newEntity = getEntityForTest();

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
        final SecurityRole result;

        // 添加测试数据
        final SecurityRole newEntity = getEntityForTest();

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
    @Transactional(isolation = Isolation.SERIALIZABLE
            , timeout = 20)
    public void deleteResource()
            throws Exception {
        //=== 添加测试数据
        final SecurityRole newEntity = getEntityForTest();
        final SecurityRole newEntity1 = getEntityForTest(1);
        final SecurityRole newEntity2 = getEntityForTest(1);

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

        //=== insertResource(..)
        final SecurityResource newResource = getResourceForTest();
        final SecurityResource newResource1 = getResourceForTest(1);
        final SecurityResource newResource2 = getResourceForTest(1);

        Assert.isTrue(resourceService.insert(newResource)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(resourceService.insert(newResource1)
                , "===== 添加测试数据 -> false");
        Assert.isTrue(resourceService.insert(newResource2)
                , "===== 添加测试数据 -> false");

        final Set<SecurityRole> roles = new HashSet<>(2);
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

        Assert.isTrue(service.insertResource(newEntity, newResource)
                , "===== insertResource(...) -> false");
        Assert.isTrue(service.insertResource(roles, newResource)
                , "===== insertResource(...) -> false");
        Assert.isTrue(service.insertResource(newEntity, resources)
                , "===== insertResource(...) -> false");
        Assert.isTrue(service.insertResource(roles, resources)
                , "===== insertResource(...) -> false");

        //===== 校验测试数据
        int existResourceNum = 0;

        for (SecurityRole each : roles) {
            final List<Map<String, Object>> resourceDataSet = service.selectResourceByCode(each.getCode());

            Assert.isTrue(null != resourceDataSet && !resourceDataSet.isEmpty()
                    , "===== 校验测试数据 <- 非预期结果! ".concat(each.toString()));

            existResourceNum += resourceDataSet.size();

            System.out.println("当前关联的资源:" + toJSONString.writeValueAsString(resourceDataSet));
        }

        Assert.isTrue(existResourceNum == 2 * 2
                        || existResourceNum == 2 * 3
                        || existResourceNum == 3 * 3
                , "===== 校验测试数据:" + existResourceNum + " <- 非预期结果!");

        //===== deleteResource(...)
        Assert.isTrue(service.deleteResource(newEntity, newResource)
                , "===== deleteResource(...) -> false");
        Assert.isTrue(service.deleteResource(roles, newResource)
                , "===== deleteResource(...) -> false");
        Assert.isTrue(service.deleteResource(newEntity, resources)
                , "===== deleteResource(...) -> false");
        Assert.isTrue(service.deleteResource(roles, resources)
                , "===== deleteResource(...) -> false");

        for (SecurityRole each : roles) {
            final List<Map<String, Object>> resourceDataSet = service.selectResourceByCode(each.getCode());

            Assert.isTrue(null == resourceDataSet
                            || resourceDataSet.isEmpty()
                    , "===== deleteResource(...) -> "
                            .concat(toJSONString.writeValueAsString(resourceDataSet))
                            .concat(" <- 非预期结果!")
            );
        }
    }

}
