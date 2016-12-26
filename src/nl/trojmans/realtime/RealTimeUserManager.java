package nl.trojmans.realtime;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;

public class RealTimeUserManager implements Listener{
	
	private HashMap<Player,RealTimeUser> users;
	private DatabaseReader database;
	private File dbFile;
	private RealTimeConfig config;
	public RealTimeUserManager(RealTime plugin){
		users = new HashMap<Player,RealTimeUser>();
		config = plugin.getRealTimeConfig();
		try {
			dbFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "GeoLite2.mmdb");
			if (config.getCache())
				database = new DatabaseReader.Builder(dbFile).withCache(new CHMCache()).build();
			else
				database = new DatabaseReader.Builder(dbFile).build();
		} catch (IOException e) {
			database = null;
			System.out.println("[RealTime] Database not found. Using fallback data.");
		}
		Bukkit.getPluginManager().registerEvents(this, plugin);

		for(Player p : Bukkit.getOnlinePlayers()){
			users.put(p, new RealTimeUser(database, p.getAddress().getHostName(), config));
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
		if (database == null) {
			try {
				database = new DatabaseReader.Builder(dbFile).withCache(new CHMCache()).build();
			} catch (IOException ex) {
				database = null;
				System.out.println("[RealTime] Database not found. Using fallback data.");
			}
		}
		users.put(e.getPlayer(), new RealTimeUser(database, e.getAddress().getHostAddress(), config));
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		users.remove(e.getPlayer());
	}

}
