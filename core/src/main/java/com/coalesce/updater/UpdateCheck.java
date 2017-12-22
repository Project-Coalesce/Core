package com.coalesce.updater;

import com.coalesce.http.CoHTTP;
import com.coalesce.plugin.CoPlugin;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public final class UpdateCheck {

    private final CoPlugin plugin;
    private UpdateData data;
    private File jarFile;

    //First is owner string, then repo
    private static final String GITHUB_ADDRESS = "https://api.github.com/repos/%s/%s/releases/latest";

    public UpdateCheck(CoPlugin plugin, String owner, String repo, File pluginJarFile, boolean autoUpdate) {

        this.plugin = plugin;
        this.jarFile = pluginJarFile;

        plugin.getCoLogger().info("Looking for updates to " + plugin.getDisplayName() + "...");

        ListenableFuture<String> future = CoHTTP.sendGet(String.format(GITHUB_ADDRESS, owner, repo), plugin.getDisplayName() + " Spigot Plugin");

        future.addListener(() -> {
            try {
                this.data = new Gson().fromJson(future.get(), UpdateData.class);

                if (!plugin.getDescription().getVersion().matches(data.getVersion())) {
                    plugin.getCoLogger().info("New version was found! [" + data.getVersion() + "]");

                    if (autoUpdate) {
                        List<Asset> javaAssets = data.assets.stream().filter(check -> check.assetName.substring((check.assetName.length() - 3)).equalsIgnoreCase("jar")).collect(Collectors.toList());

                        if (javaAssets.size() == 0) {
                            plugin.getCoLogger().info(String.format("No jars were found in the release \"%s\". Aborting auto-update. You can update manually.", data.getUrl()));
                            return;
                        } else if (javaAssets.size() == 1) {
                            Asset download = javaAssets.get(0);
                            new AutoUpdateThread(plugin, new URL(download.downloadURL), jarFile, download.assetName).start();
                            return;
                        }

                        List<Asset> labeledAssets = javaAssets.stream().filter(check -> check.label != null && check.label.equals("Auto-Download")).collect(Collectors.toList());

                        if (labeledAssets.size() != 1) {
                            plugin.getCoLogger().info(String.format("More than one possible jar was found in the release \"%s\". Aborting auto-update. You can update manually.", data.getUrl()));
                            return;
                        }

                        Asset download = labeledAssets.get(0);
                        new AutoUpdateThread(plugin, new URL(download.downloadURL), jarFile, download.assetName).start();
                    } else {
                        plugin.getCoLogger().info("Download it at: " + data.getUrl());
                    }

                    return;
                }
                plugin.getCoLogger().info(plugin.getDisplayName() + " is up to date.");
            }
            catch (NullPointerException e) {
                plugin.getCoLogger().warn("Could not find latest released version from GitHub. (This plugin may not have a public release yet)");
            }
            catch (Exception e) {
                plugin.getCoLogger().warn("There was an error checking for updates.");
            }

        }, task -> Bukkit.getScheduler().runTask(plugin, task));
    }

    private static class UpdateData {

        private String message;

        private List<Asset> assets;

        @SerializedName( "html_url" ) private String url;

        @SerializedName( "tag_name" ) private String version;

        /**
         * Gets the assets from the resource post.
         *
         * @return The assets.
         */
        public List<Asset> getAssets() {
            return assets;
        }

        /**
         * Gets the HTML URL to redirect for the user.
         *
         * @return URL in string.
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

    private static class Asset {

        @SerializedName( "browser_download_url" ) private String downloadURL;

        @SerializedName( "name" ) private String assetName;

        private String label;

    }
}
