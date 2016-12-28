package nl.trojmans.realtime;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class RealTimePacketAdapter extends PacketAdapter{
	
	private RealTime plugin;
	private RealTimeFormatter format;
	
	public RealTimePacketAdapter(RealTime plugin){
		super(plugin, PacketType.Play.Server.UPDATE_TIME);
		format = new RealTimeFormatter();
		this.plugin = plugin;
		ProtocolLibrary.getProtocolManager().addPacketListener(this);
	}
	
	@Override
	public void onPacketSending(PacketEvent e) {

		if(e.getPacketType() != PacketType.Play.Server.UPDATE_TIME)return;
		
		Player player = e.getPlayer();
		RealTimeUser user = plugin.getUserManager().getUser(player);

		if(!plugin.getRealTimeConfig().isInEveryWorldEnabled()){
			boolean inEnabledWorld = false;
			for(String world : plugin.getRealTimeConfig().getEnabledWorlds()){
				if(player.getWorld().getName() == world){
					inEnabledWorld = true;
					break;
				}
			}
			if(!inEnabledWorld) return;
		}
		
		try{
			
			Object packet = format.format(user, e.getPacket().getLongs().getValues().get(0));

			e.getClass().getMethod("setPacket" , PacketContainer.class).invoke(
					e,PacketContainer.class.getConstructor(PacketType.class, Object.class)
					.newInstance(PacketType.Play.Server.UPDATE_TIME, packet));

			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
