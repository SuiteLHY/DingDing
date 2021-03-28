package github.com.suitelhy.dingding.log.service.provider.domain.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * (安全) 用户 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 */
@SpringBootTest
public class LogServiceTests {

    @Autowired
    private LogService logService;

    @Test
    public void contextLoads() {
        Assert.notNull(logService, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void selectAll() {
        // (不报错就行)
        logService.selectAll(0, 10);
    }

    @Test
    @Transactional
    public void selectCount() {
        // (不报错就行)
        logService.selectCount(10);
    }

    @Test
    @Transactional
    public void selectCountByUsername() {
        // (不报错就行)
        logService.selectCountByUsername("abc123456", 10);
    }

    @Test
    @Transactional
    public void selectLogById() {
        // (不报错就行)
        logService.selectLogById("1");
    }

    @Test
    @Transactional
    public void selectLogByUsername() {
        // (不报错就行)
        logService.selectLogByUsername("abc123456", 0, 10);
    }

}
