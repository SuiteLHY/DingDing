/**
 * 领域层 ("模型层"): 负责业务概念、业务状态信息、业务规则的管理和表达.
 *-> 领域层是业务软件的核心. (将领域层单独分离出来是实现 Model-Driven Design 的关键)
 *
 * 尽管保存业务状态的细节 (数据持久化) 是由基础设施层管理和实现的, 但是
 *-> 反映业务情况的状态是由领域层来管理和实现的.
 *
 * 领域层的构造块:
 * · 领域对象 - 实体 (Entity): 具有唯一标识的对象.
 * · 领域对象 - 值对象 (Value Object): 没有唯一标识的对象.
 * · 领域对象 - 聚合 (Aggregate): 一组具有内聚关系的、由 Entity 和或 Value Object 组成的集合.
 *->   聚合特征: 每个聚合都有一个根 (root) 和边界 (boundary).
 * · 领域服务 (Domain Service): 对领域对象 (Entity / VO / Aggregate) 的业务操作.
 *->   领域服务 (Domain Service) 是领域层内唯一对上层 (Application Service) 提供业务实现的模块.
 * · 对象 - 仓储 (Repository): 为实体 (Entity) 和聚合 (Aggregate) 提供对象的持久化方法.
 * · 对象 - 工厂 (Factory): 提供领域对象和聚合 (Aggregate) 的创建方法.
 *->   用来封装实体 (Entity)、值对象 (Value Object)、聚合 (Aggregate) 的复杂生产业务.
 *
 * DDD (领域驱动设计) (Domain-Driven Design)
 *-> 设计参考:
 *-> <a href="https://dev.to/colaru/creating-a-domain-model-rapidly-with-java-and-spring-boot-i85">
 *->    Creating a Domain Model rapidly with Java and Spring Boot</a>,
 *-> <a href="https://www.slideshare.net/Dennis_Traub/dotnetcologne2013-ddd">
 *->     Strategic Appplication Development with Domain-Driven Design (DDD)</a> (PS: 网页标题就是有错别字的...)
 *
 * 分层间的交互: ORM 中的 JPA 实现已移至基础设施层 (Infrastructure) 相应模块管理.
 *
 * 关于数据源交互业务中的 ORM 框架选择:
 *-> 选择参考: <a href="https://jverson.com/spring-boot-demo/jdbc-orm/orm.html">对 ORM 框架的简单介绍 · Spring Boot</a>
 * 由于这个项目的初始版本主要是为了练手和明确设计思路, 所以选择可控性更高且操作粒度更细的 Mybatis 框架.
 *-> 由于想要对领域聚合做直观且绑定实体的代码注入设计, 所以最后决定手写一套简单的 JPA 实现, 顺便复习一下 JPA 规范.
 *
 */
/**
 * 出于对该即时通讯 Web 项目的性能和可伸缩性考虑，ORM框架选择符合国内主流趋势的 Mybatis (一种不完全的 ORM 框架).
 */
package github.com.suitelhy.dingding.domain;