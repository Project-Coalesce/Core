package com.coalesce.config;

import com.coalesce.Core;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * Represents a file configuration
 */
public abstract class Config {

	private static final TomlWriter CONFIG_WRITER = new TomlWriter.Builder().indentValuesBy(2).indentTablesBy(2).build();


	/**
	 * Save this config to a file
	 *
	 * @param file The file
	 * @return True if the file saved properly
	 */
	public final boolean save(@NotNull File file) {
		try {
			File parent = file.getParentFile();
			if (!parent.exists()) parent.mkdirs();

			CONFIG_WRITER.write(this, file);
			return true;
		} catch (IOException e) {
			Core.getInstance().error("Failed to write " + getClass().getSimpleName() + " to file " + file.getName());
			e.printStackTrace();
		}

		return false;
	}


	/**
	 * Attempt to load this config from a file
	 *
	 * @param defaultIpl The default config
	 * @param file       The file
	 * @param <C>        The Config class type
	 * @return The config, if the file exists, otherwise defaultImpl
	 */
	public static <C extends Config> C load(C defaultIpl, File file) {
		if (!file.exists()) {
			defaultIpl.save(file);
			return defaultIpl;
		}

		return new Toml().read(file).to((Class<C>) defaultIpl.getClass());
	}

}
