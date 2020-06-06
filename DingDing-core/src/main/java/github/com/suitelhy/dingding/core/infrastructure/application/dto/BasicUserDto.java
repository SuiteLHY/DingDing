package github.com.suitelhy.dingding.core.infrastructure.application.dto;

import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.infrastructure.application.model.DtoModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Human;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public class BasicUserDto
        implements DtoModel<User, String> {

    private static final long serialVersionUID = 1L;

    // 用户 - 昵称
    protected String nickname;

    /*// 用户 - 头像
    protected String faceImage;*/

    // 用户名称
    protected String username;

    //===== DtoModel =====//
    @NotNull
    private final transient User dtoId;

    protected BasicUserDto(@NotNull User dtoId)
            throws IllegalArgumentException {
        if (null == dtoId || dtoId.isEmpty()) {
            throw new IllegalArgumentException("非法参数: <param>dtoId</param>");
        }
        this.dtoId = dtoId;

        this.nickname = this.dtoId.getNickname();
        /*this.faceImage = this.dtoId.getFaceImage();*/
        this.username = this.dtoId.getUsername();
    }

    /**
     * 唯一标识 <- DTO 对象
     *
     * @param username
     * @param password
     * @return The unique identify of the DTO, or null.
     */
    @Nullable
    public User dtoId(@NotNull String username, @NotNull String password) {
        if (User.Validator.USER.username(username)
                && User.Validator.USER.password(password)) {
            if (this.dtoId.getUsername().equals(username)
                    && this.dtoId.equalsPassword(password)) {
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
     * @return
     * @Description 需要实现类实现该接口
     */
    @Override
    public boolean isEntityLegal() {
        return dtoId.isEntityLegal();
    }

    /**
     * 是否无效 <- DTO 对象
     *
     * @return
     * @Description 使用默认实现.
     */
    @Override
    public boolean isEmpty() {
        return DtoModel.isEmpty(this);
    }

    //===== Factory =====//
    public enum Factory implements EntityFactory<BasicUserDto> {
        USER_DTO;

        /**
         * 创建用户 DTO
         *
         * @param user
         * @return
         */
        @NotNull
        public BasicUserDto create(@NotNull User user) {
            return new BasicUserDto(user);
        }

        /**
         * 创建用户 DTO
         *
         * @param age         用户 - 年龄
         * @param firsttime   注册时间
         * @param nickname    用户 - 昵称
         * @param password    用户密码
         * @param profile     用户 - 简介
         * @param profilehead 用户 - 头像
         * @param username    用户名称
         * @param sex         用户 - 性别
         * @throws IllegalArgumentException
         */
        @NotNull
        public BasicUserDto create(@Nullable Integer age
                , @NotNull String firsttime
                , @NotNull String ip
                , @NotNull String lasttime
                , @NotNull String nickname
                , @NotNull String password
                , @Nullable String profile
                , @Nullable String profilehead
                , @NotNull String username
                , @Nullable String sex) {
            User newUser = User.Factory.USER.create(age, firsttime, ip
                    , lasttime, nickname, password
                    , profile, profilehead, username
                    , VoUtil.getVoByName(Human.SexVo.class, sex));
            return new BasicUserDto(newUser);
        }

        /**
         * 更新用户 DTO
         *
         * @param id          用户ID
         * @param age         用户 - 年龄
         * @param firsttime   注册时间
         * @param ip          最后登陆IP
         * @param lasttime    最后登录时间
         * @param nickname    用户 - 昵称
         * @param password    用户 - 密码
         * @param profile     用户 - 简介
         * @param profilehead 用户 - 头像
         * @param sex         用户 - 性别
         * @return 可为 null, 此时输入参数非法
         * @throws IllegalArgumentException 此时 <param>id</param> 非法
         */
        public BasicUserDto update(@NotNull String id
                , @Nullable Integer age
                , @NotNull String firsttime
                , @NotNull String ip
                , @NotNull String lasttime
                , @NotNull String nickname
                , @NotNull String password
                , @Nullable String profile
                , @Nullable String profilehead
                , @NotNull String username
                , @Nullable String sex) {
            User user = User.Factory.USER.update(id, age, firsttime
                    , ip, lasttime, nickname
                    , password, profile, profilehead
                    , username, VoUtil.getVoByName(Human.SexVo.class, sex));
            return new BasicUserDto(user);
        }

        /**
         * 销毁 DTO
         *
         * @param userDto
         * @return {<code>true</code> : <b>销毁成功</b>
         * ->      , <code>false</code> : <b>销毁失败; 此时 <param>user</param></b> 无效或无法销毁}
         */
        public boolean delete(@NotNull BasicUserDto userDto) {
            if (null != userDto && !userDto.isEmpty()) {
                return User.Factory.USER.delete(userDto.dtoId);
            }
            return false;
        }

    }

    //===== getter and setter =====//

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 判断密码是否相同
     *
     * @param password
     * @return {true: <tt>密码相同</tt>, false: <tt>密码不相同</tt>, null: <tt>DTO无效</tt>}
     */
    public Boolean equalsPassword(String password) {
        if (dtoId().isEmpty()) {
            return null;
        }
        return dtoId().equalsPassword(password);
    }

    /*public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }*/

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
