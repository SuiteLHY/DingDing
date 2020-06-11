package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityUserRepository;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityUserService;
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
 * (安全) 用户
 *
 * @Description (安全) 用户 - 业务实现.
 */
@Service("securityUserService")
@Order(Ordered.LOWEST_PRECEDENCE)
public class SecurityUserServiceImpl
        implements SecurityUserService {

    @Autowired
    private SecurityUserRepository repository;

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize
     * @return
     */
    @Override
    public Page<SecurityUser> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            throw new RuntimeException("非法输入: <param>dataIndex</param>");
        }
        if (pageSize < 1) {
            throw new RuntimeException("非法输入: <param>pageSize</param>");
        }
        Sort.TypedSort<SecurityUser> typedSort = Sort.sort(SecurityUser.class);
        Sort sort = typedSort.by(SecurityUser::getUserId).ascending()
                .and(typedSort.by(SecurityUser::getStatus).ascending());
        Pageable page = PageRequest.of(pageIndex, pageSize, sort);
        return repository.findAll(page);
    }

    /**
     * 查询总页数
     *
     * @Description 查询数据列表 - 分页 - 总页数
     *
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
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
     * 查询指定的用户
     *
     * @param userId
     * @return
     */
    @Nullable
    @Override
    public SecurityUser selectUserByUserId(@NotNull String userId) {
        if (!SecurityUser.Validator.USER.userId(userId)) {
            throw new RuntimeException("非法输入: 用户ID");
        }
        Optional<SecurityUser> result = repository.findById(userId);
        return result.orElse(null);
    }

    /**
     * 查询指定的用户
     *
     * @param username
     * @return
     */
    @Override
    public SecurityUser selectUserByUsername(@NotNull String username) {
        if (!SecurityUser.Validator.USER.username(username)) {
            throw new RuntimeException("非法输入: 用户名称");
        }
        Optional<SecurityUser> result = repository.findByUsername(username);
        return result.orElse(null);
    }

    /**
     * 新增一个用户
     *
     * @param user
     * @return 操作是否成功
     */
    @Override
    public boolean insert(@NotNull SecurityUser user) {
        if (null == user || !user.isEntityLegal()) {
            throw new RuntimeException("非法用户");
        }
        return !repository.saveAndFlush(user).isEmpty();
    }

    /**
     * 更新指定的用户
     *
     * @param user
     * @return 操作是否成功
     */
    @Override
    public boolean update(@NotNull SecurityUser user) {
        if (null == user || user.isEmpty()) {
            throw new RuntimeException("非法用户");
        }
        return !repository.saveAndFlush(user).isEmpty();
    }

    /**
     * 删除指定的用户
     *
     * @param user
     * @return 操作是否成功
     */
    @Override
    public boolean delete(@NotNull SecurityUser user) {
        if (null == user || user.isEmpty()) {
            throw new RuntimeException("非法用户");
        }
        repository.deleteById(user.id());
        return true;
    }

    /**
     * 删除指定的用户
     *
     * @Description 删除成功后校验持久化数据;
     * ->    主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param user
     * @return 操作是否成功
     */
    @Override
    public boolean deleteAndValidate(@NotNull SecurityUser user) {
        if (null == user || user.isEmpty()) {
            throw new RuntimeException("非法用户");
        }
        repository.deleteById(user.id());
        return !repository.existsById(user.id());
    }

}
