package com.box.samples.aws.sns.validation;

/**
 * Exception wrapper for {@link Validation}.
 * 
 * @see Validation#validate()
 * 
 * @author Stanislav Dvorscak
 *
 */
public class ValidationException extends RuntimeException {

	/**
	 * Serialization value.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see #getValidation()
	 */
	private final Validation validation;

	/**
	 * Constructor.
	 * 
	 * @param validation
	 *            {@link #getValidation()}
	 */
	public ValidationException(Validation validation) {
		this.validation = validation;
	}

	/**
	 * @return wrapped {@link Validation}
	 */
	public Validation getValidation() {
		return validation;
	}

}
