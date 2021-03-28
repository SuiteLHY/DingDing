package github.com.suitelhy.dingding.security.service.api.domain.vo;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * 安全模块 VO
 *
 * @Description 安全模块相关的 VO (接口组织枚举).
 */
public interface Security<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * 加密策略
     *
     * @Description 密码加密策略.
     */
    enum PasswordEncoderVo
            implements VoModel<PasswordEncoderVo, Integer, String> {
        BCrypt(1
                , "BCryptPasswordEncoder"
                , "BCrypt 加密策略。"
                , new BCryptPasswordEncoder()
                , "\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<PasswordEncoderVo, Integer, String> {

            /**
             * @Design (单例模式 - 登记式)
             */
            private static class Factory {
                private static final Converter SINGLETON = new Converter();
            }

            private Converter() {
                super(PasswordEncoderVo.class);
            }

            public static @NotNull Converter getInstance() {
                return Factory.SINGLETON;
            }

        }

        public final @NotNull Integer code;

        public final @NotNull String name;

        public final String description;

        public final @NotNull PasswordEncoder encoder;

        /**
         * 正则表达式模板
         */
        public final @NotNull String RegexPattern;

        /**
         * (Constructor)
         *
         * @param code
         * @param name
         * @param description
         * @param encoder
         * @param RegexPattern
         */
        PasswordEncoderVo(@NotNull Integer code, @NotNull String name, @NotNull String description
                , @NotNull PasswordEncoder encoder, @NotNull String RegexPattern)
        {
            this.code = code;
            this.name = name;
            this.description = description;
            this.encoder = encoder;
            this.RegexPattern = RegexPattern;
        }

        /**
         * VO 的值
         *
         * @Description Unique attribute.
         *
         * @return {@link this#code}
         */
        @Override
        public @NotNull Integer value() {
            return this.code;
        }

        /**
         * 详细信息
         *
         * @return {@link this#description}
         */
        @Override
        public String description() {
            return this.description;
        }

        /**
         * VO 的 (展示)名称
         *
         * @return {@link this#name}
         */
        @Override
        public @NotNull String displayName() {
            return this.name;
        }

        /**
         * 等效比较
         *
         * @Description 备注: <method>equals(Object)</method>
         *
         * @param value {@link this#equalsValue(Number)}
         *
         * @return 判断结果
         *
         * @see this#equalsValue(Number)
         */
        public boolean equals(@NotNull Integer value) {
            return equalsValue(value);
        }

        @Override
        public @NotNull String toString() {
            return VoModel.toString(this);
        }

        /**
         * 提供类型转换器
         *
         * @Design 为持久化类型转换功能提供支持.
         *
         * @return {@link Converter}
         */
        @Override
        @SuppressWarnings("unchecked")
        public @NotNull Converter voAttributeConverter() {
            return Converter.getInstance();
        }

    }

    /**
     * 角色
     *
     * @Description (安全) 用户 -> 角色.
     */
    enum RoleVo
            implements VoModel<RoleVo, Integer, String> {
        /**
         * 匿名用户
         *
         * @Description 最基础的匿名身份用户，仅允许访问公开资源；相关设计拓展务必严谨。
         */
        ROLE_ANONYMOUS(0
                , "匿名用户"
                , "最基础的匿名身份用户，仅允许访问公开资源；相关设计拓展务必严谨。") {
            /**
             * 判断是否属于[具有管理员权限的]角色
             *
             * @Description (拓展的业务校验)
             *
             * @return {@link Boolean#TYPE}
             */
            @Override
            public boolean isAdministrator() {
                return false;
            }
        },
        /**
         * 用户
         *
         * @Description 拥有基础权限的角色；能够查看自己的基础信息。
         */
        USER(1
                , "用户"
                , "拥有基础权限的角色；能够查看自己的基础信息。") {
            /**
             * 判断是否属于[具有管理员权限的]角色
             *
             * @Description (拓展的业务校验)
             *
             * @return {@link Boolean#TYPE}
             */
            @Override
            public boolean isAdministrator() {
                return false;
            }
        },
        /**
         * 管理员
         *
         * @Description 拥有上级权限的角色；能够在权限范围内操作向下权限等级的角色的信息。
         */
        ADMIN(2
                , "管理员"
                , "拥有上级权限的角色；能够在权限范围内操作向下权限等级的角色的信息。") {
            /**
             * 判断是否属于[具有管理员权限的]角色
             *
             * @Description (拓展的业务校验)
             *
             * @return {@link Boolean#TYPE}
             */
            @Override
            public boolean isAdministrator() {
                return true;
            }
        };

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<RoleVo, Integer, String> {

            /**
             * @Design (单例模式 - 登记式)
             */
            private static class Factory {
                private static final Converter SINGLETON = new Converter();
            }

            private Converter() {
                super(RoleVo.class);
            }

            public static @NotNull Converter getInstance() {
                return Factory.SINGLETON;
            }

        }

        /**
         * [具有管理员权限的]的角色的集合
         *
         * @Description 业务拓展设计.
         *
         * @Issue 需要使用限定元素操作的集合.
         *
         * @Solution 实现限定元素操作的集合（待完善）.
         */
        public final static @NotNull Set<RoleVo> ADMINISTRATOR_ROLE_VO__SET;

        static {
            ADMINISTRATOR_ROLE_VO__SET = new HashSet<>(1);
            for (RoleVo each : RoleVo.class.getEnumConstants()) {
                if (each.isAdministrator()) {
                    ADMINISTRATOR_ROLE_VO__SET.add(each);
                }
            }
        }

        public final @NotNull Integer code;

        public final @NotNull String name;

        public final String description;

        /**
         * (Constructor)
         *
         * @param code
         * @param name
         * @param description
         *
         * @throws IllegalArgumentException
         */
        RoleVo(@NotNull Integer code, @NotNull String name, @NotNull String description)
                throws IllegalArgumentException
        {
            if (null == code) {
                throw new IllegalArgumentException("非法参数: <param>code</param>");
            }
            if (null == name) {
                throw new IllegalArgumentException("非法参数: <param>name</param>");
            }
            if (null == description) {
                throw new IllegalArgumentException("非法参数: <param>description</param>");
            }

            this.code = code;
            this.name = name;
            this.description = description;
        }

        /**
         * VO 的值
         *
         * @return {@link this#code}
         */
        @Override
        public Integer value() {
            return this.code;
        }

        /**
         * 详细信息
         *
         * @return
         */
        @Override
        public String description() {
            return this.description;
        }

        /**
         * (展示)名称
         *
         * @return {@link String}
         *
         * @see this#name
         */
        @Override
        public String displayName() {
            return this.name;
        }

        /**
         * 等效比较
         *
         * @Description 备注: <method>equals(Object)</method>
         *
         * @param value {@link this#equalsValue(Number)}
         *
         * @return 判断结果
         *
         * @see this#equalsValue(Number)
         */
        public boolean equals(@NotNull Integer value) {
            return equalsValue(value);
        }

        @Override
        public @NotNull String toString() {
            return VoModel.toString(this);
        }

        /**
         * 提供类型转换器
         *
         * @Design 为持久化类型转换功能提供支持.
         *
         * @return {@link Converter}
         */
        @Override
        @SuppressWarnings("unchecked")
        public @NotNull Converter voAttributeConverter() {
            return Converter.getInstance();
        }

        //===== (业务拓展属性) =====//

        /**
         * 判断是否属于[具有管理员权限的]角色
         *
         * @Description (拓展的业务校验)
         *
         * @return {@link Boolean#TYPE}
         */
        public abstract boolean isAdministrator();

        //==========//

    }

}
