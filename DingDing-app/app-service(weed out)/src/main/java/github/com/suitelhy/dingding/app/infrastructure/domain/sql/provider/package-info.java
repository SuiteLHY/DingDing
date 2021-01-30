/**
 * 领域层 (Domain) - 相对严谨的 SQL 提供.
 * <p>
 * 注意: 该层禁止直接拼接 SQL 字符串 (项目中任何地方都不能出现这类操作).
 * <p>
 * 备注: 更复杂的 SQL (分布式架构中禁止使用) 应该使用 xml 文件配置, 虽然开发的风险也更高 (拼接SQL).
 */
package github.com.suitelhy.dingding.app.infrastructure.domain.sql.provider;