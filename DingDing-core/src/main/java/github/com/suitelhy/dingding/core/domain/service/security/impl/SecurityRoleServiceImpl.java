package github.com.suitelhy.dingding.core.domain.service.security.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleRepository;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleResourceRepository;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 角色
 *
 * @Description (安全) 角色 - 业务实现.
 *
 * @see github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService
 */
@Service("securityRoleService")
@Order(Ordered.LOWEST_PRECEDENCE)
@Transactional(isolation = Isolation.READ_COMMITTED
        , propagation = Propagation.REQUIRED
        , readOnly = true
        , rollbackFor = Exception.class
        , timeout = 15)
public class SecurityRoleServiceImpl
        implements SecurityRoleService {

    @Autowired
    ObjectMapper toJSONString;

    @Autowired
    private SecurityRoleRepository repository;

    @Autowired
    private SecurityRoleResourceRepository roleResourceRepository;

    /**
     * 判断存在
     *
     * @param code      角色编码
     * @return {@link java.lang.Boolean}
     */
    public Boolean existsByCode(@NotNull String code) {
        if (!SecurityRole.Validator.ROLE.code(code)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 角色编码"));
        }

        return repository.existsByCode(code);
    }

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @return  {@link org.springframework.data.domain.Page)
     */
    @Override
    public Page<SecurityRole> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            //-- 非法输入: <param>dataIndex</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>dataIndex</param>"));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageSize</param>"));
        }

        Sort.TypedSort<SecurityRole> typedSort = Sort.sort(SecurityRole.class);
        Sort sort = typedSort.by(SecurityRole::getCode).ascending();
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
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageSize</param>"));
        }

        long dataNumber = repository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询指定的角色
     *
     * @param code      角色编码    {@link SecurityRole}
     * @return {@link SecurityRole}
     */
    @Nullable
    @Override
    public SecurityRole selectRoleByCode(@NotNull String code) {
        if (!SecurityRole.Validator.ROLE.code(code)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 角色编码"));
        }

        Optional<SecurityRole> result = repository.findByCode(code);
        return result.orElse(null);
    }

    /**
     * 查询 (关联的) 资源
     *
     * @param code      角色编码    {@link SecurityRole}
     * @return          资源的数据   {@link SecurityResource}
     * @see SecurityResource
     */
    @Nullable
    @Override
    public List<Map<String, Object>> selectResourceByCode(@NotNull String code) {
        if (null == code || !SecurityRole.Validator.ROLE.code(code)) {
            return null;
        }

        return repository.selectResourceByCode(code);
    }

    /**
     * 新增一个角色
     *
     * @param role
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean insert(@NotNull SecurityRole role) {
        if (null == role || !role.isEntityLegal()) {
            //-- 非法输入: 非法角色
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法角色"));
        }

        if (repository.existsByCode(role.id())) {
            //--- 已存在相同数据 (根据 EntityID) 的情况
            return !repository.findByCode(role.id()).get().isEmpty();
        }

        return !repository.saveAndFlush(role).isEmpty();
    }

    /**
     * 新增 角色 - 资源 关联
     *
     * @Problem
     * · 多次单元测试后发现 并发一致性问题 - 读脏数据 的问题 (主要使用成熟方案解决问题, 故没有分配时间进行更多不必要地步进式问题跟踪).
     *->    {@link <a href="https://github.com/CyC2018/CS-Notes/blob/master/notes/%E6%95%B0%E6%8D%AE%E5%BA%93%E7%B3%BB%E7%BB%9F%E5%8E%9F%E7%90%86.md#%E8%AF%BB%E8%84%8F%E6%95%B0%E6%8D%AE">
     *->            CS-Notes/数据库系统原理.md at master · CyC2018/CS-Notes</a>}
     *
     * @Solution
     * · 并发一致性问题解决方案: {@link <a href="https://juejin.im/post/5c1852526fb9a04a0c2e5db6">Spring Boot+SQL/JPA实战悲观锁和乐观锁 - 掘金</a>}
     *
     * @param roles         角色, 必须全部合法且已持久化. {@link SecurityRole}
     * @param resources     资源, 必须全部合法且已持久化. {@link SecurityResource}
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean insertResource(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources) {
        if ((null == roles || roles.isEmpty())
                || (null == resources || resources.isEmpty())) {
            return false;
        }
        for (SecurityRole each : roles) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }
        for (SecurityResource each : resources) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }
        /*System.err.println("========== insertResource(Set<SecurityRole>, Set<SecurityResource>) ==========");
        System.err.println("角色参数:" + roles);
        System.err.println("资源参数:" + resources);*/

        boolean result = false;

        for (SecurityRole each : roles) {
            if (!roleResourceRepository.existsAllByRoleCode(each.getCode())) {
                //--- 角色不存在绑定的资源的情况
                /*System.err.println("角色" + each + "不存在绑定的资源集合");*/
                for (SecurityResource eachResource : resources) {
                    /*System.err.println("== 开始绑定, 资源" + eachResource + "... ==");*/
                    if (roleResourceRepository/*.saveAndFlush*/.save(SecurityRoleResource.Factory.ROLE_RESOURCE.create(each.getCode(), eachResource.getCode())).isEmpty()) {
                        //-- 操作失败: 新增 角色 - 资源 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 新增 角色 - 资源 关联"));
                    }
                    /*System.err.println("== 绑定成功: 角色" + each + " - 资源" + eachResource + " ==");*/
                }
            } else {
                //--- 角色存在绑定的资源的情况
                final List<SecurityRoleResource> existedRoleResources = roleResourceRepository.findAllByRoleCode(each.getCode());

                /*System.err.println("角色" + each + "存在绑定的资源集合: " + existedRoleResources);*/
                for (SecurityResource eachResource : resources) {
                    boolean exist = false;
                    for (SecurityRoleResource existedRoleResource : existedRoleResources) {
                        if (existedRoleResource.equals(each, eachResource)) {
                            /*System.err.println("确认角色" + each + "已绑定当前资源: " + eachResource
                                    + " <- " + existedRoleResource);*/
                            exist = true;
                            break;
                        }
                    }
                    if (exist) continue;

                    /*System.err.println("== 开始绑定, 资源" + eachResource + "... ==");*/
                    if (roleResourceRepository/*.saveAndFlush*/.save(SecurityRoleResource.Factory.ROLE_RESOURCE.create(each.getCode(), eachResource.getCode())).isEmpty()) {
                        //-- 操作失败: 新增 资源 - 角色 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 新增 角色 - 资源 关联"));
                    }
                    /*System.err.println("== 绑定成功: 角色" + each + " - 资源" + eachResource + " ==");*/
                }
            }

            result = true;
        }

        return result;
    }

    /**
     * 更新指定的角色
     *
     * @param role
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean update(@NotNull SecurityRole role) {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: 非法角色
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法角色"));
        }

        return !repository.saveAndFlush(role).isEmpty();
    }

    /**
     * 删除指定的角色
     *
     * @Description 删除成功后校验持久化数据;
     *->    主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param role
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean delete(@NotNull SecurityRole role) {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: 非法角色
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法角色"));
        }

        repository.deleteById(role.getId());
        return !repository.existsById(role.getId());
    }

    /**
     * 删除 角色 - 资源 关联
     *
     * @param roles     角色, 必须全部合法且已持久化.    {@link SecurityRole}
     * @param resources 资源, 必须全部合法且已持久化.    {@link SecurityResource}
     * @return 操作是否成功
     */
    @Override
    public boolean deleteResource(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources) {
        if ((null == roles || roles.isEmpty())
                || (null == resources || resources.isEmpty())) {
            return false;
        }
        for (SecurityRole each : roles) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }
        for (SecurityResource each : resources) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }

        boolean result = false;

        for (SecurityRole each : roles) {
            if (!roleResourceRepository.existsAllByRoleCode(each.getCode())) {
                //--- 角色不存在绑定的资源的情况
            } else {
                //--- 角色存在绑定的资源的情况
                final List<SecurityRoleResource> existedRoleResources = roleResourceRepository.findAllByRoleCode(each.getCode());

                for (SecurityResource eachResource : resources) {
                    SecurityRoleResource existedRoleResource = null;
                    for (SecurityRoleResource roleResource : existedRoleResources) {
                        if (roleResource.equals(each, eachResource)) {
                            existedRoleResource = roleResource;
                            break;
                        }
                    }
                    if (null == existedRoleResource) continue;

                    if (roleResourceRepository.removeByRoleCodeAndResourceCode(each.getCode(), eachResource.getCode()) <= 0) {
                        //-- 操作失败: 删除 资源 - 角色 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 删除 角色 - 资源 关联"));
                    }
                }
            }

            result = true;
        }

        return result;
    }

}
