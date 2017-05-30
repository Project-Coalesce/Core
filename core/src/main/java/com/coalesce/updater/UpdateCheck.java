package com.coalesce.updater;

import com.coalesce.http.CoHTTP;
import com.coalesce.plugin.CoPlugin;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;

import java.util.concurrent.ExecutionException;

public final class UpdateCheck {
	
	private final CoPlugin plugin;
	private final String owner;
	private final String repo;
	private UpdateData data;
	
	public UpdateCheck(CoPlugin plugin, String owner, String repoName) {
		this.plugin = plugin;
		this.owner = owner;
		this.repo = repoName;
		
		plugin.getCoLogger().info(plugin.getName() + " is checking for updates...");
		
		ListenableFuture<String> future = CoHTTP.sendGet("https://api.github.com/repos/" + owner + "/" + repo + "/releases/latest", plugin.getDisplayName() + " Spigot Plugin");
		
		future.addListener(() -> {
			try {
				this.data = new Gson().fromJson(future.get(), UpdateData.class);
				if (!plugin.getDescription().getVersion().matches(data.getVersion())) {
					plugin.getCoLogger().warn("This build of " + plugin.getName() + " is outdated...");
					plugin.getCoLogger().warn("Current version: " + plugin.getDescription().getVersion());
					plugin.getCoLogger().warn("New version: " + data.getVersion());
					plugin.getCoLogger().warn("Download: " + data.getUrl());
					return;
				}
				plugin.getCoLogger().info(plugin.getName() + " is up to date.");
			}
			catch (NullPointerException e) {
				plugin.getCoLogger().info("No public releases currently exist for " + plugin.getName());
			}
			catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}, task -> Bukkit.getScheduler().runTask(plugin, task));
	}
	
	private static class UpdateData {
		
		private String message;
		
		@SerializedName( "html_url" )
		private String url;
		
		@SerializedName( "tag_name" )
		private String version;
		
		
		/**
		 * Ggets the URL to this release
		 *
		 * @return The release page.
		 */
		public String getUrl() {
			return url;
		}
		
		/**
		 * Gets a message to check if the page exists or not.
		 * <p>
		 * <p>This is mainly used to check for missing releases.</p>
		 * <p>
		 * <p>If no releases exist the message will be "Not Found", otherwise the message will be null</p>
		 *
		 * @return A message if no release is found.
		 */
		public String getMessage() {
			return message;
		}
		
		/**
		 * Gets the tagged version for this release.
		 *
		 * @return The release version.
		 */
		public String getVersion() {
			return version;
		}
	}
}
