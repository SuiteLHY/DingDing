/**
 * 任务层 (Task): 主要负责调用(应该基于DTO; 可根据项目情况进行精简)业务方法, 发起对领域层 (Domain)
 * -> 以及基础设施层 (Infrastructure) 的操作.
 * <p>
 * 任务层 (Task) 仅负责任务调度, 任务实现由领域层的 领域服务(Domain Service) 和基础设施层 (Infrastructure)
 * -> 的相关模块负责.
 * -> 所以这一层很适合执行任务调度计划、任务监控和日志输出.
 * <p>
 * 与基础设施层的关联: 基础设施层负责任务层的通用业务实现, 而任务层则只需要调用基础设施层提供的接口.
 * -> 通俗地说, 任务层知道何时该做什么, 而不需要关心怎么做.
 * <p>
 * 任务 (Task) 及其调度的服务 (Service) 都应该是无状态的.
 */
package github.com.suitelhy.dingding.userservice.application;