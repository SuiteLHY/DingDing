package github.com.suitelhy.webchat.domain.entity;

import github.com.suitelhy.webchat.domain.vo.AccountVo;
import github.com.suitelhy.webchat.domain.vo.HandleTypeVo;
import github.com.suitelhy.webchat.domain.vo.HumanVo;
import github.com.suitelhy.webchat.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.webchat.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.webchat.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.webchat.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.webchat.infrastructure.domain.util.EntityUtil;
import github.com.suitelhy.webchat.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.webchat.infrastructure.util.CalendarController;
import github.com.suitelhy.webchat.infrastructure.web.util.NetUtil;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 用户信息
 *
 */
/**
 * 关于数据脱敏的自定义注解实现, 可参考: <a href="https://blog.csdn.net/liufei198613/article/details/79009015">
 *->     注解实现json序列化的时候自动进行数据脱敏_liufei198613的博客-CSDN博客</a>
 */
/**
 * 关于 id 生成策略, 个人倾向于使用"代理键", 所选策略还是应该交由数据库来实现.
 * @Reference <a href="https://dzone.com/articles/persisting-natural-key-entities-with-spring-data-j">
 *->     Persisting Natural Key Entities With Spring Data JPA</a>
 */
/*@SuiteTable("user")*/
@Entity
@Table
public class User /*implements EntityModel<String>*/
        extends AbstractEntityModel<String> {

    private static final long serialVersionUID = 1L;

    // 用户ID
    @GeneratedValue(/*strategy = GenerationType.TABLE*/generator = "USER_ID_STRATEGY")
    @GenericGenerator(name = "USER_ID_STRATEGY", strategy = "uuid")
    @Id
    private /*final */String userid;

    // 用户 - 年龄
    @Column
    private Integer age;

    // 注册时间
    @Column(nullable = false)
    private String firsttime;

    // 最后登陆IP
    @Column(nullable = false)
    private String ip;

    // 最后登录时间
    @Column(nullable = false)
    private String lasttime;

    // 用户 - 昵称
    /*@SuiteColumn(nullable = false)*/
    @Column(nullable = false, unique = true)
    private String nickname;

    // 用户 - 密码
    @Column(nullable = false)
    private String password;

    // 用户 - 简介
    @Column
    private String profile;

    // 用户 - 头像
    @Column
    private String profilehead;

    // 用户 - 性别 -> {0:女, 1:男}
    @Column
    @Convert(converter = HumanVo.Sex.Converter.class)
    private HumanVo.Sex sex;

    // 账号状态 -> {0:已注销, 1:正常, 2:异常&禁用}
    @Column(nullable = false)
    @Convert(converter = AccountVo.Status.Converter.class)
    private AccountVo.Status status;

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

    /**
     * 是否无效: id() 为空 || 不符合业务要求 || 未持久化
     *
     * @Description <tt>id() -> nonNull || !isLegal() || isPersistence() -> not false</tt>
     * @return
     */
    @Override
    public boolean isEmpty() {
        return (null == id() || "".equals(id().trim())) // 用户ID
                || !isLegal()
                || (null != isPersistence() && !isPersistence());
    }

    /**
     * 是否符合业务要求
     *
     * @return
     * @Description 需要实现类实现该抽象方法
     */
    @Override
    public boolean isLegal() {
        return Validator.USER.firsttime(this.getFirsttime()) // 注册时间
                && Validator.USER.ip(this.getIp()) // 最后登陆IP
                && Validator.USER.lasttime(this.getLasttime()) // 最后登录时间
                && Validator.USER.nickname(this.getNickname()) // 用户 - 昵称
                && Validator.USER.password(this.getPassword()) // 用户 - 密码
                && (Validator.USER.status(this.getStatus())
                        && AccountVo.Status.NORMAL.equals(this.getStatus()))/* 账号状态 */;
    }

    @Override
    public String toString() {
        return EntityModel.toString(this);
    }

    //===== entity validator =====//
    /**
     * 用户 - 属性校验器
     * @Description 各个属性的基础校验(注意: ≠完全校验).
     */
    public enum Validator implements EntityValidator<User, String> {
        USER;

        @Override
        public boolean validateId(@NotNull User entity) {
            return null != entity.id()
                    && userid(entity.id());
        }

        @Override
        public boolean id(@NotNull String id) {
            return userid(id);
        }

        public boolean userid(@NotNull String userid) {
            return EntityUtil.Regex.validateId(userid);
        }

        public boolean age(@Nullable Integer age) {
            return null == age || age > 0;
        }

        public boolean firsttime(@NotNull String firsttime) {
            return null != firsttime && CalendarController.isParse(firsttime);
        }

        public boolean ip(@NotNull String ip) {
            return NetUtil.validateIpAddress(ip);
        }

        public boolean lasttime(@NotNull String lasttime) {
            return null != lasttime && CalendarController.isParse(lasttime);
        }

        public boolean nickname(@NotNull String nickname) {
            return null != nickname && !"".equals(nickname.trim());
        }

        public boolean password(@NotNull String password) {
            return EntityUtil.Regex.validateUserPassword(password);
        }

        public boolean profile(String profile) {
            //--- 暂无业务设计约束
            return true;
        }

        public boolean profilehead(String profilehead) {
            //--- 暂无业务设计约束
            return true;
        }

        public boolean sex(HumanVo.Sex sex) {
            if (null == sex) {
                return null != VoUtil.getVoByValue(HumanVo.Sex.class, null);
            }
            return true;
        }

        public boolean status(@NotNull AccountVo.Status status) {
            if (null == status) {
                return null != VoUtil.getVoByValue(AccountVo.Status.class, null);
            }
            return true;
        }

    }

    //===== entity mapper =====//
//    //----- 根据是否需要个性化操作, 选择如下代码块拓展 Entity 映射操作
//    //-> 或者使用 Entity 映射模板提供的静态方法.
//    public static final UserMapper MAPPER = UserMapper.getInstance();
//
//    public static final class UserMapper extends JPAMapperModel<User> {
//
//        //=== 工厂模式实现单例 ===//
//        private UserMapper() {
//            super(User.class);
//        }
//
//        private static final class JPAMapperFactory {
//            public static final UserMapper INSTANCE = new UserMapper();
//        }
//
//        public static final UserMapper getInstance() {
//            return JPAMapperFactory.INSTANCE;
//        }
//        //======//
//
//        // 用户 - 用户业务信息字段
//        private final String[] businessFieldNames = {getColumn("userid")
//                , getColumn("age")
//                , getColumn("firsttime")
//                , getColumn("lasttime")
//                , getColumn("nickname")
//                , getColumn("password")
//                , getColumn("profile")
//                , getColumn("profilehead")
//                , getColumn("status")};
//
//        public String[] getBusinessFieldNames() {
//            return businessFieldNames;
//        }
//
//    }

    //===== base constructor =====//
    /**
     * 仅用于持久化注入
     */
    public User() {}

    //===== entity factory =====//
//    /**
//     * 创建用户 - Entity对象
//     *
//     * @Description 仅用于添加用户.
//     */
//    private User(@Nullable Integer age
//            , @NotNull String firsttime
//            , @NotNull String ip
//            , @NotNull String lasttime
//            , @NotNull String nickname
//            , @NotNull String password
//            , @Nullable String profile
//            , @Nullable String profilehead
//            , @Nullable HumanCharacteristics.Sex sex) {
//        /*this.setUserid(DBPolicy.uuid());*/
//        // 用户ID
//        String id = DBPolicy.MYSQL.uuid();
//        new User(id, age, firsttime
//                , ip, lasttime, nickname
//                , password, profile, profilehead
//                , sex);
//    }

    /**
     * 创建/更新用户 -> Entity对象
     *
     * @Description 添加(<param>id</param>为 null) / 更新(<param>id</param>合法)用户.
     * @param id            用户ID
     * @param age           用户 - 年龄
     * @param firsttime     注册时间
     * @param ip            最后登陆IP
     * @param lasttime      最后登录时间
     * @param nickname      用户 - 昵称
     * @param password      用户 - 密码
     * @param profile       用户 - 简介
     * @param profilehead   用户 - 头像
     * @param sex           用户 - 性别
     * @throws IllegalArgumentException
     */
    private User(@NotNull String id
            , @Nullable Integer age
            , @NotNull String firsttime
            , @NotNull String ip
            , @NotNull String lasttime
            , @NotNull String nickname
            , @NotNull String password
            , @Nullable String profile
            , @Nullable String profilehead
            , @Nullable HumanVo.Sex sex) {
        if (null == id) {
            //--- <param>id</param>为 null 时, 对应添加用户功能.
        } else {
            //--- 对应更新用户功能.
            if (!Validator.USER.id(id)) {
                //-- 非法输入: 用户ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        + " -> 非法输入: 用户ID");
            }
        }
        if (!Validator.USER.age(age)) {
            //-- 非法输入: 用户 - 年龄
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 用户 - 年龄");
        }
        if (!Validator.USER.firsttime(firsttime)) {
            //-- 非法输入: 注册时间
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 注册时间");
        }
        if (!Validator.USER.ip(ip)) {
            //-- 非法输入: 最后登陆IP
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 最后登陆IP");
        }
        if (!Validator.USER.lasttime(lasttime)) {
            //-- 非法输入: 最后登录时间
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 最后登录时间");
        }
        if (!Validator.USER.nickname(nickname)) {
            //-- 非法输入: 用户 - 昵称
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 用户 - 昵称");
        }
        if (!Validator.USER.password(password)) {
            //-- 非法输入: 用户 - 密码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 用户 - 密码");
        }
        // 用户ID
        this.setUserid(id);
        // 用户 - 年龄
        this.setAge(age);
        // 注册时间
        this.setFirsttime(new CalendarController().toString());
        // 最后登陆IP
        this.setIp(ip);
        // 最后登录时间
        this.setLasttime(lasttime);
        // 用户 - 昵称
        this.setNickname(nickname);
        // 用户 - 密码
        this.setPassword(password);
        // 用户 - 简介
        this.setProfile(profile);
        // 用户 - 头像
        this.setProfilehead(profilehead);
        // 用户 - 性别
        this.setSex(sex);
        // 账号状态
        this.setStatus(AccountVo.Status.NORMAL);
    }

    public enum Factory implements EntityFactory<User> {
        USER;

        /**
         * 获取 Entity 实例
         *
         * @return <code>null</code>
         */
        @Override
        public User create() {
            return null;
        }

        /**
         * 创建用户
         *
         * @param age           用户 - 年龄
         * @param firsttime     注册时间
         * @param ip            最后登陆IP
         * @param lasttime      最后登录时间
         * @param nickname      用户 - 昵称
         * @param password      用户 - 密码
         * @param profile       用户 - 简介
         * @param profilehead   用户 - 头像
         * @param sex           用户 - 性别
         * @throws IllegalArgumentException
         */
        public User create(@Nullable Integer age
                , @NotNull String firsttime
                , @NotNull String ip
                , @NotNull String lasttime
                , @NotNull String nickname
                , @NotNull String password
                , @Nullable String profile
                , @Nullable String profilehead
                , @Nullable HumanVo.Sex sex) {
            return new User(null, age, firsttime
                    , ip, lasttime, nickname
                    , password, profile, profilehead
                    , sex);
        }

        /**
         * 更新用户
         *
         * @param id            用户ID
         * @param age           用户 - 年龄
         * @param firsttime     注册时间
         * @param ip            最后登陆IP
         * @param lasttime      最后登录时间
         * @param nickname      用户 - 昵称
         * @param password      用户 - 密码
         * @param profile       用户 - 简介
         * @param profilehead   用户 - 头像
         * @param sex           用户 - 性别
         * @throws IllegalArgumentException 此时 <param>id</param> 非法
         * @return 可为 null, 此时输入参数非法
         */
        public User update(@NotNull String id
                , @Nullable Integer age
                , @NotNull String firsttime
                , @NotNull String ip
                , @NotNull String lasttime
                , @NotNull String nickname
                , @NotNull String password
                , @Nullable String profile
                , @Nullable String profilehead
                , @Nullable HumanVo.Sex sex) {
            if (null == id) {
                throw new IllegalArgumentException("非法输入: 用户ID");
            }
            return new User(id, age, firsttime
                    , ip, lasttime, nickname
                    , password, profile, profilehead
                    , sex);
        }

        /**
         * 销毁 Entity 实例
         * @param user
         * @return {<code>true</code> : <b>销毁成功</b>
         *->      , <code>false</code> : <b>销毁失败; 此时 <param>user</param></b> 无效或无法销毁}
         */
        public boolean delete(@NotNull User user) {
            if (null != user && !user.isEmpty()) {
                user.setStatus(AccountVo.Status.DESTRUCTION);
                return true;
            }
            return false;
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

    public HumanVo.Sex getSex() {
        return sex;
    }

    public void setSex(HumanVo.Sex sex) {
        this.sex = sex;
    }

    public AccountVo.Status getStatus() {
        return status;
    }

    private void setStatus(AccountVo.Status status) {
        this.status = status;
    }

}
