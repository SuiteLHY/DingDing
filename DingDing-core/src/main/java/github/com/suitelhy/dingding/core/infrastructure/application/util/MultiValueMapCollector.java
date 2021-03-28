package github.com.suitelhy.dingding.core.infrastructure.application.util;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

/**
 * A {@link Collector} for building a {@link MultiValueMap} from a {@link java.util.stream.Stream}.
 *
 * @Editor Suite
 *
 * @author Jens Schauder
 * @since 2.0
 *
 * @see org.springframework.data.util.MultiValueMapCollector
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
class MultiValueMapCollector<T, K, V>
        implements Collector<T, MultiValueMap<K, V>, MultiValueMap<K, V>> {

    private final @NonNull Function<T, K> keyFunction;
    private final @NonNull Function<T, V> valueFunction;

    /*
     * (non-Javadoc)
     * @see java.util.stream.Collector#supplier()
     */
    @Override
    public Supplier<MultiValueMap<K, V>> supplier() {
        return () -> CollectionUtils.toMultiValueMap(new HashMap<>());
    }

    /*
     * (non-Javadoc)
     * @see java.util.stream.Collector#accumulator()
     */
    @Override
    public BiConsumer<MultiValueMap<K, V>, T> accumulator() {
        return (map, t) -> map.add(keyFunction.apply(t), valueFunction.apply(t));
    }

    /*
     * (non-Javadoc)
     * @see java.util.stream.Collector#combiner()
     */
    @Override
    public BinaryOperator<MultiValueMap<K, V>> combiner() {

        return (map1, map2) -> {

            for (K key : map2.keySet()) {
                map1.addAll(key, map2.get(key));
            }

            return map1;
        };
    }

    /*
     * (non-Javadoc)
     * @see java.util.stream.Collector#finisher()
     */
    @Override
    public Function<MultiValueMap<K, V>, MultiValueMap<K, V>> finisher() {
        return Function.identity();
    }

    /*
     * (non-Javadoc)
     * @see java.util.stream.Collector#characteristics()
     */
    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
    }

}
