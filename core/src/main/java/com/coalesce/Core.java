package com.coalesce;

import com.coalesce.plugin.CoPlugin;
import org.jetbrains.annotations.Nullable;

import static org.bukkit.plugin.ServicePriority.Normal;

public class Core extends CoPlugin {
	
	
	
	private static Core instance;
	
	
	//private UserModule userModule;
	//private CommandModule commandModule;

	@Override
	public void onPluginEnable() {
		getServer().getServicesManager().register(Core.class, this, this, Normal);
		this.displayName = "CoalesceCore";
		
		//addModules(userModule = new UserModule(this), commandModule = new CommandModule(this));
	}
	
	@Override
	public void onPluginDisable() {
		instance = null;
		//userModule = null;
		//commandModule = null;
	}


	/*public @NotNull UserModule getUserModule() {
		return checkCoreEnabled(userModule);
	}
	public @NotNull CommandModule getCommandModule() {
		return checkCoreEnabled(commandModule);
	}*/
	
	/**
	 * Grabs the instance of the core.
	 * Make sure you don't call this before nor after {@link #onPluginDisable()}.
	 *
	 * @return The core instance.
	 */
	public static @Nullable Core getInstance() {
		return instance;
	}
	
	private <M> M checkCoreEnabled(M instance) {
		if (!isEnabled()) throw new IllegalStateException("Core plugin is not enabled");
		return instance;
	}
}
