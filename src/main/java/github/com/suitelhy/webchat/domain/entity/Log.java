package github.com.suitelhy.webchat.domain.entity;

import github.com.suitelhy.webchat.domain.vo.HandleTypeVo;
import github.com.suitelhy.webchat.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.webchat.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.webchat.infrastructure.domain.policy.DBPolicy;
import github.com.suitelhy.webchat.infrastructure.util.CalendarController;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 日志记录 Entity
 *
 */
@Entity
@Table
public class Log extends AbstractEntityModel<String> {

    private static final long serialVersionUID = 1L;

    // 日志记录 - 编号
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;

    // 记录的详细信息
    @Column
    private String detail;

    // 登录的IP地址
    @Column
    private String ip;

    /**
     * 操作时间
     * @Pattern CalendarController.DEFAULT_DATE_FORMAT
     */
    @Column(nullable = false)
    private String time;

    /**
     * 操作类型: {1:用户注册, 2:用户更新, 3:用户异常, 4:用户销毁, 5:用户数据删除
     *-> , 11:用户登入, 12:用户登出}
     * @Description 操作类型大体上分为: 用户信息操作和用户登录操作.
     */
    @Column
    @Convert(converter = HandleTypeVo.Log.Converter.class)
    private HandleTypeVo.Log type;

    // 用户 - 用户ID
    @Column(nullable = false)
    private String userid;

    //===== base constructor =====//
    /**
     * 仅用于 ORM 注入
     */
    public Log() {}

    //===== entity model =====//
    @Override
    public String id() {
        return this.getId();
    }

    @Override
    public boolean isEmpty() {
        return (null == id() || "".equals(id().trim()))
                || !isLegal()
                || (null != isPersistence()
                        && !isPersistence());
    }

    /**
     * 是否符合业务要求
     *
     * @return
     */
    @Override
    public boolean isLegal() {
        return (null != this.getTime()
                        && CalendarController.isParse(this.getTime()))
                && (null != this.getUserid()
                        && !"".equals(this.getUserid().trim()));
    }

    //===== entity factory =====//
    private Log(@Nullable String id
            , @Nullable String detail
            , @NotNull String ip
            , @NotNull String time
            , @NotNull HandleTypeVo.Log type
            , @NotNull String userid) {

    }

    public enum Factory implements EntityFactory<Log> {
        USER_LOG;

        /**
         * 获取 Entity 实例
         *
         * @return <code>null</code>
         */
        @Override
        public Log create() {
            return null;
        }

        /**
         * 获取 Entity 实例
         * @param id
         * @param detail
         * @param ip
         * @param time
         * @param type
         * @param userid
         * @return
         */
        @NotNull
        public Log create(@Nullable String id
                , @Nullable String detail
                , @NotNull String ip
                , @NotNull String time
                , @NotNull HandleTypeVo.Log type
                , @NotNull String userid) {
            // ...
            return new Log(id, detail, ip
                    , time, type, userid);
        }

    }

    //===== getter and setter =====//
    public String getId() {
        return id;
    }

    private void setId(String id) {
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

    public HandleTypeVo.Log getType() {
        return type;
    }

    private void setType(HandleTypeVo.Log type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    private void setUserid(String userid) {
        this.userid = userid;
    }

}
