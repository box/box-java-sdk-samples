package com.box.samples.aws.sns.utils;

import java.util.function.Supplier;

/**
 * 'Lambda' related utlities.
 * 
 * @author Stanislav Dvorscak
 *
 */
public class LambdaUtils {

	/**
	 * Only static members.
	 */
	private LambdaUtils() {
	}

	/**
	 * {@link Supplier} which do lazy loading.
	 * 
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
	 * @author Stanislav Dvorscak
	 *
	 * @param <T>
	 *            type of value
	 */
	private static class LazySupplier<T> implements Supplier<T> {

		/**
		 * {@link #value} factory
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
		public LazySupplier(Supplier<T> factory) {
			this.factory = factory;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized T get() {
			if (!loaded) {
				loaded = true;
				value = factory.get();
			}
			return value;
		}

	}

}
