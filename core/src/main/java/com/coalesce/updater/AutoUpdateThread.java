package com.coalesce.updater;

import com.coalesce.plugin.CoPlugin;
import com.coalesce.plugin.PluginUtil;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

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
			File tempDownloadFile = new File(pluginJar.getParent(), pluginJar.getName() + "-TEMP-" + System.currentTimeMillis());

            InputStream in = connection.getInputStream();
            outputStream = new FileOutputStream(tempDownloadFile);

            plugin.getCoLogger().info("Downloading update...");
            //UpdateRunnableLogger logger = new UpdateRunnableLogger(this);
            //logger.runTaskTimerAsynchronously(plugin, 20L, 20L);
			
            // I know there are at least a billion better ways to do this, but my intention is to log every second the current progress.
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
			//TODO: FIX THIS. This is the only reason why it isnt working rn
			PluginUtil.unload(plugin);

			//Delete the original jar
			if (!pluginJar.delete()){
				//If it failed
				throw new RuntimeException("Failed to delete the original plugin!");
			}

			//Rename the temp file
			tempDownloadFile.renameTo(pluginJar);

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

    private static class UpdateRunnableLogger extends BukkitRunnable{

        private AutoUpdateThread autoUpdateThread;
        private String totalSize;
        private long downloadSpeedLocal;

        private UpdateRunnableLogger(AutoUpdateThread autoUpdateThread) {

            this.autoUpdateThread = autoUpdateThread;
            totalSize = bytes(autoUpdateThread.connection.getContentLengthLong());

        }

        @Override
        public void run(){

            long downloaded = autoUpdateThread.downloaded;
            long extraDownloaded = downloaded - downloadSpeedLocal;
            downloadSpeedLocal = downloaded;

            autoUpdateThread.plugin.getCoLogger().info("Download Progress: " + bytes(downloaded) + "/" + totalSize + " " + bytes(extraDownloaded) + "/s");

        }

        private String bytes(long n){

            if(n <= 999) return n + "B";
            else if(n >= 1000 && n <= 999999) return (float) ((int) n / 100) / 10 + "KB";
            else if(n >= 1000000 && n <= 999999999) return (float) ((int) n / 100000) / 10 + "MB";
            else if(n >= 10000000 && n <= 999999999999L) return (float) ((int) n / 100000000) / 10 + "GB";
            else return (float) ((int) n / 100000000000L) / 10 + "TB";

        }

    }

}
