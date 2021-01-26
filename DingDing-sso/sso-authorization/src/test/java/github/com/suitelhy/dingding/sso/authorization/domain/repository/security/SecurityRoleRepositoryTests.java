//package github.com.suitelhy.dingding.sso.authorization.domain.repository.security;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityResourceUrlRepository;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleRepository;
//import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
//import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
//import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
//import github.com.suitelhy.dingding.core.infrastructure.web.vo.HTTP;
//import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
//import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Assert;
//
//import javax.validation.constraints.NotNull;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
///**
// * @see SecurityRoleRepository
// */
//@SpringBootTest
//public class SecurityRoleRepositoryTests {
//
//    @Autowired
//    private ObjectMapper toJSONString;
//
//    @Autowired
//    private SecurityRoleRepository repository;
//
//    @Autowired
//    private SecurityResourceUrlRepository securityResourceUrlRepository;
//
//    @Autowired
//    private SecurityRoleService service;
//
//    @Autowired
//    private SecurityResourceService securityResourceService;
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
//    private SecurityRole getEntityForTest() {
//        return SecurityRole.Factory.ROLE.create("test"
//                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
//                , "测试角色"
//                , "测试用数据");
//    }
//
//    @NotNull
//    private SecurityResource getResourceForTest() {
//        return SecurityResource.FactoryModel.RESOURCE.create("test"
//                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
//                , null
//                , null
//                , "test"
//                , null
//                , 0
//                , Resource.TypeVo.MENU);
//    }
//
//    @NotNull
//    private String getUrlForTest() {
//        return "/test/test"
//                .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""));
//    }
//
//    @NotNull
//    private String getUrlHttpMethodNameForTest() {
//        return HTTP.MethodVo.GET.name();
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
//    public void countByName()
//            throws JsonProcessingException
//    {
//        //===== 添加测试数据
//        SecurityRole newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //===== countByName()
//        long result;
//        Assert.isTrue((result = repository.countByName(newEntity.getName())) > 0
//                , "The result equals to or less than 0");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void findAllByPage()
//            throws JsonProcessingException {
//        final Page<SecurityRole> result;
//
//        Sort.TypedSort<SecurityRole> typedSort = Sort.sort(SecurityRole.class);
//        Sort sort = typedSort.by(SecurityRole::getCode).ascending();
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
//        final List<SecurityRole> result;
//
//        Assert.notEmpty(result = repository.findAll()
//                , "The result equals to or less than 0");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void findById()
//            throws JsonProcessingException {
//        //===== 添加测试数据
//        SecurityRole newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //===== findById(..)
//        Optional<SecurityRole> result = repository.findById(newEntity.getId());
//
//        Assert.notNull(result.get()
//                , "The result of findById(String userId) -> empty");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void findByCode()
//            throws JsonProcessingException {
//        //===== 添加测试数据
//        SecurityRole newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //===== findByUsername()
//        Optional<SecurityRole> result = repository.findByCode(newEntity.getCode());
//        Assert.notNull(result.get()
//                , "The result -> empty");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void findByName()
//            throws JsonProcessingException {
//        //===== 添加测试数据
//        SecurityRole newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //===== findByUsername()
//        Sort.TypedSort<SecurityRole> typedSort = Sort.sort(SecurityRole.class);
//        Sort sort = typedSort.by(SecurityRole::getCode).ascending();
//        Pageable page = PageRequest.of(0, 10, sort);
//
//        List<SecurityRole> result = repository.findByName(newEntity.getName(), page);
//        Assert.isTrue(null != result && !result.isEmpty()
//                , "The result -> empty");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void selectResourceByCode()
//            throws Exception
//    {
//        // 获取必要的测试用身份信息
//        final SecurityUser operator = operator();
//
//        //===== 添加测试数据
//        SecurityRole newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getRoleForTest() -> 无效的 SecurityRole");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        final SecurityResource newResource = getResourceForTest();
//        Assert.isTrue(securityResourceService.insert(newResource, operator)
//                , "===== 添加测试数据 -> {新增资源} 操作失败!");
//
//        Assert.isTrue(service.insertResource(newEntity, newResource, operator)
//                , "===== 添加测试数据 -> {新增 角色 - 资源 关联} 操作失败!");
//
//        //===== selectResourceByCode()
//        List<Map<String, Object>> result = repository.selectResourceByCode(newEntity.getCode());
//        Assert.isTrue(null != result && !result.isEmpty()
//                , "The result -> empty");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void selectUrlByCode()
//            throws Exception
//    {
//        // 获取必要的测试用身份信息
//        final SecurityUser operator = operator();
//
//        //===== 添加测试数据
//        SecurityRole newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        final SecurityResource newResource = getResourceForTest();
//        Assert.isTrue(securityResourceService.insert(newResource, operator)
//                , "===== 添加测试数据 -> {新增资源} 操作失败!");
//
//        Assert.isTrue(service.insertResource(newEntity, newResource, operator)
//                , "===== 添加测试数据 -> {新增 角色 - 资源 关联} 操作失败!");
//
//        final SecurityResourceUrl newResourceUrl = securityResourceUrlRepository.saveAndFlush(
//                SecurityResourceUrl.FactoryModel.RESOURCE_URL.create(newResource.getCode()
//                        , getClientId()
//                        , getUrlForTest()
//                        , getUrlHttpMethodNameForTest())
//        );
//        Assert.isTrue(!newResourceUrl.isEmpty()
//                , "===== 添加测试数据 -> {新增资源 - URL 关联} 操作失败!");
//
//        //===== selectUrlByCode(...)
//        List<Map<String, Object>> result = repository.selectUrlByCode(newEntity.getCode());
//        Assert.isTrue(null != result && !result.isEmpty()
//                , "The result -> empty");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void saveAndFlush() {
//        SecurityRole newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "getRoleForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== saveAndFlush(newEntity) -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== saveAndFlush(newEntity) -> 无效的 Entity");
//
//        System.out.println(newEntity);
//    }
//
//    @Test
//    @Transactional
//    public void deleteById() {
//        //=== 添加测试数据
//        SecurityRole newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "getRoleForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //=== deleteById(...)
//        try {
//            repository.deleteById(newEntity.getId());
//        } catch (IllegalArgumentException e) {
//            Assert.state(false
//                    , "===== deleteById(String id) -> the given entity is null.");
//        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
//            Assert.state(false
//                    , "===== deleteById(String id) -> the given entity is non persistent.");
//        }
//
//        System.out.println(newEntity);
//    }
//
//    @Test
//    @Transactional
//    public void removeByCode() {
//        //=== 添加测试数据
//        SecurityRole newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "getRoleForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //=== removeByCode(...)
//        try {
//            repository.removeByCode(newEntity.getCode());
//        } catch (IllegalArgumentException e) {
//            Assert.state(false
//                    , "===== removeByCode(String code) -> the given entity is null.");
//        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
//            Assert.state(false
//                    , "===== removeByCode(String code) -> the given entity is non persistent.");
//        }
//
//        System.out.println(newEntity);
//    }
//
//}
