package github.com.suitelhy.dingding.userservice.infrastructure.config.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * 主数据源配置
 *
 * @Reference <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#core.repository-populators">
 *->     Spring Data JPA-参考文档</a>
 *
 */
@Configuration
// {@Note 项目复用 - copy 代码以后, 一定要记得这些配置类中的所有配置项必需要彻底检查;
//->      出问题的时候, 没想起来, Context加载失败报错找不到repository的bean; 最后是找控制面板信息发现
//->      ".s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 9ms. Found 0 JPA repository interfaces."}
@EnableJpaRepositories(basePackages= {"github.com.suitelhy.dingding.userservice.domain.repository"
            , "github.com.suitelhy.dingding.core.domain.repository"
        }, entityManagerFactoryRef="primaryEntityManagerFactory"
        , transactionManagerRef="primaryTransactionManager")
@EnableTransactionManagement
public class PrimaryDataSourceConfig {

    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource primaryDataSource;

    @Autowired
    @Qualifier("vendorProperties")
    private Map<String, Object> vendorProperties;

    /**
     * 配置 事务管理器 (TransactionManager)
     * @param builder
     * @return
     */
    @Bean(name = "primaryTransactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory(builder).getObject());
        return txManager;
    }

    /**
     * 配置 实体管理器工厂 (EntityManagerFactory)
     * @return
     */
    @Bean(name = "primaryEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(primaryDataSource)
                .packages("github.com.suitelhy.dingding.server.domain.entity"
                        , "github.com.suitelhy.dingding.core.domain.entity") // 设置实体类所在位置
                .persistenceUnit("primaryPersistenceUnit")
                // ↑ 持久性单元的名称.
                //-> 如果仅构建一个 EntityManagerFactory, 则可以忽略这一点, 但是如果同一应用程序中有多个, 则应给它们指定不同的名称.
                //-> @Reference <a href="https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/orm/jpa/EntityManagerFactoryBuilder.Builder.html">
                //->          EntityManagerFactoryBuilder.Builder（Spring Boot Docs 2.2.2.RELEASE API）</a>
                .properties(vendorProperties)
                .build();
    }

    /**
     * 配置 实体管理器 (EntityManager)
     * @param builder
     * @return
     */
    @Bean(name = "primaryEntityManager")
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactory(builder).getObject().createEntityManager();
    }

}
