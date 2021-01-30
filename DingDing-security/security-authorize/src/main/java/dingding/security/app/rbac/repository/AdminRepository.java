/**
 *
 */
package dingding.security.app.rbac.repository;

import org.springframework.stereotype.Repository;

import dingding.security.app.rbac.domain.Admin;

/**
 * @author zhailiang
 *
 */
@Repository
public interface AdminRepository extends DingDingRepository<Admin> {

    Admin findByUsername(String username);

}
