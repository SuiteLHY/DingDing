package github.com.suitelhy.dingding.sso.authorization.domain.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.repository.UserRepository;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Human;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
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
public class UserRepositoryTests {

    @Autowired
    private ObjectMapper toJSONString;

    @Autowired
    private UserRepository userRepository;

    @NotNull
    private User getEntityForTest() {
        return User.Factory.USER.create(20
                , new CalendarController().toString()
                , ip()
                , new CalendarController().toString()
                , "测试用户"
                , "test123"
                , "测试数据"
                , null
                , ("测试" + new CalendarController().toString().replaceAll("[-:\\s]", ""))
                , Human.SexVo.MALE);
    }

    @NotNull
    private String ip() {
        return "127.0.0.0";
    }

    @Test
    @Transactional
    public void contextLoads() {
        Assert.notNull(userRepository, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void count() {
        long result;
        Assert.isTrue((result = userRepository.count()) > 0
                , "The result of count() equaled to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAllByPage()
            throws JsonProcessingException {
        final Page<User> result;

        Sort.TypedSort<User> userTypedSort = Sort.sort(User.class);
        Sort userSort = userTypedSort.by(User::getUserid).ascending();
        Pageable page = PageRequest.of(0, 10, userSort);

        Assert.notNull(result = userRepository.findAll(page)
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
    public void findAll() {
        List<User> result;
        Assert.notEmpty(result = userRepository.findAll()
                , "The result of count() equaled to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findById() {
        //===== 添加测试数据
        User newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal(), "User.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = userRepository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");

        //===== findById(..)
        Optional<User> result = userRepository.findById(newEntity.getUserid());
        Assert.notNull(result.get()
                , "The result of findById(String userid) -> empty");

        System.out.println(result);
    }

    @Test
    @Transactional
    public void saveAndFlush() {
        User newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal(), "User.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newEntity = userRepository.saveAndFlush(newEntity)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert(User) -> 无效的 User");

        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void modifyByIdAndStatus() {
        //=== 添加测试数据
        User newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal(), "getEntityForTest() -> 无效的 Entity");
        Assert.notNull(newEntity = userRepository.saveAndFlush(newEntity)
                , "===== 添加测试数据 -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== 添加测试数据 -> 无效的 Entity");

        //=== modifyByIdAndStatus(...)
        Assert.isTrue(userRepository.modifyByIdAndStatus("测试_最新"
                        , newEntity.getUserid()
                        , Account.StatusVo.NORMAL) > 0
                , "===== modifyByIdAndStatus(String, String, AccountVo.Status) -> fault");

        System.out.println(newEntity);
    }

    /**
     * 不建议使用的基础接口
     */
    @Test
    @Transactional
    public void delete() {
        User newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal(), "User.Factory.USER.create(..) -> 无效的 User");
        //=== insert
        Assert.notNull(newEntity = userRepository.saveAndFlush(newEntity)
                , "===== insert - insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(User) -> 无效的 User");
        //=== delete
        try {
            userRepository.delete(newEntity);
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - delete(User user) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - delete(User user) -> the given entity is non persistent.");
        }
        System.out.println(newEntity);
    }

    @Test
    @Transactional
    public void deleteById() {
        User newEntity = getEntityForTest();
        Assert.isTrue(newEntity.isEntityLegal(), "User.Factory.USER.create(..) -> 无效的 User");
        //=== insert
        Assert.notNull(newEntity = userRepository.saveAndFlush(newEntity)
                , "===== insert - insert(User) -> unexpected");
        Assert.isTrue(!newEntity.isEmpty()
                , "===== insert - insert(User) -> 无效的 User");
        //=== delete
        try {
            userRepository.deleteById(newEntity.getUserid());
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
