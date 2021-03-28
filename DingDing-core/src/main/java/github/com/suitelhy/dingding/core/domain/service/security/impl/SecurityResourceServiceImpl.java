//package github.com.suitelhy.dingding.core.domain.service.security.impl;
//
//import github.com.suitelhy.dingding.core.domain.entity.security.*;
//import github.com.suitelhy.dingding.core.domain.repository.security.SecurityResourceRepository;
//import github.com.suitelhy.dingding.core.domain.service.security.SecurityResourceService;
//import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
//import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.ObjectUtils;
//
//import javax.validation.constraints.NotNull;
//import java.util.*;
//
///**
// * (安全) 资源
// *
// * @Description (安全) 资源 - 业务实现.
// *
// * @see SecurityResourceService
// */
//@Order(Ordered.LOWEST_PRECEDENCE)
//@Service("securityResourceService")
//@Transactional(isolation = Isolation.SERIALIZABLE
//        , propagation = Propagation.REQUIRED
//        , readOnly = false
//        , rollbackFor = Exception.class
//        , timeout = 15)
//public class SecurityResourceServiceImpl
//        implements SecurityResourceService {
//
//    @Autowired
//    private SecurityResourceRepository repository;
//
////    @Autowired
////    private UserAccountOperationInfoService userAccountOperationInfoService;
//
////    @Autowired
////    private LogService logService;
//
//    /**
//     * 查询所有
//     *
//     * @param pageIndex 分页索引, 从 0 开始.
//     * @param pageSize
//     *
//     * @return {@link Page}
//     */
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    public Page<SecurityResource> selectAll(int pageIndex, int pageSize) {
//        if (pageIndex < 0) {
//            //-- 非法输入: <param>pageIndex</param>
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "pageIndex"
//                    , pageIndex
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (pageSize < 1) {
//            //-- 非法输入: <param>pageSize</param>
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "pageSize"
//                    , pageSize
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        Sort.TypedSort<SecurityResource> typedSort = Sort.sort(SecurityResource.class);
//        Sort sort = typedSort.by(SecurityResource::getSort).descending()
//                .and(typedSort.by(SecurityResource::getCode).ascending());
//        Pageable page = PageRequest.of(pageIndex, pageSize, sort);
//
//        return repository.findAll(page);
//    }
//
////    /**
////     * 查询所有 URL - ROLE 权限对应关系
////     *
////     * @return {@link java.util.List<java.util.Map<java.lang.String, java.lang.Object>>}
////     * @Description {URL - ROLE}, 一对多.
////     * [
////     * {[{"client_id" -> {@link java.lang.String}}, {"url_path" -> {@link java.lang.String}}] : ["role_code", {@link java.util.List<Object>}]}
////     * ]
////     */
////    @Override
////    public @NotNull ContainArrayHashMap<String, List<Object>> selectAllUrlRoleMap() {
////        /*final Map<String[], List<Object>> result = new LinkedHashMap<>(1);*/
////        final @NotNull ContainArrayHashMap<String, List<Object>> result = new ContainArrayHashMap<>(1);
////
////        final List<Map<String, Object>> urlRoleMapList = repository.selectAllUrlRoleMap();
////        if (null != urlRoleMapList) {
////            for (@NotNull Map<String, Object> each : urlRoleMapList) {
////                final String clientId = (String) each.get("client_id");
////                final String urlPath = (String) each.get("url_path");
////                final String eachHttpMethod = (String) each.get("url_method");
////                final Object eachRoleCode = each.get("role_code");
////
////                final @NotNull String[] urlInfo = new String[]{clientId, urlPath, eachHttpMethod};
////                final @NotNull List<Object> roles = (result.containsKey(urlInfo) && null != result.get(urlInfo))
////                        ? result.get(urlInfo)
////                        : new ArrayList<>(0);
////
////                roles.add(eachRoleCode);
////
////                /*urlRoleMapList.remove(i);
////
////                if (!urlRoleMapList.isEmpty()) {
////                    for (int j = i; j < urlRoleMapList.size(); j++) {
////                        final Map<String, Object> eachTemp = urlRoleMapList.get(j);
////
////                        if (urlPath.equals(eachTemp.get("url_path"))) {
////                            roles.add(eachTemp.get("role_code"));
////
////                            urlRoleMapList.remove(j--);
////                            --i;
////                        }
////                    }
////                }*/
////
////                result.put(urlInfo, roles);
////            }
////        }
////
////        return result;
////    }
//
////    /**
////     * 查询所有 URL - ROLE 权限对应关系
////     *
////     * @param clientId [资源服务器 ID]   {@link SecurityResourceUrl#getClientId()}
////     * @return {@link ContainArrayHashMap}
////     */
////    @Override
////    public @NotNull
////    ContainArrayHashMap<String, List<Object>> selectUrlRoleMap(@NotNull String clientId) {
////        if (!SecurityResourceUrl.Validator.RESOURCE_URL.clientId(clientId)) {
////            //-- 非法输入: [资源服务器 ID]
////            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                    , "[资源服务器 ID]"
////                    , clientId
////                    , this.getClass().getName()
////                    , Thread.currentThread().getStackTrace()[1].getMethodName()
////                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////        }
////
////        final @NotNull ContainArrayHashMap<String, List<Object>> result = new ContainArrayHashMap<>(1);
////
////        final @NotNull List<Map<String, Object>> urlRoleMapList = repository.selectUrlRoleMap(clientId);
////        if (null != urlRoleMapList) {
////            for (@NotNull Map<String, Object> each : urlRoleMapList) {
////                final String eachClientId = (String) each.get("client_id");
////                final String eachUrlPath = (String) each.get("url_path");
////                final String eachHttpMethod = (String) each.get("url_method");
////                final Object eachRoleCode = each.get("role_code");
////
////                final @NotNull String[] urlInfo = new String[]{eachClientId, eachUrlPath, eachHttpMethod};
////                final @NotNull List<Object> roles = (result.containsKey(urlInfo) && null != result.get(urlInfo))
////                        ? result.get(urlInfo)
////                        : new ArrayList<>(1);
////
////                roles.add(eachRoleCode);
////
////                result.put(urlInfo, roles);
////            }
////        }
////
////        return result;
////    }
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
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    public @NotNull Long selectCount(int pageSize) {
//        if (pageSize < 1) {
//            //-- 非法输入: <param>pageSize</param>
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "pageSize"
//                    , pageSize
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        long dataNumber = repository.count();
//        return (dataNumber % pageSize == 0)
//                ? (dataNumber / pageSize)
//                : (dataNumber / pageSize + 1);
//    }
//
//    /**
//     * 查询指定的资源
//     *
//     * @param code  {@linkplain SecurityResource#getCode() 资源编码}
//     *
//     * @return {@linkplain SecurityResource 指定的资源}
//     */
//    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED
//            , propagation = Propagation.SUPPORTS
//            , readOnly = true
//            , rollbackFor = Exception.class
//            , timeout = 10)
//    public @NotNull SecurityResource selectResourceByCode(@NotNull String code) {
//        if (! SecurityResource.Validator.RESOURCE.code(code)) {
//            //-- 非法输入: 资源编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "资源编码"
//                    , code
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return repository.findSecurityResourceByCode(code)
//                .orElseGet(SecurityResource.Factory.RESOURCE::createDefault);
//    }
//
//    /**
//     * 新增一个资源
//     *
//     * @param resource  {@linkplain SecurityResource [（安全认证）资源]}
//     * @param operator  {@linkplain SecurityUser 操作者}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
//     */
//    @Override
//    public boolean insert(@NotNull SecurityResource resource, @NotNull SecurityUser operator) {
//        if (null == resource || ! resource.isEntityLegal()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
//        /*if (null == operator_userAccountOperationInfo
//                || operator_userAccountOperationInfo.isEmpty()
//                || !operator_userAccountOperationInfo.equals(operator)) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }*/
//
//        if (repository.existsByCode(resource.id())) {
//            //--- 已存在相同数据 (根据 EntityID) 的情况
//            return ! repository.findSecurityResourceByCode(resource.id())
//                    .orElseGet(SecurityResource.Factory.RESOURCE::createDefault)
//                    .isEmpty();
//        }
//
//        return ! repository.save(resource).isEmpty();
//    }
//
////    /**
////     * 新增 角色 关联
////     *
////     * @param resources     资源, 必须全部合法且已持久化. {@link SecurityResource}
////     * @param roles         角色, 必须全部合法且已持久化. {@link SecurityRole}
////     * @param operator      操作者
////     *
////     * @return 操作是否成功
////     */
////    @Override
////    @Transactional(isolation = Isolation.SERIALIZABLE
////            , propagation = Propagation.REQUIRED
////            , timeout = 15)
////    public boolean insertRole(@NotNull Set<SecurityResource> resources, @NotNull Set<SecurityRole> roles, @NotNull SecurityUser operator) throws BusinessAtomicException {
////        if (null == resources || resources.isEmpty()) {
////            //-- 非法输入: [（安全认证）资源]
////            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                    , "[（安全认证）资源]"
////                    , resources
////                    , this.getClass().getName()
////                    , Thread.currentThread().getStackTrace()[1].getMethodName()
////                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////        } else {
////            for (SecurityResource each : resources) {
////                if (null == each || each.isEmpty()) {
////                    //-- 非法输入: [（安全认证）资源]
////                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                            , "[（安全认证）资源]"
////                            , each
////                            , this.getClass().getName()
////                            , Thread.currentThread().getStackTrace()[1].getMethodName()
////                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////                }
////            }
////        }
////        if (null == roles || roles.isEmpty()) {
////            //-- 非法输入: [（安全认证）角色]
////            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                    , "[（安全认证）角色]"
////                    , roles
////                    , this.getClass().getName()
////                    , Thread.currentThread().getStackTrace()[1].getMethodName()
////                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////        } else {
////            for (SecurityRole each : roles) {
////                if (null == each || each.isEmpty()) {
////                    //-- 非法输入: [（安全认证）角色]
////                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                            , "[（安全认证）角色]"
////                            , each
////                            , this.getClass().getName()
////                            , Thread.currentThread().getStackTrace()[1].getMethodName()
////                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////                }
////            }
////        }
////        if (null == operator || operator.isEmpty()) {
////            //-- 非法输入: 操作者
////            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                    , "操作者"
////                    , operator
////                    , this.getClass().getName()
////                    , Thread.currentThread().getStackTrace()[1].getMethodName()
////                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////        }
////        final UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
////        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
////            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
////            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                    , "操作者"
////                    , "无[有效的账户操作基础记录]"
////                    , operator
////                    , operator_userAccountOperationInfo
////                    , this.getClass().getName()
////                    , Thread.currentThread().getStackTrace()[1].getMethodName()
////                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////        }
////
////        boolean result = false;
////
////        for (SecurityResource each : resources) {
////            if (!securityRoleResourceService.existRoleByResourceCode(each.getCode())) {
////                //--- 资源不存在绑定的角色的情况
////                for (SecurityRole eachRole : roles) {
////                    final SecurityRoleResource newRoleResource = SecurityRoleResource.Factory.ROLE_RESOURCE.create(
////                            eachRole.getCode()
////                            , each.getCode()
////                    );
////                    if (!securityRoleResourceService.insert(newRoleResource, operator)) {
////                        //-- 操作失败: 新增[角色 - 资源]关联
////                        throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
////                                , newRoleResource
////                                , this.getClass().getName()
////                                , Thread.currentThread().getStackTrace()[1].getMethodName()
////                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////                    } else {
////                        //=== 操作日志记录 ===//
////                        final Log newLog_RoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
////                                , null
////                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
////                                , newRoleResource
////                                , operator
////                                , operator_userAccountOperationInfo);
////                        if (!logService.insert(newLog_RoleResource)) {
////                            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                                    , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
////                                    , "生成操作日志记录"
////                                    , newLog_RoleResource
////                                    , this.getClass().getName()
////                                    , Thread.currentThread().getStackTrace()[1].getMethodName()
////                                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////                        }
////                        //======//
////                    }
////                }
////            } else {
////                //--- 资源存在绑定的角色的情况
////                final List<SecurityRoleResource> existedRoleResources = securityRoleResourceService.selectByResourceCode(each.getCode());
////
////                for (SecurityRole eachRole : roles) {
////                    boolean exist = false;
////                    for (SecurityRoleResource roleResource : existedRoleResources) {
////                        if (eachRole.getCode().equals(roleResource.getRoleCode())) {
////                            exist = true;
////                            break;
////                        }
////                    }
////                    if (exist) continue;
////
////                    final SecurityRoleResource newRoleResource = SecurityRoleResource.Factory.ROLE_RESOURCE.create(
////                            eachRole.getCode()
////                            , each.getCode());
////                    if (!securityRoleResourceService.insert(newRoleResource, operator)) {
////                        //-- 操作失败: 新增 角色 - 资源 关联
////                        throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
////                                , newRoleResource
////                                , operator
////                                , this.getClass().getName()
////                                , Thread.currentThread().getStackTrace()[1].getMethodName()
////                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////                    } else {
////                        //=== 操作日志记录 ===//
////                        final Log newLog_RoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
////                                , null
////                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
////                                , newRoleResource
////                                , operator
////                                , operator_userAccountOperationInfo);
////                        if (!logService.insert(newLog_RoleResource)) {
////                            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                                    , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
////                                    , "生成操作日志记录"
////                                    , newLog_RoleResource
////                                    , this.getClass().getName()
////                                    , Thread.currentThread().getStackTrace()[1].getMethodName()
////                                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////                        }
////                        //======//
////                    }
////                }
////            }
////
////            result = true;
////        }
////
////        return result;
////    }
//
////    /**
////     * 新增 URL 关联
////     *
////     * @Description 将若干资源 (必须合法且已持久化) 与若干 URL (若未持久化则新增) 进行关联.
////     *
////     * @param resources
////     * @param urlInfoSet
////     * @param operator                          操作者
////     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
////     *
////     * @return 操作是否成功
////     */
////    @Override
////    @Transactional(isolation = Isolation.SERIALIZABLE
////            , propagation = Propagation.REQUIRED
////            , timeout = 15)
////    public boolean insertUrl(@NotNull Set<SecurityResource> resources
////            , @NotNull ContainArrayHashSet<String> urlInfoSet
////            , @NotNull SecurityUser operator
////            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
////            throws IllegalArgumentException, BusinessAtomicException
////    {
////        if (null == resources || resources.isEmpty()) {
////            //-- 非法输入: [（安全认证）资源]
////            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                    , "[（安全认证）资源]"
////                    , resources
////                    , this.getClass().getName()
////                    , Thread.currentThread().getStackTrace()[1].getMethodName()
////                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////        } else {
////            for (@NotNull SecurityResource each : resources) {
////                if (null == each || each.isEmpty()) {
////                    //-- 非法输入: [（安全认证）资源]
////                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                            , "[（安全认证）资源]"
////                            , each
////                            , this.getClass().getName()
////                            , Thread.currentThread().getStackTrace()[1].getMethodName()
////                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////                }
////            }
////        }
////        if (null == urlInfoSet || urlInfoSet.isEmpty()) {
////            //-- 非法输入: [URL 信息]
////            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                    , "[URL 信息]"
////                    , operator
////                    , this.getClass().getName()
////                    , Thread.currentThread().getStackTrace()[1].getMethodName()
////                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////        } else {
////            for (@NotNull String[] each : urlInfoSet) {
////                /*if (null == each
////                        || each.length != 3
////                        || !SecurityResourceUrl.Validator.RESOURCE_URL.clientId(each[0])
////                        || !SecurityResourceUrl.Validator.RESOURCE_URL.urlPath(each[1])
////                        || !SecurityResourceUrl.Validator.RESOURCE_URL.urlMethod(each[2])) {*/
////                if (! SecurityResourceUrl.Validator.RESOURCE_URL.urlInfo(each)) {
////                    //-- 非法输入: [URL 信息]
////                    throw new IllegalArgumentException(String.format("非法参数:<description>%s</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                            , "[URL 信息]"
////                            , Arrays.toString(each)
////                            , this.getClass().getName()
////                            , Thread.currentThread().getStackTrace()[1].getMethodName()
////                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////                }
////            }
////        }
////        if (null == operator || operator.isEmpty()) {
////            //-- 非法输入: 操作者
////            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                    , "操作者"
////                    , operator
////                    , this.getClass().getName()
////                    , Thread.currentThread().getStackTrace()[1].getMethodName()
////                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////        }
////        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
////        if (null == operator_userAccountOperationInfo
////                || operator_userAccountOperationInfo.isEmpty()
////                || ! operator_userAccountOperationInfo.equals(operator))
////        {
////            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
////            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                    , "操作者"
////                    , "无[有效的账户操作基础记录]"
////                    , operator_userAccountOperationInfo
////                    , this.getClass().getName()
////                    , Thread.currentThread().getStackTrace()[1].getMethodName()
////                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////        }
////
////        boolean result = false;
////
////        for (@NotNull SecurityResource each : resources) {
////            if (! resourceUrlService.existUrlByResourceCode(each.getCode())) {
////                //--- 当前资源不存在绑定的 URL 的情况
////                for (@NotNull String[] urlInfo : urlInfoSet) {
////                    @NotNull SecurityResourceUrl newResourceUrl = SecurityResourceUrl.Factory.RESOURCE_URL.create(each.getCode()
////                            , urlInfo[0]
////                            , urlInfo[1]
////                            , urlInfo[2]);
////                    if (! resourceUrlService.insert(newResourceUrl, operator, operator_userAccountOperationInfo)) {
////                        //-- 操作失败: 新增 资源 - URL 关联
////                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】&【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                                , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
////                                , "执行后数据异常"
////                                , newResourceUrl
////                                , operator
////                                , this.getClass().getName()
////                                , Thread.currentThread().getStackTrace()[1].getMethodName()
////                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////                    }
////                }
////            } else {
////                //--- 当前资源存在绑定的 URL 的情况
////                final @NotNull List<SecurityResourceUrl> existedResourceUrls = resourceUrlService.selectByResourceCode(each.getCode());
////
////                for (@NotNull String[] urlInfo : urlInfoSet) {
////                    boolean exist = false;
////                    for (@NotNull SecurityResourceUrl eachResourceUrl : existedResourceUrls) {
////                        if (eachResourceUrl.getClientId().equals(urlInfo[0])
////                                && eachResourceUrl.getUrlPath().equals(urlInfo[1]))
////                        {
////                            exist = true;
////                            break;
////                        }
////                    }
////                    if (exist) continue;
////
////                    final @NotNull SecurityResourceUrl newResourceUrl = SecurityResourceUrl.Factory.RESOURCE_URL.create(each.getCode()
////                            , urlInfo[0]
////                            , urlInfo[1]
////                            , urlInfo[2]);
////                    if (! resourceUrlService.insert(newResourceUrl, operator, operator_userAccountOperationInfo)) {
////                        //-- 操作失败: 新增 资源 - URL 关联
////                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
////                                , HandleType.LogVo.SECURITY__SECURITY_RESOURCE_URL__ADD.name
////                                , "执行后数据异常"
////                                , newResourceUrl
////                                , operator
////                                , this.getClass().getName()
////                                , Thread.currentThread().getStackTrace()[1].getMethodName()
////                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
////                    }
////                }
////            }
////
////            result = true;
////        }
////
////        return result;
////    }
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
//    @Override
//    public boolean update(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == resource || resource.isEmpty()) {
//            //-- 非法输入: 非法角色
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "非法角色"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
//        /*if (null == operator_userAccountOperationInfo
//                || operator_userAccountOperationInfo.isEmpty()
//                || !operator_userAccountOperationInfo.equals(operator)) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }*/
//
//        if (repository.saveAndFlush(resource).isEmpty()) {
//            //-- 操作失败: 更新指定的资源
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE.name
//                    , "执行后数据异常"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }/* else {
//            //=== 操作日志记录 ===//
//            final @NotNull Log newLog_Resource = Log.Factory.SecurityResource.LOG.create(null
//                    , null
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE
//                    , resource
//                    , operator
//                    , operator_userAccountOperationInfo);
//            if (! logService.insert(newLog_Resource)) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                        , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE.name
//                        , "生成操作日志记录"
//                        , newLog_Resource
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//            //======//
//        }*/
//
//        return true;
//    }
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
//    @Override
//    public boolean update(@NotNull SecurityResource old_resource, @NotNull Map<String, Object> new_resource_data, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == old_resource || !old_resource.isEntityLegal()) {
//            //-- 非法输入: 原始版本业务全量数据
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "原始版本业务全量数据"
//                    , old_resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == new_resource_data
//                || (! new_resource_data.containsKey("resource_icon") && ! new_resource_data.containsKey("resource_link") && ! new_resource_data.containsKey("resource_name") && ! new_resource_data.containsKey("resource_parentCode") && ! new_resource_data.containsKey("resource_sort") && ! new_resource_data.containsKey("resource_type"))) {
//            //-- 非法输入: 需要更新的数据
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (new_resource_data.containsKey("resource_icon")
//                && ! SecurityResource.Validator.RESOURCE.icon((String) new_resource_data.get("resource_icon"))) {
//            //-- 非法输入: 需要更新的数据 => 图标
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => 图标"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (new_resource_data.containsKey("resource_link")
//                && ! SecurityResource.Validator.RESOURCE.link((String) new_resource_data.get("resource_link"))) {
//            //-- 非法输入: 需要更新的数据 => 资源链接
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => 资源链接"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (new_resource_data.containsKey("resource_name")
//                && ! SecurityResource.Validator.RESOURCE.name((String) new_resource_data.get("resource_name"))) {
//            //-- 非法输入: 需要更新的数据 => 资源名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => 资源名称"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (new_resource_data.containsKey("resource_parentCode")
//                && ! SecurityResource.Validator.RESOURCE.parentCode((String) new_resource_data.get("resource_parentCode"))) {
//            //-- 非法输入: 需要更新的数据 => [父节点 <- 资源编码]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => [父节点 <- 资源编码]"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (new_resource_data.containsKey("resource_sort")
//                && ! SecurityResource.Validator.RESOURCE.sort((Integer) new_resource_data.get("resource_sort"))) {
//            //-- 非法输入: 需要更新的数据 => 序号
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => 序号"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (new_resource_data.containsKey("resource_type_vo_value")
//                && ! SecurityResource.Validator.RESOURCE.type_vo_value((Integer) new_resource_data.get("resource_type_vo_value"))) {
//            //-- 非法输入: 需要更新的数据 => [资源类型 -> VO 的值]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => [资源类型 -> VO 的值]"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        /*final UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operator_userAccountOperationInfo || operator_userAccountOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }*/
//
//        final SecurityResource latest_resource = repository.findSecurityResourceByCode(old_resource.getCode()).orElseGet(null);
//        if (null == latest_resource) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "原始版本对应数据已被删除"
//                    , old_resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        } else if (latest_resource.isEmpty()) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "原始版本对应数据异常"
//                    , old_resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        old_resource = SecurityResource.Factory.RESOURCE.update(latest_resource.getId()
//                , old_resource.getCode()
//                , old_resource.getIcon()
//                , old_resource.getLink()
//                , old_resource.getName()
//                , old_resource.getParentCode()
//                , old_resource.getSort()
//                , old_resource.getType());
//        if (! latest_resource.equals(old_resource)
//                || ! ObjectUtils.nullSafeEquals(old_resource.getCode(), latest_resource.getCode())
//                || ! ObjectUtils.nullSafeEquals(old_resource.getIcon(), latest_resource.getIcon())
//                || ! ObjectUtils.nullSafeEquals(old_resource.getLink(), latest_resource.getLink())
//                || ! ObjectUtils.nullSafeEquals(old_resource.getName(), latest_resource.getName())
//                || ! ObjectUtils.nullSafeEquals(old_resource.getParentCode(), latest_resource.getParentCode())
//                || ! ObjectUtils.nullSafeEquals(old_resource.getSort(), latest_resource.getSort())
//                || ! ObjectUtils.nullSafeEquals(old_resource.getType(), latest_resource.getType()))
//        {
//            //-- 非法输入: 原始版本业务全量数据 -> 已过期
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "原始版本业务全量数据"
//                    , "已过期"
//                    , latest_resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//
//        }
//
//        final @NotNull SecurityResource new_resource = SecurityResource.Factory.RESOURCE.update(latest_resource.getId()
//                , old_resource.getCode()
//                , old_resource.getIcon()
//                , old_resource.getLink()
//                , old_resource.getName()
//                , old_resource.getParentCode()
//                , old_resource.getSort()
//                , old_resource.getType());
//        if (new_resource_data.containsKey("resource_icon")
//                && ! new_resource.setIcon((String) new_resource_data.get("resource_icon"))) {
//            //-- 非法输入: 需要更新的数据 => 图标
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => 图标"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (new_resource_data.containsKey("resource_link")
//                && ! new_resource.setLink((String) new_resource_data.get("resource_link"))) {
//            //-- 非法输入: 需要更新的数据 => 资源链接
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => 资源链接"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (new_resource_data.containsKey("resource_name")
//                && ! new_resource.setLink((String) new_resource_data.get("resource_name"))) {
//            //-- 非法输入: 需要更新的数据 => 资源名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => 资源名称"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (new_resource_data.containsKey("resource_parentCode")
//                && ! new_resource.setParentCode((String) new_resource_data.get("resource_parentCode"))) {
//            //-- 非法输入: 需要更新的数据 => [父节点 <- 资源编码]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => [父节点 <- 资源编码]"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (new_resource_data.containsKey("resource_sort")
//                && ! new_resource.setSort((Integer) new_resource_data.get("resource_sort"))) {
//            //-- 非法输入: 需要更新的数据 => 序号
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => 序号"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (new_resource_data.containsKey("resource_type_vo_value")
//                && ! new_resource.setType((Integer) new_resource_data.get("resource_type_vo_value"))) {
//            //-- 非法输入: 需要更新的数据 => [资源类型 -> VO 的值]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "需要更新的数据 => [资源类型 -> VO 的值]"
//                    , new_resource_data
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        if (repository.saveAndFlush(new_resource).isEmpty()) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE.name
//                    , "执行后数据异常"
//                    , new_resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        /*//=== 操作日志记录 ===//
//        final @NotNull Log newLog_SecurityRole = Log.Factory.SecurityResource.LOG.create(null
//                , null
//                , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE
//                , old_resource
//                , operator
//                , operator_userAccountOperationInfo);
//        if (! logService.insert(newLog_SecurityRole)) {
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__UPDATE.name
//                    , "生成操作日志记录"
//                    , newLog_SecurityRole
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        //======//*/
//
//        return true;
//    }
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
//    @Override
//    public boolean delete(@NotNull SecurityResource resource, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == resource || resource.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (null == operator || operator.isEmpty()) {
//            //-- 非法输入: 操作者
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , operator
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
//        /*if (null == operator_userAccountOperationInfo
//                || operator_userAccountOperationInfo.isEmpty()
//                || !operator_userAccountOperationInfo.equals(operator)) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】 <- %s!</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operator_userAccountOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }*/
//
//        repository.removeByCode(resource.getCode());
//        if (repository.existsByCode(resource.getCode())) {
//            //-- 操作失败: 删除指定的资源
//            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__DELETION.name
//                    , "执行后数据异常"
//                    , resource
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }/* else {
//            //=== 操作日志记录 ===//
//            final @NotNull Log newLog_Resource = Log.Factory.SecurityResource.LOG.create(null
//                    , null
//                    , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__DELETION
//                    , resource
//                    , operator
//                    , operator_userAccountOperationInfo);
//            if (!logService.insert(newLog_Resource)) {
//                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】 <- %s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                        , HandleType.LogVo.SECURITY__SECURITY_RESOURCE__DELETION.name
//                        , "生成操作日志记录"
//                        , newLog_Resource
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//            //======//
//        }*/
//
//        return true;
//    }
//
//}
