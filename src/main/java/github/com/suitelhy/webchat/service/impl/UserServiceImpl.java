package github.com.suitelhy.webchat.service.impl;

import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.domain.mapper.UserMapper;
import github.com.suitelhy.webchat.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户信息相关业务
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> selectAll(int page, int pageSize) {
        return userMapper.selectAll(page, pageSize);
    }

    @Override
    public User selectUserByUserid(String userid) {
        return userMapper.selectUserByUserid(userid);
    }

    @Override
    public Integer selectCount(int pageSize) {
        Integer pageCount = Integer.parseInt(userMapper.selectCount().getUserid());
        return (pageCount % pageSize == 0)
                ? (pageCount / pageSize)
                : (pageCount/pageSize + 1);
    }

    @Override
    public boolean insert(User user) {
        return userMapper.insert(user);
    }

    @Override
    public boolean update(User user) {
        return userMapper.update(user);
    }

    @Override
    public boolean delete(String userid) {
        return userMapper.delete(userid);
    }

}
