package github.com.suitelhy.webchat.domain.entity.policy;

import java.util.UUID;

/**
 * 数据库策略
 */
public final class DBPolicy {

	/**
	 * 生成UUID
	 * @description 去除 '\t'(首尾) 和 <code>-</code>
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().trim().replace("-", "").toLowerCase();
	}
	
}
