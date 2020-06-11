package github.com.suitelhy.dingding.sso.authorization.domain.repository.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleRepository;
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
public class SecurityRoleRepositoryTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private SecurityRoleRepository repository;

    @NotNull
    private SecurityRole getEntityForTest() {
        return SecurityRole.Factory.ROLE.create("test01", "测试角色");
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
    public void countByName()
            throws JsonProcessingException {
        SecurityRole newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");
        //===== countByName()
        long result;
        Assert.isTrue((result = repository.countByName(newEntity.getName())) > 0
                , "The result of count() equaled to or less than 0");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void findAllByPage()
            throws JsonProcessingException {
        Page<SecurityRole> result;
        Sort.TypedSort<SecurityRole> typedSort = Sort.sort(SecurityRole.class);
        Sort sort = typedSort.by(SecurityRole::getCode).ascending();
        Pageable page = PageRequest.of(0, 10, sort);
        Assert.notNull(result = repository.findAll(page)
                , "The result of findAll(Pageable) equaled to or less than 0");
        Assert.notEmpty(result.getContent()
                , "The result of result.getContent() -> empty");
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
    public void findAll()
            throws JsonProcessingException {
        List<SecurityRole> result;
        Assert.notEmpty(result = repository.findAll()
                , "The result of count() equaled to or less than 0");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void findById()
            throws JsonProcessingException {
        SecurityRole newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");
        //===== findById()
        Optional<SecurityRole> result = repository.findById(newEntity.id());
        Assert.notNull(result.get()
                , "The result of findById(String userId) -> empty");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void findByCode()
            throws JsonProcessingException {
        SecurityRole newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");
        //===== findByUsername()
        Optional<SecurityRole> result = repository.findByCode(newEntity.getCode());
        Assert.notNull(result.get()
                , "The result of findByCode(String username) -> empty");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void findByName()
            throws JsonProcessingException {
        SecurityRole newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityRole.Factory.ROLE.create(..) -> 无效的 SecurityRole");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(SecurityRole) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(SecurityRole) -> 无效的 SecurityRole");
        //===== findByUsername()
        Sort.TypedSort<SecurityRole> typedSort = Sort.sort(SecurityRole.class);
        Sort sort = typedSort.by(SecurityRole::getCode).ascending();
        Pageable page = PageRequest.of(0, 10, sort);
        List<SecurityRole> result = repository.findByName(newEntity.getName(), page);
        Assert.isTrue(null != result && !result.isEmpty()
                , "The result -> empty");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void selectResourceByCode()
            throws JsonProcessingException {
        SecurityRole newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "getRoleForTest() -> 无效的 SecurityRole");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== repository.saveAndFlush(newRole) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== repository.saveAndFlush(newRole) -> 无效的 User");
        //===== findByUsername()
        List<Map<String, Object>> result = repository.selectResourceByCode(newEntity.getCode());
        Assert.isTrue(null != result && !result.isEmpty()
                , "The result -> empty");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void selectUrlByCode()
            throws JsonProcessingException {
        SecurityRole newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "getRoleForTest() -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== repository.saveAndFlush(newEntity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== repository.saveAndFlush(newEntity) -> 无效的 User");
        //===== findByUsername()
        List<Map<String, Object>> result = repository.selectUrlByCode(newEntity.getCode());
        Assert.isTrue(null != result && !result.isEmpty()
                , "The result -> empty");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void saveAndFlush() {
        SecurityRole newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getRoleForTest() -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== repository.saveAndFlush(newEntity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== repository.saveAndFlush(newEntity) -> 无效的 SecurityRole");
        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void deleteById() {
        SecurityRole newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getRoleForTest() -> 无效的 User");
        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - repository.saveAndFlush(newRole) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - repository.saveAndFlush(newRole) -> 无效的 SecurityRole");
        //=== delete
        try {
            repository.deleteById(newEntity.id());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - delete(String id) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - delete(String id) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void removeByCode() {
        SecurityRole newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getRoleForTest() -> 无效的 User");
        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - repository.saveAndFlush(newEntity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - repository.saveAndFlush(newEntity) -> 无效的 SecurityRole");
        //=== delete
        try {
            repository.removeByCode(newEntity.getCode());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - removeByCode(String code) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - removeByCode(String code) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

}
