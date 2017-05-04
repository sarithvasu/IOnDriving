package com.ion.iondriving.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;


import com.ion.iondriving.macro.MacroConstant;
import com.ion.iondriving.utilities.DPCSVData;
import com.ion.iondriving.utilities.DPConverter;
import com.ion.iondriving.utilities.DPUtility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;


@SuppressLint("NewApi")
public class DPFileHandler {
	// Header Index in csv file
	/*private static final int LATITUDE_INDEX=1;
	private static final int LONGITUDE_INDEX=2;
	private static final int SPEED_INDEX=3;
	private static final int ACCELERATION_INDEX=4;
	private static final int BREAKING_INDEX=5;
	private static final int CORNERING_INDEX=6;*/

	public static String ACCELERATION_KEY= "accelerationScore";
	public static String BREAKING_KEY = "breakingScore";
	public static String CORNERING_KEY= "corneringScore";
	public static String DRIVINGSCORE_KEY= "drivingScore";
	public static String TRIPDISTANCE_KEY= "tripDistance";
	public static String TRIPDURATION_KEY = "tripDuration";
	public static String TRPDISTANCE_KEY="tripDistance";
	public static String TRIPID_KEY= "tripId";
	public static String LASTRECORDEDTIMESTAMP_KEY= "lastRecordedTimeStamp";
	public static String FIRSTRECORDEDTIMESTAMP_KEY= "firstRecordedTimeStamp";
	public static String TRIPDATE_KEY = "tripDate";
	public static String SCORERATING_KEY = "ScoreRatings";

	String OriginalFileName=null;
	String updatedFileName=null;

	private static String ERROR_CALCULATED_DISTANCE = "Calculated distance is null and return";
	private static String ERROR_CSV_DATA_COLLECTION = "csv data collection is null and return";
	private static String ERROR_EMPTY_FILE_PATH = "Unable to get new trip folder path and return";
	//private DataBaseHelper m_dbHelper=null;

	String strTripId=null;


	//HashMap<String, String>  m_CSVDataCollection=new HashMap<String, String>();
	ArrayList<DPCSVData> m_CSVDataCollection=new ArrayList<DPCSVData>();
	String m_calculatedDistance;
	ArrayList<String> m_CSVDataCollec=new ArrayList<String>();
	String  m_strTextException; 


	String   filePath ;
	String _update_path;
	File m_file;
	private boolean isDirty;
	File m_documentPath = new File(Environment.getExternalStorageDirectory()
			+ "/"+ MacroConstant.APPLICATION_DATA_FOLDER+"/" + MacroConstant.TRIP_FOLDER);
	static Context m_context=null;
	//static DataBaseHelper m_DBHelper=new DataBaseHelper(m_context);
	File m_ExternalDataStorage = Environment.getExternalStorageDirectory();
	Date t1,t2;
	Dictionary<String, String> m_dictScoreRating;

	private Boolean isEmptyObject(Object  object, String strlogMessage)
	{
		boolean result = true;
		if (object != null)
		{
			result = false;
		}
		else
		{
			System.out.println(MacroConstant.DP_APPLICATION_ERROR + strlogMessage+ "...........");
		}

		return result;
	}



