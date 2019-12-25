package github.com.suitelhy.webchat.domain.entity;

import github.com.suitelhy.webchat.domain.entity.annotation.SuiteColumn;
import github.com.suitelhy.webchat.domain.entity.policy.EntityMapperTable;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * 用户信息
 */
@Repository("user")
public class User implements Serializable {

    // 用户ID
    @SuiteColumn
    private String userid;

    // 用户 - 年龄
    @SuiteColumn
    private Integer age;

    // 注册时间
    @SuiteColumn
    private String firsttime;

    // 最后登陆IP
    @SuiteColumn
    private String ip;

    // 最后登录时间
    @SuiteColumn
    private String lasttime;

    // 用户 - 昵称
    @SuiteColumn
    private String nickname;

    // 用户 - 密码
    @SuiteColumn
    private String password;

    // 用户 - 简介
    @SuiteColumn
    private String profile;

    // 用户 - 头像
    @SuiteColumn
    private String profilehead;

    // 用户 - 性别
    @SuiteColumn
    private Integer sex;

    // 账号状态 -> [{1:正常}, {0:异常&禁用}]
    @SuiteColumn
    private Integer status;

    //===== entity map table business =====//
    public static final EntityMapper ENTITY_MAPPER = EntityMapper.getInstance();

    //TIPS: 后期可以添加必要的缓存来优化 SQL 生产方法的性能
    public static class EntityMapper extends EntityMapperTable<User> {

        private static class EntityMapperFactory {
            public static final EntityMapper INSTANCE = new EntityMapper();
        }

        public static final EntityMapper getInstance() {
            return EntityMapperFactory.INSTANCE;
        }

        private EntityMapper() {
            super(User.class);
        }

        // 用户 - 用户业务信息字段
        private final String[] businessFields = {getColumn("userid")
                , getColumn("age")
                , getColumn("firsttime")
                , getColumn("lasttime")
                , getColumn("nickname")
                , getColumn("password")
                , getColumn("profile")
                , getColumn("profilehead")
                , getColumn("status")};

        public String[] getBusinessFields() {
            return businessFields;
        }

    }

    //===== getter & setter =====//
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFirsttime() {
        return firsttime;
    }

    public void setFirsttime(String firsttime) {
        this.firsttime = firsttime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProfilehead() {
        return profilehead;
    }

    public void setProfilehead(String profilehead) {
        this.profilehead = profilehead;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
