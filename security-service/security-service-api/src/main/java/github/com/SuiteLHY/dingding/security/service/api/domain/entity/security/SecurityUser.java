package github.com.suitelhy.dingding.security.service.api.domain.entity.security;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntity;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * （安全认证）用户
 *
 * @Description 用户关联的安全信息.
 *
 * @author Suite
 */
@Entity
@Table(name = "SECURITY_USER")
public class SecurityUser
        extends AbstractEntity<String> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 ID
     */
    @GeneratedValue(generator = "USER_ID_STRATEGY")
    @GenericGenerator(name = "USER_ID_STRATEGY", strategy = "uuid")
    @Id
    @Column(name = "user_id", length = 64)
    private String userId;

    /**
     * 账号状态
     */
    @Column(nullable = false)
    @Convert(converter = Account.StatusVo.Converter.class)
    private Account.StatusVo status;

    /**
     * 用户名称
     *
     * @Description 业务唯一.
     */
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time")
    @Transient
    private LocalDateTime dataTime;

    //===== Entity Model =====//

    @Override
    public @NotNull
    String id() {
        return this.username;
    }

    /**
     * 是否无效
     *
     * @return
     * @Description 保证 User 的基本业务实现中的合法性.
     */
    @Override
    public boolean isEmpty() {
        return !Validator.USER.userId(this.userId)
                || !this.isEntityLegal();
    }

    /**
     * 是否符合基础数据合法性要求
     *
     * @return
     * @Description 只保证 User 的数据合法, 不保证 User 的业务实现中的合法性.
     */
    @Override
    public boolean isEntityLegal() {
        return Validator.USER.username(this.username)
                && Validator.USER.status(this.status); // 用户名称
    }

    /**
     * 校验 Entity - ID
     *
     * @param entityId {@link this#id()}
     * @return
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
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
            implements EntityValidator<SecurityUser, String> {
        USER;

        @Override
        public boolean validateId(@NotNull SecurityUser entity) {
            return null != entity.id()
                    && this.entity_id(entity.id());
        }

        @Override
        public boolean entity_id(@NotNull String entityId) {
            return this.username(entityId);
        }

        public boolean userId(@NotNull String userId) {
            return User.Validator.USER.userid(userId);
        }

        public boolean username(@NotNull String username) {
            return User.Validator.USER.username(username);
        }

        /*public boolean roleCodes(@NotNull String roleCodes) {
            if (null != roleCodes) {
                for (String each : roleCodes.split(",")) {
                    if (!SecurityRole.Validator.ROLE.code(each)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }*/

        public boolean status(@NotNull Account.StatusVo status) {
            return User.Validator.USER.status(status);
        }

    }

    //===== base constructor =====//

    /**
     * @Description 仅用于持久化注入.
     */
    public SecurityUser() {
    }

    //===== entity factory =====//

    /**
     * 创建/更新用户 -> Entity对象
     *
     * @param userid   用户 ID
     * @param username 用户名称
     * @param status   账户状态
     * @throws IllegalArgumentException
     * @Description 添加(< param > userid < / param > 为 null) / 更新(<param>userid</param>合法)用户.
     */
    private SecurityUser(@NotNull String userid
            , @NotNull String username
            , @NotNull Account.StatusVo status)
            throws IllegalArgumentException {
        if (null == userid) {
            //--- 添加用户功能
        } else {
            //--- 更新用户功能
            if (!Validator.USER.userId(userid)) {
                //-- 非法输入: [用户 ID]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "[用户 ID]"
                        , userid
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
        }
        if (!Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (!Validator.USER.status(status)) {
            //-- 非法输入: 账户状态
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "账户状态"
                    , status
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        // 用户ID
        this.setUserId(userid);
        // 账户状态
        this.setStatus(status);
        // 用户名称
        this.setUsername(username);
    }

    public enum Factory
            implements EntityFactoryModel<SecurityUser> {
        USER;

        /**
         * 创建用户
         *
         * @param userId   用户ID
         * @param username 用户名称
         * @param status   账户状态
         * @return {@link SecurityUser}
         * @throws IllegalArgumentException 此时 {@param userId}/{@param username}/{@param status} 非法.
         * @see User
         */
        public @NotNull
        SecurityUser create(@NotNull String userId
                , @NotNull String username
                , @NotNull Account.StatusVo status)
                throws IllegalArgumentException {
            return new SecurityUser(userId, username, status);
        }

        /**
         * 创建用户
         *
         * @param user {@link User}
         *
         * @return {@link SecurityUser}
         *
         * @throws IllegalArgumentException 此时 {@param user} 非法.
         */
        public @NotNull SecurityUser create(@NotNull User user)
                throws IllegalArgumentException
        {
            if (null == user || user.isEmpty()) {
                //-- 非法输入: 非法用户
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "非法用户"
                        , user
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new SecurityUser(/*user.getUserid()*/null
                    , user.getUsername()
                    , user.getStatus());
        }

        /**
         * 更新用户
         *
         * @param userId   用户ID
         * @param username 用户名称
         * @param status   账户状态    {@link Account.StatusVo}
         * @return {@link SecurityUser}
         * @throws IllegalArgumentException 此时 {@param userId} 非法
         */
        public @NotNull
        SecurityUser update(@NotNull String userId
                , @NotNull String username
                , @NotNull Account.StatusVo status)
                throws IllegalArgumentException {
            if (!Validator.USER.userId(userId)) {
                //-- 非法输入: [用户 ID]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "[用户 ID]"
                        , userId
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new SecurityUser(userId, username, status);
        }

        /**
         * 销毁
         *
         * @param securityUser {@link SecurityUser}
         * @return 操作结果
         * {
         * {@code true} : <b>销毁成功</b>,
         * {@code false} : <b>销毁失败</b> -> 此时 {@param securityUser} 无效或无法销毁.
         * }
         */
        public boolean delete(@NotNull SecurityUser securityUser) {
            if (null != securityUser && !securityUser.isEmpty()) {
                return securityUser.setStatus(Account.StatusVo.DESTRUCTION);
            }
            return false;
        }

        /**
         * 获取空对象
         *
         * @return 非 {@code null}.
         */
        @Override
        public @NotNull
        SecurityUser createDefault() {
            return new SecurityUser();
        }

    }

    //===== getter & setter =====//

    public @NotNull
    String getUserId() {
        return userId;
    }

    private boolean setUserId(@NotNull String userId) {
        if (Validator.USER.userId(userId)) {
            this.userId = userId;
            return true;
        }
        return false;
    }

    public @NotNull
    Account.StatusVo getStatus() {
        return status;
    }

    private boolean setStatus(Account.StatusVo statusVo) {
        if (Validator.USER.status(statusVo)) {
            this.status = statusVo;
            return true;
        }
        return false;
    }

    public @NotNull
    String getUsername() {
        return username;
    }

    private boolean setUsername(String username) {
        if (Validator.USER.username(username)) {
            this.username = username;
            return true;
        }
        return false;
    }

}