	public void saveToCoreData (Dictionary<String,ArrayList<DPCSVData>> resulte) throws IOException{


		this.isDirty = true;

		ArrayList<DPCSVData> temp = resulte.get(MacroConstant.KEY_CALCULATED_DISTANCE);
		String strcalculatedDistance = temp.toString().replaceAll("\\[", "").replaceAll("\\]","");

		ArrayList<DPCSVData> csvCollection = resulte.get(MacroConstant.KEY_CSV_COLLECTION);


		if (isEmptyObject(strcalculatedDistance, ERROR_CALCULATED_DISTANCE))
		{
			return;
		}

		if (isEmptyObject(csvCollection, ERROR_CSV_DATA_COLLECTION))
		{
			return;
		}


		this.setCalculatedDistance(strcalculatedDistance);
		System.out.println("TripDistance is..........."+this.getCalculatedDistance());


		this.m_CSVDataCollection = csvCollection;
		// soumen blocked
		this.saveCoreDataToCSVFile();
	}
	private void saveCoreDataToCSVFile() throws IOException{
		filePath= DPUtility.getSingletonInstance().getNewFileName(MacroConstant.TRIP_FOLDER);
		System.out.println("file path"+filePath);

		// If the filePath is empty, then write to a log file otherwise scall writeCoreDataToCSVFile method
		if (isEmptyObject(filePath, ERROR_EMPTY_FILE_PATH))
		{
			return;
		}



		this.writeCoreDataToCSVFile(filePath);

		// Now file is created. Therefore generate the unique trip file id and update the m_dictScoreRating.
	/*	this.updateScoreRatingDictionary(filePath);

		if(!this.isDirty)
		{
			// We are ready with all the values for Score Rating Entity. Insert data to Score Ratings Entity
			//[self insertNewRecordToScoreRating];
			DataBaseHelper dbHelper=new DataBaseHelper(DPUtility.getGlobalApplicationContext());
			dbHelper.InsertingDataToScoreratingTable (this.m_dictScoreRating );


			DPInterruptHandlerCollection.getSingletonInstance().InsertToInterruptTable();

			// we have already inserted in score rating  therefore make the object dirty
			this.isDirty = true;
			dbHelper = null;



		}*/
	}

	private void setCalculatedDistance(String calculatedDistance){
		this.m_calculatedDistance=calculatedDistance;
		System.out.println("distance.............."+m_calculatedDistance);

		// update the dictionary 
		//this.m_dictScoreRating.put(MacroConstant.SCORE_RATING_TRIP_DISTANCE_KEY, this.m_calculatedDistance);
	}

	private String getCalculatedDistance(){
		return m_calculatedDistance;
	}







