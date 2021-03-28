package github.com.suitelhy.dingding.security.service.api.domain.entity;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntity;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntitySecurity;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Human;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.security.service.api.infrastructure.domain.util.EntityUtil;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用户 - 基础信息
 *
 * @Description
 * · 关于数据脱敏的自定义注解实现, 可参考:
 * {@link <a href="https://blog.csdn.net/liufei198613/article/details/79009015">注解实现json序列化的时候自动进行数据脱敏_liufei198613的博客-CSDN博客</a>}
 * {@linkplain <a href="https://dzone.com/articles/persisting-natural-key-entities-with-spring-data-j">Persisting Natural Key Entities With Spring Data JPA</a> 关于 id 生成策略, 个人倾向于使用"代理键" ———— 所选策略还是应该交由数据库来实现.}
 */
@Entity
@Table
public class User
        extends AbstractEntity<String> {

    private static final long serialVersionUID = 1L;

    // 用户ID
    @GeneratedValue(generator = "USER_ID_STRATEGY")
    @GenericGenerator(name = "USER_ID_STRATEGY", strategy = "uuid")
    @Id
    @Column(length = 64)
    private String userid;

    // 用户名称
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time")
    @Transient
    private LocalDateTime dataTime;

    /**
     * 用户密码
     *
     * @Description (不可逆)加密后的密文.
     */
    @Column(nullable = false, length = 64)
    private String password;

    /**
     * 用户密码_明文
     *
     * @Description 最大长度为20.
     *
     * @Design (不可逆)加密前的明文; 仅生成新的用户时使用.
     */
    @Nullable
    @Transient
    private String passwordPlaintext;

    // 账号状态
    @Column(nullable = false)
    @Convert(converter = Account.StatusVo.Converter.class)
    private Account.StatusVo status;

    //===== Entity Model =====//

    @Override
    public @NotNull
    String id() {
        return this.username;
    }

    /**
     * 是否无效
     *
     * @Description 保证 User 的基本业务实现中的合法性.
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        /*return super.isEmpty()
                && !Account.StatusVo.NORMAL.equals(this.status);*/
        return ! Validator.USER.userid(this.userid)
                || ! this.isEntityLegal()
                || ! Account.StatusVo.NORMAL.equals(this.status);
    }

    /**
     * 是否符合基础数据合法性要求
     *
     * @Description 只保证 User 的数据合法, 不保证 User 的业务实现中的合法性.
     *
     * @return
     */
    @Override
    public boolean isEntityLegal() {
        return Validator.USER.password(this.password)       // 用户密码（密文）
                && (null == this.passwordPlaintext || Validator.USER.passwordPlaintext(this.passwordPlaintext)) // 用户密码（明文）
                && Validator.USER.username(this.username)   // 用户名称
                && Validator.USER.status(this.status)/* 账号状态 */;
    }

    /**
     * 校验 Entity - ID
     *
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     *
     * @param entityId <method>id()</method>
     *
     * @return {@link Boolean#TYPE}
     */
    @Override
    protected boolean validateId(@NotNull String entityId) {
        return Validator.USER.entity_id(entityId);
    }

    //===== Entity Validator =====//

    /**
     * 用户 - 属性校验器
     *
     * @Description 各个属性的基础校验(注意 : ≠ 完全校验).
     */
    public enum Validator
            implements EntityValidator<User, String> {
        USER;

        @Override
        public boolean validateId(@NotNull User entity) {
            return null != entity.id()
                    && entity_id(entity.id());
        }

        @Override
        public boolean entity_id(@NotNull String entityId) {
            return this.username(entityId);
        }

        public boolean userid(@NotNull String userid) {
            return EntityUtil.Regex.validateId(userid)
                    && userid.length() < 65;
        }

        public boolean username(@NotNull String username) {
            return EntityUtil.Regex.validateUsername(username)
                    && username.length() < 31;
        }

//        public boolean nickname(@NotNull String nickname) {
//            return UserPersonInfo.Validator.USER.nickname(nickname);
//        }
//
//        public boolean age(@Nullable Integer age) {
//            return UserPersonInfo.Validator.USER.age(age);
//        }
//
//        public boolean registrationTime(@NotNull String registrationTime) {
//            return UserAccountOperationInfo.Validator.USER.registrationTime(registrationTime);
//        }
//
//        public boolean ip(@NotNull String ip) {
//            /*return NetUtil.validateIpAddress(ip);*/
//            return UserAccountOperationInfo.Validator.USER.ip(ip);
//        }
//
//        public boolean lastLoginTime(@NotNull String lastLoginTime) {
//            /*return null != lasttime && CalendarController.isParse(lasttime);*/
//            return UserAccountOperationInfo.Validator.USER.lastLoginTime(lastLoginTime);
//        }

        public boolean password(@NotNull String password) {
            return EntityUtil.Regex.validateUserPassword(password)
                    && password.length() < 65;
        }

        /**
         * 用户密码（明文） <- 校验
         *
         * @Design 仅创建新的用户时执行该校验; 其他情况下应该验证 {@param passwordPlaintext} 为 {@literal null} 且予以放行.
         *
         * @param passwordPlaintext
         *
         * @return
         */
        public boolean passwordPlaintext(@NotNull String passwordPlaintext) {
            return EntityUtil.Regex.validateUserPasswordPlaintext(passwordPlaintext)
                    && passwordPlaintext.length() < 21;
        }

//        public boolean introduction(String introduction) {
//            return UserPersonInfo.Validator.USER.introduction(introduction);
//        }
//
//        public boolean faceImage(String faceImage) {
//            return UserPersonInfo.Validator.USER.faceImage(faceImage);
//        }
//
//        public boolean sex(Human.SexVo sex) {
//            return UserPersonInfo.Validator.USER.sex(sex);
//        }

        public boolean status(@NotNull Account.StatusVo status) {
            if (null == status) {
                return null != VoUtil.getInstance().getVoByValue(Account.StatusVo.class, null);
            }
            return true;
        }

    }

    //===== base constructor =====//

    /**
     * 仅用于持久化注入
     */
    public User() {
    }

    //===== entity factory =====//

    /**
     * 创建/更新用户 -> Entity对象
     *
     * @Description 添加 (<param>id</param>为 null) / 更新 (<param>id</param>合法) 用户.
     *
     * @param userid            [用户 ID]
     * @param username          用户名称
     * @param password          用户密码（密文）    {@param passwordPlaintext} 为 {@literal null} 时非空; 否则不执行业务校验.
     * @param passwordPlaintext 用户密码（明文）    非 {@literal null} 时, {@param password} 将不被使用.
     *
     * @throws IllegalArgumentException
     */
    private User(@NotNull String userid
            , @NotNull String username
            , @Nullable String password
            , @Nullable String passwordPlaintext)
            throws IllegalArgumentException
    {
        if (null == userid) {
            //--- 添加用户功能
            if (! Validator.USER.passwordPlaintext(passwordPlaintext)
                    || ! this.setPasswordPlaintext(passwordPlaintext)/* 用户密码（明文） */) {
                //-- 非法输入: 用户密码（明文）
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户密码（明文）"
                        , passwordPlaintext
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
        } else {
            //--- 更新用户功能
            if (! Validator.USER.userid(userid)) {
                //-- 非法输入: [用户 ID]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "[用户 ID]"
                        , userid
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (! Validator.USER.password(password)) {
                //-- 非法输入: 用户密码（密文）
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户密码（密文）"
                        , password
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            // 用户密码（密文）
            this.setPassword(password);
        }
        if (! Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! Validator.USER.password(this.password)) {
            //-- 非法输入: 用户密码（密文）
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户密码（密文）"
                    , this.password
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        // 用户ID
        this.setUserid(userid);
        // 用户名称
        this.setUsername(username);
        // 账号状态
        this.setStatus(Account.StatusVo.NORMAL);
    }

    public enum Factory
            implements EntityFactoryModel<User> {
        USER;

        /**
         * 创建
         *
         * @param username          用户名称
         * @param passwordPlaintext 用户密码（明文）
         *
         * @return {@linkplain User 业务对象}; 非 {@literal null}.
         *
         * @throws IllegalArgumentException
         */
        @NotNull
        public User create(@NotNull String username, @NotNull String passwordPlaintext)
                throws IllegalArgumentException
        {
            return new User(null, username, null
                    , passwordPlaintext);
        }

        /**
         * 更新
         *
         * @param userid   用户 ID
         * @param username 用户名称
         * @param password 用户 - 密码（密文）
         *
         * @return {@linkplain User 更新后的业务对象}; 非 {@literal null}.
         *
         * @throws IllegalArgumentException 此时 {@param id} 非法.
         */
        @NotNull
        public User update(@NotNull String userid
                , @NotNull String username
                , @NotNull String password)
                throws IllegalArgumentException
        {
            if (! Validator.USER.userid(userid)) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "用户 ID"
                        , userid
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new User(userid, username, password
                    , null);
        }

        /**
         * 销毁 Entity 实例
         *
         * @param user {@link User}
         *
         * @return {{@literal true}:<b>销毁成功</b>, {@literal false}:<b>销毁失败</b>; 此时 {@param user} 无效或无法被销毁}
         */
        public boolean delete(@NotNull User user) {
            if (null != user && ! user.isEmpty()) {
                user.setStatus(Account.StatusVo.DESTRUCTION);
                return true;
            }
            return false;
        }

        /**
         * 获取空对象
         *
         * @return {@linkplain User 业务对象}; 非 {@literal null}.
         */
        @NotNull
        @Override
        public User createDefault() {
            return new User();
        }

    }

    //===== entity security =====//

    public interface SecurityStrategy
            extends EntitySecurity {

        enum PasswordEncoder
                implements org.springframework.security.crypto.password.PasswordEncoder {
            INSTANCE(Security.PasswordEncoderVo.BCrypt);

            @NotNull
            public final Security.PasswordEncoderVo encoderVo;

            PasswordEncoder(Security.PasswordEncoderVo encoderVo) {
                this.encoderVo = encoderVo;
            }

            @Override
            public String encode(CharSequence rawPassword) {
                return encoderVo.encoder.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encoderVo.encoder.matches(rawPassword, encodedPassword);
            }
        }

    }

    /*public static void main(String[] args) {
        System.err.println(SecurityStrategy.PasswordEncoder.INSTANCE.encode("admin1"));
    }*/

    //===== business expansion =====//

    /**
     * {@linkplain User 用户} 属性的数据层面等效比较
     *
     * @Description 主要数据, 不包括时效性等数据.
     *
     * @param thatUser {@linkplain User 目标用户 (必需合法)}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    public boolean equalsOnData(@NotNull User thatUser) {
        if (null == thatUser || ! thatUser.isEntityLegal()) {
            return false;
        }
        return this.isEntityLegal()
                && ObjectUtils.nullSafeEquals(this.id(), thatUser.id())
                && ObjectUtils.nullSafeEquals(this.userid, thatUser.userid)
                && ObjectUtils.nullSafeEquals(this.username, thatUser.username)
                && ObjectUtils.nullSafeEquals(this.password, thatUser.password)
                && ObjectUtils.nullSafeEquals(this.status, thatUser.status);
    }

    /**
     * {@linkplain User 用户} 属性的数据层面等效比较
     *
     * @Description 所有数据, 包括时效性等数据; 要求所有属性完全一致.
     *
     * @param thatUser {@linkplain User 目标用户 (必需合法)}
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    public boolean equalsOnAllData(@NotNull User thatUser) {
        if (null == thatUser || ! thatUser.isEntityLegal()) {
            return false;
        }
        return this.isEntityLegal()
                && ObjectUtils.nullSafeEquals(this.id(), thatUser.id())
                && ObjectUtils.nullSafeEquals(this.userid, thatUser.userid)
                && ObjectUtils.nullSafeEquals(this.username, thatUser.username)
                && ObjectUtils.nullSafeEquals(this.password, thatUser.password)
                && ObjectUtils.nullSafeEquals(this.passwordPlaintext, thatUser.passwordPlaintext)
                && ObjectUtils.nullSafeEquals(this.status, thatUser.status)
                && ObjectUtils.nullSafeEquals(this.dataTime, thatUser.dataTime);
    }

    //===== getter & setter =====//

    @Nullable
    public String getUserid() {
        return userid;
    }

    private boolean setUserid(@NotNull String userid) {
        if (Validator.USER.userid(userid)) {
            this.userid = userid;
            return true;
        }
        return false;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    private boolean setPassword(@NotNull String password) {
        if (Validator.USER.password(password)) {
            this.password = password;
            return true;
        }
        return false;
    }

    /**
     * 设置用户的密码 (密文)
     *
     * @param password
     * @param operator
     * @param operator_userRole
     * @param operator_role
     *
     * @return {@linkplain Boolean#TYPE 操作结果}
     */
    public boolean setPassword(@NotNull String password, @NotNull SecurityUser operator, @NotNull SecurityUserRole operator_userRole
            , @NotNull SecurityRole operator_role)
    {
        Security.RoleVo operator_roleVo;
        if (Validator.USER.password(password)
                && (null != operator && ! operator.isEmpty())
                && (null != operator_userRole && ! operator_userRole.isEmpty())
                && (null != operator_role && ! operator_role.isEmpty() && null != (operator_roleVo = SecurityRole.changeIntoRoleVo(operator_role)) && operator_roleVo.isAdministrator()))
        {
            return this.setPassword(password);
        }
        return false;
    }

    @Nullable
    private String getPasswordPlaintext() {
        return null;
    }

    private boolean setPasswordPlaintext(@NotNull String passwordPlaintext) {
        if (Validator.USER.passwordPlaintext(passwordPlaintext)) {
            this.passwordPlaintext = passwordPlaintext;
            return this.setPassword(SecurityStrategy.PasswordEncoder.INSTANCE.encode(this.passwordPlaintext));
        }
        return false;
    }

    /**
     * 设置用户的密码 (明文)
     *
     * @param passwordPlaintext
     * @param operator
     * @param operator_userRole
     * @param operator_role
     *
     * @return {@linkplain Boolean#TYPE 操作结果}
     */
    public boolean setPasswordPlaintext(@NotNull String passwordPlaintext, @NotNull SecurityUser operator, @NotNull SecurityUserRole operator_userRole
            , @NotNull SecurityRole operator_role)
    {
        Security.RoleVo operator_roleVo;
        if (Validator.USER.passwordPlaintext(passwordPlaintext)
                && (null != operator && ! operator.isEmpty())
                && (null != operator_userRole && ! operator_userRole.isEmpty())
                && (null != operator_role && ! operator_role.isEmpty() && null != (operator_roleVo = SecurityRole.changeIntoRoleVo(operator_role)) && operator_roleVo.isAdministrator())
                && operator_userRole.equals(operator, operator_role))
        {
            return this.setPasswordPlaintext(passwordPlaintext);
        }
        return false;
    }

    /**
     * 判断密码（明文）是否相同
     *
     * @param passwordPlaintext 用户密码（明文）
     *
     * @return {true: <tt>密码相同</tt>, false: <tt>密码不相同</tt>, null: <tt>Entity无效</tt>}
     */
    public Boolean equalsPassword(@NotNull String passwordPlaintext) {
        if (Validator.USER.passwordPlaintext(passwordPlaintext)) {
            if (/*this.isEmpty()*/! this.isEntityLegal()) {
                return null;
            }
            /*return this.password.equals(password);*/
            return SecurityStrategy.PasswordEncoder.INSTANCE.matches(passwordPlaintext, this.password);
        }
        return false;
    }

    /*public static void main(String[] args) {
        System.err.println(SecurityStrategy.PasswordEncoder.INSTANCE
                .matches("admin1", "$2a$10$F70RcLNEJTPyvTClAy7zfeiPNbYH4qugRaPIjsfxkb39ne2eMryIe"));
    }*/

    public Account.StatusVo getStatus() {
        return status;
    }

    private boolean setStatus(Account.StatusVo statusVo) {
        if (Validator.USER.status(statusVo)) {
            this.status = statusVo;
            return true;
        }
        return false;
    }

    /**
     * 设置用户的账号状态
     *
     * @param statusVo
     * @param operator
     * @param operator_userRole
     * @param operator_role
     *
     * @return {@linkplain Boolean#TYPE 操作结果}
     */
    public boolean setStatus(@NotNull Account.StatusVo statusVo, @NotNull SecurityUser operator, @NotNull SecurityUserRole operator_userRole
            , @NotNull SecurityRole operator_role)
    {
        Security.RoleVo operator_roleVo;
        if (Validator.USER.status(statusVo)
                && (null != operator && ! operator.isEmpty())
                && (null != operator_userRole && ! operator_userRole.isEmpty())
                && (null != operator_role && ! operator_role.isEmpty() && null != (operator_roleVo = SecurityRole.changeIntoRoleVo(operator_role)) && operator_roleVo.isAdministrator())
                && operator_userRole.equals(operator, operator_role))
        {
            return this.setStatus(statusVo);
        }
        return false;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    private boolean setUsername(@NotNull String username) {
        if (Validator.USER.username(username)) {
            this.username = username;
            return true;
        }
        return false;
    }

}
