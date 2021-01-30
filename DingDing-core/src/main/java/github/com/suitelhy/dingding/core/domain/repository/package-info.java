/**
 * 领域资源层 (Repository): 为实体 (Entity) 和聚合 (Aggregate) 提供对象的持久化方法.
 * <p>
 * <p>
 * ORM 框架选用 Spring Data JPA
 *
 * @Docs <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#preface">
 * ->     Spring Data JPA-参考文档</a>
 * @Description Spring开发团队 在持久化领域给出了 Based Hibernate 的答案, 证明事实上 Hibernate
 * -> 才是JPA 落地和市场份额的王者. 如果不是出于框架二次开发的目的 (比如 <company>阿里巴巴</company>
 * -> 有构建自生态圈的需要), 请在基于 Spring Framework 搭建项目的情况下尽量使用 Spring Data JPA,
 * -> 而不是 Mybatis (<jar>mybatis-spring-${version}</jar> 等资源是 Mybatis 团队提供的, 该项目前
 * -> 期搭建中踩了不少 <jar>Mybatis-Spring</jar> 的坑, 最后在遇到 类型处理器(TypeHandle) 的 issue
 * -> 时, 由于 Mybatis 对应版本无法提供有效的项目设计实现支持, 决定弃用 Mybatis, 最后转向 Spring
 * -> 官方推荐的 Spring Data JPA).
 * <p>
 * =============================================================================================
 * <p>
 * ORM 框架选用 Spring Data JPA
 * @Docs <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#preface">
 * ->     Spring Data JPA-参考文档</a>
 * @Description Spring开发团队 在持久化领域给出了 Based Hibernate 的答案, 证明事实上 Hibernate
 * -> 才是JPA 落地和市场份额的王者. 如果不是出于框架二次开发的目的 (比如 <company>阿里巴巴</company>
 * -> 有构建自生态圈的需要), 请在基于 Spring Framework 搭建项目的情况下尽量使用 Spring Data JPA,
 * -> 而不是 Mybatis (<jar>mybatis-spring-${version}</jar> 等资源是 Mybatis 团队提供的, 该项目前
 * -> 期搭建中踩了不少 <jar>Mybatis-Spring</jar> 的坑, 最后在遇到 类型处理器(TypeHandle) 的 issue
 * -> 时, 由于 Mybatis 对应版本无法提供有效的项目设计实现支持, 决定弃用 Mybatis, 最后转向 Spring
 * -> 官方推荐的 Spring Data JPA).
 * <p>
 * =============================================================================================
 * <p>
 * 关于数据库事务的管理
 * @Description Spring Data JPA 的注解式事务声明方式.
 * @Reference -> {@link <a href="https://www.cnblogs.com/sxdcgaq8080/p/8985662.html">【spring data jpa】使用spring data jpa时，关于service层一个方法中进行【删除】和【插入】两种操作在同一个事务内处理 - Angel挤一挤 - 博客园</a>}
 * -> {@link <a href="https://zhuanlan.zhihu.com/p/71517302">面试官：说说Spring中的事务传播行为 - 知乎</a>}
 * -> {@link <a href="https://juejin.im/post/5e455eb76fb9a07cad3b92cf#heading-2">CRUD更要知道的Spring事务传播机制 - 掘金</a>}
 * -> {@link <a href="https://juejin.im/post/5e9bc8ad6fb9a03c7f3ffcac">面试官：你知道哪些事务失效的场景？ - 掘金</a>}
 * -> {@link <a href="https://my.oschina.net/lsl1991/blog/670030">Spring 事务隔离级别   Isolation.SERIALIZABLE)  实验总结 - lyqbnmasd的个人页面 - OSCHINA</a>}
 * -> {@link <a href="https://blog.csdn.net/starlh35/article/details/76445267?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase">
 * ->    数据库并发事务存在的问题（脏读、不可重复读、幻读等）_starlh35的博客-CSDN博客_事务脏读</a>}
 * -> {@link <a href="https://juejin.im/post/5c1852526fb9a04a0c2e5db6">Spring Boot+SQL/JPA实战悲观锁和乐观锁 - 掘金</a>}
 * -> {@link <a href="https://blog.csdn.net/Terry_Long/article/details/54291455?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase">
 * ->    使用 @Lock 注解实现Spring JAP锁_Terry_Long的博客-CSDN博客_@lock</a>}
 * -> {@link <a href="https://blog.csdn.net/zzg19950824/article/details/85468318?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.nonecase">
 * ->    JPA 加锁机制 及 @Version版本控制</a>}
 * -> {@link <a href="https://stackoverflow.com/questions/16159396/how-to-enable-lockmodetype-pessimistic-write-when-looking-up-entities-with-sprin">
 * ->    java - How to enable LockModeType.PESSIMISTIC_WRITE when looking up entities with Spring Data JPA? - Stack Overflow</a>}
 * -> {@link <a href="https://github.com/CyC2018/CS-Notes/blob/master/notes/%E6%95%B0%E6%8D%AE%E5%BA%93%E7%B3%BB%E7%BB%9F%E5%8E%9F%E7%90%86.md#%E8%AF%BB%E8%84%8F%E6%95%B0%E6%8D%AE">
 * ->    CS-Notes/数据库系统原理.md at master · CyC2018/CS-Notes</a>}
 */
@org.springframework.lang.NonNullApi
package github.com.suitelhy.dingding.core.domain.repository;