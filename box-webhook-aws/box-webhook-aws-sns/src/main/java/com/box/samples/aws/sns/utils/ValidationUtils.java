package com.box.samples.aws.sns.utils;

import com.box.samples.aws.sns.validation.Validation;

/**
 * {@link Validation} related utilities.
 * 
 * @author Stanislav Dvorscak
 *
 */
public class ValidationUtils {

	/**
	 * Only static members.
	 */
	private ValidationUtils() {
	}

	/**
	 * Check that a provided value is not null.
	 * 
	 * @param validation
	 *            context
	 * @param path
	 *            to the value
	 * @param value
	 *            for check
	 */
	public static void notNull(Validation validation, String path, Object value) {
		if (value == null) {
			validation.addError(path, "must_not_be_null");
		}
	}

	/**
	 * Checks that a provided {@link String} value is not blank (empty or white space only).
	 * 
	 * @param validation
	 *            context
	 * @param path
	 *            to the value
	 * @param value
	 *            for check
	 */
	public static void notBlank(Validation validation, String path, String value) {
		if (StringUtils.isBlank(value)) {
			validation.addError(path, "must_not_be_blank");
		}
	}

}
