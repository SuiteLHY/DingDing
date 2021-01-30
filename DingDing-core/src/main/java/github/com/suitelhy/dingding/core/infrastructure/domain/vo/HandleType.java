package github.com.suitelhy.dingding.core.infrastructure.domain.vo;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * 操作类型
 *
 * @Description 例如: xx业务的操作类型.
 */
public interface HandleType<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * 日志记录 - 操作类型
     *
     * @see github.com.suitelhy.dingding.core.domain.entity.Log#getType()
     * @see github.com.suitelhy.dingding.core.domain.entity.Log.Validator#type(LogVo)
     */
    enum LogVo
            implements VoModel<LogVo, Integer, String> {
        /**
         * 添加用户基础数据
         */
        USER__USER__ADD(1
                , "添加用户基础数据"
                , "正常业务流程, 注册用户的一部分"),
        /**
         * 用户基础数据更新
         */
        USER__USER__UPDATE(2
                , "用户基础数据更新"
                , "正常业务流程, 更新用户信息"),
        /**
         * 用户基础数据异常
         */
        USER__USER__ABNORMALITY(3
                , "用户基础数据异常"
                , "非正常业务流程, 或者发现脏数据等情况; 该账户异常, 很可能出现了严重的问题, 应该被禁用"),
        /**
         * 用户变为已销毁状态
         *
         * @Description · 完整的业务流程.
         * · 正常业务流程, 一般用于用户销毁业务.
         */
        USER__USER__DESTRUCTION(4
                , "用户基础数据变为已销毁状态"
                , "正常业务流程, 一般用于用户销毁业务"),
        /**
         * 用户基础数据删除
         */
        USER__USER__DATA_DELETION(5
                , "用户基础数据被删除"
                , "删除用户基础数据, 或确认用户基础数据丢失"),
        /**
         * 用户已被删除
         *
         * @Description · 完整的业务流程.
         * · 正常业务流程, 一般用于用户删除业务.
         */
        USER__USER__DELETION(6
                , "用户及其所有关联的数据被删除"
                , "正常业务流程, 一般用于用户删除业务"),
        /**
         * 用户注册
         */
        USER__REGISTRATION(11
                , "用户注册"
                , "正常业务流程, 注册用户"),
        /**
         * 用户登入
         */
        USER__LOGIN(12
                , "用户登入"
                , "正常业务流程, 用户登入系统"),
        /**
         * 用户登出
         */
        USER__LOGGED_OUT(13
                , "用户登出"
                , "正常业务流程, 用户登出系统"),
        /**
         * 用户销毁
         */
        USER__DESTRUCTION(14
                , "用户销毁"
                , "正常业务流程, 用户变为\"已销毁\"状态; \"已销毁\"状态的用户应该被禁用且不应该出现在用户列表之中"),
        /**
         * 添加（安全认证）用户
         */
        SECURITY__SECURITY_USER__ADD(21
                , "添加（安全认证）用户"
                , "用于用户注册业务"),
        /**
         * 修改（安全认证）用户
         */
        SECURITY__SECURITY_USER__UPDATE(22
                , "修改（安全认证）用户"
                , null),
        /**
         * 删除（安全认证）用户
         */
        SECURITY__SECURITY_USER__DELETE(23
                , "删除（安全认证）用户"
                , "用于用户数据删除业务"),
        /**
         * 添加[用户 -> 账户操作基础记录]
         */
        USER__USER_ACCOUNT_OPERATION_INFO__ADD(31
                , "添加[用户 -> 账户操作基础记录]"
                , "用于用户数据添加业务"),
        /**
         * 修改[用户 -> 账户操作基础记录]
         */
        USER__USER_ACCOUNT_OPERATION_INFO__UPDATE(32
                , "修改[用户 -> 账户操作基础记录]"
                , "用于用户数据修改业务"),
        /**
         * 删除[用户 -> 账户操作基础记录]
         */
        USER__USER_ACCOUNT_OPERATION_INFO__DELETE(33
                , "删除[用户 -> 账户操作基础记录]"
                , "用于用户数据删除业务"),
        /**
         * 添加（安全认证）角色
         */
        SECURITY__SECURITY_ROLE__ADD(41
                , "添加（安全认证）角色"
                , null),
        /**
         * 修改（安全认证）角色
         */
        SECURITY__SECURITY_ROLE__UPDATE(42
                , "修改（安全认证）角色"
                , null),
        /**
         * 删除（安全认证）角色
         */
        SECURITY__SECURITY_ROLE__DELETION(43
                , "删除（安全认证）角色"
                , null),
        /**
         * 添加（安全认证）资源
         */
        SECURITY__SECURITY_RESOURCE__ADD(51
                , "添加（安全认证）资源"
                , null),
        /**
         * 修改（安全认证）资源
         */
        SECURITY__SECURITY_RESOURCE__UPDATE(52
                , "修改（安全认证）资源"
                , null),
        /**
         * 删除（安全认证）资源
         */
        SECURITY__SECURITY_RESOURCE__DELETION(53
                , "删除（安全认证）资源"
                , null),
        /**
         * 添加[（安全认证）用户 ←→ 角色]关联关系
         */
        SECURITY__SECURITY_USER_ROLE__ADD(61
                , "添加[（安全认证）用户 ←→ 角色]关联关系"
                , null),
        /**
         * 删除[（安全认证）用户 ←→ 角色]关联关系
         */
        SECURITY__SECURITY_USER_ROLE__DELETION(63
                , "删除[（安全认证）用户 ←→ 角色]关联关系"
                , null),
        /**
         * 添加[角色 ←→ 资源]关联关系
         */
        SECURITY__SECURITY_ROLE_RESOURCE__ADD(71
                , "添加[角色 ←→ 资源]关联关系"
                , null),
        /**
         * 删除[角色 ←→ 资源]关联关系
         */
        SECURITY__SECURITY_ROLE_RESOURCE__DELETION(73
                , "删除[角色 ←→ 资源]关联关系"
                , null),
        /**
         * 添加[资源 ←→ URL]关联关系
         */
        SECURITY__SECURITY_RESOURCE_URL__ADD(81
                , "添加[资源 ←→ URL]关联关系"
                , null),
        /**
         * 删除[资源 ←→ URL]关联关系
         */
        SECURITY__SECURITY_RESOURCE_URL__DELETION(83
                , "删除[资源 ←→ URL]关联关系"
                , null),
        /**
         * 添加[用户 -> 个人信息]
         */
        USER__USER_PERSON_INFO__ADD(91
                , "添加[用户 -> 个人信息]"
                , "用于用户数据添加业务"),
        /**
         * 修改[用户 -> 个人信息]
         */
        USER__USER_PERSON_INFO__UPDATE(92
                , "修改[用户 -> 个人信息]"
                , "用于用户数据修改业务"),
        /**
         * 删除[用户 -> 个人信息]
         */
        USER__USER_PERSON_INFO__DELETE(93
                , "删除[用户 -> 个人信息]"
                , "用于用户数据删除业务");

        /**
         * 类型转换器
         *
         * @Description 为持久化类型转换器提供支持.
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<LogVo, Integer, String> {

            /**
             * @Design (单例模式 - 登记式)
             */
            private static class Factory {
                private static final Converter SINGLETON = new Converter();
            }

            private Converter() {
                super(LogVo.class);
            }

            @NotNull
            public static Converter getInstance() {
                return Factory.SINGLETON;
            }

        }

        @NotNull
        public final Integer code;

        @NotNull
        public final String name;

        @NotNull
        public final String description;

        LogVo(@NotNull Integer code, @NotNull String name, @Nullable String description) {
            this.code = code;
            this.name = name;
            this.description = (null != description)
                    ? description
                    : "";
        }

        /**
         * VO 的值
         *
         * @return
         */
        @Override
        public Integer value() {
            return this.code;
        }

        /**
         * 等效比较
         *
         * @param value
         * @return {@link boolean}
         * @Description 备注: <method>equals(Object)</method>
         */
        public boolean equals(@Nullable Integer value) {
            return equalsValue(value);
        }

        @Override
        public String toString() {
            return VoModel.toString(this);
        }

        /**
         * 提供类型转换器
         *
         * @Design 为持久化类型转换功能提供支持.
         */
        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public Converter voAttributeConverter() {
            return Converter.getInstance();
        }

    }

}
