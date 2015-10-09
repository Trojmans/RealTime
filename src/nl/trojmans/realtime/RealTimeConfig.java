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
	private RealTimeFormatter.version timeFormatVersion;
	public RealTimeConfig(RealTime plugin){
		this.plugin = plugin;
		config = plugin.getConfig();
		config.options().copyDefaults(true);
		plugin.saveConfig();
		if(!reload()){
			for(Player p : Bukkit.getOnlinePlayers()) if(p.isOp()) p.sendMessage(ChatColor.RED + "");
		}
	}
	public boolean reload(){
		try{
			inEveryWorldEnabled = config.getBoolean("inEveryWorldEnabled");
			enabledWorlds = (List<String>) config.getList("enabledWorlds");
			timeFormatVersion = RealTimeFormatter.version.valueOf(config.getString("timeFormatVersion"));
			return true;
		}catch(Exception e){//Config is invalid
			for(String variables : config.getKeys(false)){//Clearing configs
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
	public RealTimeFormatter.version getTimeFormatVersion(){
		return timeFormatVersion;
	}
}
