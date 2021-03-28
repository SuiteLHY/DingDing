package github.com.suitelhy.dingding.user.service.provider.domain.service.impl;

import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Human;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserReadService;
import github.com.suitelhy.dingding.user.service.api.domain.entity.Log;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.user.service.provider.domain.repository.UserPersonInfoRepository;
import github.com.suitelhy.dingding.user.service.provider.domain.service.LogService;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserAccountOperationInfoService;
import github.com.suitelhy.dingding.user.service.provider.domain.service.UserPersonInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 用户 -> 账户操作记录 - 业务实现
 *
 * @see UserPersonInfo
 * @see User
 * @see UserPersonInfoRepository
 * @see UserPersonInfoService
 * @see UserAccountOperationInfoService
 * @see LogService
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Service("userPersonInfoService")
@Transactional(isolation = Isolation.SERIALIZABLE
        , propagation = Propagation.REQUIRED
        , readOnly = false
        , rollbackFor = Exception.class
        , timeout = 15)
public class UserPersonInfoServiceImpl
        implements UserPersonInfoService {

    @Autowired
    private UserPersonInfoRepository userPersonInfoRepository;

    @Autowired
    private UserAccountOperationInfoService userAccountOperationInfoService;

    @Autowired
    private LogService logService;

    @Reference
    private SecurityUserReadService securityUserReadService;

    /**
     * 查询记录列表
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     *
     * @return {@link Page}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull Page<UserPersonInfo> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            //-- 非法输入: <param>pageIndex</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageIndex"
                    , pageIndex
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        Sort.TypedSort<UserPersonInfo> typedSort = Sort.sort(UserPersonInfo.class);
        @NotNull Sort sort = typedSort.by(UserPersonInfo::getUsername).ascending();
        @NotNull Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return userPersonInfoRepository.findAll(page);
    }

    /**
     * 查询记录列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull Long selectCount(int pageSize) {
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final long dataNumber = userPersonInfoRepository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询指定的记录
     *
     * @param id 数据 ID   {@link UserPersonInfo.Validator#id(String)}
     * @return {@link UserPersonInfo}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull UserPersonInfo selectById(@NotNull String id) {
        if (!UserPersonInfo.Validator.USER.id(id)) {
            //-- 非法输入: [数据 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[数据 ID]"
                    , id
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return userPersonInfoRepository.findById(id)
                .orElseGet(UserPersonInfo.Factory.USER::createDefault);
    }

    /**
     * 查询指定的记录
     *
     * @param username 用户名称    {@link User.Validator#username(String)}
     * @return {@link UserPersonInfo}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull UserPersonInfo selectByUsername(@NotNull String username) {
        if (! UserPersonInfo.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "用户名称"
                    , username
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return userPersonInfoRepository.findByUsername(username)
                .orElseGet(UserPersonInfo.Factory.USER::createDefault);
    }

    /**
     * 查询指定的记录
     *
     * @param nickname [用户 - 昵称]   {@link UserPersonInfo.Validator#nickname(String)}
     *
     * @return {@link UserPersonInfo}
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED
            , propagation = /*Propagation.SUPPORTS*/Propagation.REQUIRED
            , readOnly = true
            , rollbackFor = Exception.class
            , timeout = 10)
    public @NotNull UserPersonInfo selectByNickname(@NotNull String nickname) {
        if (! UserPersonInfo.Validator.USER.username(nickname)) {
            //-- 非法输入: [用户 - 昵称]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 - 昵称]"
                    , nickname
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return userPersonInfoRepository.findByNickname(nickname)
                .orElseGet(UserPersonInfo.Factory.USER::createDefault);
    }

    /**
     * 新增一条记录
     *
     * @param user     [用户 -> 个人信息]    {@link UserPersonInfo}
     * @param operator 操作者               {@link SecurityUser}
     *
     * @return 操作是否成功
     */
    @Override
    public boolean insert(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == user || !user.isEntityLegal()) {
            //-- 非法输入: [用户 -> 个人信息]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 - 个人信息]"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final @NotNull UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (userPersonInfoRepository.existsByUsername(user.getUsername())) {
            //--- 已存在指定用户的情况
            return !userPersonInfoRepository.findByUsername(user.getUsername()).get().isEmpty();
        }

        final UserPersonInfo newUser = userPersonInfoRepository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__ADD.name
                    , "操作后的用户数据异常"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final @NotNull Log newLog_UserPersonInfo = Log.Factory.UserPersonInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_PERSON_INFO__ADD
                , newUser
                , operator
                , operator_OperationInfo);
        if (! logService.insert(newLog_UserPersonInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__ADD.name
                    , "生成操作日志记录失败"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

    //===== 修改操作 =====//

    /**
     * 更新指定的记录
     *
     * @param user      {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return 操作是否成功
     */
    @Override
    public boolean update(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: [用户 -> 个人信息]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 -> 个人信息]"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_UserAccountOperationInfo
                || operator_UserAccountOperationInfo.isEmpty()
                || ! operator_UserAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础户记录]"
                    , operator_UserAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull UserPersonInfo newUser = userPersonInfoRepository.saveAndFlush(user);
        if (newUser.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__UPDATE.name
                    , "操作后的用户数据异常"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final @NotNull Log newLog_UserPersonInfo = Log.Factory.UserPersonInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_PERSON_INFO__UPDATE
                , newUser
                , operator
                , operator_UserAccountOperationInfo);
        if (! logService.insert(newLog_UserPersonInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__UPDATE.name
                    , "生成操作日志记录失败"
                    , newUser
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

    /**
     * 更新指定的记录
     *
     * @param old_userPersonInfo                {@linkplain UserPersonInfo 被修改的[用户 -> 个人信息]}
     * @param new_userPersonInfo_data           {@linkplain Map 被修改的[用户 -> 个人信息] <- 需要更新的数据}
     * · 数据结构:
     * {
     *  "nickname": [用户 - 昵称],
     *  "age": [用户 - 年龄],
     *  "faceImage": [用户 - 头像],
     *  "introduction": [用户 - 简介],
     *  "sex": [用户 - 性别]
     * }
     * @param operator                          {@linkplain SecurityUser 操作者}
     * @param operator_UserAccountOperationInfo {@linkplain UserAccountOperationInfo [操作者 - 账户操作基础记录]}
     * @param operator_userRole                 {@linkplain SecurityUserRole [操作者 - （安全认证）用户 ←→ 角色]}
     * @param operator_role                     {@linkplain SecurityRole [操作者 - （安全认证）角色]}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull UserPersonInfo old_userPersonInfo, @NotNull Map<String, Object> new_userPersonInfo_data, @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo, @NotNull SecurityUserRole operator_userRole, @NotNull SecurityRole operator_role)
            throws IllegalArgumentException, BusinessAtomicException
    {
        if (null == old_userPersonInfo || old_userPersonInfo.isEmpty()) {
            //-- 非法输入: 被修改的[用户 -> 个人信息] <- 原始版本业务全量数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "被修改的用户 <- 原始版本业务全量数据"
                    , old_userPersonInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == new_userPersonInfo_data
                || (! new_userPersonInfo_data.containsKey("nickname") && ! new_userPersonInfo_data.containsKey("age") && ! new_userPersonInfo_data.containsKey("faceImage") && ! new_userPersonInfo_data.containsKey("introduction") && ! new_userPersonInfo_data.containsKey("sex")))
        {
            //-- 非法输入: 需要更新的数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "被修改的[用户 -> 个人信息] <- 需要更新的数据"
                    , new_userPersonInfo_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (new_userPersonInfo_data.containsKey("nickname")
                && ! UserPersonInfo.Validator.USER.nickname((String) new_userPersonInfo_data.get("nickname")))
        {
            //-- 非法输入: 需要更新的数据 => [用户 - 昵称]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [用户 - 昵称]"
                    , new_userPersonInfo_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_userPersonInfo_data.containsKey("age")
                && ! UserPersonInfo.Validator.USER.age((Integer) new_userPersonInfo_data.get("age")))
        {
            //-- 非法输入: 需要更新的数据 => [用户 - 年龄]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [用户 - 年龄]"
                    , new_userPersonInfo_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_userPersonInfo_data.containsKey("faceImage")
                && ! UserPersonInfo.Validator.USER.faceImage((String) new_userPersonInfo_data.get("faceImage")))
        {
            //-- 非法输入: 需要更新的数据 => [用户 - 头像]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [用户 - 头像]"
                    , new_userPersonInfo_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_userPersonInfo_data.containsKey("introduction")
                && ! UserPersonInfo.Validator.USER.introduction((String) new_userPersonInfo_data.get("introduction")))
        {
            //-- 非法输入: 需要更新的数据 => [用户 - 简介]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [用户 - 简介]"
                    , new_userPersonInfo_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_userPersonInfo_data.containsKey("sex")
                && ! UserPersonInfo.Validator.USER.sex((Human.SexVo) new_userPersonInfo_data.get("sex")))
        {
            //-- 非法输入: 需要更新的数据 => [用户 - 性别]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [用户 - 性别]"
                    , new_userPersonInfo_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (operator_UserAccountOperationInfo.isEmpty()
                || ! operator_UserAccountOperationInfo.equals(operator))
        {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>%s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者 <- 无有效的账户操作记录"
                    , operator_UserAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! old_userPersonInfo.equals(operator)
                && ! securityUserReadService.existAdminPermission(operator.getUsername()))
        {
            //-- 非法输入: 操作者 <- 没有所需的操作权限
            throw new IllegalArgumentException(String.format("非法参数:<description>%s</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者 <- 没有所需的操作权限"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator_role || operator_role.isEmpty()) {
            //-- 非法输入: [操作者 - （安全认证）角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[操作者 - （安全认证）角色]"
                    , operator_role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator_userRole
                || operator_userRole.isEmpty()
                || ! operator_userRole.equals(operator, operator_role))
        {
            //-- 非法输入: [操作者 - （安全认证）用户 ←→ 角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[操作者 - （安全认证）用户 ←→ 角色]"
                    , operator_userRole
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final UserPersonInfo latest_userPersonInfo = userPersonInfoRepository.findByUsername(old_userPersonInfo.getUsername())
                .orElseGet(null);
        if (null == latest_userPersonInfo) {
            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "原始版本对应数据已被删除"
                    , old_userPersonInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        } else if (latest_userPersonInfo.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "原始版本对应数据异常"
                    , old_userPersonInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (! latest_userPersonInfo.equals(old_userPersonInfo)
                || ! latest_userPersonInfo.equalsOnAllData(old_userPersonInfo))
        {
            //-- 非法输入: 被修改的[用户 -> 个人信息] -> 已过期
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "被修改的[用户 -> 个人信息]"
                    , "已过期"
                    , latest_userPersonInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull UserPersonInfo new_userPersonInfo = UserPersonInfo.Factory.USER.update(latest_userPersonInfo.getId()
                , old_userPersonInfo.getUsername()
                , old_userPersonInfo.getNickname()
                , old_userPersonInfo.getAge()
                , old_userPersonInfo.getFaceImage()
                , old_userPersonInfo.getIntroduction()
                , old_userPersonInfo.getSex());
        if (new_userPersonInfo_data.containsKey("nickname")
                && ! new_userPersonInfo.setNickname((String) new_userPersonInfo_data.get("nickname"), operator, operator_userRole, operator_role))
        {
            //-- 数据更新失败: 需要更新的数据 => [用户 - 昵称]
            throw new IllegalArgumentException(String.format("数据更新失败:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [用户 - 昵称]"
                    , new_userPersonInfo_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_userPersonInfo_data.containsKey("age")
                && ! new_userPersonInfo.setAge((Integer) new_userPersonInfo_data.get("age"), operator, operator_userRole, operator_role))
        {
            //-- 数据更新失败: 需要更新的数据 => [用户 - 年龄]
            throw new IllegalArgumentException(String.format("数据更新失败:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [用户 - 年龄]"
                    , new_userPersonInfo_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_userPersonInfo_data.containsKey("faceImage")
                && ! new_userPersonInfo.setFaceImage((String) new_userPersonInfo_data.get("faceImage"), operator, operator_userRole, operator_role))
        {
            //-- 数据更新失败: 需要更新的数据 => [用户 - 头像]
            throw new IllegalArgumentException(String.format("数据更新失败:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [用户 - 头像]"
                    , new_userPersonInfo_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_userPersonInfo_data.containsKey("introduction")
                && ! new_userPersonInfo.setIntroduction((String) new_userPersonInfo_data.get("introduction"), operator, operator_userRole, operator_role))
        {
            //-- 数据更新失败: 需要更新的数据 => [用户 - 简介]
            throw new IllegalArgumentException(String.format("数据更新失败:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [用户 - 简介]"
                    , new_userPersonInfo_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_userPersonInfo_data.containsKey("sex")
                && ! new_userPersonInfo.setSex((Human.SexVo) new_userPersonInfo_data.get("sex"), operator, operator_userRole, operator_role))
        {
            //-- 数据更新失败: 需要更新的数据 => [用户 - 性别]
            throw new IllegalArgumentException(String.format("数据更新失败:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => [用户 - 性别]"
                    , new_userPersonInfo_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final @NotNull UserPersonInfo newUserPersonInfo = userPersonInfoRepository.saveAndFlush(new_userPersonInfo);
        if (newUserPersonInfo.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__UPDATE.name
                    , "操作后的数据异常"
                    , newUserPersonInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //===== 操作日志记录 =====//

        final @NotNull Log newLog_UserPersonInfo = Log.Factory.UserPersonInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_PERSON_INFO__UPDATE
                , newUserPersonInfo
                , operator
                , operator_UserAccountOperationInfo);
        if (! logService.insert(newLog_UserPersonInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__UPDATE.name
                    , "生成操作日志失败"
                    , newLog_UserPersonInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //==========//

        return true;
    }

    //===== 删除操作 =====//

    /**
     * 删除指定的记录
     *
     * @param user     [用户 -> 个人信息]    {@link UserPersonInfo}
     * @param operator 操作者               {@link SecurityUser}
     *
     * @return 操作是否成功
     */
    @Override
    public boolean delete(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws BusinessAtomicException
    {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: [用户 -> 账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 -> 账户操作基础记录]"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator_OperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (! userPersonInfoRepository.existsByUsername(user.getUsername())) {
            return true;
        }

        userPersonInfoRepository.deleteByUsername(user.getUsername());

        //=== 操作日志记录 ===//

        final @NotNull Log newLog_UserPersonInfo = Log.Factory.UserPersonInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_PERSON_INFO__DELETE
                , user
                , operator
                , operator_OperationInfo);
        if (! logService.insert(newLog_UserPersonInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__DELETE.name
                    , "生成操作日志记录失败"
                    , newLog_UserPersonInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //======//

        return true;
    }

    /**
     * 删除指定的记录
     *
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user     [用户 -> 个人信息]    {@link UserPersonInfo}
     * @param operator 操作者               {@link SecurityUser}
     *
     * @return 操作是否成功
     */
    @Override
    public boolean deleteAndValidate(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws BusinessAtomicException
    {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: [用户 -> 账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "[用户 -> 账户操作基础记录]"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        final UserAccountOperationInfo operator_OperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
        if (null == operator_OperationInfo || operator_OperationInfo.isEmpty()) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础户记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator_OperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (! userPersonInfoRepository.existsByUsername(user.getUsername())) {
            return true;
        }

        userPersonInfoRepository.deleteByUsername(user.getUsername());
        if (userPersonInfoRepository.existsByUsername(user.getUsername())) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__DELETE.name
                    , "更新后的数据异常"
                    , user
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final @NotNull Log newLog_UserPersonInfo = Log.Factory.UserPersonInfo.LOG.create(null
                , null
                , HandleType.LogVo.USER__USER_PERSON_INFO__DELETE
                , user
                , operator, operator_OperationInfo);
        if (! logService.insert(newLog_UserPersonInfo)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , HandleType.LogVo.USER__USER_PERSON_INFO__DELETE.name
                    , "生成操作日志记录失败"
                    , newLog_UserPersonInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

}
