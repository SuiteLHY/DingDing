package github.com.suitelhy.dingding.log.service.client.domain.service;

import github.com.suitelhy.dingding.log.service.api.domain.service.write.idempotence.LogIdempotentWriteService;
import github.com.suitelhy.dingding.user.service.api.domain.service.read.LogReadService;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 日志服务客户端 <- 测试单元
 *
 * @Description 日志服务客户端 (消费者) <- 测试单元.
 *
 * @Solution
 * · {@linkplain <a href="https://zhuanlan.zhihu.com/p/83404546">dubbo使用过程中遇到的一些坑 - 知乎</a> dubbo默认采用Hessian方式序列化，但是不能较好的适应Java的泛型，建议把序列化方式改为 kryo}
 */
@SpringBootTest
public class LogServiceTests {

    @Reference
    private LogReadService logReadService;

    @Reference
    private LogIdempotentWriteService logIdempotentWriteService;

    @Test
    public void contextLoads() {
        Assert.notNull(logReadService, "获取测试单元失败");
        Assert.notNull(logIdempotentWriteService, "获取测试单元失败");
    }

    @Test
    /*@Transactional*/
    public void selectAll() {
        // (不报错就行)
        logReadService.selectAll(0, 10);
    }

    @Test
    /*@Transactional*/
    public void selectCount() {
        // (不报错就行)
        logReadService.selectCount(10);
    }

    @Test
    /*@Transactional*/
    public void selectCountByUsername() {
        // (不报错就行)
        logReadService.selectCountByUsername("abc123456", 10);
    }

    @Test
    /*@Transactional*/
    public void selectLogById() {
        // (不报错就行)
        logReadService.selectLogById("1");
    }

    @Test
    /*@Transactional*/
    public void selectLogByUsername() {
        // (不报错就行)
        logReadService.selectLogByUsername("abc123456", 0, 10);
    }

}
