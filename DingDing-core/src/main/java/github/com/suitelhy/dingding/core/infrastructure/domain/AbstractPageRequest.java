package github.com.suitelhy.dingding.core.infrastructure.domain;

import java.io.Serializable;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * Abstract Java Bean implementation of {@code Pageable}.
 *
 * @Description 支持 RPC 框架的序列化及其通过反射注入对象的方式.
 *
 * @Editor Suite
 *
 * @author Thomas Darimont
 * @author Oliver Gierke
 * @author Alex Bondarev
 *
 * @see org.springframework.data.domain.AbstractPageRequest
 */
public abstract class AbstractPageRequest
        implements Pageable, Serializable {

    private static final long serialVersionUID = 1232825578694716871L;

    private final int page;

    private final int size;

    /**
     * Creates a new {@link AbstractPageRequest}. Pages are zero indexed, thus providing 0 for {@code page} will return
     * the first page.
     *
     * @param page must not be less than zero.
     * @param size must not be less than one.
     */
    public AbstractPageRequest(int page, int size) {

        this.page = page < 0
                ? 0
                : page;
        this.size = size < 1
                ? 1
                : size;
    }

    protected AbstractPageRequest() {
        this(0,1);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getPageSize()
     */
    public int getPageSize() {
        return size;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getPageNumber()
     */
    public int getPageNumber() {
        return page;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getOffset()
     */
    public long getOffset() {
        return (long) page * (long) size;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#hasPrevious()
     */
    public boolean hasPrevious() {
        return page > 0;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#previousOrFirst()
     */
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#next()
     */
    public abstract Pageable next();

    /**
     * Returns the {@link Pageable} requesting the previous {@link Page}.
     *
     * @return
     */
    public abstract Pageable previous();

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#first()
     */
    public abstract Pageable first();

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;

        result = prime * result + page;
        result = prime * result + size;

        return result;
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

        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }

        AbstractPageRequest other = (AbstractPageRequest) obj;
        return this.page == other.page
                && this.size == other.size;
    }

    /**
     * Get the JSON format string of <tt>this</tt>
     *
     * @return {@linkplain String JSON format string}
     */
    @NotNull
    public String toJSONString() {
        return toString();
    }

}
