//package github.com.suitelhy.dingding.core.domain.entity;
//
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntity;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
//import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
//import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
//import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Human;
//import github.com.suitelhy.dingding.core.infrastructure.util.Toolbox;
//import org.hibernate.annotations.GenericGenerator;
//import org.springframework.lang.Nullable;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import java.time.LocalDateTime;
//
///**
// * [用户 -> 个人信息]
// *
// * <p>关于 id 生成策略, 个人倾向于使用"代理键" ———— 所选策略还是应该交由数据库来实现.
// *
// * @Reference <a href="https://dzone.com/articles/persisting-natural-key-entities-with-spring-data-j">
// * ->     Persisting Natural Key Entities With Spring Data JPA</a>
// * </p>
// */
//@Entity
//@Table(name = "USER_PERSON_INFO")
//public class UserPersonInfo
//        extends AbstractEntity<String[]> {
//
//    private static final long serialVersionUID = 1L;
//
//    // [数据 ID]
//    @GeneratedValue(generator = "USER_PERSON_INFO_ID_STRATEGY")
//    @GenericGenerator(name = "USER_PERSON_INFO_ID_STRATEGY", strategy = "uuid")
//    @Id
//    @Column(length = 64)
//    private String id;
//
//    // 用户名称
//    @Column(nullable = false, unique = true, length = 50)
//    private String username;
//
//    // 数据更新时间 (由数据库管理)
//    @Column(name = "data_time")
//    @Transient
//    private LocalDateTime dataTime;
//
//    // 用户 - 昵称
//    @Column(nullable = false, unique = true, length = 50)
//    private String nickname;
//
//    // 用户 - 年龄
//    @Column
//    private Integer age;
//
//    // 用户 - 头像
//    @Column(name = "face_image", length = 255)
//    private String faceImage;
//
//    // 用户 - 简介
//    @Column(length = 200)
//    private String introduction;
//
//    // 用户 - 性别
//    @Column
//    @Convert(converter = Human.SexVo.Converter.class)
//    private Human.SexVo sex;
//
//    //===== Entity Model =====//
//
////    /**
////     * 判断关联用户
////     *
////     * @param user  {@link User}
////     *
////     * @return
////     */
////    public boolean equals(@NotNull User user) {
////        if (null != user && user.isEntityLegal() && null != user.id()
////                && this.isEntityLegal()) {
////            return this.id()[0].equals(user.id());
////        }
////        return false;
////    }
//
//    /**
//     * 等效比较
//     *
//     * @param obj {@link Object}
//     * @return 判断结果
//     * @Description 在 {@link super#equals(Object)} 的基础上添加[判断关联用户]的功能.
//     * @Solution · 约束声明原则 -> {@code javax.validation.ConstraintDeclarationException: HV000151: A method overriding another method must not redefine the parameter constraint configuration, but method UserAccountOperationInfo#equals(User) redefines the configuration of Object#equals(Object).}
//     */
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof User && ((User) obj).isEntityLegal() && null != ((User) obj).id()
//                && this.isEntityLegal()) {
//            return this.id()[0].equals(((User) obj).id());
//        }
//        return super.equals(obj);
//    }
//
//    /**
//     * 判断是否相同 <- [{@link EntityModel} 实例]
//     *
//     * @param user 实体对象, 必须合法且可未持久化    {@link EntityModel}
//     * @return 判断结果
//     * @see User
//     */
//    public boolean equals(@NotNull User user) {
//        if (null != user && user.isEntityLegal() && null != user.id()
//                && this.isEntityLegal()) {
//            return this.id()[0].equals(user.id());
//        }
//        return false;
//    }
//
//    @Override
//    public @NotNull
//    String[] id() {
//        return new String[]{
//                this.username
//                , this.nickname
//        };
//    }
//
//    /**
//     * 是否无效
//     *
//     * @return
//     * @Description 保证 User 的基本业务实现中的合法性.
//     */
//    @Override
//    public boolean isEmpty() {
//        return !Validator.USER.id(this.id)
//                || !this.isEntityLegal();
//    }
//
//    /**
//     * 是否符合基础数据合法性要求
//     *
//     * @return
//     * @Description 只保证 User 的数据合法, 不保证 User 的业务实现中的合法性.
//     */
//    @Override
//    public boolean isEntityLegal() {
//        return Validator.USER.age(this.age)                 // 用户 - 年龄
//                && Validator.USER.faceImage(this.faceImage) // 用户 - 头像
//                && Validator.USER.introduction(this.introduction) // 用户 - 简介
//                && Validator.USER.nickname(this.nickname)   // 用户 - 昵称
//                && Validator.USER.username(this.username)   // 用户名称
//                && Validator.USER.sex(this.sex)/* 用户 - 性别 */;
//    }
//
//    /**
//     * 校验 Entity - ID
//     *
//     * @param entityId <method>id()</method>
//     * @return boolean
//     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
//     */
//    @Override
//    protected boolean validateId(@NotNull String[] entityId) {
//        return Validator.USER.entity_id(entityId);
//    }
//
//    //===== Entity Validator =====//
//
//    /**
//     * 用户 - 属性校验器
//     *
//     * @Description 各个属性的基础校验(注意 : ≠ 完全校验).
//     */
//    public enum Validator
//            implements EntityValidator<UserPersonInfo, String[]> {
//        USER;
//
//        @Override
//        public boolean validateId(@NotNull UserPersonInfo entity) {
//            return null != entity.id()
//                    && entity_id(entity.id());
//        }
//
//        @Override
//        public boolean entity_id(@NotNull String[] entityId) {
//            return null != entityId
//                    && entityId.length == 2
//                    && this.username(entityId[0])
//                    && this.nickname(entityId[1]);
//        }
//
//        /**
//         * {@link AbstractEntity} 实例的 ID 校验
//         *
//         * @param entityIdString
//         * @return {@link Boolean#TYPE}
//         * @Design 对 {@link this#entity_id(String[])} 的补充拓展.
//         */
//        public boolean entity_id(@NotNull String entityIdString) {
//            return null != entityIdString
//                    && Toolbox.RegexGeneralRule.getInstance().oneDimensionalArray(entityIdString);
//        }
//
//        public boolean id(@NotNull String id) {
//            return null != id
//                    && EntityUtil.Regex.validateId(id)
//                    && id.length() < 65;
//        }
//
//        public boolean username(@NotNull String username) {
//            return User.Validator.USER.username(username)
//                    && username.length() < 51;
//        }
//
//        public boolean nickname(@NotNull String nickname) {
//            return null != nickname
//                    && !"".equals(nickname.trim())
//                    && nickname.length() < 51;
//        }
//
//        public boolean age(@Nullable Integer age) {
//            return null == age
//                    || (age > 0 && age < 1000);
//        }
//
//        public boolean introduction(String introduction) {
//            return null == introduction
//                    || introduction.length() < 201;
//        }
//
//        public boolean faceImage(String faceImage) {
//            return null == faceImage
//                    || faceImage.length() < 256;
//        }
//
//        public boolean sex(Human.SexVo sex) {
//            if (null == sex) {
//                return null != VoUtil.getInstance().getVoByValue(Human.SexVo.class, null);
//            }
//            return true;
//        }
//
//    }
//
//    //===== base constructor =====//
//
//    /**
//     * 仅用于持久化注入
//     */
//    public UserPersonInfo() {
//    }
//
//    //===== entity factory =====//
//
//    /**
//     * 创建/更新记录 -> Entity对象
//     *
//     * @param id           数据 ID
//     * @param username     用户名称
//     * @param nickname     用户 - 昵称
//     * @param age          用户 - 年龄
//     * @param faceImage    用户 - 头像
//     * @param introduction 用户 - 简介
//     * @param sex          用户 - 性别
//     * @throws IllegalArgumentException
//     * @Description 添加 (<param>id</param>为 null) / 更新 (<param>id</param>合法) 记录.
//     */
//    private UserPersonInfo(@Nullable String id
//            , @NotNull String username
//            , @NotNull String nickname
//            , @Nullable Integer age
//            , @Nullable String faceImage
//            , @Nullable String introduction
//            , @Nullable Human.SexVo sex)
//            throws IllegalArgumentException {
//        if (null == id) {
//            //--- 添加数据
//        } else {
//            //--- 更新数据
//            if (!Validator.USER.id(id)) {
//                //-- 非法输入: 数据 ID
//                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , "数据 ID"
//                        , id
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            // 数据ID
//            this.setId(id);
//        }
//        if (!Validator.USER.username(username)) {
//            //-- 非法输入: 用户名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "用户名称"
//                    , username
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.USER.nickname(nickname)) {
//            //-- 非法输入: [用户 - 昵称]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[用户 - 昵称]"
//                    , nickname
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.USER.age(age)) {
//            //-- 非法输入: [用户 - 年龄]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[用户 - 年龄]"
//                    , age
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.USER.faceImage(faceImage)) {
//            //-- 非法输入: [用户 - 头像]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[用户 - 头像]"
//                    , faceImage
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.USER.introduction(introduction)) {
//            //-- 非法输入: [用户 - 简介]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[用户 - 简介]"
//                    , introduction
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.USER.sex(sex)) {
//            //-- 非法输入: [用户 - 性别]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[用户 - 性别]"
//                    , sex
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        // 用户名称
//        this.setUsername(username);
//        // 用户 - 昵称
//        this.setNickname(nickname);
//        // 用户 - 年龄
//        this.setAge(age);
//        // 用户 - 头像
//        this.setFaceImage(faceImage);
//        // 用户 - 简介
//        this.setIntroduction(introduction);
//        // 用户 - 性别
//        this.setSex(sex);
//    }
//
//    public enum Factory
//            implements EntityFactoryModel<UserPersonInfo> {
//        USER;
//
//        /**
//         * 创建
//         *
//         * @param username     用户名称
//         * @param nickname     用户 - 昵称
//         * @param age          用户 - 年龄
//         * @param faceImage    用户 - 头像
//         * @param introduction 用户 - 简介
//         * @param sex          用户 - 性别
//         * @return {@link UserPersonInfo}
//         * @throws IllegalArgumentException
//         */
//        public UserPersonInfo create(@NotNull String username
//                , @NotNull String nickname
//                , @Nullable Integer age
//                , @Nullable String faceImage
//                , @Nullable String introduction
//                , @Nullable Human.SexVo sex)
//                throws IllegalArgumentException {
//            return new UserPersonInfo(null, username, nickname
//                    , age, faceImage, introduction
//                    , sex);
//        }
//
//        /**
//         * 创建
//         *
//         * @param userPersonInfo [用户 -> 个人信息]
//         * @return {@link UserPersonInfo}
//         * @throws IllegalArgumentException
//         */
//        public UserPersonInfo create(@NotNull UserPersonInfo userPersonInfo)
//                throws IllegalArgumentException {
//            if (null == userPersonInfo || !userPersonInfo.isEntityLegal()) {
//                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , "[用户 -> 个人信息]"
//                        , userPersonInfo
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            return new UserPersonInfo(null, userPersonInfo.getUsername(), userPersonInfo.getNickname()
//                    , userPersonInfo.getAge(), userPersonInfo.getFaceImage(), userPersonInfo.getIntroduction()
//                    , userPersonInfo.getSex());
//        }
//
//        /**
//         * 获取空对象
//         *
//         * @return 非 {@code null}.
//         */
//        @Override
//        public UserPersonInfo createDefault() {
//            return new UserPersonInfo();
//        }
//
//    }
//
//    //===== getter & setter =====//
//
//    @Nullable
//    public String getId() {
//        return id;
//    }
//
//    private boolean setId(@NotNull String id) {
//        if (Validator.USER.id(id)) {
//            this.id = id;
//            return true;
//        }
//        return false;
//    }
//
//    @NotNull
//    public String getUsername() {
//        return username;
//    }
//
//    private boolean setUsername(@NotNull String username) {
//        if (Validator.USER.username(username)) {
//            this.username = username;
//            return true;
//        }
//        return false;
//    }
//
//    @NotNull
//    public String getNickname() {
//        return nickname;
//    }
//
//    public boolean setNickname(@NotNull String nickname) {
//        if (Validator.USER.nickname(nickname)) {
//            this.nickname = nickname;
//            return true;
//        }
//        return false;
//    }
//
//    public Integer getAge() {
//        return age;
//    }
//
//    public boolean setAge(Integer age) {
//        if (Validator.USER.age(age)) {
//            this.age = age;
//            return true;
//        }
//        return false;
//    }
//
//    public String getFaceImage() {
//        return faceImage;
//    }
//
//    public boolean setFaceImage(String faceImage) {
//        if (Validator.USER.faceImage(faceImage)) {
//            this.faceImage = faceImage;
//            return true;
//        }
//        return false;
//    }
//
//    public String getIntroduction() {
//        return introduction;
//    }
//
//    public boolean setIntroduction(String introduction) {
//        if (Validator.USER.introduction(introduction)) {
//            this.introduction = introduction;
//            return true;
//        }
//        return false;
//    }
//
//    public Human.SexVo getSex() {
//        return sex;
//    }
//
//    public boolean setSex(Human.SexVo sexVo) {
//        if (Validator.USER.sex(sexVo)) {
//            this.sex = sexVo;
//            return true;
//        }
//        return false;
//    }
//}
