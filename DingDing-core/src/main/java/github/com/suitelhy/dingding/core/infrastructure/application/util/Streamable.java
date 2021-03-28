package github.com.suitelhy.dingding.core.infrastructure.application.util;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.data.util.StreamUtils;
//import org.springframework.util.Assert;

/**
 * Simple interface to ease streamability of {@link Iterable}s.
 *
 * @Description Task 层使用.
 *
 * @Editor Suite
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 *
 * @since 2.0
 *
 * @see org.springframework.data.util.Streamable
 */
@FunctionalInterface
public interface Streamable<T>
        extends Iterable<T>, Supplier<Stream<T>> {

    /**
     * Returns an empty {@link Streamable}.
     *
     * @return will never be {@literal null}.
     */
    static <T> Streamable<T> empty() {
        return Collections::emptyIterator;
    }

    /**
     * Returns a {@link Streamable} with the given elements.
     *
     * @param t the elements to return.
     *
     * @return
     */
    @SafeVarargs
    static <T> Streamable<T> of(T... t) {
        return () -> Arrays.asList(t).iterator();
    }

    /**
     * Returns a {@link Streamable} for the given {@link Iterable}.
     *
     * @param iterable must not be {@literal null}.
     *
     * @return
     */
    static <T> Streamable<T> of(@NotNull Iterable<T> iterable) {

//        Assert.notNull(iterable, "Iterable must not be null!");
        if (null == iterable) {
            //-- 非法输入: <param>iterable</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "iterable"
                    , "Iterable must not be null"
                    , Streamable.class.getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return iterable::iterator;
    }

    static <T> Streamable<T> of(Supplier<? extends Stream<T>> supplier) {
        return LazyStreamable.of(supplier);
    }

    /**
     * Creates a non-parallel {@link Stream} of the underlying {@link Iterable}.
     *
     * @return will never be {@literal null}.
     */
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns a new {@link Streamable} that will apply the given {@link Function} to the current one.
     *
     * @param mapper must not be {@literal null}.
     *
     * @return
     *
     * @see Stream#map(Function)
     */
    default <R> Streamable<R> map(Function<? super T, ? extends R> mapper) {

//        Assert.notNull(mapper, "Mapping function must not be null!");
        if (null == mapper) {
            //-- 非法输入: <param>iterable</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "mapper"
                    , "Mapping function must not be null"
                    , Streamable.class.getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return Streamable.of(() -> stream().map(mapper));
    }

    /**
     * Returns a new {@link Streamable} that will apply the given {@link Function} to the current one.
     *
     * @param mapper must not be {@literal null}.
     *
     * @return
     *
     * @see Stream#flatMap(Function)
     */
    default <R> Streamable<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {

//        Assert.notNull(mapper, "Mapping function must not be null!");
        if (null == mapper) {
            //-- 非法输入: <param>iterable</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "mapper"
                    , "Mapping function must not be null"
                    , Streamable.class.getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return Streamable.of(() -> stream().flatMap(mapper));
    }

    /**
     * Returns a new {@link Streamable} that will apply the given filter {@link Predicate} to the current one.
     *
     * @param predicate must not be {@literal null}.
     * @return
     * @see Stream#filter(Predicate)
     */
    default Streamable<T> filter(Predicate<? super T> predicate) {

//        Assert.notNull(predicate, "Filter predicate must not be null!");
        if (null == predicate) {
            //-- 非法输入: <param>iterable</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "predicate"
                    , "Filter predicate must not be null"
                    , Streamable.class.getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return Streamable.of(() -> stream().filter(predicate));
    }

    /**
     * Returns whether the current {@link Streamable} is empty.
     *
     * @return
     */
    default boolean isEmpty() {
        return !iterator().hasNext();
    }

    /**
     * Creates a new {@link Streamable} from the current one and the given {@link Stream} concatenated.
     *
     * @param stream must not be {@literal null}.
     * @return
     * @since 2.1
     */
    default Streamable<T> and(Supplier<? extends Stream<? extends T>> stream) {

//        Assert.notNull(stream, "Stream must not be null!");
        if (null == stream) {
            //-- 非法输入: <param>iterable</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "stream"
                    , "Stream must not be null"
                    , Streamable.class.getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return Streamable.of(() -> Stream.concat(this.stream(), stream.get()));
    }

    /**
     * Creates a new {@link Streamable} from the current one and the given values concatenated.
     *
     * @param others must not be {@literal null}.
     *
     * @return will never be {@literal null}.
     *
     * @since 2.2
     */
    @SuppressWarnings("unchecked")
    default Streamable<T> and(T... others) {

//        Assert.notNull(others, "Other values must not be null!");
        if (null == others) {
            //-- 非法输入: <param>iterable</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "others"
                    , "Other must not be null"
                    , Streamable.class.getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return Streamable.of(() -> Stream.concat(this.stream(), Arrays.stream(others)));
    }

    /**
     * Creates a new {@link Streamable} from the current one and the given {@link Iterable} concatenated.
     *
     * @param iterable must not be {@literal null}.
     *
     * @return will never be {@literal null}.
     *
     * @since 2.2
     */
    default Streamable<T> and(Iterable<? extends T> iterable) {

//        Assert.notNull(iterable, "Iterable must not be null!");
        if (null == iterable) {
            //-- 非法输入: <param>iterable</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "iterable"
                    , "Iterable must not be null"
                    , Streamable.class.getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return Streamable.of(() -> Stream.concat(this.stream(), StreamSupport.stream(iterable.spliterator(), false)));
    }

    /**
     * Convenience method to allow adding a {@link Streamable} directly as otherwise the invocation is ambiguous between
     * {@link #and(Iterable)} and {@link #and(Supplier)}.
     *
     * @param streamable must not be {@literal null}.
     *
     * @return will never be {@literal null}.
     *
     * @since 2.2
     */
    default Streamable<T> and(Streamable<? extends T> streamable) {
        return and((Supplier<? extends Stream<? extends T>>) streamable);
    }

    /**
     * Creates a new, unmodifiable {@link List}.
     *
     * @return will never be {@literal null}.
     *
     * @since 2.2
     */
    default List<T> toList() {
        return stream().collect(StreamUtils.toUnmodifiableList());
    }

    /**
     * Creates a new, unmodifiable {@link Set}.
     *
     * @return will never be {@literal null}.
     *
     * @since 2.2
     */
    default Set<T> toSet() {
        return stream().collect(StreamUtils.toUnmodifiableSet());
    }

    /*
     * (non-Javadoc)
     * @see java.util.function.Supplier#get()
     */
    default Stream<T> get() {
        return stream();
    }

    /**
     * A collector to easily produce a {@link Streamable} from a {@link Stream} using {@link Collectors#toList} as
     * intermediate collector.
     *
     * @return
     *
     * @see #toStreamable(Collector)
     *
     * @since 2.2
     */
    public static <S> Collector<S, ?, Streamable<S>> toStreamable() {
        return toStreamable(Collectors.toList());
    }

    /**
     * A collector to easily produce a {@link Streamable} from a {@link Stream} and the given intermediate collector.
     *
     * @return
     *
     * @since 2.2
     */
    @SuppressWarnings("unchecked")
    public static <S, T extends Iterable<S>> Collector<S, ?, Streamable<S>>
    toStreamable(Collector<S, ?, T> intermediate)
    {
        return Collector.of( //
                (Supplier<T>) intermediate.supplier(), //
                (BiConsumer<T, S>) intermediate.accumulator(), //
                (BinaryOperator<T>) intermediate.combiner(), //
                Streamable::of);
    }

}
