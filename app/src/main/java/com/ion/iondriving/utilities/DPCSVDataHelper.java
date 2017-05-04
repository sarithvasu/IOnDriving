//  
// Copyright (C) Effone Technologies private Limited All rights reserved. 
// 



package com.ion.iondriving.utilities;

import android.location.Location;
import android.util.Log;


import com.ion.iondriving.core_logic.ABCCalculator;
import com.ion.iondriving.dpconverter.VectorBase;
import com.ion.iondriving.macro.MacroConstant;
import com.ion.iondriving.model.CSVDataCollection;
import com.ion.iondriving.model.DPFileHandler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

/**
 *<pre> Description of the class.
 *</pre>
 *<pre>Author Effone Technologies Private Limited on 03/03/2015.</pre>
 * */ 
public class DPCSVDataHelper {

	private boolean m_isDPCSVDataInitialized;
	private  CSVDataCollection m_dpcsvDataList;
	private boolean m_isInitilizedLocation;
	private Location m_oldLocation;
	private float m_totalDistanceTravelled;
	private Date m_previousLocationTimeStamp;
	private DPFileHandler m_dpFilehandler;
	private boolean m_isLocationTImeStampInitilized; 
	private int m_scaleSingnalStrength;

	Dictionary<String, ArrayList<DPCSVData>> m_arrResult =new Hashtable<String, ArrayList<DPCSVData>>();
	List calculatedDistnaceList=new ArrayList();
	private DPFileHandler m_DPFileHandler ;
	private static DPCSVDataHelper instance_1;
	/**
	 * Create  the Singleton instance of  DPCSVDataHelper now.
	 */



	public static DPCSVDataHelper getSingletonInstance(){

		if (instance_1 == null) {

			instance_1 = new DPCSVDataHelper();
		}
		return instance_1;

	}


	/**
	 * Add  the  data  to DPCSVData.
	 *
	 * @param abcCaluculator is the instance of ABCCalculator class
       {@ abcCaluculator}
        @param  currentLocation the instance of Location class which contains lat,long,speed etc.
       {@ currentLocation}
       @param rawAccelerometerData is the instance of VectorBase class for Accelerometer.
       {@rawAccelerometerData}
       @param rawGyroscopeData is the instance of VectorBase class for Gyroscope.
       {@rawGyroscopeData}
       @param currentSpeed is a double value which contains speed
	 * <p>
	 * Created by effone Technologies Private Limited on 03/03/15.
     </p>


	 */

	public boolean buildCSVDataAndAddToCollection(ABCCalculator abcCaluculator, Location currentLocation, VectorBase rawAccelerometerData, VectorBase rawGyroscopeData, double currentSpeed,int breakingValue)
	{

		System.out.println(MacroConstant.DP_FLOW+"Accelerometer Data...."+rawAccelerometerData.getX()+rawAccelerometerData.getY()+rawAccelerometerData.getZ());
		/*if (abcCaluculator == null)
		{
			return false;
		}*/

		boolean isSuccessFullyAddedToCollection = true;

		try {

			DPCSVData dpcsvData = new DPCSVData();

//			dpcsvData.setScore(DPConverter.ConvertDoubleWithPreseision(abcCaluculator.GetCaculatedScore(), 1));
			dpcsvData.setLatitude(DPConverter.ConvertDoubleWithPreseision(currentLocation.getLatitude(), 6));
			dpcsvData.setLongitude(DPConverter.ConvertDoubleWithPreseision(currentLocation.getLongitude(),6));

			dpcsvData.setSpeed(currentSpeed);
			/*dpcsvData.setAcceleration(DPConverter.ConvertDoubleWithPreseision(abcCaluculator.GetCaculatedAcceleration(),1));
			dpcsvData.setBreaking(DPConverter.ConvertDoubleWithPreseision(abcCaluculator.GetCaculatedBreaking(),1));
			dpcsvData.setCornerning(DPConverter.ConvertDoubleWithPreseision(abcCaluculator.GetCaculatedCornering(),1));*/


			String strDate =DPConverter.LongToDateInString(currentLocation.getTime(), MacroConstant.APPLICATION_DATE_FORMAT);
			dpcsvData.setDate(strDate);	

			//Manas Done in 27 th May 2015

			if(currentLocation.getAccuracy() <= 40)
			{
				m_scaleSingnalStrength = 1;
			}
			else  if(currentLocation.getAccuracy() > 40 && currentLocation.getAccuracy() <= 60)
			{
				m_scaleSingnalStrength =2;
			}
			else  if(currentLocation.getAccuracy() > 60 && currentLocation.getAccuracy()<= 150)
			{
				m_scaleSingnalStrength = 3;
			}
			else
			{
				m_scaleSingnalStrength = 4;
			}





			String strTime =DPConverter.LongToTimeInString(System.currentTimeMillis(), MacroConstant.APPLICATION_TIME_FORMAT_12_HOURS);
			dpcsvData.setTime(strTime);	
			System.out.println("Time Is"+dpcsvData.getTime()+"setTime"+strTime);
			//Manas  Blocked  bcoz  sensor   is in the  predefined method   Need  to  discuss with  soumen

			System.out.println(MacroConstant.DP_FLOW+"raw Accelerometer data in x-axis...."+rawAccelerometerData.getX());
			dpcsvData.setAccX(DPConverter.ConvertDoubleWithPreseision(rawAccelerometerData.getX(),6));
			System.out.println(MacroConstant.DP_FLOW+"raw Accelerometer data in Y-axis...."+rawAccelerometerData.getY());
			dpcsvData.setAccY(DPConverter.ConvertDoubleWithPreseision(rawAccelerometerData.getY(),6));
			System.out.println(MacroConstant.DP_FLOW+"raw Accelerometer data in z-axis...."+rawAccelerometerData.getZ());
			dpcsvData.setAccZ(DPConverter.ConvertDoubleWithPreseision(rawAccelerometerData.getZ(),6));
			dpcsvData.setSignalStrength(m_scaleSingnalStrength); 
			System.out.println(MacroConstant.DP_FLOW+"rawGyroscope data in x-axis...."+rawGyroscopeData.getX());
			dpcsvData.setGyroX(DPConverter.ConvertDoubleWithPreseision(rawGyroscopeData.getX(),6));
			System.out.println(MacroConstant.DP_FLOW+"raw Gyroscpe data in y-axis...."+rawGyroscopeData.getX());
			dpcsvData.setGyroY(DPConverter.ConvertDoubleWithPreseision(rawGyroscopeData.getY(),6));
			System.out.println(MacroConstant.DP_FLOW+"rawGyroscope data in x-axis...."+rawGyroscopeData.getZ());
			dpcsvData.setGyroZ(DPConverter.ConvertDoubleWithPreseision(rawGyroscopeData.getZ(),6));


			dpcsvData.setGPSAccuracy(currentLocation.getAccuracy());


			dpcsvData.setLocation(currentLocation);


			dpcsvData.setBreaking_value(breakingValue);
            Log.e("DPCSDataHelper",""+breakingValue);
            addToDPCSVDataCollection(dpcsvData);

			System.out.println(MacroConstant.DP_FLOW+"Data added to the csv Collection object....");

		} 
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("manas"+e.getMessage());
		}

