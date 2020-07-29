package github.com.suitelhy.dingding.core.domain.service.impl;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.entity.User;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.repository.UserRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.UserService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;


/**
 * 用户信息 - 业务实现
 *
 * @Description {@link UserService}
 *
 * @see github.com.suitelhy.dingding.core.domain.service.UserService
 */

@Order(Ordered.LOWEST_PRECEDENCE)
@Service("userService")
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public class UserServiceImpl
        implements UserService {

    @Autowired
    private LogService logService;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            //-- 非法输入: <param>dataIndex</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>dataIndex</param>"));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageSize</param>"));
        }

        Sort.TypedSort<User> typedSort = Sort.sort(User.class);
        Sort sort = typedSort.by(User::getUserid).ascending()
                .and(typedSort.by(User::getStatus).ascending());
        Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return userRepository.findAll(page);
    }

    @Nullable
    @Override
    public User selectUserByUserid(@NotNull String userid) {
        if (!User.Validator.USER.userid(userid)) {
            //-- 非法输入: 用户ID
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 用户ID"));
        }

        Optional<User> result = userRepository.findById(userid);
        return result.orElse(null);
    }

    /**
     * 查询指定的用户
     *
     * @param username
     * @return
     */
    @Nullable
    @Override
    public User selectUserByUsername(@NotNull String username) {
        if (!User.Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 用户名称"));
        }

        Optional<User> result = userRepository.findUserByUsernameAndStatus(username
                , Account.StatusVo.NORMAL);
        return result.orElse(null);
    }

    @Override
    public Long selectCount(int pageSize) {
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageSize</param>"));
        }

        final long dataNumber = userRepository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    @Transactional(isolation  = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean insert(@NotNull User user) {
        if (null == user || !user.isEntityLegal()) {
            //-- 非法输入: 非法用户
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法用户"));
        }

        if (userRepository.existsByUsername((String) user.id()[0])) {
            //--- 已存在指定用户的情况
            return !userRepository.findUserByUsernameAndStatus((String) user.id()[0], Account.StatusVo.NORMAL).get().isEmpty();
        }

        final User newUser = userRepository.saveAndFlush(user);
        if (newUser.isEmpty()) return false;

        if (!securityUserService.insert(SecurityUser.Factory.USER.create(newUser))) {
            //-- 操作失败: 新增 (安全) 用户
            throw new RuntimeException(this.getClass().getSimpleName()
                    .concat(" -> 操作失败: 新增 (安全) 用户"));
        }

        final Log newLog = Log.Factory.USER_LOG.create(HandleType.LogVo.USER_REGISTRATION.description
                , HandleType.LogVo.USER_REGISTRATION
                , newUser);

        return logService.insert(newLog);
    }

    @Override
    @Transactional(isolation  = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean update(@NotNull User user) {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 非法用户
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法用户"));
        }

        final User newUser = userRepository.saveAndFlush(user);

        final Log newLog = Log.Factory.USER_LOG.create(HandleType.LogVo.USER_UPDATE.description
                , HandleType.LogVo.USER_UPDATE
                , newUser);

        return !newUser.isEmpty()
                && logService.insert(newLog);
    }

    @Override
    @Transactional(isolation  = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean delete(@NotNull User user) {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 非法用户
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法用户"));
        }

        final Log newLog = Log.Factory.USER_LOG.create(HandleType.LogVo.DATA_USER_DELETION.description
                , HandleType.LogVo.DATA_USER_DELETION
                , user);

        userRepository.deleteById(user.getUserid());

        return logService.insert(newLog);
    }

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据; 主要是避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation  = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean deleteAndValidate(@NotNull User user) {
        if (null == user || user.isEmpty()) {
            //-- 非法输入: 非法用户
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法用户"));
        }

        final Log newLog = Log.Factory.USER_LOG.create(HandleType.LogVo.DATA_USER_DELETION.description
                , HandleType.LogVo.DATA_USER_DELETION
                , user);

        userRepository.deleteById(user.getUserid());

        return !userRepository.existsById(user.getUserid())
                && logService.insert(newLog);
    }

}
