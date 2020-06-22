package github.com.suitelhy.dingding.core.domain.entity.security;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用户 - 安全相关信息
 *
 * @Description 关联的安全信息.
 *
 * @author Suite
 */
@Entity
@Table(name = "SECURITY_USER")
public class SecurityUser
        extends AbstractEntityModel<String> {

    private static final long serialVersionUID = 1L;

    // 用户ID
    @GeneratedValue(generator = "USER_ID_STRATEGY")
    @GenericGenerator(name = "USER_ID_STRATEGY", strategy = "uuid")
    @Id
    @Column(name = "user_id")
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
    @Column(nullable = false, unique = true)
    private String username;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time")
    @Transient
    private LocalDateTime dataTime;

    //===== Entity Model =====//

    @Override
    public @NotNull String id() {
        return this.getUsername();
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
        return !Validator.USER.userId(this.userId)
                || !this.isEntityLegal();
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
        return Validator.USER.username(this.username)
                && Validator.USER.status(this.status); // 用户名称
    }

    /**
     * 校验 Entity - ID
     *
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     *
     * @param entityId      {@link this#id()}
     * @return
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
    public SecurityUser() {}

    //===== entity factory =====//

    /**
     * 创建/更新用户 -> Entity对象
     *
     * @Description 添加(<param> id </param>为 null) / 更新(<param>id</param>合法)用户.
     * @param userid            用户ID
     * @param username          用户名称
     * @param status            账户状态
     * @throws IllegalArgumentException
     */
    private SecurityUser(@NotNull String userid
            , @NotNull String username
            , @NotNull Account.StatusVo status)
            throws IllegalArgumentException {
        if (null == userid) {
            //--- 添加用户功能
        } else {
            //--- 更新用户功能
            if (!SecurityUser.Validator.USER.userId(userid)) {
                //-- 非法输入: 用户ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        .concat(" -> 非法输入: 用户ID"));
            }
        }
        if (!SecurityUser.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 用户名称"));
        }
        if (!SecurityUser.Validator.USER.status(status)) {
            //-- 非法输入: 账户状态
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 账户状态"));
        }

        // 用户ID
        this.setUserId(userid);
        // 账户状态
        this.setStatus(status);
        // 用户名称
        this.setUsername(username);
    }

    public enum Factory
            implements EntityFactory<SecurityUser> {
        USER;

        /**
         * 创建用户
         *
         * @param userId        用户ID
         * @param username      用户名称
         * @param status        账户状态
         * @return 可为 null, 此时输入参数非法.
         * @see github.com.suitelhy.dingding.core.domain.entity.User
         * @throws IllegalArgumentException     此时 <param>id</param> 非法.
         */
        public SecurityUser create(@NotNull String userId
                , @NotNull String username
                , @NotNull Account.StatusVo status)
                throws IllegalArgumentException {
            return new SecurityUser(userId, username, status);
        }

        /**
         * 创建用户
         *
         * @param user          {@link github.com.suitelhy.dingding.core.domain.entity.User}
         * @return 可为 null, 此时输入参数非法.
         * @throws IllegalArgumentException     此时 <param>user</param> 非法.
         */
        public SecurityUser create(@NotNull User user)
                throws IllegalArgumentException {
            if (null == user || user.isEmpty()) {
                //-- 非法输入: 非法用户
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        .concat(" -> 非法输入: 非法用户"));
            }

            return new SecurityUser(user.getUserid()
                    , user.getUsername()
                    , user.getStatus());
        }

        /**
         * 更新用户
         *
         * @param userId            用户ID
         * @param username          用户名称
         * @param status            账户状态    {@link Account.StatusVo}
         * @throws IllegalArgumentException 此时 <param>id</param> 非法
         * @return 可为 null, 此时输入参数非法
         */
        public SecurityUser update(@NotNull String userId
                , @NotNull String username
                , @NotNull Account.StatusVo status)
                throws IllegalArgumentException {
            if (!Validator.USER.userId(userId)) {
                //-- 非法输入: 用户ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        .concat(" -> 非法输入: 用户ID"));
            }

            return new SecurityUser(userId, username, status);
        }

        /**
         * 销毁
         *
         * @param securityUser
         * @return {<code>true</code> : <b>销毁成功</b>
         *->      , <code>false</code> : <b>销毁失败; 此时 <param>user</param></b> 无效或无法销毁}
         */
        public boolean delete(@NotNull SecurityUser securityUser) {
            if (null != securityUser && !securityUser.isEmpty()) {
                securityUser.setStatus(Account.StatusVo.DESTRUCTION);
                return true;
            }
            return false;
        }

    }

    //===== getter & setter =====//

    @NotNull
    public String getUserId() {
        return userId;
    }

    private boolean setUserId(@NotNull String userId) {
        if (Validator.USER.userId(userId)) {
            this.userId = userId;
            return true;
        }
        return false;
    }

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

    @NotNull
    public String getUsername() {
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
