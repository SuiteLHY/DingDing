package github.com.suitelhy.dingding.app.infrastructure.domain.policy;

import github.com.suitelhy.dingding.app.infrastructure.util.RegexUtil;
import org.springframework.lang.Nullable;

/*import java.util.UUID;*/

/**
 * 数据库策略
 *
 * @Description 每个厂商的数据库对应一个实例.
 */
public /*final class*/enum DBPolicy {
	MYSQL;

//	/**
//	 * 生成UUID
//	 * @Description 去除 '\t'(首尾) 和 <code>-</code>
//	 * @Update 已废弃; 禁止使用自然键 (由应用服务而不是数据库服务控制的主键).
//	 * @return
//	 */
//	public String uuid() {
//		return UUID.randomUUID().toString().trim().replace("-", "").toLowerCase();
//	}

	/**
	 * 校验符合 <code>uuid()</code> 生成策略的 UUID 字符串
	 *
	 * @Reference <a href="https://stackoverflow.com/questions/37615731/java-regex-for-uuid">
	 *->     java regex for UUID - Stack Overflow</a>
	 * @param uuid
	 * @return
	 */
	public boolean validateUuid(@Nullable String uuid) {
		return null != uuid
				&& RegexUtil.getPattern("([a-f0-9]{32})").matcher(uuid).matches();
	}
	
}
