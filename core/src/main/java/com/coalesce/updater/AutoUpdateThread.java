package com.coalesce.updater;

import com.coalesce.plugin.CoPlugin;
import com.coalesce.plugin.PluginUtil;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class AutoUpdateThread extends Thread {

	private final CoPlugin plugin;
	private final URL downloadUrl;

	private File pluginJar;
	private String name;

	private HttpURLConnection connection;
	private FileOutputStream outputStream;
    private long downloaded;

	private static final byte[] BUFFER = new byte[1024];

    public AutoUpdateThread(CoPlugin plugin, URL downloadUrl, File pluginJar, String name) throws Exception {

        this.plugin = plugin;
		this.downloadUrl = downloadUrl;

		this.pluginJar = pluginJar;
        this.name = name;
        downloaded = 0L;

        setName("Auto-Update");
        setDaemon(false);
    }

    @Override
    public void run() {

        try{

			connection = (HttpURLConnection) downloadUrl.openConnection();
			connection.setRequestProperty("User-Agent", plugin.getDisplayName() + " Spigot Plugin");

			//Create a temp file. We dont want to delete the plugin if it is still in use, or if the download fails
			File tempDownloadFile = new File(pluginJar.getParentFile() + File.separator + name);
			tempDownloadFile.createNewFile();

            InputStream in = connection.getInputStream();
            outputStream = new FileOutputStream(tempDownloadFile);

            plugin.getCoLogger().info("Downloading update...");
			
            int count;
            while ((count = in.read(BUFFER, 0, 1024)) != -1) {
                outputStream.write(BUFFER, 0, count);
                downloaded += count;
            }
           // logger.cancel();

            outputStream.close();
            in.close();
            plugin.getCoLogger().info("Update downloaded!");

			//Unload the plugin
			PluginUtil.unload(plugin);
			
			//Delete the original jar
			if (!pluginJar.getAbsoluteFile().delete()){
				//If it failed
				throw new RuntimeException("Failed to delete the original plugin!");
			}

			//Enable the new jar
			Plugin updatedPlugin = PluginUtil.load(tempDownloadFile);
			if (updatedPlugin == null){
				throw new RuntimeException("Failed enable update!");
			}

			updatedPlugin.getLogger().log(Level.INFO, "Successfully updated!");

        } catch (Exception e) {
            if (outputStream != null) try { outputStream.close(); } catch (Exception ex) {}
            e.printStackTrace();
        }
    }
}
