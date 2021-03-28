//package github.com.suitelhy.dingding.core.domain.service.security;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.*;
//import github.com.suitelhy.dingding.core.domain.service.security.impl.SecurityResourceServiceImpl;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
//import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
//import org.springframework.data.domain.Page;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.validation.constraints.NotNull;
//import java.util.*;
//
///**
// * (安全) 资源
// *
// * @Description (安全) 资源 - 业务接口.
// *
// * @see SecurityResourceServiceImpl
// */
//@Transactional(isolation = Isolation.SERIALIZABLE
//        , propagation = Propagation.REQUIRED
//        , readOnly = false
//        , rollbackFor = Exception.class
//        , timeout = 15)
//public interface SecurityResourceService
//        extends EntityService {
//
//    /**
//     * 查询所有
//     *
//     * @param pageIndex 分页索引, 从 0 开始.
//     * @param pageSize
//     *
//     * @return {@link Page}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull Page<SecurityResource> selectAll(int pageIndex, int pageSize)
//            throws IllegalArgumentException;
//
////    /**
////     * 查询所有 [URL - ROLE] 权限对应关系
////     *
////     * @return {@link ContainArrayHashMap}
////     */
////    @NotNull ContainArrayHashMap<String, List<Object>> selectAllUrlRoleMap()
////            throws IllegalArgumentException;
//
////    /**
////     * 查询所有 [URL - ROLE] 权限对应关系
////     *
////     * @param clientId [资源服务器 ID]  {@link SecurityResourceUrl#getClientId()}
////     *
////     * @return {@link ContainArrayHashMap}
////     */
////    @NotNull ContainArrayHashMap<String, List<Object>> selectUrlRoleMap(@NotNull String clientId)
////            throws IllegalArgumentException;
//
//    /**
//     * 查询总页数
//     *
//     * @Description 查询数据列表 - 分页 - 总页数.
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
//    @NotNull Long selectCount(int pageSize)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询指定的资源
//     *
//     * @param code  {@linkplain SecurityResource#getCode() 资源编码}
//     *
//     * @return {@linkplain SecurityResource 指定的资源}
//     */
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    @NotNull SecurityResource selectResourceByCode(@NotNull String code)
//            throws IllegalArgumentException;
//
//    /**
//     * 新增一个资源
//     *
//     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
//     */
//    boolean insert(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 更新指定的资源
//     *
//     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功}
//     *
//     * @throws IllegalArgumentException
//     * @throws BusinessAtomicException
//     */
//    boolean update(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 更新指定的资源
//     *
//     * @param old_resource      {@linkplain SecurityResource [（安全认证）资源]} <- 原始版本业务全量数据
//     * @param new_resource_data [（安全认证）资源] <- 需要更新的数据
//     * · 数据结构:
//     * {
//     *  “resource_icon”: [图标],
//     *  “resource_link“: [资源链接],
//     *  “resource_name“: [资源名称],
//     *  “resource_parentCode“: [父节点 <- 资源编码],
//     *  “resource_sort“: [序号],
//     *  “resource_type“: [资源类型]
//     * }
//     * @param operator          {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功}
//     */
//    boolean update(@NotNull SecurityResource old_resource, @NotNull Map<String, Object> new_resource_data, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//    /**
//     * 删除指定的资源
//     *
//     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
//     * · 完整业务流程的一部分.
//     *
//     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功}
//     */
//    boolean delete(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException;
//
//}
