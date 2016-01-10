package nl.trojmans.realtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimeZone;

import org.bukkit.entity.Player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RealTimeUser {
	
	private double latitude;
	private double longitude;
	private TimeZone timeZone;
	private short debugCount = 8;
	public RealTimeUser(String ip){
		try{
			String url = "http://geoplugin.net/json.gp?";
			if(!ip.equals("127.0.0.1")){//localhost
				url += "ip=" + ip;
			}
			JsonObject jo = readJSONFromURL(url).getAsJsonObject();
			
			latitude = Double.parseDouble(a(jo.get("geoplugin_latitude").toString()));
			longitude = Double.parseDouble(a(jo.get("geoplugin_longitude").toString()));

			String country = a(jo.get("geoplugin_countryCode").toString());
			String region = a(jo.get("geoplugin_regionCode").toString());
			
			timeZone = TimeZone.getTimeZone(com.maxmind.geoip.timeZone.timeZoneByCountryAndRegion(country, region));
						
		}catch(IOException ex){
			
			System.out.println("IOException in realtime plugin: " + ex.getMessage());
		}
	}
	private JsonElement readJSONFromURL(String url) throws IOException{
		URL webpageurl = new URL(url);
		URLConnection urlConnection = webpageurl.openConnection();
		InputStream is = urlConnection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);

		int numCharsRead;
		char[] charArray = new char[1024];
		StringBuffer sb = new StringBuffer();
		while ((numCharsRead = isr.read(charArray)) > 0) {
			sb.append(charArray, 0, numCharsRead);
		}
		String result = sb.toString();
		return new JsonParser().parse(result);
	}
	private String a(String string){
		return string.replaceAll(Character.toString('"'), "");
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the timeZone
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}
	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	/**
	 *@return the player
	 */
	 public Player getPlayer(){
		 return RealTime.getPlugin().getUserManager().getPlayer(this);
	 }
	 /**
	  * @return debugCount
	  */
	 public short getDebugCount(){
		 return debugCount;
	 }
	 public void setDebugCount(short debugCount){
		 this.debugCount = debugCount;
	 }
}
