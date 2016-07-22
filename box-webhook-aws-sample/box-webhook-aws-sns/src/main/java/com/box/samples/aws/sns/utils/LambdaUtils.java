package com.box.samples.aws.sns.utils;

import java.util.function.Supplier;

/**
 * 'Lambda' related utilities.
 */
public class LambdaUtils {

    /**
     * Only static members.
     */
    protected LambdaUtils() {
    }

    /**
     * {@link Supplier} which do lazy loading.
     *
     * @param <T>
     *            type of supplied value
     * @param factory
     *            for value
     * @return lazy {@link Supplier}
     */
    public static <T> Supplier<T> lazy(Supplier<T> factory) {
        return new LazySupplier<>(factory);
    }

    /**
     * Lazy supplier.
     *
     * @param <T>
     *            type of value
     */
    private static class LazySupplier<T> implements Supplier<T> {

        /**
         * {@link #value} factory.
         */
        private final Supplier<T> factory;

        /**
         * Loaded value.
         */
        private volatile T value;

        /**
         * Was value loaded?
         */
        private volatile boolean loaded;

        /**
         * Constructor.
         *
         * @param factory
         *            value factory
         */
        LazySupplier(Supplier<T> factory) {
            this.factory = factory;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public synchronized T get() {
            if (!this.loaded) {
                this.loaded = true;
                this.value = this.factory.get();
            }
            return this.value;
        }

    }

}
