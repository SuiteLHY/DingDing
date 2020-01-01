/**
 * 应用层 (Service): 主要负责调用业务方法, 发起对领域层 (Domain) 以及基础设施层 (Infrastructure) 的操作.
 *
 * 应用层 (Service) 仅负责任务调度, 任务实现由领域层的 领域服务(Domain Service) 和基础设施层 (Infrastructure)
 *-> 相关模块负责.
 *-> 所以这一层很适合执行任务调度计划、任务监控和日志输出.
 *
 * 与基础设施层的关联: 基础设施层负责应用层的通用业务实现, 而应用层则只需要调用基础设施层提供的接口.
 *-> 通俗地说, 应用层知道何时该做什么, 而不需要关心怎么做.
 *
 * 服务 (Service) 都应该是无状态的.
 *
 */
package github.com.suitelhy.webchat.application;