package github.com.suitelhy.dingding.domain.entity;

import github.com.suitelhy.dingding.infrastructure.util.CalendarController;
import github.com.suitelhy.dingding.infrastructure.web.util.NetUtil;
import github.com.suitelhy.dingding.infrastructure.domain.vo.AccountVo;
import github.com.suitelhy.dingding.infrastructure.domain.vo.HumanVo;
import github.com.suitelhy.dingding.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.infrastructure.domain.util.EntityUtil;
import github.com.suitelhy.dingding.infrastructure.domain.util.VoUtil;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
@Entity
@Table
public class User
        extends AbstractEntityModel<String> {

    private static final long serialVersionUID = 1L;

    // 用户ID
    @GeneratedValue(generator = "USER_ID_STRATEGY")
    @GenericGenerator(name = "USER_ID_STRATEGY", strategy = "uuid")
    @Id
    private /*final */String userid;

    // 用户名称
    @Column(nullable = false, unique = true)
    private String username;

    // 用户 - 昵称
    @Column(nullable = false, unique = true)
    private String nickname;

    // 用户 - 年龄
    @Column
    private Integer age;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time")
    @Transient
    private LocalDateTime dataTime;

    // 用户 - 头像
    @Column(name = "face_image")
    private String faceImage;

    // 注册时间
    @Column(nullable = false)
    private String firsttime;

    // 用户 - 简介
    @Column
    private String introduction;

    // 最后登陆IP
    @Column(nullable = false)
    private String ip;

    // 最后登录时间
    @Column(nullable = false)
    private String lasttime;

    // 用户密码
    @Column(nullable = false)
    private String password;

    // 用户 - 性别
    @Column
    @Convert(converter = HumanVo.Sex.Converter.class)
    private HumanVo.Sex sex;

    // 账号状态
    @Column(nullable = false)
    @Convert(converter = AccountVo.Status.Converter.class)
    private AccountVo.Status status;

    //===== Entity Model =====//
    @Override
    public @NotNull String id() {
        return this.getUserid();
    }

    /*@Override
    public boolean equals(Object obj) {
        return EntityModel.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return EntityModel.hashCode(this);
    }*/

//    /**
//     * 是否无效: id() 为空 || 不符合业务要求 || 未持久化
//     *
//     * @Description <tt>id() -> nonNull || !isLegal() || isPersistence() -> not false</tt>
//     * @return
//     */
//    @Override
//    public boolean isEmpty() {
//        return (null == id() || "".equals(id().trim())) // Entity - ID
//                || !isEntityLegal()
//                || (null != isEntityPersistence() && !isEntityPersistence());
//    }

    /**
     * 是否符合业务要求
     *
     * @Description 需要实现类实现该抽象方法
     * @return
     */
    @Override
    public boolean isEntityLegal() {
        return Validator.USER.firsttime(this.firsttime) // 注册时间
                && Validator.USER.ip(this.ip) // 最后登陆IP
                && Validator.USER.lasttime(this.lasttime) // 最后登录时间
                && Validator.USER.nickname(this.nickname) // 用户 - 昵称
                && Validator.USER.password(this.password) // 用户密码
                && Validator.USER.username(this.username) // 用户名称
                && (Validator.USER.status(this.status)
                        && AccountVo.Status.NORMAL.equals(this.status))/* 账号状态 */;
    }

//    @Override
//    public String toString() {
//        return EntityModel.toString(this);
//    }

    /**
     * 校验 Entity - ID
     *
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     * @param id <method>id()</method>
     * @return
     */
    @Override
    protected boolean validateId(@NotNull String id) {
        return Validator.USER.id(id);
    }

    //===== Entity Validator =====//
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

        public boolean username(@NotNull String username) {
            return EntityUtil.Regex.validateUsername(username);
        }

        public boolean nickname(@NotNull String nickname) {
            return null != nickname && !"".equals(nickname.trim());
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

        public boolean password(@NotNull String password) {
            return EntityUtil.Regex.validateUserPassword(password);
        }

        public boolean introduction(String introduction) {
            //--- 暂无业务设计约束
            return true;
        }

        public boolean faceImage(String faceImage) {
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
     * @param password      用户密码
     * @param introduction       用户 - 简介
     * @param faceImage   用户 - 头像
     * @param username      用户名称
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
            , @Nullable String introduction
            , @Nullable String faceImage
            , @NotNull String username
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
        if (!Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 用户名称");
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
            //-- 非法输入: 用户密码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 用户密码");
        }
        // 用户ID
        this.setUserid(id);
        // 用户名称
        this.setUsername(username);
        // 用户 - 昵称
        this.setNickname(nickname);
        // 用户 - 年龄
        this.setAge(age);
        // 注册时间
        this.setFirsttime(new CalendarController().toString());
        // 最后登陆IP
        this.setIp(ip);
        // 最后登录时间
        this.setLasttime(lasttime);
        // 用户密码
        this.setPassword(password);
        // 用户 - 简介
        this.setIntroduction(introduction);
        // 用户 - 头像
        this.setFaceImage(faceImage);
        // 用户 - 性别
        this.setSex(sex);
        // 账号状态
        this.setStatus(AccountVo.Status.NORMAL);
    }

    public enum Factory implements EntityFactory<User> {
        USER;

        /**
         * 创建用户
         *
         * @param age           用户 - 年龄
         * @param firsttime     注册时间
         * @param ip            最后登陆IP
         * @param lasttime      最后登录时间
         * @param nickname      用户 - 昵称
         * @param password      用户密码
         * @param introduction       用户 - 简介
         * @param faceImage   用户 - 头像
         * @param username      用户名称
         * @param sex           用户 - 性别
         * @throws IllegalArgumentException
         */
        public User create(@Nullable Integer age
                , @NotNull String firsttime
                , @NotNull String ip
                , @NotNull String lasttime
                , @NotNull String nickname
                , @NotNull String password
                , @Nullable String introduction
                , @Nullable String faceImage
                , @NotNull String username
                , @Nullable HumanVo.Sex sex) {
            return new User(null, age, firsttime
                    , ip, lasttime, nickname
                    , password, introduction, faceImage
                    , username, sex);
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
         * @param introduction       用户 - 简介
         * @param faceImage   用户 - 头像
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
                , @Nullable String introduction
                , @Nullable String faceImage
                , @NotNull String username
                , @Nullable HumanVo.Sex sex) {
            if (!Validator.USER.id(id)) {
                throw new IllegalArgumentException("非法输入: 用户ID");
            }
            return new User(id, age, firsttime
                    , ip, lasttime, nickname
                    , password, introduction, faceImage
                    , username, sex);
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
    @NotNull
    public String getUserid() {
        return userid;
    }

    private boolean setUserid(String userid) {
        if (Validator.USER.userid(userid)) {
            this.userid = userid;
            return true;
        }
        return false;
    }

    public Integer getAge() {
        return age;
    }

    public boolean setAge(Integer age) {
        if (Validator.USER.age(age)) {
            this.age = age;
            return true;
        }
        return false;
    }

    public String getFirsttime() {
        return firsttime;
    }

    public boolean setFirsttime(String firsttime) {
        if (Validator.USER.firsttime(firsttime)) {
            this.firsttime = firsttime;
            return true;
        }
        return false;
    }

    public String getIp() {
        return ip;
    }

    public boolean setIp(String ip) {
        if (Validator.USER.ip(ip)) {
            this.ip = ip;
            return true;
        }
        return false;
    }

    public String getLasttime() {
        return lasttime;
    }

    public boolean setLasttime(String lasttime) {
        if (Validator.USER.lasttime(lasttime)) {
            this.lasttime = lasttime;
            return true;
        }
        return false;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean setNickname(String nickname) {
        if (Validator.USER.nickname(nickname)) {
            this.nickname = nickname;
            return true;
        }
        return false;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public boolean setPassword(@NotNull String password) {
        if (Validator.USER.password(password)) {
            this.password = password;
            return true;
        }
        return false;
    }

    /**
     * 判断密码是否相同
     *
     * @param password
     * @return {true: <tt>密码相同</tt>, false: <tt>密码不相同</tt>, null: <tt>Entity无效</tt>}
     */
    public Boolean equalsPassword(@NotNull String password) {
        if (Validator.USER.password(password)) {
            if (isEmpty()) {
                return null;
            }
            return this.password.equals(password);
        }
        return false;
    }

    public String getIntroduction() {
        return introduction;
    }

    public boolean setIntroduction(String introduction) {
        if (Validator.USER.introduction(introduction)) {
            this.introduction = introduction;
            return true;
        }
        return false;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public boolean setFaceImage(String faceImage) {
        if (Validator.USER.faceImage(faceImage)) {
            this.faceImage = faceImage;
            return true;
        }
        return false;
    }

    public HumanVo.Sex getSex() {
        return sex;
    }

    public boolean setSex(HumanVo.Sex sex) {
        if (Validator.USER.sex(sex)) {
            this.sex = sex;
            return true;
        }
        return false;
    }

    public AccountVo.Status getStatus() {
        return status;
    }

    private boolean setStatus(AccountVo.Status status) {
        if (Validator.USER.status(status)) {
            this.status = status;
            return true;
        }
        return false;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    private boolean setUsername(String username) {
        if (Validator.USER.username(username)) {
            this.username = username;
            return true;
        }
        return false;
    }

}
