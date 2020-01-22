package github.com.suitelhy.dingding.domain.entity;

import github.com.suitelhy.dingding.infrastructure.domain.model.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用户 <- 附件
 *
 * @Description 用于存储用户直接关联的各种附件;
 *-> 其中数据占用小的直接存储数据内容, 大的则存储文件系统上的存放地址链接.
 */
@Entity
@Table(name = "user_attach")
public class UserAttach
        extends AbstractEntityModel<String> {

    private static final long serialVersionUID = 1L;

    // 附件Id
    @GeneratedValue(generator = "USER_ATTACH_ID_STRATEGY")
    @GenericGenerator(name = "USER_ATTACH_ID_STRATEGY", strategy = "uuid")
    @Id
    private String id;

    // 用户Id
    @Column(nullable = false)
    private String userid;

    // 附件：内容数据（小）/存放路径（大）
    @Column(name = "attach_content", nullable = false)
    private String attachContent;

    // 附件类型
    @Column(name = "attach_type", nullable = false)
    @Convert(converter = github.com.suitelhy.dingding.infrastructure.domain.vo.UserAttach.AttachTypeVo.Converter.class)
    private github.com.suitelhy.dingding.infrastructure.domain.vo.UserAttach.AttachTypeVo attachType;

    // 附件描述信息
    @Column(name = "attach_description")
    private String attachDescription;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time", nullable = false)
    @Transient
    private LocalDateTime dataTime;

    //===== Entity Validator =====//
    public enum Validator implements EntityValidator<UserAttach, String> {
        USER(User.class, User.Validator.USER);

        //===== 外键设计 (仅限也仅应该为 Entity 的唯一标识) (模块化实现) =====//
        private final ForeignEntityValidator FOREIGN_VALIDATOR;

        <E extends EntityModel<ID>, ID> Validator(@NotNull Class<E> foreignEntityClazz
                , @NotNull EntityValidator<E, ID> foreignValidator) {
            this.FOREIGN_VALIDATOR = new ForeignEntityValidator(foreignEntityClazz, foreignValidator);
        }
        //==========//

        /**
         * 校验 Entity - ID
         *
         * @param id
         * @return
         */
        @Override
        public boolean id(@NotNull String id) {
            return EntityValidator.id(id);
        }

        public boolean userid(@NotNull String userid) {
            return USER.FOREIGN_VALIDATOR.foreignId(User.class, userid);
        }

        public boolean attachContent(@NotNull String attachContent) {
            return null != attachContent && !"".equals(attachContent.trim());
        }

        public boolean attachType(@NotNull github.com.suitelhy.dingding.infrastructure.domain.vo.UserAttach.AttachTypeVo attachTypeVo) {
            return null != attachTypeVo;
        }

        public boolean attachDescription(@Nullable String attachDescription) {
            //--- 暂无业务设计约束
            return true;
        }

    }

    //===== Entity Factory =====//
    public enum Factory implements EntityFactory<UserAttach> {
        USER_ATTACH;

        public UserAttach create(@NotNull String id
                , @NotNull String userid
                , @NotNull String attachContent
                , @NotNull github.com.suitelhy.dingding.infrastructure.domain.vo.UserAttach.AttachTypeVo attachTypeVo
                , @Nullable String attachDescription) {
            if (!Validator.USER.id(id)) {
                throw new IllegalArgumentException("非法参数: <param>id</param>");
            }
            return new UserAttach(id, userid, attachContent
                    , attachTypeVo, attachDescription);
        }

        public UserAttach update(@NotNull String userid
                , @NotNull String attachContent
                , @NotNull github.com.suitelhy.dingding.infrastructure.domain.vo.UserAttach.AttachTypeVo attachTypeVo
                , @Nullable String attachDescription) {
            return new UserAttach(null, userid, attachContent
                    , attachTypeVo, attachDescription);
        }

    }

    //===== Entity Model =====//
    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the entity.
     */
    @Override
    public @NotNull String id() {
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
        if (!Validator.USER.userid(userid)) {
            return false;
        }
        if (!Validator.USER.attachContent(attachContent)) {
            return false;
        }
        if (!Validator.USER.attachType(attachType)) {
            return false;
        }
        if (!Validator.USER.attachDescription(attachDescription)) {
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
    protected @NotNull boolean validateId(@NotNull String id) {
        return Validator.USER.id(id);
    }

    //===== base constructor =====//
    public UserAttach() {}

    private UserAttach(@Nullable String id
            , @NotNull String userid
            , @NotNull String attachContent
            , @NotNull github.com.suitelhy.dingding.infrastructure.domain.vo.UserAttach.AttachTypeVo attachTypeVo
            , @Nullable String attachDescription) {
        if (null != id) {
            //--- 更新业务
            if (!validateId(id)) {
                throw new IllegalArgumentException("非法参数: <param>id</param>");
            }
        }
        if (!Validator.USER.userid(userid)) {
            throw new IllegalArgumentException("非法参数: <param>userid</param>");
        }
        if (!Validator.USER.attachContent(attachContent)) {
            throw new IllegalArgumentException("非法参数: <param>attachContent</param>");
        }
        if (!Validator.USER.attachType(attachTypeVo)) {
            throw new IllegalArgumentException("非法参数: <param>attachType</param>");
        }
        if (!Validator.USER.attachDescription(attachDescription)) {
            throw new IllegalArgumentException("非法参数: <param>attachDescription</param>");
        }
        this.id = id;
        this.userid = userid;
        this.attachContent = attachContent;
        this.attachType = attachTypeVo;
        this.attachDescription = attachDescription;
    }

    //===== getter and setter =====//
    public String getUserid() {
        return userid;
    }

    public String getAttachContent() {
        return attachContent;
    }

    public void setAttachContent(String attachContent) {
        this.attachContent = attachContent;
    }

    public github.com.suitelhy.dingding.infrastructure.domain.vo.UserAttach.AttachTypeVo getAttachType() {
        return attachType;
    }

    public void setAttachType(github.com.suitelhy.dingding.infrastructure.domain.vo.UserAttach.AttachTypeVo attachTypeVo) {
        this.attachType = attachTypeVo;
    }

    public String getAttachDescription() {
        return attachDescription;
    }

    public void setAttachDescription(String attachDescription) {
        this.attachDescription = attachDescription;
    }

}
