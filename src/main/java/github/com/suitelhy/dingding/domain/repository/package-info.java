/**
 * 领域资源层 (Repository): 为实体 (Entity) 和聚合 (Aggregate) 提供对象的持久化方法.
 *
 */
/**
 * ORM 框架选用 Spring Data JPA
 * @Docs <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#preface">
 *->     Spring Data JPA-参考文档</a>
 * @Description Spring开发团队 在持久化领域给出了 Based Hibernate 的答案, 证明事实上 Hibernate
 *-> 才是JPA 落地和市场份额的王者. 如果不是出于框架二次开发的目的 (比如 <company>阿里巴巴</company>
 *-> 有构建自生态圈的需要), 请在基于 Spring Framework 搭建项目的情况下尽量使用 Spring Data JPA,
 *-> 而不是 Mybatis (<jar>mybatis-spring-${version}</jar> 等资源是 Mybatis 团队提供的, 该项目前
 *-> 期搭建中踩了不少 <jar>Mybatis-Spring</jar> 的坑, 最后在遇到 类型处理器(TypeHandle) 的 issue
 *-> 时, 由于 Mybatis 对应版本无法提供有效的项目设计实现支持, 决定弃用 Mybatis, 最后转向 Spring
 *-> 官方推荐的 Spring Data JPA).
 *
 */
@org.springframework.lang.NonNullApi
package github.com.suitelhy.dingding.domain.repository;