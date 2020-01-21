package github.com.suitelhy.dingding.infrastructure.config.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 数据源配置
 *
 * @Description 多数据源, 主从分离.
 * @Reference <a href="https://github.com/ityouknow/spring-boot-examples/blob/master/spring-boot-jpa/spring-boot-multi-Jpa/src/main/java/com/neo/config/DataSourceConfig.java">
 *->     spring-boot-examples/DataSourceConfig.java at master · ityouknow/spring-boot-examples</a>
 *
 */
/**
 * Spring boot - version 2.2.2 -> 踩坑(...):
 *-> <a href="https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.2-Release-Notes">
 *->     Spring Boot 2.2发行说明·spring-projects / spring-boot Wiki</a>
 * Spring 官方 Docs 链接的官方 Demo: <a href="https://github.com/spring-projects/spring-data-examples">
 *->     spring-projects/spring-data-examples: Spring Data Example Projects</a>, 2019年12月的, 最后只能靠自己
 * Spring Data JPA:
 *-> <a href="https://github.com/spring-projects/spring-data-examples/tree/master/jpa">
 *->     spring-projects/spring-data-examples/jpa/</a>
 *
 */
@Configuration
public class DataSourceConfig {

    // Spring Data JPA 配置
    @Autowired
    private JpaProperties jpaProperties;

    // Hibernate 配置
    @Autowired
    private HibernateProperties hibernateProperties;

    /**
     * 主数据源
     * @return
     */
    @Bean(name = "primaryDataSource")
    @Primary
    @ConfigurationProperties("dingding.datasource.test")
    public DataSource firstDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 辅助数据源
     * @return
     */
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties("dingding.datasource.user")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 数据源供应商属性
     * @return
     */
    @Bean(name = "vendorProperties")
    public Map<String, Object> getVendorProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties()
                , new HibernateSettings());
    }

}