	/*private void insertNewRecordToScoreRating() {
		ScoreRatings scoreRatings = new ScoreRatings();



		scoreRatings.setTripId(m_dictScoreRating.get(MacroConstant.SCORE_RATING_TRIP_ID_KEY));
		scoreRatings.setAccelerationScore(Integer.parseInt(m_dictScoreRating.get(MacroConstant.SCORE_RATING_AVERAGE_ACCELERATION_KEY)));
		scoreRatings.setBreakingScore(Integer.parseInt(m_dictScoreRating.get(MacroConstant.SCORE_RATING_AVERAGE_BREAKING_KEY)));
		scoreRatings.setCorneringScore(Integer.parseInt(m_dictScoreRating.get(MacroConstant.SCORE_RATING_AVERAGE_CORNERING_KEY)));
		scoreRatings.setDrivingScore(Integer.parseInt(m_dictScoreRating.get(MacroConstant.SCORE_RATING_AVERAGE_SCORE_KEY)));
		scoreRatings.setTripDate(m_dictScoreRating.get(MacroConstant.SCORE_RATING_TRIP_DATE_KEY));
		scoreRatings.setTripDuration(m_dictScoreRating.get(MacroConstant.SCORE_RATING_TRIP_DURATION_KEY));
		scoreRatings.setFirstRecordedTimeStamp(m_dictScoreRating.get(MacroConstant.SCORE_RATING_FIRST_RECORDED_TIMESTAMP_KEY));
		scoreRatings.setLastRecordedTimeStamp(m_dictScoreRating.get(MacroConstant.SCORE_RATING_LAST_RECORDED_TIMESTAMP_KEY));

		scoreRatings.setTripDistance(getCalculatedDistance());




		//Save to persistant storage
		//[[NSManagedObjectContext MR_defaultContext] MR_saveToPersistentStoreAndWait]; 

		//saveToPersistentStoreAndWait();
		String insertquerry="";
		String insertQuerry = "insert into " + SCORERATING_KEY + " (" + ACCELERATION_KEY + ", " + BREAKING_KEY
				+","+ CORNERING_KEY+","+DRIVINGSCORE_KEY+","+TRIPDISTANCE_KEY+","+TRIP_DURATION_KEY+","+TRIP_ID_KEY+","+LAST_RECORDED_TIMESTAMP_KEY+","+FIRSTRECORDEDTIMESTAMP_KEY+","+TRIP_DATE_KEY+") values('null', 'b','c');";
		ContentValues row = new ContentValues();
		row.put(ACCELERATION_KEY,scoreRatings.getAccelerationScore());
		row.put(BREAKING_KEY,scoreRatings.getBreakingScore());
		row.put(CORNERING_KEY,scoreRatings.getCorneringScore());
		row.put(DRIVINGSCORE_KEY,scoreRatings.getDrivingScore());
		row.put(TRIPDISTANCE_KEY,scoreRatings.getTripDistance());
		row.put(TRIP_DURATION_KEY,scoreRatings.getTripDuration());
		row.put(TRIP_ID_KEY,scoreRatings.getTripId());
		row.put(LAST_RECORDED_TIMESTAMP_KEY,scoreRatings.getLastRecordedTimeStamp());
		row.put(FIRSTRECORDEDTIMESTAMP_KEY,scoreRatings.getFirstRecordedTimeStamp());
		row.put(TRIP_DATE_KEY,scoreRatings.getTripDate());
		//long  InsertId = m_DBHelper.getInsertingDataToDataBase(insertquerry,null,row);


	}*/
	/*private void saveToPersistentStoreAndWait() {
		// TODO Auto-generated method stub
		ScoreRatings scoreRatings = new ScoreRatings();
		String insertquerry="";
		String insertQuerry = "insert into " + SCORERATING_KEY + " (" + ACCELERATION_KEY + ", " + BREAKING_KEY
				+","+ CORNERING_KEY+","+DRIVINGSCORE_KEY+","+TRIPDISTANCE_KEY+","+TRIP_DURATION_KEY+","+TRIP_ID_KEY+","+LAST_RECORDED_TIMESTAMP_KEY+","+FIRSTRECORDEDTIMESTAMP_KEY+","+TRIP_DATE_KEY+") values('null', 'b','c');";
		ContentValues row = new ContentValues();
		row.put(ACCELERATION_KEY,scoreRatings.getAccelerationScore());
		row.put(BREAKING_KEY,scoreRatings.getBreakingScore());
		row.put(CORNERING_KEY,scoreRatings.getCorneringScore());
		row.put(DRIVINGSCORE_KEY,scoreRatings.getDrivingScore());
		row.put(TRIPDISTANCE_KEY,scoreRatings.getTripDistance());
		row.put(TRIP_DURATION_KEY,scoreRatings.getTripDuration());
		row.put(TRIP_ID_KEY,scoreRatings.getTripId());
		row.put(LAST_RECORDED_TIMESTAMP_KEY,scoreRatings.getLastRecordedTimeStamp());
		row.put(FIRSTRECORDEDTIMESTAMP_KEY,scoreRatings.getFirstRecordedTimeStamp());
		row.put(TRIP_DATE_KEY,scoreRatings.getTripDate());
		long  InsertId = m_DBHelper.getInsertingDataToDataBase(insertquerry,null,row);

	}*/
	private String calculateTripDuration(long lastTimeStamp){
		return this.stringRepresentationForSeconds(lastTimeStamp);
	}
	private String stringRepresentationForSeconds(long lastTimeStamp)
	{
		long timeDuration = lastTimeStamp;
		long hour = 0;
		long minute = 0;
		long seconds = timeDuration;

		if (timeDuration >= 60) {
			minute = timeDuration / 60;
			seconds = timeDuration % 60;
		}
		if (minute >= 60) {
			hour = minute / 60;
			minute = hour % 60;
		}

		StringBuilder timeStr= new StringBuilder();
		if (hour > 0) {
			timeStr.append(hour+" h");
		}
		if (minute > 0) {

			timeStr.append(minute+" min ");
		}

		if (seconds > 0) {


			timeStr.append(seconds+" sec");
		}

		return timeStr.toString();

	}


