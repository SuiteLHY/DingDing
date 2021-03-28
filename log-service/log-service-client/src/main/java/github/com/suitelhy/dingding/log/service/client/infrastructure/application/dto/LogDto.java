package github.com.suitelhy.dingding.log.service.client.infrastructure.application.dto;

import github.com.suitelhy.dingding.core.infrastructure.application.model.DtoModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.HandleType;
import github.com.suitelhy.dingding.core.infrastructure.util.Toolbox;
import github.com.suitelhy.dingding.log.service.api.domain.entity.Log;

import javax.validation.constraints.NotNull;

/**
 * 日志记录
 *
 * @see DtoModel
 */
public class LogDto
        implements DtoModel<Log, Long> {

    private static final long serialVersionUID = 1L;

    protected Long id;

    protected String description;

    protected String detail;

    protected String time;

    protected HandleType.LogVo type;

    protected String operatorIp;

    protected String operatorUsername;

    protected String targetId;

    private final transient @NotNull Log dtoId;

    //===== Factory =====//

    public enum Factory {
        DEFAULT;

        /**
         * 创建
         *
         * @param log   {@linkplain Log 日志记录}
         *
         * @return {@linkplain LogDto 日志记录}
         *
         * @throws IllegalArgumentException {@param log} maybe {@literal null} or {@code isEmpty()}
         */
        public @NotNull LogDto create(@NotNull Log log)
                throws IllegalArgumentException
        {
            if (null == log || log.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                        , "日志记录"
                        , log
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new LogDto(log);
        }

        /**
         * 创建默认对象
         *
         * @Description 空对象.
         *
         * @return {@linkplain LogDto 日志记录}
         */
        public @NotNull LogDto createDefault() {
            return new LogDto();
        }

    }

    //===== Constructor =====//

    /**
     * (Constructor)
     *
     * @Description 用于构造空对象.
     */
    protected LogDto() {
        this.dtoId = Log.Factory.User.LOG.createDefault();
    }

    /**
     * (Constructor)
     *
     * @param dtoId {@link Log}
     *
     * @throws IllegalArgumentException 此时 {@param dtoId} 非法.
     */
    protected LogDto(@NotNull Log dtoId)
            throws IllegalArgumentException
    {
        if (null == dtoId || dtoId.isEmpty()) {
            //-- 非法输入: <param>dtoId</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                    , "dtoId"
                    , dtoId
                    , this.getClass().getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        this.dtoId = dtoId;

        this.id = this.dtoId.getId();
        this.description = this.dtoId.getDescription();
        this.detail = this.dtoId.getDetail();
        this.time = this.dtoId.getTime();
        this.type = this.dtoId.getType();
        this.operatorIp = this.dtoId.getOperatorIp();
        this.operatorUsername = this.dtoId.getOperatorUsername();
        this.targetId = this.dtoId.getTargetId();
    }

    //===== DtoModel =====//

    /**
     * 唯一标识 <- Entity 对象
     *
     * @return The unique identify of the <tt>dtoId()</tt>.
     */
    @Override
    public Long id() {
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
     * 是否符合业务要求 <- {@link EntityModel} 实例
     *
     * @Description 需要实现类实现该接口.
     *
     * @return 判断结果
     */
    @Override
    public boolean isEntityLegal() {
        return dtoId.isEntityLegal();
    }

    /**
     * 是否无效 <- DTO 对象
     *
     * @Description 使用默认实现.
     *
     * @return 判断结果
     */
    @Override
    public boolean isEmpty() {
        return DtoModel.isEmpty(this);
    }

    /**
     * 转换为 JSON 格式的字符串
     *
     * @return 转换结果
     */
    @Override
    public @NotNull String toJSONString() {
        if (! isEmpty()) {
            return String.format("{\"_class_name\":\"%s\", \"id\":\"%s\", \"description\":\"%s\", \"detail\":\"%s\", \"time\":\"%s\", \"type\":\"%s\", \"operatorIp\":\"%s\", \"operatorUsername\":\"%s\", \"targetId\":\"%s\"}"
                    , getClass().getSimpleName()
                    , Toolbox.StringUtil.getInstance().notNull(this.id)
                    , Toolbox.StringUtil.getInstance().notNull(this.description)
                    , Toolbox.StringUtil.getInstance().notNull(this.detail)
                    , Toolbox.StringUtil.getInstance().notNull(this.time)
                    , Toolbox.StringUtil.getInstance().notNull((null != this.type) ? this.type.name : "")
                    , Toolbox.StringUtil.getInstance().notNull(this.operatorIp)
                    , Toolbox.StringUtil.getInstance().notNull(this.operatorUsername)
                    , Toolbox.StringUtil.getInstance().notNull(this.targetId));
        }
        return String.format("{\"_class_name\":\"%s\"}", getClass().getSimpleName());
    }

    @Override
    public @NotNull String toString() {
        return this.toJSONString();
    }

    //==========//

}
