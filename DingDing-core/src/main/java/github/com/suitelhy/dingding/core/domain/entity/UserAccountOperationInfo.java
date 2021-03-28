//package github.com.suitelhy.dingding.core.domain.entity;
//
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntity;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
//import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
//import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
//import github.com.suitelhy.dingding.core.infrastructure.web.util.NetUtil;
//import org.hibernate.annotations.GenericGenerator;
//import org.springframework.lang.Nullable;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import java.time.LocalDateTime;
//
///**
// * [用户 -> 账户操作基础记录]
// *
// * @Description 关于 id 生成策略, 个人倾向于使用"代理键" ———— 所选策略还是应该交由数据库来实现.
// * @Reference · {@link <a href="https://dzone.com/articles/persisting-natural-key-entities-with-spring-data-j">Persisting Natural Key Entities With Spring Data JPA</a>}
// */
//@Entity
//@Table(name = "USER_ACCOUNT_OPERATION_INFO")
//public class UserAccountOperationInfo
//        extends AbstractEntity<String> {
//
//    private static final long serialVersionUID = 1L;
//
//    // 数据 ID
//    @GeneratedValue(generator = "USER_ACCOUNT_OPERATION_INFO_ID_STRATEGY")
//    @GenericGenerator(name = "USER_ACCOUNT_OPERATION_INFO_ID_STRATEGY", strategy = "uuid")
//    @Id
//    @Column(length = 64)
//    private String id;
//
//    // 用户名称
//    @Column(nullable = false, unique = true, length = 50)
//    private String username;
//
//    // 数据更新时间 (由数据库管理)
//    @Column(name = "data_time")
//    @Transient
//    private LocalDateTime dataTime;
//
//    // 最后登陆 IP
//    @Column(nullable = false, length = 50)
//    private String ip;
//
//    // 最后登录时间
//    @Column(name = "last_login_time", nullable = false, length = 50)
//    private String lastLoginTime;
//
//    // 注册时间
//    @Column(name = "registration_time", nullable = false, length = 50)
//    private String registrationTime;
//
//    //===== Entity Model =====//
//
////    /**
////     * 判断关联用户
////     *
////     * @Issue 不符合约束声明原则, 更换实现形式.
////     *-> {@code javax.validation.ConstraintDeclarationException: HV000151: A method overriding another method must not redefine the parameter constraint configuration, but method UserAccountOperationInfo#equals(User) redefines the configuration of Object#equals(Object).}
////     *
////     * @param user  {@link User}
////     *
////     * @return
////     */
////    public boolean equals(@NotNull User user) {
////        if (null != user && user.isEntityLegal() && null != user.id()
////                && this.isEntityLegal()) {
////            return this.id().equals(user.id());
////        }
////        return false;
////    }
//
//    /**
//     * 等效比较
//     *
//     * @param obj {@link Object}
//     * @return 判断结果
//     * @Description 在 {@link super#equals(Object)} 的基础上添加[判断关联用户]的功能.
//     * @Solution · 约束声明原则 -> {@code javax.validation.ConstraintDeclarationException: HV000151: A method overriding another method must not redefine the parameter constraint configuration, but method UserAccountOperationInfo#equals(User) redefines the configuration of Object#equals(Object).}
//     */
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof User && ((User) obj).isEntityLegal() && null != ((User) obj).id()
//                && this.isEntityLegal()) {
//            return this.id().equals(((User) obj).id());
//        }
//        return super.equals(obj);
//    }
//
//    /**
//     * 判断关联用户
//     *
//     * @param user {@link User}
//     * @return {@link Boolean#TYPE}
//     */
//    public boolean equals(@NotNull User user) {
//        if (null != user && user.isEntityLegal() && null != user.id()
//                && this.isEntityLegal()) {
//            return this.id().equals(user.id());
//        }
//        return false;
//    }
//
//    @Override
//    public @NotNull
//    String id() {
//        return this.username;
//    }
//
//    /**
//     * 是否无效
//     *
//     * @return
//     * @Description 保证 User 的基本业务实现中的合法性.
//     */
//    @Override
//    public boolean isEmpty() {
//        return !Validator.USER.id(this.id)
//                || !this.isEntityLegal();
//    }
//
//    /**
//     * 是否符合基础数据合法性要求
//     *
//     * @return
//     * @Description 只保证 User 的数据合法, 不保证 User 的业务实现中的合法性.
//     */
//    @Override
//    public boolean isEntityLegal() {
//        return Validator.USER.ip(this.ip)                                   // 最后登陆IP
//                && Validator.USER.lastLoginTime(this.lastLoginTime)         // 最后登录时间
//                && Validator.USER.registrationTime(this.registrationTime)   // 注册时间
//                && Validator.USER.username(this.username);                  // 用户名称
//    }
//
//    /**
//     * 校验 Entity - ID
//     *
//     * @param entityId <method>id()</method>
//     * @return boolean
//     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
//     */
//    @Override
//    protected boolean validateId(@NotNull String entityId) {
//        return Validator.USER.entity_id(entityId);
//    }
//
//    //===== Entity Validator =====//
//
//    /**
//     * 用户 - 属性校验器
//     *
//     * @Description 各个属性的基础校验(注意 : ≠ 完全校验).
//     */
//    public enum Validator
//            implements EntityValidator<UserAccountOperationInfo, String> {
//        USER;
//
//        @Override
//        public boolean validateId(@NotNull UserAccountOperationInfo entity) {
//            return null != entity.id()
//                    && entity_id(entity.id());
//        }
//
//        @Override
//        public boolean entity_id(@NotNull String entityId) {
//            return this.username(entityId);
//        }
//
//        public boolean id(@NotNull String id) {
//            return EntityUtil.Regex.validateId(id)
//                    && id.length() < 65;
//        }
//
//        public boolean username(@NotNull String username) {
//            return User.Validator.USER.username(username)
//                    && username.length() < 51;
//        }
//
//        public boolean ip(@NotNull String ip) {
//            return NetUtil.validateIpAddress(ip)
//                    && ip.length() < 51;
//        }
//
//        public boolean lastLoginTime(@NotNull String lastLoginTime) {
//            return null != lastLoginTime
//                    && CalendarController.isParse(lastLoginTime)
//                    && lastLoginTime.length() < 51;
//        }
//
//        public boolean registrationTime(@NotNull String registrationTime) {
//            return null != registrationTime
//                    && CalendarController.isParse(registrationTime)
//                    && registrationTime.length() < 51;
//        }
//
//    }
//
//    //===== base constructor =====//
//
//    /**
//     * 仅用于持久化注入
//     */
//    public UserAccountOperationInfo() {
//    }
//
//    //===== entity factory =====//
//
//    /**
//     * 创建/更新记录 -> Entity对象
//     *
//     * @param id               数据 ID
//     * @param username         用户名称
//     * @param ip               最后登陆IP
//     * @param lastLoginTime    最后登录时间
//     * @param registrationTime 注册时间
//     * @throws IllegalArgumentException
//     * @Description 添加 (<param>id</param>为 null) / 更新 (<param>id</param>合法) 记录.
//     */
//    private UserAccountOperationInfo(@Nullable String id
//            , @NotNull String username
//            , @NotNull String ip
//            , @NotNull String lastLoginTime
//            , @NotNull String registrationTime)
//            throws IllegalArgumentException {
//        if (null == id) {
//            //--- 添加数据
//        } else {
//            //--- 更新数据
//            if (!Validator.USER.id(id)) {
//                //-- 非法输入: 数据ID
//                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , "数据 ID"
//                        , id
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            // 数据ID
//            this.setId(id);
//        }
//        if (!Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.USER.ip(ip)) {
//            //-- 非法输入: 最后登陆IP
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "最后登陆IP"
//                    , ip
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.USER.lastLoginTime(lastLoginTime)) {
//            //-- 非法输入: 最后登录时间
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "最后登录时间"
//                    , lastLoginTime
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.USER.registrationTime(registrationTime)) {
//            //-- 非法输入: 注册时间
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "注册时间"
//                    , registrationTime
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        // 用户名称
//        this.setUsername(username);
//        // 最后登陆IP
//        this.setIp(ip);
//        // 最后登录时间
//        this.setLastLoginTime(lastLoginTime);
//        // 注册时间
//        this.setRegistrationTime(/*new CalendarController().toString()*/registrationTime);
//    }
//
//    public enum Factory
//            implements EntityFactoryModel<UserAccountOperationInfo> {
//        USER;
//
//        /**
//         * 创建
//         *
//         * @param username         用户名称
//         * @param ip               最后登陆IP
//         * @param lastLoginTime    最后登录时间
//         * @param registrationTime 注册时间
//         * @return {@link UserAccountOperationInfo}
//         * @throws IllegalArgumentException
//         */
//        public UserAccountOperationInfo create(@NotNull String username
//                , @NotNull String ip
//                , @NotNull String lastLoginTime
//                , @NotNull String registrationTime)
//                throws IllegalArgumentException {
//            return new UserAccountOperationInfo(null, username, ip
//                    , lastLoginTime, registrationTime);
//        }
//
//        /**
//         * 创建
//         *
//         * @param id               数据 ID
//         * @param username         用户名称
//         * @param ip               最后登陆IP
//         * @param lastLoginTime    最后登录时间
//         * @param registrationTime 注册时间
//         * @return {@link UserAccountOperationInfo}
//         * @throws IllegalArgumentException
//         */
//        public UserAccountOperationInfo update(@NotNull String id
//                , @NotNull String username
//                , @NotNull String ip
//                , @NotNull String lastLoginTime
//                , @NotNull String registrationTime)
//                throws IllegalArgumentException {
//            return new UserAccountOperationInfo(id, username, ip
//                    , lastLoginTime, registrationTime);
//        }
//
//        /**
//         * 获取空对象
//         *
//         * @return 非 {@code null}.
//         */
//        @Override
//        public UserAccountOperationInfo createDefault() {
//            return new UserAccountOperationInfo();
//        }
//
//    }
//
//    //===== getter & setter =====//
//
//    @Nullable
//    public String getId() {
//        return id;
//    }
//
//    private boolean setId(@NotNull String id) {
//        if (Validator.USER.id(id)) {
//            this.id = id;
//            return true;
//        }
//        return false;
//    }
//
//    public String getIp() {
//        return ip;
//    }
//
//    public boolean setIp(@NotNull String ip) {
//        if (Validator.USER.ip(ip)) {
//            this.ip = ip;
//            return true;
//        }
//        return false;
//    }
//
//    public String getLastLoginTime() {
//        return lastLoginTime;
//    }
//
//    public boolean setLastLoginTime(@NotNull String lastLoginTime) {
//        if (Validator.USER.lastLoginTime(lastLoginTime)) {
//            this.lastLoginTime = lastLoginTime;
//            return true;
//        }
//        return false;
//    }
//
//    public String getRegistrationTime() {
//        return registrationTime;
//    }
//
//    public boolean setRegistrationTime(@NotNull String registrationTime) {
//        if (Validator.USER.registrationTime(registrationTime)) {
//            this.registrationTime = registrationTime;
//            return true;
//        }
//        return false;
//    }
//
//    @NotNull
//    public String getUsername() {
//        return username;
//    }
//
//    private boolean setUsername(@NotNull String username) {
//        if (Validator.USER.username(username)) {
//            this.username = username;
//            return true;
//        }
//        return false;
//    }
//
//}
