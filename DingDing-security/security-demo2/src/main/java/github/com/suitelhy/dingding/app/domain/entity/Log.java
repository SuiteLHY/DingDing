package github.com.suitelhy.dingding.app.domain.entity;

import github.com.suitelhy.dingding.app.infrastructure.util.CalendarController;
import github.com.suitelhy.dingding.app.infrastructure.web.util.NetUtil;
import github.com.suitelhy.dingding.app.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.app.infrastructure.domain.util.EntityUtil;
import github.com.suitelhy.dingding.app.infrastructure.domain.util.VoUtil;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

/**
 * 日志记录 Entity
 */
@Entity
@Table
public class Log extends AbstractEntityModel<Long> {

    private static final long serialVersionUID = 1L;

    // 日志记录 - 编号
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    // 记录的详细信息
    @Column
    private String detail;

    @Transient
    private Calendar dataTime;

    // 登录的IP地址
    @Column
    private String ip;

    /**
     * 操作时间
     *
     * @Pattern CalendarController.DEFAULT_DATE_FORMAT
     */
    @Column(nullable = false)
    private String time;

    /**
     * 操作类型: {1:用户注册, 2:用户更新, 3:用户异常, 4:用户销毁, 5:用户数据删除
     * -> , 11:用户登入, 12:用户登出}
     *
     * @Description 操作类型大体上分为: 用户信息操作和用户登录操作.
     */
    @Column
    @Convert(converter = HandleType.LogVo.Converter.class)
    private HandleType.LogVo type;

    // 用户 - 用户ID
    @Column(nullable = false)
    private String userid;

    //===== base constructor =====//

    /**
     * 仅用于 ORM 注入
     */
    public Log() {
    }

    //===== entity model =====//
    @Override
    public Long id() {
        return this.getId();
    }

//    @Override
//    public boolean isEmpty() {
//        return !Validator.LOG.validateId(this)
//                || !isEntityLegal()
//                || (null != isEntityPersistence() && !isEntityPersistence());
//    }

    /**
     * 是否符合业务要求
     *
     * @return
     */
    @Override
    public boolean isEntityLegal() {
        return Validator.LOG.detail(this.detail)
                && Validator.LOG.ip(this.ip)
                && Validator.LOG.time(this.time)
                && Validator.LOG.type(this.type)
                && Validator.USER.userid(this.userid);
    }

    /**
     * 校验 Entity - ID
     *
     * @param id <method>id()</method>
     * @return
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     */
    @Override
    protected @NotNull
    boolean validateId(@NotNull Long id) {
        return Validator.LOG.id(id);
    }

    //===== entity validator =====//
    public enum Validator implements EntityValidator<Log, Long> {
        LOG, USER(User.class, User.Validator.USER);

        //===== 外键设计 (仅限也仅应该为 Entity 的唯一标识) (简单实现) =====//
        /*public final EntityValidator FOREIGN_VALIDATOR;

        public final Class FOREIGN_ID_CLAZZ;

        Validator() {
            this.FOREIGN_VALIDATOR = null;
            this.FOREIGN_ID_CLAZZ = null;
        }

        <E extends EntityModel<ID>, ID> Validator(@NotNull EntityValidator<E, ID> foreignValidator
                , @NotNull Class<ID> idClass) {
            this.FOREIGN_VALIDATOR = foreignValidator;
            this.FOREIGN_ID_CLAZZ = idClass;
        }

        private boolean foreignId(Object id) {
            if (null != this.FOREIGN_VALIDATOR
                    && null != id
                    && id.getClass() == FOREIGN_ID_CLAZZ) {
                return this.FOREIGN_VALIDATOR.id(id);
            }
            return false;
        }*/
        //===== 外键设计 (仅限也仅应该为 Entity 的唯一标识) (模块化实现) =====//
        private final ForeignEntityValidator FOREIGN_VALIDATOR;

        Validator() {
            this.FOREIGN_VALIDATOR = null;
        }

        <E extends EntityModel<ID>, ID> Validator(@NotNull Class<E> foreignEntityClazz
                , @NotNull EntityValidator<E, ID> foreignValidator) {
            this.FOREIGN_VALIDATOR = new ForeignEntityValidator(foreignEntityClazz, foreignValidator);
        }

        //==========//
        @Override
        public boolean id(@NotNull Long id) {
            return null != id && id > 0L;
        }

        public boolean id(@NotNull String id) {
            return EntityUtil.Regex.validateId(id);
        }

        public boolean detail(@Nullable String detail) {
            //--- 暂无业务设计约束
            return true;
        }

        public boolean ip(@Nullable String ip) {
            return NetUtil.validateIpAddress(ip);
        }

        public boolean time(@Nullable String time) {
            return null != time && CalendarController.isParse(time);
        }

        public boolean type(@Nullable HandleType.LogVo type) {
            if (null == type) {
                return null != VoUtil.getVoByValue(HandleType.LogVo.class, null);
            }
            return true;
        }

        public boolean userid(String userid) {
            return /*Validator.USER.foreignId(userid)*/
                    USER.FOREIGN_VALIDATOR.foreignId(User.class, userid);
        }

    }

    //===== entity factory =====//
    private Log(@Nullable String id
            , @Nullable String detail
            , @NotNull String ip
            , @NotNull String time
            , @NotNull HandleType.LogVo type
            , @NotNull String userid) {
        if (null == id) {
            //--- <param>id</param>为 null 时, 对应添加日志记录功能.
        } else {
            //--- 对应更新日志记录功能.
            if (!Validator.LOG.id(id)) {
                //-- 非法输入: 日志记录ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        + " -> 非法输入: 日志记录ID");
            }
        }
        if (!Validator.LOG.detail(detail)) {
            //-- 非法输入: 记录的详细信息
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 记录的详细信息");
        }
        if (!Validator.LOG.ip(ip)) {
            //-- 非法输入: 登录的IP地址
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 登录的IP地址");
        }
        if (!Validator.LOG.time(time)) {
            //-- 非法输入: 操作时间
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 操作时间");
        }
        if (!Validator.LOG.type(type)) {
            //-- 非法输入: 操作类型
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 操作类型");
        }
        if (!Validator.USER.userid(userid)) {
            //-- 非法输入:
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 用户 - 用户ID");
        }
        if (null != id) {
            this.setId(Long.parseLong(id));
        }
        this.setDetail(detail);
        this.setIp(ip);
        this.setTime(time);
        this.setType(type);
        this.setUserid(userid);
    }

    public enum Factory implements EntityFactory<Log> {
        USER_LOG;

        /**
         * 获取 Entity 实例
         *
         * @param detail 记录的详细信息
         * @param ip     登录的IP地址
         * @param time   操作时间
         * @param type   操作类型
         * @param userid 用户 - 用户ID
         * @return
         */
        @NotNull
        public Log create(@Nullable String detail
                , @NotNull String ip
                , @NotNull String time
                , @NotNull HandleType.LogVo type
                , @NotNull String userid) {
            return new Log(null, detail, ip
                    , time, type, userid);
        }

    }

    //===== getter and setter =====//
    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getIp() {
        return ip;
    }

    private void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public HandleType.LogVo getType() {
        return type;
    }

    private void setType(HandleType.LogVo type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    private void setUserid(String userid) {
        this.userid = userid;
    }

}
