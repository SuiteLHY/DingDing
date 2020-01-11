package github.com.suitelhy.webchat.domain;

import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.domain.repository.UserRepository;
import github.com.suitelhy.webchat.domain.vo.AccountVo;
import github.com.suitelhy.webchat.domain.vo.HumanVo;
import github.com.suitelhy.webchat.infrastructure.util.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void contextLoads() {
        Assert.notNull(userRepository, "获取测试单元失败");
        // 批量添加测试数据
        for (int i = 0; i < 10; i++) {
            saveAndFlush();
        }
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
    public void findAllByPage() {
        Page<User> result;
        Sort.TypedSort<User> userTypedSort = Sort.sort(User.class);
        Sort userSort = userTypedSort.by(User::getUserid).ascending();
        Pageable page = PageRequest.of(0, 10, userSort);
        Assert.notNull(result = userRepository.findAll(page)
                , "The result of findAll(Pageable) equaled to or less than 0");
        Assert.notEmpty(result.getContent()
                , "The result of result.getContent() -> empty");
        System.out.println(result);
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
        Optional<User> result = userRepository.findById(/*"admin"*/"402880e56f89643e016f89645d8c0000");
        Assert.notNull(result.get()
                , "The result of findById(String userid) -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void saveAndFlush() {
        User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.1"
                , new CalendarController().toString()
                , "测试"
                , "a12345678"
                , "测试数据"
                , null
                , HumanVo.Sex.MALE);
        Assert.isTrue(newUser.isLegal(), "User.Factory.USER.create(..) -> 无效的 User");
        Assert.notNull(newUser = userRepository.saveAndFlush(newUser)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert(User) -> 无效的 User");
        System.out.println(newUser);
    }

    @Test
    @Transactional
    public void modifyByIdAndStatus() {
        User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.1"
                , new CalendarController().toString()
                , "测试"
                , "a12345678"
                , "测试数据"
                , null
                , HumanVo.Sex.MALE);
        Assert.isTrue(newUser.isLegal(), "User.Factory.USER.create(..) -> 无效的 User");
        //=== insert
        Assert.notNull(newUser = userRepository.saveAndFlush(newUser)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert(User) -> 无效的 User");
        //--- update
        Assert.isTrue(userRepository.modifyByIdAndStatus("测试_最新"
                        , newUser.id()
                        , AccountVo.Status.NORMAL) > 0
                , "===== modifyByIdAndStatus(String, String, AccountVo.Status) -> fault");
        System.out.println(newUser);
    }

    @Test
    @Transactional
    /**
     * 不建议使用的基础接口
     */
    public void delete() {
        User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.1"
                , new CalendarController().toString()
                , "测试"
                , "a12345678"
                , "测试数据"
                , null
                , HumanVo.Sex.MALE);
        Assert.isTrue(newUser.isLegal(), "User.Factory.USER.create(..) -> 无效的 User");
        //=== insert
        Assert.notNull(newUser = userRepository.saveAndFlush(newUser)
                , "===== insert - insert(User) -> unexpected");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert - insert(User) -> 无效的 User");
        //=== delete
        try {
            userRepository.delete(newUser);
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - delete(User user) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - delete(User user) -> the given entity is non persistent.");
        }
        System.out.println(newUser);
    }

    @Test
    @Transactional
    public void deleteById() {
        User newUser = User.Factory.USER.create(20
                , new CalendarController().toString()
                , "127.0.0.1"
                , new CalendarController().toString()
                , "测试"
                , "a12345678"
                , "测试数据"
                , null
                , HumanVo.Sex.MALE);
        Assert.isTrue(newUser.isLegal(), "User.Factory.USER.create(..) -> 无效的 User");
        //=== insert
        Assert.notNull(newUser = userRepository.saveAndFlush(newUser)
                , "===== insert - insert(User) -> unexpected");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert - insert(User) -> 无效的 User");
        //=== delete
        try {
            userRepository.deleteById(newUser.id());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== delete - delete(String id) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== delete - delete(String id) -> the given entity is non persistent.");
        }
        System.out.println(newUser);
    }

}
