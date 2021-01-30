package github.com.suitelhy.dingding.sso.authorization.domain.event.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityResourceEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityRoleEvent;
import github.com.suitelhy.dingding.core.domain.event.security.SecurityUrlEvent;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceUrlService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashMap;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashSet;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import github.com.suitelhy.dingding.core.infrastructure.web.vo.HTTP;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 资源 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 * @see SecurityResourceService
 */
@SpringBootTest
public class SecurityUrlEventTests {

    @Autowired
    private SecurityResourceService service;

    @Autowired
    private SecurityRoleService roleService;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private SecurityResourceUrlService securityResourceUrlService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserEvent userEvent;

    @Autowired
    private SecurityRoleEvent securityRoleEvent;

    @Autowired
    private SecurityResourceEvent securityResourceEvent;

    @Autowired
    private SecurityUrlEvent securityUrlEvent;

    @Value("${dingding.security.client-id}")
    private String clientId;

    /**
     * 获取(测试用的)操作者信息
     *
     * @return {@link SecurityUser}
     */
    private @NotNull
    SecurityUser operator() {
        return securityUserService.selectByUsername("admin");
    }

    private @NotNull
    String getClientId() {
        return this.clientId;
    }

    private @NotNull
    SecurityResource getEntityForTest() {
        return getEntityForTest(null);
    }

