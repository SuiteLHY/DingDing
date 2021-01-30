package github.com.suitelhy.dingding.sso.authorization.application.security;

import com.sun.istack.Nullable;
import github.com.suitelhy.dingding.core.application.task.UserTask;
import github.com.suitelhy.dingding.core.application.task.security.RoleTask;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.event.UserEvent;
import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.core.domain.service.UserPersonInfoService;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.domain.service.security.impl.DingDingUserDetailsService;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.UserDto;
import github.com.suitelhy.dingding.core.infrastructure.application.dto.security.RoleDto;
import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import github.com.suitelhy.dingding.core.infrastructure.web.AbstractSecurityUser;
import github.com.suitelhy.dingding.sso.authorization.application.LogTaskTests;
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
import java.util.*;

@SpringBootTest
public class RoleTaskTests {

    /**
     * 用户（安全认证）基本信息
     *
     * @Description {@link AbstractSecurityUser} 的项目定制化实现.
     * @Design {@link AbstractSecurityUser}
     * · 【安全设计】使用[静态的嵌套类]能够有效地限制[对 {@link DingDingUserDetailsService.SecurityUser} 的滥用].
     * @see AbstractSecurityUser
     */
    public static class SecurityUserTestClass
            extends DingDingUserDetailsService.SecurityUser {

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
                throws AccountStatusException, IllegalArgumentException {
            super(username, password, authorities
                    , accountNonExpired, accountNonLocked, credentialsNonExpired
                    , enabled);
        }

        /**
         * 判断是否正常
         *
         * @return {@link Boolean#TYPE}
         * @Description 综合判断.
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
     * @Design {@link github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails}
     * · 【安全设计】使用[静态的嵌套类]能够有效地限制[对 {@link github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails} 的滥用].
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

                private final @NotNull
                String clientId;

                private final Collection<String> scope;

                private final @NotNull
                String userName;

                /**
                 * (Constructor)
                 *
                 * @param active      是否处于活动状态
                 * @param authorities 权限集合
                 * @param clientId    (凭证对应的)客户端编号
                 * @param scope       可操作范围
                 * @param userName    (凭证对应的)用户名称
                 * @throws BadCredentialsException             非法的凭证参数
                 * @throws InsufficientAuthenticationException 不满足构建[用户认证凭据 - 详细信息]的必要条件
                 * @Description 限制 {@link github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails} 实现类的构造.
                 * @see this#isActive()
                 * @see this#getAuthorities()
                 * @see this#getClientId()
                 * @see this#getScope()
                 * @see this#getUserName()
                 */
                protected UserDetails(Boolean active, @NotNull Collection<String> authorities, @NotNull String clientId
                        , @NotNull Collection<String> scope, @NotNull String userName)
                        throws BadCredentialsException, InsufficientAuthenticationException, IllegalArgumentException {
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
                public @NotNull
                Collection<String> getAuthorities() {
                    return this.authorities;
                }

                @Override
                public @NotNull
                String getClientId() {
                    return this.clientId;
                }

                @Override
                public @NotNull
                Collection<String> getScope() {
                    return this.scope;
                }

                @Override
                public @NotNull
                String getUserName() {
                    return this.userName;
                }

            }

        }

    }

    @Value("${dingding.security.client-id}")
    private String clientId;

    @Autowired
    private UserTask userTask;

    @Autowired
    private RoleTask roleTask;

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
    private SecurityUserTestClass operator() {
        final User user = userEvent.selectUserByUsername("admin");

        if (null == user) {
            throw new UsernameNotFoundException("获取不到指定的用户");
        }

        // 自定义认证用户 - 权限
        final Collection<GrantedAuthority> authorities = new HashSet<>(1);
        final List<SecurityRole> roleList = userEvent.selectRoleOnUserByUsername(user.getUsername());
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
     * @return {@link SecurityUserTestClass}
     */
    private @NotNull
    OAuth2AuthenticationInfo.AbstractUserAuthentication.UserDetails operator_OAuth2_UserDetails() {
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
     * 获取(测试用的)[(安全) 角色]
     *
     * @return {@link RoleDto}
     */
    @NotNull
    private RoleDto getRoleForTest() {
        return getRoleForTest(null);
    }

    /**
     * 获取(测试用的)[(安全) 角色]
     *
     * @param seed
     * @return {@link RoleDto}
     */
    @NotNull
    private RoleDto getRoleForTest(@Nullable Integer seed) {
        long current_Millisecond = new CalendarController().getTimeInMillis();

        String role_Code = Long.toString(current_Millisecond);
        if (null != seed) {
            role_Code.concat(Integer.toString(seed));
        }

        return RoleDto.Factory.ROLE_DTO.create(role_Code
                , String.format("测试%s", role_Code)
                , "测试用的数据。");
    }

    /**
     * 获取测试用的[用户（安全认证）基本信息]
     *
     * @return {@link AbstractSecurityUser}
     */
    @NotNull
    private AbstractSecurityUser getSecurityUserForTest() {
        return (AbstractSecurityUser) userDetailsService.loadUserByUsername(username());
    }

    /**
     * 获取测试用的用户信息
     *
     * @param securityUser {@link AbstractSecurityUser}
     * @return {@link UserDto}
     * @throws IllegalAccessException
     */
    @NotNull
    private UserDto getUserInfo(@NotNull AbstractSecurityUser securityUser)
            throws IllegalAccessException {
        final @NotNull User user = userEvent.selectUserByUsername(securityUser.getUsername());
        if (null == user || user.isEmpty()) {
            throw new IllegalArgumentException("非法输入:<param>securityUser</param> <- 无效的用户");
        }

        final UserAccountOperationInfo userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(user.getUsername());
        final UserPersonInfo userPersonInfo = userEvent.selectUserPersonInfoByUsername(user.getUsername());

        return UserDto.Factory.USER_DTO.create(user, userAccountOperationInfo, userPersonInfo);
    }

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
        Assert.notNull(roleTask, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAllRole__operator() {
        final @NotNull TaskResult<List<RoleDto>> result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUserTestClass operator = operator();

        Assert.isTrue((result = roleTask.selectAllRole(0, 10, operator)).isSuccess()
                , String.format("selectAllRole(...) -> false => 【%s】", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectAllRole__operator_OAuth2_UserDetails() {
        final @NotNull TaskResult<List<RoleDto>> result;

        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        Assert.isTrue((result = roleTask.selectAllRole(0, 10, operator_OAuth2_UserDetails)).isSuccess()
                , String.format("selectAllRole(...) -> false => 【%s】", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectRoleByCode() {
        final @NotNull TaskResult<RoleDto> result;

        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据

        // 添加（测试用的）角色数据
        final @NotNull RoleDto newRoleDto = getRoleForTest();
        final @NotNull TaskResult<RoleDto> newRole_TaskResult;
        Assert.isTrue((newRole_TaskResult = roleTask.addRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , operator_OAuth2_UserDetails)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）角色数据 -> false => %s", newRole_TaskResult));

        //=== selectRoleByCode(..)
        Assert.isTrue((result = roleTask.selectRoleByCode(newRole_TaskResult.data.getCode(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("selectRoleByCode(..) -> false => 【%s】", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void selectRoleByUsername()
            throws IllegalAccessException {
        final @NotNull TaskResult<List<RoleDto>> result;

        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据

        // 添加（测试用的）角色数据
        final @NotNull RoleDto newRoleDto = getRoleForTest();
        final @NotNull TaskResult<RoleDto> newRole_TaskResult;
        Assert.isTrue((newRole_TaskResult = roleTask.addRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , operator_OAuth2_UserDetails)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）角色数据 -> false => %s", newRole_TaskResult));

        // 添加（测试用的）用户数据
        final @NotNull AbstractSecurityUser newUser = getSecurityUserForTest();
        final @NotNull UserDto newUserDto = getUserInfo(newUser);
        final @NotNull TaskResult<UserDto> newUser_TaskResult;
        Assert.isTrue((newUser_TaskResult = userTask.registerUser(newUserDto, password(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）用户数据 -> false => %s", newUser_TaskResult));

        // 添加（测试用的）[用户 - 角色]关联数据
        final @NotNull TaskResult<Boolean> newUserRole_TaskResult;
        Assert.isTrue((newUserRole_TaskResult = roleTask.addRoleToUser(newRoleDto, newUserDto, operator_OAuth2_UserDetails)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）[用户 - 角色]关联数据 -> false => %s", newUserRole_TaskResult));

        //=== selectRoleByUsername(..)
        Assert.isTrue((result = roleTask.selectRoleByUsername(newUserDto.getUsername(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("selectRoleByUsername(..) -> false => 【%s】", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void addRole__operator() {
        final @NotNull TaskResult<RoleDto> result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUserTestClass operator = operator();
        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据

        // addRole(...)
        final @NotNull RoleDto newRoleDto = getRoleForTest();
        final @NotNull TaskResult<RoleDto> newRole_TaskResult;
        Assert.isTrue((newRole_TaskResult = roleTask.addRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , operator)).isSuccess()
                , String.format("添加测试数据 - addRole(...) -> false => %s", newRole_TaskResult));

        //=== 验证测试数据
        Assert.isTrue((result = roleTask.selectRoleByCode(newRole_TaskResult.data.getCode(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("验证测试数据 -> false => 【%s】", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void addRole__operator_OAuth2_UserDetails() {
        final @NotNull TaskResult<RoleDto> result;

        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据

        // addRole(...)
        final @NotNull RoleDto newRoleDto = getRoleForTest();
        final @NotNull TaskResult<RoleDto> newRole_TaskResult;
        Assert.isTrue((newRole_TaskResult = roleTask.addRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , operator_OAuth2_UserDetails)).isSuccess()
                , String.format("添加测试数据 - addRole(...) -> false => %s", newRole_TaskResult));

        //=== 验证测试数据
        Assert.isTrue((result = roleTask.selectRoleByCode(newRole_TaskResult.data.getCode(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("验证测试数据 -> false => 【%s】", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void addRoleToUser__operator()
            throws IllegalAccessException {
        final @NotNull TaskResult<List<RoleDto>> result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUserTestClass operator = operator();
        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据

        // 添加（测试用的）角色数据
        final @NotNull RoleDto newRoleDto = getRoleForTest();
        final @NotNull TaskResult<RoleDto> newRole_TaskResult;
        Assert.isTrue((newRole_TaskResult = roleTask.addRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , operator)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）角色数据 -> false => %s", newRole_TaskResult));

        // 添加（测试用的）用户数据
        final @NotNull AbstractSecurityUser newUser = getSecurityUserForTest();
        final @NotNull UserDto newUserDto = getUserInfo(newUser);
        final @NotNull TaskResult<UserDto> newUser_TaskResult;
        Assert.isTrue((newUser_TaskResult = userTask.registerUser(newUserDto, password(), operator)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）用户数据 -> false => %s", newUser_TaskResult));

        // addRoleToUser(...)
        final @NotNull TaskResult<Boolean> newUserRole_TaskResult;
        Assert.isTrue((newUserRole_TaskResult = roleTask.addRoleToUser(newRoleDto, newUserDto, operator)).isSuccess()
                , String.format("addRoleToUser(...) -> false => %s", newUserRole_TaskResult));

        //=== 验证（测试用的）[用户 - 角色]关联数据
        Assert.isTrue((result = roleTask.selectRoleByUsername(newUserDto.getUsername(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("验证（测试用的）[用户 - 角色]关联数据 -> false => 【%s】", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void addRoleToUser__operator_OAuth2_UserDetails()
            throws IllegalAccessException {
        final @NotNull TaskResult<List<RoleDto>> result;

        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据

        // 添加（测试用的）角色数据
        final @NotNull RoleDto newRoleDto = getRoleForTest();
        final @NotNull TaskResult<RoleDto> newRole_TaskResult;
        Assert.isTrue((newRole_TaskResult = roleTask.addRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , operator_OAuth2_UserDetails)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）角色数据 -> false => %s", newRole_TaskResult));

        // 添加（测试用的）用户数据
        final @NotNull AbstractSecurityUser newUser = getSecurityUserForTest();
        final @NotNull UserDto newUserDto = getUserInfo(newUser);
        final @NotNull TaskResult<UserDto> newUser_TaskResult;
        Assert.isTrue((newUser_TaskResult = userTask.registerUser(newUserDto, password(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）用户数据 -> false => %s", newUser_TaskResult));

        // addRoleToUser(...)
        final @NotNull TaskResult<Boolean> newUserRole_TaskResult;
        Assert.isTrue((newUserRole_TaskResult = roleTask.addRoleToUser(newRoleDto, newUserDto, operator_OAuth2_UserDetails)).isSuccess()
                , String.format("addRoleToUser(...) -> false => %s", newUserRole_TaskResult));

        //=== 验证（测试用的）[用户 - 角色]关联数据
        Assert.isTrue((result = roleTask.selectRoleByUsername(newUserDto.getUsername(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("验证（测试用的）[用户 - 角色]关联数据 -> false => 【%s】", result));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void updateRole__operator() {
        final @NotNull TaskResult<RoleDto> result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUserTestClass operator = operator();
        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据

        // 添加（测试用的）角色数据
        final @NotNull RoleDto newRoleDto = getRoleForTest();
        final @NotNull TaskResult<RoleDto> newRole_TaskResult;
        Assert.isTrue((newRole_TaskResult = roleTask.addRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , operator)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）角色数据 -> false => %s", newRole_TaskResult));

        // updateRole(...)
        final @NotNull RoleDto newRoleDto1 = getRoleForTest();
        final @NotNull Map<String, Object> new_role_data = new HashMap<>(2);
        new_role_data.put("role_name", newRoleDto1.getName());
        new_role_data.put("role_description", newRoleDto1.getDescription());

        final @NotNull TaskResult<RoleDto> newRole_TaskResult1;
        Assert.isTrue((newRole_TaskResult1 = roleTask.updateRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , new_role_data
                , operator)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）角色数据 -> false => %s", newRole_TaskResult1));

        //=== 验证测试数据
        Assert.isTrue((result = roleTask.selectRoleByCode(newRole_TaskResult.data.getCode(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("验证测试数据 -> false => 【%s】", result));
        Assert.isTrue(result.data.equals(newRole_TaskResult1.data)
                        && result.data.getName().equals(newRole_TaskResult1.data.getName())
                        && result.data.getDescription().equals(newRole_TaskResult1.data.getDescription())
                , String.format("验证测试数据 -> 数据更新非预期 => 【%s】&【%s】", result, newRole_TaskResult1.data));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void updateRole__operator_OAuth2_UserDetails() {
        final @NotNull TaskResult<RoleDto> result;

        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据

        // 添加（测试用的）角色数据
        final @NotNull RoleDto newRoleDto = getRoleForTest();
        final @NotNull TaskResult<RoleDto> newRole_TaskResult;
        Assert.isTrue((newRole_TaskResult = roleTask.addRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , operator_OAuth2_UserDetails)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）角色数据 -> false => %s", newRole_TaskResult));

        // updateRole(...)
        final @NotNull RoleDto newRoleDto1 = getRoleForTest();
        final @NotNull Map<String, Object> new_role_data = new HashMap<>(2);
        new_role_data.put("role_name", newRoleDto1.getName());
        new_role_data.put("role_description", newRoleDto1.getDescription());

        final @NotNull TaskResult<RoleDto> newRole_TaskResult1;
        Assert.isTrue((newRole_TaskResult1 = roleTask.updateRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , new_role_data
                , operator_OAuth2_UserDetails)).isSuccess()
                , String.format("添加测试数据 - 添加（测试用的）角色数据 -> false => %s", newRole_TaskResult1));

        //=== 验证测试数据
        Assert.isTrue((result = roleTask.selectRoleByCode(newRole_TaskResult.data.getCode(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("验证测试数据 -> false => 【%s】", result));
        Assert.isTrue(result.data.equals(newRole_TaskResult1.data)
                        && result.data.getName().equals(newRole_TaskResult1.data.getName())
                        && result.data.getDescription().equals(newRole_TaskResult1.data.getDescription())
                , String.format("验证测试数据 -> 数据更新非预期 => 【%s】&【%s】", result, newRole_TaskResult1.data));
        System.out.println(result);
    }

    @Test
    @Transactional
    public void deleteRole__operator() {
        final @NotNull TaskResult<RoleDto> result;

        // 获取必要的测试用身份信息
        final @NotNull SecurityUserTestClass operator = operator();
        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据

        // 添加(测试用的)角色数据
        final @NotNull RoleDto newRoleDto = getRoleForTest();
        final @NotNull TaskResult<RoleDto> newRole_TaskResult;
        Assert.isTrue((newRole_TaskResult = roleTask.addRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , operator)).isSuccess()
                , String.format("添加(测试用的)角色数据 -> false => 【%s】", newRole_TaskResult));

        //=== 验证[添加测试数据]
        final @NotNull TaskResult<RoleDto> newRole_TaskResult1;
        Assert.isTrue((newRole_TaskResult1 = roleTask.selectRoleByCode(newRole_TaskResult.data.getCode(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("验证[添加测试数据] -> false => 【%s】", newRole_TaskResult1));

        // deleteRole(..)
        Assert.isTrue((result = roleTask.deleteRole(newRole_TaskResult.data.getCode(), operator)).isSuccess()
                , String.format("deleteRole(..) -> false => 【%s】", result));

        System.out.println(result);
    }

    @Test
    @Transactional
    public void deleteRole__operator_OAuth2_UserDetails() {
        final @NotNull TaskResult<RoleDto> result;

        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();

        //=== 添加测试数据

        // 添加(测试用的)角色数据
        final @NotNull RoleDto newRoleDto = getRoleForTest();
        final @NotNull TaskResult<RoleDto> newRole_TaskResult;
        Assert.isTrue((newRole_TaskResult = roleTask.addRole(newRoleDto.getCode()
                , newRoleDto.getName()
                , newRoleDto.getDescription()
                , operator_OAuth2_UserDetails)).isSuccess()
                , String.format("添加(测试用的)角色数据 -> false => 【%s】", newRole_TaskResult));

        //=== 验证[添加测试数据]
        final @NotNull TaskResult<RoleDto> newRole_TaskResult1;
        Assert.isTrue((newRole_TaskResult1 = roleTask.selectRoleByCode(newRole_TaskResult.data.getCode(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("验证[添加测试数据] -> false => 【%s】", newRole_TaskResult1));

        // deleteRole(..)
        Assert.isTrue((result = roleTask.deleteRole(newRole_TaskResult.data.getCode(), operator_OAuth2_UserDetails)).isSuccess()
                , String.format("deleteRole(..) -> false => 【%s】", result));

        System.out.println(result);
    }

}
