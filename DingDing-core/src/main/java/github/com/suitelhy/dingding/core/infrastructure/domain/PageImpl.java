package github.com.suitelhy.dingding.core.infrastructure.domain;

import java.util.List;
import java.util.function.Function;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

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
        extends Chunk<T>
        implements Page<T> {

    private static final long serialVersionUID = 867755909294344406L;

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
         *                  缺省值为{@link Page#empty()}
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
            if (null == page/* || page.isEmpty()*/) {
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
         * @param content   当前页内容; 应该为<tt>page</tt>的内容的加工数据.
         * @param page      {@linkplain org.springframework.data.domain.Page 分页对象}, 其内容应该为<tt>content</tt>对应的原始数据.
         *                  缺省值为{@link Page#empty()}
         *
         * @param <T>       <tt>content</tt>的成员类型
         *
         * @return {@linkplain PageImpl 分页对象}
         *
         * @throws IllegalArgumentException 此时<tt>content</tt> | <tt>page</tt>非法
         */
        public <T> PageImpl<T> create(@NotNull List<T> content, @NotNull org.springframework.data.domain.Page<?> page)
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
            if (null == page/* || page.isEmpty()*/) {
                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "分页对象"
                        , page
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }
            if (content.size() != page.getNumberOfElements()) {
                throw new IllegalArgumentException(String.format("非法参数:<description>【%s ←→ %s】-【%s】</description>->【%s】&【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
                        , "当前页内容"
                        , "分页对象"
                        , "[当前页内容]的成员数量与[分页对象]的当前页元素数量不一致"
                        , content
                        , page
                        , this.getClass().getName()
                        , Thread.currentThread().getStackTrace()[1].getMethodName()
                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
            }

            return new PageImpl<>(content
                    , page.getPageable().isPaged()
                        ? PageRequest.of(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), new Sort(page.getPageable().getSort()))
                        : Pageable.unpaged()
                    , page.getTotalElements());
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
         * 创建
         *
         * @param page  {@linkplain org.springframework.data.domain.Page 分页对象}
         *
         * @param <T>   <tt>page</tt>的成员类型
         *
         * @return {@linkplain PageImpl 分页对象}
         *
         * @throws IllegalArgumentException 此时<tt>page</tt>非法
         */
        public <T> PageImpl<T> create(@NotNull org.springframework.data.domain.Page<T> page)
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

            return new PageImpl<>(page.getContent()
                , page.getPageable().isPaged()
                    ? PageRequest.of(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), new Sort(page.getPageable().getSort()))
                    : Pageable.unpaged()
                , page.getTotalElements());
        }

//        /**
//         * 创建
//         *
//         * @param page      {@linkplain org.springframework.data.domain.PageImpl 分页对象}
//         * @param pageable  {@linkplain org.springframework.data.domain.Pageable 分页信息}, <tt>page</tt>对应的{@link org.springframework.data.domain.PageImpl#getPageable()}
//         *
//         * @param <T>       <tt>page</tt>的成员类型
//         *
//         * @return {@linkplain PageImpl 分页对象}
//         *
//         * @throws IllegalArgumentException 此时 <tt>page</tt> | <tt>pageable</tt> 非法
//         */
//        public <T> PageImpl<T> create(@NotNull org.springframework.data.domain.PageImpl<T> page, @NotNull org.springframework.data.domain.Pageable pageable)
//                throws IllegalArgumentException
//        {
//            if (null == page) {
//                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , "分页对象"
//                        , page
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//            if (null == pageable) {
//                throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= [<class>%s</class>-<method>%s</method> <- 第%s行]"
//                        , "分页信息"
//                        , pageable
//                        , this.getClass().getName()
//                        , Thread.currentThread().getStackTrace()[1].getMethodName()
//                        , Thread.currentThread().getStackTrace()[1].getLineNumber()));
//            }
//
//            return new PageImpl<>(page, pageable);
//        }

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

    protected /*final */long total;

    /**
     * Constructor of {@code PageImpl}.
     *
     * @param content the content of this page, must not be {@literal null}.
     * @param pageable the paging information, must not be {@literal null}.
     * @param total the total amount of items available. The total might be adapted considering the length of the content
     *          given, if it is going to be the content of the last page. This is in place to mitigate inconsistencies.
     */
    /*public*/protected PageImpl(List<T> content, Pageable pageable, long total) {

        super(content, pageable);

        this.total = pageable.toOptional().filter(it -> ! content.isEmpty())//
                .filter(it -> it.getOffset() + it.getPageSize() > total)//
                .map(it -> it.getOffset() + content.size())//
                .orElse(total);
    }

    /**
     * Creates a new {@link PageImpl} with the given content. This will result in the created {@link Page} being identical
     * to the entire {@link List}.
     *
     * @param content must not be {@literal null}.
     */
    /*public*/protected PageImpl(List<T> content) {
        this(content, Pageable.unpaged(), null == content ? 0 : content.size());
    }

//    /**
//     * (Constructor)
//     *
//     * @param content
//     * @param page
//     */
//    protected PageImpl(@NotNull List<T> content, @NotNull org.springframework.data.domain.Page<?> page) {
//        this(content
//                , page.getPageable().isPaged()
//                        ? PageRequest.of(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), new Sort(page.getPageable().getSort()))
//                        : Pageable.unpaged()
//                , page.getTotalElements());
//    }

//    /**
//     * (Constructor)
//     *
//     * @param page
//     * @param pageable
//     */
//    /*public*/protected PageImpl(@NotNull org.springframework.data.domain.PageImpl<T> page, @NotNull org.springframework.data.domain.Pageable pageable) {
//        this(page.getContent()
//                , /*page.getPageable()*/(null == pageable || ! pageable.isPaged()) ? Pageable.unpaged() : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(pageable.getSort()))
//                , page.getTotalElements());
//    }

    /**
     * (Constructor)
     *
     * @Description 用于序列化和反序列化.
     */
    protected PageImpl() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#getTotalPages()
     */
    @Override
    public int getTotalPages() {
        return getSize() == 0
                ? 1
                : (int) Math.ceil((double) total / (double) getSize());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#getTotalElements()
     */
    @Override
    public long getTotalElements() {
        return total;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#hasNext()
     */
    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#isLast()
     */
    @Override
    public boolean isLast() {
        return ! hasNext();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#transform(org.springframework.core.convert.converter.Converter)
     */
    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new PageImpl<>(getConvertedContent(converter), getPageable(), total);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        String contentType = "UNKNOWN";
        List<T> content = getContent();

        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Page %s of %d containing %s instances", getNumber() + 1, getTotalPages(), contentType);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(@Nullable Object obj) {

        if (this == obj) {
            return true;
        }

        if (! (obj instanceof PageImpl<?>)) {
            return false;
        }

        PageImpl<?> that = (PageImpl<?>) obj;

        return this.total == that.total
                && super.equals(obj);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result += 31 * (int) (total ^ total >>> 32);
        result += 31 * super.hashCode();

        return result;
    }

}
