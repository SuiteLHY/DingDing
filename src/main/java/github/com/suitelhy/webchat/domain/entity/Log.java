package github.com.suitelhy.webchat.domain.entity;

import github.com.suitelhy.webchat.infrastructure.domain.annotation.SuiteColumn;
import github.com.suitelhy.webchat.infrastructure.domain.annotation.SuiteTable;
import github.com.suitelhy.webchat.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.webchat.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.webchat.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.webchat.infrastructure.domain.policy.DBPolicy;
import github.com.suitelhy.webchat.infrastructure.util.CalendarController;

/**
 * 日志记录 Entity
 *
 */
@SuiteTable("log")
public class Log extends AbstractEntityModel<String> {

    private static final long serialVersionUID = 1L;

    // 日志记录 - 编号
    @SuiteColumn(nullable = false)
    private String id;

    // 记录的详细信息
    @SuiteColumn
    private transient String detail;

    // 登录的IP地址
    @SuiteColumn
    private transient String ip;

    /**
     * 操作时间
     * @Pattern CalendarController.DEFAULT_DATE_FORMAT
     */
    @SuiteColumn(nullable = false)
    private transient String time;

    /**
     * 操作类型: {1:用户注册, 2:用户更新, 3:用户异常, 4:用户销毁, 5:用户数据删除
     *-> , 11:用户登入, 12:用户登出}
     * @Description 操作类型大体上分为: 用户信息操作和用户登录操作.
     */
    @SuiteColumn
    private transient Integer type;

    // 用户 - 用户ID
    @SuiteColumn(nullable = false)
    private transient String userid;

    //===== entity model =====//
    @Override
    public String id() {
        return this.getId();
    }

    @Override
    public boolean isEmpty() {
        return null == id()
                || "".equals(id().trim())
                || null == this.getTime()
                || !CalendarController.isParse(this.getTime())
                || null == this.getUserid()
                || "".equals(this.getUserid().trim());
    }

    //===== entity factory =====//
    private Log() {
        this.setId(DBPolicy.uuid());
    }

    public enum Factory implements EntityFactory<Log> {
        SINGLETON;

        /**
         * 获取 Entity 实例
         *
         * @return
         */
        @Override
        public Log create() {
            return new Log();
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

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
