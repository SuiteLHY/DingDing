//package github.com.suitelhy.webchat.infrastructure.config.datasource;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//
///**
// * 主数据源配置
// *
// * 参照资料: <a href="http://www.ityouknow.com/springboot/2016/11/25/spring-boot-multi-mybatis.html">
// *->      Spring Boot(七)：Mybatis 多数据源最简解决方案 - 纯洁的微笑博客 </a>
// * 参考资料: <a href="http://mybatis.org/spring/zh/getting-started.html">
// *->     mybatis-spring – MyBatis-Spring | 入门</a>,
// *-> <a href="http://mybatis.org/spring/zh/mappers.html">mybatis-spring – MyBatis-Spring | 注入映射器</a>
// *-> , <a href="https://www.jianshu.com/p/dba49fc5a741">mybatis源码-@Mapper @MapperScan配置及注入原理 - 简书</a>
// *-> (Mapper接口映射器的自动注入)
// *
// * @Description 基于 Mybatis 框架的数据源的配置和注入. 逐层地 配置并注入.
// */
//@Configuration
//// @MapperScan 扫描领域层(Domain)的领域资源层(Repository), 且向其中注入指定的 SqlSessionTemplate.
//// TIPS: 不使用这个数据源的配置就屏蔽该注解 (有声明配置无使用绑定)
///*@MapperScan(basePackages = "github.com.suitelhy.webchat.domain.repository"
//        , sqlSessionTemplateRef = "userSqlSessionTemplate")*/
//public class UserDataSourceConfig {

//    /**
//     * 配置数据源 (DataSource)
//     * @return
//     */
//    @Bean(name = "userDataSource")
//    // 注入配置文件属性
//    @ConfigurationProperties(prefix = "webchat.datasource.user")
//    // @Primary: 优先考虑被注解的对象的注入
//    @Primary
//    public DataSource userDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    /**
//     * 配置 Mybatis 的 SQL会话工厂
//     * @param dataSource - 数据源 (DataSource)
//     * @return
//     * @throws Exception
//     */
//    @Bean(name = "userSqlSessionFactory")
//    @Primary
//    public SqlSessionFactory userSqlSessionFactory(@Qualifier("userDataSource") DataSource dataSource)
//            throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        /*//--- 使用 MyBatis 映射器的 XML 配置文件
//        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath:mybatis/mapper/user/*.xml"));*/
//        //--- 使用 MyBatis 注解来指定 SQL 语句
//        //-> , 此时通过注解..(估计)..会自动配置并注入 Mybatis 的 SqlSessionFactoryBean.
//        return factoryBean.getObject();
//    }
//
//    /**
//     * 配置 Mybatis 的 SQL会话模板
//     * @param sqlSessionFactory - Mybatis的 SQL会话工厂
//     * @return
//     */
//    @Bean(name = "userSqlSessionTemplate")
//    @Primary
//    public SqlSessionTemplate userSqlSessionTemplate(@Qualifier("userSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//    /**
//     * 配置 Mybatis 的 数据源事务管理
//     * @param dataSource - 数据源 (DataSource)
//     * @return
//     */
//    @Bean(name = "userTransactionManager")
//    @Primary
//    public DataSourceTransactionManager userTransactionManager(@Qualifier("userDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//}
