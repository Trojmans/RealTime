package nl.trojmans.realtime;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;

public class AzimuthFormatter {
	
	private TimeZone gmt = TimeZone.getTimeZone("GMT");

	static DecimalFormat df = new DecimalFormat("#.0");
	public double getAzimuth(RealTimeUser user){
		LocalTime localTime = LocalTime.now();

		double lat = user.getLatitude();
		double lon = user.getLongitude();
		TimeZone timeZone = user.getTimeZone();
		
	    int day = dayOfYear();
	    double equationOfTime = equationOfTime(day);
	    double lstm = localTime(timeZone);
	    double lst = localSolarTime(equationOfTime, lon, lstm,localTime);
	    double declination = declination(day);
	    double hourAngle = hourAngle(lst);

	    double zenith = zenith(lat,declination,hourAngle);
	    double azimuth = azimuth(lon, declination, zenith, hourAngle); 
	    return azimuth;
	}
	private double localTime(TimeZone timeZone){
	    int td = gmt.getRawOffset() - timeZone.getRawOffset();
	    double localStandardTimeMeridian = 15 * (td/(1000*60*60)); //convert td to hours

	    return localStandardTimeMeridian;
	}
	private double azimuth(double lat, double declination, double zenith, double hourAngle){
	  double elevation = Math.toRadians(90 - zenith);
	  lat = Math.toRadians(lat);
	  declination = Math.toRadians(declination);
	  hourAngle = Math.toRadians(hourAngle);
	  double azimuthRadian = Math.acos(((Math.sin(declination) * Math.cos(lat)) - (Math.cos(hourAngle) * Math.cos(declination) * Math.sin(lat))) / Math.cos(elevation));
	  double azimuthDegree = Math.toDegrees(azimuthRadian);
	  if (hourAngle > 0)
	    azimuthDegree = 360 - azimuthDegree;
	  
	  return azimuthDegree;
	}		
	private double localSolarTime(double equationOfTime, double longitude, double lstm,LocalTime localTime) { 
	    //LocalSolarTime = 4min * (longitude + localStandardTimeMeridian) + equationOfTime
	    //Time Correction is time variation within given time zone (minutes)
	    //longitude = longitude/60; //convert degrees to arcminutes
	    double localStandardTimeMeridian = lstm;
	    double timeCorrection = (4 * (longitude + localStandardTimeMeridian) + equationOfTime);
	    //localSolarTime represents solar time where noon represents sun's is highest position 
	    // in sky and the hour angle is 0 -- hour angle is negative in morning, and positive after solar noon.
	    double localSolarTime = (localTime.toSecondOfDay() + (timeCorrection*60)); //(seconds)
	    localSolarTime = localSolarTime/(60*60);  //convert from seconds to hours

	    return localSolarTime;
	}
	private double hourAngle(double localSolarTime) {
		
	    double hourAngle = 15 * (localSolarTime - 13);
	    
	    return hourAngle;
	}
	private double declination(int dayOfYear) {
	    double declination = 23.5*Math.sin((Math.toRadians(360.0/365.0))*(dayOfYear - 81));

	    return declination;
	}
	private double equationOfTime (double day) {
	    double d =(360.0/365.0)*(day - 81);
	    d = Math.toRadians(d);
	    double equationTime = 9.87*Math.sin(2*d)-7.53*Math.cos(d)-1.54*Math.sin(d); 

	    return equationTime;
	}
	private int dayOfYear() {
	    Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
	    int dayOfYear = localCalendar.get(Calendar.DAY_OF_YEAR); 

	    return dayOfYear;
	}

	private double zenith(double lat, double declination, double hourAngle) {
	    lat = Math.toRadians(lat);
	    declination = Math.toRadians(declination);
	    hourAngle = Math.round(hourAngle);
	    hourAngle = Math.toRadians(hourAngle);
	    //Solar Zenith Angle 
	    double zenith = Math.toDegrees(Math.acos(Math.sin(lat)*Math.sin(declination) + (Math.cos(lat)*Math.cos(declination)*Math.cos(hourAngle))));
	    //Solar Elevation Angle
	   // double elevation = Math.toDegrees(Math.asin(Math.sin(lat)*Math.sin(declination) + (Math.cos(lat)*Math.cos(declination)*Math.cos(hourAngle))));
	
	    return zenith;
	}
}
