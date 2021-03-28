package github.com.suitelhy.dingding.sso.server.application.task;

import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.domain.Page;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.AbstractSecurityUser;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.sso.server.application.task.impl.UserTaskImpl;
import github.com.suitelhy.dingding.sso.server.infrastructure.application.dto.BasicUserDto;
import github.com.suitelhy.dingding.sso.server.infrastructure.application.dto.UserDto;

import javax.validation.constraints.NotNull;
import java.util.List;

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
    @NotNull TaskResult<Page<UserDto>> selectAll(int pageCount, int pageSize, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

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
     * @return 操作结果   {@link TaskResult}
     */
    @NotNull TaskResult<BasicUserDto> delete(@NotNull /*UserDto*/BasicUserDto userDto, OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails operator);

}
