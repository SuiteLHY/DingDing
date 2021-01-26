//package github.com.suitelhy.dingding.sso.authorization.domain.repository.security;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
//import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityResourceRepository;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityResourceUrlRepository;
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
//public class SecurityResourceRepositoryTests {
//
//    @Autowired
//    private ObjectMapper toJSONString;
//
//    @Autowired
//    private SecurityResourceRepository repository;
//
//    @Autowired
//    private SecurityResourceUrlRepository securityResourceUrlRepository;
//
//    @Value("${dingding.security.client-id}")
//    private String clientId;
//
//    private String getClientId() {
//        return this.clientId;
//    }
//
//    @NotNull
//    private SecurityResource getEntityForTest() {
//        return getEntityForTest(null);
//    }
//
//    @NotNull
//    private SecurityResource getEntityForTest(@Nullable Integer seed) {
//        return SecurityResource.FactoryModel.RESOURCE.create(
//                "test"
//                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
//                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
//                , null
//                , null
//                , "test"
//                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
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
//        long result;
//        Assert.isTrue((result = repository.count()) > 0
//                , "The result equaled to or less than 0");
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void countByParentCode() {
//        SecurityResource newEntity = getEntityForTest();
//        String parentCode = "test";
//
//        //===== 添加测试数据
//        Assert.isTrue((newEntity.setParentCode(parentCode) && newEntity.isEntityLegal())
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //===== countByParentCode(...)
//        long result;
//        Assert.isTrue((result = repository.countByParentCode(parentCode)) > 0
//                , "The result equals to or less than 0");
//
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void existsByCodeAndParentCode() {
//        SecurityResource newEntity = getEntityForTest();
//
//        //===== 添加测试数据
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //===== existsByCodeAndParentCode(...)
//        boolean result = repository.existsByCodeAndParentCode(newEntity.getCode()
//                , newEntity.getParentCode());
//        Assert.isTrue(result
//                , "The result -> not true");
//    }
//
//    @Test
//    @Transactional
//    public void findAll()
//            throws JsonProcessingException {
//        final List<SecurityResource> result;
//
//        Assert.notEmpty(result = repository.findAll()
//                , "The result equals to or less than 0");
//
//        System.out.println(toJSONString.writeValueAsString(result));
//    }
//
//    @Test
//    @Transactional
//    public void findAllByPage()
//            throws JsonProcessingException {
//        final Page<SecurityResource> result;
//
//        Sort.TypedSort<SecurityResource> typedSort = Sort.sort(SecurityResource.class);
//        Sort sort = typedSort.by(SecurityResource::getSort).descending()
//                .and(typedSort.by(SecurityResource::getCode).ascending());
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
//    public void findById() {
//        final Optional<SecurityResource> result;
//
//        //===== 添加测试数据
//        SecurityResource newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== insert(Entity) -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== insert(Entity) -> 无效的 Entity");
//
//        //===== findById(..)
//        result = repository.findById(newEntity.getId());
//
//        Assert.notNull(result.get()
//                , "The result -> empty");
//
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void findByCode() {
//        //===== 添加测试数据
//        SecurityResource newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== insert(Entity) -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== insert(Entity) -> 无效的 Entity");
//
//        //===== findByCode()
//        Optional<SecurityResource> result = repository.findByCode(newEntity.getCode());
//
//        Assert.notNull(result.get()
//                , "The result -> empty");
//
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void findAllByParentCode() {
//        final Page<SecurityResource> result;
//
//        //===== 添加测试数据
//        SecurityResource newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //===== findAllByParentCode()
//        Sort.TypedSort<SecurityResource> typedSort = Sort.sort(SecurityResource.class);
//        Sort sort = typedSort.by(SecurityResource::getSort).descending()
//                .and(typedSort.by(SecurityResource::getCode).ascending());
//        Pageable page = PageRequest.of(0, 10, sort);
//
//        result = repository.findAllByParentCode(newEntity.getParentCode(), page);
//
//        Assert.isTrue(!result.isEmpty()
//                , "The result -> empty");
//
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void selectUrlByCode()
//            throws JsonProcessingException {
//        final List<Map<String, Object>> result;
//
//        //===== 添加测试数据
//        final SecurityResource newEntity = getEntityForTest();
//        final String newUrl = getUrlForTest();
//        final SecurityResourceUrl newSecurityResourceUrl;
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        newSecurityResourceUrl = securityResourceUrlRepository.saveAndFlush(
//                SecurityResourceUrl.FactoryModel.RESOURCE_URL.create(newEntity.getCode(), getClientId(), newUrl
//                        , getUrlHttpMethodNameForTest())
//        );
//
//        Assert.isTrue(!newSecurityResourceUrl.isEmpty()
//                , "===== 添加测试数据 -> 无效的 SecurityResourceUrl");
//
//        //===== selectUrlByCode(...)
//        result = repository.selectUrlByCode(newSecurityResourceUrl.getCode());
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
//        SecurityResource newEntity = getEntityForTest();
//
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== insert(Entity) -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== insert(Entity) -> 无效的 Entity");
//
//        System.out.println(newEntity);
//    }
//
//    @Test
//    @Transactional
//    public void deleteById() {
//        SecurityResource newEntity = getEntityForTest();
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//
//        //=== 添加测试数据
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //=== delete
//        try {
//            repository.deleteById(newEntity.getId());
//        } catch (IllegalArgumentException e) {
//            Assert.state(false
//                    , "===== deleteById(...) -> the given entity is null.");
//        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
//            Assert.state(false
//                    , "===== deleteById(...) -> the given entity is non persistent.");
//        }
//        System.out.println(newEntity);
//    }
//
//    @Test
//    @Transactional
//    public void removeByCode() {
//        SecurityResource newEntity = getEntityForTest();
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//
//        //=== 添加测试数据
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
//                    , "===== removeByCode(...) -> the given entity is null.");
//        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
//            Assert.state(false
//                    , "===== removeByCode(...) -> the given entity is non persistent.");
//        }
//        System.out.println(newEntity);
//    }
//
//    @Test
//    @Transactional
//    public void removeByCodeAndParentCode() {
//        SecurityResource newEntity = getEntityForTest();
//        Assert.isTrue(newEntity.isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//
//        //=== 添加测试数据
//        Assert.notNull(newEntity = repository.saveAndFlush(newEntity)
//                , "===== 添加测试数据 -> unexpected");
//        Assert.isTrue(!newEntity.isEmpty()
//                , "===== 添加测试数据 -> 无效的 Entity");
//
//        //=== removeByCodeAndParentCode(...)
//        try {
//            repository.removeByCodeAndParentCode(newEntity.getCode()
//                    , newEntity.getParentCode());
//        } catch (IllegalArgumentException e) {
//            Assert.state(false
//                    , "===== removeByRoleCodeAndResourceCode(...) -> the given entity is null.");
//        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
//            Assert.state(false
//                    , "===== removeByRoleCodeAndResourceCode(...) -> the given entity is non persistent.");
//        }
//        System.out.println(newEntity);
//    }
//
//}
