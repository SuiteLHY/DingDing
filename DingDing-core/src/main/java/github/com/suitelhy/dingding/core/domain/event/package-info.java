/**
 * 领域服务层 -> 复杂业务集合 (Event)
 *
 * @Description 比 {@link github.com.suitelhy.dingding.core.domain.service} 的业务复杂度高一个单位.
 * @Design · 领域服务层 (Domain Service): 对事物对象 (Entity、VO 和其他领域内定义的对象) 的操作.
 * · event: 封装并管理 - 对多个子事务, 进行基于业务连续性的、针对聚合 (Aggregate) 的、支持 ACID 的复杂操作.
 * · event 层本质上管理的是多个[聚合 (Aggregate) 或实体 (Entity) （以聚合为主）]及其[业务基础单元 (Service)]集合.
 * · event 层操作的基本单元: 聚合 (Aggregate) 或实体 (Entity).
 * · event 层操作的主要对象: 业务基础单元 (Service).
 * @see github.com.suitelhy.dingding.core.domain.service
 */
package github.com.suitelhy.dingding.core.domain.event;