package nl.trojmans.realtime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RealTime extends JavaPlugin{

	private static RealTime plugin;
	private RealTimePacketAdapter realTimePacketAdapter;
	private RealTimeConfig realTimeConfig;
	private RealTimeUserManager userManager;
	private File dbFile;
	
	public void onEnable(){
		plugin = this;
		realTimeConfig = new RealTimeConfig(this);
		userManager = new RealTimeUserManager(this);
		realTimePacketAdapter = new RealTimePacketAdapter(this);
		dbFile = new File(getDataFolder().getAbsolutePath() + "\\GeoLite2.mmdb");

		Bukkit.getPluginCommand("realtimereloadconfig").setExecutor(new RealTimeReloadConfigCommand());
		System.out.println("[RealTime] This product includes GeoLite2 data created by MaxMind, available from http://www.maxmind.com.");
		if (!dbFile.exists() || (System.currentTimeMillis() - dbFile.lastModified()) > TimeUnit.DAYS.toMillis(30)) {
			getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
				@Override
				public void run() {
					try {
						if (dbFile.exists())
							dbFile.delete();
						URLConnection conn = new URL("http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz").openConnection();
						conn.setConnectTimeout(10000);
						conn.connect();
						InputStream input = conn.getInputStream();
						if (conn.getURL().toString().endsWith(".gz")) {
							input = new GZIPInputStream(input);
						}
						OutputStream output = new FileOutputStream(getDataFolder().getAbsolutePath() + File.separator + "GeoLite2.mmdb");
						byte[] buffer = new byte[2048];
						int length = input.read(buffer);
						while (length >= 0) {
							output.write(buffer, 0, length);
							length = input.read(buffer);
						}
						output.close();
						input.close();
					} catch (IOException e) {
						System.out.println("[RealTime] Could not download GeoLite2 Database:\n" + e);
					}
				}
			});
		}
	}
	public void onDisable(){
		
	}
	/**
	 * 	
	 * @return the RealTime plugin
	 */
	public static RealTime getPlugin() {
		return plugin;
	}
	/**
	 * 
	 * @return the packet adapter of the RealTime plugin
	 */
	public RealTimePacketAdapter getRealTimePacketAdapter(){
		return realTimePacketAdapter;
	}
	/**
	 * 
	 * @return the config of the RealTime plugin
	 */
	public RealTimeConfig getRealTimeConfig(){
		return realTimeConfig;
	}
	/**
	 * 
	 * @return the usermanager of the RealTime plugin
	 */
	public RealTimeUserManager getUserManager(){
		return userManager;
	}
}
