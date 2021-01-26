package github.com.suitelhy.dingding.userservice.application;

import github.com.suitelhy.dingding.core.application.task.UserTask;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.service.security.impl.DingDingUserDetailsService;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.BasicUserDto;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.infrastructure.web.AbstractSecurityUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
public class UserTaskTests {

    /**
     * 用户（安全认证）基本信息
     *
     * @Description {@link AbstractSecurityUser} 的项目定制化实现.
     *
     * @Design {@link AbstractSecurityUser}
     * · 【安全设计】使用[静态的嵌套类]能够有效地限制[对 {@link DingDingUserDetailsService.SecurityUser} 的滥用].
     *
     * @see AbstractSecurityUser
     */
    public static class SecurityUserTestClass
            extends DingDingUserDetailsService.SecurityUser
    {

        /**
         * (Constructor)
         *
         * @param username
         * @param password
         * @param authorities
         * @param accountNonExpired     {@link this#isAccountNonExpired()}
         * @param accountNonLocked      {@link this#isAccountNonLocked()}
         * @param credentialsNonExpired {@link this#isCredentialsNonExpired()}
         * @param enabled               {@link this#isEnabled()}
         *
         * @throws AccountStatusException
         * @throws IllegalArgumentException
         */
        private SecurityUserTestClass(@NotNull String username
                , @NotNull String password
                , @NotNull Collection<? extends GrantedAuthority> authorities
                , boolean accountNonExpired
                , boolean accountNonLocked
                , boolean credentialsNonExpired
                , boolean enabled)
                throws AccountStatusException, IllegalArgumentException
        {
            super(username, password, authorities
                    , accountNonExpired, accountNonLocked, credentialsNonExpired
                    , enabled);
        }

        /**
         * 判断是否正常
         *
         * @Description 综合判断.
         *
         * @return {@link Boolean#TYPE}
         */
        public boolean isNormal() {
            return this.isAccountNonExpired()
                    && this.isAccountNonLocked()
                    && this.isCredentialsNonExpired()
                    && this.isEnabled();
        }

    }

    /**
     * [用户 - 身份验证令牌信息]
     *
     * @Description {@link github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails} 的项目定制化实现.
     *
     * @Design {@link github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails}
     * · 【安全设计】使用[静态的嵌套类]能够有效地限制[对 {@link github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails} 的滥用].
     *
     * @see github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails
     */
    private static final class OAuth2AuthenticationInfo {

        /**
         * 用户认证凭据
         */
        private static abstract class AbstractUserAuthentication {

            /**
             * 详细信息
             *
             * @see github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails
             */
            private static class UserDetails
                    extends github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails {

                private final Boolean active;

                private final Collection<String> authorities;

                private final @NotNull String clientId;

                private final Collection<String> scope;

                private final @NotNull String userName;

                /**
                 * (Constructor)
                 *
                 * @Description 限制 {@link github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails} 实现类的构造.
                 *
                 * @param active      是否处于活动状态
                 * @param authorities 权限集合
                 * @param clientId    (凭证对应的)客户端编号
                 * @param scope       可操作范围
                 * @param userName    (凭证对应的)用户名称
                 *
                 * @throws BadCredentialsException             非法的凭证参数
                 * @throws InsufficientAuthenticationException 不满足构建[用户认证凭据 - 详细信息]的必要条件
                 *
                 * @see this#isActive()
                 * @see this#getAuthorities()
                 * @see this#getClientId()
                 * @see this#getScope()
                 * @see this#getUserName()
                 */
                protected UserDetails(Boolean active, @NotNull Collection<String> authorities, @NotNull String clientId
                        , @NotNull Collection<String> scope, @NotNull String userName)
                        throws BadCredentialsException, InsufficientAuthenticationException, IllegalArgumentException
                {
                    super(active, authorities, clientId, scope, userName);

                    this.active = active;
                    this.authorities = authorities;
                    this.clientId = clientId;
                    this.scope = scope;
                    this.userName = userName;
                }

                @Override
                public Boolean isActive() {
                    return this.active;
                }

                @Override
                public @NotNull Collection<String> getAuthorities() {
                    return this.authorities;
                }

                @Override
                public @NotNull String getClientId() {
                    return this.clientId;
                }

                @Override
                public @NotNull Collection<String> getScope() {
                    return this.scope;
                }

                @Override
                public @NotNull String getUserName() {
                    return this.userName;
                }

            }

        }

    }

