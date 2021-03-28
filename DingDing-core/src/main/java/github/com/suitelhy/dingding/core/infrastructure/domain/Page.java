package github.com.suitelhy.dingding.core.infrastructure.domain;

import java.util.Collections;
import java.util.function.Function;

import github.com.suitelhy.dingding.core.infrastructure.application.PageImpl;

import javax.validation.constraints.NotNull;

/**
 * A page is a sublist of a list of objects.
 * It allows gain information about the position of it in the containing entire list.
 *
 * @Description 分页结果所对应的接口.
 *
 * @Editor Suite
 *
 * @see org.springframework.data.domain.Page
 */
public interface Page<T>
        /*extends org.springframework.data.domain.Page<T>*/extends Slice<T> {

    /**
     * Creates a new empty {@link Page}.
     *
     * @return
     *
     * @since 2.0
     */
    static <T> Page<T> empty() {
        /*return empty(Pageable.unpaged());*/
        return (Page<T>) PageImpl.Factory.DEFAULT.createDefault();
    }

    /**
     * Creates a new empty {@link Page} for the given {@link Pageable}.
     *
     * @param pageable must not be {@literal null}.
     *
     * @return
     *
     * @since 2.0
     */
    static <T> Page<T> empty(Pageable pageable) {
        return PageImpl.Factory.DEFAULT.create(Collections.emptyList(), pageable, 0);
    }

    /**
     * Returns the number of total pages.
     *
     * @return the number of total pages
     */
    int getTotalPages();

    /**
     * Returns the total amount of elements.
     *
     * @return the total amount of elements
     */
    long getTotalElements();

    /**
     * Returns a new {@link Page} with the content of the current one mapped by the given {@link Function}.
     *
     * @param converter must not be {@literal null}.
     *
     * @return a new {@link Page} with the content of the current one mapped by the given {@link Function}.
     *
     * @since 1.10
     */
    <U> Page<U> map(Function<? super T, ? extends U> converter);

    /**
     * Get the JSON format string of <tt>this</tt>
     *
     * @return {@linkplain String JSON format string}
     */
    @NotNull
    default String toJSONString() {
        return String.format("{\"_class_name\":\t\"%s\",\t\"totalElements\":\t%d,\t\"totalPages\":\t%d}"
                , Page.class.getSimpleName()
                , getTotalElements()
                , getTotalPages());
    }

}
