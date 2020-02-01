/**
 * event: 封装并管理 - 对多个子事务, 进行基于业务连续性的、针对聚合 (Aggregate) 的、支持 ACID 的复杂操作.
 *
 * event 层本质上管理的是多个聚合 (Aggregate).
 * event 操作的基本单元: 聚合 (Aggregate).
 *
 */
package github.com.suitelhy.dingding.sso.domain.event;