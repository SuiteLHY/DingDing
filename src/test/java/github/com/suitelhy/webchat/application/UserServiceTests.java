package github.com.suitelhy.webchat.application;

import github.com.suitelhy.webchat.application.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
        Assert.notNull(userService, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAll() {
        Assert.notEmpty(userService.selectAll(0, 10)
                , "Method selectAll exception");
    }

    @Test
    @Transactional
    public void selectUserByUserid() {
        final String userid = "admin";
        Assert.notNull(userService.selectUserByUserid(userid)
                , "Method selectUserByUserid exception");
    }

}
