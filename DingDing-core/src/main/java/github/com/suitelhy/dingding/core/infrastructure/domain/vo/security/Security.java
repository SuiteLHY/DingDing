package github.com.suitelhy.dingding.core.infrastructure.domain.vo.security;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;

import javax.validation.constraints.NotNull;

/**
 * 安全模块 VO
 *
 * @Description 安全模块相关的 VO (接口组织枚举).
 */
public interface Security<VO extends Enum & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * 角色
     *
     * @Description (安全) 用户 -> 角色.
     */
    enum RoleVo
            implements VoModel<RoleVo, Integer, String> {
        USER(1
                , "用户"
                , "拥有基础权限的角色；能够查看自己的基础信息。")
        , ADMIN(2
                , "管理员"
                , "拥有上级权限的角色；能够在权限范围内操作向下权限等级的角色的信息。");

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<RoleVo, Integer, String> {

            public Converter() {
                super(RoleVo.class);
            }

        }

        @NotNull
        public final Integer code;

        @NotNull
        public final String name;

        public final String description;

        RoleVo(@NotNull Integer code, @NotNull String name, @NotNull String description) {
            this.code = code;
            this.name = name;
            this.description = description;
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
         * @return
         */
        @Override
        public String displayName() {
            return this.name;
        }

        /**
         * 备注: <method>equals(Object)</method>
         *
         * @param value
         * @return
         */
        public boolean equals(@NotNull Integer value) {
            return equalsValue(value);
        }

        @Override
        public String toString() {
            return VoModel.toString(this);
        }

    }

}