    @Value("${dingding.security.client-id}")
    private String clientId;

    @Autowired
    private UserTask userTask;

    /*@Autowired
    private UserService userService;

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    @Autowired
    private UserPersonInfoService userPersonInfoService;

    @Autowired
    private SecurityUserService securityUserService;*/

    @Autowired
    private UserEvent userEvent;

    @Autowired
    @Qualifier("dingDingUserDetailsService")
    private DingDingUserDetailsService userDetailsService;

    /**
     * 获取(测试用的)操作者身份认证信息
     *
     * @return {@link LogTaskTests.SecurityUserTestClass}
     */
    private @NotNull SecurityUserTestClass operator() {
        /*final User user = userTask.selectUserByUsername("admin").data
                .dtoId("admin", "admin1");*/
        final User user = userEvent.selectUserByUsername("admin");

        if (null == user) {
            throw new UsernameNotFoundException("获取不到指定的用户");
        }

        // 自定义认证用户 - 权限
        final @NotNull Collection<GrantedAuthority> authorities = new HashSet<>(1);
        final @NotNull List<SecurityRole> roleList = userEvent.selectRoleOnUserByUsername(user.getUsername());
        for (SecurityRole role : roleList) {
            authorities.add(new SimpleGrantedAuthority(role.getCode()));
        }

        return new SecurityUserTestClass(user.getUsername()
                , user.getPassword()
                , authorities
                , !Account.StatusVo.DESTRUCTION.equals(user.getStatus())
                , !Account.StatusVo.LOCKED.equals(user.getStatus())
                , Account.StatusVo.NORMAL.equals(user.getStatus())
                , !user.isEmpty());
    }

    /**
     * 获取(测试用的)[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
     *
     * @return {@link LogTaskTests.SecurityUserTestClass}
     */
    private @NotNull UserTaskTests.OAuth2AuthenticationInfo.AbstractUserAuthentication.UserDetails operator_OAuth2_UserDetails() {
        final User user = userEvent.selectUserByUsername("admin");

        if (null == user) {
            throw new UsernameNotFoundException("获取不到指定的用户");
        }

        // 自定义认证用户 - 权限
        final @NotNull Collection<String> authorities = new HashSet<>(1);
        final @NotNull List<SecurityRole> roleList = userEvent.selectRoleOnUserByUsername(user.getUsername());
        for (SecurityRole role : roleList) {
            authorities.add(role.getCode());
        }

        // 自定义认证用户 - 可操作范围
        final @NotNull Collection<String> scope = new HashSet<>(1);
        scope.add("all");

        return new OAuth2AuthenticationInfo.AbstractUserAuthentication.UserDetails(true
                , authorities
                , this.clientId
                , scope
                , user.getUsername());
    }

    /**
     * 获取测试用的[用户（安全认证）基本信息]
     *
     * @return {@link AbstractSecurityUser}
     */
    @NotNull
    private AbstractSecurityUser getEntityForTest() {
        /*final User newUser = User.Factory.USER.update(id()
                , 20
                , new CalendarController().toString()
                , ip()
                , new CalendarController().toString()
                , "测试用户"
                , "test123"
                , "测试数据"
                , null
                , ("测试" + new CalendarController().toString().replaceAll("[-:\\s]", ""))
                , HumanVo.Sex.MALE);*/
        return (AbstractSecurityUser) userDetailsService.loadUserByUsername(username());
    }

