package github.com.suitelhy.dingding.core.infrastructure.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;

/**
 * A chunk of data restricted by the configured {@link Pageable}.
 *
 * @Description 支持 RPC 框架的序列化及其通过反射注入对象的方式.
 *
 * @Editor Suite
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @since 1.8
 *
 * @see org.springframework.data.domain.Chunk
 */
public abstract class Chunk<T>
        implements Slice<T>, Serializable {

    private static final long serialVersionUID = 867755909294344406L;

    /*private*/protected final @NotNull List<T> content = new ArrayList<>(0);

    /*private*/protected @NotNull Pageable pageable;

    /**
     * Creates a new {@link Chunk} with the given content and the given governing {@link Pageable}.
     *
     * @param content must not be {@literal null}.
     * @param pageable must not be {@literal null}.
     */
    public Chunk(List<T> content, Pageable pageable) {

        Assert.notNull(content, "Content must not be null!");
        Assert.notNull(pageable, "Pageable must not be null!");

        this.content.addAll(content);
        /*this.pageable = pageable;*/
        this.pageable = PageRequest.of(pageable.getPageNumber()
                , pageable.getPageSize()
                , pageable.getSort());
    }

    /**
     * (Constructor)
     *
     * @Description 用于序列化和反序列化.
     */
    protected Chunk() {
        this.pageable = Pageable.unpaged();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getNumber()
     */
    public int getNumber() {
        return /*pageable.isPaged()*/! isEmpty()
                ? pageable.getPageNumber()
                : 0;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getSize()
     */
    public int getSize() {
        return /*pageable.isPaged()*/! isEmpty()
                ? pageable.getPageSize()
                : content.size();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getNumberOfElements()
     */
    public int getNumberOfElements() {
        return content.size();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#hasPrevious()
     */
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    /**
     * 判断 {@linkplain Chunk 调用者} 是否合法.
     *
     * @return {@linkplain Boolean#TYPE 判断结果}
     */
    public boolean isEmpty() {
        return null == this.pageable
                || ! this.pageable.isPaged();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#isFirst()
     */
    public boolean isFirst() {
        return ! hasPrevious();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#isLast()
     */
    public boolean isLast() {
        return ! hasNext();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#nextPageable()
     */
    public Pageable nextPageable() {
        return hasNext()
                ? pageable.next()
                : Pageable.unpaged();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#previousPageable()
     */
    public Pageable previousPageable() {
        return hasPrevious()
                ? pageable.previousOrFirst()
                : Pageable.unpaged();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#hasContent()
     */
    public boolean hasContent() {
        return ! content.isEmpty();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getContent()
     */
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getSort()
     */
    @Override
    public /*org.springframework.data.domain.Sort*/Sort getSort() {
        /*return pageable.getSort();*/
        return ! isEmpty()
                ? pageable.getSort()
                : /*org.springframework.data.domain.Sort*/Sort.unsorted();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<T> iterator() {
        return content.iterator();
    }

    /**
     * Applies the given {@link Function} to the content of the {@link Chunk}.
     *
     * @param converter must not be {@literal null}.
     *
     * @return
     */
    protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {

        Assert.notNull(converter, "Function must not be null!");

        return this.stream()
                .map(converter::apply)
                .collect(Collectors.toList());
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

        if (! (obj instanceof Chunk<?>)) {
            return false;
        }

        Chunk<?> that = (Chunk<?>) obj;

        boolean contentEqual = this.content.equals(that.content);
        boolean pageableEqual = this.pageable.equals(that.pageable);

        return contentEqual && pageableEqual;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result += 31 * pageable.hashCode();
        result += 31 * content.hashCode();

        return result;
    }

    //===== Getter Method =====//

    @Override
    public @NotNull Pageable getPageable() {
        return ! isEmpty()
                ? pageable
                : Pageable.unpaged();
    }

}
