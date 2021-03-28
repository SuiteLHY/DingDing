//package github.com.suitelhy.dingding.core.domain.service;
//
//import github.com.suitelhy.dingding.core.domain.entity.Log;
//import github.com.suitelhy.dingding.core.domain.repository.LogRepository;
//import github.com.suitelhy.dingding.core.domain.service.impl.LogServiceImpl;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityService;
//import org.springframework.data.domain.Page;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.validation.constraints.NotNull;
//import java.util.List;
//
///**
// * 日志记录 - 业务接口
// *
// * @see Log
// * @see LogServiceImpl
// */
//@Transactional(isolation = Isolation.READ_COMMITTED
//        , propagation = Propagation.REQUIRED
//        , readOnly = true
//        , rollbackFor = Exception.class
//        , timeout = 15)
//public interface LogService
//        extends EntityService {
//
//    /**
//     * 查询所有日志记录 (分页)
//     *
//     * @param pageIndex 分页索引, 从0开始
//     * @param pageSize  分页 - 每页容量
//     *
//     * @return {@link org.springframework.data.domain.Page}
//     */
//    @NotNull Page<Log> selectAll(int pageIndex, int pageSize)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询所有日志记录数量
//     *
//     * @param pageSize 分页 - 每页容量
//     *
//     * @return {@linkplain Long 日志记录数量}
//     */
//    @NotNull Long selectCount(int pageSize)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询[指定的操作者对应的日志记录数量]
//     *
//     * @param username {@linkplain Log.Validator#operatorUsername(String) [操作者 - 用户名称]}
//     * @param pageSize 分页 - 每页容量
//     *
//     * @return {@linkplain Long 指定的操作者对应的日志记录数量}
//     */
//    @NotNull Long selectCountByUsername(@NotNull String username, int pageSize)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询指定的日志记录
//     *
//     * @param id [日志记录 - 编号]
//     *
//     * @return {@linkplain Log 指定的日志记录}
//     */
//    @NotNull Log selectLogById(@NotNull String id)
//            throws IllegalArgumentException;
//
//    /**
//     * 查询[指定操作者对应的日志记录] (分页)
//     *
//     * @param username  {@linkplain Log.Validator#operatorUsername(String) [操作者 - 用户名称]}
//     * @param pageIndex 分页索引, 从 0 开始
//     * @param pageSize  分页 - 每页容量
//     *
//     * @return {@linkplain Log 指定操作者对应的日志记录}
//     */
//    @NotNull List<Log> selectLogByUsername(@NotNull String username, int pageIndex, int pageSize)
//            throws IllegalArgumentException;
//
//    /**
//     * 新增日志记录
//     *
//     * @param log {@linkplain Log 日志记录}
//     *
//     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在有效数据}
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean insert(@NotNull Log log)
//            throws IllegalArgumentException;
//
//    /**
//     * 删除日志记录
//     *
//     * @param id    {@linkplain Log.Validator#id(String) [日志记录 - 编号]}
//     *
//     * @return {@link Boolean#TYPE 操作是否成功 / 指定日志记录是否已被删除}
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE
//            , rollbackFor = Exception.class
//            , timeout = 15)
//    boolean deleteById(@NotNull Long id)
//            throws IllegalArgumentException;
//
//    // 屏蔽不严谨设计的业务方法
//    /*boolean deleteThisUser(String userid);*/
//
//    // 屏蔽不严谨设计的业务方法
//    /*boolean deleteAll();*/
//
//}
