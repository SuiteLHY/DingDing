package github.com.suitelhy.dingding.security.service.provider.infrastructure.config.springdata;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

/**
 * 数据源配置
 *
 * @Description 多数据源, 主从分离.
 *
 * @Reference
 * · {@link <a href="https://github.com/ityouknow/spring-boot-examples/blob/master/spring-boot-jpa/spring-boot-multi-Jpa/src/main/java/com/neo/config/DataSourceConfig.java">spring-boot-examples/DataSourceConfig.java at master · ityouknow/spring-boot-examples</a>}
 * · Spring boot - version 2.2.2 -> 踩坑(...):
 * {@link <a href="https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.2-Release-Notes">Spring Boot 2.2发行说明·spring-projects / spring-boot Wiki</a>}
 * {@linkplain <a href="https://github.com/spring-projects/spring-data-examples">spring-projects/spring-data-examples: Spring Data Example Projects</a> Spring 官方 Demo (2019年12月的, 最后只能靠自己)}
 * {@linkplain <a href="https://github.com/spring-projects/spring-data-examples/tree/master/jpa">spring-projects/spring-data-examples/jpa/</a> Spring Data JPA}
 * {@linkplain <a href="https://xinxiamu.github.io/2019/04/20/scloud-properties-refresh/">Spring Cloud外部环境配置刷新 | 小木</a> 解决本地服务接入外部服务注册中心后, 遇到的配置中心无法刷新本地服务的问题}
 */
@Configuration
public class DataSourceConfig {

    /**
     * Spring Data JPA 配置
     */
    @Autowired
    private JpaProperties jpaProperties;

    /**
     * Hibernate 配置
     */
    @Autowired
    private HibernateProperties hibernateProperties;

    /**
     * 主数据源
     *
     * @Description 返回类型写死为 {@linkplain HikariDataSource 默认的数据库连接池 HikariDataSource} 是为了解决本地服务接入外部服务注册中心后, 遇到的配置中心无法刷新本地服务的问题.
     * 解决方案参阅 {@link <a href="https://xinxiamu.github.io/2019/04/20/scloud-properties-refresh/">Spring Cloud外部环境配置刷新 | 小木</a>}.
     *
     * @return {@linkplain HikariDataSource 默认的数据库连接池 HikariDataSource}
     *
     * @see <a href="https://xinxiamu.github.io/2019/04/20/scloud-properties-refresh/">Spring Cloud外部环境配置刷新 | 小木</a>
     */
    @Bean(name = "primaryDataSource")
    @Primary
    @ConfigurationProperties("dingding.datasource.test")
    public /*DataSource*/HikariDataSource firstDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * 辅助数据源
     *
     * @Description 返回类型写死为 {@linkplain HikariDataSource 默认的数据库连接池 HikariDataSource} 是为了解决本地服务接入外部服务注册中心后, 遇到的配置中心无法刷新本地服务的问题.
     * 解决方案参阅 {@link <a href="https://xinxiamu.github.io/2019/04/20/scloud-properties-refresh/">Spring Cloud外部环境配置刷新 | 小木</a>}.
     *
     * @return {@linkplain HikariDataSource 默认的数据库连接池 HikariDataSource}
     *
     * @see <a href="https://xinxiamu.github.io/2019/04/20/scloud-properties-refresh/">Spring Cloud外部环境配置刷新 | 小木</a>
     */
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties("dingding.datasource.user")
    public /*DataSource*/HikariDataSource secondDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * 数据源供应商属性
     *
     * @return
     */
    @Bean(name = "vendorProperties")
    public Map<String, Object> getVendorProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties()
                , new HibernateSettings());
    }

}