    /**
     * 获取测试用的用户信息
     *
     * @param securityUser  {@link AbstractSecurityUser}
     *
     * @return {@link UserDto}
     *
     * @throws IllegalAccessException
     */
    @NotNull
    private UserDto getUserInfo(@NotNull AbstractSecurityUser securityUser)
            throws IllegalAccessException
    {
        final @NotNull User user = userEvent.selectUserByUsername(securityUser.getUsername());
        if (null == user || user.isEmpty()) {
            throw new IllegalArgumentException("非法输入:<param>securityUser</param> <- 无效的用户");
        }

        final UserAccountOperationInfo userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(user.getUsername());
        final UserPersonInfo userPersonInfo = userEvent.selectUserPersonInfoByUsername(user.getUsername());

        return UserDto.Factory.USER_DTO.create(user, userAccountOperationInfo, userPersonInfo);
    }

//    @NotNull
//    private String ip() {
//        return "127.0.0.0";
//    }

//    @NotNull
//    public String id() {
//        return "402880e56fb72000016fb72014fc0000";
//    }

    @NotNull
    public String password() {
        return "test123";
    }

    @NotNull
    public String username() {
        return "测试20200118132850";
    }

    @Test
    @Transactional
    public void contextLoads() {
        Assert.notNull(userTask, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAll() {
        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        final @NotNull TaskResult<List<UserDto>> result;
        Assert.isTrue((result = userTask.selectAll(1, 10, operator_OAuth2_UserDetails)).isSuccess()
                , String.format("selectAll(int, int) -> false => 【%s】", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectUserByUserid()
            throws IllegalAccessException
    {
        final @NotNull TaskResult<UserDto> result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUserTestClass operator = operator();
        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据
        final @NotNull AbstractSecurityUser newUser = getEntityForTest();
        final @NotNull UserDto newUserDto = getUserInfo(newUser);

        final @NotNull TaskResult<UserDto> newUser_TaskResult;
        Assert.isTrue((newUser_TaskResult = userTask.registerUser(newUserDto, password(), operator)).isSuccess()
                , String.format("===== 添加测试数据 -> false => %s", newUser_TaskResult));

        //=== selectUserByUserid(..)
        Assert.isTrue((result = userTask.selectUserByUserid(newUserDto.getUserId(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("selectUserByUserid(..) -> false => 【%s】", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectCount() {
        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        final @NotNull TaskResult<Long> result;
        Assert.isTrue((result = userTask.selectCount(10, operator_OAuth2_UserDetails)).isSuccess()
                , String.format("selectCount(int) -> false => 【%s】", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void insert__operator()
            throws IllegalAccessException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUserTestClass operator = operator();

        final @NotNull AbstractSecurityUser newUser = getEntityForTest();
        final @NotNull UserDto newUserDto = getUserInfo(newUser);

        final @NotNull TaskResult<UserDto> newUser_TaskResult;
        Assert.isTrue((newUser_TaskResult = userTask.registerUser(newUserDto, password(), operator)).isSuccess()
                , String.format("===== insert(...) -> false\nnewUser_TaskResult => %s", newUser_TaskResult));
    }

    @Test
    @Transactional
    public void insert__operator_OAuth2_UserDetails()
            throws IllegalAccessException
    {
        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        final @NotNull AbstractSecurityUser newUser = getEntityForTest();
        final @NotNull UserDto newUserDto = getUserInfo(newUser);

        final @NotNull TaskResult<UserDto> newUser_TaskResult_1;
        Assert.isTrue((newUser_TaskResult_1 = userTask.registerUser(newUserDto, password(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("===== insert(...) -> false\nnewUser_TaskResult => %s", newUser_TaskResult_1));
    }

    @Test
    @Transactional
    public void update__operator()
            throws IllegalAccessException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUserTestClass operator = operator();

        //=== 添加测试数据
        final @NotNull AbstractSecurityUser newUser = getEntityForTest();
        final @NotNull UserDto newUserDto = getUserInfo(newUser);

        final @NotNull TaskResult<UserDto> newUser_TaskResult;
        Assert.isTrue((newUser_TaskResult = userTask.registerUser(newUserDto, password(), operator)).isSuccess()
                , String.format("===== 添加测试数据 -> false => %s", newUser_TaskResult));

        //=== update(...)

        final @NotNull User newUser_Entity = newUserDto.dtoId(username(), password());
        final @NotNull UserPersonInfo newUser_UserPersonInfo = newUserDto.dtoId_UserPersonInfo(newUser_Entity);
        final @NotNull UserPersonInfo updatedUser_UserPersonInfo = UserPersonInfo.Factory.USER.create(newUser_UserPersonInfo);

        updatedUser_UserPersonInfo.setAge(18);
        updatedUser_UserPersonInfo.setIntroduction(
                String.format("最新_%s", newUser_UserPersonInfo.getIntroduction())
        );

        final @NotNull TaskResult<UserDto> newUser_TaskResult1;
        Assert.isTrue((newUser_TaskResult1 = userTask.update(newUserDto, operator)).isSuccess()
                , String.format("===== update(...) -> false => 【%s】", newUser_TaskResult1));
        System.out.println(newUserDto);
    }

    @Test
    @Transactional
    public void update__operator_OAuth2_UserDetails()
            throws IllegalAccessException
    {
        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据
        final @NotNull AbstractSecurityUser newUser = getEntityForTest();
        final @NotNull UserDto newUserDto = getUserInfo(newUser);

        final @NotNull TaskResult<UserDto> newUser_TaskResult;
        Assert.isTrue((newUser_TaskResult = userTask.registerUser(newUserDto, password(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("===== 添加测试数据 -> false => %s", newUser_TaskResult));

        //=== update(...)

        final @NotNull User newUser_Entity = newUserDto.dtoId(username(), password());
        final @NotNull UserPersonInfo newUser_UserPersonInfo = newUserDto.dtoId_UserPersonInfo(newUser_Entity);
        final @NotNull UserPersonInfo updatedUser_UserPersonInfo = UserPersonInfo.Factory.USER.create(newUser_UserPersonInfo);

        updatedUser_UserPersonInfo.setAge(18);
        updatedUser_UserPersonInfo.setIntroduction(
                String.format("最新_%s", newUser_UserPersonInfo.getIntroduction())
        );

        final @NotNull TaskResult<UserDto> newUser_TaskResult1;
        Assert.isTrue((newUser_TaskResult1 = userTask.update(newUserDto, operator_OAuth2_UserDetails)).isSuccess()
                , String.format("===== update(...) -> false => 【%s】", newUser_TaskResult1));
        System.out.println(newUserDto);
    }

    @Test
    @Transactional
    public void delete__operator()
            throws IllegalAccessException
    {
        // 获取必要的测试用身份信息
        final @NotNull SecurityUserTestClass operator = operator();

        //=== 添加测试数据
        final @NotNull AbstractSecurityUser newUser = getEntityForTest();
        final @NotNull UserDto newUserDto = getUserInfo(newUser);

        final @NotNull TaskResult<UserDto> newUser_TaskResult;
        Assert.isTrue((newUser_TaskResult = userTask.registerUser(newUserDto, password(), operator)).isSuccess()
                , String.format("===== 添加测试数据 -> false => %s", newUser_TaskResult));

        //=== delete(...)
        final @NotNull TaskResult<BasicUserDto> result;
        Assert.isTrue((result = userTask.delete(newUserDto, operator)).isSuccess()
                , String.format("===== delete(...) -> false => %s", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void delete__operator_OAuth2_UserDetails()
            throws IllegalAccessException
    {
        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据
        final @NotNull AbstractSecurityUser newUser = getEntityForTest();
        final @NotNull UserDto newUserDto = getUserInfo(newUser);

        final @NotNull TaskResult<UserDto> newUser_TaskResult;
        Assert.isTrue((newUser_TaskResult = userTask.registerUser(newUserDto, password(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("===== 添加测试数据 -> false => %s", newUser_TaskResult));

        //=== delete(...)
        final @NotNull TaskResult<BasicUserDto> result;
        Assert.isTrue((result = userTask.delete(newUserDto, operator_OAuth2_UserDetails)).isSuccess()
                , String.format("===== delete(...) -> false => %s", result));
        System.out.println(result);
    }

}
