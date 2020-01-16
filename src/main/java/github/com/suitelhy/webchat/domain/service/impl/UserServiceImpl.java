package github.com.suitelhy.webchat.domain.service.impl;

import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.domain.repository.UserRepository;
import github.com.suitelhy.webchat.domain.service.UserService;
import github.com.suitelhy.webchat.domain.vo.AccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * 用户信息 - 业务实现
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            throw new RuntimeException("非法输入: <param>dataIndex</param>");
        }
        if (pageSize < 1) {
            throw new RuntimeException("非法输入: <param>pageSize</param>");
        }
        Sort.TypedSort<User> userTypedSort = Sort.sort(User.class);
        Sort userSort = userTypedSort.by(User::getUserid).ascending()
                .and(userTypedSort.by(User::getStatus).ascending());
        Pageable page = PageRequest.of(pageIndex, pageSize, userSort);
        return userRepository.findAll(page);
    }

    @Nullable
    @Override
    public User selectUserByUserid(String userid){
        if (!User.Validator.USER.userid(userid)) {
            throw new RuntimeException("非法输入: 用户ID");
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
    @Override
    public User selectUserByUsername(@NotNull String username) {
        if (!User.Validator.USER.username(username)) {
            throw new RuntimeException("非法输入: 用户名称");
        }
        Optional<User> result = userRepository.findUserByUsernameAndStatus(username
                , AccountVo.Status.NORMAL);
        return result.orElse(null);
    }

    @Override
    public Long selectCount(int pageSize) {
        if (pageSize < 1) {
            throw new RuntimeException("非法输入: <param>pageSize</param>");
        }
        long dataNumber = userRepository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    @Override
    public boolean insert(User user) {
        if (null == user || !user.isLegal()) {
            throw new RuntimeException("非法用户");
        }
        return !userRepository.saveAndFlush(user).isEmpty();
    }

    @Override
    public boolean update(User user) {
        if (null == user || user.isEmpty()) {
            throw new RuntimeException("非法用户");
        }
        return !userRepository.saveAndFlush(user).isEmpty();
    }

    @Override
    public boolean delete(User user) {
        if (null == user || user.isEmpty()) {
            throw new RuntimeException("非法用户");
        }
        userRepository.deleteById(user.id());
        return true;
    }

    /**
     * 删除指定的用户
     *
     * @param user
     * @return 操作是否成功
     * @Description 删除成功后校验持久化数据; 主要是
     * -> 避免在未提交的事务中进行对操作结果的非预期判断.
     */
    @Override
    public boolean deleteAndValidate(User user) {
        if (null == user || user.isEmpty()) {
            throw new RuntimeException("非法用户");
        }
        userRepository.deleteById(user.id());
        return !userRepository.existsById(user.id());
    }

}
