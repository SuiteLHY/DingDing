package github.com.suitelhy.dingding.sso.authorization.domain.repository.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityResourceRepository;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class SecurityResourceRepositoryTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private SecurityResourceRepository repository;

    @NotNull
    private SecurityResource getEntityForTest() {
        return SecurityResource.Factory.RESOURCE.create("test001", null, null
                , "test", null, 0
                , Resource.TypeVo.MENU);
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
    public void countByParentCode() {
        SecurityResource newEntity = getEntityForTest();
        String parentCode = "test";

        //===== 添加测试数据
        Assert.isTrue((newEntity.setParentCode(parentCode) && newEntity.isEntityLegal())
                , "getEntityForTest() -> 无效的 Entity");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== countByParentCode(...)
        long result;
        Assert.isTrue((result = repository.countByParentCode(parentCode)) > 0
                , "The result equaled to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void existsByCodeAndParentCode() {
        SecurityResource newEntity = getEntityForTest();

        //===== 添加测试数据
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== existsByCodeAndParentCode(...)
        boolean result = repository.existsByCodeAndParentCode(newEntity.getCode()
                , newEntity.getParentCode());
        Assert.isTrue(result
                , "The result -> not true");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAll()
            throws JsonProcessingException {
        List<SecurityResource> result;
        Assert.notEmpty(result = repository.findAll()
                , "The result equaled to or less than 0");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void findAllByPage()
            throws JsonProcessingException {
        Page<SecurityResource> result;

        Sort.TypedSort<SecurityResource> typedSort = Sort.sort(SecurityResource.class);
        Sort sort = typedSort.by(SecurityResource::getSort).descending()
                .and(typedSort.by(SecurityResource::getCode).ascending());
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
        SecurityResource newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== findById()
        Optional<SecurityResource> result = repository.findById(newEntity.id());
        Assert.notNull(result.get()
                , "The result -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findByCode() {
        SecurityResource newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== findByCode()
        Optional<SecurityResource> result = repository.findByCode(newEntity.getCode());
        Assert.notNull(result.get()
                , "The result -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAllByParentCode() {
        SecurityResource newEntity = getEntityForTest();

        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");

        //===== findAllByParentCode()
        Sort.TypedSort<SecurityResource> typedSort = Sort.sort(SecurityResource.class);
        Sort sort = typedSort.by(SecurityResource::getSort).descending()
                .and(typedSort.by(SecurityResource::getCode).ascending());
        Pageable page = PageRequest.of(0, 10, sort);

        Page<SecurityResource> result = repository.findAllByParentCode(newEntity.getParentCode()
                , page);
        Assert.isTrue(!result.isEmpty()
                , "The result -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectUrlByCode()
            throws JsonProcessingException {
        SecurityResource newEntity = getEntityForTest();

        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== selectUrlByCode(...)
        List<Map<String, Object>> result = repository.selectUrlByCode(newEntity.getCode());
        Assert.isTrue(null != result && !result.isEmpty()
                , "The result -> empty");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void saveAndFlush() {
        SecurityResource newEntity = getEntityForTest();
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
        SecurityResource newEntity = getEntityForTest();
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
    public void removeByCode() {
        SecurityResource newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(Entity) -> 无效的 Entity");

        //=== removeByCode(...)
        try {
            repository.removeByCode(newEntity.getCode());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - removeByCode(...) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - removeByCode(...) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void removeByCodeAndParentCode() {
        SecurityResource newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(Entity) -> 无效的 Entity");

        //=== removeByCodeAndParentCode(...)
        try {
            repository.removeByCodeAndParentCode(newEntity.getCode()
                    , newEntity.getParentCode());
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
