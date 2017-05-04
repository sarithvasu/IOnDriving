package com.ion.iondriving.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;


import com.ion.iondriving.MonitorSpeedActivity;
import com.ion.iondriving.macro.MacroConstant;
import com.ion.iondriving.utilities.DPBusinessEvent;
import com.ion.iondriving.utilities.DPConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import static com.ion.iondriving.macro.MacroConstant.METER_PER_SECOND_TO_KPH_CONVERSION_FACTOR;
import static com.ion.iondriving.macro.MacroConstant.acceleration_value;
import static com.ion.iondriving.macro.MacroConstant.breaking_value;


public class DPLocationManager implements LocationListener {
    private double m_currentSpeed;
    private boolean m_isInitialized_Location;
    private Location m_lastLocation;
    private double m_currentManagerSpeed;
    private double m_currentManagerSpeedOld;


    private LocationManager m_locationManager;
    private Location m_location;
    private boolean m_Is_Invalid_GPSSignal;

    private LocationListener m_locationListener = null;
    /*;

	public boolean m_pausesLocationUpdatesAutomatically ;

	 */


    Context m_context;
    private static DPLocationManager instance_1;
    TextView lblspeed, lblspeedUnit;


    // ## Single Instance Created
    public static DPLocationManager getSingletonInstance() {
        if (instance_1 == null) {
            instance_1 = new DPLocationManager();
        }
        return instance_1;
    }

    // ## Default Constructor
    private DPLocationManager() {
    }

    // ## Start -: Public Method
    //Manas Blocked This  for Testing
	/*public boolean startMonitoringLocationManager(Context m_context)
	{
		this.m_context=m_context;
		System.out.println(MacroConstant.DP_FLOW+"Start >>>>>>>>>>startMonitoringLocationManager");
		boolean result = false;

		try 
		{
			initializeLocationManager(m_context);
			// Getting the name of the provider that meets the criteria
			String strProviderName = this.getProviderName(m_context);
			boolean isGPS_Enabled = m_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (!isGPS_Enabled) {
				Toast.makeText(m_context, "Please Enable the Gps", 5000).show();
				return false;
			}

			if (isGPS_Enabled) 
			{
				if(strProviderName!=null && !strProviderName.equals(""))
				{
					// Get the location from the given provider
					this.m_location = this.m_locationManager.getLastKnownLocation(strProviderName);




					// Start Location Update === >>> Configure LocationManager with Update Interval and Distance Filter 
					this.m_locationManager.requestLocationUpdates(strProviderName, MacroConstant.UPDATE_INTERVAL_IN_MILLISECONDS, MacroConstant.DISTANCE_FILTER_IN_METERS, this);

					result= true;
				}
				else
				{
					// soumen write to log file
					System.out.println(MacroConstant.DP_FLOW+"Gps is Not Enable in The Device....");
					result= false;
				}
			} 
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}

		// return
		return result;
	}*/


