package github.com.suitelhy.dingding.user.service.application.task;

import github.com.suitelhy.dingding.security.service.api.infrastructure.web.AbstractSecurityUser;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.user.service.application.task.impl.UserTaskImpl;
import github.com.suitelhy.dingding.user.service.infrastructure.application.dto.BasicUserDto;
import github.com.suitelhy.dingding.user.service.infrastructure.application.dto.UserDto;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 用户 - 任务调度接口
 *
 * @see UserTaskImpl
 * @see OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails
 */
public interface UserTask {

    /**
     * 查询用户列表
     *
     * @param pageCount 页码, 从1开始
     * @param pageSize  每页数据容量, 最大值为 20
     * @param operator  操作者
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<List<UserDto>> selectAll(int pageCount, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询指定的用户
     *
     * @param userid
     * @param operator 操作者
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> selectUserByUserid(String userid, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询指定的用户
     *
     * @param username
     * @param operator 操作者
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> selectUserByUsername(String username, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 查询用户列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     * @param operator 操作者
     *
     * @return {@linkplain TaskResult 操作结果, [分页 - 总页数]}
     */
    @NotNull TaskResult<Long> selectCount(int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 新增一个用户
     *
     * @param userDto           用户
     * @param passwordPlaintext 用户密码_明文
     * @param operator          操作者
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> registerUser(@NotNull UserDto userDto
            , @NotNull String passwordPlaintext
            , @NotNull AbstractSecurityUser operator);

    /**
     * 新增一个用户
     *
     * @param userDto           用户
     * @param passwordPlaintext 用户密码_明文
     * @param operator          操作者
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> registerUser(@NotNull UserDto userDto
            , @NotNull String passwordPlaintext
            , OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 更新指定的用户
     *
     * @Description 更新的数据包括[用户 - 基础信息]和[用户 -> 个人信息].
     *
     * @param userDto  用户
     * @param operator 操作者（具备管理员权限）
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> update(@NotNull UserDto userDto, @NotNull AbstractSecurityUser operator);

    /**
     * 更新指定的用户
     *
     * @Description 更新的数据包括[用户 - 基础信息]和[用户 -> 个人信息].
     *
     * @param userDto  用户
     * @param operator 操作者（具备管理员权限）
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> update(@NotNull UserDto userDto, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 更新指定的用户 -> [用户 - 基础信息]
     *
     * @Description 更新的数据仅限于[用户 - 基础信息].
     *
     * @param old_user_data     {@linkplain Map 被修改的[用户 - 基础信息] <- 原始版本全量数据}
     * · 数据结构:
     * {
     *  “userid”: [用户 ID],
     *  “username”: 用户名称,
     *  "password": [用户 - 密码（密文）]
     * }
     * @param new_user_data     {@linkplain Map 被修改的[用户 - 基础信息] <- 需要更新的数据}
     * · 数据结构:
     * {
     *  “password”: [用户密码_明文],
     *  “status”: [账号状态]
     * }
     * @param operator          {@linkplain AbstractSecurityUser [操作者（具备管理员权限）]}
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> updateUser(@NotNull Map<String, Object> old_user_data, @NotNull Map<String, Object> new_user_data, @NotNull AbstractSecurityUser operator);

    /**
     * 更新指定的用户 -> [用户 - 基础信息]
     *
     * @Description 更新的数据仅限于[用户 - 基础信息].
     *
     * @param old_user_data     {@linkplain Map 被修改的[用户 - 基础信息] <- 原始版本全量数据}
     * · 数据结构:
     * {
     *  “userid”: [用户 ID],
     *  “username”: 用户名称,
     *  "password": [用户 - 密码（密文）]
     * }
     * @param new_user_data     {@linkplain Map 被修改的[用户 - 基础信息] <- 需要更新的数据}
     * · 数据结构:
     * {
     *  “password”: [用户密码_明文],
     *  “status”: [账号状态]
     * }
     * @param operator          {@linkplain OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails [操作者（具备管理员权限）]}
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> updateUser(@NotNull Map<String, Object> old_user_data, @NotNull Map<String, Object> new_user_data, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 更新指定的用户 -> [用户 -> 个人信息]
     *
     * @Description 更新的数据包括[用户 -> 个人信息].
     *
     * @param userDto  用户
     * @param operator 操作者
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> updateUserPersonInfo(@NotNull UserDto userDto, @NotNull AbstractSecurityUser operator);

    /**
     * 更新指定的用户 -> [用户 -> 个人信息]
     *
     * @Description 更新的数据包括[用户 -> 个人信息].
     *
     * @param userDto  用户
     * @param operator 操作者
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> updateUserPersonInfo(@NotNull UserDto userDto, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 更新指定的用户 -> [用户 -> 个人信息]
     *
     * @Description 更新的数据仅限于[用户 - 个人信息].
     *
     * @param old_userPersonInfo_data {@linkplain Map [用户 -> 个人信息] <- 原始版本全量数据}
     * · 数据结构:
     * {
     *  "id": [数据 ID],
     *  "username": 用户名称,
     *  "nickname": [用户 - 昵称],
     *  "age": [用户 - 年龄],
     *  "faceImage": [用户 - 头像],
     *  "introduction": [用户 - 简介],
     *  "sex": [用户 - 性别]
     * }
     * @param new_userPersonInfo_data {@linkplain Map [用户 -> 个人信息] <- 需要更新部分的数据}
     * · 数据结构:
     * {
     *  "nickname": [用户 - 昵称],
     *  "age": [用户 - 年龄],
     *  "faceImage": [用户 - 头像],
     *  "introduction": [用户 - 简介],
     *  "sex": [用户 - 性别]
     * }
     * @param operator                {@linkplain AbstractSecurityUser 操作者（具备管理员权限）}
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> updateUserPersonInfo(@NotNull Map<String, Object> old_userPersonInfo_data, @NotNull Map<String, Object> new_userPersonInfo_data, @NotNull AbstractSecurityUser operator);

    /**
     * 更新指定的用户 -> [用户 -> 个人信息]
     *
     * @Description 更新的数据仅限于[用户 - 个人信息].
     *
     * @param old_userPersonInfo_data   {@linkplain Map [用户 -> 个人信息] <- 原始版本全量数据}
     * · 数据结构:
     * {
     *  "id": [数据 ID],
     *  "username": 用户名称,
     *  "nickname": [用户 - 昵称],
     *  "age": [用户 - 年龄],
     *  "faceImage": [用户 - 头像],
     *  "introduction": [用户 - 简介],
     *  "sex": [用户 - 性别]
     * }
     * @param new_userPersonInfo_data   {@linkplain Map [用户 -> 个人信息] <- 需要更新部分的数据}
     * · 数据结构:
     * {
     *  "nickname": [用户 - 昵称],
     *  "age": [用户 - 年龄],
     *  "faceImage": [用户 - 头像],
     *  "introduction": [用户 - 简介],
     *  "sex": [用户 - 性别]
     * }
     * @param operator                  {@linkplain OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails 操作者（具备管理员权限）}
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> updateUserPersonInfo(@NotNull Map<String, Object> old_userPersonInfo_data, @NotNull Map<String, Object> new_userPersonInfo_data, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 更新指定的用户 -> [用户 -> 账户操作基础记录]
     *
     * @Description 更新的数据仅限于[用户 - 账户操作基础记录].
     *
     * @param old_userAccountOperationInfo_data {@linkplain Map [用户 -> 账户操作基础记录] <- 原始版本全量数据}
     * · 数据结构:
     * {
     *  "id": [数据 ID],
     *  "username": 用户名称,
     *  "ip": [最后登陆 IP],
     *  "lastLoginTime": 最后登录时间,
     *  "registrationTime": 注册时间
     * }
     * @param new_userAccountOperationInfo_data {@linkplain Map [用户 -> 账户操作基础记录] <- 需要更新部分的数据}
     * · 数据结构:
     * {
     *  "ip": [最后登陆 IP],
     *  "lastLoginTime": 最后登录时间
     * }
     * @param operator                          {@linkplain AbstractSecurityUser 操作者（具备管理员权限）}
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> updateUserAccountOperationInfo(@NotNull Map<String, Object> old_userAccountOperationInfo_data, @NotNull Map<String, Object> new_userAccountOperationInfo_data, @NotNull AbstractSecurityUser operator);

    /**
     * 更新指定的用户 -> [用户 -> 账户操作基础记录]
     *
     * @Description 更新的数据仅限于[用户 - 账户操作基础记录].
     *
     * @param old_userAccountOperationInfo_data {@linkplain Map [用户 -> 账户操作基础记录] <- 原始版本全量数据}
     * · 数据结构:
     * {
     *  "id": [数据 ID],
     *  "username": 用户名称,
     *  "ip": [最后登陆 IP],
     *  "lastLoginTime": 最后登录时间,
     *  "registrationTime": 注册时间
     * }
     * @param new_userAccountOperationInfo_data {@linkplain Map [用户 -> 账户操作基础记录] <- 需要更新部分的数据}
     * · 数据结构:
     * {
     *  "ip": [最后登陆 IP],
     *  "lastLoginTime": 最后登录时间
     * }
     * @param operator                          {@linkplain OAuth2AuthenticationInfo.AbstractUserAuthentication.AbstractDetails 操作者（具备管理员权限）}
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<UserDto> updateUserAccountOperationInfo(@NotNull Map<String, Object> old_userAccountOperationInfo_data, @NotNull Map<String, Object> new_userAccountOperationInfo_data, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 删除指定的用户
     *
     * @param userDto  用户
     * @param operator 操作者
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<BasicUserDto> delete(@NotNull /*UserDto*/BasicUserDto userDto, @NotNull AbstractSecurityUser operator);

    /**
     * 删除指定的用户
     *
     * @param userDto  用户
     * @param operator 操作者
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<BasicUserDto> delete(@NotNull /*UserDto*/BasicUserDto userDto, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

    /**
     * 销毁指定的用户
     *
     * @Design 数据保留, 正常业务功能不可用.
     *
     * @param userDto  用户
     * @param operator 操作者
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<BasicUserDto> destroy(@NotNull BasicUserDto userDto, @NotNull AbstractSecurityUser operator);

    /**
     * 销毁指定的用户
     *
     * @Design 数据保留, 正常业务功能不可用.
     *
     * @param userDto  用户
     * @param operator 操作者
     *
     * @return {@linkplain TaskResult 操作结果}
     */
    @NotNull TaskResult<BasicUserDto> destroy(@NotNull BasicUserDto userDto, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

}
