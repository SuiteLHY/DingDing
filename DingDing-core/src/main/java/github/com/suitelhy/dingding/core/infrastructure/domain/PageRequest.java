package github.com.suitelhy.dingding.core.infrastructure.domain;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * Basic Java Bean implementation of {@code Pageable}.
 *
 * @Description 支持 RPC 框架的序列化及其通过反射注入对象的方式.
 *
 * @Editor Suite
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 *
 * @see org.springframework.data.domain.PageRequest
 */
public class PageRequest
        extends AbstractPageRequest {

    private static final long serialVersionUID = -4541509938956089562L;

    private @NotNull Sort sort;

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param page zero-based page index, must not be negative.
     * @param size the size of the page to be returned, must be greater than 0.
     * @param sort must not be {@literal null}, use {@link Sort#unsorted()} instead.
     */
    protected PageRequest(int page, int size, Sort sort) {

        super(page, size);

        this.sort = (null != sort)
                ? sort
                : Sort.unsorted();
    }

    /**
     * Creates a new unsorted {@link PageRequest}.
     *
     * @param page zero-based page index, must not be negative.
     * @param size the size of the page to be returned, must be greater than 0.
     *
     * @since 2.0
     */
    public static PageRequest of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     * @param sort must not be {@literal null}, use {@link Sort#unsorted()} instead.
     *
     * @since 2.0
     */
    public static PageRequest of(int page, int size, Sort sort) {
        return new PageRequest(page, size, sort);
    }

    /**
     * Creates a new {@link PageRequest} with sort direction and properties applied.
     *
     * @param page zero-based page index, must not be negative.
     * @param size the size of the page to be returned, must be greater than 0.
     * @param direction must not be {@literal null}.
     * @param properties must not be {@literal null}.
     *
     * @since 2.0
     */
    public static PageRequest of(int page, int size, Sort.Direction direction, String... properties) {
        return of(page, size, Sort.by(direction, properties));
    }

    /*
     * (non-Javadoc)
     * @see Pageable#getSort()
     */
    public Sort getSort() {
        return Sort.toSort(sort);
    }

    /*
     * (non-Javadoc)
     * @see Pageable#next()
     */
    @Override
    public Pageable next() {
        return new PageRequest(getPageNumber() + 1, getPageSize(), getSort());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.AbstractPageRequest#previous()
     */
    @Override
    public PageRequest previous() {
        return (getPageNumber() == 0)
                ? this
                : new PageRequest(getPageNumber() - 1, getPageSize(), getSort());
    }

    /*
     * (non-Javadoc)
     * @see Pageable#first()
     */
    @Override
    public Pageable first() {
        return new PageRequest(0, getPageSize(), getSort());
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

        if (! (obj instanceof PageRequest)) {
            return false;
        }

        PageRequest that = (PageRequest) obj;

        return super.equals(that)
                && this.sort.equals(that.sort);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + sort.hashCode();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        /*return String.format("Page request [number: %d, size %d, sort: %s]", getPageNumber(), getPageSize(), sort);*/
        return toJSONString();
    }

    /**
     * Get the JSON format string of <tt>this</tt>
     *
     * @return {@linkplain String JSON format string}
     */
    @Override
    public @NotNull String toJSONString() {
        return String.format("{\"_class_name\":\t\"%s\",\t\"number\":\t%d,\t\"size\":\t%d,\t\"sort\":\t%s}"
                , getClass().getSimpleName()
                , getPageNumber()
                , getPageSize()
                , sort.toJSONString());
    }

}