    private @NotNull
    SecurityResource getEntityForTest(@Nullable Integer seed) {
        return SecurityResource.Factory.RESOURCE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , null
                , null
                , "test"
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , null
                , 0
                , Resource.TypeVo.MENU);
    }

    private @NotNull
    SecurityRole getRoleForTest() {
        return getRoleForTest(null);
    }

    private @NotNull
    SecurityRole getRoleForTest(@Nullable Integer seed) {
        return SecurityRole.Factory.ROLE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
                                .concat(null == seed ? "" : Integer.toString(seed)))
                , "测试角色".concat(null == seed ? "" : Integer.toString(seed))
                , "测试用数据");
    }

    private @NotNull
    SecurityUser getSecurityUserForTest(@NotNull User user) {
        if (securityUserService.existByUsername(user.getUsername())) {
            return securityUserService.selectByUsername(user.getUsername());
        }
        return SecurityUser.Factory.USER.create(user);
    }

    private @NotNull
    String getUrlForTest() {
        return getUrlForTest(null);
    }

    private @NotNull
    String getUrlForTest(@Nullable Integer seed) {
        return "/test/test"
                .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                .concat((null == seed || seed < 0) ? "" : Integer.toString(seed));
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @return {@link this#getUserForTest(Integer)}
     * · 数据结构:
     * {
     * "user": {@link User},
     * "userAccountOperationInfo": {@link UserAccountOperationInfo},
     * "userPersonInfo": {@link UserPersonInfo}
     * }
     */
    private @NotNull
    Map<String, EntityModel<?>> getUserForTest() {
        return getUserForTest(null);
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @param seed
     * @return {@link Map}
     * · 数据结构:
     * {
     * "user": {@link User},
     * "userAccountOperationInfo": {@link UserAccountOperationInfo},
     * "userPersonInfo": {@link UserPersonInfo}
     * }
     */
    private @NotNull
    Map<String, EntityModel<?>> getUserForTest(Integer seed) {
        @NotNull Map<String, EntityModel<?>> result = new HashMap<>(3);

        final @NotNull User newUser = User.Factory.USER.create(
                "测试用户".concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , "test123"
        );

        @NotNull String currentTime = new CalendarController().toString();
        final @NotNull UserAccountOperationInfo userAccountOperationInfo = UserAccountOperationInfo.Factory.USER.create(
                newUser.getUsername()
                , "127.0.0.1"
                , currentTime
                , currentTime
        );

        final @NotNull UserPersonInfo userPersonInfo = UserPersonInfo.Factory.USER.create(
                newUser.getUsername()
                , "测试用户"
                , null
                , null
                , null
                , null
        );

        result.put("user", newUser);
        result.put("userAccountOperationInfo", userAccountOperationInfo);
        result.put("userPersonInfo", userPersonInfo);

        return result;
    }

    private @NotNull
    String getUrlHttpMethodNameForTest() {
        return HTTP.MethodVo.GET.name();
    }

    private @NotNull
    String[] getUrlInfoForTest() {
        return getUrlInfoForTest(null);
    }

    private @NotNull
    String[] getUrlInfoForTest(@Nullable Integer seed) {
        return new String[]{
                getClientId(),
                getUrlForTest(seed),
                getUrlHttpMethodNameForTest()
        };
    }

    private @NotNull
    String ip() {
        return "127.0.0.0";
    }

    @Test
    public void contextLoads() {
        Assert.notNull(service, "获取测试单元失败");
    }

    /**
     * @see SecurityUrlEvent#existResourceOnUrlInfo(String[])
     */
    @Test
    @Transactional
    public void existResourceOnUrlInfo()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator().getUsername());

        // 添加测试数据
        final @NotNull SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据失败!");

        final @NotNull String[] newUrlInfo = getUrlInfoForTest();

        Assert.isTrue(securityResourceUrlService.insert(SecurityResourceUrl.Factory.RESOURCE_URL.create(newEntity, newUrlInfo)
                , operator
                , operator_userAccountOperationInfo)
                , "===== 添加测试数据失败!");

        Assert.isTrue(securityUrlEvent.insertResourceUrlRelationship(newEntity, newUrlInfo, operator)
                , "===== 添加测试数据失败!");

        //=== existResourceOnUrlInfo(String[])

        Assert.isTrue(securityUrlEvent.existResourceOnUrlInfo(newUrlInfo)
                , "===== existResourceOnUrlInfo(String[]) -> false");
    }

    /**
     * @see SecurityUrlEvent#selectResourceUrlRelationshipOnUrlInfoByClientIdAndUrlPath(String, String)
     */
    @Test
    @Transactional
    public void selectResourceUrlRelationshipOnUrlInfoByClientIdAndUrlPath()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        final @NotNull String[] newUrl = getUrlInfoForTest();
        final @NotNull String[] newUrl1 = getUrlInfoForTest(1);
        final @NotNull String[] newUrl2 = getUrlInfoForTest(1);

        final @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        @NotNull final @NotNull ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(3);
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(newUrl)) {
            urls.add(newUrl);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(newUrl1)) {
            urls.add(newUrl1);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(newUrl2)) {
            urls.add(newUrl2);
        }

        Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newEntity, newUrl, operator)
                , "===== 添加测试数据【%s】&【%s】 -> false");
        for (@NotNull SecurityResource eachResource : resources) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, newUrl, operator)
                    , "===== 添加测试数据【%s】&【%s】 -> false");
        }
        for (@NotNull String[] urlInfo : urls) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newEntity, urlInfo, operator)
                    , "===== 添加测试数据【%s】&【%s】 -> false");
        }
        for (@NotNull SecurityResource eachResource : resources) {
            for (@NotNull String[] urlInfo : urls) {
                Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, urlInfo, operator)
                        , "===== 添加测试数据【%s】&【%s】 -> false");
            }
        }

        //===== selectResourceUrlRelationshipOnUrlInfoByClientIdAndUrlPath(String, String)

        for (@NotNull String[] eachUrl : urls) {
            final @NotNull List<SecurityResourceUrl> eachUrl_resourceUrl = securityUrlEvent.selectResourceUrlRelationshipOnUrlInfoByClientIdAndUrlPath(eachUrl[0], eachUrl[1]);

            Assert.isTrue(!eachUrl_resourceUrl.isEmpty()
                    , "===== selectResourceUrlRelationshipOnUrlInfoByClientIdAndUrlPath(String, String) <- 非预期结果!");

            for (@NotNull SecurityResource eachResource : resources) {
                boolean existedResourceUrlFlag = false;
                for (@NotNull SecurityResourceUrl resourceUrl : eachUrl_resourceUrl) {
                    if (eachResource.getCode().equals(resourceUrl.getCode())) {
                        existedResourceUrlFlag = true;
                        break;
                    }
                }
                Assert.isTrue(existedResourceUrlFlag
                        , "===== selectResourceUrlRelationshipOnUrlInfoByClientIdAndUrlPath(String, String) <- 非预期结果!");
            }
        }
    }

    /**
     * @see SecurityUrlEvent#insertUrlInfo(SecurityResource, String[], SecurityUser)
     */
    @Test
    @Transactional
    public void insertUrlInfo()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //=== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        final @NotNull String[] newUrlInfo = getUrlInfoForTest();

        Assert.isTrue(securityUrlEvent.insertUrlInfo(newEntity, newUrlInfo, operator)
                , "===== 添加测试数据 -> false");

        //=== 校验数据

        final @NotNull List<SecurityResourceUrl> newEntity_existedResourceUrls = securityUrlEvent.selectUrlInfoOnResourceByResourceCode(newEntity.getCode());

        boolean newUrlInfo_flag = false;
        if (!newEntity_existedResourceUrls.isEmpty()) {
            for (@NotNull SecurityResourceUrl eachResourceUrl : newEntity_existedResourceUrls) {
                if (Arrays.equals(newUrlInfo, eachResourceUrl.getUrlInfo())) {
                    newUrlInfo_flag = true;
                    break;
                }
            }
        }

        Assert.isTrue(newUrlInfo_flag, "===== 校验数据 -> false");
    }

    /**
     * @see SecurityUrlEvent#deleteUrlInfo(String[], SecurityUser)
     */
    @Test
    @Transactional
    public void deleteUrlInfo()
            throws BusinessAtomicException {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
        final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        //===== 添加测试数据

        final @NotNull SecurityResource newEntity = getEntityForTest();
        final @NotNull SecurityResource newEntity1 = getEntityForTest(1);
        final @NotNull SecurityResource newEntity2 = getEntityForTest(1);

        Assert.isTrue(service.insert(newEntity, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity1, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        Assert.isTrue(service.insert(newEntity2, operator, operator_userAccountOperationInfo)
                , "===== 添加测试数据 -> false");

        //===== insertResourceUrlRelationship(...)

        final @NotNull String[] newUrl = getUrlInfoForTest();
        final @NotNull String[] newUrl1 = getUrlInfoForTest(1);
        final @NotNull String[] newUrl2 = getUrlInfoForTest(1);

        final @NotNull Set<SecurityResource> resources = new HashSet<>(1);
        if (!newEntity.isEmpty()) {
            resources.add(newEntity);
        }
        if (!newEntity1.isEmpty()) {
            resources.add(newEntity1);
        }
        if (!newEntity2.isEmpty()) {
            resources.add(newEntity2);
        }

        final @NotNull ContainArrayHashSet<String> urls = new ContainArrayHashSet<>(3);
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(newUrl)) {
            urls.add(newUrl);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(newUrl1)) {
            urls.add(newUrl1);
        }
        if (SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(newUrl2)) {
            urls.add(newUrl2);
        }

        Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newEntity, newUrl, operator)
                , "===== insertResourceUrlRelationship(...) -> false");
        for (@NotNull SecurityResource eachResource : resources) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, newUrl, operator)
                    , "===== insertResourceUrlRelationship(...) -> false");
        }
        for (@NotNull String[] eachUrlInfo : urls) {
            Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(newEntity, eachUrlInfo, operator)
                    , "===== insertResourceUrlRelationship(...) -> false");
        }
        for (@NotNull SecurityResource eachResource : resources) {
            for (@NotNull String[] eachUrlInfo : urls) {
                Assert.isTrue(securityResourceEvent.insertResourceUrlRelationship(eachResource, eachUrlInfo, operator)
                        , "===== insertResourceUrlRelationship(...) -> false");
            }
        }

        //===== 校验测试数据

        int existResourceNum = 0;
        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityResourceUrl> eachResource_roles = securityResourceEvent.selectUrlInfoOnResourceByResourceCode(eachResource.getCode());

            Assert.isTrue(null != eachResource_roles && !eachResource_roles.isEmpty()
                    , "===== insertResourceUrlRelationship(...) <- 非预期结果!");

            existResourceNum += eachResource_roles.size();
        }

        Assert.isTrue((3 < existResourceNum && existResourceNum < 10)
                , String.format("===== 校验测试数据 -> 【%s】 <- 操作失败, 非预期结果!", existResourceNum));

        //===== SecurityUrlEvent#deleteUrlInfo(String[], SecurityUser)

        Assert.isTrue(securityUrlEvent.deleteUrlInfo(newUrl, operator)
                , "===== SecurityUrlEvent#deleteUrlInfo(String[], SecurityUser) -> false");
        for (@NotNull String[] eachUrlInfo : urls) {
            Assert.isTrue(securityUrlEvent.deleteUrlInfo(eachUrlInfo, operator)
                    , "===== SecurityUrlEvent#deleteUrlInfo(String[], SecurityUser) -> false");
        }

        //=== 校验数据

        for (@NotNull SecurityResource eachResource : resources) {
            final @NotNull List<SecurityResourceUrl> eachResource_urls = securityUrlEvent.selectUrlInfoOnResourceByResourceCode(eachResource.getCode());

            boolean existedUrlsFlag = false;
            if (!eachResource_urls.isEmpty()) {
                for (@NotNull SecurityResourceUrl eachExistedUrl : eachResource_urls) {
                    for (@NotNull String[] eachUrl : urls) {
                        if (Arrays.equals(eachUrl, eachExistedUrl.getUrlInfo())) {
                            existedUrlsFlag = true;
                            break;
                        }
                    }
                }
            }

            Assert.isTrue(!existedUrlsFlag
                    , String.format("===== 校验数据 -> 【%s】&【%s】 <- 非预期结果!", eachResource, eachResource_urls));
        }
    }

}
