package github.com.suitelhy.dingding.core.infrastructure.application;

import github.com.suitelhy.dingding.core.infrastructure.domain.Page;
import github.com.suitelhy.dingding.core.infrastructure.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Basic {@code Page} implementation.
 *
 * @Description 支持 RPC 框架的序列化及其通过反射注入对象的方式.
 *
 * @param <T> the type of which the page consists.
 *
 * @see org.springframework.data.domain.PageImpl
 */
public class PageImpl<T>
        extends github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl<T> {

    private static final long serialVersionUID = 2L;

    private static final PageImpl<?> EMPTY_INSTANCE = new PageImpl<>(/*Collections.emptyList()*/);

    public enum Factory {
        DEFAULT;

        /**
         * 创建
         *
         * @param content   the content of this page, must not be {@literal null}.
         * @param pageable  the paging information, must not be {@literal null}.
         * @param total     the total amount of items available.
         *                  The total might be adapted considering the length of the content given, if it is going to be the content of the last page.
         *                  This is in place to mitigate inconsistencies.
         *
         * @param <T>       the element type of <tt>content</tt>
         *
         * @return {@linkplain PageImpl 分页对象}
         *
         * @throws IllegalArgumentException 此时<tt>content</tt> | <tt>pageable</tt>非法
         */
        @NotNull
        public <T> PageImpl<T> create(@NotNull List<T> content, @NotNull Pageable pageable, long total)
                throws IllegalArgumentException
        {
            if (null == content) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "当前页内容"
                        , content
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (null == pageable) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "分页信息"
                        , pageable
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new PageImpl<>(content, pageable, (total < content.size()) ? content.size() : total);
        }

        /**
         * 创建
         *
         * @param content   当前页内容; 应该为<tt>page</tt>的内容的加工数据.
         * @param page      分页对象, 其内容应该为<tt>content</tt>对应的原始数据.
         *
         * @param <T>       <tt>content</tt>的成员类型
         *
         * @return {@linkplain PageImpl 分页对象}
         *
         * @throws IllegalArgumentException 此时<tt>content</tt> | <tt>page</tt>非法
         */
        public <T> PageImpl<T> create(@NotNull List<T> content, @NotNull Page<?> page)
                throws IllegalArgumentException
        {
            if (null == content) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "当前页内容"
                        , content
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (null == page || page.isEmpty()) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "分页对象"
                        , page
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new PageImpl<>(content
                    , page.getPageable()
                    , (page.getTotalElements() < content.size()) ? content.size() : page.getTotalElements());
        }

        /**
         * 创建
         *
         * @param page  分页对象
         *
         * @param <T>   <tt>page</tt>的成员类型
         *
         * @return {@linkplain PageImpl 分页对象}
         *
         * @throws IllegalArgumentException 此时<tt>page</tt>非法
         */
        public <T> PageImpl<T> create(@NotNull Page<T> page)
                throws IllegalArgumentException
        {
            if (null == page) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "分页对象"
                        , page
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new PageImpl<>(page.getContent(), page.getPageable(), page.getTotalElements());
        }

        /**
         * 创建默认实例
         *
         * @return {@linkplain PageImpl 空对象}
         */
        @NotNull
        public PageImpl<?> createDefault() {
            return PageImpl.EMPTY_INSTANCE;
        }

    }

    /**
     * Constructor of {@code PageImpl}.
     *
     * @param content the content of this page, must not be {@literal null}.
     * @param pageable the paging information, must not be {@literal null}.
     * @param total the total amount of items available. The total might be adapted considering the length of the content
     *          given, if it is going to be the content of the last page. This is in place to mitigate inconsistencies.
     */
    protected PageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    /**
     * Creates a new {@link github.com.suitelhy.dingding.core.infrastructure.domain.PageImpl} with the given content. This will result in the created {@link Page} being identical
     * to the entire {@link List}.
     *
     * @param content must not be {@literal null}.
     */
    protected PageImpl(List<T> content) {
        super(content);
    }

    /**
     * (Constructor)
     *
     * @Description 用于序列化和反序列化.
     */
    protected PageImpl() {
        super();
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    /**
     * Get the JSON format string of <tt>this</tt>
     *
     * @return {@linkplain String JSON format string}
     */
    @Override
    public @NotNull String toJSONString() {
        return String.format("{\"_class_name\":\t\"%s\",\"totalElements\":\t%d,\t\"totalPages\":\t%d,\t\"pageSize\":\t%d,\t\"pageNumber\":\t%d,\t\"sort\":\t%s,\t\"content\":\t%s,\t\"numberOfElements\":\t%d,\t\"isFirst\":\t%b,\t\"isLast\":\t%b}"
                , getClass().getSimpleName()
                , this.getTotalElements()
                , this.getTotalPages()
                , this.getSize()
                , this.getNumber()
                , this.getSort()
                , this.getContent()
                , this.getNumberOfElements()
                , this.isFirst()
                , this.isLast());
    }

}
