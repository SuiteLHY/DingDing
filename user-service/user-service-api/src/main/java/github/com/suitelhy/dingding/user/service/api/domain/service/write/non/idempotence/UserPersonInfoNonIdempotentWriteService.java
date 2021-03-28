package github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 用户 -> 账户操作记录 - 业务接口
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see UserPersonInfo
 * @see Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
public interface UserPersonInfoNonIdempotentWriteService {

    //===== 添加操作业务 =====//

    /**
     * 新增一条记录
     *
     * @param user      {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean insert(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 修改操作业务 =====//

    /**
     * 更新指定的用户 -> [用户 -> 个人信息]
     *
     * @param old_userPersonInfo        {@linkplain UserPersonInfo 被修改的[用户 -> 个人信息] <- 原始版本业务全量数据}
     * @param new_userPersonInfo_data   {@linkplain Map 被修改的[用户 -> 个人信息] <- 需要更新的数据}
     * · 数据结构:
     * {
     *  "nickname": [用户 - 昵称],
     *  "age": [用户 - 年龄],
     *  "faceImage": [用户 - 头像],
     *  "introduction": [用户 - 简介],
     *  "sex": [用户 - 性别]
     * }
     * @param operator                  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean update(@NotNull UserPersonInfo old_userPersonInfo, @NotNull Map<String, Object> new_userPersonInfo_data, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

    //===== 删除操作业务 =====//

    /**
     * 删除指定的记录
     *
     * @param user      {@linkplain UserPersonInfo [用户 -> 个人信息]}
     * @param operator  {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    boolean delete(@NotNull UserPersonInfo user, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException;

}
