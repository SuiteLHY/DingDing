package github.com.suitelhy.dingding.security.service.provider.domain.event.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.event.read.SecurityRoleReadEvent;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.idempotence.SecurityRoleIdempotentEvent;
import github.com.suitelhy.dingding.security.service.api.domain.event.write.non.idempotence.SecurityRoleNonIdempotentEvent;
import github.com.suitelhy.dingding.security.service.provider.domain.event.SecurityRoleEvent;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityResourceService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityRoleService;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityUserService;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 角色 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 */
@SpringBootTest
public class SecurityRoleEventTests {

    @Autowired
    private SecurityRoleService securityRoleService;

    @Autowired
    private SecurityResourceService securityResourceService;

    @Autowired
    private SecurityUserService securityUserService;

//    @Autowired
//    private UserReadEvent userReadEvent;

    @Autowired
    private SecurityRoleEvent securityRoleEvent;

    /**
     * 获取(测试用的)操作者信息
     *
     * @return {@link SecurityUser}
     */
    private @NotNull SecurityUser operator() {
        return securityUserService.selectByUsername("admin");
    }

    private @NotNull SecurityRole getEntityForTest() {
        return getEntityForTest(null);
    }

    private @NotNull SecurityRole getEntityForTest(@Nullable Integer seed) {
        return SecurityRole.Factory.ROLE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
                                .concat(null == seed ? "" : Integer.toString(seed)))
                , "测试角色".concat(null == seed ? "" : Integer.toString(seed))
                , "测试用数据");
    }

    private @NotNull SecurityResource getResourceForTest() {
        return getResourceForTest(null);
    }

    private @NotNull SecurityResource getResourceForTest(@Nullable Integer seed) {
        @NotNull String seedString = (null == seed)
                ? ""
                : Integer.toString(seed);
        return SecurityResource.Factory.RESOURCE.create(
                "test"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", "")
                                .concat(seedString))
                , null
                , null
                , "test".concat(seedString)
                , null
                , 0
                , Resource.TypeVo.MENU);
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @return {@link this#getUserForTest(Integer)}
     * · 数据结构:
     * {
     *  "user": {@link User},
     *  "userAccountOperationInfo": {@link UserAccountOperationInfo},
     *  "userPersonInfo": {@link UserPersonInfo}
     * }
     */
    private @NotNull Map<String, EntityModel<?>> getUserForTest() {
        return getUserForTest(null);
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @param seed
     *
     * @return {@link Map}
     * · 数据结构:
     * {
     *  "user": {@link User},
     *  "userAccountOperationInfo": {@link UserAccountOperationInfo},
     *  "userPersonInfo": {@link UserPersonInfo}
     * }
     */
    private @NotNull Map<String, EntityModel<?>> getUserForTest(Integer seed) {
        final @NotNull Map<String, EntityModel<?>> result = new HashMap<>(3);

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

    @Test
    public void contextLoads() {
        Assert.notNull(securityRoleService, "获取测试单元失败");
        Assert.notNull(securityResourceService, "获取测试单元失败");
        Assert.notNull(securityUserService, "获取测试单元失败");
        /*Assert.notNull(userReadEvent, "获取测试单元失败");*/
        Assert.notNull(securityRoleEvent, "获取测试单元失败");
    }

    /**
     * @see SecurityRoleReadEvent#existRoleOnUserByUsername(String)
     */
    @Test
    @Transactional
    public void existRoleOnUserByUsername()
            throws BusinessAtomicException
    {
        /*// 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userReadEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(securityRoleIdempotentService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        final @NotNull Map<String, EntityModel<?>> newUserInfoMap = getUserForTest();

        Assert.isTrue(userNonIdempotentWriteEvent.registerUser((User) newUserInfoMap.get("user")
                    , (UserAccountOperationInfo) newUserInfoMap.get("userAccountOperationInfo")
                    , (UserPersonInfo) newUserInfoMap.get("userPersonInfo")
                    , operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityUser newUserInfoMap_securityUser = userReadEvent.selectSecurityUserByUsername(((User) newUserInfoMap.get("user")).getUsername());
        Assert.isTrue(securityRoleIdempotentEvent.insertUserRoleRelationship(newUserInfoMap_securityUser, newEntity, operator)
                , "===== 添加测试数据失败!");

        // existRoleOnUserByUsername(String)

        Assert.isTrue(securityRoleEvent.existRoleOnUserByUsername(((User) newUserInfoMap.get("user")).getUsername())
                , "===== existRoleOnUserByUsername(String) -> false");*/

        // (不报错就行)
        securityRoleEvent.existRoleOnUserByUsername("abc123456");
    }

    /**
     * @see SecurityRoleReadEvent#selectResourceOnRoleByRoleCode(String)
     */
    @Test
    @Transactional
    public void selectResourceOnRoleByRoleCode()
            throws BusinessAtomicException
    {
        final @NotNull Page<SecurityRole> result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(newEntity.isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        Assert.isTrue(securityRoleService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        // selectAll(..)

        Assert.isTrue(! (result = securityRoleService.selectAll(0, 10)).isEmpty()
                , "The result is empty");

        System.out.println(result);
    }

    /**
     * @see SecurityRoleReadEvent#selectRoleOnResourceByResourceCode(String)
     */
    @Test
    @Transactional
    public void selectRoleOnResourceByResourceCode()
            throws BusinessAtomicException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();
//        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());

        // 添加测试数据
        final @NotNull SecurityRole newEntity = getEntityForTest();

        Assert.isTrue(securityRoleService.insert(newEntity, operator)
                , "===== 添加测试数据失败!");

        final @NotNull SecurityResource newResource = getResourceForTest();

        Assert.isTrue(securityResourceService.insert(newResource, operator)
                , "===== 添加测试数据失败!");
        Assert.isTrue(securityRoleEvent.insertRoleResourceRelationship(newEntity, newResource, operator)
                , "===== 添加测试数据失败!");

        // selectRoleOnResourceByResourceCode(String)

        final @NotNull List<SecurityRole> resource_roles = securityRoleEvent.selectRoleOnResourceByResourceCode(newResource.getCode());

        boolean resultFlag = false;
        if (! resource_roles.isEmpty()) {
            for (@NotNull SecurityRole eachRole : resource_roles) {
                if (newEntity.equals(eachRole)) {
                    resultFlag = true;
                    break;
                }
            }
        }

        Assert.isTrue(resultFlag, "===== existRoleOnUserByUsername(String) -> false");
    }

}
