package github.com.suitelhy.dingding.core.infrastructure.application.dto.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
import github.com.suitelhy.dingding.core.infrastructure.application.model.DtoModel;
import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResource;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactoryModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.ContainArrayHashSet;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.HashSet;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Resource;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * (安全) 资源
 *
 * @Description (安全) 资源 -> DTO.
 *
 * @see SecurityResource
 */
public class ResourceDto
        implements DtoModel<SecurityResource, String> {

    private static final long serialVersionUID = 1L;

    // ID
    protected Long id;

    // 资源编码
    protected String code;

    // 图标
    protected String icon;

    // 资源链接
    protected String link;

    // 资源名称
    protected String name;

    // 父节点 <- 资源编码
    protected String parentCode;

    // 序号
    protected Integer sort;

    // 资源类型
    protected Resource.TypeVo type;

    // (关联的) [URL 信息]
    protected final @NotNull ContainArrayHashSet<String> urlInfoSet = new ContainArrayHashSet<>(0);

    //===== DtoModel =====//

    /**
     * [唯一标识 <- DTO 对象]
     */
    @NotNull
    private final transient SecurityResource dtoId;

    /**
     * (Constructor)
     *
     * @Description 用于构建缺省空对象.
     */
    protected ResourceDto() {
        this.dtoId = SecurityResource.Factory.RESOURCE.createDefault();
    }

    /**
     * (Constructor)
     *
     * @param dtoId [唯一标识 <- DTO 对象]
     *
     * @throws IllegalArgumentException
     */
    protected ResourceDto(final @NotNull SecurityResource dtoId)
            throws IllegalArgumentException
    {
        if (null == dtoId || !dtoId.isEntityLegal()) {
            //-- 非法输入: <param>dtoId</param>
            throw new IllegalArgumentException(String.format("%s -> 非法输入: <param>dtoId</param>", this.getClass().getSimpleName()));
        }

        this.dtoId = dtoId;

        this.id = this.dtoId.getId();
        this.code = this.dtoId.getCode();
        this.icon = this.dtoId.getIcon();
        this.link = this.dtoId.getLink();
        this.name = this.dtoId.getName();
        this.parentCode = this.dtoId.getParentCode();
        this.sort = this.dtoId.getSort();
        this.type = this.dtoId.getType();
    }

    /**
     * (Constructor)
     *
     * @param dtoId         [唯一标识 <- DTO 对象]
     * @param resourceUrls  {@param dtoId} 关联的[（安全认证）资源 ←→ URL]集合
     *
     * @throws IllegalArgumentException
     */
    protected ResourceDto(final @NotNull SecurityResource dtoId, final @NotNull HashSet<SecurityResourceUrl> resourceUrls)
            throws IllegalArgumentException
    {
        if (null == dtoId || !dtoId.isEntityLegal()) {
            //-- 非法输入: [唯一标识 <- DTO 对象]
            throw new IllegalArgumentException(String.format("非法输入: <param>%s</param>->【%s】 <- %s"
                    , "[唯一标识 <- DTO 对象]"
                    , dtoId
                    , this.getClass().getSimpleName()));
        } else {
            this.dtoId = dtoId;
        }
        if (null == resourceUrls) {
            //-- 非法输入: 关联的[（安全认证）资源 ←→ URL]集合
            throw new IllegalArgumentException(String.format("非法输入: <param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "关联的[（安全认证）资源 ←→ URL]集合"
                    , resourceUrls
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        } else if (!resourceUrls.isEmpty()) {
            for (final SecurityResourceUrl eachResourceUrl : resourceUrls) {
                if (null == eachResourceUrl
                        || !eachResourceUrl.isEntityLegal()
                        || !eachResourceUrl.equals(this.dtoId))
                {
                    //-- 非法输入: 关联的[（安全认证）资源 ←→ URL]集合
                    throw new IllegalArgumentException(String.format("非法输入: <param>%s <- 【%s】</param>->【%s <- %s&%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                            , "关联的[（安全认证）资源 ←→ URL]集合"
                            , "关联的[（安全认证）资源 ←→ URL]"
                            , resourceUrls
                            , eachResourceUrl
                            , this.dtoId
                            , this.getClass().getName()
                            , Thread.currentThread().getStackTrace()[1].getMethodName()
                            , Thread.currentThread().getStackTrace()[1].getLineNumber()));
                }

                this.urlInfoSet.add(eachResourceUrl.getUrlInfo());
            }
        }

        this.id = this.dtoId.getId();
        this.code = this.dtoId.getCode();
        this.icon = this.dtoId.getIcon();
        this.link = this.dtoId.getLink();
        this.name = this.dtoId.getName();
        this.parentCode = this.dtoId.getParentCode();
        this.sort = this.dtoId.getSort();
        this.type = this.dtoId.getType();
    }

    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the <tt>dtoId()</tt>.
     */
    @Override
    public String id() {
        return this.dtoId.id();
    }

    /**
     * 是否符合业务要求 <- DTO 对象
     *
     * @return
     */
    @Override
    public boolean isDtoLegal() {
        return isEntityLegal();
    }

    /**
     * 是否符合业务要求 <- Entity 对象
     *
     * @Description 需要实现类实现该接口
     *
     * @return
     */
    @Override
    public boolean isEntityLegal() {
        return dtoId.isEntityLegal();
    }

    /**
     * 转换为 JSON 格式的字符串
     *
     * @return 转换结果
     */
    @Override
    public @NotNull String toJSONString() {
        return String.format("%s{code=\"%s\", name=\"%s\", type=%s}"
                , getClass().getSimpleName()
                , (null != this.code) ? this.code : ""
                , (null != this.name) ? this.name : ""
                , (null != this.type) ? this.type : "");
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public @NotNull String toString() {
        return DtoModel.toString(this);
    }

    //===== Factory =====//

    public enum Factory
            implements EntityFactoryModel<ResourceDto> {
        RESOURCE_DTO;

        /**
         * 创建 DTO
         *
         * @param resource  {@link SecurityResource}
         *
         * @return {@link ResourceDto}
         */
        @NotNull
        public ResourceDto create(@NotNull SecurityResource resource) {
            return new ResourceDto(resource);
        }

        /**
         * 创建 DTO
         *
         * @param resource      {@link SecurityResource}
         * @param urlInfoSet    关联的[URL 信息]集合
         * · 数据结构:
         * [{
         *      clientId : [资源服务器 ID],
         *      urlPath : [资源对应的 URL (Path部分)],
         *      urlMethod : [资源对应的 URL Method]
         * }]
         *
         * @return {@link ResourceDto}
         */
        @NotNull
        public ResourceDto create(@NotNull SecurityResource resource, @NotNull HashSet<Map<String, Object>> urlInfoSet) {
            if (null == urlInfoSet || urlInfoSet.isEmpty()) {
                //-- 非法输入: 关联的[URL 信息]集合
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param> -> 【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "关联的[URL 信息]集合"
                        , urlInfoSet
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            final @NotNull HashSet<SecurityResourceUrl> resourceUrlSet = new HashSet<>(0);
            for (Map<String, Object> eachUrlInfo : urlInfoSet) {
                resourceUrlSet.add(SecurityResourceUrl.Factory.RESOURCE_URL.create(resource.getCode()
                        , (String) eachUrlInfo.get("clientId")
                        , (String) eachUrlInfo.get("urlPath")
                        , (String) eachUrlInfo.get("urlMethod")));
            }

            return new ResourceDto(resource, resourceUrlSet);
        }

        /**
         * 创建 DTO
         *
         * @param code          角色编码
         * @param icon          图标
         * @param link          资源链接
         * @param name          角色名称
         * @param parentCode    父节点 <- 资源编码
         * @param sort          序号
         * @param type          资源类型
         *
         * @return {@link ResourceDto}
         */
        @NotNull
        public ResourceDto create(@NotNull String code
                , String icon
                , String link
                , @NotNull String name
                , @Nullable String parentCode
                , @NotNull int sort
                , @NotNull Resource.TypeVo type)
                throws IllegalArgumentException
        {
            final @NotNull SecurityResource resource = SecurityResource.Factory.RESOURCE.create(
                    code, icon, link
                    , name, parentCode, sort
                    , type);
            return new ResourceDto(resource);
        }

        /**
         * 更新 DTO
         *
         * @param id            数据 ID
         * @param code          角色编码
         * @param icon          图标
         * @param link          资源链接
         * @param name          角色名称
         * @param parentCode    父节点 <- 资源编码
         * @param sort          序号
         * @param type          资源类型
         *
         * @return {@link ResourceDto}
         *
         * @throws IllegalArgumentException
         *-> 此时 {@param id}/{@param code}/{@param icon}/{@param link}/{@param name}/{@param parentCode}/{@param sort}/{@param type} 非法
         */
        @NotNull
        public ResourceDto update(@NotNull Long id
                , @NotNull String code
                , String icon
                , String link
                , @NotNull String name
                , @Nullable String parentCode
                , @NotNull int sort
                , @NotNull Resource.TypeVo type)
                throws IllegalArgumentException
        {
            final @NotNull SecurityResource resource = SecurityResource.Factory.RESOURCE.update(
                    id, code, icon
                    , link, name, parentCode
                    , sort, type);
            return new ResourceDto(resource);
        }

        /**
         * 获取空对象
         *
         * @return 非 {@code null}.
         */
        @NotNull
        @Override
        public ResourceDto createDefault() {
            return new ResourceDto();
        }

    }

    //===== Getter And Setter =====//

    protected Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Resource.TypeVo getType() {
        return type;
    }

    public void setType(Resource.TypeVo type) {
        this.type = type;
    }

}
