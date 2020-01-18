/**
 * 领域对象 - 值对象 (Value Object): 没有唯一标识 (Identify) 的对象.
 *
 * 值对象 (VO) 可以提供一个实例 (Entity) 的相关信息, 它在概念上应该是一个整体.
 *-> 作为概念模型, 具有内部属性不可变性 (可以更换整个概念模型). (特殊情况: 何时允许可变性)
 *
 * 值对象 (VO) 的设计原则: 当我们只关心一个模型元素的属性时, 应该把它归类为 Value Object.
 *-> 我们应该使这个模型元素能够表示出其属性的意义, 并为它提供相关的功能. Value Object 应该
 *-> 是不可变的, 不要为它分配任何标识, 也不要把它设计成像 Entity 那么复杂.
 *
 * 值对象 (VO) 的使用原则: 包括复制、共享、修改指向 VO 的引用.
 *
 * 很多对象没有概念上的标识, 它们描述了事物的某种特征. 这类对象在 Model-Driven Design 中
 *-> 被称为值对象 (Value Object). 其主要作用是: 描述事物 (Entity) 的特征.
 *
 * 值对象 (VO) 经常作为参数在对象之前传递消息. 它们常常是一个临时对象, 在一次操作中被创建,
 *-> 之后被丢弃.
 *
 */
/**
 * VO 对象目前全都选用 枚举类型(enum) 对象实现.
 *
 * 关于枚举 (enum):
 * · 枚举继承了 java.lang.Enum 类.
 *-> 详见 API Docs:
 *-> <a href="http://www.enseignement.polytechnique.fr/informatique/Java/1.8/java/lang/Enum.html">
 *->     Enum (Java Platform SE 8 )</a>
 * · 枚举 (enum) 与 Mybatis 的类型处理器:
 *-> (1) 设计: 需要配置 Mybatis 的类型处理器 -> 针对 VO 进行合适的处理.
 *-> (2) 官方文档:
 *-> <a href="https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers">
 *->     mybatis – MyBatis 3 | 配置#类型处理器（typeHandlers）</a>
 *
 */
package github.com.suitelhy.webchat.infrastructure.domain.vo;