//package github.com.suitelhy.dingding.core.domain.entity.security;
//
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
//import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntity;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
//import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
//import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
//import github.com.suitelhy.dingding.core.infrastructure.util.Toolbox;
//import github.com.suitelhy.dingding.core.infrastructure.web.vo.HTTP;
//import org.springframework.lang.Nullable;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * （安全认证）资源 ←→ URL
// *
// * @Description [（安全认证）资源 ←→ URL]关联关系.
// * @see SecurityResource
// */
//@Entity
//@Table(name = "SECURITY_RESOURCE_URL")
//public class SecurityResourceUrl
//        extends AbstractEntity</*Long*/Object[]> {
//
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * ID
//     *
//     * @Description 数据 ID.
//     */
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    @Column(length = 64)
//    private Long id;
//
//    /**
//     * 资源编码
//     *
//     * @Description 业务唯一.
//     */
//    @Column(nullable = false, length = 20)
//    private String code;
//
//    /**
//     * 资源服务器 ID
//     *
//     * @Description 对应资源服务器的唯一标识 {clientId}.
//     */
//    @Column(name = "client_id", nullable = false, length = 50)
//    private String clientId;
//
//    /**
//     * 资源对应的 URL Method
//     *
//     * @Description 单个对象.
//     */
//    @Column(name = "url_method", length = 50)
//    private String urlMethod;
//
//    /**
//     * 资源对应的 URL (Path部分)
//     *
//     * @Description 单个 URL.
//     */
//    @Column(name = "url_path", nullable = false, length = 255)
//    private String urlPath;
//
//    // 数据更新时间 (由数据库管理)
//    @Column(name = "data_time")
//    @Transient
//    private LocalDateTime dataTime;
//
//    //===== Entity Model =====//
//
//    @Override
//    public @NotNull /*Long*/Object[] id() {
//        return new Object[]{
//                this.code
//                , this.clientId
//                , this.urlPath
//                , this.urlMethod
//        };
//    }
//
//    /**
//     * 是否无效
//     *
//     * @return {@link Boolean#TYPE}
//     * @Description 保证 User 的基本业务实现中的合法性.
//     */
//    @Override
//    public boolean isEmpty() {
//        /*return super.isEmpty();*/
//        return !Validator.RESOURCE_URL.id(this.id)
//                || !this.isEntityLegal();
//    }
//
//    /**
//     * 是否符合基础数据合法性要求
//     *
//     * @return 判断结果
//     * @Description 只保证数据合法, 不保证业务实现中的合法性.
//     */
//    @Override
//    public boolean isEntityLegal() {
//        /*return Validator.RESOURCE_URL.code(this.code)
//                && Validator.RESOURCE_URL.clientId(this.clientId)
//                && Validator.RESOURCE_URL.urlMethod(this.urlMethod)
//                && Validator.RESOURCE_URL.urlPath(this.urlPath);*/
//        return Validator.RESOURCE_URL.validateId(this);
//    }
//
//    /**
//     * 校验 Entity - ID
//     *
//     * @param id {@link this#id()}
//     * @return {@link Boolean#TYPE}
//     * @Description {@link AbstractEntity} 提供的模板设计.
//     */
//    @Override
//    protected boolean validateId(@NotNull /*Long*/Object[] id) {
//        return Validator.RESOURCE_URL.entity_id(id);
//    }
//
//    /**
//     * 等效比较
//     *
//     * @param obj {@link Object}
//     * @return 判断结果
//     * @Description 在 {@link super#equals(Object)} 的基础上添加[判断关联（安全认证）资源]的功能.
//     * @Solution · 约束声明原则 -> {@code javax.validation.ConstraintDeclarationException: HV000151: A method overriding another method must not redefine the parameter constraint configuration, but method UserAccountOperationInfo#equals(User) redefines the configuration of Object#equals(Object).}
//     * <= 【解法: 避免在被重写方法的基础上进一步限制形参的范围】
//     */
//    @Override
//    public boolean equals(Object obj) {
//        /*if (obj instanceof EntityModel<?>) {
//            if (obj instanceof SecurityResource) {
//                return equals((SecurityResource) obj);
//            }
//            return equals((EntityModel<?>) obj);
//        }
//        return super.equals(obj);*/
//        if (obj instanceof EntityModel<?>) {
//            return this.equals((EntityModel<?>) obj);
//        }
//        return super.equals(obj);
//    }
//
//    /**
//     * 判断是否相同 <- [{@link EntityModel} 实例]
//     *
//     * @param entity 实体对象, 必须合法且可未持久化    {@link EntityModel}
//     * @return 判断结果
//     * @Description 默认按照 {@link EntityModel} 设计实现, 不应该被重写.
//     */
//    @Override
//    public boolean equals(EntityModel<?> entity) {
//        if (null != entity && this.isEntityLegal()) {
//            if (entity instanceof SecurityResource
//                    && entity.isEntityLegal()
//                    && null != ((SecurityResource) entity).id()) {
//                return this.id()[0].equals(((SecurityResource) entity).id());
//            }
//        }
//        return super.equals(entity);
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
//            implements EntityValidator<SecurityResourceUrl, /*Long*/Object[]> {
//        RESOURCE_URL;
//
//        @Override
//        public boolean validateId(@NotNull SecurityResourceUrl entity) {
//            return null != entity.id()
//                    && entity_id(entity.id());
//        }
//
//        @Override
//        public boolean entity_id(@NotNull /*Long*/Object[] entityId) {
//            return null != entityId
//                    && entityId.length == 4
//                    && this.code((String) entityId[0])
//                    && this.clientId((String) entityId[1])
//                    && this.urlPath((String) entityId[2])
//                    && this.urlMethod((String) entityId[3]);
//        }
//
//        public boolean id(@NotNull Long id) {
//            return null != id
//                    && EntityUtil.Regex.validateId(Long.toString(id));
//        }
//
//        public boolean code(@NotNull String code) {
//            return SecurityResource.Validator.RESOURCE.code(code)
//                    && code.length() < 21;
//        }
//
//        public boolean clientId(@NotNull String clientId) {
//            Map<String, Object> param_rule = new HashMap<>(1);
//            param_rule.put("maxLength", 50);
//            return EntityUtil.Regex.GeneralRule.getInstance().englishPhrases_Number_Underscore(clientId, param_rule)
//                    && clientId.length() < 51;
//        }
//
//        /**
//         * URL 校验
//         *
//         * @param methodVo URL (Method部分)
//         * @return 校验结果
//         * @see HTTP.MethodVo
//         */
//        public boolean urlMethod(@NotNull HTTP.MethodVo methodVo) {
//            return null != methodVo;
//        }
//
//        /**
//         * URL 校验
//         *
//         * @param urlMethod URL (Method部分)
//         * @return 校验结果
//         * @see EntityUtil.Regex.GeneralRule#urlPath(String)
//         */
//        public boolean urlMethod(@NotNull String urlMethod) {
//            /*return EntityUtil.Regex.GeneralRule.englishPhrases(urlMethod, null, null);*/
//            return null != VoUtil.getInstance().getVoByName(HTTP.MethodVo.class, urlMethod)
//                    && urlMethod.length() < 51;
//        }
//
//        /**
//         * URL 校验
//         *
//         * @param urlPath URL (Path部分)
//         * @return 校验结果
//         * @see EntityUtil.Regex.GeneralRule#urlPath(String)
//         */
//        public boolean urlPath(@NotNull String urlPath) {
//            return null != urlPath
//                    && EntityUtil.Regex.GeneralRule.getInstance().urlPath(urlPath)
//                    && urlPath.length() < 256;
//        }
//
//        //===== (拓展属性) =====//
//
//        /**
//         * URL 相关信息
//         *
//         * @param urlInfo [
//         *                {@link SecurityResourceUrl.Validator#clientId(String)}
//         *                , {@link SecurityResourceUrl.Validator#urlPath(String)}
//         *                , {@link SecurityResourceUrl.Validator#urlMethod(String)}
//         *                ]
//         * @return 校验结果
//         * @Description 业务拓展设计.
//         */
//        public boolean urlInfo(@NotNull String[] urlInfo) {
//            return null != urlInfo
//                    && urlInfo.length == 3
//                    && clientId(urlInfo[0])
//                    && urlPath(urlInfo[1])
//                    && urlMethod(urlInfo[2]);
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
//    public SecurityResourceUrl() {
//    }
//
//    //===== entity factory =====//
//
//    /**
//     * (构造器)
//     *
//     * @param id       数据 ID
//     * @param code     资源编码
//     * @param clientId 资源服务器 ID
//     * @param urlPath  资源对应的 URL (单个)
//     * @param methodVo 资源对应的 URL - 通信方法    {@link HTTP.MethodVo}
//     * @throws IllegalArgumentException
//     */
//    private SecurityResourceUrl(@Nullable Long id
//            , @NotNull String code
//            , @NotNull String clientId
//            , @NotNull String urlPath
//            , @NotNull HTTP.MethodVo methodVo)
//            throws IllegalArgumentException {
//        if (null == id) {
//            //--- 添加功能
//        } else {
//            //--- 更新功能
//            if (!Validator.RESOURCE_URL.id(id)) {
//                //-- 非法输入: [数据 ID]
//                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , "[数据 ID]"
//                        , id
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//        }
//        if (!Validator.RESOURCE_URL.code(code)) {
//            //-- 非法输入: 资源编码
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "资源编码"
//                    , code
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.RESOURCE_URL.clientId(clientId)) {
//            //-- 非法输入: [资源服务器 ID]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[资源服务器 ID]"
//                    , clientId
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.RESOURCE_URL.urlPath(urlPath)) {
//            //-- 非法输入: [资源对应的 URL]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[资源对应的 URL]"
//                    , urlPath
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//        if (!Validator.RESOURCE_URL.urlMethod(methodVo)) {
//            //-- 非法输入: [URL 通信方法]
//            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                    , "[URL 通信方法]"
//                    , methodVo
//                    , this.getClass().getName()
//                    , Thread.currentThread().getStackTrace()[1].getMethodName()
//                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//        }
//
//        // 数据ID
//        this.serId(id);
//        // 资源编码
//        this.setCode(code);
//        // 资源服务器 ID
//        this.setClientId(clientId);
//        // 资源对应的 URL
//        this.setUrlPath(urlPath);
//        // URL通信方法
//        this.setUrlMethod(methodVo.name());
//    }
//
//    public enum Factory
//            implements EntityFactoryModel<SecurityResourceUrl> {
//        RESOURCE_URL;
//
//        /**
//         * 创建
//         *
//         * @param code     资源编码
//         * @param clientId 资源服务器 ID
//         * @param url      资源对应的 URL (单个)
//         * @param methodVo 资源对应的 URL - 通信方法    {@link HTTP.MethodVo}
//         * @return {@link SecurityResourceUrl}
//         * @throws IllegalArgumentException
//         */
//        public @NotNull
//        SecurityResourceUrl create(@NotNull String code
//                , @NotNull String clientId
//                , @NotNull String url
//                , @NotNull HTTP.MethodVo methodVo)
//                throws IllegalArgumentException {
//            return new SecurityResourceUrl(null, code, clientId
//                    , url, methodVo);
//        }
//
//        /**
//         * 创建
//         *
//         * @param code       资源编码
//         * @param clientId   资源服务器 ID
//         * @param url        资源对应的 URL (单个)
//         * @param methodName 资源对应的 URL - 通信方法    {@link HTTP.MethodVo#name()}
//         * @return {@link SecurityResourceUrl}
//         * @throws IllegalArgumentException
//         */
//        public @NotNull
//        SecurityResourceUrl create(@NotNull String code
//                , @NotNull String clientId
//                , @NotNull String url
//                , @NotNull String methodName)
//                throws IllegalArgumentException {
//            return new SecurityResourceUrl(null
//                    , code
//                    , clientId
//                    , url
//                    , Toolbox.VoUtil.getInstance().getVoByName(HTTP.MethodVo.class, methodName));
//        }
//
//        /**
//         * 创建
//         *
//         * @param resource [（安全认证）资源]
//         * @param urlInfo  [URL 相关信息]
//         * @return {@link SecurityResourceUrl}
//         * @throws IllegalArgumentException
//         */
//        public @NotNull
//        SecurityResourceUrl create(@NotNull SecurityResource resource, @NotNull String[] urlInfo)
//                throws IllegalArgumentException {
//            if (null == resource || resource.isEmpty()) {
//                //-- 非法输入: [（安全认证）资源]
//                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , "[（安全认证）资源]"
//                        , resource
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//            if (!Validator.RESOURCE_URL.urlInfo(urlInfo)) {
//                //-- 非法输入: [URL 相关信息]
//                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , "[URL 相关信息]"
//                        , Arrays.toString(urlInfo)
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            return new SecurityResourceUrl(null
//                    , resource.getCode()
//                    , urlInfo[0]
//                    , urlInfo[1]
//                    , Toolbox.VoUtil.getInstance().getVoByName(HTTP.MethodVo.class, urlInfo[2]));
//        }
//
//        /**
//         * 更新
//         *
//         * @param id       数据 ID
//         * @param code     资源编码
//         * @param clientId 资源服务器 ID
//         * @param url      资源对应的 URL (单个)
//         * @return {@link SecurityResourceUrl}
//         * @throws IllegalArgumentException
//         */
//        public @NotNull
//        SecurityResourceUrl update(@NotNull Long id
//                , @NotNull String code
//                , @NotNull String clientId
//                , @NotNull String url
//                , @NotNull HTTP.MethodVo methodVo)
//                throws IllegalArgumentException {
//            if (!Validator.RESOURCE_URL.id(id)) {
//                //-- 非法输入: [数据 ID]
//                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , "[数据 ID]"
//                        , id
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            return new SecurityResourceUrl(id, code, clientId
//                    , url, methodVo);
//        }
//
//        /**
//         * 获取空对象
//         *
//         * @return 非 {@code null}.
//         */
//        @Override
//        public @NotNull
//        SecurityResourceUrl createDefault() {
//            return new SecurityResourceUrl();
//        }
//
//    }
//
//    //===== getter & setter =====//
//
//    /*@NotNull // 名称为"id"的属性, 在持久化之前会被根据getter方法进行校验; 在新增数据时, 作为ID的字段值由数据库管理填充, 所以此处不应该使用该注解*/
//    @Nullable
//    public Long getId() {
//        return id;
//    }
//
//    private boolean serId(@NotNull Long id) {
//        if (!Validator.RESOURCE_URL.id(id)) {
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
//        if (Validator.RESOURCE_URL.code(code)) {
//            this.code = code;
//            return true;
//        }
//        return false;
//    }
//
//    @NotNull
//    public String getClientId() {
//        return clientId;
//    }
//
//    public boolean setClientId(@NotNull String clientId) {
//        if (Validator.RESOURCE_URL.clientId(clientId)) {
//            this.clientId = clientId;
//            return true;
//        }
//        return false;
//    }
//
//    public String getUrlMethod() {
//        return urlMethod;
//    }
//
//    public boolean setUrlMethod(String urlMethod) {
//        if (Validator.RESOURCE_URL.urlMethod(urlMethod)) {
//            this.urlMethod = urlMethod;
//            return true;
//        }
//        return false;
//    }
//
//    @NotNull
//    public String getUrlPath() {
//        return urlPath;
//    }
//
//    private boolean setUrlPath(@NotNull String urlPath) {
//        if (Validator.RESOURCE_URL.urlPath(urlPath)) {
//            this.urlPath = urlPath;
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 获取[URL 信息]
//     *
//     * @return {@link String[]}
//     * @Description 业务拓展方法.
//     * @see Validator#urlInfo(String[])
//     */
//    public @NotNull
//    String[] getUrlInfo() {
//        return new String[]{
//                this.getClientId()
//                , this.getUrlPath()
//                , this.getUrlMethod()
//        };
//    }
//
//    /**
//     * 设置[URL 信息]
//     *
//     * @param urlInfo [URL 信息]
//     * @return 操作结果 - 是否全部成功（已成功的操作不会被回滚）
//     * · {@code true}   - 此时所有操作全部[合法且执行成功].
//     * · {@code false}  - 此时所有或部分操作[不合法或执行失败].
//     * @see Validator#urlInfo(String[])
//     */
//    public boolean setUrlInfo(@NotNull String[] urlInfo) {
//        if (Validator.RESOURCE_URL.urlInfo(urlInfo)) {
//            return this.setClientId(urlInfo[0])
//                    && this.setUrlPath(urlInfo[1])
//                    && this.setUrlMethod(urlInfo[2]);
//        }
//        return false;
//    }
//
//}