    public boolean startMonitoringLocationManager(Context m_context, TextView lblspeed, TextView lblspeedUnit) {
        this.lblspeedUnit = lblspeedUnit;
        this.m_context = m_context;
        this.lblspeed = lblspeed;
        System.out.println(MacroConstant.DP_FLOW + "Start >>>>>>>>>>startMonitoringLocationManager");
        boolean result = false;

        try {
            Location arr = new Location("");
            System.out.println("" + arr);
            arr.setLatitude(12.345);
            arr.setLongitude(77.345);
            arr.setAccuracy(2);
            //m_lastLocation=get


            //Manas for Desktop Purpose code


            initializeLocationManager(m_context);
            // Getting the name of the provider that meets the criteria
            String strProviderName = this.getProviderName(m_context);
            System.out.println("provider Name is" + strProviderName);
            boolean isGPS_Enabled = m_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPS_Enabled) {
                System.out.println("please  Enable Gps" + isGPS_Enabled);
                //	Toast.makeText(m_context, "Please Enable the Gps", 5000).show();
                return false;
            }

            if (isGPS_Enabled) {
                if (strProviderName != null && !strProviderName.equals("")) {

                    System.out.println("Location is" + this.m_location);
                    // Get the location from the given provider
                    if (ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return false;
                    }
                    this.m_location = this.m_locationManager.getLastKnownLocation(strProviderName);
                    System.out.println("Location is" + this.m_location);
                    // Start Location Update === >>> Configure LocationManager with Update Interval and Distance Filter
                    m_locationManager.requestLocationUpdates(strProviderName, 0, 0, this);

                    DPMotionManager.getSingletonInstance().waypointWithLocation(m_location, 0.0, lblspeed, lblspeedUnit);
                    result = true;
                } else {
                    // soumen write to log file
                    System.out.println(MacroConstant.DP_FLOW + "Gps is Not Enable in The Device....");
                    result = false;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        // return
        return result;
    }

    public void stopMonitoringLocationManager() {
        System.out.println(MacroConstant.DP_FLOW + "start>>>>>>>>>>>>>>>stopMonitoringLocationManager");

        this.stopLocationUpdates();

        System.out.println(MacroConstant.DP_FLOW + "End>>>>>>>>>>>>>>>stopMonitoringLocationManager");
        MonitorSpeedActivity.m_ttsManager.
                initQueue("");

    }

    public void initializeLocationManager(Context m_Context) {
        if (m_locationManager != null) {
            m_locationManager = null;
            System.out.println("Now  the  location is" + m_locationManager);
        }
        try {


            this.m_locationManager = (LocationManager) m_Context.getSystemService(Context.LOCATION_SERVICE);
            System.out.println("Now  the  location is" + m_locationManager);
            this.m_currentSpeed = 0.0f;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopLocationUpdates() {
        try {
            //Stop Location removeUpdate
            //this.m_locationManager.removeUpdates((LocationListener) this.m_locationManager);
            if (ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            this.m_locationManager.removeUpdates(this);

            this.m_isInitialized_Location = false;
            this.m_lastLocation = null;
            this.m_currentManagerSpeed = 0;
            this.m_currentManagerSpeedOld = 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reIntitilizeLocationManager() {
        try {
            System.out.println(MacroConstant.DP_FLOW + "Start>>>>>>>>>>>>>>>>>>>>>>>>reIntitilizeLocationManager");

            this.stopLocationUpdates();
            this.initializeLocationManager(m_context);

            System.out.println(MacroConstant.DP_FLOW + "Stop>>>>>>>>>>>>>>>>>>>>>>>>reIntitilizeLocationManager");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ## End -: Public Method


    /*@Override
    public void onLocationChanged(Location location)
    {
        try
        {
            this.m_location=location;
            System.out.println(MacroConstant.DP_FLOW+"OnlocationChage Got Gps Speed..");

             Check GPS Last Location Time Stamp. If the time interval is more than 5 Seconds then do not consider that gps update and return it.
            We perform this because Core Locatin Framework store the last location update by default.
            Failing to do this check may cause sudden jump in speed reading due to stale location.
            //long howRecent = location.getTime()-this.m_lastLocation.getTime(); // tobe verify

            long howRecent = Calendar.getInstance().getTimeInMillis()- location.getTime(); // tobe verify
            if (howRecent > MacroConstant.STALE_LOCATION_TIME_INTERVAL_IN_MILLISECONDS)
            {
                return;
            }
            else
            {
                double managerSpeed = location.getSpeed();
                // managerSpeed = managerSpeed/0.92; (Only For India)

                if (managerSpeed >= 0)
                {
                    this.m_Is_Invalid_GPSSignal = false;

                    this.m_currentManagerSpeed = managerSpeed;
                    this.m_currentManagerSpeedOld = this.m_currentManagerSpeed;

                    // Pass this location information to DP Motion Manager class
                    DPMotionManager.getSingletonInstance().waypointWithLocation(location, this.m_currentManagerSpeed);
                }
                if(this.m_lastLocation.getAccuracy() == 0) //0 indicates invalid GPS Reading
                {
                    this.gpsInterpputWithInterruptStatus("Signal Lost");
                    this.m_Is_Invalid_GPSSignal = true;
                }

                this.gpsSignalWithSpeed();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/
    DPBusinessEvent dpBusinessEvent = new DPBusinessEvent();

    //Manas Changed For  see  the log on Dec 15
    @Override
    public void onLocationChanged(Location location) {
        try {
            System.out.println(MacroConstant.DP_FLOW + "Location is-----------" + location);
            this.m_location = location;
            System.out.println(MacroConstant.DP_FLOW + "+++++++OnlocationChage Got Gps Speed..");
            Toast.makeText(m_context, "Location" + m_location.getLatitude() + " " + m_location.getLongitude(), Toast.LENGTH_SHORT).show();


            /* Check GPS Last Location Time Stamp. If the time interval is more than 5 Seconds then do not consider that gps update and return it.
			We perform this because Core Locatin Framework store the last location update by default. 
			Failing to do this check may cause sudden jump in speed reading due to stale location.*/
            //long howRecent = location.getTime()-this.m_lastLocation.getTime(); // tobe verify


            String str1 = DPConverter.LongToDateInString(location.getTime(), MacroConstant.APPLICATION_TIME_FORMAT_12_HOURS);
            String str2 = DPConverter.LongToDateInString(Calendar.getInstance().getTimeInMillis(), MacroConstant.APPLICATION_TIME_FORMAT_12_HOURS);


            System.out.println(MacroConstant.DP_FLOW + "Location received at " + str1);
            System.out.println(MacroConstant.DP_FLOW + "Current Time is  " + str2);

            long howRecent = DPConverter.DeltaInSeconds(str1, str2) * 1000;


            //	long howRecent = Calendar.getInstance().getTimeInMillis()- location.getTime(); // tobe verify
            System.out.println(MacroConstant.DP_FLOW + "how recent is >>>" + howRecent);


			/*if (howRecent > MacroConstant.STALE_LOCATION_TIME_INTERVAL_IN_MILLISECONDS) 
			{
				System.out.println(MacroConstant.DP_FLOW+"Location Return From Here");
				return;
			}*/
            //else

            {
                double managerSpeed = location.getSpeed();//location.getSpeed() will return meter/second
                //managerSpeed = managerSpeed/0.92; //(Only For India)
                Log.e("DPLocationManager", "" + managerSpeed);

                managerSpeed = managerSpeed * METER_PER_SECOND_TO_KPH_CONVERSION_FACTOR;
                // has manager Speed is meterper second we are converting the meter per second to km per hour 1 mps = 3.6 kph so i am mulitpling that
                System.out.println(MacroConstant.DP_FLOW + "Current Manager Speed...." + managerSpeed);

                if (managerSpeed >= 0) {
                    this.m_Is_Invalid_GPSSignal = false;


                    this.m_currentManagerSpeed = managerSpeed;
                    this.m_currentManagerSpeedOld = this.m_currentManagerSpeed;
                    System.out.println(MacroConstant.DP_FLOW + "instance of....." + this.m_currentManagerSpeed + "old speed" + this.m_currentManagerSpeedOld);

                    // Pass this location information to DP Motion Manager class


                int i= dpBusinessEvent.AccelerationBreakingAudioAlert(managerSpeed, 10f,m_context);
                if(i == acceleration_value){
                    DPMotionManager.getSingletonInstance().waypointWithLocation(location, this.m_currentManagerSpeed, lblspeed, lblspeedUnit);

                }else if(i == breaking_value){
                    DPMotionManager.getSingletonInstance().waypointWithLocation(location, this.m_currentManagerSpeed, lblspeed, lblspeedUnit);
                }else {
                    DPMotionManager.getSingletonInstance().waypointWithLocation(location, this.m_currentManagerSpeed, lblspeed, lblspeedUnit);

                }
                }
                //Manas Blocked  on 2nd Jan 2014
				/*if(this.m_lastLocation.getAccuracy() == 0) //0 indicates invalid GPS Reading
				{
					this.gpsInterpputWithInterruptStatus("Signal Lost");
					this.m_Is_Invalid_GPSSignal = true;

					System.out.println(MacroConstant.DP_FLOW+"Invalid GPS Readings");
				}*/
                this.gpsSignalWithSpeed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gpsSignalWithSpeed() {
        System.out.println(MacroConstant.DP_FLOW + "m_Is_Invalid_GPSSignal ==" + this.m_Is_Invalid_GPSSignal);

        if (this.m_Is_Invalid_GPSSignal) {
            invalidGPSSignal();
            System.out.println(MacroConstant.DP_FLOW + "Invalid GPS Signal");
        }
    }

    private void invalidGPSSignal() {
        System.out.println(MacroConstant.DP_FLOW + "m_Is_Invalid_GPSSignal ==" + this.m_Is_Invalid_GPSSignal);

        if (this.m_Is_Invalid_GPSSignal) {
            // Pass this location information to DP Motion Manager class
            DPMotionManager.getSingletonInstance().waypointWithLocation(this.m_location, 0.0, lblspeed, lblspeedUnit);
            System.out.println(MacroConstant.DP_FLOW + "fourcfully set to zero speed");

        }
    }

    public void gpsInterpputWithInterruptStatus(String interruptStatus) {
        // TODO Auto-generated method stub
        try {
            if (DPMotionManager.getSingletonInstance().hasCrossedMinimumSpeed()) {

                Date date = Calendar.getInstance().getTime();

                SimpleDateFormat APPLICATION_DATE_FORMAT = new SimpleDateFormat(MacroConstant.APPLICATION_DATE_FORMAT);
                String currentDate = APPLICATION_DATE_FORMAT.format(date);

                SimpleDateFormat APPLICATION_TIME_FORMAT_12_HOURS = new SimpleDateFormat(MacroConstant.APPLICATION_TIME_FORMAT_12_HOURS);
                String currenttime = APPLICATION_TIME_FORMAT_12_HOURS.format(date);


                // build the interrupt details
                Dictionary<String, String> dictGPSInterrupt = new Hashtable<String, String>();

                dictGPSInterrupt.put(MacroConstant.DATE, currentDate);
                dictGPSInterrupt.put(MacroConstant.START_TIME, currenttime);
                dictGPSInterrupt.put(MacroConstant.END_TIME, "N/A");
                dictGPSInterrupt.put(MacroConstant.DURATION, "N/A");
                dictGPSInterrupt.put(MacroConstant.INTERRUPT_TYPE, "GPS");
                dictGPSInterrupt.put(MacroConstant.INTERRUPT_STATUS, interruptStatus);

                // interrupt object is build.... now push it to the collection.
                //[[DPInterruptHandler sharedInstance]addGPSInterruptToCollection:dictGPSInterrupt]
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private String getProviderName(Context m_conContext) {
        LocationManager locationManager = (LocationManager) m_conContext
                .getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_HIGH); // Chose your desired power consumption level.
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy requirement.
        criteria.setSpeedRequired(true); // Chose if speed for first location fix is required.
        criteria.setAltitudeRequired(false); // Choose if you use altitude.
        criteria.setBearingRequired(false); // Choose if you use bearing.
        criteria.setCostAllowed(false); // Choose if this provider can waste money

        // Provide your criteria and flag enabledOnly that tells
        // LocationManager only to return active providers.
        return locationManager.getBestProvider(criteria, true);

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub


        if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {

            this.gpsInterpputWithInterruptStatus("GPS Failed");

        }

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

        this.gpsInterpputWithInterruptStatus("Called when User off Gps in phone setting");

    }

	/*public static void File() {

		String text = "hello";

		try {
			File directory = new File(Environment.getExternalStorageDirectory(),"logger" );
			if(!directory.exists()){
				directory.mkdir();
			}

			try {
				File file = new File(directory, "log"+".txt");
				file.createNewFile();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");

				String currentDateandTime = sdf.format(new Date());
				//text+= currentDateandTime+"";
				text= currentDateandTime+":-"+text;

				Writer writer = new BufferedWriter(new FileWriter(file)); 
				writer.append(text);
				writer.flush();
				writer.close();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("ex: " + e);
			}
		} 
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("ex: " + e);
		}

	}*/

}
