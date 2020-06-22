package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityResourceRepository;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityResourceUrlRepository;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleResourceRepository;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 资源
 *
 * @Description (安全) 资源 - 业务实现.
 *
 * @see github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService
 */
@Service("securityResourceService")
@Order(Ordered.LOWEST_PRECEDENCE)
public class SecurityResourceServiceImpl
        implements SecurityResourceService {

    @Autowired
    private SecurityResourceRepository repository;

    @Autowired
    private SecurityResourceUrlRepository resourceUrlRepository;

    @Autowired
    private SecurityRoleResourceRepository roleResourceRepository;

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
            //-- 非法输入: <param>pageIndex</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageIndex</param>"));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: <param>pageSize</param>"));
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
     * 查询指定的资源
     *
     * @param code
     * @return
     */
    @Nullable
    @Override
    public SecurityResource selectResourceByCode(@NotNull String code) {
        if (!SecurityResource.Validator.RESOURCE.code(code)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 角色编码"));
        }

        Optional<SecurityResource> result = repository.findByCode(code);
        return result.orElse(null);
    }

    /**
     * 查询 (关联的) 角色
     *
     * @param code            {@link SecurityResource#getCode()}
     * @return 数据集          {@link SecurityRole}
     * @see SecurityRole
     */
    @Nullable
    @Override
    public List<Map<String, Object>> selectRoleByCode(@NotNull String code) {
        if (null == code
                || !SecurityResource.Validator.RESOURCE.code(code)) {
            return null;
        }

        return repository.selectRoleByCode(code);
    }

    /**
     * 查询 (关联的) URL
     *
     * @param code          {@link SecurityResource#getCode()}
     * @return URL集合       {@link SecurityResourceUrl#getUrlPath()}
     * @see SecurityResourceUrl
     */
    @Override
    public List<Map<String, Object>> selectUrlByCode(@NotNull String code) {
        final List<Map<String, Object>> result = new ArrayList<>(1);

        final List<SecurityResourceUrl> dataSet = resourceUrlRepository.findAllByCode(code);
        if (null != dataSet) {
            for (SecurityResourceUrl each : dataSet) {
                if (null == each || each.isEmpty()) {
                    break;
                }

                Map<String, Object> eachMap = new HashMap<>(1);
                eachMap.put("url", each.getUrlPath());

                result.add(eachMap);
            }
        }
        return result;
    }

    /**
     * 新增一个资源
     *
     * @param resource
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean insert(@NotNull SecurityResource resource) {
        if (null == resource || !resource.isEntityLegal()) {
            //-- 非法输入: 非法角色
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法角色"));
        }

        if (repository.existsByCode(resource.id())) {
            /*//--- 需要更新的情况
            resource = SecurityResource.Factory.RESOURCE.update(
                    repository.findByCode(resource.getCode()).get()
                    , resource
            );
            if (null == resource) return false;*/

            //--- 已存在相同数据 (根据 EntityID) 的情况
            return !repository.findByCode(resource.id()).get().isEmpty();
        }

        return !repository.save(resource).isEmpty();
    }

    /**
     * 新增 角色 关联
     *
     * @param resources 资源, 必须全部合法且已持久化. {@link SecurityResource}
     * @param roles     角色, 必须全部合法且已持久化. {@link SecurityRole}
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean insertRole(@NotNull Set<SecurityResource> resources, @NotNull Set<SecurityRole> roles) {
        if ((null == resources || resources.isEmpty())
                || (null == roles || roles.isEmpty())) {
            return false;
        }
        for (SecurityResource each : resources) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }
        for (SecurityRole each : roles) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }

        boolean result = false;

        for (SecurityResource each : resources) {
            if (!roleResourceRepository.existsAllByResourceCode(each.getCode())) {
                //--- 资源不存在绑定的角色的情况
                for (SecurityRole eachRole : roles) {
                    if (roleResourceRepository.save(SecurityRoleResource.Factory.ROLE_RESOURCE.create(eachRole.getCode(), each.getCode())).isEmpty()) {
                        //-- 操作失败: 新增 角色 - 资源 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 新增 角色 - 资源 关联"));
                    }
                }
            } else {
                //--- 资源存在绑定的角色的情况
                final List<SecurityRoleResource> existedRoleResources = roleResourceRepository.findAllByResourceCode(each.getCode());

                for (SecurityRole eachRole : roles) {
                    boolean exist = false;
                    for (SecurityRoleResource roleResource : existedRoleResources) {
                        if (eachRole.getCode().equals(roleResource.getRoleCode())) {
                            exist = true;
                            break;
                        }
                    }
                    if (exist) continue;

                    if (roleResourceRepository.save(SecurityRoleResource.Factory.ROLE_RESOURCE.create(eachRole.getCode(), each.getCode())).isEmpty()) {
                        //-- 操作失败: 新增 角色 - 资源 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 新增 角色 - 资源 关联"));
                    }
                }
            }

            result = true;
        }

        return result;
    }

    /**
     * 新增 URL 关联
     *
     * @Description 将若干资源 (必须合法且已持久化) 与若干 URL (若未持久化则新增) 进行关联.
     *
     * @param resources
     * @param urls
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean insertUrl(@NotNull Set<SecurityResource> resources, @NotNull Set<String> urls) {
        if ((null == resources || resources.isEmpty())
                || (null == urls || urls.isEmpty())) {
            return false;
        }
        for (SecurityResource each : resources) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }
        for (String each : urls) {
            if (null == each
                    || !SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(each)) {
                return false;
            }
        }

        boolean result = false;

        for (SecurityResource each : resources) {
            if (!resourceUrlRepository.existsAllByCode(each.getCode())) {
                //--- 当前资源不存在绑定的 URL 的情况
                for (String url : urls) {
                    if (resourceUrlRepository.save(SecurityResourceUrl.Factory.RESOURCE_URL.create(each.getCode(), url)).isEmpty()) {
                        //-- 操作失败: 新增 资源 - URL 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 新增 资源 - URL 关联"));
                    }
                }
            } else {
                List<SecurityResourceUrl> existedResourceUrls = resourceUrlRepository.findAllByCode(each.getCode());

                //--- 当前资源存在绑定的 URL 的情况
                for (String url : urls) {
                    boolean exist = false;
                    for (SecurityResourceUrl eachResourceUrl : existedResourceUrls) {
                        if (eachResourceUrl.getUrlPath().equals(url)) {
                            exist = true;
                            break;
                        }
                    }
                    if (exist) continue;

                    if (resourceUrlRepository.save(SecurityResourceUrl.Factory.RESOURCE_URL.create(each.getCode(), url)).isEmpty()) {
                        //-- 操作失败: 新增 资源 - URL 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 新增 资源 - URL 关联"));
                    }
                }
            }

            result = true;
        }

        return result;
    }

    /**
     * 更新指定的资源
     *
     * @param resource
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean update(@NotNull SecurityResource resource) {
        if (null == resource || resource.isEmpty()) {
            //-- 非法输入: 非法角色
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法角色"));
        }

        return !repository.saveAndFlush(resource).isEmpty();
    }

    /**
     * 删除指定的资源
     *
     * @Description 删除成功后校验持久化数据;
     *->    主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     *
     * @param resource
     * @return 操作是否成功
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , timeout = 15)
    public boolean delete(@NotNull SecurityResource resource) {
        if (null == resource || resource.isEmpty()) {
            //-- 非法输入: 非法角色
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 非法角色"));
        }

        repository.deleteById(resource.getId());
        return !repository.existsById(resource.getId());
    }

    /**
     * 删除 角色 关联
     *
     * @param resources 资源, 必须全部合法且已持久化. {@link SecurityResource}
     * @param roles     角色, 必须全部合法且已持久化. {@link SecurityRole}
     * @return 操作是否成功
     */
    @Override
    public boolean deleteRole(@NotNull Set<SecurityResource> resources, @NotNull Set<SecurityRole> roles) {
        if ((null == resources || resources.isEmpty())
                || (null == roles || roles.isEmpty())) {
            return false;
        }
        for (SecurityResource each : resources) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }
        for (SecurityRole each : roles) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }

        boolean result = false;

        for (SecurityResource each : resources) {
            if (!roleResourceRepository.existsAllByResourceCode(each.getCode())) {
                //--- 资源不存在绑定的角色的情况
            } else {
                //--- 资源存在绑定的角色的情况
                final List<SecurityRoleResource> existedRoleResources = roleResourceRepository.findAllByResourceCode(each.getCode());

                for (SecurityRole eachRole : roles) {
                    SecurityRoleResource existedRoleResource = null;
                    for (SecurityRoleResource roleResource : existedRoleResources) {
                        if (eachRole.getCode().equals(roleResource.getRoleCode())) {
                            existedRoleResource = roleResource;
                            break;
                        }
                    }
                    if (null == existedRoleResource) continue;

                    if (roleResourceRepository.removeByRoleCodeAndResourceCode(eachRole.getCode(), each.getCode()) <= 0) {
                        //-- 操作失败: 删除 角色 - 资源 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 删除 角色 - 资源 关联"));
                    }
                }
            }

            result = true;
        }

        return result;
    }

    /**
     * 删除 URL 关联
     *
     * @param resources
     * @param urls
     * @return 操作是否成功
     * @Description 将若干资源 (必须合法且已持久化) 与若干 URL (若未持久化则新增) 的关联进行删除.
     */
    @Override
    public boolean deleteUrl(@NotNull Set<SecurityResource> resources, @NotNull Set<String> urls) {
        if ((null == resources || resources.isEmpty())
                || (null == urls || urls.isEmpty())) {
            return false;
        }
        for (SecurityResource each : resources) {
            if (null == each || each.isEmpty()) {
                return false;
            }
        }
        for (String each : urls) {
            if (null == each
                    || !SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(each)) {
                return false;
            }
        }

        boolean result = false;

        for (SecurityResource each : resources) {
            if (!resourceUrlRepository.existsAllByCode(each.getCode())) {
                //--- 当前资源不存在绑定的 URL 的情况
            } else {
                List<SecurityResourceUrl> existedResourceUrls = resourceUrlRepository.findAllByCode(each.getCode());

                //--- 当前资源存在绑定的 URL 的情况
                for (String url : urls) {
                    SecurityResourceUrl existedResourceUrl = null;
                    for (SecurityResourceUrl eachResourceUrl : existedResourceUrls) {
                        if (eachResourceUrl.getUrlPath().equals(url)) {
                            existedResourceUrl = eachResourceUrl;
                            break;
                        }
                    }
                    if (null == existedResourceUrl) continue;

                    if (resourceUrlRepository.removeByCodeAndUrlPath(each.getCode(), url) <= 0) {
                        //-- 操作失败: 删除 资源 - URL 关联
                        throw new RuntimeException(this.getClass().getSimpleName()
                                .concat(" -> 操作失败: 删除 资源 - URL 关联"));
                    }
                }
            }

            result = true;
        }

        return result;
    }

}