		return isSuccessFullyAddedToCollection;

	}

	private String ConvertDateToString(int type)
	{
		if (type==0) 
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(MacroConstant.APPLICATION_DATE_FORMAT);
			String currentDate = dateFormat.format(new Date());
			return currentDate;
		}
		else 
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(MacroConstant.APPLICATION_TIME_FORMAT_12_HOURS);
			String currentTime = dateFormat.format(new Date());
			return currentTime;
		}
	}

	/**
	 * Add  the  data  to DPCSVDataCollection and call the method caluculateDisance(csvData) .
	 *
	 * @param csvData is the instance of DPCSVData class which contain all the data like lat,long speed etc
       {@ csvData}

	 * <p>
	 * Created by effone Technologies Private Limited on 03/03/15.
     </p>


	 */
	private void addToDPCSVDataCollection(DPCSVData csvData) {
		// TODO Auto-generated method stub

		if (!this.m_isDPCSVDataInitialized) 
		{
			//m_dpcsvDataList 

			this.m_dpcsvDataList = new CSVDataCollection();
			this.m_dpcsvDataList.add(csvData);
			this.m_isDPCSVDataInitialized = true;

		}
		else if (m_dpcsvDataList != null) 
		{
			this.m_dpcsvDataList.add(csvData);
			this.caluculateDisance(csvData);
		}
	}
	/**
	 * get the old location and current location and call to getDistanceInMiles(newLocation,m_oldLocation).
	 *
	 * @param csvData is the instance of DPCSVData class which contain all the data like lat,long speed etc
       {@ csvData}

	 * <p>
	 * Created by effone Technologies Private Limited on 03/03/15.
     </p>
	 */

	public void caluculateDisance(DPCSVData csvData) {

		if (!this.m_isInitilizedLocation) {

			this.m_oldLocation = csvData.getLocation();
			this.m_isInitilizedLocation = true;
			return;

		}

		//Location newLocation = csvData.getCurrentLocation();
		Location newLocation = csvData.getLocation();
		float distanceChange =this.getDistanceInMiles(newLocation,m_oldLocation);
		this.m_totalDistanceTravelled = this.m_totalDistanceTravelled+distanceChange;
		this.m_oldLocation = newLocation;

	}
	/**
	 * this method calculate the distance in miles by passing two location
	 *
	 * @param newLocation is the instance of Location class which represents the current location
       {@ newLocation}
       @param oldLocation is  the  instance of Location class which represents the previous location
       {@oldLocation}
	 * <p>
	 * Created by effone Technologies Private Limited on 03/03/15.
     </p>
	 */
	public float getDistanceInMiles( Location newLocation,Location oldLocation) {
		// TODO Auto-generated method stub
		float lat1,lon1,lat2,lon2;


		lat1 = (float) (newLocation.getLatitude()  * Math.PI / 180);
		lon1 = (float) (newLocation.getLongitude() * Math.PI / 180);

		lat2 = (float) (oldLocation.getLatitude()  * Math.PI / 180);
		lon2 = (float) (oldLocation.getLongitude() * Math.PI / 180);

		float R = 3963; // Radius of the earth in miles (  6,371 km ) // 3963 miles
		float dLat = lat2-lat1;
		float dLon = lon2-lon1;

		float a = (float) (Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(lat1) *  Math.cos(lat2) * Math.sin(dLon/2) * Math.sin(dLon/2));
		float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)));
		float d = R * c;


		return d;


	}

	/**
	 * Get the time difference between two consecutive time stamp of a location object in seconds.
	 *
	 * @param currentTimeStamp is the Date Instance which show the current time.
       {@ currentTimeStamp}

	 * <p>
	 * Created by effone Technologies Private Limited on 03/03/15.
     </p>
	 */
	public long getLocationTimeStampDifferenceInSeconds(Date currentTimeStamp) {
		// TODO Auto-generated method stub

		long totalSecond = 0;
		if (!this.m_isLocationTImeStampInitilized)
		{
			this.m_previousLocationTimeStamp = currentTimeStamp;
			this.m_isLocationTImeStampInitilized = true;
			return 0;
		}

		Calendar firstDate = Calendar.getInstance();
		Calendar secondDate = Calendar.getInstance();

		long diffInMillisec = firstDate.getTimeInMillis() - secondDate.getTimeInMillis(); 

		totalSecond = diffInMillisec;
		return totalSecond;
	}


	public int scaleSingnalStrength(Location location){

		int signalStrengthValue = 0;
		int signalStrong = 3;
		int signalAverage = 2;
		int signalWeek = 1;
		int signalLost = 0;


		if (location.getAccuracy()>=0) {

			if (location.getAccuracy()<=40) {

				signalStrengthValue =  signalStrong;
			}

			else if (location.getAccuracy() >40 && location.getAccuracy() <=60) {

				signalStrengthValue = signalAverage;
			}

			else if (location.getAccuracy() >=60 && location.getAccuracy() <=150) {

				signalStrengthValue = signalWeek;

			}

			else {
				signalStrengthValue = signalLost;
			}

		}
		return signalStrengthValue;

	}


	/**
	 * It saves and writes the data to CSV File
	 * <p>
	 * Created by effone Technologies Private Limited on 03/03/15.
     </p>
	 */
	public void saveDataInDPCSVDataHelper()
	{
		//need  to do later
		if (m_dpcsvDataList != null)
		{
			try {

				this.m_dpFilehandler = new DPFileHandler();

				List calculatedDistnaceList = new ArrayList();
				String strCalculatedDistnace  = this.getCalculatedDistance();
				calculatedDistnaceList.add(strCalculatedDistnace);


				m_arrResult.put(MacroConstant.KEY_CALCULATED_DISTANCE, (ArrayList<DPCSVData>) calculatedDistnaceList);
				m_arrResult.put(MacroConstant.KEY_CSV_COLLECTION, this.m_dpcsvDataList);


				this.m_dpFilehandler.saveToCoreData(m_arrResult );


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.resetMemberVariables();
		}
	}


	private String getCalculatedDistance()
	{
		return  Float.toString(this.m_totalDistanceTravelled);
		/*return [NSString stringWithFormat:@"%f", self.m_totalDistanceTravelled];

	    return this.m_totalDistanceTravelled*/
	}

	public ArrayList<String> formatDate(Date inputDate){

		ArrayList<String> formattedDateAndTime = new ArrayList<String>();

		//android.text.format.DateFormat formatter = new android.text.format.DateFormat();
		DateFormat formatter = new SimpleDateFormat();
		formatter.format(MacroConstant.APPLICATION_DATE_FORMAT);// @"dd/MM/yyyy"
		String stringFromDate = formatter.format(inputDate);

		formattedDateAndTime.add(stringFromDate);

		formatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
		formatter.format(MacroConstant.APPLICATION_TIME_FORMAT_12_HOURS);//@"hh:mm:ss a"
		String timeFromDate = formatter.format(inputDate);
		formattedDateAndTime.add(timeFromDate);
		formatter = DateFormat.getDateInstance(DateFormat.LONG,Locale.US);
		formatter.format(MacroConstant.APPLICATION_TIME_FORMAT_SECOND);//@"hh:mm:ss a"
		String secondFromDate = formatter.format(inputDate);
		formattedDateAndTime.add(secondFromDate);

		return formattedDateAndTime;
	}

	/**
	 * Reset all the Member variable 
	 * <p>
	 * Created by effone Technologies Private Limited on 03/03/15.
     </p>
	 */
	private void resetMemberVariables()
	{
		this.m_dpcsvDataList = null;
		this.m_isDPCSVDataInitialized = false;

		this.m_isInitilizedLocation = false;
		this.m_oldLocation = null;

		this.m_totalDistanceTravelled = 0;


	}





}

