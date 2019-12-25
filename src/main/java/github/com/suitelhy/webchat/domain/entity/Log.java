package github.com.suitelhy.webchat.domain.entity;

import github.com.suitelhy.webchat.domain.entity.annotation.SuiteColumn;
import github.com.suitelhy.webchat.domain.entity.policy.DBPolicy;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * 日志记录信息
 *
 */
@Repository("log")
public class Log implements Serializable {

    // 日志记录 - 编号
    @SuiteColumn
    private String id;

    // 记录的详细信息
    @SuiteColumn
    private String detail;

    // 登录的IP地址
    @SuiteColumn
    private String ip;

    // 操作时间
    @SuiteColumn
    private String time;

    // 操作类型
    @SuiteColumn
    private String type;

    // 用户 - 用户ID
    @SuiteColumn
    private String userid;

    //===== constructor =====//
    private Log() {
        // 新用户 - 用户ID, 使用UUID策略
        this.setId(DBPolicy.uuid());
    }

    //===== getter and setter =====//
    public String getId() {
        return id;
    }

    void setId(String id) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
