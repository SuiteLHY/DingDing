package github.com.suitelhy.dingding.log.service.provider.domain.repository;//package github.com.suitelhy.dingding.sso.authorization.domain.repository.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@SpringBootTest
public class LogRepositoryTests {

    @Autowired
    private LogRepository repository;

    @Test
    @Transactional
    public void contextLoads() {
        Assert.notNull(repository, "获取测试单元失败");
    }

    @Test
    @Transactional
    public void count() {
        // (不报错就行)
        repository.count();
    }

    @Test
    @Transactional
    public void countByOperatorUsername() {
        // (不报错就行)
        repository.countByOperatorUsername("abc12346");
    }

    @Test
    @Transactional
    public void countByTargetId() {
        // (不报错就行)
        repository.countByTargetId("1");
    }

    @Test
    @Transactional
    public void findAll() {
        // (不报错就行)
        repository.findAll();
    }

    @Test
    @Transactional
    public void findLogById() {
        // (不报错就行)
        repository.findLogById(1L);
    }

    @Test
    @Transactional
    public void findByOperatorUsername() {
        // (不报错就行)
        Pageable pageable = PageRequest.of(0, 10);
        repository.findByOperatorUsername("abc123456", pageable);
    }

    @Test
    @Transactional
    public void findByTargetId() {
        // (不报错就行)
        Pageable pageable = PageRequest.of(0, 10);
        repository.findByTargetId("1", pageable);
    }

}
