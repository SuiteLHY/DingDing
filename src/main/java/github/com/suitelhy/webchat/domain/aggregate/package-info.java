/**
 * 聚合 (Aggregate): 一组具有内聚关系的领域对象的集合. 主要作用是提供"关联模型".
 * 聚合 (Aggregate) 的特征: 每个聚合都有一个根 (root) 和边界 (boundary).
 *
 * 聚合 (Aggregate) 其实换一个角度就能很好地理解: 在基于数据库驱动的设计模式中,
 *-> 多个存储实体数据的表之间可能会有包括"多对多"在内的各种耦合. 例如一个用户的
 *-> 信息对应多条日志记录, 最简便的设计是在日志记录表中添加一个"用户表Id"字段,
 *-> 用来存放关联的用户信息的 Id 字段数据; 这个"用户表Id"字段的数据必须能够确定
 *-> 唯一的用户信息 (也就是 Identify). 通过这个实体数据中唯一标识的数据形成的两
 *-> 个实体间的依赖关系, 就是聚合的基础.
 *-> 而现在, 在以上论述的基础上, 将两个实体表之间的关联提取到一个关联数据表中进
 *-> 行维护. 是不是很熟悉?
 * 聚合 (Aggregate) 的角色是建立在上述的"关联数据表"的基础上的, 所以就不难理解:
 *-> 为什么聚合需要有聚合根 (Aggregate Root), 为什么聚合根 (Aggregate Root) 必须
 *-> 是实体 (Entity), 为什么聚合 (Aggregate) 的标识就是聚合根 (Aggregate Root)
 *-> (实际上是一个 Entity) 的标识 (Identify).
 * 简而言之, 聚合 (Aggregate) 的角色定位就是管理一个基于聚合根 (Aggregate Root)
 *-> 的关联数据表, 且查询方向是一个聚合根 (Aggregate Root) 对应多个边界 (boundary).
 *
 * 现实问题中普遍存在大量"多对多"关联, 这些普遍的关联会使开发和维护变得很复杂.
 *-> 尽可能地对关系进行约束是非常重要的, 双向关联意味着只有将其中的那两个对象放在
 *-> 一起考虑才能理解它们.
 * 至少有3种方法可以使得关联更易于控制:
 *-> (1) 规定一个遍历方向;
 *->     限定"多对多"关联方向可以有效地将其转化为"一对多"关联.
 *-> (2) 添加一个限定符;
 *-> (3) 消除不必要的关联.
 *
 */
package github.com.suitelhy.webchat.domain.aggregate;