package com.box.samples.aws.sns.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * {@link Exception} related utilities.
 * 
 * @author Stanislav Dvorscak
 *
 */
public class ExceptionUtils {

	/**
	 * Only static members.
	 */
	private ExceptionUtils() {
	}

	/**
	 * @param e
	 *            for transformation
	 * @return builds stack trace for a provided {@link Exception}.
	 */
	public static String stackTrace(Exception e) {
		StringWriter result = new StringWriter();
		PrintWriter writer = new PrintWriter(result);
		e.printStackTrace(writer);
		writer.close();
		return result.toString();
	}

}