	private void writeCoreDataToCSVFile(String filePath) throws IOException{
		{
			String strResultLine = this.createTripFile(filePath);


			if (strResultLine != null && strResultLine.length() > 0)
			{
				String fileName = filePath.substring(filePath.lastIndexOf("/") + 1); 
				System.out.println("Image name " + fileName);
				// Soumen Added
				File fileWithinMyDir = new File("/data/data/com.ion.iondriving/app_DRIVER PROFILE DATA/Trip", fileName);
				//File fileWithinMyDir = new File(filePath); 

				// Use the stream as usual to write into the file.
				FileOutputStream out = new FileOutputStream(fileWithinMyDir);


				if (fileWithinMyDir.exists()) {

					System.out.println("Yesss");
				}

				//	FileOutputStream out = new FileOutputStream(filePath);
				//m_file= new File(filePath);
				//m_file.createNewFile();

				FileWriter fileWriter = new FileWriter(fileWithinMyDir);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(strResultLine); 
				bufferedWriter.close();


			}

		}
	}
	/*private String createTripFile(String  filePath){
		int  collectionCount = this.m_CSVDataCollection.size();

		if (collectionCount==0) 
		{
			System.out.println("DATA IS"+collectionCount);
			return null;
		}


		String m_strHeader = "#Score, Lat, Lon, Speed, Acceleration, Brkng, Crng, Date, Time, Signal, Acc X, Acc Y, Acc Z, Gyro X, Gyro Y, Gyro Z, Accuracy";

		String[] HeaderArray = m_strHeader.split(",");
		String data=""+"lat" +" "+"long";
		System.out.println("data Not found"+HeaderArray[1]);
		StringBuilder resultString = new StringBuilder();
		resultString.append(m_strHeader);
		System.out.println("String is "+m_strHeader);
		double averageAcceleration = 0;
		double averageBreaking = 0;
		double averageCornering = 0;
		double averageScore = 0;
		//int lastTimeStamp = 0;
		String  tripDuration = null;
		// Read from Coredata "Reading" Entity
		for (DPCSVData csvData : m_CSVDataCollection) {
			double acceleration =csvData.getAcceleration() ;////9900000
			averageAcceleration +=  acceleration;

			double breaking =csvData.getBreaking();
			averageBreaking +=  breaking;

			double cornering = csvData.getCornerning();
			averageCornering +=  cornering;

			double score = csvData.getScore();
			averageScore +=  score;
			String dataString = "score"+csvData.getLatitude()+","+ csvData.getLongitude()+","+csvData.getSpeed()+","+acceleration+","+breaking+","+cornering+","+csvData.getDate()+", "+csvData.getTime()+","+csvData.getSignalStrength()+","+csvData.getAccX()+","+csvData.getAccY()+","+csvData.getAccZ()+"," + csvData.getGyroX()+","+csvData.getGyroY()+","+csvData.getGyroZ()+","+csvData.getAccuracy();
			resultString.append(dataString);
			dataString = null;

		}
		// Start :- Set the following Score Ratings attributes except tripId and tripDistance for ScoreRatings Entity
		averageAcceleration =  averageAcceleration/collectionCount;
		averageBreaking =  averageBreaking/collectionCount;
		averageCornering =  averageCornering/collectionCount;
		averageScore =  averageScore/collectionCount;
		//Hard coded  value  to be  passed
		String lastRecordedTimeStamp ="12:32:54:pm";
		String  firstRecordedTimeStamp ="";
		String tripDate = "date";


		//String  lastRecordedTimeStamp = m_CSVDataCollection.get( (collectionCount-1));
		SimpleDateFormat dateFormat = new SimpleDateFormat(MacroConstant.APPLICATION_TIME_FORMAT_12_HOURS);


		String Stringt1 = dateFormat.format(firstRecordedTimeStamp);
		String Stringt2 =dateFormat.format(lastRecordedTimeStamp) ;

		try {
			t1 = dateFormat.parse(Stringt1);  
			t2=dateFormat.parse(Stringt2);
		} catch (Exception e) {
			// TODO: handle exception
		}
		long startTime=t1.getTime();
		long endTime=t2.getTime();
		//Sushil need to tell
		long difference=endTime-startTime;
		int diff =timeIntervalSinceDate(difference);
		tripDuration = calculateTripDuration(diff);






		this.m_dictScoreRating = new Hashtable<String, String>();
		m_dictScoreRating.put(AVERAGE_ACCELERATION_KEY,String.valueOf(averageAcceleration));
		m_dictScoreRating.put(AVERAGE_BREAKING_KEY, String.valueOf(averageBreaking));
		m_dictScoreRating.put(AVERAGE_CORNERING_KEY, String.valueOf(averageCornering));
		m_dictScoreRating.put(AVERAGE_SCORE_KEY, String.valueOf(averageScore));
		m_dictScoreRating.put(TRIP_DURATION_KEY, tripDuration);
		m_dictScoreRating.put(LAST_RECORDED_TIMESTAMP_KEY, lastRecordedTimeStamp);
		m_dictScoreRating.put(FIRST_RECORDED_TIMESTAMP_KEY, firstRecordedTimeStamp);
		m_dictScoreRating.put(TRIP_DATE_KEY, tripDate);
		// we have populated the dictionary so make the object ready to use (object is not dirty)
		isDirty = false;
		return this.filePath;

	}*/


