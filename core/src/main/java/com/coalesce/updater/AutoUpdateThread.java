package com.coalesce.updater;

import com.coalesce.Core;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

public class AutoUpdateThread extends Thread {

    private static final byte[] BUFFER = new byte[1024];
    private FileOutputStream output;
    private HttpURLConnection connection;
    private CoPlugin plugin;
    private long downloaded;

    public AutoUpdateThread(CoPlugin plugin, URL url) throws Exception {

        this.plugin = plugin;
        downloaded = 0L;

        setName("Auto-Update");
        setDaemon(false);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", plugin.getDisplayName() + " Spigot Plugin");

    }

    @Override
    public void run() {

        try{
            File file = (File) Core.getInstance().getFileField().get(plugin);
            file.createNewFile();
            InputStream in = connection.getInputStream();

            output = new FileOutputStream(file);

            plugin.getCoLogger().info("Downloading update...");
            UpdateRunnableLogger logger = new UpdateRunnableLogger(this);
            logger.runTaskTimerAsynchronously(plugin, 20L, 20L);

            // I know there are at least a billion better ways to do this, but my intention is to log every second the current progress.
            int count;
            while ((count = in.read(BUFFER, 0, 1024)) != -1) {
                output.write(BUFFER, 0, count);
                downloaded += count;
            }
            logger.cancel();

            output.close();
            in.close();

            plugin.getCoLogger().info("Update succeeded.");
        } catch (Exception e) {
            if (output != null) try { output.close(); } catch (Exception ex) {}
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
