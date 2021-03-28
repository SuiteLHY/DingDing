package github.com.suitelhy.dingding.core.infrastructure.domain.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.core.CollectionFactory;
import org.springframework.core.ResolvableType;
import org.springframework.data.util.Optionals;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * API to record method invocations via method references on a proxy.
 *
 * @Editor Suite
 *
 * @author Oliver Gierke
 *
 * @since 2.2
 * @soundtrack The Intersphere - Don't Think Twice (The Grand Delusion)
 *
 * @see org.springframework.data.util.MethodInvocationRecorder
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodInvocationRecorder {

    public static MethodInvocationRecorder.PropertyNameDetectionStrategy DEFAULT = MethodInvocationRecorder.DefaultPropertyNameDetectionStrategy.INSTANCE;

    private @NotNull Optional<MethodInvocationRecorder.RecordingMethodInterceptor> interceptor;

    /**
     * Creates a new {@link MethodInvocationRecorder}.
     * For ad-hoc instantation prefer the static {@link #forProxyOf(Class)}.
     */
    private MethodInvocationRecorder() {
        this(Optional.empty());
    }

    /**
     * Creates a new {@link MethodInvocationRecorder.Recorded} for the given type.
     *
     * @param type must not be {@literal null}.
     *
     * @return
     */
    public static <T> MethodInvocationRecorder.Recorded<T> forProxyOf(Class<T> type) {

        Assert.notNull(type, "Type must not be null!");
        Assert.isTrue(! Modifier.isFinal(type.getModifiers()), "Type to record invocations on must not be final!");

        return new MethodInvocationRecorder().create(type);
    }

    /**
     * Creates a new {@link MethodInvocationRecorder.Recorded} for the given type based on the current {@link MethodInvocationRecorder} setup.
     *
     * @param type
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> MethodInvocationRecorder.Recorded<T> create(Class<T> type) {

        MethodInvocationRecorder.RecordingMethodInterceptor interceptor = new MethodInvocationRecorder.RecordingMethodInterceptor();

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addAdvice(interceptor);

        if (! type.isInterface()) {
            proxyFactory.setTargetClass(type);
            proxyFactory.setProxyTargetClass(true);
        } else {
            proxyFactory.addInterface(type);
        }

        T proxy = (T) proxyFactory.getProxy(type.getClassLoader());

        return new MethodInvocationRecorder.Recorded<T>(proxy, new MethodInvocationRecorder(Optional.ofNullable(interceptor)));
    }

    private Optional<String> getPropertyPath(List<MethodInvocationRecorder.PropertyNameDetectionStrategy> strategies) {
        return interceptor.flatMap(it -> it.getPropertyPath(strategies));
    }

    private class RecordingMethodInterceptor
            implements org.aopalliance.intercept.MethodInterceptor
    {

        private MethodInvocationRecorder.InvocationInformation information = MethodInvocationRecorder.InvocationInformation.NOT_INVOKED;

        /*
         * (non-Javadoc)
         * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
         */
        @Override
        @SuppressWarnings("null")
        public Object invoke(MethodInvocation invocation)
                throws Throwable
        {

            Method method = invocation.getMethod();
            Object[] arguments = invocation.getArguments();

            if (ReflectionUtils.isObjectMethod(method)) {
                return method.invoke(this, arguments);
            }

            ResolvableType type = ResolvableType.forMethodReturnType(method);
            Class<?> rawType = type.resolve(Object.class);

            if (Collection.class.isAssignableFrom(rawType)) {

                Class<?> clazz = type.getGeneric(0).resolve(Object.class);

                MethodInvocationRecorder.InvocationInformation information = registerInvocation(method, clazz);

                Collection<Object> collection = CollectionFactory.createCollection(rawType, 1);
                collection.add(information.getCurrentInstance());

                return collection;
            }

            if (Map.class.isAssignableFrom(rawType)) {

                Class<?> clazz = type.getGeneric(1).resolve(Object.class);
                MethodInvocationRecorder.InvocationInformation information = registerInvocation(method, clazz);

                Map<Object, Object> map = CollectionFactory.createMap(rawType, 1);
                map.put("_key_", information.getCurrentInstance());

                return map;
            }

            return registerInvocation(method, rawType).getCurrentInstance();
        }

        private Optional<String> getPropertyPath(List<MethodInvocationRecorder.PropertyNameDetectionStrategy> strategies) {
            return this.information.getPropertyPath(strategies);
        }

        private MethodInvocationRecorder.InvocationInformation registerInvocation(Method method, Class<?> proxyType) {

            MethodInvocationRecorder.Recorded<?> create = Modifier.isFinal(proxyType.getModifiers())
                    ? new MethodInvocationRecorder.Unrecorded()
                    : create(proxyType);
            MethodInvocationRecorder.InvocationInformation information = new MethodInvocationRecorder.InvocationInformation(create, method);

            return this.information = information;
        }

    }

    @Value
    private static class InvocationInformation {

        static final MethodInvocationRecorder.InvocationInformation NOT_INVOKED = new MethodInvocationRecorder.InvocationInformation(new MethodInvocationRecorder.Unrecorded(), null);

        @NonNull MethodInvocationRecorder.Recorded<?> recorded;
        @Nullable Method invokedMethod;

        @Nullable
        Object getCurrentInstance() {
            return recorded.currentInstance;
        }

        Optional<String> getPropertyPath(List<MethodInvocationRecorder.PropertyNameDetectionStrategy> strategies) {

            Method invokedMethod = this.invokedMethod;

            if (null == invokedMethod) {
                return Optional.empty();
            }

            String propertyName = getPropertyName(invokedMethod, strategies);
            Optional<String> next = recorded.getPropertyPath(strategies);

            return Optionals.firstNonEmpty(() -> next.map(it -> propertyName.concat(".").concat(it)), //
                    () -> Optional.of(propertyName));
        }

        private static String getPropertyName(Method invokedMethod, List<MethodInvocationRecorder.PropertyNameDetectionStrategy> strategies) {

            return strategies.stream() //
                    .map(it -> it.getPropertyName(invokedMethod)) //
                    .findFirst() //
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("No property name found for method %s!", invokedMethod)));
        }
    }

    public interface PropertyNameDetectionStrategy {

        @Nullable
        String getPropertyName(Method method);

    }

    private static enum DefaultPropertyNameDetectionStrategy
            implements MethodInvocationRecorder.PropertyNameDetectionStrategy {
        INSTANCE;

        /*
         * (non-Javadoc)
         * @see org.springframework.hateoas.core.Recorder.PropertyNameDetectionStrategy#getPropertyName(java.lang.reflect.Method)
         */
        @Nonnull
        @Override
        public String getPropertyName(Method method) {
            return getPropertyName(method.getReturnType(), method.getName());
        }

        private static String getPropertyName(Class<?> type, String methodName) {

            String pattern = getPatternFor(type);
            String replaced = methodName.replaceFirst(pattern, "");

            return StringUtils.uncapitalize(replaced);
        }

        private static String getPatternFor(Class<?> type) {
            return type.equals(boolean.class) ? "^(is)" : "^(get|set)";
        }
    }

    @ToString
    /*@RequiredArgsConstructor*/
    public static class Recorded<T> {

        private /*final */@Nullable T currentInstance;
        private /*final */@Nullable MethodInvocationRecorder recorder;

        /**
         * (Constructor)
         *
         * @Description 用于支持 RPC 框架的序列化和反序列化.
         */
        private Recorded() {
            this.currentInstance = null;
            this.recorder = null;
        }

        /**
         * (Constructor)
         *
         * @param currentInstance
         */
        public Recorded(@Nullable T currentInstance) {
            this.currentInstance = currentInstance;
            this.recorder = null;
        }

        /**
         * (Constructor)
         *
         * @param recorder
         */
        public Recorded(@Nullable MethodInvocationRecorder recorder) {
            this.currentInstance = null;
            this.recorder = recorder;
        }

        /**
         * (Constructor)
         *
         * @param currentInstance
         * @param recorder
         */
        public Recorded(@Nullable T currentInstance, @Nullable MethodInvocationRecorder recorder) {
            this.currentInstance = currentInstance;
            this.recorder = recorder;
        }

        public Optional<String> getPropertyPath() {
            return getPropertyPath(MethodInvocationRecorder.DEFAULT);
        }

        public Optional<String> getPropertyPath(MethodInvocationRecorder.PropertyNameDetectionStrategy strategy) {

            MethodInvocationRecorder recorder = this.recorder;

            return (null == recorder)
                    ? Optional.empty()
                    : recorder.getPropertyPath(Arrays.asList(strategy));
        }

        public Optional<String> getPropertyPath(List<MethodInvocationRecorder.PropertyNameDetectionStrategy> strategies) {

            MethodInvocationRecorder recorder = this.recorder;

            return (null == recorder)
                    ? Optional.empty()
                    : recorder.getPropertyPath(strategies);
        }

        /**
         * Applies the given Converter to the recorded value and remembers the property accessed.
         *
         * @param converter must not be {@literal null}.
         *
         * @return
         */
        public <S> MethodInvocationRecorder.Recorded<S> record(Function<? super T, S> converter) {

            Assert.notNull(converter, "Function must not be null!");

            return new MethodInvocationRecorder.Recorded<S>(converter.apply(currentInstance), recorder);
        }

        /**
         * Record the method invocation traversing to a collection property.
         *
         * @param converter must not be {@literal null}.
         *
         * @return
         */
        public <S> MethodInvocationRecorder.Recorded<S> record(MethodInvocationRecorder.Recorded.ToCollectionConverter<T, S> converter) {

            Assert.notNull(converter, "Converter must not be null!");

            return new MethodInvocationRecorder.Recorded<S>(converter.apply(currentInstance).iterator().next(), recorder);
        }

        /**
         * Record the method invocation traversing to a map property.
         *
         * @param converter must not be {@literal null}.
         *
         * @return
         */
        public <S> MethodInvocationRecorder.Recorded<S> record(MethodInvocationRecorder.Recorded.ToMapConverter<T, S> converter) {

            Assert.notNull(converter, "Converter must not be null!");

            return new MethodInvocationRecorder.Recorded<S>(converter.apply(currentInstance).values().iterator().next(), recorder);
        }

        public interface ToCollectionConverter<T, S> extends Function<T, Collection<S>> {}

        public interface ToMapConverter<T, S> extends Function<T, Map<?, S>> {}

    }

    static class Unrecorded
            extends MethodInvocationRecorder.Recorded<Object>
    {

        @SuppressWarnings("null")
        private Unrecorded() {
            super(null, null);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.util.MethodInvocationRecorder.Recorded#getPropertyPath(java.util.List)
         */
        @Override
        public Optional<String> getPropertyPath(List<MethodInvocationRecorder.PropertyNameDetectionStrategy> strategies) {
            return Optional.empty();
        }

    }

}
