package github.com.suitelhy.dingding.core.domain.service.security.impl;

import github.com.suitelhy.dingding.core.domain.entity.Log;
import github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.domain.repository.security.SecurityRoleRepository;
import github.com.suitelhy.dingding.core.domain.service.LogService;
import github.com.suitelhy.dingding.core.domain.service.security.SecurityRoleService;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.security.Security;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 角色
 *
 * @Description (安全) 角色 - 业务实现.
 * @see SecurityRoleService
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
    private SecurityRoleRepository repository;

    @Autowired
    private LogService logService;

    /**
     * 判断存在
     *
     * @param code 角色编码
     * @return {@link java.lang.Boolean}
     */
    public @NotNull
    Boolean existsByCode(@NotNull String code) {
        if (!SecurityRole.Validator.ROLE.code(code)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "角色编码"
                    , code
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.existsByCode(code);
    }

    /**
     * 查询所有
     *
     * @param pageIndex 分页索引, 从0开始
     * @param pageSize  分页 - 每页容量
     * @return {@link org.springframework.data.domain.Page)
     */
    @Override
    public @NotNull
    Page<SecurityRole> selectAll(int pageIndex, int pageSize) {
        if (pageIndex < 0) {
            //-- 非法输入: <param>pageIndex</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "pageIndex"
                    , pageIndex
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        Sort.TypedSort<SecurityRole> typedSort = Sort.sort(SecurityRole.class);
        Sort sort = typedSort.by(SecurityRole::getCode).ascending();
        Pageable page = PageRequest.of(pageIndex, pageSize, sort);

        return repository.findAll(page);
    }

    /**
     * 查询总页数
     *
     * @param pageSize 分页 - 每页容量
     * @return 分页 - 总页数
     * @Description 查询数据列表 - 分页 - 总页数
     */
    @Override
    public @NotNull
    Long selectCount(int pageSize) {
        if (pageSize < 1) {
            //-- 非法输入: <param>pageSize</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "pageSize"
                    , pageSize
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        long dataNumber = repository.count();
        return (dataNumber % pageSize == 0)
                ? (dataNumber / pageSize)
                : (dataNumber / pageSize + 1);
    }

    /**
     * 查询指定的角色
     *
     * @param code 角色编码    {@link SecurityRole}
     * @return {@link SecurityRole}
     */
    @Override
    public @NotNull
    SecurityRole selectRoleByCode(@NotNull String code) {
        if (!SecurityRole.Validator.ROLE.code(code)) {
            //-- 非法输入: 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "角色编码"
                    , code
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return repository.findByCode(code)
                .orElseGet(SecurityRole.Factory.ROLE::createDefault);
    }

//    /**
//     * 查询 (关联的) 资源
//     *
//     * @param code  角色编码    {@link SecurityRole}
//     *
//     * @return 资源的数据    {@link SecurityResource}
//     *
//     * @see SecurityResource
//     */
//    @Override
//    public List<Map<String, Object>> selectResourceByCode(@NotNull String code) {
//        if (null == code || !SecurityRole.Validator.ROLE.code(code)) {
//            //-- 非法输入: 角色编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "角色编码"
//                    , code
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        return repository.selectResourceByCode(code);
//    }

    /**
     * 新增一个角色
     *
     * @param roleVo [安全模块 VO -> 角色].    {@link Security.RoleVo}
     * @return 操作是否成功 / 是否已存在相同的有效数据
     */
    @Override
    public boolean insert(@NotNull Security.RoleVo roleVo)
            throws IllegalArgumentException, BusinessAtomicException {
        SecurityRole role;
        if (null == roleVo
                || !(role = SecurityRole.Factory.ROLE.create(roleVo)).isEntityLegal()) {
            //-- 非法输入: [安全模块 VO -> 角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[安全模块 VO -> 角色]"
                    , roleVo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (repository.existsByCode(role.getCode())) {
            //--- 已存在相同数据 (根据 EntityID) 的情况
            return !repository.findByCode(role.getCode())
                    .orElseGet(SecurityRole.Factory.ROLE::createDefault)
                    .isEmpty();
        } else {
            if (repository.saveAndFlush(role).isEmpty()) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , HandleType.LogVo.SECURITY__SECURITY_ROLE__ADD.name
                        , "执行后数据异常"
                        , role
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
        }

        return true;
    }

    /**
     * 新增一个角色
     *
     * @param role                              [（安全认证）角色], 必须合法.   {@link SecurityRole}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功 / 是否已存在相同的有效数据
     * @Description · 完整的业务流程.
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean insert(@NotNull SecurityRole role, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == role || !role.isEntityLegal()) {
            //-- 非法输入: [（安全认证）角色]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[（安全认证）角色]"
                    , role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
        if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || !operator_userAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (repository.existsByCode(role.getCode())) {
            //--- 已存在相同数据 (根据 EntityID) 的情况
            return !repository.findByCode(role.getCode())
                    .orElseGet(SecurityRole.Factory.ROLE::createDefault)
                    .isEmpty();
        } else {
            if (repository.saveAndFlush(role).isEmpty()) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , HandleType.LogVo.SECURITY__SECURITY_ROLE__ADD.name
                        , "执行后数据异常"
                        , role
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            //=== 操作日志记录 ===//
            final @NotNull Log newLog_UserRole = Log.Factory.SecurityRole.LOG.create(null
                    , null
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__ADD
                    , role
                    , operator
                    , operator_userAccountOperationInfo);
            if (!logService.insert(newLog_UserRole)) {
                throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , HandleType.LogVo.SECURITY__SECURITY_ROLE__ADD.name
                        , "生成操作日志记录"
                        , newLog_UserRole
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            //======//
        }

        return true;
    }

//    /**
//     * 新增 角色 - 资源 关联
//     *
//     * @Issue
//     * · 多次单元测试后发现 并发一致性问题 - 读脏数据 的问题 (主要使用成熟方案解决问题, 故没有分配时间进行更多不必要地步进式问题跟踪).
//     * {@link <a href="https://github.com/CyC2018/CS-Notes/blob/master/notes/%E6%95%B0%E6%8D%AE%E5%BA%93%E7%B3%BB%E7%BB%9F%E5%8E%9F%E7%90%86.md#%E8%AF%BB%E8%84%8F%E6%95%B0%E6%8D%AE">CS-Notes/数据库系统原理.md at master · CyC2018/CS-Notes</a>}
//     *
//     * @Solution
//     * · 并发一致性问题解决方案: {@link <a href="https://juejin.im/post/5c1852526fb9a04a0c2e5db6">Spring Boot+SQL/JPA实战悲观锁和乐观锁 - 掘金</a>}
//     *
//     * @param roles         [（安全认证）角色], 必须全部合法且已持久化.    {@link SecurityRole}
//     * @param resources     [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , propagation = Propagation.REQUIRED
//            , timeout = 15)
//    public boolean insertResource(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if (null == roles || roles.isEmpty()) {
//            //-- 非法输入: [（安全认证）角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）角色]"
//                    , roles
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        } else {
//            for (SecurityRole each : roles) {
//                if (null == each || each.isEmpty()) {
//                    //-- 非法输入: [（安全认证）角色]
//                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , "[（安全认证）角色]"
//                            , each
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        }
//        if (null == resources || resources.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resources
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        } else {
//            for (SecurityResource each : resources) {
//                if (null == each || each.isEmpty()) {
//                    //-- 非法输入: [（安全认证）资源]
//                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , "[（安全认证）资源]"
//                            , each
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
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
//        final UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operatorOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        /*System.err.println("========== insertResource(Set<SecurityRole>, Set<SecurityResource>) ==========");
//        System.err.println("角色参数:" + roles);
//        System.err.println("资源参数:" + resources);*/
//
//        boolean result = false;
//
//        for (SecurityRole each : roles) {
//            if (!roleResourceService.existResourceByRoleCode(each.getCode())) {
//                //--- 角色不存在绑定的资源的情况
//                /*System.err.println("角色" + each + "不存在绑定的资源集合");*/
//                for (SecurityResource eachResource : resources) {
//                    /*System.err.println("== 开始绑定, 资源" + eachResource + "... ==");*/
//                    if (!roleResourceService.insert(each, eachResource, operator)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                                , each
//                                , eachResource
//                                , operator
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                    /*System.err.println("== 绑定成功: 角色" + each + " - 资源" + eachResource + " ==");*/
//
//                    //=== 操作日志记录 ===//
//                    final @NotNull Log newLog_SecurityRoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
//                            , null
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
//                            , SecurityRoleResource.FactoryModel.ROLE_RESOURCE.create(each.getCode(), eachResource.getCode())
//                            , operator
//                            , operatorOperationInfo);
//                    if (! logService.insert(newLog_SecurityRoleResource)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                                , "生成操作日志记录"
//                                , newLog_SecurityRoleResource
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                    //======//
//                }
//            } else {
//                //--- 角色存在绑定的资源的情况
//                final List<SecurityRoleResource> existedRoleResources = roleResourceService.selectByRoleCode(each.getCode());
//
//                /*System.err.println("角色" + each + "存在绑定的资源集合: " + existedRoleResources);*/
//                for (SecurityResource eachResource : resources) {
//                    boolean exist = false;
//                    for (SecurityRoleResource existedRoleResource : existedRoleResources) {
//                        if (existedRoleResource.equals(each, eachResource)) {
//                            exist = true;
//                            break;
//                        }
//                    }
//                    if (exist) continue;
//
//                    /*System.err.println("== 开始绑定, 资源" + eachResource + "... ==");*/
//                    if (!roleResourceService.insert(each, eachResource, operator)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                                , each
//                                , eachResource
//                                , operator
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                    /*System.err.println("== 绑定成功: 角色" + each + " - 资源" + eachResource + " ==");*/
//
//                    //=== 操作日志记录 ===//
//                    final @NotNull Log newLog_SecurityRoleResource = Log.Factory.SecurityRoleResource.LOG.create(null
//                            , null
//                            , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD
//                            , SecurityRoleResource.FactoryModel.ROLE_RESOURCE.create(each.getCode(), eachResource.getCode())
//                            , operator
//                            , operatorOperationInfo);
//                    if (! logService.insert(newLog_SecurityRoleResource)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__ADD.name
//                                , "生成操作日志记录"
//                                , newLog_SecurityRoleResource
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                    //======//
//                }
//            }
//
//            result = true;
//        }
//
//        return result;
//    }

    /**
     * 更新指定的角色
     *
     * @param role                              [（安全认证）角色]  {@link SecurityRole}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功
     * @Description 全量更新.
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean update(@NotNull SecurityRole role, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: 非法角色
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "非法角色"
                    , role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
        if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || !operator_userAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (repository.saveAndFlush(role).isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__UPDATE.name
                    , "执行后数据异常"
                    , role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final @NotNull Log newLog_SecurityRole = Log.Factory.SecurityRole.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_ROLE__UPDATE
                , role
                , operator
                , operator_userAccountOperationInfo);
        if (!logService.insert(newLog_SecurityRole)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__UPDATE.name
                    , "生成操作日志记录"
                    , newLog_SecurityRole
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

    /**
     * 更新指定的角色
     *
     * @param old_role                          原始版本业务全量数据.
     * @param new_role_data                     需要更新的数据.
     *                                          · 数据格式:
     *                                          {
     *                                          role_name : [角色名称],
     *                                          role_description : [角色描述]
     *                                          }
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功
     * @Description 增量更新.
     * · 完整业务
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean update(@NotNull SecurityRole old_role
            , @NotNull Map<String, Object> new_role_data
            , @NotNull SecurityUser operator
            , @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == old_role || !old_role.isEntityLegal()) {
            //-- 非法输入: 原始版本业务全量数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "原始版本业务全量数据"
                    , old_role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == new_role_data
                || (!new_role_data.containsKey("role_name") && !new_role_data.containsKey("role_description"))) {
            //-- 非法输入: 需要更新的数据
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据"
                    , new_role_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_role_data.containsKey("role_code")
                && !SecurityRole.Validator.ROLE.code((String) new_role_data.get("role_code"))) {
            //-- 非法输入: 需要更新的数据 => 角色编码
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 角色编码"
                    , new_role_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_role_data.containsKey("role_name")
                && !SecurityRole.Validator.ROLE.name((String) new_role_data.get("role_name"))) {
            //-- 非法输入: 需要更新的数据 => 角色名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 角色名称"
                    , new_role_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_role_data.containsKey("role_description")
                && !SecurityRole.Validator.ROLE.description((String) new_role_data.get("role_description"))) {
            //-- 非法输入: 需要更新的数据 => 角色描述
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 角色描述"
                    , new_role_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        /*final @NotNull UserAccountOperationInfo operator_userAccountOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());*/
        if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || !operator_userAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        final SecurityRole latest_role = repository.findByCode(old_role.getCode()).orElseGet(null);
        if (null == latest_role) {
            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "原始版本对应数据已被删除"
                    , old_role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        } else if (latest_role.isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>%s!</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "原始版本对应数据异常"
                    , old_role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        old_role = SecurityRole.Factory.ROLE.update(latest_role.getId()
                , old_role.getCode()
                , old_role.getName()
                , old_role.getDescription());
        if (!latest_role.equals(old_role)
                || !ObjectUtils.nullSafeEquals(old_role.getCode(), latest_role.getCode())
                || !ObjectUtils.nullSafeEquals(old_role.getName(), latest_role.getName())
                || !ObjectUtils.nullSafeEquals(old_role.getDescription(), latest_role.getDescription())) {
            //-- 非法输入: 原始版本业务全量数据 -> 已过期
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "原始版本业务全量数据"
                    , "已过期"
                    , latest_role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));

        }

        final @NotNull SecurityRole new_role = SecurityRole.Factory.ROLE.update(latest_role.getId()
                , latest_role.getCode()
                , latest_role.getName()
                , latest_role.getDescription());
        if (new_role_data.containsKey("role_name")
                && !new_role.setName((String) new_role_data.get("role_name"))) {
            //-- 非法输入: 需要更新的数据 => 角色名称
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 角色名称"
                    , new_role_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (new_role_data.containsKey("role_description")
                && !new_role.setDescription((String) new_role_data.get("role_description"))) {
            //-- 非法输入: 需要更新的数据 => 角色描述
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "需要更新的数据 => 角色描述"
                    , new_role_data
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (repository.saveAndFlush(new_role).isEmpty()) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__UPDATE.name
                    , "执行后数据异常"
                    , new_role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final @NotNull Log newLog_SecurityRole = Log.Factory.SecurityRole.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_ROLE__UPDATE
                , old_role
                , operator
                , operator_userAccountOperationInfo);
        if (!logService.insert(newLog_SecurityRole)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__UPDATE.name
                    , "生成操作日志记录"
                    , newLog_SecurityRole
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

    /**
     * 删除指定的角色
     *
     * @param role                              [（安全认证）角色]  {@link SecurityRole}
     * @param operator                          操作者
     * @param operator_userAccountOperationInfo [操作者 - 账户操作基础记录]
     * @return 操作是否成功
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分.
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED
            , rollbackFor = Exception.class
            , timeout = 15)
    public boolean delete(@NotNull SecurityRole role, @NotNull SecurityUser operator, @NotNull UserAccountOperationInfo operator_userAccountOperationInfo)
            throws IllegalArgumentException, BusinessAtomicException {
        if (null == role || role.isEmpty()) {
            //-- 非法输入: 非法角色
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "非法角色"
                    , role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator || operator.isEmpty()) {
            //-- 非法输入: 操作者
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , operator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (null == operator_userAccountOperationInfo
                || operator_userAccountOperationInfo.isEmpty()
                || !operator_userAccountOperationInfo.equals(operator)) {
            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作者"
                    , "无[有效的账户操作基础记录]"
                    , operator
                    , operator_userAccountOperationInfo
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        repository.removeByCode(role.getCode());
        if (repository.existsByCode(role.getCode())) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__DELETION.name
                    , "执行后数据异常"
                    , role
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        //=== 操作日志记录 ===//
        final @NotNull Log newLog_SecurityRole = Log.Factory.SecurityRole.LOG.create(null
                , null
                , HandleType.LogVo.SECURITY__SECURITY_ROLE__DELETION
                , role
                , operator
                , operator_userAccountOperationInfo);
        if (!logService.insert(newLog_SecurityRole)) {
            throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , HandleType.LogVo.SECURITY__SECURITY_ROLE__DELETION.name
                    , "生成操作日志记录"
                    , newLog_SecurityRole
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        //======//

        return true;
    }

//    /**
//     * 删除 角色 - 资源 关联
//     *
//     * @param roles         [（安全认证）角色], 必须全部合法且已持久化.    {@link SecurityRole}
//     * @param resources     [（安全认证）资源], 必须全部合法且已持久化.    {@link SecurityResource}
//     * @param operator      操作者
//     *
//     * @return 操作是否成功
//     */
//    @Override
//    public boolean deleteResource(@NotNull Set<SecurityRole> roles, @NotNull Set<SecurityResource> resources, @NotNull SecurityUser operator)
//            throws IllegalArgumentException, BusinessAtomicException
//    {
//        if ((null == roles || roles.isEmpty())) {
//            //-- 非法输入: [（安全认证）角色]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）角色]"
//                    , roles
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        } else {
//            for (SecurityRole each : roles) {
//                if (null == each || each.isEmpty()) {
//                    //-- 非法输入: [（安全认证）角色]
//                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , "[（安全认证）角色]"
//                            , each
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
//        }
//        if (null == resources || resources.isEmpty()) {
//            //-- 非法输入: [（安全认证）资源]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "[（安全认证）资源]"
//                    , resources
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        } else {
//            for (SecurityResource each : resources) {
//                if (null == each || each.isEmpty()) {
//                    //-- 非法输入: [（安全认证）资源]
//                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                            , "[（安全认证）资源]"
//                            , each
//                            , this.getClass().getName()
//                            , Thread.currentThread().getStackTrace()[1].getMethodName()
//                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                }
//            }
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
//        final UserAccountOperationInfo operatorOperationInfo = userAccountOperationInfoService.selectByUsername(operator.getUsername());
//        if (null == operatorOperationInfo || operatorOperationInfo.isEmpty()) {
//            //-- 非法输入: 操作者 <- 无[有效的账户操作基础记录]
//            throw new IllegalArgumentException(String.format("非法参数:<description>【%s】<-【%s】</description>->【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                    , "操作者"
//                    , "无[有效的账户操作基础记录]"
//                    , operator
//                    , operatorOperationInfo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        boolean result = false;
//
//        for (SecurityRole each : roles) {
//            if (!roleResourceService.existResourceByRoleCode(each.getCode())) {
//                //--- 角色不存在绑定的资源的情况
//            } else {
//                //--- 角色存在绑定的资源的情况
//                final List<SecurityRoleResource> existedRoleResources = roleResourceService.selectByRoleCode(each.getCode());
//
//                for (SecurityResource eachResource : resources) {
//                    SecurityRoleResource existedRoleResource = null;
//                    for (SecurityRoleResource roleResource : existedRoleResources) {
//                        if (roleResource.equals(each, eachResource)) {
//                            existedRoleResource = roleResource;
//                            break;
//                        }
//                    }
//                    if (null == existedRoleResource) continue;
//
//                    if (!roleResourceService.delete(each, eachResource, operator)) {
//                        throw new BusinessAtomicException(String.format("操作失败:<description>【%s】<-【%s】</description>->【%s】&【%s】&【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
//                                , HandleType.LogVo.SECURITY__SECURITY_ROLE_RESOURCE__DELETION.name
//                                , "执行后数据异常"
//                                , each
//                                , eachResource
//                                , operator
//                                , this.getClass().getName()
//                                , Thread.currentThread().getStackTrace()[1].getMethodName()
//                                , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//                    }
//                }
//            }
//
//            result = true;
//        }
//
//        return result;
//    }

}
