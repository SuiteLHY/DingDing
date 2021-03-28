//package github.com.suitelhy.dingding.core.domain.service.security;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.*;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleResourceRepository;
//import github.com.suitelhy.dingding.core.domain.service.LogService;
//import github.com.suitelhy.dingding.core.domain.service.UserAccountOperationInfoService;
//import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityRoleResourceServiceImpl;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
//import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
//import org.springframework.data.domain.Page;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.validation.constraints.NotNull;
//import java.util.List;
//
///**
// * （安全认证）角色 ←→ 资源
// *
// * @Description [（安全认证）角色 ←→ 资源]关联关系 - 业务接口.
// *
// * @see SecurityRoleResource
// * @see SecurityRoleResourceRepository
// * @see SecurityRoleResourceServiceImpl
// * @see UserAccountOperationInfoService
// */
//@Transactional(isolation = Isolation.SERIALIZABLE
//        , propagation = Propagation.REQUIRED
//        , readOnly = false
//        , rollbackFor = Exception.class
//        , timeout = 15)
//public interface SecurityRoleResourceService
//        extends EntityService {
//
//    /**
//     * 判断[（安全认证）资源]是否存在 (关联的) [（安全认证）角色]
//     *
//     * @param resourceCode  {@linkplain SecurityRoleResource.Validator#resourceCode(String) 资源编码}
//     *
//     * @return {@linkplain Boolean#TYPE 判断结果}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    boolean existRoleByResourceCode(@NotNull String resourceCode);
//
//    /**
//     * 判断[（安全认证）角色]是否存在 (关联的) [（安全认证）资源]
//     *
//     * @param roleCode  {@linkplain SecurityRoleResource.Validator#roleCode(String) 角色编码}
//     *
//     * @return {@linkplain Boolean#TYPE 判断结果}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    boolean existResourceByRoleCode(@NotNull String roleCode);
//
//    /**
//     * 查询所有
//     *
//     * @param pageIndex 分页索引, 从0开始
//     * @param pageSize  分页 - 每页容量
//     *
//     * @return {@link Page}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull Page<SecurityRoleResource> selectAll(int pageIndex, int pageSize);
//
//    /**
//     * 查询总页数
//     *
//     * @Description 查询数据列表 - 分页 - 总页数
//     *
//     * @param pageSize 分页 - 每页容量
//     *
//     * @return 分页 - 总页数
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull Long selectCount(int pageSize);
//
//    /**
//     * 查询
//     *
//     * @param resourceCode  {@linkplain SecurityRoleResource.Validator#resourceCode(String) 资源编码}
//     *
//     * @return {@link SecurityRoleResource}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull List<SecurityRoleResource> selectByResourceCode(@NotNull String resourceCode);
//
//    /**
//     * 查询
//     *
//     * @param roleCode  {@linkplain SecurityRoleResource.Validator#roleCode(String) 角色编码}
//     *
//     * @return {@link SecurityRoleResource}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull List<SecurityRoleResource> selectByRoleCode(@NotNull String roleCode);
//
//    //===== 添加操作业务 =====//
//
//    /**
//     * 新增一个[（安全认证）角色 ←→ 资源]关联关系
//     *
//     * @param roleResource  {@linkplain SecurityRoleResource [（安全认证）角色 ←→ 资源]}
//     * @param operator      {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
//     */
//    boolean insert(@NotNull SecurityRoleResource roleResource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    //===== 删除操作业务 =====//
//
//    /**
//     * 删除指定的关联关系
//     *
//     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
//     * · 完整业务流程的一部分
//     *
//     * @param roleResource  {@linkplain SecurityRoleResource [（安全认证）角色 ←→ 资源]}
//     * @param operator      {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功 / 指定的[（安全认证）角色 ←→ 资源]已被删除}
//     */
//    boolean delete(@NotNull SecurityRoleResource roleResource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//}
