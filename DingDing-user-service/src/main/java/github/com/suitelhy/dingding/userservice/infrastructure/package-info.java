/**
 * 基础设施层 (Infrastructure): 为表现层 (Application)、应用层 (Service)、模型层 (Domain)
 * -> 分别提供通用的技术能力支持 (具体实现).
 * -> 包括且不限于: 为模型层提供持久化机制, 为应用层传递消息, 为表现层提供页面渲染组件.
 * <p>
 * 基础设施层主要作用是简化各个分层的职责, 使其职责更专一.
 * -> 另外, 基础设施层还能够通过架构来支持各个层次之间的交互模式.
 */
package github.com.suitelhy.dingding.userservice.infrastructure;