package com.box.samples.aws.sns.utils;

import java.nio.charset.Charset;

/**
 * {@link String} related utilities.
 * 
 * @author Stanislav Dvorscak
 *
 */
public class StringUtils {

	/**
	 * UTF_8 {@link Charset}.
	 */
	public static final Charset UTF_8 = Charset.forName("UTF-8");

	/**
	 * Only static members.
	 */
	private StringUtils() {
	}

	/**
	 * @param value
	 *            for check
	 * @return true if a provided value is blank
	 */
	public static boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

}
