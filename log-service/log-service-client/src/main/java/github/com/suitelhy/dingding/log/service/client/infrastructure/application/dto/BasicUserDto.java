package github.com.suitelhy.dingding.log.service.client.infrastructure.application.dto;

import github.com.suitelhy.dingding.core.infrastructure.application.model.DtoFactoryModel;
import github.com.suitelhy.dingding.core.infrastructure.application.model.DtoModel;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.AbstractSecurityUser;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * 用户基础信息
 *
 * @see DtoModel
 */
public class BasicUserDto
        implements DtoModel<User, String> {

    private static final long serialVersionUID = 1L;

    // 用户 ID
    protected String userId;

    // 用户名称
    protected String username;

    //===== DtoModel =====//

    private final transient @NotNull User dtoId;

    /**
     * (Constructor)
     *
     * @Description 用于构造空对象.
     */
    protected BasicUserDto() {
        this.dtoId = User.Factory.USER.createDefault();
    }

    /**
     * (Constructor)
     *
     * @param dtoId {@link User}
     *
     * @throws IllegalArgumentException 此时 {@param dtoId} 非法.
     */
    protected BasicUserDto(@NotNull User dtoId)
            throws IllegalArgumentException
    {
        if (null == dtoId || dtoId.isEmpty()) {
            //-- 非法输入: <param>dtoId</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "dtoId"
                    , dtoId
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        this.dtoId = dtoId;

        this.userId = this.dtoId.getUserid();
        this.username = this.dtoId.getUsername();
    }

    /**
     * (Constructor)
     *
     * @param basicUserDto {@link BasicUserDto}
     *
     * @throws IllegalArgumentException 此时 {@param basicUserDto} 非法.
     */
    protected BasicUserDto(@NotNull BasicUserDto basicUserDto)
            throws IllegalArgumentException
    {
        if (null == basicUserDto || basicUserDto.isEmpty()) {
            //-- 非法输入: <param>basicUserDto</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "basicUserDto"
                    , basicUserDto
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        this.dtoId = basicUserDto.dtoId;

        this.userId = this.dtoId.getUserid();
        this.username = this.dtoId.getUsername();
    }

    /**
     * 唯一标识 <- DTO 对象
     *
     * @param username
     * @param password 用户密码（明文或密文）
     *
     * @return The unique identify of the DTO, or null.
     */
    public @Nullable User dtoId(@NotNull String username, @NotNull String password) {
        if (User.Validator.USER.username(username)
                && (User.Validator.USER.password(password) || User.Validator.USER.passwordPlaintext(password)))
        {
            if (this.dtoId.getUsername().equals(username)
                    && (this.dtoId.equalsPassword(password) || this.dtoId.getPassword().equals(password)))
            {
                //设计: 用户 Entity 对象的获取需要进行严格地控制.
                return this.dtoId;
            }
        }
        return null;
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
     * @Description 需要实现类实现该接口.
     *
     * @return 判断结果
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
     * @return 判断结果
     */
    @Override
    public boolean isEmpty() {
        return DtoModel.isEmpty(this);
    }

    /**
     * 转换为 JSON 格式的字符串
     *
     * @return 转换结果
     */
    @Override
    public @NotNull String toJSONString() {
        return String.format("{\"username\":\"%s\"}", this.username);
    }

    @Override
    public @NotNull String toString() {
        return this.toJSONString();
    }

    //===== Factory =====//

    public enum Factory
            implements DtoFactoryModel<BasicUserDto, String> {
        USER_DTO;

        /**
         * 创建用户 DTO
         *
         * @param user {@link User}
         *
         * @return {@link BasicUserDto}
         */
        public @NotNull BasicUserDto create(@NotNull User user)
                throws IllegalArgumentException
        {
            return new BasicUserDto(user);
        }

        /**
         * 创建用户 DTO
         *
         * @param username 用户名称
         * @param password 用户密码
         *
         * @return {@link BasicUserDto}
         *
         * @throws IllegalArgumentException
         */
        public @NotNull BasicUserDto create(@NotNull String username, @NotNull String password)
                throws IllegalArgumentException
        {
            final User newUser = User.Factory.USER.create(username, password);
            return new BasicUserDto(newUser);
        }

        /**
         * 更新用户 DTO
         *
         * @param id       用户 ID       {@link User.Validator#userid(String)}
         * @param username 用户名称       {@link User.Validator#username(String)}
         * @param password 用户 - 密码    {@link User.Validator#password(String)}
         *
         * @return {@link BasicUserDto}
         *
         * @throws IllegalArgumentException 此时 <param>id</param> 非法
         */
        public @NotNull BasicUserDto update(@NotNull String id
                , @NotNull String username
                , @NotNull String password)
                throws IllegalArgumentException
        {
            final User user = User.Factory.USER.update(id, username, password);
            return new BasicUserDto(user);
        }

        /**
         * 销毁 DTO
         *
         * @param userDto {@link BasicUserDto}
         *
         * @return
         * {
         * {@code true}: <b>销毁成功</b>,
         * {@code false}: <b>销毁失败, 此时 {@param userDto} 无效或无法销毁</b>
         * }
         */
        public boolean delete(@NotNull BasicUserDto userDto) {
            if (null != userDto && ! userDto.isEmpty()) {
                return User.Factory.USER.delete(userDto.dtoId);
            }
            return false;
        }

        /**
         * 获取空对象
         *
         * @return 非 {@code null}.
         */
        @Override
        public @NotNull BasicUserDto createDefault() {
            return new BasicUserDto();
        }

    }

    //===== getter and setter =====//

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //==========//

    /**
     * 等效判断
     *
     * @param securityUser {@link AbstractSecurityUser}
     *
     * @return 判断结果
     */
    public boolean equals(AbstractSecurityUser securityUser) {
        if (null == securityUser
                || securityUser.isEmpty()
                || ! this.isDtoLegal()) {
            return false;
        }
        return this.username.equals(securityUser.getUsername());
    }

    /**
     * 等效判断
     *
     * @param userAuthenticationDetails {@link OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails}
     *
     * @return 判断结果
     */
    public boolean equals(OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails userAuthenticationDetails) {
        if (null == userAuthenticationDetails
                || ! Boolean.TRUE.equals(userAuthenticationDetails.isActive())
                || ! this.isDtoLegal()) {
            return false;
        }
        return this.username.equals(userAuthenticationDetails.getUserName());
    }

    /**
     * 判断密码是否相同
     *
     * @param password
     *
     * @return {true: <tt>密码相同</tt>, false: <tt>密码不相同</tt>, null: <tt>DTO无效</tt>}
     */
    public @Nullable Boolean equalsPassword(String password) {
        if (dtoId.isEmpty()) {
            return null;
        }
        return dtoId.equalsPassword(password);
    }

}
