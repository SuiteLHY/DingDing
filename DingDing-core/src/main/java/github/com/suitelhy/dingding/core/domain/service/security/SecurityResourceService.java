package github.com.suitelhy.dingding.core.domain.service.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

/**
 * (安全) 资源
 *
 * @Description (安全) 资源 - 业务接口.
 */
public interface SecurityResourceService {

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从 0 开始.
     * @param pageSize
     * @return
     */
    Page<SecurityResource> selectAll(int pageIndex, int pageSize);

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数.
     *
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     */
    Long selectCount(int pageSize);

    /**
     * 查询指定的资源
     *
     * @param code
     * @return
     */
    SecurityResource selectResourceByCode(@NotNull String code);

    /**
     * 新增一个资源
     *
     * @param resource
     * @return 操作是否成功
     */
    boolean insert(@NotNull SecurityResource resource);

    /**
     * 更新指定的资源
     *
     * @param resource
     * @return 操作是否成功
     */
    boolean update(@NotNull SecurityResource resource);

    /**
     * 删除指定的资源
     *
     * @param resource
     * @return 操作是否成功
     */
    boolean delete(@NotNull SecurityResource resource);

    /**
     * 删除指定的资源
     *
     * @Description 删除成功后校验持久化数据;
     *->    主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param resource
     * @return 操作是否成功
     */
    boolean deleteAndValidate(@NotNull SecurityResource resource);

}
