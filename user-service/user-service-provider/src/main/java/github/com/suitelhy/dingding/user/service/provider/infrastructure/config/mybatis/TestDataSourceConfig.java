package github.com.suitelhy.dingding.user.service.provider.infrastructure.config.mybatis;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.SpringBootConfiguration;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//
///**
// * 测试用的数据源 & Mybatis框架 配置
// *
// * 参照资料: <a href="http://www.ityouknow.com/springboot/2016/11/25/spring-boot-multi-mybatis.html">
// *->      Spring Boot(七)：Mybatis 多数据源最简解决方案 - 纯洁的微笑博客 </a>
// * 参考资料: <a href="http://mybatis.org/spring/zh/getting-started.html">
// *->     mybatis-spring – MyBatis-Spring | 入门</a>,
// *-> <a href="http://mybatis.org/spring/zh/mappers.html">mybatis-spring – MyBatis-Spring | 注入映射器</a>
// *-> , <a href="https://www.jianshu.com/p/dba49fc5a741">mybatis源码-@Mapper @MapperScan配置及注入原理 - 简书</a>
// *-> (Mapper接口映射器的自动注入), <a href="https://mybatis.org/mybatis-3/zh/configuration.html#environments">
// *->     mybatis – MyBatis 3 | 配置#环境配置（environments）</a>
// *
// * @Description 基于 Mybatis 框架的数据源的配置和注入.
// *-> 逐层地配置并注入: 首先创建 DataSource, 然后创建 SqlSessionFactory 再创建事务, 最后包装到 SqlSessionTemplate 中.
// *
// */
///*@Configuration*/
//@SpringBootConfiguration
//// 使用自定义数据源, 需要重新注入 mapper 接口的映射器及其关联的 SqlSessionTemplate.
//// @MapperScan: 扫描 领域层(Domain) 的 领域资源层(Repository)接口 并注册.
////-> Use this annotation <code>@MapperScan</code> to register MyBatis mapper interfaces when using Java Config.
////-> It performs when same work as MapperScannerConfigurer via MapperScannerRegistrar.
//@MapperScan(basePackages = {"github.com.suitelhy.dingding.app.domain.repository"}
//        , sqlSessionTemplateRef = "testSqlSessionTemplate")
//public class TestDataSourceConfig {
//
//    /**
//     * 生成数据源 (DataSource)
//     * @return
//     */
//    @Bean(name = "testDataSource")
//    // 注入配置文件属性
//    @ConfigurationProperties(prefix = "dingding.datasource.test")
//    // @Primary: 优先考虑被注解对象的注入
//    @Primary
//    public DataSource testDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    /**
//     * 配置 Mybatis 的 SQL会话工厂 & Mybatis 全局配置
//     *
//     * @param dataSource - 数据源 (DataSource)
//     * @return
//     * @throws Exception
//     */
//    @Bean(name = "testSqlSessionFactory")
//    @Primary
//    public SqlSessionFactory testSqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource)
//            throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        //===== Mybatis 配置 =====//
//        // 配置数据源
//        factoryBean.setDataSource(dataSource);
//        // Mybatis - 自定义配置实现注入
//        factoryBean.setTypeHandlersPackage("github.com.suitelhy.dingding.infrastructure.config.mybatis.typehandler");
//        /*//--- 使用 MyBatis 映射器的 XML 配置文件
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath:mybatis/mapper/test/*.xml"));*/
//        //==========//
//        return factoryBean.getObject();
//    }
//
//    /**
//     * 配置事务
//     * @param dataSource - 数据源 (DataSource)
//     * @return
//     */
//    @Bean(name = "testTransactionManager")
//    @Primary
//    public DataSourceTransactionManager
//    testTransactionManager(@Qualifier("testDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    /**
//     * 配置 Mybatis 的 SQL会话模板
//     * @param sqlSessionFactory - Mybatis的 SQL会话工厂
//     * @return
//     */
//    @Bean(name = "testSqlSessionTemplate")
//    @Primary
//    public SqlSessionTemplate
//    testSqlSessionTemplate(@Qualifier("testSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//}
