package github.com.suitelhy.webchat.domain.entity;

import github.com.suitelhy.webchat.infrastructure.domain.annotation.SuiteColumn;
import github.com.suitelhy.webchat.infrastructure.domain.annotation.SuiteTable;
import github.com.suitelhy.webchat.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.webchat.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.webchat.infrastructure.domain.policy.DBPolicy;
import github.com.suitelhy.webchat.infrastructure.domain.policy.JPAMapperModel;
import github.com.suitelhy.webchat.infrastructure.util.CalendarController;

/**
 * 用户信息
 *
 */
/**
 * 关于数据脱敏的自定义注解实现, 可参考: <a href="https://blog.csdn.net/liufei198613/article/details/79009015">
 *->     注解实现json序列化的时候自动进行数据脱敏_liufei198613的博客-CSDN博客</a>
 */
@SuiteTable("user")
public class User implements EntityModel<String> {

    private static final long serialVersionUID = 1L;

    // 用户ID
    @SuiteColumn(nullable = false)
    private String userid;

    // 用户 - 年龄
    @SuiteColumn
    private transient Integer age;

    // 注册时间
    @SuiteColumn(nullable = false)
    private transient String firsttime;

    // 最后登陆IP
    @SuiteColumn(nullable = false)
    private transient String ip;

    // 最后登录时间
    @SuiteColumn(nullable = false)
    private transient String lasttime;

    // 用户 - 昵称
    @SuiteColumn(nullable = false)
    private transient String nickname;

    // 用户 - 密码
    @SuiteColumn(nullable = false)
    private transient String password;

    // 用户 - 简介
    @SuiteColumn
    private transient String profile;

    // 用户 - 头像
    @SuiteColumn
    private transient String profilehead;

    // 用户 - 性别
    @SuiteColumn
    private transient Integer sex;

    // 账号状态 -> {0:已注销, 1:正常, 2:异常&禁用}
    @SuiteColumn(nullable = false)
    private transient Integer status;

    //===== Entity Model =====//
    @Override
    public String id() {
        return this.getUserid();
    }

    @Override
    public boolean equals(Object obj) {
        return EntityModel.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return EntityModel.hashCode(this);
    }

    @Override
    public boolean isEmpty() {
        return null == id()
                || "".equals(id().trim())
                || !"1".equals(this.getStatus())
                || null == this.getFirsttime()
                || !CalendarController.isParse(this.getFirsttime());
    }

    @Override
    public String toString() {
        return EntityModel.toString(this);
    }

    //===== entity mapper =====//
    //----- 根据是否需要个性化操作, 选择如下代码块拓展 Entity 映射操作
    //-> 或者使用 Entity 映射模板提供的静态方法.
    public static final UserMapper MAPPER = UserMapper.getInstance();

    public static final class UserMapper extends JPAMapperModel<User> {

        //=== 工厂模式实现单例 ===//
        private UserMapper() {
            super(User.class);
        }

        private static final class JPAMapperFactory {
            public static final UserMapper INSTANCE = new UserMapper();
        }

        public static final UserMapper getInstance() {
            return JPAMapperFactory.INSTANCE;
        }
        //======//

        // 用户 - 用户业务信息字段
        private final String[] businessFieldNames = {getColumn("userid")
                , getColumn("age")
                , getColumn("firsttime")
                , getColumn("lasttime")
                , getColumn("nickname")
                , getColumn("password")
                , getColumn("profile")
                , getColumn("profilehead")
                , getColumn("status")};

        public String[] getBusinessFieldNames() {
            return businessFieldNames;
        }

    }

    //===== entity factory =====//
    private User() {
        this.setUserid(DBPolicy.uuid());
        this.setStatus(1);
        this.setFirsttime(new CalendarController().toCalendarString());
    }

    public enum Factory implements EntityFactory<User> {
        SINGLETON;

        /**
         * 获取 Entity 实例
         *
         * @return
         */
        @Override
        public User create() {
            return new User();
        }

    }

    //===== getter & setter =====//
    public String getUserid() {
        return userid;
    }

    private void setUserid(String userid) {
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

    private void setStatus(Integer status) {
        this.status = status;
    }

}