	private String createTripFile(String  filePath)
	{
		StringBuilder resultString = new StringBuilder();

		int  collectionCount = this.m_CSVDataCollection.size();
		if (collectionCount==0) 
		{
			System.out.println(MacroConstant.DP_APPLICATION_ERROR+"Unable To create csv Trip file due Empty Collection....");
			return null;
		}

/*
		//Build The Header  Details for Trip Files
		String strHeader = "Score, Lat, Lon, Speed, Acc, Brkng, Crng, Date, Time, Acc X, Acc Y, Acc Z, Signal Strength, Gyro X, Gyro Y, Gyro Z, GPS Accuracy";


		resultString.append(strHeader);
		resultString.append("\n");
*/

		double accelerationSum = 0;

		double breakingSum = 0;
		double corneringSum = 0;
		double scoreSum = 0;
		OriginalFileName=filePath.substring(filePath.lastIndexOf("/") + 1); 
		updatedFileName=filePath.substring(filePath.lastIndexOf("/") + 1);

		Iterator<DPCSVData> itr = m_CSVDataCollection.iterator();

		while(itr.hasNext()) 
		{
			//Object element = itr.next();
			//DPCSVData csvData = (DPCSVData)element;

			DPCSVData csvData = itr.next();

			double acceleration = csvData.getAcceleration() ;
			accelerationSum += acceleration;

			double breaking = csvData.getBreaking();
			breakingSum += breaking;

			double cornering = csvData.getCornerning();

			corneringSum += cornering;

			double score = csvData.getScore();
			scoreSum +=  score;


			String strdate = csvData.getDate();
			String strtime = csvData.getTime();


			resultString.append(score);
			resultString.append(",");
			resultString.append(csvData.getLatitude());
			resultString.append(",");
			resultString.append(csvData.getLongitude());
			resultString.append(",");
			resultString.append(csvData.getSpeed());
			resultString.append(",");


			resultString.append(acceleration);
			resultString.append(",");
			resultString.append(breaking);
			resultString.append(",");
			resultString.append(cornering);
			resultString.append(",");


			resultString.append(strdate);
			resultString.append(",");
			resultString.append(strtime);
			resultString.append(",");

			resultString.append(csvData.getAccX());
			resultString.append(",");
			resultString.append(csvData.getAccY());
			resultString.append(",");
			resultString.append(csvData.getAccZ());
			resultString.append(",");

			resultString.append(csvData.getSignalStrength());
			resultString.append(",");


			resultString.append(csvData.getGyroX());
			resultString.append(",");
			resultString.append(csvData.getGyroY());
			resultString.append(",");
			resultString.append(csvData.getGyroZ());
			resultString.append(",");


			resultString.append(csvData.getGPSAccuracy());
			resultString.append(",");
			resultString.append(""+csvData.getBreaking_value());

			resultString.append("\n");
		}

		// Start :- Set the following Score Ratings attributes except tripId and tripDistance for ScoreRatings table in database
		double averageAcceleration =  accelerationSum/collectionCount;
		double averageBreaking =  breakingSum /collectionCount;
		double averageCornering =  corneringSum/collectionCount;
		double averageScore =  scoreSum/collectionCount;

		String tripDate = this.m_CSVDataCollection.get(collectionCount -1).getDate();

		String tripDuration = null;
		String firstRecordedTimeStamp = this.m_CSVDataCollection.get(0).getTime();
		String lastRecordedTimeStamp = this.m_CSVDataCollection.get(collectionCount -1).getTime();
		long differenceInsec= DPConverter.DeltaInSeconds(firstRecordedTimeStamp, lastRecordedTimeStamp);

		tripDuration = this.calculateTripDuration(differenceInsec);


		this.m_dictScoreRating = new Hashtable<String, String>();
		m_dictScoreRating.put(MacroConstant.SCORE_RATING_AVERAGE_ACCELERATION_KEY, String.valueOf(averageAcceleration));
		m_dictScoreRating.put(MacroConstant.SCORE_RATING_AVERAGE_BREAKING_KEY, String.valueOf(averageBreaking));
		m_dictScoreRating.put(MacroConstant.SCORE_RATING_AVERAGE_CORNERING_KEY, String.valueOf(averageCornering));
		m_dictScoreRating.put(MacroConstant.SCORE_RATING_AVERAGE_SCORE_KEY, String.valueOf(averageScore));
		m_dictScoreRating.put(MacroConstant.SCORE_RATING_TRIP_DATE_KEY, tripDate);
		m_dictScoreRating.put(MacroConstant.SCORE_RATING_TRIP_DURATION_KEY, tripDuration);
		m_dictScoreRating.put(MacroConstant.SCORE_RATING_TRIP_DISTANCE_KEY, this.m_calculatedDistance);
		m_dictScoreRating.put(MacroConstant.SCORE_RATING_LAST_RECORDED_TIMESTAMP_KEY, lastRecordedTimeStamp);
		m_dictScoreRating.put(MacroConstant.SCORE_RATING_FIRST_RECORDED_TIMESTAMP_KEY, firstRecordedTimeStamp);







		String fast = m_dictScoreRating.get(MacroConstant.SCORE_RATING_FIRST_RECORDED_TIMESTAMP_KEY);
		String last = m_dictScoreRating.get(MacroConstant.SCORE_RATING_LAST_RECORDED_TIMESTAMP_KEY);

		System.out.println("soumen File handeler...............fast"+fast);
		System.out.println("soumen File handeler...............last"+last);

		// we have populated the dictionary so make the object ready to use (object is not dirty)
		isDirty = false;
		// End :- Set the following Score Ratings attributes except tripId and tripDistance for ScoreRatings table in database
		return resultString.toString();
	}
	public static long timeIntervalSinceDate(long seconds) {
		// TODO Auto-generated method stub
		long s = (seconds/1000) % 60;
		long m = (seconds / 1000)/60;
		long h = (seconds / 1000)/60 % 24;
		return h+m+s;
	}

