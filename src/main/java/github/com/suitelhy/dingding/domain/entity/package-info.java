/**
 * 领域对象 - 实体 (Entity): 具有唯一标识的对象.
 *
 * 实体 (Entity) 具有2个特征:
 * 1) 系统设计上, 具有唯一的标识 (Identify);
 * 2) 业务设计上, 具有一定的生命周期, 且在生命周期内具有连续性的业务状态.
 *
 * 设计一个领域对象是否为 Entity (= 是否具有 Identify) 取决于设计 Identify
 *-> 对于软件业务的实现是否有必要. 而不是依赖于其所抽象的现实事物的特征.
 *
 */
package github.com.suitelhy.dingding.domain.entity;