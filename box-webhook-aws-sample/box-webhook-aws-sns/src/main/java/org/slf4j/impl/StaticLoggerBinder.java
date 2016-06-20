package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import com.box.samples.aws.sns.logging.LambdaLoggerSlf4jLoggerFactory;

/**
 * {@link LoggerFactoryBinder} for {@link LambdaLoggerSlf4jLoggerFactory}.
 */
public final class StaticLoggerBinder implements LoggerFactoryBinder {

    /**
     * Singleton.
     */
    private static final StaticLoggerBinder INSTANCE = new StaticLoggerBinder();

    /**
     * @see #INSTANCE
     */
    private StaticLoggerBinder() {
    }

    /**
     * @return {@link LoggerFactoryBinder} implementation
     */
    public static StaticLoggerBinder getSingleton() {
        return StaticLoggerBinder.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ILoggerFactory getLoggerFactory() {
        return LambdaLoggerSlf4jLoggerFactory.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLoggerFactoryClassStr() {
        return "com.box.samples.aws.sns.logging.LambdaLoggerSlf4jLoggerFactory";
    }

}
