package nl.trojmans.realtime;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RealTime extends JavaPlugin{

	private static RealTime plugin;
	private RealTimePacketAdapter realTimePacketAdapter;
	private RealTimeConfig realTimeConfig;
	private RealTimeUserManager userManager;
	
	public void onEnable(){
		plugin = this;
		userManager = new RealTimeUserManager(this);
		realTimeConfig = new RealTimeConfig(this);
		realTimePacketAdapter = new RealTimePacketAdapter(this);
		
		Bukkit.getPluginCommand("realtimereloadconfig").setExecutor(new RealTimeReloadConfigCommand());
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
