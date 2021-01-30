/**
 *
 */
package dingding.security.app.rbac.repository;

import org.springframework.stereotype.Repository;

import dingding.security.app.rbac.domain.Resource;

/**
 * @author zhailiang
 *
 */
@Repository
public interface ResourceRepository
        extends DingDingRepository<Resource> {

    Resource findByName(String name);

}
