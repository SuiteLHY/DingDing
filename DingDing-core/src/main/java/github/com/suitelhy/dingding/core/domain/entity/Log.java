package github.com.suitelhy.dingding.core.domain.entity;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.core.infrastructure.util.Toolbox;
import github.com.suitelhy.dingding.core.infrastructure.web.util.NetUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntity;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 日志记录
 *
 * @Design {@link github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel}
 */
@Entity
@Table
public class Log
        extends AbstractEntity<Long> {

    private static final long serialVersionUID = 1L;

    // [日志记录 - 编号]
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(length = 20)
    private Long id;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time")
    @Transient
    private LocalDateTime dataTime;

    /**
     * 操作的描述信息
     */
    @Column(nullable = false, length = 200)
    private String description;

    /**
     * 操作的详细信息
     */
    @Column(length = 200)
    private String detail;

    /**
     * 操作时间
     *
     * @Description {@link CalendarController#DEFAULT_DATE_FORMAT}
     */
    @Column(nullable = false)
    private String time;

    /**
     * 操作类型
     *
     * @Description 日志操作类型对应的 VO ({@link github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel}).
     *
     * @see HandleType.LogVo
     */
    @Column
    @Convert(converter = HandleType.LogVo.Converter.class)
    private HandleType.LogVo type;

    /**
     * [操作者 - （执行操作时）登录的 IP 地址]
     */
    @Column(name = "operator_ip", nullable = false, length = 32)
    private String operatorIp;

    /**
     * [操作者 - 用户名称]
     */
    @Column(name = "operator_username", nullable = false, length = 50)
    private String operatorUsername;

    /**
     * [被操作对象 - 用于日志追踪的 ID]
     */
    @Column(name = "target_id", nullable = false, length = 64)
    private String targetId;

    /**
     * 实体验证器
     *
     * @see Validator
     */
    @Transient
    private final @NotNull Validator validator;

    //===== base constructor =====//

    /**
     * 仅用于 ORM 注入
     */
    public Log() {
        validator = Validator.LOG;
    }

    //===== entity model =====//

    @Override
    public Long id() {
        return this.getId();
    }

    /**
     * 是否符合业务要求
     *
     * @return {@link Boolean#TYPE}
     */
    @Override
    public boolean isEntityLegal() {
        return this.validator.description(this.description)
                && this.validator.detail(this.detail)
                && this.validator.time(this.time)
                && this.validator.type(this.type)
                && this.validator.operatorIp(this.operatorIp)
                && this.validator.operatorUsername(this.operatorUsername)
                && this.validator.targetId(this.targetId);
    }

    /**
     * 校验 Entity - ID
     *
     * @Design {@link AbstractEntity} 提供的模板设计.
     *
     * @param id <method>id()</method>
     *
     * @return {@link Boolean#TYPE}
     */
    @Override
    protected @NotNull boolean validateId(@NotNull Long id) {
        return Validator.LOG.entity_id(id);
    }

    //===== entity validator =====//

    public enum Validator
            implements EntityValidator<Log, Long> {
        LOG {
            @Override
            public boolean type(@NotNull HandleType.LogVo type) {
                return null != type;
            }

            @Override
            public boolean targetId(@NotNull String targetId) {
                return null != targetId
                        && !"".equals(targetId)
                        && targetId.length() < 256;
            }
        },
        SECURITY_RESOURCE {
            @Override
            public boolean type(@NotNull HandleType.LogVo type) {
                switch (type) {
                    case SECURITY__SECURITY_RESOURCE__ADD:
                    case SECURITY__SECURITY_RESOURCE__UPDATE:
                    case SECURITY__SECURITY_RESOURCE__DELETION:
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean targetId(@NotNull String targetId) {
                return SecurityResource.Validator.RESOURCE.entity_id(targetId);
            }
        },
        SECURITY_RESOURCE_URL {
            @Override
            public boolean type(@NotNull HandleType.LogVo type) {
                switch (type) {
                    case SECURITY__SECURITY_RESOURCE_URL__ADD:
                    case SECURITY__SECURITY_RESOURCE_URL__DELETION:
                        return true;
                    default:
                        return false;
                }
            }

            /**
             * 校验 {@link SecurityResourceUrl} 的 {@code id} 方法
             *
             * @param targetId  {@link Object}
             *
             * @return {@link Boolean#TYPE}
             */
            @Override
            public boolean targetId(@NotNull Object targetId) {
                if (targetId instanceof String) {
                    return targetId((String) targetId);
                }
                return Toolbox.ArrayUtil.OneDimensionalArray.getInstance().belongToArray(Object.class, targetId)
                        && targetId((Object[]) targetId);
            }

            public boolean targetId(@NotNull Object[] targetId) {
                return SecurityResourceUrl.Validator.RESOURCE_URL.entity_id(targetId);
            }

            @Override
            boolean targetId(@NotNull String targetId) {
                return null != targetId
                        && targetId.startsWith("[")
                        && targetId.endsWith("]");
            }
        },
        SECURITY_ROLE {
            @Override
            public boolean type(@NotNull HandleType.LogVo type) {
                switch (type) {
                    case SECURITY__SECURITY_ROLE__ADD:
                    case SECURITY__SECURITY_ROLE__UPDATE:
                    case SECURITY__SECURITY_ROLE__DELETION:
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean targetId(@NotNull String targetId) {
                return SecurityRole.Validator.ROLE.entity_id(targetId);
            }
        },
        USER {
            @Override
            public boolean type(@NotNull HandleType.LogVo type) {
                switch (type) {
                    case USER__USER__ABNORMALITY:
                    case USER__USER__ADD:
                    case USER__USER__DATA_DELETION:
                    case USER__USER__DESTRUCTION:
                    case USER__USER__DELETION:
                    case USER__USER__UPDATE:
                    case USER__REGISTRATION:
                    case USER__LOGIN:
                    case USER__LOGGED_OUT:
                    case USER__DESTRUCTION:
                    case SECURITY__SECURITY_USER__ADD:
                    case SECURITY__SECURITY_USER__UPDATE:
                    case SECURITY__SECURITY_USER__DELETE:
                    case USER__USER_ACCOUNT_OPERATION_INFO__ADD:
                    case USER__USER_ACCOUNT_OPERATION_INFO__UPDATE:
                    case USER__USER_ACCOUNT_OPERATION_INFO__DELETE:
                    case SECURITY__SECURITY_USER_ROLE__ADD:
                    case SECURITY__SECURITY_USER_ROLE__DELETION:
                    case USER__USER_PERSON_INFO__ADD:
                    case USER__USER_PERSON_INFO__UPDATE:
                    case USER__USER_PERSON_INFO__DELETE:
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean targetId(@NotNull String targetId) {
                return null != targetId
                        && User.Validator.USER.entity_id(targetId)
                        && targetId.length() < 256;
            }
        },
        USER_ACCOUNT_OPERATION_INFO {
            @Override
            public boolean type(@NotNull HandleType.LogVo type) {
                switch (type) {
                    case USER__REGISTRATION:
                    case USER__DESTRUCTION:
                    case USER__USER_ACCOUNT_OPERATION_INFO__ADD:
                    case USER__USER_ACCOUNT_OPERATION_INFO__UPDATE:
                    case USER__USER_ACCOUNT_OPERATION_INFO__DELETE:
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean targetId(@NotNull String targetId) {
                return null != targetId
                        && UserAccountOperationInfo.Validator.USER.entity_id(targetId)
                        && targetId.length() < 256;
            }
        },
        USER_PERSON_INFO {
            @Override
            public boolean type(@NotNull HandleType.LogVo type) {
                switch (type) {
                    case USER__REGISTRATION:
                    case USER__DESTRUCTION:
                    case USER__USER_PERSON_INFO__ADD:
                    case USER__USER_PERSON_INFO__UPDATE:
                    case USER__USER_PERSON_INFO__DELETE:
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean targetId(@NotNull String targetId) {
                return UserPersonInfo.Validator.USER.entity_id(targetId)
                        && targetId.length() < 256;
            }
        };

        /*//===== 外键设计 (仅限也仅应该为 Entity 的唯一标识) (简单实现) =====//
        public final EntityValidator FOREIGN_VALIDATOR;

        public final Class FOREIGN_ID_CLAZZ;

        Validator() {
            this.FOREIGN_VALIDATOR = null;
            this.FOREIGN_ID_CLAZZ = null;
        }

        <E extends EntityModel<ID>, ID> Validator(@NotNull EntityValidator<E, ID> foreignValidator
                , @NotNull Class<ID> idClass) {
            this.FOREIGN_VALIDATOR = foreignValidator;
            this.FOREIGN_ID_CLAZZ = idClass;
        }

        private boolean foreignId(Object id) {
            if (null != this.FOREIGN_VALIDATOR
                    && null != id
                    && id.getClass() == FOREIGN_ID_CLAZZ) {
                return this.FOREIGN_VALIDATOR.id(id);
            }
            return false;
        }*/

        /*//===== 外键设计 (仅限也仅应该为 Entity 的唯一标识) (模块化实现) =====//
        private final ForeignEntityValidator FOREIGN_VALIDATOR;

        Validator() {
            this.FOREIGN_VALIDATOR = null;
        }

        <E extends EntityModel<ID>, ID> Validator(@NotNull Class<E> foreignEntityClazz
                , @NotNull EntityValidator<E, ID> foreignValidator) {
            this.FOREIGN_VALIDATOR = new ForeignEntityValidator(foreignEntityClazz, foreignValidator);
        }

        //==========//*/

        @Override
        public boolean entity_id(@NotNull Long entityId) {
            return this.id(entityId);
        }

        public boolean entity_id(@NotNull String entityId) {
            /*return EntityUtil.Regex.validateId(entityId);*/
            return this.id(entityId);
        }

        public boolean id(@NotNull Long id) {
            return null != id && id > 0L;
        }

        public boolean id(@NotNull String id) {
            return EntityUtil.Regex.validateId(id) && id.length() < 21;
        }

        public boolean description(@NotNull String description) {
            return null != description && description.length() < 201;
        }

        public boolean detail(@Nullable String detail) {
            return null == detail || detail.length() < 201;
        }

        public boolean time(@Nullable String time) {
            return null != time && CalendarController.isParse(time);
        }

        public boolean type(HandleType.LogVo type) {
            if (null == type) {
                return null != VoUtil.getInstance().getVoByValue(HandleType.LogVo.class, null);
            }
            return true;
        }

        public boolean operatorIp(@NotNull String ip) {
            return NetUtil.validateIpAddress(ip) && ip.length() < 33;
        }

        public boolean operatorUsername(@NotNull String username) {
            return User.Validator.USER.username(username) && username.length() < 51;
        }

        boolean targetId(@NotNull Object targetId) {
            return targetId instanceof String
                    && targetId((String) targetId);
        }

        abstract boolean targetId(@NotNull String targetId);

    }

    //===== entity factory =====//

    /**
     * (Constructor)
     *
     * @param id                [日志记录 - 编号]                       {@link this#id}
     * @param description       操作的描述信息                          {@link this#description}
     * @param detail            操作的详细信息                          {@link this#detail}
     * @param time              操作时间                               {@link this#time}
     * @param type              操作类型                               {@link this#type}
     * @param operatorIp        [操作者 - （执行操作时）登录的 IP 地址]    {@link this#operatorIp}
     * @param operatorUsername  [操作者 - 用户名称]                     {@link this#operatorUsername}
     * @param targetId          [被操作对象 - 用于日志追踪的 ID]          {@link this#targetId}
     * @param validator         实体验证器                             {@link Validator}
     *
     * @throws IllegalArgumentException
     */
    private Log(@Nullable String id
            , @NotNull String description
            , @Nullable String detail
            , @NotNull String time
            , @NotNull HandleType.LogVo type
            , @NotNull String operatorIp
            , @NotNull String operatorUsername
            , @NotNull String targetId
            , @NotNull Validator validator)
            throws IllegalArgumentException, NullPointerException
    {
        if (null == validator) {
            //-- 非法输入: 实体验证器
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "实体验证器"
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        } else {
            this.validator = validator;
        }

        if (null == id) {
            //--- <param>id</param>为 null 时, 对应添加日志记录功能.
        } else {
            //--- 对应更新日志记录功能.
            if (! /*Validator.LOG*/this.validator.entity_id(id)) {
                //-- 非法输入: [日志记录 - 编号]
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "[日志记录 - 编号]"
                        , id
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
        }
        if (! this.validator.description(description)) {
            //-- 非法输入: 操作的描述信息
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作的描述信息"
                    , description
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! this.validator.detail(detail)) {
            //-- 非法输入: 操作的详细信息
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作的详细信息"
                    , detail
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! this.validator.time(time)) {
            //-- 非法输入: 操作时间
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作时间"
                    , time
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! this.validator.type(type)) {
            //-- 非法输入: 操作类型
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】(对应的实体验证器:%s) <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "操作类型"
                    , type
                    , this.validator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! this.validator.operatorIp(operatorIp)) {
            //-- 非法输入: [操作者 - （执行操作时）登录的 IP 地址]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[操作者 - （执行操作时）登录的 IP 地址]"
                    , operatorIp
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! /*Validator.USER*/this.validator.operatorUsername(operatorUsername)) {
            //-- 非法输入: [操作者 - 用户名称]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[操作者 - 用户名称]"
                    , operatorUsername
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
        if (! this.validator.targetId(targetId)) {
            //-- 非法输入: [被操作对象 - 用于日志追踪的 ID]
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】(对应的实体验证器:%s) <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "[被操作对象 - 用于日志追踪的 ID]"
                    , targetId
                    , validator
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        if (null != id) {
            this.setId(Long.parseLong(id));
        }
        this.setDescription(description);
        this.setDetail(detail);
        this.setTime(time);
        this.setType(type);
        this.setOperatorIp(operatorIp);
        this.setOperatorUsername(operatorUsername);
        this.setTargetId(/*validator, */targetId);
    }

    public interface Factory {

        enum SecurityResource
                implements EntityFactoryModel<Log> {
            LOG;

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param time                              操作时间
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param resource                          （安全认证）资源              {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull String time
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource resource
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == resource || !resource.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "（安全认证）资源"
                            , resource
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("[（安全认证）资源]")
                        .append(resource)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , time
                        , type
                        , /*ip*/operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , resource.id()
                        , Validator.LOG);
            }

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param resource                          （安全认证）资源              {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource resource
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
            {
                if (null == resource || !resource.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "（安全认证）资源"
                            , resource
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("[（安全认证）资源]")
                        .append(resource)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , new CalendarController().toString()
                        , type
                        , /*ip*/operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , resource.id()
                        , Validator.LOG);
            }

            /**
             * 获取空对象
             *
             * @return 非 {@code null}.
             */
            @NotNull
            @Override
            public Log createDefault() {
                return new Log();
            }

        }

        enum SecurityResourceUrl
                implements EntityFactoryModel<Log> {
            LOG;

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param time                              操作时间
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param resourceUrl                       [资源 ←→ URL]               {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull String time
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl resourceUrl
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == resourceUrl || !resourceUrl.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[资源 ←→ URL]"
                            , resourceUrl
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                String fullDescription = String.format("[资源 ←→ URL]%s数据操作:%s"
                        , resourceUrl
                        , (null != description) ? description : type.name);
                return new Log(null
                        , fullDescription
                        , detail
                        , time
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , Arrays.toString(resourceUrl.id())
                        , Validator.SECURITY_RESOURCE_URL);
            }

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param resourceUrl                       [资源 ←→ URL]               {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl resourceUrl
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == resourceUrl || !resourceUrl.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[资源 ←→ URL]"
                            , resourceUrl
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                String fullDescription = String.format("[资源 ←→ URL]%s数据操作:%s"
                        , resourceUrl
                        , (null != description) ? description : type.name);
                return new Log(null
                        , fullDescription
                        , detail
                        , new CalendarController().toString()
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , Arrays.toString(resourceUrl.id())
                        , Validator.SECURITY_RESOURCE_URL);
            }

            /**
             * 获取空对象
             *
             * @return 非 {@code null}.
             */
            @NotNull
            @Override
            public Log createDefault() {
                return new Log();
            }

        }

        enum SecurityRole
                implements EntityFactoryModel<Log> {
            LOG;

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param time                              操作时间
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param role                              （安全认证）角色              {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull String time
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole role
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == role || !role.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "（安全认证）角色"
                            , role
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("（安全认证）角色")
                        .append(role)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , time
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , role.id()
                        , Validator.SECURITY_ROLE);
            }

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param role                              （安全认证）角色              {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityRole role
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == role || !role.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "（安全认证）角色"
                            , role
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("（安全认证）角色")
                        .append(role)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , new CalendarController().toString()
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , role.id()
                        , Validator.SECURITY_ROLE);
            }

            /**
             * 获取空对象
             *
             * @return 非 {@code null}.
             */
            @NotNull
            @Override
            public Log createDefault() {
                return new Log();
            }

        }

        enum SecurityRoleResource
                implements EntityFactoryModel<Log> {
            LOG;

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param time                              操作时间
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param roleResource                      [角色 ←→ 资源]               {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull String time
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource roleResource
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == roleResource || !roleResource.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[角色 ←→ 资源]"
                            , roleResource
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("[角色 ←→ 资源]")
                        .append(roleResource)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , time
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , Arrays.toString(roleResource.id())
                        , Validator.LOG);
            }

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param roleResource                      [角色 ←→ 资源]               {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityRoleResource roleResource
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == roleResource || !roleResource.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[角色 ←→ 资源]"
                            , roleResource
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("[角色 ←→ 资源]")
                        .append(roleResource)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , new CalendarController().toString()
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , Arrays.toString(roleResource.id())
                        , Validator.LOG);
            }

            /**
             * 获取空对象
             *
             * @return 非 {@code null}.
             */
            @NotNull
            @Override
            public Log createDefault() {
                return new Log();
            }

        }

        enum SecurityUser
                implements EntityFactoryModel<Log> {
            LOG;

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param time                              操作时间
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param user                              （安全认证）用户              {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull String time
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser user
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == user || !user.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "（安全认证）用户"
                            , user
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));

                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("（安全认证）用户")
                        .append(user)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , time
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , user.id()
                        , Validator.USER);
            }

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param user                              （安全认证）用户              {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser user
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == user || !user.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "（安全认证）用户"
                            , user
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("（安全认证）用户")
                        .append(user)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , new CalendarController().toString()
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , user.id()
                        , Validator.USER);
            }

            /**
             * 获取空对象
             *
             * @return 非 {@code null}.
             */
            @NotNull
            @Override
            public Log createDefault() {
                return new Log();
            }

        }

        enum SecurityUserRole
                implements EntityFactoryModel<Log> {
            LOG;

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param time                              操作时间
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param userRole                          [（安全认证）用户 ←→ 角色]     {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUserRole}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull String time
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUserRole userRole
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == userRole || !userRole.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[（安全认证）用户 ←→ 角色]"
                            , userRole
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("[（安全认证）用户 ←→ 角色]")
                        .append(userRole)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , time
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , Arrays.toString(userRole.id())
                        , Validator.LOG);
            }

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param userRole                          [（安全认证）用户 ←→ 角色]     {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUserRole}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUserRole userRole
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == userRole || !userRole.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[（安全认证）用户 ←→ 角色]"
                            , userRole
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("[（安全认证）用户 ←→ 角色]")
                        .append(userRole)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , new CalendarController().toString()
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , Arrays.toString(userRole.id())
                        , Validator.LOG);
            }

            /**
             * 获取空对象
             *
             * @return 非 {@code null}.
             */
            @NotNull
            @Override
            public Log createDefault() {
                return new Log();
            }

        }

        enum User
                implements EntityFactoryModel<Log> {
            LOG;

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param time                              操作时间
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param user                              用户基础信息                 {@link github.com.suitelhy.dingding.core.domain.entity.User}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull String time
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.User user
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == user || !user.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "用户基础信息"
                            , user
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("用户基础信息")
                        .append(user)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , time
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , user.id()
                        , Validator.USER);
            }

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param user                              用户基础信息                 {@link github.com.suitelhy.dingding.core.domain.entity.User}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.User user
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == user || !user.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "用户基础信息"
                            , user
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("用户基础信息")
                        .append(user)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , new CalendarController().toString()
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , user.id()
                        , Validator.USER);
            }

            /**
             * 获取空对象
             *
             * @return 非 {@code null}.
             */
            @NotNull
            @Override
            public Log createDefault() {
                return new Log();
            }

        }

        enum UserAccountOperationInfo
                implements EntityFactoryModel<Log> {
            LOG;

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param time                              操作时间
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param operationInfo                     [用户 -> 账户操作基础记录]     {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull String time
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operationInfo
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == operationInfo || !operationInfo.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "用户基础信息"
                            , operationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("用户基础信息")
                        .append(operationInfo)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , time
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , operationInfo.id()
                        , Validator.USER_ACCOUNT_OPERATION_INFO);
            }

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param operationInfo                     [用户 -> 账户操作基础记录]     {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operationInfo
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == operationInfo || !operationInfo.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[用户 -> 账户操作基础记录]"
                            , operationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("[用户 -> 账户操作基础记录]")
                        .append(operationInfo)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , new CalendarController().toString()
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , operationInfo.id()
                        , Validator.USER_ACCOUNT_OPERATION_INFO);
            }

            /**
             * 获取空对象
             *
             * @return 非 {@code null}.
             */
            @NotNull
            @Override
            public Log createDefault() {
                return new Log();
            }

        }

        enum UserPersonInfo
                implements EntityFactoryModel<Log> {
            LOG;

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param time                              操作时间
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param userPersonInfo                    [用户 -> 个人信息]           {@link github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull String time
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo userPersonInfo
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == userPersonInfo || !userPersonInfo.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[用户 -> 个人信息]"
                            , userPersonInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                StringBuilder fullDescription = new StringBuilder("[用户 -> 个人信息]")
                        .append(userPersonInfo)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , time
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , Arrays.toString(userPersonInfo.id())
                        , Validator.USER_PERSON_INFO);
            }

            /**
             * 创建
             *
             * @param description                       操作的描述信息
             * @param detail                            操作的详细信息
             * @param type                              操作类型                     {@link HandleType.LogVo}
             * @param userPersonInfo                    [用户 -> 个人信息]           {@link github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo}
             * @param operator                          操作者                      {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser}
             * @param operator_UserAccountOperationInfo [操作者 - 账户操作基础记录]    {@link github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo}
             *
             * @return {@link Log}
             */
            @NotNull
            public Log create(@Nullable String description
                    , @Nullable String detail
                    , @NotNull HandleType.LogVo type
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserPersonInfo userPersonInfo
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.security.SecurityUser operator
                    , @NotNull github.com.suitelhy.dingding.core.domain.entity.UserAccountOperationInfo operator_UserAccountOperationInfo)
                    throws IllegalArgumentException
            {
                if (null == userPersonInfo || !userPersonInfo.isEntityLegal()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[用户 -> 个人信息]"
                            , userPersonInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator || operator.isEmpty()) {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "操作者"
                            , operator
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }
                if (null == operator_UserAccountOperationInfo
                        || operator_UserAccountOperationInfo.isEmpty()
                        || !operator_UserAccountOperationInfo.equals(operator))
                {
                    throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "[操作者 - 账户操作基础记录]"
                            , operator_UserAccountOperationInfo
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                System.err.println(String.format("【调试】=> %s", userPersonInfo));

                StringBuilder fullDescription = new StringBuilder("[用户 -> 个人信息]")
                        .append(userPersonInfo)
                        .append("数据操作:")
                        .append((null != description) ? description : type.name);
                return new Log(null
                        , fullDescription.toString()
                        , detail
                        , new CalendarController().toString()
                        , type
                        , operator_UserAccountOperationInfo.getIp()
                        , operator.getUsername()
                        , Arrays.toString(userPersonInfo.id())
                        , Validator.USER_PERSON_INFO);
            }

            /**
             * 获取空对象
             *
             * @return 非 {@code null}.
             */
            @NotNull
            @Override
            public Log createDefault() {
                return new Log();
            }

        }

    }

