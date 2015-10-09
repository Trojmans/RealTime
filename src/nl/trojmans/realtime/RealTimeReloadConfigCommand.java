package nl.trojmans.realtime;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RealTimeReloadConfigCommand implements CommandExecutor{

	private RealTimeConfig config = RealTime.getPlugin().getRealTimeConfig();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] arguments) {
		
		if(!sender.isOp()){
			sender.sendMessage(ChatColor.RED + "This is command is only for administators");
			return true;
		}
		if(config.reload()){
			sender.sendMessage(ChatColor.GREEN + "config reloaded!");
			return true;
		}else{
			sender.sendMessage(ChatColor.RED + "config invalid! config changed to default config");
			return false;
		}
	}
}
