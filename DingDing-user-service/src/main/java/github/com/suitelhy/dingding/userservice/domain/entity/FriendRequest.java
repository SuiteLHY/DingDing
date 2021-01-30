package github.com.suitelhy.dingding.userservice.domain.entity;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntity;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用户 <- 好友请求
 */
@Entity
@Table(name = "friend_request")
public class FriendRequest
        extends AbstractEntity<String> {

    private static final long serialVersionUID = 1L;

    // id
    @GeneratedValue(generator = "FRIEND_REQUEST_ID_STRATEGY")
    @GenericGenerator(name = "FRIEND_REQUEST_ID_STRATEGY", strategy = "uuid")
    @Id
    private String id;

    // 发送请求的用户Id
    @Column(name = "send_user_id", nullable = false)
    private String sendUserId;

    // 接收请求的用户Id
    @Column(name = "accept_user_id", nullable = false)
    private String acceptUserId;

    // 请求发送时间
    @Column(name = "request_data_time", nullable = false)
    private LocalDateTime requestDataTime;

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
        if (!Validator.USER.sendUserId(sendUserId)) {
            return false;
        }
        if (!Validator.USER.acceptUserId(acceptUserId)) {
            return false;
        }
        if (!Validator.USER.requestDataTime(requestDataTime)) {
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
        return Validator.USER.entity_id(id);
    }

    //===== EntityValidator =====//
    public enum Validator
            implements EntityValidator<FriendRequest, String> {
        USER/*(User.class, User.Validator.USER)*/;

        /*//===== 外键设计 (仅限也仅应该为 Entity 的唯一标识) (模块化实现) =====//
        private final ForeignEntityValidator FOREIGN_VALIDATOR;

        <E extends EntityModel<ID>, ID> Validator(@NotNull Class<E> foreignEntityClazz
                , @NotNull EntityValidator<E, ID> foreignValidator) {
            this.FOREIGN_VALIDATOR = new ForeignEntityValidator(foreignEntityClazz, foreignValidator);
        }
        //==========//*/

        @Override
        public boolean entity_id(String id) {
            return EntityValidator.entity_id(id);
        }

        public boolean sendUserId(String sendUserId) {
            /*return USER.FOREIGN_VALIDATOR.foreignId(User.class, sendUserId);*/
            return User.Validator.USER.userid(sendUserId);
        }

        public boolean acceptUserId(String acceptUserId) {
            /*return USER.FOREIGN_VALIDATOR.foreignId(User.class, acceptUserId);*/
            return User.Validator.USER.userid(acceptUserId);
        }

        public boolean requestDataTime(LocalDateTime requestDataTime) {
            return null != requestDataTime;
        }

    }

    //===== EntityValidator =====//
    public FriendRequest() {
    }

    private FriendRequest(@Nullable String id
            , @NotNull String sendUserId
            , @NotNull String acceptUserId
            , @NotNull LocalDateTime requestDataTime) {
        if (null != id && !Validator.USER.entity_id(id)) {
            //--- 更新业务
            throw new IllegalArgumentException("非法参数: <param>id</param>");
        }
        if (!Validator.USER.sendUserId(sendUserId)) {
            throw new IllegalArgumentException("非法参数: <param>sendUserId</param>");
        }
        if (!Validator.USER.acceptUserId(acceptUserId)) {
            throw new IllegalArgumentException("非法参数: <param>acceptUserId</param>");
        }
        if (!Validator.USER.requestDataTime(requestDataTime)) {
            throw new IllegalArgumentException("非法参数: <param>requestDataTime</param>");
        }
        this.id = id;
        this.sendUserId = sendUserId;
        this.acceptUserId = acceptUserId;
        this.requestDataTime = requestDataTime;
    }

    public enum Factory
            implements EntityFactoryModel<FriendRequest> {
        FRIEND_REQUEST;

        /**
         * 获取 Entity 实例
         *
         * @return
         */
        public FriendRequest create(@Nullable String id
                , @NotNull String sendUserId
                , @NotNull String acceptUserId
                , @NotNull LocalDateTime requestDataTime) {
            return new FriendRequest(null, sendUserId, acceptUserId
                    , requestDataTime);
        }

        /**
         * 获取空对象
         *
         * @return 非 {@code null}.
         */
        @NotNull
        @Override
        public FriendRequest createDefault() {
            return new FriendRequest();
        }

    }

    //===== getter =====//
    public String getSendUserId() {
        return sendUserId;
    }

    public String getAcceptUserId() {
        return acceptUserId;
    }

    public LocalDateTime getRequestDataTime() {
        return requestDataTime;
    }
    //==========//

}
