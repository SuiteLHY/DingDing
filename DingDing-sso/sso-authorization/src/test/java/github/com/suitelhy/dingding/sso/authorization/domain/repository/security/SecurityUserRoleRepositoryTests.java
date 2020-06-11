package github.com.suitelhy.dingding.sso.authorization.domain.repository.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRoleRepository;
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
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class SecurityUserRoleRepositoryTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private SecurityUserRoleRepository repository;

    @NotNull
    private SecurityUserRole getEntityForTest() {
        return SecurityUserRole.Factory.USER_ROLE.create("测试20200118132850", "1");
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
                , "The result of count() equaled to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void existsByUsernameAndRoleCode() {
        SecurityUserRole newEntity = getEntityForTest();
        //===== 添加测试数据
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== existsByUsernameAndRoleCode(...)
        boolean result = repository.existsByUsernameAndRoleCode(newEntity.getUsername()
                , newEntity.getRoleCode());
        Assert.isTrue(result
                , "The result -> not true");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAll()
            throws JsonProcessingException {
        List<SecurityUserRole> result;
        Assert.notEmpty(result = repository.findAll()
                , "The result equaled to or less than 0");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void findAllByPage()
            throws JsonProcessingException {
        Page<SecurityUserRole> result;

        Sort.TypedSort<SecurityUserRole> typedSort = Sort.sort(SecurityUserRole.class);
        Sort sort = typedSort.by(SecurityUserRole::getUsername).ascending()
                .and(typedSort.by(SecurityUserRole::getRoleCode).ascending());
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
        SecurityUserRole newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== findById()
        Optional<SecurityUserRole> result = repository.findById(newEntity.id());
        Assert.notNull(result.get()
                , "The result -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAllByUsername() {
        SecurityUserRole newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");

        //===== findByUsername()
        Sort.TypedSort<SecurityUserRole> typedSort = Sort.sort(SecurityUserRole.class);
        Sort sort = typedSort.by(SecurityUserRole::getUsername).ascending()
                .and(typedSort.by(SecurityUserRole::getRoleCode).ascending());
        Pageable page = PageRequest.of(0, 10, sort);

        Page<SecurityUserRole> result = repository.findAllByUsername(newEntity.getUsername(), page);
        Assert.isTrue(!result.isEmpty()
                , "The result of findByUsername(String username) -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAllByRoleCode() {
        SecurityUserRole newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== findAllByRoleCode(...)
        Sort.TypedSort<SecurityUserRole> typedSort = Sort.sort(SecurityUserRole.class);
        Sort sort = typedSort.by(SecurityUserRole::getUsername).ascending()
                .and(typedSort.by(SecurityUserRole::getRoleCode).ascending());
        Pageable page = PageRequest.of(0, 10, sort);

        Page<SecurityUserRole> result = repository.findAllByRoleCode(newEntity.getRoleCode(), page);
        Assert.isTrue(!result.isEmpty()
                , "The result -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void saveAndFlush() {
        SecurityUserRole newEntity = getEntityForTest();
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
        SecurityUserRole newEntity = getEntityForTest();
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
    public void removeByUsername() {
        SecurityUserRole newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(Entity) -> 无效的 Entity");

        //=== delete
        try {
            repository.removeByUsername(newEntity.getUsername());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - removeByUsername(...) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - removeByUsername(...) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void removeByRoleCode() {
        SecurityUserRole newEntity = getEntityForTest();
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
    public void removeByUsernameAndRoleCode() {
        SecurityUserRole newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(Entity) -> 无效的 Entity");

        //=== delete
        try {
            repository.removeByUsernameAndRoleCode(newEntity.getUsername(), newEntity.getRoleCode());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - removeByUsernameAndRoleCode(...) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - removeByUsernameAndRoleCode(...) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

}