//    public enum Factory
//            implements EntityFactory<Log> {
//        USER_LOG;
//
//        /**
//         * 创建
//         *
//         * @param detail 记录的详细信息
//         * @param ip     登录的IP地址
//         * @param time   操作时间
//         * @param type   操作类型
//         * @param userid 用户 - 用户ID
//         *
//         * @return {@link Log}
//         */
//        @NotNull
//        public Log create(@Nullable String detail
//                , @NotNull String ip
//                , @NotNull String time
//                , @NotNull HandleType.LogVo type
//                , @NotNull String userid) {
//            return new Log(null, detail, ip
//                    , time, type, userid);
//        }
//
//        /**
//         * 创建
//         *
//         * @param detail    记录的详细信息
//         * @param time      操作时间
//         * @param type      操作类型
//         * @param user      用户 {@link User}
//         *
//         * @return {@link Log}
//         */
//        @NotNull
//        public Log create(@Nullable String detail
//                , @NotNull String time
//                , @NotNull HandleType.LogVo type
//                , @NotNull User user) {
//            return new Log(null, detail, user.getIp()
//                    , time, type, user.getUserid());
//        }
//
//        /**
//         * 创建
//         *
//         * @param detail    记录的详细信息
//         * @param type      操作类型
//         * @param user      用户 {@link User}
//         *
//         * @return {@link Log}
//         */
//        @NotNull
//        public Log create(@Nullable String detail
//                , @NotNull HandleType.LogVo type
//                , @NotNull User user) {
//            return new Log(null
//                    , detail
//                    , user.getIp()
//                    , new CalendarController().toString()
//                    , type
//                    , user.getUserid());
//        }
//
//    }

    //===== getter and setter =====//

    @Nullable
    public Long getId() {
        return id;
    }

    private boolean setId(@NotNull Long id) {
        if (this.validator.id(id)) {
            this.id = id;
            return true;
        }
        return false;
    }

    public String getDescription() {
        return description;
    }

    public boolean setDescription(@NotNull String description) {
        if (this.validator.description(description)) {
            this.description = description;
            return true;
        }
        return false;
    }

    public String getTargetId() {
        return targetId;
    }

    public boolean setTargetId(/*@NotNull Validator validator, */@NotNull String targetId) {
        if (this.validator.targetId(targetId)) {
            this.targetId = targetId;
            return true;
        }
        return false;
    }

    public String getDetail() {
        return detail;
    }

    public boolean setDetail(String detail) {
        if (this.validator.detail(detail)) {
            this.detail = detail;
            return true;
        }
        return false;
    }

    public String getOperatorIp() {
        return operatorIp;
    }

    private boolean setOperatorIp(@NotNull String ip) {
        if (this.validator.operatorIp(ip)) {
            this.operatorIp = ip;
            return true;
        }
        return false;
    }

    public String getTime() {
        return time;
    }

    public boolean setTime(@NotNull String time) {
        if (this.validator.time(time)) {
            this.time = time;
            return true;
        }
        return false;
    }

    public HandleType.LogVo getType() {
        return type;
    }

    private boolean setType(@NotNull HandleType.LogVo type) {
        if (this.validator.type(type)) {
            this.type = type;
            return true;
        }
        return false;
    }

    public String getOperatorUsername() {
        return operatorUsername;
    }

    private boolean setOperatorUsername(@NotNull String username) {
        if (this.validator.operatorUsername(username)) {
            this.operatorUsername = username;
            return true;
        }
        return false;
    }

}
