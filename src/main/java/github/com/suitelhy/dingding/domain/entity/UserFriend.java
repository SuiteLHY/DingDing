package github.com.suitelhy.dingding.domain.entity;

import github.com.suitelhy.dingding.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.infrastructure.domain.model.EntityValidator;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用户 - 好友关系
 */
@Entity
@Table(name = "user_friend")
public class UserFriend
        extends AbstractEntityModel<String> {

    private static final long serialVersionUID = 1L;

    // id
    @GeneratedValue(generator = "USER_FRIEND_ID_STRATEGY")
    @GenericGenerator(name = "USER_FRIEND_ID_STRATEGY", strategy = "uuid")
    @Id
    private String id;

    // 用户Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    // 好友用户Id
    @Column(name = "friend_user_id", nullable = false)
    private String friendUserId;

    // 数据更新时间 (数据库管理)
    @Column(name = "data_time", nullable = false)
    private LocalDateTime dataTime;

    //===== AbstractEntityModel =====//
    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the entity.
     */
    @Override
    public String id() {
        return this.id;
    }

    /**
     * 是否符合业务要求 <- Entity 对象
     *
     * @return
     * @Description 需要实现类实现该抽象方法
     */
    @Override
    public boolean isEntityLegal() {
        if (!Validator.USER.userId(userId)) {
            return false;
        }
        if (!Validator.USER.friendUserId(friendUserId)) {
            return false;
        }
        return true;
    }

    /**
     * 校验 Entity - ID
     *
     * @param id <method>id()</method>
     * @return
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     */
    @Override
    protected boolean validateId(@NotNull String id) {
        return Validator.USER.id(this.id);
    }

    //===== EntityValidator =====//
    enum Validator implements EntityValidator<UserFriend, String> {
        USER(User.class, User.Validator.USER);

        //===== 外键设计 (仅限也仅应该为 Entity 的唯一标识) (模块化实现) =====//
        private final ForeignEntityValidator FOREIGN_VALIDATOR;

        <E extends EntityModel<ID>, ID> Validator(@NotNull Class<E> foreignEntityClazz
                , @NotNull EntityValidator<E, ID> foreignValidator) {
            this.FOREIGN_VALIDATOR = new ForeignEntityValidator(foreignEntityClazz, foreignValidator);
        }
        //==========//

        @Override
        public boolean id(String id) {
            return EntityValidator.id(id);
        }

        public boolean userId(String userId) {
            return USER.FOREIGN_VALIDATOR.foreignId(User.class, userId);
        }

        public boolean friendUserId(String friendUserId) {
            return USER.FOREIGN_VALIDATOR.foreignId(User.class, friendUserId);
        }

    }

    //===== EntityFactory =====//
    public UserFriend() {}

    private UserFriend(@Nullable String id
            , @NotNull String userId
            , @NotNull String friendUserId) {
        if (null != id && !Validator.USER.id(id)) {
            //--- 更新业务
            throw new IllegalArgumentException("非法参数: <param>id</param>");
        }
        if (!Validator.USER.userId(userId)) {
            throw new IllegalArgumentException("非法参数: <param>userId</param>");
        }
        if (!Validator.USER.friendUserId(friendUserId)) {
            throw new IllegalArgumentException("非法参数: <param>friendUserId</param>");
        }
        this.id = id;
        this.userId = userId;
        this.friendUserId = friendUserId;
    }

    public enum Factory implements EntityFactory<UserFriend> {
        USER_FRIEND;

        /**
         * 获取 Entity 实例
         *
         * @return
         */
        public UserFriend create(@NotNull String userId
                , @NotNull String friendUserId) {
            return new UserFriend(null, userId, friendUserId);
        }

    }

    //===== getter =====//
    public String getUserId() {
        return userId;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public LocalDateTime getDataTime() {
        return dataTime;
    }
    //==========//

}
