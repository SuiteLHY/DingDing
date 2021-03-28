//package github.com.suitelhy.dingding.core.domain.entity.security;
//
//import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
//import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntity;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
//import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
//import github.com.suitelhy.dingding.core.infrastructure.util.Toolbox;
//import org.springframework.lang.Nullable;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * （安全认证）角色
// *
// * @Description 安全认证 -> 角色表.
// */
//@Entity
//@Table(name = "SECURITY_ROLE")
//public class SecurityRole
//        extends AbstractEntity<String> {
//
////    static {
////        //--- (使用静态块) 初始化相关数据
////        for (Security.RoleVo vo : Security.RoleVo.class.getEnumConstants()) {
////            //-- (编译器会自动调用枚举的构造器)
////        }
////    }
//
//    /**
//     * ID
//     *
//     * @Description 数据 ID.
//     * @Design {@link github.com.suitelhy.dingding.security.service.api.domain.vo.Security.RoleVo#code}
//     * @see github.com.suitelhy.dingding.security.service.api.domain.vo.Security.RoleVo#code
//     */
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    @Column(length = 64)
//    private Long id;
//
//    /**
//     * 角色编码
//     *
//     * @Description 业务唯一.
//     * @Design {@link Security.RoleVo#name()}
//     * @see Security.RoleVo#name()
//     */
//    @Column(nullable = false, unique = true, length = 20)
//    private String code;
//
//    /**
//     * 角色描述
//     *
//     * @Description 可为空.
//     * @Design {@link github.com.suitelhy.dingding.security.service.api.domain.vo.Security.RoleVo#description}
//     * @see github.com.suitelhy.dingding.security.service.api.domain.vo.Security.RoleVo#description
//     */
//    @Column(length = 200)
//    private String description;
//
//    /**
//     * 角色名称
//     *
//     * @Design {@link github.com.suitelhy.dingding.security.service.api.domain.vo.Security.RoleVo#name}
//     * @see github.com.suitelhy.dingding.security.service.api.domain.vo.Security.RoleVo#name
//     */
//    @Column(nullable = false, length = 50)
//    private String name;
//
//    //===== Entity Model =====//
//
//    @Override
//    public @NotNull
//    String id() {
//        return this.code;
//    }
//
//    /**
//     * 是否无效
//     *
//     * @return
//     * @Description 保证 Entity 的基本业务实现中的合法性.
//     */
//    @Override
//    public boolean isEmpty() {
//        return !Validator.ROLE.id(this.id)
//                || !this.isEntityLegal();
//    }
//
//    /**
//     * 是否符合基础数据合法性要求
//     *
//     * @return
//     * @Description 只保证 Entity 的数据合法, 不保证 Entity 的业务实现中的合法性.
//     */
//    @Override
//    public boolean isEntityLegal() {
//        return Validator.ROLE.code(this.code)
//                && Validator.ROLE.name(this.name)
//                && Validator.ROLE.description(this.description);
//    }
//
//    /**
//     * 校验 Entity - ID
//     *
//     * @param id <method>id()</method>
//     * @return
//     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
//     */
//    @Override
//    protected boolean validateId(@NotNull String id) {
//        return Validator.ROLE.entity_id(id);
//    }
//
//    //===== Entity Validator =====//
//
//    /**
//     * 角色 - 属性校验器
//     *
//     * @Description 各个属性的基础校验(注意 : 此校验 ≠ 完全校验).
//     */
//    public enum Validator
//            implements EntityValidator<SecurityRole, String> {
//        ROLE,
//        ADMIN_ROLE;
//
//        @Override
//        public boolean validateId(@NotNull SecurityRole entity) {
//            return null != entity.id() && entity_id(entity.id());
//        }
//
//        @Override
//        public boolean entity_id(@NotNull String entityId) {
//            return this.code(entityId);
//        }
//
//        public boolean id(@NotNull Long id) {
//            return null != id
//                    && EntityUtil.Regex.validateId(Long.toString(id));
//        }
//
//        public boolean code(@NotNull String code) {
//            Map<String, Object> param_rule = new HashMap<>(1);
//            param_rule.put("maxLength", 20);
//            return EntityUtil.Regex.GeneralRule.getInstance().englishPhrases_Number(code, param_rule)
//                    || EntityUtil.Regex.GeneralRule.getInstance().englishPhrases_Number_Underscore(code, param_rule);
//        }
//
//        public boolean description(@NotNull String description) {
//            return null != description && description.length() < 201;
//        }
//
//        public boolean name(@NotNull String name) {
//            Map<String, Object> param_rule = new HashMap<>(1);
//            param_rule.put("maxLength", 20);
//            return EntityUtil.Regex.GeneralRule.getInstance().chinese_EnglishPhrases_Number(name, param_rule);
//        }
//
//        //===== 拓展的业务校验 =====//
//
//        /**
//         * 判断是否属于[具有管理员权限的]角色
//         *
//         * @param role {@link SecurityRole}
//         * @return {@link Boolean#TYPE}
//         * @Description (拓展的业务校验)
//         */
//        public static boolean isAdminRole(@NotNull SecurityRole role) {
//            if (null != role && !role.isEmpty()) {
//                for (Security.RoleVo each : Security.RoleVo.ADMINISTRATOR_ROLE_VO__SET) {
//                    if (role.equals(each)) {
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//
//        //==========//
//
//    }
//
//    //===== base constructor =====//
//
//    /**
//     * 仅用于持久化注入
//     */
//    public SecurityRole() {
//    }
//
//    //===== entity factory =====//
//
//    /**
//     * 创建/更新
//     *
//     * @param id          数据 ID
//     * @param code        角色编码
//     * @param name        角色名称
//     * @param description 角色描述
//     * @throws IllegalArgumentException
//     */
//    private SecurityRole(@NotNull Long id
//            , @NotNull String code
//            , @NotNull String name
//            , @NotNull String description)
//            throws IllegalArgumentException {
//        if (null == id) {
//            //--- 添加功能
//        } else {
//            //--- 更新功能
//            if (!Validator.ROLE.id(id)) {
//                //-- 非法输入: [数据 ID]
//                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , "[数据 ID]"
//                        , id
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//        }
//        if (!Validator.ROLE.code(code)) {
//            //-- 非法输入: 角色编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "角色编码"
//                    , code
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.ROLE.name(name)) {
//            //-- 非法输入: 角色名称
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "角色名称"
//                    , name
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.ROLE.description(description)) {
//            //-- 非法输入: 角色描述
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "角色描述"
//                    , description
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        // 数据ID
//        this.setId(id);
//        // 角色编码
//        this.setCode(code);
//        // 角色名称
//        this.setName(name);
//        // 角色描述
//        this.setDescription(description);
//    }
//
//    public enum Factory
//            implements EntityFactoryModel<SecurityRole> {
//        ROLE;
//
//        /**
//         * 创建
//         *
//         * @param code        角色编码
//         * @param name        角色名称
//         * @param description 角色描述
//         * @throws IllegalArgumentException
//         */
//        public SecurityRole create(@NotNull String code
//                , @NotNull String name
//                , @NotNull String description)
//                throws IllegalArgumentException {
//            return new SecurityRole(null, code, name, description);
//        }
//
//        /**
//         * 创建
//         *
//         * @param roleVo {@link Security.RoleVo}
//         * @throws IllegalArgumentException
//         */
//        public SecurityRole create(@NotNull Security.RoleVo roleVo)
//                throws IllegalArgumentException {
//            return new SecurityRole(null, roleVo.name(), roleVo.name
//                    , roleVo.description);
//        }
//
//        /**
//         * 更新
//         *
//         * @param id          数据 ID
//         * @param code        角色编码
//         * @param name        角色名称
//         * @param description 角色描述
//         * @return {@link SecurityRole}
//         * @throws IllegalArgumentException 此时 {@param id} 非法
//         */
//        public SecurityRole update(@NotNull Long id
//                , @NotNull String code
//                , @NotNull String name
//                , @NotNull String description)
//                throws IllegalArgumentException {
//            if (!Validator.ROLE.id(id)) {
//                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , "[数据 ID]"
//                        , id
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//            return new SecurityRole(id, code, name, description);
//        }
//
//        /**
//         * 获取空对象
//         *
//         * @return 非 {@code null}.
//         */
//        @NotNull
//        @Override
//        public SecurityRole createDefault() {
//            return new SecurityRole();
//        }
//
//    }
//
//    //===== 基础业务设计 =====//
//
//    /**
//     * 等效比较
//     *
//     * @param roleVo {@link Security.RoleVo}
//     * @return 判断结果
//     * @Tips · 覆盖另一个方法的方法一定不能重新定义参数约束配置;
//     * {@link this#equals(Security.RoleVo)} 不能重新定义 {@link Object#equals(Object)} 的配置.
//     */
//    public boolean equals(/*@NotNull */Security.RoleVo roleVo) {
//        return null != roleVo
//                /*&& roleVo.equals(Toolbox.VoUtil.getInstance().getVoByName(Security.RoleVo.class, this.code)*/
//                && roleVo.name().equals(this.code);
//    }
//
//    //===== getter & setter =====//
//
//    @Nullable
//    public Long getId() {
//        return id;
//    }
//
//    private boolean setId(@NotNull Long id) {
//        if (Validator.ROLE.id(id)) {
//            this.id = id;
//            return true;
//        }
//        return false;
//    }
//
//    @NotNull
//    public String getCode() {
//        return code;
//    }
//
//    private boolean setCode(@NotNull String code) {
//        if (Validator.ROLE.code(code)) {
//            this.code = code;
//            return true;
//        }
//        return false;
//    }
//
//    @NotNull
//    public String getDescription() {
//        return description;
//    }
//
//    public boolean setDescription(@NotNull String description) {
//        if (Validator.ROLE.description(description)) {
//            this.description = description;
//            return true;
//        }
//        return false;
//    }
//
//    @NotNull
//    public String getName() {
//        return name;
//    }
//
//    public boolean setName(@NotNull String name) {
//        if (Validator.ROLE.name(name)) {
//            this.name = name;
//            return true;
//        }
//        return false;
//    }
//
//}
