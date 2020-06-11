package github.com.suitelhy.dingding.sso.authorization.domain.repository.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRepository;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
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
public class SecurityUserRepositoryTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private SecurityUserRepository repository;

    @NotNull
    private SecurityUser getEntityForTest() {
        return SecurityUser.Factory.USER.update("402880e56fb72000016fb72014fc0000"
                , "测试20200118132850"
                , VoUtil.getVoByValue(Account.StatusVo.class, 1));
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
    public void findAllByPage()
            throws JsonProcessingException {
        Page<SecurityUser> result;
        Sort.TypedSort<SecurityUser> typedSort = Sort.sort(SecurityUser.class);
        Sort sort = typedSort.by(SecurityUser::getUsername).ascending();
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
        List<SecurityUser> result;
        Assert.notEmpty(result = repository.findAll()
                , "The result of count() equaled to or less than 0");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void findById() {
        SecurityUser newEntity = getEntityForTest();

        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");

        //===== findById()
        Optional<SecurityUser> result = repository.findById(newEntity.id());
        Assert.notNull(result.get()
                , "The result of findById(String userId) -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findByUsername() {
        SecurityUser newEntity = getEntityForTest();
        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");
        //===== findByUsername()
        Optional<SecurityUser> result = repository.findByUsername(newEntity.getUsername());
        Assert.notNull(result.get()
                , "The result of findByUsername(String username) -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectRoleByUsername()
            throws JsonProcessingException {
        SecurityUser newEntity = getEntityForTest();

        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");

        //===== findByUsername()
        List<Map<String, Object>> result = repository.selectRoleByUsername(newEntity.getUsername());
        Assert.isTrue(null != result && !result.isEmpty()
                , "The result of selectRoleByUsername(String username) -> empty");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void selectResourceByUsername()
            throws JsonProcessingException {
        SecurityUser newEntity = getEntityForTest();

        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");

        //===== findByUsername()
        List<Map<String, Object>> result = repository.selectResourceByUsername(newEntity.getUsername());
        Assert.isTrue(null != result && !result.isEmpty()
                , "The result of selectResourceByUsername(String username) -> empty");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void selectURLByUsername()
            throws JsonProcessingException {
        SecurityUser newEntity = getEntityForTest();

        //===== saveAndFlush()
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");

        //===== findByUsername()
        List<Map<String, Object>> result = repository.selectURLByUsername(newEntity.getUsername());
        Assert.isTrue(null != result && !result.isEmpty()
                , "The result of selectURLByUsername(String username) -> empty");
        System.out.println(toJSONString.writeValueAsString(result));
    }

    @Test
    @Transactional
    public void saveAndFlush() {
        SecurityUser newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");
        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void deleteById() {
        SecurityUser newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(User) -> 无效的 User");

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
    public void removeByUsername() {
        SecurityUser newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal()
                , "User.Factory.USER.create(..) -> 无效的 User");

        //=== insert
        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
                , "===== insert - insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(User) -> 无效的 User");

        //=== delete
        try {
            repository.removeByUsername(newEntity.getUsername());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - delete(String id) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - delete(String id) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

}
