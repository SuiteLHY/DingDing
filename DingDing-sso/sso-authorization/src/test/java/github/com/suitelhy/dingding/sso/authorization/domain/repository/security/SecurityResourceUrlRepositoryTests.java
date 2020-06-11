package github.com.suitelhy.dingding.sso.authorization.domain.repository.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityResourceUrlRepository;
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
public class SecurityResourceUrlRepositoryTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private SecurityResourceUrlRepository repository;

    @NotNull
    private SecurityResourceUrl getEntityForTest() {
        return SecurityResourceUrl.Factory.RESOURCE_URL.create("1", "/test/1");
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
    public void findById() {
        SecurityResourceUrl newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== findById()
        Optional<SecurityResourceUrl> result = repository.findById(newEntity.id());
        Assert.notNull(result.get()
                , "The result -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAll()
            throws JsonProcessingException {
        List<SecurityResourceUrl> result;
        Assert.notEmpty(result = repository.findAll()
                , "The result equaled to or less than 0");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void findAllByPage()
            throws JsonProcessingException {
        Page<SecurityResourceUrl> result;

        Sort.TypedSort<SecurityResourceUrl> typedSort = Sort.sort(SecurityResourceUrl.class);
        Sort sort = typedSort.by(SecurityResourceUrl::getCode).ascending();
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
    public void findAllByCode() {
        SecurityResourceUrl newEntity = getEntityForTest();

        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");

        //===== findAllByCode()
        Sort.TypedSort<SecurityResourceUrl> typedSort = Sort.sort(SecurityResourceUrl.class);
        Sort sort = typedSort.by(SecurityResourceUrl::getCode).ascending();
        Pageable page = PageRequest.of(0, 10, sort);

        Page<SecurityResourceUrl> result = repository.findAllByCode(newEntity.getCode()
                , page);
        Assert.isTrue(!result.isEmpty()
                , "The result -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAllByUrlPath() {
        SecurityResourceUrl newEntity = getEntityForTest();

        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(Entity) -> 无效的 Entity");

        //===== findAllByUrlPath(...)
        Sort.TypedSort<SecurityResourceUrl> typedSort = Sort.sort(SecurityResourceUrl.class);
        Sort sort = typedSort.by(SecurityResourceUrl::getCode).ascending();
        Pageable page = PageRequest.of(0, 10, sort);

        Page<SecurityResourceUrl> result = repository.findAllByUrlPath(newEntity.getUrlPath(), page);
        Assert.isTrue(!result.isEmpty()
                , "The result -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void saveAndFlush() {
        SecurityResourceUrl newEntity = getEntityForTest();
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
        SecurityResourceUrl newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(Entity) -> 无效的 Entity");

        //=== deleteById(...)
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
        SecurityResourceUrl newEntity = getEntityForTest();
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
    public void removeByUrl() {
        SecurityResourceUrl newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(Entity) -> 无效的 Entity");

        //=== removeByUrlPath(...)
        try {
            repository.removeByUrlPath(newEntity.getUrlPath());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - removeByUrlPath(...) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - removeByUrlPath(...) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void removeByCodeAndUrlPath() {
        SecurityResourceUrl newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(Entity) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(Entity) -> 无效的 Entity");

        //=== removeByCodeAndUrlPath(...)
        try {
            repository.removeByCodeAndUrlPath(newEntity.getCode(), newEntity.getUrlPath());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - removeByCodeAndUrlPath(...) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - removeByCodeAndUrlPath(...) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

}
