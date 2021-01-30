package github.com.suitelhy.dingding.app.domain.entity;

import github.com.suitelhy.dingding.app.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.app.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.app.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.app.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.app.infrastructure.domain.vo.Message;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 聊天消息
 */
@Entity
@Table(name = "chat_message")
public class ChatMessage
        extends AbstractEntityModel<String> {

    private static final long serialVersionUID = 1L;

    // 消息Id
    @GeneratedValue(generator = "ENTITY_ID_STRATEGY")
    @GenericGenerator(name = "ENTITY_ID_STRATEGY", strategy = "uuid")
    @Id
    private String id;

    // 发送消息的用户的id
    @Column(name = "send_user_id", nullable = false)
    private String sendUserId;

    // 接收消息的用户的id
    @Column(name = "accept_user_id", nullable = false)
    private String acceptUserId;

    // 消息文本内容
    @Column(name = "content_text", nullable = false)
    private String contentText;

    // 消息附件内容（关联id）
    @Column(name = "content_attach")
    private String contentAttach;

    // 消息接收状态
    @Column(name = "accept_status", nullable = false)
    private Message.ChatMessageVo.Status.AcceptStatusVo acceptStatus;

    // 数据入库时间
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
        if (!Validator.USER.sendUserId(sendUserId)) {
            return false;
        }
        if (!Validator.USER.acceptUserId(acceptUserId)) {
            return false;
        }
        if (!Validator.USER.contentText(contentText)) {
            return false;
        }
        if (!Validator.USER.contentAttach(contentAttach)) {
            return false;
        }
        if (!Validator.USER.acceptStatus(acceptStatus)) {
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
        return Validator.USER.id(id);
    }

    //===== Entity Validator =====//
    public enum Validator implements EntityValidator<ChatMessage, String> {
        USER(User.class, User.Validator.USER);

        //===== 外键设计 (仅限也仅应该为 Entity 的唯一标识) (模块化实现) =====//
        private final ForeignEntityValidator FOREIGN_VALIDATOR;

        <E extends EntityModel<ID>, ID> Validator(@NotNull Class<E> foreignEntityClazz
                , @NotNull EntityValidator<E, ID> foreignValidator) {
            this.FOREIGN_VALIDATOR = new ForeignEntityValidator(foreignEntityClazz, foreignValidator);
        }
        //==========//

        public boolean id(String id) {
            return EntityValidator.id(id);
        }

        public boolean sendUserId(String sendUserId) {
            return USER.FOREIGN_VALIDATOR.foreignId(User.class, sendUserId);
        }

        public boolean acceptUserId(String acceptUserId) {
            return USER.FOREIGN_VALIDATOR.foreignId(User.class, acceptUserId);
        }

        public boolean contentText(String contentText) {
            return null != contentText && contentText.length() <= 300;
        }

        public boolean contentAttach(String contentAttach) {
            return null == contentAttach || contentAttach.length() <= 255;
        }

        public boolean acceptStatus(Message.ChatMessageVo.Status.AcceptStatusVo acceptStatus) {
            return null != acceptStatus;
        }

    }

    //===== Entity Factory =====//
    public ChatMessage() {
    }

    private ChatMessage(@NotNull String id
            , @NotNull String sendUserId
            , @NotNull String acceptUserId
            , @NotNull String contentText
            , @Nullable String contentAttach
            , @NotNull Message.ChatMessageVo.Status.AcceptStatusVo acceptStatus) {
        if (null != id && !Validator.USER.id(id)) {
            //--- 更新业务
            throw new IllegalArgumentException("非法参数: <param>id</param>");
        }
        if (!Validator.USER.sendUserId(sendUserId)) {
            throw new IllegalArgumentException("非法参数: <param>sendUserId</param>");
        }
        if (!Validator.USER.acceptUserId(acceptUserId)) {
            throw new IllegalArgumentException("非法参数: <param>acceptUserId</param>");
        }
        if (!Validator.USER.contentText(contentText)) {
            throw new IllegalArgumentException("非法参数: <param>contentText</param>");
        }
        if (!Validator.USER.contentAttach(contentAttach)) {
            throw new IllegalArgumentException("非法参数: <param>contentAttach</param>");
        }
        if (!Validator.USER.acceptStatus(acceptStatus)) {
            throw new IllegalArgumentException("非法参数: <param>acceptStatus</param>");
        }
        this.id = id;
        this.sendUserId = sendUserId;
        this.acceptUserId = acceptUserId;
        this.contentText = contentText;
        this.contentAttach = contentAttach;
        this.acceptStatus = acceptStatus;
    }

    public enum Factory implements EntityFactory<ChatMessage> {
        CHAT_MESSAGE;

        public ChatMessage create(@NotNull String sendUserId
                , @NotNull String acceptUserId
                , @NotNull String contentText
                , @Nullable String contentAttach
                , @NotNull Message.ChatMessageVo.Status.AcceptStatusVo acceptStatus) {
            return new ChatMessage(null, sendUserId, acceptUserId
                    , contentText, contentAttach, acceptStatus);
        }

    }

    //===== getter and setter =====//
    public String getSendUserId() {
        return sendUserId;
    }

    public String getAcceptUserId() {
        return acceptUserId;
    }

    public Message.ChatMessageVo.Status.AcceptStatusVo getAcceptStatus() {
        return acceptStatus;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getContentAttach() {
        return contentAttach;
    }

    public void setContentAttach(String contentAttach) {
        this.contentAttach = contentAttach;
    }

    public LocalDateTime getDataTime() {
        return dataTime;
    }
    //==========//

}
