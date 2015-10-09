package nl.trojmans.realtime;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class RealTimeUserManager implements Listener{
	
	private HashMap<Player,RealTimeUser> users;
	public RealTimeUserManager(RealTime plugin){
		users = new HashMap<Player,RealTimeUser>();
		Bukkit.getPluginManager().registerEvents(this, plugin);
		for(Player p : Bukkit.getOnlinePlayers()){
			users.put(p, new RealTimeUser(p.getAddress().getHostName()));
		}
	}
	public void addUser(Player player, RealTimeUser user){
		users.put(player, user);
	}
	public RealTimeUser getUser(Player player){
		return users.get(player);
	}
	public Player getPlayer(RealTimeUser user){
		for(Entry<Player, RealTimeUser> entry : users.entrySet()){
			if(entry.getValue() == user)return entry.getKey();
		}
		return null;
	}
	public void removeUser(RealTimeUser user){
		users.remove(user);
	}
	@EventHandler(ignoreCancelled=false)
	public void onPlayerLogin(PlayerLoginEvent e){
		users.put(e.getPlayer(), new RealTimeUser(e.getAddress().getHostAddress()));
	}

}
