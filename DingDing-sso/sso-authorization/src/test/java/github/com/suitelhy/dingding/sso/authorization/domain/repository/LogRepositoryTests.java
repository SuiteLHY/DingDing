//package github.com.suitelhy.dingding.sso.authorization.domain.repository;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import github.com.suitelhy.dingding.core.domain.entity.Log;
//import github.com.suitelhy.dingding.core.domain.entity.User;
//import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
//import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
//import github.com.suitelhy.dingding.core.domain.repository.LogRepository;
//import github.com.suitelhy.dingding.core.domain.service.UserService;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
//import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
//import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
//import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Assert;
//
//import javax.validation.constraints.NotNull;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//public class LogRepositoryTests {
//
//    @Autowired
//    private ObjectMapper toJSONString;
//
//    @Autowired
//    private LogRepository logRepository;
//
//    @Autowired
//    private UserService userService;
//
//    /**
//     * 获取操作者信息
//     *
//     * @return {@link SecurityUser}
//     */
//    @NotNull
//    private SecurityUser operator() {
//        return SecurityUser.Factory.USER.create(userService.selectUserByUsername("admin"));
//    }
//
////    @NotNull
////    private User getEntityForTest() {
////        /*return User.Factory.USER.create(20
////                , new CalendarController().toString()
////                , ip()
////                , new CalendarController().toString()
////                , "测试用户"
////                , "test123"
////                , "测试数据"
////                , null
////                , ("测试" + new CalendarController().toString().replaceAll("[-:\\s]", ""))
////                , Human.SexVo.MALE);*/
////        return User.Factory.USER.create(
////                "测试".concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
////                , "test123");
////    }
//
//    /**
//     * 获取测试用的用户相关 {@link EntityModel} 集合
//     *
//     * @return {@link this#getEntityForTest(Integer)}
//     */
//    @NotNull
//    private Map<String, EntityModel> getEntityForTest() {
//        return getEntityForTest(null);
//    }
//
//    /**
//     * 获取测试用的用户相关 {@link EntityModel} 集合
//     *
//     * @param seed
//     *
//     * @return {@link Map}
//     * · 数据结构:
//     *-> {
//     *->    "user": {@link User},
//     *->    "userAccountOperationInfo": {@link UserAccountOperationInfo},
//     *->    "userPersonInfo": {@link UserPersonInfo}
//     *-> }
//     */
//    @NotNull
//    private Map<String, EntityModel> getEntityForTest(Integer seed) {
//        Map<String, EntityModel> result = new HashMap<>(3);
//
//        User newUser = User.Factory.USER.create(
//                "测试用户".concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
//                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
//                , "test123"
//        );
//
//        String currentTime = new CalendarController().toString();
//        UserAccountOperationInfo userAccountOperationInfo = UserAccountOperationInfo.Factory.USER.create(
//                newUser.getUsername()
//                , "127.0.0.1"
//                , currentTime
//                , currentTime
//        );
//
//        UserPersonInfo userPersonInfo = UserPersonInfo.Factory.USER.create(
//                newUser.getUsername()
//                , "测试用户"
//                , null
//                , null
//                , null
//                , null
//        );
//
//        result.put("user", newUser);
//        result.put("userAccountOperationInfo", userAccountOperationInfo);
//        result.put("userPersonInfo", userPersonInfo);
//
//        return result;
//    }
//
//    @NotNull
//    private String ip() {
//        return "127.0.0.0";
//    }
//
//    @Test
//    @Transactional
//    public void contextLoads() {
//        Assert.notNull(logRepository, "获取测试单元失败");
//    }
//
//    @Test
//    @Transactional
//    public void count() {
//        long result;
//        Assert.isTrue((result = logRepository.count()) > 0
//                , "The result of count() equal to or less than 0");
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void countByUsername() {
//        //===== findAll(Pageable) =====//
//        Page<Log> logPage;
//        Sort.TypedSort<Log> logTypedSort = Sort.sort(Log.class);
//        Sort logSort = logTypedSort.by(Log::getUsername).ascending()
//                .and(logTypedSort.by(Log::getId).ascending());
//        Pageable page = PageRequest.of(0, 10, logSort);
//        Assert.notNull(logPage = logRepository.findAll(page)
//                , "The result of findAll(Pageable) equal to or less than 0");
//        Assert.notEmpty(logPage.getContent()
//                , "The result of logPage.getContent() -> empty");
//
//        //===== countByUsername(String username) =====//
//        String username = logPage.getContent().get(0).getUsername();
//        long result;
//        Assert.isTrue((result = logRepository.countByUsername(username)) > 0
//                , "The result of countByUsername(String username) equaled to or less than 0");
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void findAll()
//            throws JsonProcessingException
//    {
//        Page<Log> result;
//        Sort.TypedSort<Log> logTypedSort = Sort.sort(Log.class);
//        Sort logSort = logTypedSort.by(Log::getUsername).ascending()
//                .and(logTypedSort.by(Log::getId).ascending());
//        Pageable page = PageRequest.of(0, 10, logSort);
//
//        Assert.notNull(result = logRepository.findAll(page)
//                , "The result of findAll(Pageable) equal to or less than 0");
//        Assert.notEmpty(result.getContent()
//                , "The result of result.getContent() -> empty");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//        System.out.println(result.getContent());
//        System.out.println(result.getTotalElements());
//        System.out.println(result.getTotalPages());
//        System.out.println(result.getNumber());
//        System.out.println(result.getSize());
//        System.out.println(result.getSort());
//    }
//
//    @Test
//    @Transactional
//    public void findByUsername() {
//        //===== findAll(Pageable) =====//
//        Page<Log> logPage;
//        Sort.TypedSort<Log> logTypedSort = Sort.sort(Log.class);
//        Sort logSort = logTypedSort.by(Log::getUsername).ascending()
//                .and(logTypedSort.by(Log::getId).ascending());
//        Pageable page = PageRequest.of(0, 10, logSort);
//
//        Assert.notNull(logPage = logRepository.findAll(page)
//                , "The result of findAll(Pageable) equal to or less than 0");
//        Assert.notEmpty(logPage.getContent()
//                , "The result of logPage.getContent() -> empty");
//
//        //===== findByUsername(String username, Pageable pageable) =====//
//        List<Log> result = logRepository.findByUsername(
//                logPage.getContent().get(0).getUsername()
//                , page);
//        Assert.notEmpty(result
//                , "The result of findByUsername(String username, Pageable pageable) -> empty");
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void saveAndFlush()
//            throws BusinessAtomicException
//    {
//        // 获取必要的测试用身份信息
//        final SecurityUser operator = operator();
//
//        //===== userService =====//
//
//        final @NotNull Map<String, EntityModel> newEntity = getEntityForTest();
//
//        Assert.isTrue(((User) newEntity.get("user")).isEntityLegal()
//                , "User.Factory.USER.create(..) -> 无效的 User");
//        Assert.isTrue(userService.insert((User) newEntity.get("user")
//                    , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
//                    , (UserPersonInfo) newEntity.get("userPersonInfo")
//                    , operator)
//                , "===== insert(User) -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== insert(User) -> 无效的 User");
//        System.out.println("newEntity: " + newEntity);
//
//        //===== logRepository =====//
//
//        Log newLog = Log.Factory.User.LOG.create(null
//                , ((UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")).getIp()
//                , HandleType.LogVo.USER__USER__ADD
//                , (User) newEntity.get("user")
//                , operator
//        );
//        Assert.isTrue(newLog.isEntityLegal()
//                , "Log.Factory.USER_LOG.create(..) -> 无效的 Log");
//        Assert.notNull(newLog = logRepository.saveAndFlush(newLog)
//                , "===== saveAndFlush(Log) -> unexpected");
//        Assert.isTrue(!newLog.isEmpty()
//                , "===== saveAndFlush(Log) -> 无效的 Log");
//        System.out.println("newLog: " + newLog);
//    }
//
//    @Test
//    @Transactional
//    public void deleteById()
//            throws BusinessAtomicException
//    {
//        // 获取必要的测试用身份信息
//        final SecurityUser operator = operator();
//
//        //===== userService =====//
//        final @NotNull Map<String, EntityModel> newEntity = getEntityForTest();
//
//        Assert.isTrue(((User) newEntity.get("user")).isEntityLegal()
//                , "User.Factory.USER.create(..) -> 无效的 User");
//        Assert.isTrue(userService.insert((User) newEntity.get("user")
//                    , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
//                    , (UserPersonInfo) newEntity.get("userPersonInfo")
//                    , operator)
//                , "===== insert(User) -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== insert(User) -> 无效的 User");
//        System.out.println("newEntity: " + newEntity);
//
//        //===== logRepository =====//
//
//        //=== saveAndFlush(Log log)
//        Log newLog = Log.Factory.User.LOG.create(null
//                , ((UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")).getIp()
//                , HandleType.LogVo.USER__USER__ADD
//                , (User) newEntity.get("user")
//                , operator
//        );
//        Assert.isTrue(newLog.isEntityLegal()
//                , "Log.Factory.USER_LOG.create(..) -> 无效的 Log");
//        Assert.notNull(newLog = logRepository.saveAndFlush(newLog)
//                , "===== saveAndFlush(Log) -> unexpected");
//        Assert.isTrue(!newLog.isEmpty()
//                , "===== saveAndFlush(Log) -> 无效的 Log");
//        System.out.println("newLog: " + newLog);
//
//        //=== deleteById(String id)
//
//        try {
//            logRepository.deleteById(newLog.id());
//        } catch (IllegalArgumentException e) {
//            Assert.state(false
//                    , "===== deleteById(String id) -> the given entity is null.");
//        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
//            Assert.state(false
//                    , "===== deleteById(String id) -> the given entity is non persistent.");
//        }
//        System.out.println(newLog);
//    }
//
//    @Test
//    @Transactional
//    public void removeByUserid()
//            throws BusinessAtomicException
//    {
//        // 获取必要的测试用身份信息
//        final SecurityUser operator = operator();
//
//        //===== userService =====//
//        final @NotNull Map<String, EntityModel> newEntity = getEntityForTest();
//
//        Assert.isTrue(((User) newEntity.get("user")).isEntityLegal()
//                , "getEntityForTest() -> 无效的 Entity");
//        Assert.isTrue(userService.insert((User) newEntity.get("user")
//                    , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
//                    , (UserPersonInfo) newEntity.get("userPersonInfo")
//                    , operator)
//                , "===== insert(User) -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== insert(User) -> 无效的 Entity");
//        System.out.println("newEntity: " + newEntity);
//
//        //===== logRepository =====//
//
//        //=== saveAndFlush(Log log)
//        Log newLog = Log.Factory.User.LOG.create(null
//                , ((UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")).getIp()
//                , HandleType.LogVo.USER__USER__ADD
//                , (User) newEntity.get("user")
//                , operator
//        );
//        Assert.isTrue(newLog.isEntityLegal()
//                , "Log.Factory.USER_LOG.create(..) -> 无效的 Log");
//        Assert.notNull(newLog = logRepository.saveAndFlush(newLog)
//                , "===== saveAndFlush(Log) -> unexpected");
//        Assert.isTrue(!newLog.isEmpty()
//                , "===== saveAndFlush(Log) -> 无效的 Log");
//        System.out.println("newLog: " + newLog);
//
//        //=== removeByUserid(String userid)
//
//        long result;
//        Assert.isTrue((result = logRepository.removeByUsername(newLog.getUsername())) > 0
//                , "===== The result is equal to or less than 0");
//        System.out.println(result);
//    }
//
//}
