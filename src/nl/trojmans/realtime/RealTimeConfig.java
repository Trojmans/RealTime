package nl.trojmans.realtime;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RealTimeConfig {

	private Plugin plugin;
	private FileConfiguration config;
	private boolean inEveryWorldEnabled;
	private List<String> enabledWorlds;
	private String fallbackCountry;
	private String fallbackRegion;
	public boolean cache;
	public RealTimeConfig(RealTime plugin){
		this.plugin = plugin;
		config = plugin.getConfig();
		config.options().copyDefaults(true);
		plugin.saveConfig();
		if(!reload()){
			for(Player p : Bukkit.getOnlinePlayers()) if(p.isOp()) p.sendMessage(ChatColor.RED + "");
		}
	}
	@SuppressWarnings("unchecked")
	public boolean reload(){
		try{
			inEveryWorldEnabled = config.getBoolean("inEveryWorldEnabled");
			enabledWorlds = (List<String>) config.getList("enabledWorlds");
			fallbackCountry = config.getString("fallbackCountry");
			fallbackRegion = config.getString("fallbackRegion");
			cache = config.getBoolean("cache");
			return true;
		}catch(Exception e){//Config is invalid
			for(String variables : config.getKeys(false)){
				config.set(variables, null);
			}
			plugin.saveDefaultConfig();
			return false;
		}
	}
	public boolean isInEveryWorldEnabled(){
		return inEveryWorldEnabled;
	}
	public List<String> getEnabledWorlds(){
		return enabledWorlds;
	}
	public String getFallbackCountry() {
		return fallbackCountry;
	}
	public String getFallbackRegion() {
		return fallbackRegion;
	}
	public boolean getCache() {
		return cache;
	}
}
