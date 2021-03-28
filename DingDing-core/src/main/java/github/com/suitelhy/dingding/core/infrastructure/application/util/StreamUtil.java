package github.com.suitelhy.dingding.core.infrastructure.application.util;

import static java.util.stream.Collectors.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.data.util.CloseableIterator;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

/**
 * Spring Data specific Java {@link Stream} utility methods and classes.
 *
 * @Editor Suite
 *
 * @author Thomas Darimont
 * @author Oliver Gierke
 *
 * @since 1.10
 *
 * @see org.springframework.data.util.StreamUtils
 */
public enum StreamUtil {
    DEFAULT;

    /**
     * Returns a {@link Stream} backed by the given {@link Iterator}
     *
     * @param iterator must not be {@literal null}.
     * @return
     */
    public <T> Stream<T> createStreamFromIterator(Iterator<T> iterator) {

        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.NONNULL);
        return StreamSupport.stream(spliterator, false);
    }

    /**
     * Returns a {@link Stream} backed by the given {@link CloseableIterator} and forwarding calls to
     * {@link Stream#close()} to the iterator.
     *
     * @param iterator must not be {@literal null}.
     *
     * @return
     *
     * @since 2.0
     */
    public <T> Stream<T> createStreamFromIterator(CloseableIterator<T> iterator) {

//        Assert.notNull(iterator, "Iterator must not be null!");
        if (null == iterator) {
            //-- 非法输入: <param>iterable</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "iterator"
                    , "Iterable must not be null"
                    , Streamable.class.getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        return createStreamFromIterator((Iterator<T>) iterator).onClose(() -> iterator.close());
    }

    /**
     * Returns a {@link Collector} to create an unmodifiable {@link List}.
     *
     * @return will never be {@literal null}.
     */
    public <T> Collector<T, ?, List<T>> toUnmodifiableList() {
        return collectingAndThen(toList(), Collections::unmodifiableList);
    }

    /**
     * Returns a {@link Collector} to create an unmodifiable {@link Set}.
     *
     * @return will never be {@literal null}.
     */
    public <T> Collector<T, ?, Set<T>> toUnmodifiableSet() {
        return collectingAndThen(toSet(), Collections::unmodifiableSet);
    }

    /**
     * Returns a {@link Collector} to create a {@link MultiValueMap}.
     *
     * @param keyFunction {@link Function} to create a key from an element of the {@link java.util.stream.Stream}
     * @param valueFunction {@link Function} to create a value from an element of the {@link java.util.stream.Stream}
     */
    public <T, K, V> Collector<T, MultiValueMap<K, V>, MultiValueMap<K, V>> toMultiMap(Function<T, K> keyFunction,
                                                                                       Function<T, V> valueFunction) {
        return MultiValueMapCollector.of(keyFunction, valueFunction);
    }

    /**
     * Creates a new {@link Stream} for the given value returning an empty {@link Stream} if the value is {@literal null}.
     *
     * @param source can be {@literal null}.
     * @return a new {@link Stream} for the given value returning an empty {@link Stream} if the value is {@literal null}.
     * @since 2.0.6
     */
    public static <T> Stream<T> fromNullable(@Nullable T source) {
        return source == null ? Stream.empty() : Stream.of(source);
    }

    /**
     * Zips the given {@link Stream}s using the given {@link BiFunction}. The resulting {@link Stream} will have the
     * length of the shorter of the two, abbreviating the zipping when the shorter of the two {@link Stream}s is
     * exhausted.
     *
     * @param left must not be {@literal null}.
     * @param right must not be {@literal null}.
     * @param combiner must not be {@literal null}.
     *
     * @return
     *
     * @since 2.1
     */
    public static <L, R, T> Stream<T> zip(Stream<L> left, Stream<R> right, BiFunction<L, R, T> combiner) {

//        Assert.notNull(left, "Left stream must not be null!");
        if (null == left) {
            //-- 非法输入: <param>left</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "left"
                    , "Left stream must not be null"
                    , Streamable.class.getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
//        Assert.notNull(right, "Right must not be null!");
        if (null == right) {
            //-- 非法输入: <param>iterable</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "right"
                    , "Right must not be null"
                    , Streamable.class.getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }
//        Assert.notNull(combiner, "Combiner must not be null!");
        if (null == combiner) {
            //-- 非法输入: <param>iterable</param>
            throw new IllegalArgumentException(String.format("非法参数:<param>%s</param>->【%s】 <= 【<class>%s</class>-<method>%s</method> <- 第%s行】"
                    , "combiner"
                    , "Combiner must not be null"
                    , Streamable.class.getName()
                    , Thread.currentThread().getStackTrace()[1].getMethodName()
                    , Thread.currentThread().getStackTrace()[1].getLineNumber()));
        }

        Spliterator<L> lefts = left.spliterator();
        Spliterator<R> rights = right.spliterator();

        long size = Long.min(lefts.estimateSize(), rights.estimateSize());
        int characteristics = lefts.characteristics() & rights.characteristics();
        boolean parallel = left.isParallel() || right.isParallel();

        return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(size, characteristics) {

            @Override
            @SuppressWarnings("null")
            public boolean tryAdvance(Consumer<? super T> action) {
                return lefts.tryAdvance(left -> rights.tryAdvance(right -> action.accept(combiner.apply(left, right))));
            }

        }, parallel);
    }

}
