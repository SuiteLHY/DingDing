package github.com.suitelhy.dingding.infrastructure.config.springdata;

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
@EnableJpaRepositories(basePackages= {"github.com.suitelhy.dingding.domain.repository"}
        , entityManagerFactoryRef="primaryEntityManagerFactory"
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
//        Object v = ((Map.Entry) ((Map) vendorProperties).entrySet().toArray()[0]).getValue();
//        System.err.println("===== ((Map.Entry) ((Map) vendorProperties).entrySet().toArray()[0]).getValue() -> " + v);
        return builder
                .dataSource(primaryDataSource)
                .packages("github.com.suitelhy.dingding.domain.entity") // 设置实体类所在位置
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
