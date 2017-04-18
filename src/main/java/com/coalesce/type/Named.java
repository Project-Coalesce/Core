package com.coalesce.type;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that has a name
 */
public interface Named {

	/**
	 * Get the name of this object
	 *
	 * @return The name
	 */
	@NotNull String getName();

}
