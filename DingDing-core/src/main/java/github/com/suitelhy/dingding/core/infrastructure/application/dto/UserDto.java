package github.com.suitelhy.dingding.core.infrastructure.application.dto;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.core.infrastructure.application.model.DtoFactoryModel;
import github.com.suitelhy.dingding.core.infrastructure.application.model.DtoModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.HashMap;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Human;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

public class UserDto
        extends BasicUserDto {

    private static final long serialVersionUID = 1L;

    // 用户 - 年龄
    protected String age;

    // 用户注册时间
    protected String registrationTime;

    // 用户 - 昵称
    protected String nickname;

    // 用户密码
    protected String password;

    // 用户 - 简介
    protected String introduction;

    // 用户 - 头像
    protected String faceImage;

    // 用户 - 性别
    protected String sex;

    // 账号状态
    protected String status;

    // 用户名称
    protected String username;

    //===== DtoModel =====//

    @NotNull
    private final transient User dtoId;

    /**
     * User - 拓展属性
     *
     * @Design
     *-> {
     *->    userAccountOperationInfo: [用户 -> 账户操作基础记录]
     *->    , userPersonInfo: [用户 -> 个人信息]
     *-> }
     */
    @NotNull
    private final transient Map<String, Object> dtoIdExtension;

    private UserDto() {
        super();

        //=== dtoId 相关 ===//
        this.dtoId = User.Factory.USER.createDefault();
        this.dtoIdExtension = new HashMap<>(2);
        this.dtoIdExtension.put("userAccountOperationInfo", UserAccountOperationInfo.Factory.USER.createDefault());
        this.dtoIdExtension.put("userPersonInfo", UserPersonInfo.Factory.USER.createDefault());
        //======//
    }

    /**
     * (Constructor)
     *
     * @param dtoId                         {@link User}
     * @param dtoIdAccountOperationInfo     {@link UserAccountOperationInfo}
     * @param dtoIdPersonInfo               {@link UserPersonInfo}
     *
     * @throws IllegalArgumentException
     */
    protected UserDto(@NotNull User dtoId, @NotNull UserAccountOperationInfo dtoIdAccountOperationInfo, @NotNull UserPersonInfo dtoIdPersonInfo)
            throws IllegalArgumentException
    {
        super(dtoId);

        if (!dtoId.isEntityLegal()) {
            throw new IllegalArgumentException("非法参数: <param>dtoId</param>");
        }
        if (null == dtoIdAccountOperationInfo
                || !dtoIdAccountOperationInfo.isEntityLegal()) {
            throw new IllegalArgumentException("非法参数: <param>dtoIdAccountOperationInfo</param>");
        }
        if (null == dtoIdPersonInfo
                || !dtoIdPersonInfo.isEntityLegal()) {
            throw new IllegalArgumentException("非法参数: <param>dtoIdPersonInfo</param>");
        }

        //=== dtoId 相关 ===//
        this.dtoId = dtoId;
        this.dtoIdExtension = new HashMap<>(2);
        this.dtoIdExtension.put("userAccountOperationInfo", dtoIdAccountOperationInfo);
        this.dtoIdExtension.put("userPersonInfo", dtoIdPersonInfo);
        //======//

        this.age = String.format("%d岁"
                , ((UserPersonInfo) Objects.requireNonNull(dtoIdExtension.get("userPersonInfo"))).getAge());
        this.registrationTime = ((UserAccountOperationInfo) Objects.requireNonNull(this.dtoIdExtension.get("userAccountOperationInfo")))
                .getRegistrationTime();
        this.nickname = ((UserPersonInfo) Objects.requireNonNull(dtoIdExtension.get("userPersonInfo")))
                .getNickname();
        this.password = /*"******************"*/this.dtoId.getPassword();
        this.introduction = ((UserPersonInfo) Objects.requireNonNull(dtoIdExtension.get("userPersonInfo")))
                .getIntroduction();
        this.faceImage = ((UserPersonInfo) Objects.requireNonNull(dtoIdExtension.get("userPersonInfo")))
                .getFaceImage();
        this.sex = ((UserPersonInfo) Objects.requireNonNull(dtoIdExtension.get("userPersonInfo")))
                .getSex()
                .displayName();
        this.status = this.dtoId.getStatus()
                .displayName();
        this.username = this.dtoId.getUsername();
    }

    /**
     * (Constructor)
     *
     * @param userDto   {@link UserDto}
     *
     * @throws IllegalArgumentException
     */
    protected UserDto(@NotNull UserDto userDto)
            throws IllegalArgumentException
    {
        super(userDto);

        if (!userDto.isDtoLegal() || !userDto.isEntityLegal()) {
            //-- 非法输入: <param>userDto</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "userDto"
                    , userDto
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == userDto.dtoIdExtension.get("userAccountOperationInfo")
                || !((UserAccountOperationInfo) userDto.dtoIdExtension.get("userAccountOperationInfo")).isEntityLegal()) {
            //-- 非法输入: <param>userDto</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "userDto"
                    , userDto
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == userDto.dtoIdExtension.get("userPersonInfo")
                || !((UserPersonInfo) userDto.dtoIdExtension.get("userPersonInfo")).isEntityLegal()) {
            //-- 非法输入: <param>userDto</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "userDto"
                    , userDto
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== dtoId 相关 ===//
        this.dtoId = userDto.dtoId;
        this.dtoIdExtension = new HashMap<>(2);
        this.dtoIdExtension.put("userAccountOperationInfo", userDto.dtoIdExtension.get("userAccountOperationInfo"));
        this.dtoIdExtension.put("userPersonInfo", userDto.dtoIdExtension.get("userPersonInfo"));
        //======//

        this.age = String.format("%d岁"
                , ((UserPersonInfo) Objects.requireNonNull(dtoIdExtension.get("userPersonInfo"))).getAge());
        this.registrationTime = ((UserAccountOperationInfo) Objects.requireNonNull(this.dtoIdExtension.get("userAccountOperationInfo")))
                .getRegistrationTime();
        this.nickname = ((UserPersonInfo) Objects.requireNonNull(dtoIdExtension.get("userPersonInfo")))
                .getNickname();
        this.password = /*"******************"*/this.dtoId.getPassword();
        this.introduction = ((UserPersonInfo) Objects.requireNonNull(dtoIdExtension.get("userPersonInfo")))
                .getIntroduction();
        this.faceImage = ((UserPersonInfo) Objects.requireNonNull(dtoIdExtension.get("userPersonInfo")))
                .getFaceImage();
        this.sex = ((UserPersonInfo) Objects.requireNonNull(dtoIdExtension.get("userPersonInfo")))
                .getSex()
                .displayName();
        this.status = this.dtoId.getStatus()
                .displayName();
        this.username = this.dtoId.getUsername();
    }

    /**
     * 唯一标识 <- DTO 对象
     *
     * @param username          用户名
     * @param passwordPlaintext 用户密码（明文）
     *
     * @return The unique identify of the DTO, or null.
     */
    @Nullable
    public User dtoId(@NotNull String username, @NotNull String passwordPlaintext) {
        if (User.Validator.USER.username(username)
                && User.Validator.USER.passwordPlaintext(passwordPlaintext)) {
            if (this.dtoId.getUsername().equals(username)
                    && Boolean.TRUE.equals(this.dtoId.equalsPassword(passwordPlaintext))) {
                //设计: 用户 Entity 对象的获取需要进行严格地控制.
                return this.dtoId;
            }
        }
        /*System.err.println(String.format("【%s】【%s】-> false", username, passwordPlaintext));*/
        return null;
    }

    /**
     * 唯一标识 <- DTO 对象
     *
     * @param dtoId User
     *
     * @return The unique identify of the DTO, or null.
     */
    @Nullable
    public User dtoId(@NotNull User dtoId) {
        if (null == dtoId || dtoId.isEmpty()
                || !this.dtoId.equals(dtoId)) {
            return null;
        }

        return this.dtoId;
    }

//    /**
//     * 获取[User - 拓展属性]
//     *
//     * @param dtoId User
//     *
//     * @return [User - 拓展属性]
//     */
//    @Nullable
//    public Map<String, Object> dtoIdExtension(@NotNull User dtoId) {
//        if (null == dtoId || !this.dtoId.equals(dtoId)) {
//            return null;
//        }
//
//        return this.dtoIdExtension;
//    }

    /**
     * 获取[用户 -> 账户操作基础记录]
     *
     * @param dtoId User
     *
     * @return [用户 -> 账户操作基础记录]
     */
    @Nullable
    public UserAccountOperationInfo dtoId_UserAccountOperationInfo(@NotNull User dtoId) {
        if (null == dtoId || !this.dtoId.equals(dtoId)) {
            return null;
        }

        return (UserAccountOperationInfo) this.dtoIdExtension.get("userAccountOperationInfo");
    }

    /**
     * 获取[用户 -> 个人信息]
     *
     * @param dtoId User
     *
     * @return [用户 -> 个人信息]
     */
    @Nullable
    public UserPersonInfo dtoId_UserPersonInfo(@NotNull User dtoId) {
        if (null == dtoId || !this.dtoId.equals(dtoId)) {
            return null;
        }

        return (UserPersonInfo) this.dtoIdExtension.get("userPersonInfo");
    }

    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the <tt>dtoId()</tt>.
     */
    @Override
    public String id() {
        return this.dtoId.id();
    }

    /**
     * 是否符合业务要求 <- DTO 对象
     *
     * @return
     */
    @Override
    public boolean isDtoLegal() {
        return isEntityLegal();
    }

    /**
     * 是否符合业务要求
     *
     * @Description 需要实现类实现该接口
     *
     * @return
     */
    @Override
    public boolean isEntityLegal() {
        return dtoId.isEntityLegal();
    }

    /**
     * 是否无效 <- DTO 对象
     *
     * @Description 使用默认实现.
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return DtoModel.isEmpty(this);
    }

    @NotNull
    @Override
    public String toString() {
        return this.toJSONString();
    }

    //===== Factory =====//

    public enum Factory
            implements DtoFactoryModel<UserDto, String> {
        USER_DTO;

        /**
         * 创建用户 DTO
         *
         * @param age               用户 - 年龄
         * @param registrationTime  注册时间
         * @param ip                最后登陆 IP
         * @param lastLoginTime     最后登录时间
         * @param nickname          用户 - 昵称
         * @param password          用户 - 密码
         * @param introduction      用户 - 简介
         * @param faceImage         用户 - 头像
         * @param sex               用户 - 性别
         *
         * @return 可为 null, 此时输入参数非法
         *
         * @throws IllegalArgumentException 此时 <param>id</param> 非法
         */
        public UserDto create(@Nullable Integer age
                , @NotNull String registrationTime
                , @NotNull String ip
                , @NotNull String lastLoginTime
                , @NotNull String nickname
                , @NotNull String password
                , @Nullable String introduction
                , @Nullable String faceImage
                , @NotNull String username
                , @Nullable String sex)
                throws IllegalAccessException
        {
            final User user = User.Factory.USER.create(username, password);
            final UserAccountOperationInfo userAccountOperationInfo = UserAccountOperationInfo.Factory.USER.create(username
                    , ip
                    , lastLoginTime
                    , registrationTime);
            final UserPersonInfo userPersonInfo = UserPersonInfo.Factory.USER.create(username
                    , nickname
                    , age
                    , faceImage
                    , introduction
                    , VoUtil.getInstance().getVoByName(Human.SexVo.class, sex));
            if (!user.isEntityLegal()) {
                throw new IllegalAccessException("非法参数: [用户 - 基础信息]");
            }
            if (!userAccountOperationInfo.isEntityLegal()
                    || !userAccountOperationInfo.equals(user)) {
                throw new IllegalAccessException("非法参数: [用户 -> 账户操作基础记录]");
            }
            if (!userPersonInfo.isEntityLegal()
                    || !userPersonInfo.equals(user)) {
                throw new IllegalAccessException("非法参数: [用户 -> 个人信息]");
            }

            return new UserDto(user, userAccountOperationInfo, userPersonInfo);
        }

        /**
         * 创建用户 DTO
         *
         * @param user                  [用户 - 基础信息]           {@link User}
         * @param userOperationInfo     [用户 -> 账户操作基础记录]    {@link UserAccountOperationInfo}
         * @param userPersonInfo        [用户 -> 个人信息]          {@link UserPersonInfo}
         *
         * @return {@link UserDto}
         */
        @NotNull
        public UserDto create(@NotNull User user, @NotNull UserAccountOperationInfo userOperationInfo, @NotNull UserPersonInfo userPersonInfo)
                throws IllegalAccessException
        {
            if (null == user || !user.isEntityLegal()) {
                throw new IllegalAccessException("非法参数: 用户基础信息");
            }
            if (null == userOperationInfo
                    || !userOperationInfo.isEntityLegal()
                    || !userOperationInfo.equals(user))
            {
                throw new IllegalAccessException("非法参数: [用户 -> 账户操作基础记录]");
            }
            if (null == userPersonInfo
                    || !userPersonInfo.isEntityLegal()
                    || !userPersonInfo.equals(user))
            {
                throw new IllegalAccessException("非法参数: [用户 -> 个人信息]");
            }

            return new UserDto(user, userOperationInfo, userPersonInfo);
        }

//        /**
//         * 创建用户 DTO
//         *
//         * @param age         用户 - 年龄
//         * @param firsttime   注册时间
//         * @param nickname    用户 - 昵称
//         * @param password    用户密码
//         * @param profile     用户 - 简介
//         * @param profilehead 用户 - 头像
//         * @param username    用户名称
//         * @param sex         用户 - 性别
//         * @throws IllegalArgumentException
//         */
//        @NotNull
//        public UserDto create(@Nullable Integer age
//                , @NotNull String firsttime
//                , @NotNull String ip
//                , @NotNull String lasttime
//                , @NotNull String nickname
//                , @NotNull String password
//                , @Nullable String profile
//                , @Nullable String profilehead
//                , @NotNull String username
//                , @Nullable String sex) {
//            User newUser = User.Factory.USER.create(age, firsttime, ip
//                    , lasttime, nickname, password
//                    , profile, profilehead, username
//                    , VoUtil.getVoByName(Human.SexVo.class, sex));
//            return new UserDto(newUser);
//        }

        /**
         * 更新用户 DTO
         *
         * @param user                      用户基础信息              {@link User}
         * @param userAccountOperationInfo  [用户 -> 账户操作基础记录]  {@link UserAccountOperationInfo}
         * @param userPersonInfo            [用户 -> 个人信息]        {@link UserPersonInfo}
         *
         * @return {@link UserDto}
         *
         * @throws IllegalAccessException
         */
        public UserDto update(@NotNull User user, @NotNull UserAccountOperationInfo userAccountOperationInfo, @NotNull UserPersonInfo userPersonInfo)
                throws IllegalAccessException
        {
            if (null == user || user.isEmpty()) {
                throw new IllegalAccessException("非法参数: 用户基础信息");
            }
            if (userAccountOperationInfo.isEmpty()
                    || !userAccountOperationInfo.equals(user)) {
                throw new IllegalAccessException("非法参数: [用户 -> 账户操作基础记录]");
            }
            if (userPersonInfo.isEmpty()
                    || !userPersonInfo.equals(user)) {
                throw new IllegalAccessException("非法参数: [用户 -> 个人信息]");
            }

            return new UserDto(user, userAccountOperationInfo, userPersonInfo);
        }

        /**
         * 更新用户 DTO
         *
         * @param id                用户 ID
         * @param age               用户 - 年龄
         * @param registrationTime  注册时间
         * @param ip                最后登陆 IP
         * @param lastLoginTime     最后登录时间
         * @param nickname          用户 - 昵称
         * @param password          用户 - 密码
         * @param introduction      用户 - 简介
         * @param faceImage         用户 - 头像
         * @param sex               用户 - 性别
         *
         * @return 可为 null, 此时输入参数非法
         *
         * @throws IllegalArgumentException 此时 <param>id</param> 非法
         */
        public UserDto update(@NotNull String id
                , @Nullable Integer age
                , @NotNull String registrationTime
                , @NotNull String ip
                , @NotNull String lastLoginTime
                , @NotNull String nickname
                , @NotNull String password
                , @Nullable String introduction
                , @Nullable String faceImage
                , @NotNull String username
                , @Nullable String sex)
                throws IllegalAccessException
        {
            if (!User.Validator.USER.entity_id(id)) {
                throw new IllegalAccessException("非法参数: 用户 ID");
            }

            final User user = User.Factory.USER.update(id, username, password);
            final UserAccountOperationInfo userAccountOperationInfo = UserAccountOperationInfo.Factory.USER.create(username
                    , ip
                    , lastLoginTime
                    , registrationTime);
            final UserPersonInfo userPersonInfo = UserPersonInfo.Factory.USER.create(username
                    , nickname
                    , age
                    , faceImage
                    , introduction
                    , VoUtil.getInstance().getVoByName(Human.SexVo.class, sex));
            if (user.isEmpty()) {
                throw new IllegalAccessException("非法参数: 用户基础信息");
            }
            if (userAccountOperationInfo.isEmpty()
                    || !userAccountOperationInfo.equals(user)) {
                throw new IllegalAccessException("非法参数: [用户 -> 账户操作基础记录]");
            }
            if (userPersonInfo.isEmpty()
                    || !userPersonInfo.equals(user)) {
                throw new IllegalAccessException("非法参数: [用户 -> 个人信息]");
            }

            return new UserDto(user, userAccountOperationInfo, userPersonInfo);
        }

        /**
         * 销毁 DTO
         *
         * @param userDto   {@link UserDto}
         *
         * @return
         *-> {
         *->    <code>true</code> : <b>销毁成功</b>
         *->    , <code>false</code> : <b>销毁失败; 此时 <param>user</param></b> 无效或无法销毁
         *-> }
         */
        public boolean delete(@NotNull UserDto userDto) {
            if (null != userDto && !userDto.isEmpty()) {
                return User.Factory.USER.delete(userDto.dtoId);
            }
            return false;
        }

        /**
         * 获取空对象
         *
         * @return 非 {@code null}.
         */
        @NotNull
        @Override
        public UserDto createDefault() {
            return new UserDto();
        }

    }

    //===== Getter And Setter =====//

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
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

    /**
     * 判断密码是否相同
     *
     * @param password
     *
     * @return {true: <tt>密码相同</tt>, false: <tt>密码不相同</tt>, null: <tt>DTO无效</tt>}
     */
    public Boolean equalsPassword(@NotNull String password) {
        if (Objects.requireNonNull(dtoId()).isEmpty()) {
            return null;
        }
        return Objects.requireNonNull(dtoId()).equalsPassword(password);
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
