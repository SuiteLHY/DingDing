package dingding.security.app.rbac.repository.spec;

import dingding.security.app.rbac.domain.Admin;
import dingding.security.app.rbac.dto.AdminCondition;
import dingding.security.app.rbac.repository.support.DingDingSpecification;
import dingding.security.app.rbac.repository.support.QueryWraper;

/**
 * @author zhailiang
 *
 */
public class AdminSpec
		extends DingDingSpecification<Admin, AdminCondition> {

	public AdminSpec(AdminCondition condition) {
		super(condition);
	}

	@Override
	protected void addCondition(QueryWraper<Admin> queryWrapper) {
		addLikeCondition(queryWrapper, "username");
	}

}
