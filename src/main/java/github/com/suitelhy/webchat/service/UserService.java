package github.com.suitelhy.webchat.service;

import github.com.suitelhy.webchat.domain.entity.User;

import java.util.List;

public interface UserService {

    List<User> selectAll(int page, int pageSize);

    User selectUserByUserid(String userid);

    Integer selectCount(int pageSize);

    boolean insert(User user);

    boolean update(User user);

    boolean delete(String userid);

}
