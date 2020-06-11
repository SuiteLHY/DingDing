package github.com.suitelhy.dingding.sso.authorization.domain.repository.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleResourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class SecurityRoleResourceRepositoryTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private SecurityRoleResourceRepository repository;

    @NotNull
    private SecurityRoleResource getEntityForTest() {
        return SecurityRoleResource.Factory.ROLE_RESOURCE.create("1", "1");
    }

    @Test
    @Transactional
    public void contextLoads() {
        Assert.notNull(repository, "获取测试单元失败");
        // 批量添加测试数据
        for (int i = 0; i < 10; i++) {
            saveAndFlush();
        }
    }

    @Test
    @Transactional
    public void count() {
        long result;
        Assert.isTrue((result = repository.count()) > 0
                , "The result equaled to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void existsByRoleCodeAndResourceCode() {
        SecurityRoleResource newEntity = getEntityForTest();
        //===== 添加测试数据
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== existsByRoleCodeAndResourceCode(...)
        boolean result = repository.existsByRoleCodeAndResourceCode(newEntity.getRoleCode()
                , newEntity.getResourceCode());
        Assert.isTrue(result
                , "The result -> not true");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAll()
            throws JsonProcessingException {
        List<SecurityRoleResource> result;
        Assert.notEmpty(result = repository.findAll()
                , "The result equaled to or less than 0");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void findAllByPage()
            throws JsonProcessingException {
        Page<SecurityRoleResource> result;

        Sort.TypedSort<SecurityRoleResource> typedSort = Sort.sort(SecurityRoleResource.class);
        Sort sort = typedSort.by(SecurityRoleResource::getRoleCode).ascending()
                .and(typedSort.by(SecurityRoleResource::getResourceCode).ascending());
        Pageable page = PageRequest.of(0, 10, sort);

        Assert.notNull(result = repository.findAll(page)
                , "The result equaled to or less than 0");
        Assert.notEmpty(result.getContent()
                , "The result -> empty");
        System.out.println(toJSONString.writeValueAsString(result));
        System.out.println(result.getContent());
        System.out.println(result.getTotalElements());
        System.out.println(result.getTotalPages());
        System.out.println(result.getNumber());
        System.out.println(result.getSize());
        System.out.println(result.getSort());
    }

    @Test
    @Transactional
    public void findById() {
        SecurityRoleResource newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== findById()
        Optional<SecurityRoleResource> result = repository.findById(newEntity.id());
        Assert.notNull(result.get()
                , "The result -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAllByResourceCode() {
        SecurityRoleResource newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");

        //===== findByUsername()
        Sort.TypedSort<SecurityRoleResource> typedSort = Sort.sort(SecurityRoleResource.class);
        Sort sort = typedSort.by(SecurityRoleResource::getRoleCode).ascending()
                .and(typedSort.by(SecurityRoleResource::getResourceCode).ascending());
        Pageable page = PageRequest.of(0, 10, sort);

        Page<SecurityRoleResource> result = repository.findAllByResourceCode(newEntity.getResourceCode()
                , page);
        Assert.isTrue(!result.isEmpty()
                , "The result -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAllByRoleCode() {
        SecurityRoleResource newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== findAllByRoleCode(...)
        Sort.TypedSort<SecurityRoleResource> typedSort = Sort.sort(SecurityRoleResource.class);
        Sort sort = typedSort.by(SecurityRoleResource::getRoleCode).ascending()
                .and(typedSort.by(SecurityRoleResource::getResourceCode).ascending());
        Pageable page = PageRequest.of(0, 10, sort);

        Page<SecurityRoleResource> result = repository.findAllByRoleCode(newEntity.getRoleCode(), page);
        Assert.isTrue(!result.isEmpty()
                , "The result -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void saveAndFlush() {
        SecurityRoleResource newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");
        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void deleteById() {
        SecurityRoleResource newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(Entity) -> 无效的 Entity");

        //=== delete
        try {
            repository.deleteById(newEntity.id());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - deleteById(...) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - deleteById(...) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void removeByResourceCode() {
        SecurityRoleResource newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(Entity) -> 无效的 Entity");

        //=== delete
        try {
            repository.removeByResourceCode(newEntity.getResourceCode());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - removeByResourceCode(...) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - removeByResourceCode(...) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void removeByRoleCode() {
        SecurityRoleResource newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(Entity) -> 无效的 Entity");

        //=== delete
        try {
            repository.removeByRoleCode(newEntity.getRoleCode());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - removeByRoleCode(...) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - removeByRoleCode(...) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void removeByRoleCodeAndResourceCode() {
        SecurityRoleResource newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(Entity) -> 无效的 Entity");

        //=== delete
        try {
            repository.removeByRoleCodeAndResourceCode(newEntity.getRoleCode()
                    , newEntity.getResourceCode());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - removeByRoleCodeAndResourceCode(...) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - removeByRoleCodeAndResourceCode(...) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

}
