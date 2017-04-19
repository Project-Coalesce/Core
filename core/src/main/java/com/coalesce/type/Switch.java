package com.coalesce.type;

/**
 * Represents an object that can be enabled and disabled
 */
public interface Switch {

	/**
	 * Enable this object
	 */
	void enable();

	/**
	 * Disable this object
	 */
	void disable();

	/**
	 * Check whether this switch is enabled
	 *
	 * @return True if it's enabled
	 */
	boolean isEnabled();

}
