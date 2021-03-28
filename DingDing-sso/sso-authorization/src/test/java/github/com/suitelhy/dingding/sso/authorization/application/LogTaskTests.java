//package github.com.suitelhy.dingding.sso.authorization.application;
//
//import LogIdempotentWriteService;
//import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
//import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
//import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
//import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
//import github.com.suitelhy.dingding.security.service.api.infrastructure.web.AbstractSecurityUser;
//import github.com.suitelhy.dingding.sso.authorization.domain.service.security.DingDingUserDetailsService;
//import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
//import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
//import github.com.suitelhy.dingding.user.service.api.domain.event.read.UserReadEvent;
//import github.com.suitelhy.dingding.user.service.api.domain.service.read.LogReadService;
//import github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence.LogNonIdempotentWriteService;
//import org.apache.dubbo.config.annotation.Reference;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.AccountStatusException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.InsufficientAuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Assert;
//
//import javax.validation.constraints.NotNull;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//
//@SpringBootTest
//public class LogTaskTests {
//
//    /**
//     * 用户（安全认证）基本信息
//     *
//     * @Description {@link AbstractSecurityUser} 的项目定制化实现.
//     *
//     * @Design {@link AbstractSecurityUser}
//     * · 【安全设计】使用[静态的嵌套类]能够有效地限制[对 {@link DingDingUserDetailsService.SecurityUser} 的滥用].
//     *
//     * @see AbstractSecurityUser
//     */
//    public static class SecurityUserTestClass
//            extends DingDingUserDetailsService.SecurityUser {
//
//        /**
//         * (Constructor)
//         *
//         * @param username
//         * @param password
//         * @param authorities
//         * @param accountNonExpired     {@link this#isAccountNonExpired()}
//         * @param accountNonLocked      {@link this#isAccountNonLocked()}
//         * @param credentialsNonExpired {@link this#isCredentialsNonExpired()}
//         * @param enabled               {@link this#isEnabled()}
//         *
//         * @throws AccountStatusException
//         * @throws IllegalArgumentException
//         */
//        private SecurityUserTestClass(@NotNull String username
//                , @NotNull String password
//                , @NotNull Collection<? extends GrantedAuthority> authorities
//                , boolean accountNonExpired
//                , boolean accountNonLocked
//                , boolean credentialsNonExpired
//                , boolean enabled)
//                throws AccountStatusException, IllegalArgumentException
//        {
//            super(username, password, authorities
//                    , accountNonExpired, accountNonLocked, credentialsNonExpired
//                    , enabled);
//        }
//
//        /**
//         * 判断是否正常
//         *
//         * @Description 综合判断.
//         *
//         * @return {@link Boolean#TYPE}
//         */
//        public boolean isNormal() {
//            return this.isAccountNonExpired()
//                    && this.isAccountNonLocked()
//                    && this.isCredentialsNonExpired()
//                    && this.isEnabled();
//        }
//
//    }
//
//    /**
//     * [用户 - 身份验证令牌信息]
//     *
//     * @Description {@link github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails} 的项目定制化实现.
//     *
//     * @Design {@link github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails}
//     * · 【安全设计】使用[静态的嵌套类]能够有效地限制[对 {@link github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails} 的滥用].
//     *
//     * @see github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails
//     */
//    private static final class OAuth2AuthenticationInfo {
//
//        /**
//         * 用户认证凭据
//         */
//        private static abstract class AbstractUserAuthentication {
//
//            /**
//             * 详细信息
//             *
//             * @see github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails
//             */
//            private static class UserDetails
//                    extends github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails {
//
//                private final Boolean active;
//
//                private final Collection<String> authorities;
//
//                private final @NotNull
//                String clientId;
//
//                private final Collection<String> scope;
//
//                private final @NotNull
//                String userName;
//
//                /**
//                 * (Constructor)
//                 *
//                 * @param active      是否处于活动状态
//                 * @param authorities 权限集合
//                 * @param clientId    (凭证对应的)客户端编号
//                 * @param scope       可操作范围
//                 * @param userName    (凭证对应的)用户名称
//                 * @throws BadCredentialsException             非法的凭证参数
//                 * @throws InsufficientAuthenticationException 不满足构建[用户认证凭据 - 详细信息]的必要条件
//                 * @Description 限制 {@link github.com.suitelhy.dingding.core.infrastructure.web.OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails} 实现类的构造.
//                 * @see this#isActive()
//                 * @see this#getAuthorities()
//                 * @see this#getClientId()
//                 * @see this#getScope()
//                 * @see this#getUserName()
//                 */
//                protected UserDetails(Boolean active, @NotNull Collection<String> authorities, @NotNull String clientId
//                        , @NotNull Collection<String> scope, @NotNull String userName)
//                        throws BadCredentialsException, InsufficientAuthenticationException, IllegalArgumentException {
//                    super(active, authorities, clientId, scope, userName);
//
//                    this.active = active;
//                    this.authorities = authorities;
//                    this.clientId = clientId;
//                    this.scope = scope;
//                    this.userName = userName;
//                }
//
//                @Override
//                public Boolean isActive() {
//                    return this.active;
//                }
//
//                @Override
//                public @NotNull
//                Collection<String> getAuthorities() {
//                    return this.authorities;
//                }
//
//                @Override
//                public @NotNull
//                String getClientId() {
//                    return this.clientId;
//                }
//
//                @Override
//                public @NotNull
//                Collection<String> getScope() {
//                    return this.scope;
//                }
//
//                @Override
//                public @NotNull
//                String getUserName() {
//                    return this.userName;
//                }
//
//            }
//
//        }
//
//    }
//
//    @Value("${dingding.security.client-id}")
//    private String clientId;
//
//    @Reference
//    private LogReadService logReadService;
//
//    @Reference
//    private LogIdempotentWriteService logIdempotentWriteService;
//
//    @Reference
//    private LogNonIdempotentWriteService logNonIdempotentWriteService;
//
//    @Reference
//    private UserReadEvent userReadEvent;
//
//    /*@Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserAccountOperationInfoService userAccountOperationInfoService;
//
//    @Autowired
//    private UserPersonInfoService userPersonInfoService;
//
//    @Autowired
//    private SecurityUserService securityUserService;*/
//
//    @Autowired
//    private UserEvent userEvent;
//
//    @Autowired
//    @Qualifier("dingDingUserDetailsService")
//    private DingDingUserDetailsService userDetailsService;
//
//    /**
//     * 获取(测试用的)操作者身份认证信息
//     *
//     * @return {@link SecurityUserTestClass}
//     */
//    private SecurityUserTestClass operator() {
//        /*final User user = userTask.selectUserByUsername("admin").data
//                .dtoId("admin", "admin1");*/
//        final User user = userEvent.selectUserByUsername("admin");
//
//        if (null == user) {
//            throw new UsernameNotFoundException("获取不到指定的用户");
//        }
//
//        // 自定义认证用户 - 权限
//        final Collection<GrantedAuthority> authorities = new HashSet<>(1);
//        final List<SecurityRole> roleList = userEvent.selectRoleOnUserByUsername(user.getUsername());
//        for (SecurityRole role : roleList) {
//            authorities.add(new SimpleGrantedAuthority(role.getCode()));
//        }
//
//        return new SecurityUserTestClass(user.getUsername()
//                , user.getPassword()
//                , authorities
//                , !Account.StatusVo.DESTRUCTION.equals(user.getStatus())
//                , !Account.StatusVo.LOCKED.equals(user.getStatus())
//                , Account.StatusVo.NORMAL.equals(user.getStatus())
//                , !user.isEmpty());
//    }
//
//    /**
//     * 获取(测试用的)[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
//     *
//     * @return {@link SecurityUserTestClass}
//     */
//    private @NotNull
//    OAuth2AuthenticationInfo.AbstractUserAuthentication.UserDetails operator_OAuth2_UserDetails() {
//        final User user = userEvent.selectUserByUsername("admin");
//
//        if (null == user) {
//            throw new UsernameNotFoundException("获取不到指定的用户");
//        }
//
//        // 自定义认证用户 - 权限
//        final @NotNull Collection<String> authorities = new HashSet<>(1);
//        final @NotNull List<SecurityRole> roleList = userEvent.selectRoleOnUserByUsername(user.getUsername());
//        for (SecurityRole role : roleList) {
//            authorities.add(role.getCode());
//        }
//
//        // 自定义认证用户 - 可操作范围
//        final @NotNull Collection<String> scope = new HashSet<>(1);
//        scope.add("all");
//
//        return new OAuth2AuthenticationInfo.AbstractUserAuthentication.UserDetails(true
//                , authorities
//                , this.clientId
//                , scope
//                , user.getUsername());
//    }
//
//    @NotNull
//    private AbstractSecurityUser getUserForTest() {
//        /*final UserDto newUser = userTask.selectUserByUserid(id());
//        final User user = newUser.dtoId(username(), password());
//        final Collection<GrantedAuthority> authorities = new HashSet<>(1);
//
//        final List<Map<String, Object>> roleMapList = securityUserService.selectRoleByUsername(user.getUsername());
//        for (Map<String, Object> roleMap : roleMapList) {
//            authorities.add(new SimpleGrantedAuthority((String) roleMap.get("role_code")));
//        }
//
//        return new SecurityUser(user.getUsername()
//                , passwordEncoder.encode(user.getPassword())
//                , authorities
//                , !Account.StatusVo.DESTRUCTION.equals(user.getStatus())
//                , !Account.StatusVo.LOCKED.equals(user.getStatus())
//                , Account.StatusVo.NORMAL.equals(user.getStatus())
//                , !user.isEmpty());*/
//        return (AbstractSecurityUser) userDetailsService.loadUserByUsername(username());
//    }
//
//    /**
//     * 获取测试用的 {@link Log} 对象
//     *
//     * @param securityUser {@link User}
//     * @param operator     {@link SecurityUser}
//     * @param logVo        {@link HandleType.LogVo}
//     * @return {@link Log}
//     */
//    @NotNull
//    private Log getEntityForTest(@NotNull /*User*/AbstractSecurityUser securityUser, @NotNull /*SecurityUser*/SecurityUserTestClass operator, @NotNull HandleType.LogVo logVo)
//            throws IllegalArgumentException {
//        if (null == securityUser) {
//            throw new IllegalArgumentException("非法参数: <param>user</param>");
//        }
//        /*if (null == operator || operator.isEmpty()) {
//            throw new IllegalArgumentException("非法参数: <param>operator</param>");
//        }*/
//        if (null == operator || !operator.isNormal()) {
//            throw new IllegalArgumentException("非法参数: <param>operator</param>");
//        }
//        if (null == logVo) {
//            throw new IllegalArgumentException("非法参数: <param>logVo</param>");
//        }
//        final @NotNull User user = userReadEvent.selectUserByUsername(securityUser.getUsername());
//        final @NotNull SecurityUser securityOperator = userReadEvent.selectSecurityUserByUsername(operator.getUsername());
//
//        final UserAccountOperationInfo operator_OperationInfo = userReadEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());
//        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无有效的账户操作记录
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "操作者"
//                    , "无有效的账户操作记录"
//                    , operator
//                    , operator_OperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return Log.Factory.User.LOG.create(null
//                , null
//                , logVo
//                , user
//                , securityOperator
//                , operator_OperationInfo);
//    }
//
//    /**
//     * 获取测试用的用户信息
//     *
//     * @param securityUser {@link AbstractSecurityUser}
//     * @return {@link UserDto}
//     * @throws IllegalAccessException
//     * @see UserDto
//     */
//    @NotNull
//    private /*BasicUserDto*/UserDto getUserInfo(@NotNull AbstractSecurityUser securityUser)
//            throws IllegalAccessException {
//        /*return userDetailsService.getUserInfo(user);*/
//        final User user = userEvent.selectUserByUsername(securityUser.getUsername());
//        if (null == user || user.isEmpty()) {
//            throw new IllegalArgumentException("非法输入:<param>securityUser</param> <- 无效的用户");
//        }
//
//        final UserAccountOperationInfo userAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(user.getUsername());
//        final UserPersonInfo userPersonInfo = userEvent.selectUserPersonInfoByUsername(user.getUsername());
//
//        return UserDto.Factory.USER_DTO.create(user, userAccountOperationInfo, userPersonInfo);
//    }
//
//    @NotNull
//    private String ip() {
//        return "127.0.0.0";
//    }
//
//    @NotNull
//    public String id() {
//        return "402880e56fb72000016fb72014fc0000";
//    }
//
//    @NotNull
//    public String password() {
//        return "test123";
//    }
//
//    @NotNull
//    public String username() {
//        return "测试20200118132850";
//    }
//
//    @Test
//    @Transactional
//    public void contextLoads() {
//        Assert.notNull(logTask, "获取测试单元失败");
//        Assert.notNull(userTask, "获取测试单元失败");
//    }
//
//    @Test
//    @Transactional
//    public void selectAll() {
//        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
//        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();
//
//        TaskResult<List<Log>> result = logTask.selectAll(1, 10, operator_OAuth2_UserDetails);
//        Assert.isTrue(result.isSuccess()
//                , String.format("===== selectAll(int pageCount, int pageSize) -> false => 【%s】", result));
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void selectCount() {
//        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
//        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();
//
//        TaskResult<Long> result = logTask.selectCount(10, operator_OAuth2_UserDetails);
//        Assert.isTrue(result.isSuccess()
//                , String.format("===== selectCount(int pageSize) -> false => 【%s】", result));
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void selectLogById()
//            throws IllegalAccessException {
//        // 获取必要的测试用身份信息
//        final SecurityUserTestClass operator = operator();
//        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
//        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();
//
//        //===== userTask =====//
//
//        final @NotNull AbstractSecurityUser newUser = getUserForTest();
//        final @NotNull UserDto newUserDto = getUserInfo(newUser);
//
//        @NotNull TaskResult<UserDto> newUser_TaskResult;
//        Assert.isTrue((newUser_TaskResult = userTask.registerUser(newUserDto, password(), operator_OAuth2_UserDetails)).isSuccess()
//                , String.format("registerUser(User) -> false\nnewUser_TaskResult => %s", newUser_TaskResult));
//
//        //===== logTask =====//
//
//        @NotNull Log newEntity = getEntityForTest(newUser, operator, HandleType.LogVo.USER__USER__ADD);
//        @NotNull TaskResult<Log> taskResult_newEntity;
//        Assert.isTrue((taskResult_newEntity = logTask.insert(newEntity, operator_OAuth2_UserDetails)).isSuccess()
//                , "===== insert(...) -> false!");
//
//        //=== selectLogById(...)
//        @NotNull TaskResult<Log> result = logTask.selectLogById(String.valueOf(taskResult_newEntity.data.getId())
//                , operator_OAuth2_UserDetails);
//        Assert.isTrue(result.isSuccess()
//                , String.format("===== selectLogById(...) -> false => 【%s】", result));
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void selectLogByUsername()
//            throws IllegalAccessException {
//        // 获取必要的测试用身份信息
//        final SecurityUserTestClass operator = operator();
//        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
//        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();
//
//        //===== userTask =====//
//
//        final @NotNull AbstractSecurityUser newUser = getUserForTest();
//        final @NotNull UserDto newUserDto = getUserInfo(newUser);
//
//        @NotNull TaskResult<UserDto> newUser_TaskResult;
//        Assert.isTrue((newUser_TaskResult = userTask.registerUser(newUserDto, password(), /*operator*/operator_OAuth2_UserDetails)).isSuccess()
//                , String.format("insert(User user) -> false\nnewUser_TaskResult => %s", newUser_TaskResult));
//
//        //===== logTask =====//
//
//        @NotNull Log newEntity = getEntityForTest(newUser, operator, HandleType.LogVo.USER__USER__ADD);
//        Assert.isTrue(logTask.insert(newEntity, operator_OAuth2_UserDetails).isSuccess()
//                , "===== insert(...) -> false!");
//
//        //=== selectLogByUsername(...)
//        @NotNull TaskResult<List<Log>> result = logTask.selectLogByUsername(newUserDto.getUsername()
//                , 1
//                , 10
//                , operator_OAuth2_UserDetails);
//        Assert.isTrue(result.isSuccess()
//                , String.format("===== selectLogByUsername(...) -> false => 【%s】", result));
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void selectCountByUsername()
//            throws IllegalAccessException {
//        // 获取必要的测试用身份信息
//        final SecurityUserTestClass operator = operator();
//        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
//        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();
//
//        //===== userTask =====//
//
//        final @NotNull AbstractSecurityUser newUser = getUserForTest();
//        final @NotNull UserDto newUserDto = getUserInfo(newUser);
//        Assert.notNull(newUser
//                , "User.Factory.USER -> create(..) -> null");
//        Assert.isTrue(userTask.registerUser(newUserDto, password(), /*operator*/operator_OAuth2_UserDetails).isSuccess()
//                , "insert(User user) -> false");
//
//        //===== logTask =====//
//
//        //=== insert(Log log)
//        @NotNull Log newEntity = getEntityForTest(newUser, operator, HandleType.LogVo.USER__USER__ADD);
//        @NotNull TaskResult<Log> taskResult_newEntity;
//        Assert.isTrue((taskResult_newEntity = logTask.insert(newEntity, operator_OAuth2_UserDetails)).isSuccess()
//                , String.format("===== insert(...) -> false => 【%s】", taskResult_newEntity));
//
//        //=== selectCountByUsername(String, int)
//        TaskResult<Long> result = logTask.selectCountByUsername(newUserDto.getUsername()
//                , 10
//                , operator_OAuth2_UserDetails);
//        Assert.isTrue(result.isSuccess()
//                , String.format("===== selectCountByUsername(String, int) -> false => 【%s】", result));
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void insert()
//            throws IllegalAccessException {
//        // 获取必要的测试用身份信息
//        final SecurityUserTestClass operator = operator();
//        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
//        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();
//
//        //===== userTask =====//
//
//        final @NotNull AbstractSecurityUser newUser = getUserForTest();
//        final @NotNull UserDto newUserDto = getUserInfo(newUser);
//        Assert.notNull(newUser
//                , "User.Factory.USER -> create(..) -> null");
//        Assert.isTrue(userTask.registerUser(newUserDto, password(), operator_OAuth2_UserDetails).isSuccess()
//                , "insert(User user) -> false");
//
//        //===== logTask =====//
//
//        @NotNull Log newEntity = getEntityForTest(newUser, operator, HandleType.LogVo.USER__USER__ADD);
//        final @NotNull TaskResult<Log> result;
//        Assert.isTrue((result = logTask.insert(newEntity, operator_OAuth2_UserDetails)).isSuccess()
//                , String.format("===== insert(...) -> false => 【%s】", result));
//        System.out.println(result);
//    }
//
//    @Test
//    @Transactional
//    public void delete()
//            throws IllegalAccessException {
//        // 获取必要的测试用身份信息
//        final SecurityUserTestClass operator = operator();
//        // 获取必要的测试用[操作者 - 身份验证令牌信息 - 用户认证凭据 - 详细信息]
//        final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull UserDetails operator_OAuth2_UserDetails = operator_OAuth2_UserDetails();
//
//        //===== userTask =====//
//
//        final @NotNull AbstractSecurityUser newUser = getUserForTest();
//        final @NotNull UserDto newUserDto = getUserInfo(newUser);
//        Assert.notNull(newUser
//                , "User.Factory.USER -> create(..) -> null");
//        Assert.isTrue(userTask.registerUser(newUserDto, password(), operator_OAuth2_UserDetails).isSuccess()
//                , "insert(User user) -> false");
//
//        //===== logTask =====//
//
//        //=== insert(Log)
//        @NotNull Log newEntity = getEntityForTest(newUser, operator, HandleType.LogVo.USER__USER__ADD);
//        @NotNull TaskResult<Log> taskResult_newEntity;
//        Assert.isTrue((taskResult_newEntity = logTask.insert(newEntity, operator_OAuth2_UserDetails)).isSuccess()
//                , String.format("===== insert(Log) -> false => 【%s】", taskResult_newEntity));
//
//        //=== delete(String)
//        final @NotNull TaskResult<Boolean> result;
//        Assert.isTrue((result = logTask.delete(String.valueOf(taskResult_newEntity.data.getId()), operator_OAuth2_UserDetails)).isSuccess()
//                , String.format("===== delete(String) -> false => 【%s】", result));
//        System.out.println(newEntity);
//    }
//
//}
