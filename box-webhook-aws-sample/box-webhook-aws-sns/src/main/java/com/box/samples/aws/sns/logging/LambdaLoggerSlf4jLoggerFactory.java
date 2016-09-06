package com.box.samples.aws.sns.logging;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * {@link ILoggerFactory} for {@link LambdaLoggerSlf4j}.
 */
public class LambdaLoggerSlf4jLoggerFactory implements ILoggerFactory {

    /**
     * Singleton.
     */
    public static final LambdaLoggerSlf4jLoggerFactory INSTANCE = new LambdaLoggerSlf4jLoggerFactory();

    /**
     * @see #INSTANCE
     */
    protected LambdaLoggerSlf4jLoggerFactory() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger(String name) {
        Level level;
        if (name.startsWith("com.box.samples")) {
            level = Level.INFO;
        } else {
            level = Level.WARN;
        }
        return new LambdaLoggerSlf4j(level, name);
    }

}
