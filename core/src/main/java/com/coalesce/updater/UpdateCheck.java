package com.coalesce.updater;

import com.coalesce.http.CoHTTP;
import com.coalesce.plugin.CoPlugin;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public final class UpdateCheck {

	private final CoPlugin plugin;
	private UpdateData data;
	
	public UpdateCheck(CoPlugin plugin, String owner, String repo, boolean autoUpdate) {
		this.plugin = plugin;
		
		plugin.getCoLogger().info("Looking for updates to " + plugin.getDisplayName() + "...");

		ListenableFuture<String> future = CoHTTP.sendGet("https://api.github.com/repos/" + owner + "/" + repo + "/releases/latest",
                plugin.getDisplayName() + " Spigot Plugin");

		future.addListener(() -> {
			try {
				this.data = new Gson().fromJson(future.get(), UpdateData.class);

				if (!plugin.getDescription().getVersion().matches(data.getVersion())) {
					plugin.getCoLogger().info("A new version of " + plugin.getDisplayName() + " is out! [" + data.getVersion() + "]");
                    List<Asset> javaAssets = data.assets.stream().filter(check -> check.assetName.matches("\\.(jar|JAR)$")).collect(Collectors.toList());

                    if (javaAssets.size() == 0) {
                        plugin.getCoLogger().info("Unable to auto-update due to release not having JAR downloads, download from: " + data.getUrl());
                        return;
                    } else if (javaAssets.size() == 1) {
                        Asset download = javaAssets.get(0);
                        new AutoUpdateThread(plugin, new URL(download.downloadURL)).start();
                        return;
                    }

                    List<Asset> labeledAssets = javaAssets.stream().filter(check -> check.label.equals("Auto-Download")).collect(Collectors.toList());;
                    if (labeledAssets.size() != 0) {
                        plugin.getCoLogger().info("Unable to auto-update due to release having too many JAR downloads, download from: " + data.getUrl());
                        return;
                    }

                    Asset download = labeledAssets.get(0);
                    new AutoUpdateThread(plugin, new URL(download.downloadURL)).start();

					return;
				}
				plugin.getCoLogger().info(plugin.getDisplayName() + " is up to date.");
			}
			catch (NullPointerException e) {
				plugin.getCoLogger().warn("No public releases currently exist for " + plugin.getName());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}, task -> Bukkit.getScheduler().runTask(plugin, task));
	}

	private static class UpdateData {

		private String message;

		private List<Asset> assets;

        @SerializedName( "html_url" )
        private String url;

		@SerializedName( "tag_name" )
		private String version;

        /**
         * Gets the assets from the resource post.
         *
         * @return The assets.
         */
        public List<Asset> getAssets(){
            return assets;
        }

        /**
         * Gets the HTML URL to redirect for the user.
         *
         * @return URL in string.
         */
        public String getUrl(){
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

	private static class Asset {

	    @SerializedName( "browser_download_url" )
        private String downloadURL;

	    @SerializedName( "name" )
        private String assetName;

	    private String label;

    }
}
