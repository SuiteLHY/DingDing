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
 * @Description
 * 1. 关于枚举 (enum):
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
/**
 * VO <- 确定使用枚举来实现
 *
 * @Design
 * 1. 枚举之间的关联管理:
 * 1.1 一对多: (1) 枚举 嵌套 接口(-> 接口 嵌套 枚举); (2) 接口(-> 接口 嵌套 枚举).
 * 1.2 一对一: (1) 枚举 嵌套 枚举.
 * 2. 所有 VO 及其管理接口, 有且仅->拓展 <interface>VoModel</interface>.
 *
 * @Problem
 * 1. JDK 8 提供的 enum 类型, 目前在任何情况下不允许被(enum 类型)继承. 所以很多通用设计且可模块化复用的代码只能在实现类中重复实现.(orz)
 *-> 如果允许声明为 <code>abstract</code> 的 enum 可以被 enum 继承, 那就完美了! (梦太美QAQ)
 *-> {@Reference
 *->      <a href="https://stackoverflow.com/questions/22074497/java-enum-tostring-method">Java Enum toString() method - Stack Overflow</a>
 *->      , 【☆】<a href="https://stackoverflow.com/questions/1414755/can-enums-be-subclassed-to-add-new-elements">
 *->            java - Can enums be subclassed to add new elements? - Stack Overflow</a>
 *->      , <a href="https://stackoverflow.com/questions/18883646/java-enum-methods">Java Enum Methods - Stack Overflow</a>
 *->      , <a href="https://stackoverflow.com/questions/9969690/whats-the-advantage-of-a-java-enum-versus-a-class-with-public-static-final-fiel">
 *->            What's the advantage of a Java enum versus a class with public static final fields? - Stack Overflow</a>
 *-> }
 *
 * @Tips
 * 1. 管理枚举的->[接口/枚举] 应该直接继承 <interface>VoModel</interface>, 而不是 <interface>VoModel</interface>的拓展接口 !
 *
 */
package github.com.suitelhy.dingding.app.infrastructure.domain.vo;