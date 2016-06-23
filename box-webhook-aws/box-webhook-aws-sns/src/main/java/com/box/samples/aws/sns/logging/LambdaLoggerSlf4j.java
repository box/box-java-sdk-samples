package com.box.samples.aws.sns.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.slf4j.event.Level;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

/**
 * SLF4j binding for {@link LambdaLogger}.
 * 
 * @author Stanislav Dvorscak
 *
 */
public class LambdaLoggerSlf4j extends MarkerIgnoringBase {

	/**
	 * Serial version id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Lambda context holder.
	 */
	private static ThreadLocal<Context> context = new ThreadLocal<>();

	/**
	 * Minimal logging level.
	 */
	private final Level level;

	/**
	 * Name of logger.
	 */
	private final String name;

	/**
	 * Date formatter.
	 */
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS 'GMT'").withZone(ZoneOffset.UTC);

	/**
	 * Constructor.
	 * 
	 * @param level
	 *            minimal logging level
	 * @param name
	 *            of logger
	 */
	public LambdaLoggerSlf4j(Level level, String name) {
		this.level = level;
		this.name = name;
	}

	/**
	 * @param context
	 *            support to set Lambda context
	 */
	public static void setContext(Context context) {
		LambdaLoggerSlf4j.context.set(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraceEnabled() {
		return LocationAwareLogger.TRACE_INT >= level.toInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String msg) {
		log(Level.TRACE, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String msg, Throwable t) {
		log(Level.TRACE, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String format, Object arg) {
		log(Level.TRACE, format, arg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String format, Object arg1, Object arg2) {
		log(Level.TRACE, format, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String format, Object... arguments) {
		log(Level.TRACE, format, arguments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugEnabled() {
		return LocationAwareLogger.DEBUG_INT >= level.toInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String msg) {
		log(Level.DEBUG, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String msg, Throwable t) {
		log(Level.DEBUG, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String format, Object arg) {
		log(Level.DEBUG, format, arg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String format, Object arg1, Object arg2) {
		log(Level.DEBUG, format, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String format, Object... arguments) {
		log(Level.DEBUG, format, arguments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInfoEnabled() {
		return LocationAwareLogger.INFO_INT >= level.toInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String msg) {
		log(Level.INFO, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String msg, Throwable t) {
		log(Level.INFO, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String format, Object arg) {
		log(Level.INFO, format, arg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String format, Object arg1, Object arg2) {
		log(Level.INFO, format, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String format, Object... arguments) {
		log(Level.INFO, format, arguments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWarnEnabled() {
		return LocationAwareLogger.WARN_INT >= level.toInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String msg) {
		log(Level.WARN, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String msg, Throwable t) {
		log(Level.WARN, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String format, Object arg) {
		log(Level.WARN, format, arg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String format, Object arg1, Object arg2) {
		log(Level.WARN, format, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String format, Object... arguments) {
		log(Level.WARN, format, arguments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isErrorEnabled() {
		return LocationAwareLogger.ERROR_INT >= level.toInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg) {
		log(Level.ERROR, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg, Throwable t) {
		log(Level.ERROR, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String format, Object arg) {
		log(Level.ERROR, format, arg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String format, Object arg1, Object arg2) {
		log(Level.ERROR, format, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String format, Object... arguments) {
		log(Level.ERROR, format, arguments);
	}

	/**
	 * @see #log(Level, FormattingTuple)
	 * @param level
	 *            logging level
	 * @param format
	 *            of {@link FormattingTuple}
	 * @param arg
	 *            of {@link FormattingTuple}
	 */
	private void log(Level level, String format, Object arg) {
		log(level, MessageFormatter.format(format, arg));
	}

	/**
	 * @see #log(Level, FormattingTuple)
	 * @param level
	 *            logging level
	 * @param format
	 *            of {@link FormattingTuple}
	 * @param arg1
	 *            of {@link FormattingTuple}
	 * @param arg2
	 *            of {@link FormattingTuple}
	 */
	private void log(Level level, String format, Object arg1, Object arg2) {
		log(level, MessageFormatter.format(format, arg1, arg2));
	}

	/**
	 * @see #log(Level, FormattingTuple)
	 * @param level
	 *            logging level
	 * @param format
	 *            of {@link FormattingTuple}
	 * @param args
	 *            of {@link FormattingTuple}
	 */
	private void log(Level level, String format, Object... args) {
		log(level, MessageFormatter.arrayFormat(format, args));
	}

	/**
	 * Logs a provided {@link FormattingTuple}.
	 * 
	 * @see #log(Level, String, Throwable)
	 * 
	 * @param level
	 *            logging level
	 * @param tupple
	 *            created {@link FormattingTuple}
	 */
	private void log(Level level, FormattingTuple tupple) {
		log(level, tupple.getMessage(), tupple.getThrowable());
	}

	/**
	 * Logs a provided message.
	 * 
	 * @param level
	 *            logging level
	 * @param message
	 *            for logging
	 * @param t
	 *            {@link Throwable} can be null if none
	 */
	private void log(Level level, String message, Throwable t) {
		if (level.toInt() < this.level.toInt()) {
			return;
		}
		Context context = LambdaLoggerSlf4j.context.get();
		LambdaLogger logger = context != null ? context.getLogger() : null;

		// no lambda logger / can not do logging
		if (logger == null) {
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(message).append('\n');

		if (t != null) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			t.printStackTrace(printWriter);
			printWriter.flush();
			builder.append(stringWriter.toString()).append('\n');
		}

		logger.log(String.format("[%s] [%s] [%s] %s", level, formatter.format(Instant.now()), name, builder.toString()));
	}

}
