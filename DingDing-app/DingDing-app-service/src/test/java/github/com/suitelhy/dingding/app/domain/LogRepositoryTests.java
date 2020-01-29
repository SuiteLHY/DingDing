package github.com.suitelhy.dingding.app.domain;

import github.com.suitelhy.dingding.app.domain.entity.Log;
import github.com.suitelhy.dingding.app.domain.entity.User;
import github.com.suitelhy.dingding.app.domain.repository.LogRepository;
import github.com.suitelhy.dingding.app.domain.service.UserService;
import github.com.suitelhy.dingding.app.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.app.infrastructure.domain.vo.Human;
import github.com.suitelhy.dingding.app.infrastructure.util.CalendarController;
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

@SpringBootTest
public class LogRepositoryTests {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserService userService;

    @NotNull
    private User getUserForTest() {
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
        Assert.notNull(logRepository, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void count() {
        long result;
        Assert.isTrue((result = logRepository.count()) > 0
                , "The result of count() equal to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void countByUserid() {
        //===== findAll(Pageable) =====//
        Page<Log> logPage;
        Sort.TypedSort<Log> logTypedSort = Sort.sort(Log.class);
        Sort logSort = logTypedSort.by(Log::getUserid).ascending()
                .and(logTypedSort.by(Log::getId).ascending());
        Pageable page = PageRequest.of(0, 10, logSort);
        Assert.notNull(logPage = logRepository.findAll(page)
                , "The result of findAll(Pageable) equal to or less than 0");
        Assert.notEmpty(logPage.getContent()
                , "The result of logPage.getContent() -> empty");

        //===== countByUserid(String userid) =====//
        String userid = logPage.getContent().get(0).getUserid();
        long result;
        Assert.isTrue((result = logRepository.countByUserid(userid)) > 0
                , "The result of countByUserid(String userid) equaled to or less than 0");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void findAll() {
        Page<Log> result;
        Sort.TypedSort<Log> logTypedSort = Sort.sort(Log.class);
        Sort logSort = logTypedSort.by(Log::getUserid).ascending()
                .and(logTypedSort.by(Log::getId).ascending());
        Pageable page = PageRequest.of(0, 10, logSort);
        Assert.notNull(result = logRepository.findAll(page)
                , "The result of findAll(Pageable) equal to or less than 0");
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
    public void findByUserid() {
        //===== findAll(Pageable) =====//
        Page<Log> logPage;
        Sort.TypedSort<Log> logTypedSort = Sort.sort(Log.class);
        Sort logSort = logTypedSort.by(Log::getUserid).ascending()
                .and(logTypedSort.by(Log::getId).ascending());
        Pageable page = PageRequest.of(0, 10, logSort);
        Assert.notNull(logPage = logRepository.findAll(page)
                , "The result of findAll(Pageable) equal to or less than 0");
        Assert.notEmpty(logPage.getContent()
                , "The result of logPage.getContent() -> empty");

        //===== findByUserid(String userid, Pageable pageable) =====//
        List<Log> result = logRepository.findByUserid(
                logPage.getContent().get(0).getUserid()
                , page);
        Assert.notEmpty(result
                , "The result of findByUserid(String userid, Pageable pageable) -> empty");
        System.out.println(result);
    }

    @Test
    @Transactional
    public void saveAndFlush() {
        //===== userService =====//
        User newUser = getUserForTest();
        Assert.isTrue(newUser.isEntityLegal()
                , "User.Factory.USER.create(..) -> 无效的 User");
        Assert.isTrue(userService.insert(newUser)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert(User) -> 无效的 User");
        System.out.println("newUser: " + newUser);

        //===== logRepository =====//
        Log newLog = Log.Factory.USER_LOG.create(null
                , newUser.getIp()
                , new CalendarController().toString()
                , HandleType.LogVo.USER_REGISTRATION
                , newUser.getUserid()
        );
        Assert.isTrue(newLog.isEntityLegal()
                , "Log.Factory.USER_LOG.create(..) -> 无效的 Log");
        Assert.notNull(newLog = logRepository.saveAndFlush(newLog)
                , "===== saveAndFlush(Log) -> unexpected");
        Assert.isTrue(!newLog.isEmpty()
                , "===== saveAndFlush(Log) -> 无效的 Log");
        System.out.println("newLog: " + newLog);
    }

    @Test
    @Transactional
    public void deleteById() {
        //===== userService =====//
        User newUser = getUserForTest();
        Assert.isTrue(newUser.isEntityLegal()
                , "User.Factory.USER.create(..) -> 无效的 User");
        Assert.isTrue(userService.insert(newUser)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert(User) -> 无效的 User");
        System.out.println("newUser: " + newUser);

        //===== logRepository =====//
        //=== saveAndFlush(Log log)
        Log newLog = Log.Factory.USER_LOG.create(null
                , newUser.getIp()
                , new CalendarController().toString()
                , HandleType.LogVo.USER_REGISTRATION
                , newUser.getUserid()
        );
        Assert.isTrue(newLog.isEntityLegal()
                , "Log.Factory.USER_LOG.create(..) -> 无效的 Log");
        Assert.notNull(newLog = logRepository.saveAndFlush(newLog)
                , "===== saveAndFlush(Log) -> unexpected");
        Assert.isTrue(!newLog.isEmpty()
                , "===== saveAndFlush(Log) -> 无效的 Log");
        System.out.println("newLog: " + newLog);

        //=== deleteById(String id)
        try {
            logRepository.deleteById(newLog.id());
        } catch (IllegalArgumentException e) {
            Assert.state(false
                    , "===== deleteById(String id) -> the given entity is null.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assert.state(false
                    , "===== deleteById(String id) -> the given entity is non persistent.");
        }
        System.out.println(newLog);
    }

    @Test
    @Transactional
    public void removeByUserid() {
        //===== userService =====//
        User newUser = getUserForTest();
        Assert.isTrue(newUser.isEntityLegal()
                , "User.Factory.USER.create(..) -> 无效的 User");
        Assert.isTrue(userService.insert(newUser)
                , "===== insert(User) -> unexpected");
        Assert.isTrue(!newUser.isEmpty()
                , "===== insert(User) -> 无效的 User");
        System.out.println("newUser: " + newUser);

        //===== logRepository =====//
        //=== saveAndFlush(Log log)
        Log newLog = Log.Factory.USER_LOG.create(null
                , newUser.getIp()
                , new CalendarController().toString()
                , HandleType.LogVo.USER_REGISTRATION
                , newUser.getUserid()
        );
        Assert.isTrue(newLog.isEntityLegal()
                , "Log.Factory.USER_LOG.create(..) -> 无效的 Log");
        Assert.notNull(newLog = logRepository.saveAndFlush(newLog)
                , "===== saveAndFlush(Log) -> unexpected");
        Assert.isTrue(!newLog.isEmpty()
                , "===== saveAndFlush(Log) -> 无效的 Log");
        System.out.println("newLog: " + newLog);

        //=== removeByUserid(String userid)
        long result;
        Assert.isTrue((result = logRepository.removeByUserid(newLog.getUserid())) > 0
                , "===== The result of removeByUserid(String userid) is equal to or less than 0");
        System.out.println(result);
    }

}
