package nl.trojmans.realtime;

import java.util.Calendar;
import java.util.TimeZone;

import org.bukkit.Bukkit;

public class RealTimeFormatter {
	
	private String nms = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + ".";
	
	@SuppressWarnings("static-access")
	public Object format(RealTimeUser user, long worldTime) throws Exception{
		Calendar cal = Calendar.getInstance();
				
		long timeZoneTime = Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis()
				+ user.getTimeZone().getRawOffset();
		
		int timeUntilMoonGoesThrougThe8Phases = 192000;
		
		long time = (long)  (timeZoneTime / 3600  - 6000) % 24000;
		
		worldTime -= worldTime % timeUntilMoonGoesThrougThe8Phases;

		int moonPhase = moonPhase(cal.get(cal.YEAR),cal.get(cal.MONTH),cal.get(cal.DAY_OF_MONTH));
		moonPhase += 4;// The moon phases apear in a different order than the function calculates
		if (moonPhase > 7) moonPhase -= 8;
		
		time += moonPhase * 24000;
		worldTime += time;
		
		Object packet = Class.forName(nms + "PacketPlayOutUpdateTime")
				.getConstructor(long.class,long.class,boolean.class).newInstance(worldTime, (long) time,false);
		
		return packet;
	}
	
    private static final int    day_year[] = { -1, -1, 30, 58, 89, 119, 
        150, 180, 211, 241, 272, 
        303, 333 };
 
    
    
    public int  moonPhase(int year, int month, int day) {
        int             phase;          // Moon phase
        int             cent;           // Century number (1979 = 20)
        int             epact;          // Age of the moon on Jan. 1
        int             diy;            // Day in the year
        int             golden;         // Moon's golden number

        if (month < 0 || month > 12) month = 0;     // Just in case
        diy = day + day_year[month];                // Day in the year
        if ((month > 2) && this.isLeapYear(year)) 
            diy++;                                  // Leapyear fixup
        cent = (year / 100) + 1;                    // Century number
        golden = (year % 19) + 1;                   // Golden number
        epact = ((11 * golden) + 20                 // Golden number
                + (((8 * cent) + 5) / 25) - 5       // 400 year cycle
                - (((3 * cent) / 4) - 12)) % 30;    //Leap year correction
        if (epact <= 0)
                epact += 30;                        // Age range is 1 .. 30
        if ((epact == 25 && golden > 11) || 
            epact == 24)
                epact++;
                
        // Calculate the phase, using the magic numbers defined above.
        // Note that (phase and 7) is equivalent to (phase mod 8) and
        // is needed on two days per year (when the algorithm yields 8).

        phase = (((((diy + epact) * 6) + 11) % 177) / 22) & 7;

        return(phase);
    }
    int daysInMonth(int month,int year) {
        int result = 31;
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                result = 30;
                break;
            case 2:
                result = ( this.isLeapYear(year) ? 29 : 28 );
        }
        return result; 
    }
    public  boolean isLeapYear(int year) {
        return ((year % 4 == 0) && 
                ((year % 400 == 0) || (year % 100 != 0)));
    }

}
