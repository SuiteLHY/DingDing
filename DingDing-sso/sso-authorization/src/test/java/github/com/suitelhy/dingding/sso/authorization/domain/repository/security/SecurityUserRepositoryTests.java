//package github.com.suitelhy.dingding.sso.authorization.domain.repository.security;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRepository;
//import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
//import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
//import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
//import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
//import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
//import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
//import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
//import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.lang.Nullable;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Assert;
//
//import javax.validation.constraints.NotNull;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@SpringBootTest
//public class SecurityUserRepositoryTests {
//
//    @Autowired
//    private ObjectMapper toJSONString;
//
//    @Autowired
//    private SecurityUserRepository repository;
//
//    @Autowired
//    private SecurityUserService service;
//
//    @Autowired
//    private SecurityRoleService roleService;
//
//    @Autowired
//    private SecurityResourceService resourceService;
//
//    @Autowired
//    private SecurityUserService securityUserService;
//
//    @Value("${dingding.security.client-id}")
//    private String clientId;
//
//    /**
//     * 获取(测试用的)操作者信息
//     *
//     * @return {@link SecurityUser}
//     */
//    @NotNull
//    private SecurityUser operator() {
//        return securityUserService.selectByUsername("admin");
//    }
//
//    private String getClientId() {
//        return this.clientId;
//    }
//
//    @NotNull
//    private SecurityUser getEntityForTest() {
//        return SecurityUser.Factory.USER.create("402880e56fb72000016fb72014fc0000"
//                , "测试20200118132850"
//                , VoUtil.getVoByValue(Account.StatusVo.class, 1));
//    }
//
//    @NotNull
//    private SecurityRole getRoleForTest() {
//        return getRoleForTest(null);
//    }
//
//    @NotNull
//    private SecurityRole getRoleForTest(@Nullable Integer seed) {
//        return SecurityRole.Factory.ROLE.create(
//                "test"
//                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
//                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
//                , "测试角色"
//                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
//                , "测试用数据");
//    }
//
//    @NotNull
//    private SecurityResource getResourceForTest() {
//        return getResourceForTest(null);
//    }
//
//    @NotNull
//    private SecurityResource getResourceForTest(@Nullable Integer seed) {
//        return SecurityResource.FactoryModel.RESOURCE.create(
//                "test"
//                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
//                                .concat((null == seed || seed < 0) ? "" : Integer.toString(seed)))
//                , null
//                , null
//                , "test".concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
//                , null
//                , 0
//                , Resource.TypeVo.MENU);
//    }
//
//    @Test
//    @Transactional
//    public void contextLoads() {
//        Assert.notNull(repository, "获取测试单元失败");
//    }
//
//    @Test
//    @Transactional
//    public void count() {
//        final long result;
//
//        Assert.isTrue((result = repository.count()) > 0
//                , "The result equals to or less than 0");
//
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void findAllByPage()
//            throws JsonProcessingException {
//        final Page<SecurityUser> result;
//
//        Sort.TypedSort<SecurityUser> typedSort = Sort.sort(SecurityUser.class);
//        Sort sort = typedSort.by(SecurityUser::getUsername).ascending();
//        Pageable page = PageRequest.of(0, 10, sort);
//
//        Assert.notNull(result = repository.findAll(page)
//                , "The result equals to or less than 0");
//        Assert.notEmpty(result.getContent()
//                , "The result -> empty");
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
//    public void findAll()
//            throws JsonProcessingException {
//        List<SecurityUser> result;
//        Assert.notEmpty(result = repository.findAll()
//                , "The result equals to or less than 0");
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void findById() {
//        //===== 添加测试数据
//        SecurityUser newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //===== findById(..)
//        Optional<SecurityUser> result = repository.findById(newEntity.getUserId());
//
//        Assert.notNull(result.get()
//                , "The result -> empty");
//
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void findByUsername() {
//        //===== 添加测试数据
//        SecurityUser newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest(..) -> 无效的 User");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 User");
//
//        //===== findByUsername()
//        Optional<SecurityUser> result = repository.findByUsername(newEntity.getUsername());
//
//        Assert.notNull(result.get()
//                , "The result -> empty");
//
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void selectRoleByUsername()
//            throws JsonProcessingException, BusinessAtomicException
//    {
//        //===== 添加测试数据
//        final SecurityUser newEntity = getEntityForTest();
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        final SecurityRole newRole = getRoleForTest();
//        Assert.isTrue(newRole.isEntityLegal()
//                , "===== getRoleForTest() -> 无效的 Role");
//        Assert.isTrue(roleService.insert(newRole, operator())
//                , "===== 添加测试数据 -> false");
//        Assert.isTrue(!newRole.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Role");
//
//        Assert.isTrue(service.insertRole(newEntity, newRole, operator())
//                , "===== 添加测试数据 -> {新增 用户 - 角色 关联} 操作失败");
//
//        //===== selectRoleByUsername(..)
//        List<Map<String, Object>> result = repository.selectRoleByUsername(newEntity.getUsername());
//
//        Assert.isTrue(null != result && !result.isEmpty()
//                , "The result -> empty");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void selectResourceByUsername()
//            throws Exception {
//        final List<Map<String, Object>> result;
//
//        //===== 添加测试数据
//        final SecurityUser newEntity = getEntityForTest();
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        final SecurityRole newRole = getRoleForTest();
//        Assert.isTrue(newRole.isEntityLegal()
//                , "===== getRoleForTest() -> 无效的 Role");
//        Assert.isTrue(roleService.insert(newRole, operator())
//                , "===== 添加测试数据 -> false");
//        Assert.isTrue(!newRole.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Role");
//
//        Assert.isTrue(service.insertRole(newEntity, newRole, operator())
//                , "===== 添加测试数据 -> {新增 用户 - 角色 关联} 操作失败");
//
//        final SecurityResource newResource = getResourceForTest();
//        Assert.isTrue(newResource.isEntityLegal()
//                , "===== getResourceForTest() -> 无效的 Resource");
//        Assert.isTrue(resourceService.insert(newResource, operator())
//                , "===== 添加测试数据 -> false");
//        Assert.isTrue(!newResource.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Resource");
//
//        Assert.isTrue(roleService.insertResource(newRole, newResource, operator())
//                , "===== 添加测试数据 -> {新增 角色 - 资源 关联} 操作失败");
//
//        //===== selectResourceByUsername(..)
//        result = repository.selectResourceByUsername(newEntity.getUsername());
//
//        Assert.isTrue(null != result && !result.isEmpty()
//                , "The result -> empty");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void selectURLByUsernameAndClientId()
//            throws JsonProcessingException {
//        //===== 添加测试数据
//        SecurityUser newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //===== selectURLByUsernameAndClientId(..)
//        List<Map<String, Object>> result = repository.selectURLByUsernameAndClientId(newEntity.getUsername()
//                , getClientId());
//
//        Assert.isTrue(null != result && !result.isEmpty()
//                , "The result -> empty");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void saveAndFlush() {
//        SecurityUser newEntity = getEntityForTest();
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== insert(User) -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== insert(User) -> 无效的 User");
//        System.out.println(newEntity);
//    }
//
//    @Test
//    @Transactional
//    public void deleteById() {
//        SecurityUser newEntity = getEntityForTest();
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "SecurityUser.Factory.USER.create(..) -> 无效的 User");
//
//        //=== insert
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== insert - insert(User) -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== insert - insert(User) -> 无效的 User");
//
//        //=== delete
//        try {
//            repository.deleteById(newEntity.getUserId());
//        } catch (IllegalArgumentException e) {
//            Assert.state(false
//                    , "===== delete - delete(String id) -> the given entity is null.");
//        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
//            Assert.state(false
//                    , "===== delete - delete(String id) -> the given entity is non persistent.");
//        }
//        System.out.println(newEntity);
//    }
//
//    @Test
//    @Transactional
//    public void removeByUsername() {
//        SecurityUser newEntity = getEntityForTest();
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "User.Factory.USER.create(..) -> 无效的 User");
//
//        //=== insert
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== insert - insert(User) -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== insert - insert(User) -> 无效的 User");
//
//        //=== delete
//        try {
//            repository.removeByUsername(newEntity.getUsername());
//        } catch (IllegalArgumentException e) {
//            Assert.state(false
//                    , "===== delete - delete(String id) -> the given entity is null.");
//        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
//            Assert.state(false
//                    , "===== delete - delete(String id) -> the given entity is non persistent.");
//        }
//        System.out.println(newEntity);
//    }
//
//}
