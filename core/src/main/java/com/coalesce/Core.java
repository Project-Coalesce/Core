package com.coalesce;

import com.coalesce.api.NMSCore;
import com.coalesce.nms.reflection.NMSReflection;
import com.coalesce.nms.v1_11_2_r0_1.NMS1_11_2_R0_1;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.Bukkit;
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

		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		if (version.matches(".+1_11(_2)?_R0_1.+")) {
			NMSCore.set(new NMS1_11_2_R0_1());
		} else {
			NMSCore.set(new NMSReflection());
		}

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