	/*private String getNewFileName() throws IOException { Soumen we have shifted this code to Utility

		String logfileName=null;
		String fileNameEnding=null;
		int fileNameCount = 1;
		// TODO Auto-generated method stub
		File mydir = new File(Environment.getExternalStorageDirectory()
				+ "/"+MacroConstant.APPLICATION_DATA_FOLDER+"/"+MacroConstant.TRIP_FOLDER);
		if (!mydir.exists()) {
			mydir.mkdirs();
		} else {
			String path = "/"+"sdcard"+"/"+MacroConstant.APPLICATION_DATA_FOLDER+"/"+MacroConstant.TRIP_FOLDER+"/";
			String tripFilePath = MacroConstant.APPLICATION_DATA_FOLDER+"/"+MacroConstant.TRIP_FOLDER+"/";
			File sdcard = Environment.getExternalStorageDirectory();
			m_file= new File(sdcard, tripFilePath);
			File[] listfiles =m_file.listFiles();
			ArrayList<String> Alfilenames=new ArrayList<String>();


			for(int i=0;i<listfiles.length;i++)
			{
			Alfilenames.add(listfiles[i].getName());
			}
			logfileName = "Trip-";
			fileNameEnding = "0001";
			for (String filname : Alfilenames) {

				if (filname.equals(logfileName + fileNameEnding + ".csv")) {
					System.out.println("yes there is a log file found" + logfileName);
					fileNameCount++;
				}
				if (fileNameCount < 10)
					fileNameEnding = "000" + fileNameCount;
				else if (fileNameCount < 100)
					fileNameEnding = "00" + fileNameCount;
				else if (fileNameCount < 1000)
					fileNameEnding = "0" + fileNameCount;
				else
					fileNameEnding = Integer.toString(fileNameCount);
			}
			logfileName += fileNameEnding + ".csv";//trip001
			_update_path = path + logfileName;//doc/trip001
			m_file = new File(_update_path);
			m_file.createNewFile();
		}

		return _update_path;
	}
	 */


}
