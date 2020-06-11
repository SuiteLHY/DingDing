package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityResourceRepository;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * (安全) 资源
 *
 * @Description (安全) 资源 - 业务实现.
 */
@Service("securityResourceService")
@Order(Ordered.LOWEST_PRECEDENCE)
public class SecurityResourceServiceImpl
        implements SecurityResourceService {

    @Autowired
    private SecurityResourceRepository repository;

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从 0 开始.
     * @param pageSize
     * @return
     */
    @Override
    public Page<SecurityResource> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            throw new RuntimeException("非法输入: <param>dataIndex</param>");
        }
        if (pageSize < 1) {
            throw new RuntimeException("非法输入: <param>pageSize</param>");
        }
        Sort.TypedSort<SecurityResource> typedSort = Sort.sort(SecurityResource.class);
        Sort sort = typedSort.by(SecurityResource::getSort).descending()
                .and(typedSort.by(SecurityResource::getCode).ascending());
        Pageable page = PageRequest.of(pageIndex, pageSize, sort);
        return repository.findAll(page);
    }

    /**
     * 查询总页数
     *
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     * @Description 查询数据列表 - 分页 - 总页数.
     */
    @Override
    public Long selectCount(int pageSize) {
        if (pageSize < 1) {
            throw new RuntimeException("非法输入: <param>pageSize</param>");
        }
        long dataNumber = repository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询指定的资源
     *
     * @param code
     * @return
     */
    @Nullable
    @Override
    public SecurityResource selectResourceByCode(@NotNull String code) {
        if (!SecurityResource.Validator.RESOURCE.code(code)) {
            throw new RuntimeException("非法输入: 角色编码");
        }
        Optional<SecurityResource> result = repository.findByCode(code);
        return result.orElse(null);
    }

    /**
     * 新增一个资源
     *
     * @param resource
     * @return 操作是否成功
     */
    @Override
    public boolean insert(@NotNull SecurityResource resource) {
        if (null == resource || !resource.isEntityLegal()) {
            throw new RuntimeException("非法角色");
        }
        return !repository.saveAndFlush(resource).isEmpty();
    }

    /**
     * 更新指定的资源
     *
     * @param resource
     * @return 操作是否成功
     */
    @Override
    public boolean update(@NotNull SecurityResource resource) {
        if (null == resource || resource.isEmpty()) {
            throw new RuntimeException("非法角色");
        }
        return !repository.saveAndFlush(resource).isEmpty();
    }

    /**
     * 删除指定的资源
     *
     * @param resource
     * @return 操作是否成功
     */
    @Override
    public boolean delete(@NotNull SecurityResource resource) {
        if (null == resource || resource.isEmpty()) {
            throw new RuntimeException("非法角色");
        }
        repository.deleteById(resource.id());
        return true;
    }

    /**
     * 删除指定的资源
     *
     * @param resource
     * @return 操作是否成功
     * @Description 删除成功后校验持久化数据;
     * ->    主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     */
    @Override
    public boolean deleteAndValidate(@NotNull SecurityResource resource) {
        if (null == resource || resource.isEmpty()) {
            throw new RuntimeException("非法角色");
        }
        repository.deleteById(resource.id());
        return !repository.existsById(resource.id());
    }

}
