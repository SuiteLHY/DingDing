package github.com.suitelhy.webchat.application.service.impl;

import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.domain.repository.UserRepository;
import github.com.suitelhy.webchat.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户信息相关业务
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    // (@Autowired 在 @Service 标识的类中可以省略)
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> selectAll(int dataIndex, int pageSize) {
        return userRepository.selectAll(dataIndex, pageSize);
    }

    @Override
    public User selectUserByUserid(String userid) {
        return userRepository.selectUserByUserid(userid);
    }

    @Override
    public Integer selectCount(int pageSize) {
        Integer dataNumber = Integer.parseInt(userRepository.selectCount().getUserid());
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber/pageSize + 1);
    }

    @Override
    public boolean insert(User user) {
        return userRepository.insert(user);
    }

    @Override
    public boolean update(User user) {
        return userRepository.update(user);
    }

    @Override
    public boolean delete(User user) {
        return userRepository.delete(user);
    }

}
