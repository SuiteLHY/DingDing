package github.com.suitelhy.dingding.core.infrastructure.application.util;

import lombok.Value;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Lazy implementation of {@link org.springframework.data.util.Streamable} obtains a {@link Stream} from a given {@link Supplier}.
 *
 * @author Oliver Gierke
 *
 * @since 2.0
 *
 * @see org.springframework.data.util.LazyStreamable
 */
@Value(staticConstructor = "of")
class LazyStreamable<T>
        implements Streamable<T> {

    private final Supplier<? extends Stream<T>> stream;

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.util.Streamable#stream()
     */
    @Override
    public Stream<T> stream() {
        return stream.get();
    }

}
