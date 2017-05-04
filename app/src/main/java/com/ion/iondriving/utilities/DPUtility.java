package com.ion.iondriving.utilities;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.Environment;


import com.ion.iondriving.macro.MacroConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TimeZone;

public class DPUtility {
	// Header Index in csv file

	//Manas Blocked
	/*	private static final int SCORERATING_ACCELERATION_SCORE = 0;
	private static final int SCORERATING_BREAKING_SCORE = 1;
	private static final int SCORERATING_CORNERING_SCORE = 2;
	private static final int SCORERATING_DRIVING_SCORE = 3;
	private static final int SCORERATING_TRIP_DISTANCE= 4;
	private static final int SCORERATING_TRIP_DURATION= 5;
	private static final int SCORERATING_TRIP_ID=6;
	private static final int SCORERATING_LAST_RECORDED_TIMESTAMP =7;
	private static final int SCORERATING_FIRST_RECORDED_TIME_STAMP=8;
	private static final int SCORERATING_TRIP_DATE=9;*/





	/*data*/

	ArrayList<String> m_LatitudePoints ;
	ArrayList<String> m_LongitudePoints ;
	ArrayList<String> m_SpeedCollection ;
	int  m_AccelerationRating;
	int m_BreakingRating ;
	int m_CorneringRating ;
	Dictionary<String, ArrayList<String>> m_dictList;
	Location  previousLocation;
	static Cursor m_cursor=null;
	private static Context m_context=null;
	private static  DPUtility instance_1=null;

	// ## Single Instance Created
	public static  DPUtility getSingletonInstance() 
	{
		if (instance_1 == null) 
		{
			instance_1 = new DPUtility();
		}
		return instance_1;
	}


	public static  Context getGlobalApplicationContext() 
	{
		Context thiscontext = null;

		if (m_context != null) 
		{
			thiscontext = m_context; 
		}

		return thiscontext;
	}


	public static  void SetGlobalApplicationContext(Context context)
	{
		if (m_context == null) 
		{
			m_context = context;
		}
	}



	public static String getTripFolderPath()
	{
		String documentPath=Environment.getExternalStorageState()+"/"+ MacroConstant.APPLICATION_DATA_FOLDER+"/" +MacroConstant.TRIP_FOLDER;
		return documentPath;

	}
	//This will set the fully qualified path for interrupt folder
	public static String getInterruptFolderPath()
	{
		String documentPath=Environment.getExternalStorageState()+"/"+MacroConstant.APPLICATION_DATA_FOLDER+"/" +MacroConstant.INTERRUPT_FOLDER;
		return documentPath;
	}





	public static ArrayList<String> arrayRepresentationOfTripfileForMap(String path)
	{
		ArrayList<String> arrDuplicateLocation = null ; 

		try {
			ArrayList<String> arrRowData = arryRepresentationOfCSVFile(path);
			if (arrRowData != null)
			{
				arrDuplicateLocation = new ArrayList<String>();

				//Removing the Header Row
				arrRowData.remove(0);

				for (String string : arrRowData) 
				{
					String[] subArray=string.split(","); // string representation of a single row
					String latitude  = subArray[MacroConstant.LATITUDE_INDEX];
					String longitude = subArray[MacroConstant.LONGITUDE_INDEX];
					String speed     = subArray[MacroConstant.SPEED_INDEX];
					String resultStr=latitude+","+longitude+","+speed;
					arrDuplicateLocation.add(resultStr);

				}
			}
			else
			{
				System.out.println(MacroConstant.DP_APPLICATION_ERROR+"in arrayRepresentationOfTripfile method...");
			}



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Return
		return arrDuplicateLocation;
	}



	private static  ArrayList<String> removeDuplicateEntriesFromArray(ArrayList<String> duplicateArray)
	{
		//ArrayList<String> nonDuplicate = new ArrayList<String>(duplicateArray.size());

		ArrayList<String> nonDuplicate = new ArrayList<String>();
		Iterator<String> iterator = duplicateArray.iterator();

		while (iterator.hasNext())
		{
			String data = iterator.next();
			if(!duplicateArray.contains(data)) nonDuplicate.add(data);
		}

		// Return
		return duplicateArray;
	}

	public ArrayList<String> readTripFile(String path) 
	{
		BufferedReader br = null;
		String line = "";
		ArrayList<String> totalListArray=new ArrayList<String>();
		try {

			br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				// use \n as separator
				String[] arrsingleRowData = line.split("\n");
				totalListArray.add(arrsingleRowData[0]);
				//System.out.println("total"+totalListArray.size());


			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return totalListArray;

	}



	public static String  buildInterruptFilePath(String tripFilePath)
	{
		// Initialize Interrupt File Path
		String interruptFilePath = null;
		// Get the last path component (trip file name)
		String tripFileName =  tripFilePath.substring(0,tripFilePath.lastIndexOf(" "));
		String  documentPath = Environment.getExternalStorageState()+MacroConstant.APPLICATION_DATA_FOLDER+MacroConstant.TRIP_FOLDER;
		String tripname=tripFileName.substring(0, tripFileName.length()-4).replace("Trip", "Interrupt");
		return interruptFilePath;

	}











	// Ex. input (28/09/2014) => output 2014-09-28
	public static String convertDateFormate(String dateStr)
	{
		SimpleDateFormat formater=new SimpleDateFormat();
		//Need  To know  about the variable Name
		formater.format(MacroConstant.APPLICATION_DATE_FORMAT_NEW);

		String oldDate = formater.format(dateStr);
		formater.format(MacroConstant.APPLICATION_DATE_FORMAT);

		String newDateStr = formater.format(oldDate);
		return newDateStr;
	}
	public static String createUniqueId(String  filePath )
	{

		//File extStore = Environment.getExternalStorageDirectory();
		//File myFile = new File(extStore.getAbsolutePath()+ "/"+MacroConstant.APPLICATION_DATA_FOLDER+"/" + MacroConstant.TRIP_FOLDER +"/" + "Trip-0003.csv");



		File myFile = new File(filePath);
		String tripFileCreationDateInString = null;

		boolean abc = myFile.exists();
		long time = myFile.lastModified();
		Date date = new Date(time);
		SimpleDateFormat dateFormatter = new SimpleDateFormat(MacroConstant.UNIQUE_KEY_FORMATTER);
		tripFileCreationDateInString = dateFormatter.format(date);






		/*Path path = Paths.get(filePath);
			BasicFileAttributeView view = Files.getFileAttributeView(path,
					BasicFileAttributeView.class);
			try {
				BasicFileAttributes attributes = view.readAttributes();
				System.out.println(attributes.lastAccessTime());
				FileTime createTime = attributes.creationTime();
				SimpleDateFormat dateFormatter = new SimpleDateFormat();
				dateFormatter.format(MacroConstant.UNIQUE_KEY_FORMATTER);

				tripFileCreationDateInString = dateFormatter.format(createTime);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 */

		return tripFileCreationDateInString;
	}


	//get the data from Megical record

	/*public static DPTripBase getScroreDataById(String tripId)
	{
		DPTripBase thisTrip=null;
		String[] allRecords =new String[10];

		String predicate = "SELECT * from  ScoreRatings  WHERE tripId=" + tripId;
		m_cursor= m_DBHelper.getScoreRatingByTripId(predicate, null );

		while(m_cursor.moveToNext())
		{
			//your code here
			thisTrip=new DPTripBase();
			thisTrip.setAccelerationRating(m_cursor.getInt(SCORERATING_ACCELERATION_SCORE));
			thisTrip.setBreakingRating(m_cursor.getInt(SCORERATING_CORNERING_SCORE));
			thisTrip.setCorneringRating(m_cursor.getInt(SCORERATING_DRIVING_SCORE));
			thisTrip.setTripDistance(m_cursor.getInt(SCORERATING_TRIP_DISTANCE));
			thisTrip.setTripDuration(m_cursor.getString(SCORERATING_TRIP_DURATION));

			thisTrip.setLastRecordedTimeStamp(m_cursor.getString(SCORERATING_LAST_RECORDED_TIMESTAMP));
			thisTrip.setFirstRecordedTimeStamp(m_cursor.getString(SCORERATING_FIRST_RECORDED_TIME_STAMP));
			thisTrip.setTripDate(m_cursor.getString(SCORERATING_TRIP_DATE));


		}
		m_cursor.close();

		//return
		return thisTrip;
	}*/


	// Check wheather to mark (*) with trip file name if associated interrupt file is available
	public static boolean isTripFileToBeMarked(String tripFileName)
	{
		boolean result = false;
		String strTripLastPathComponent =  tripFileName.substring(0,tripFileName.lastIndexOf(" "));

		ArrayList<String>  arrMatchedTripFile = fileToBeMarkedForInterrupt();

		if (arrMatchedTripFile.size() != 0)
		{
			for (String strTripFileName :arrMatchedTripFile)
			{
				if (strTripLastPathComponent.compareTo(strTripFileName) == 1)
				{
					result = true;
				}
			}
		}
		return result;
	}

	// Build the trip file list to be marked in association with interrupt file
	public static ArrayList<String> fileToBeMarkedForInterrupt()
	{
		/*ArrayList<String> arrTripList = [[NSFileManager defaultManager] subpathsAtPath:[self getTripFolderPath]];

	  ArrayList<String> arrInterruptList = [[NSFileManager defaultManager] subpathsAtPath:[self getInterruptFolderPath]];*/
		//Need to do like file
		ArrayList<String> arrInterruptList=new ArrayList<String>();
		ArrayList<String> arrTripList=new ArrayList<String>();
		// List of Trip File that has associated interrupt file
		ArrayList<String> arrMatchedTripFile = new ArrayList<String>();

		// Check array is not empty
		if (arrInterruptList.size() != 0)
		{
			// substringFromIndex:5 returns 0001.csv from Trip-0001.csv
			ArrayList<String> arrTruncatedTrip =getTruncatedFileName(arrTripList, 5);
			// substringFromIndex:10 returns 0001.csv from Interrupt-0001.csv
			ArrayList<String> arrTruncatedInterrupt =  getTruncatedFileName(arrInterruptList, 10);

			for (int i = 0; i <arrTruncatedInterrupt.size(); i++)
			{
				String strInterruptFileName = arrTruncatedInterrupt.get(i);
				for (String strTripFileName : arrTruncatedTrip)
				{
					if (strInterruptFileName.compareTo(strTripFileName) == 1)
					{
						// Rebuild the Trip File ( Concatenate two string ) "Trip-"  +  "0001.csv"
						String strConcatenatedTripFileName = "Trip-";
						strConcatenatedTripFileName = strConcatenatedTripFileName .concat(strTripFileName);
						arrMatchedTripFile.add(strConcatenatedTripFileName);
					}
				}
			}
		}
		// Return Matching Trip File List that has associated interrupt file
		return arrMatchedTripFile;
	}





	// Build truncated trip & interrupt file name by passing trip or interrupt file collection and substring length to take out the number portion along with .csv extension from trip or interrupt file name
	public static ArrayList<String> getTruncatedFileName(ArrayList<String> fileList ,int subStringLength)
	{
		ArrayList<String> arrTruncatedfileList = new ArrayList<String>();


		for (String fileName : fileList)
		{
			// substringFromIndex:10 returns 0001.csv from Trip-0001.csv
			String  fileNumber =fileName.substring(subStringLength);
			arrTruncatedfileList.add(fileNumber);
		}
		// Return Truncated Name of Files
		return arrTruncatedfileList;
	}

	// Convert to 12 to 24 hour format
	public static String convertTime_12to24_Hours_Format(String date)
	{
		// Initialize the date formatter
		SimpleDateFormat  dateFormatter =new SimpleDateFormat();

		// Set the system zone
		dateFormatter.setTimeZone(TimeZone.getDefault());

		// Forcefully set to application's default 12 hours format
		dateFormatter.format(MacroConstant.APPLICATION_TIME_FORMAT_12_HOURS);
		// Input time to be converted from 12 hours to 24 hours format
		String dateToBeConverted = dateFormatter.format(date);

		// Destination Format ( 24 hours format )
		dateFormatter.format(MacroConstant.APPLICATION_TIME_FORMAT_24_HOURS);
		// Perform 12 to 24 hours time conversion
		return dateFormatter.format(dateToBeConverted);
	}


	public ArrayList<String> getTripDateAndStartTimeStamp(ArrayList<String> correctedCSVComponents)
	{
		ArrayList<String> arrResult = new ArrayList<String>();

		// After removing the empty row, getting the corrected CSVComponents
		ArrayList<String> correctedCollection = correctedCSVComponents;

		// remove the column header
		correctedCollection.remove(0);
		ArrayList<String> correctedCollectionAfterRemove=new ArrayList<String>();
		correctedCollectionAfterRemove.add(correctedCollection.get(1));

		// Create a object for one row
		String rowData[] = new String[correctedCollectionAfterRemove.size()];
		rowData = correctedCollectionAfterRemove.toArray(rowData);
		// Get the trip date and add it to the result array index 0
		arrResult.add(rowData[7]);
		// Get the first time stamp and add it to the result array index 1
		arrResult.add(rowData[8]);


		return arrResult;
	}
	public static StringBuilder calculateTripDuration(int[] rowData)
	{
		int lastTimeStamp = rowData[7];
		return  stringRepresentationForSeconds(lastTimeStamp);
	}


	public static StringBuilder stringRepresentationForSeconds(int lastTimeStamp)
	{

		int timeDuration = lastTimeStamp;

		int hour = 0;

		int minute = 0;

		int seconds = timeDuration;

		if (timeDuration >= 60) {

			minute = timeDuration / 60;

			seconds = timeDuration % 60;
		}

		if (minute >= 60) {

			hour = minute / 60;

			minute = hour % 60;
		}

		StringBuilder timeStr = new StringBuilder();

		if (hour > 0) {

			timeStr.append("h"+hour);

		}

		if (minute > 0) {
			timeStr.append("m"+minute);

		}

		if (seconds > 0) {
			timeStr.append("s"+seconds);

		}
		return timeStr;
	}

	public static double getDistanceInMiles(Location newLocation, Location fromLocation, Location oldLocation)
	{

		double lat1,lon1,lat2,lon2;

		lat1 = newLocation.getLatitude() *Math.PI / 180;
		lon1 = newLocation.getLongitude()*Math.PI / 180;

		lat2 = oldLocation.getLatitude() * Math.PI / 180;
		lon2 = oldLocation.getLongitude() * Math.PI / 180;

		float R = 3963; // Radius of the earth in miles (  6,371 km ) // 3963 miles
		double dLat = lat2-lat1;
		double dLon = lon2-lon1;

		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +Math.cos(lat1) *Math.cos(lat2) * Math.sin(dLon/2) *Math.sin(dLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = R * c;

		return d;
	}



	// Retruns Collections in Descending Order

	public static File[] sortCollection(File[]  unsortedfiles)
	{
		Arrays.sort(unsortedfiles, new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				/*return new Long(((File)o1).lastModified()).compareTo
								(new Long(((File) o2).lastModified()));*/

				return -1*  Long.valueOf(((File)o1).lastModified()).compareTo
						(new Long(((File) o2).lastModified()));

			}
		})	;	

		return unsortedfiles;
	}

	// Populate A, B, C, Lat, Long, Speed Collection and add individual collection to a dictionary for future use
	public void setList(ArrayList<String> correctedCollection)
	{
		m_dictList = new Hashtable<String, ArrayList<String>>();

		ArrayList<String> arrLatitude =new ArrayList<String>();
		ArrayList<String> arrLongitude =new ArrayList<String>();
		ArrayList<String> arrSpeed =new ArrayList<String>();
		ArrayList<String> arrAccln =new ArrayList<String>();
		ArrayList<String> arrBrkng =new ArrayList<String>();
		ArrayList<String> arrCrng =new ArrayList<String>();


		// Populate A, B, C, Lat, Long, Speed Collection ( Note : Index is 1 because we don't want to take header )

		for (String string : correctedCollection)
		{
			String[] subArray=string.split(",");

			String latitude  = subArray[MacroConstant.LATITUDE_INDEX];
			String longitude = subArray[MacroConstant.LONGITUDE_INDEX];
			String speed     = subArray[MacroConstant.SPEED_INDEX];
			String acceleration=subArray[MacroConstant.ACCELERATION_INDEX];
			String breaking  = subArray[MacroConstant.BREAKING_INDEX];
			String cornering  = subArray[MacroConstant.CORNERING_INDEX];
			arrLatitude.add(latitude);
			arrLongitude.add(longitude);
			arrLatitude.add(speed);
			arrLatitude.add(acceleration);
			arrLatitude.add(breaking);
			arrLatitude.add(cornering);
		}
		m_dictList.put(MacroConstant.LATITUDE_KEY, arrLatitude);
		m_dictList.put(MacroConstant.LONGITUDE_KEY, arrLongitude);
		m_dictList.put(MacroConstant.SPEED_KEY, arrSpeed);
		m_dictList.put(MacroConstant.ACCELERATION_KEY, arrAccln);
		m_dictList.put(MacroConstant.BREAKING_KEY, arrBrkng);
		m_dictList.put(MacroConstant.CORNERING_KEY, arrCrng);

	}


	// Start :- Helper Methods to calculate Acceleration, Breaking, Cornering and Score Rating for Score View
	// Calculate Latitude Points (Collection of Latitude)
	public ArrayList<String> getLatitudePoints(String strKey)
	{
		// Get the Latitude collection from Master Object dictList
		m_LatitudePoints = m_dictList.get(strKey);

		// Return
		return m_LatitudePoints;

	}
	// Calculate Longitude Points (Collection of Longitude)
	public ArrayList<String> getLongitudePoints(String strKey)
	{
		// Get the Longitude collection from Master Object dictList
		m_LongitudePoints =m_dictList.get(strKey);

		// Return
		return m_LongitudePoints;
	}
	// Calculate Speed (Collection of Speed)
	public ArrayList<String> getSpeedCollection(String strKey)
	{
		// Get the Speed collection from Master Object dictList
		m_SpeedCollection = m_dictList.get(strKey);

		// Return
		return m_SpeedCollection;
	}

	// Calculate Acceleration Rating
	public int  calculateAccelerationRating(String strKey)
	{
		// Get the average acceleration
		m_AccelerationRating =(int)getAverage(strKey);

		// Return
		return m_AccelerationRating;
	}

	// Calculate Breaking Rating
	public int calculateBreakingRating(String strKey)
	{
		// Get the average Breaking
		m_BreakingRating = (int)getAverage(strKey);

		// Return
		return m_BreakingRating;
	}
	// Calculate Cornering Rating
	public int calculateCorneringRating(String strKey)
	{
		// Get the average Cornering
		m_CorneringRating = (int)getAverage(strKey);

		// Return
		return m_CorneringRating;
	}

	// Calculate Score
	public int calculateScore()
	{
		int score = 0;
		score = (m_AccelerationRating +m_BreakingRating + m_CorneringRating)/3;
		return score;
	}


	// Calculate Distraction Rating
	public static int calculateDistractionRating(String strTripFilepath) throws IOException
	{
		int distractionRating = 0;
		String  strInterruptFilePath =  buildInterruptFilePath(strTripFilepath);
		File f1=new File(strInterruptFilePath);

		boolean exists = f1.exists();

		if (exists)
		{
			// soumen need to change this  Blocked this line
			ArrayList<String > csvComponents =arryRepresentationOfCSVFile(strInterruptFilePath);

			// Remove the last row (this row is empty)
			//[csvComponents removeLastObject];

			// After removing the empty row, getting the corrected CSVComponents
			ArrayList<String> correctedCSVComponents = csvComponents;

			int count = correctedCSVComponents.size();

			// Remove the header row
			count = count - 1;

			if (count == 0)
			{
				distractionRating = 100;
			}
			else if (count == 1)
			{
				distractionRating = 87;
			}
			else if (count == 2)
			{
				distractionRating = 72;
			}
			else if (count == 3)
			{
				distractionRating = 55;
			}
			else if (count == 4)
			{
				distractionRating = 35;
			}
			else if (count == 5)
			{
				distractionRating = 10;
			}
			else if (count > 5)
			{
				distractionRating = 5;
			}
		}
		// Return
		return distractionRating;
	}
	/*public static ArrayList<String> arryRepresentationOfCSVFile(String path) throws IOException {
		//String csvFile = "C://Excel//Driver-.csv";
		BufferedReader br = null;
		String line = "";
		ArrayList<String> totalListArray=new ArrayList<String>();
		try {

			br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				// use \n as separator
				String[] arrsingleRowData = line.split("\n");
				totalListArray.add(arrsingleRowData[0]);
				//System.out.println("total"+totalListArray.size());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return totalListArray;
	}*/

	public static ArrayList<String> arryRepresentationOfCSVFile(String strFilePath) throws IOException {

		ArrayList<String> totalListArray=new ArrayList<String>();
		BufferedReader br = null;
		String line = "";

		try {

			br = new BufferedReader(new FileReader(strFilePath));
			while ((line = br.readLine()) != null) {
				String[] arrsingleRowData = line.split("\n"); // use \n as separator
				totalListArray.add(arrsingleRowData[0]);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// Return
		return totalListArray;
	}

	public double getAverage(String strKey)
	{
		double value = 0;
		double average = 0;
		ArrayList<String> arrAcceleration =m_dictList.get(strKey);


		for(int idx = 0; idx < arrAcceleration.size(); idx++)
		{
			String data= value +arrAcceleration.get(idx);
			value= Double.parseDouble(data);
		}

		// calculate average
		average = value/(arrAcceleration.size());


		// Return
		return average;
	}
	public String getNewFileName(String folderName) throws IOException {
		String _update_path=null;
		String logfileName=null;
		String fileNameEnding=null;
		int fileNameCount = 1;
		File file = null;

		if(folderName.equals(MacroConstant.TRIP_FOLDER))
		{
			logfileName = MacroConstant.TRIP_FOLDER+"-";
		}
		else if(folderName.equals(MacroConstant.INTERRUPT_FOLDER)) {
			logfileName = MacroConstant.INTERRUPT_FOLDER + "-";
		}

		File mydirMain =DPUtility.getGlobalApplicationContext().getDir( MacroConstant.APPLICATION_DATA_FOLDER, Context.MODE_WORLD_READABLE);
		System.out.println("DIRECTORY........"+mydirMain);
		File mydir = new File(mydirMain, folderName);
		/*mySubDir.mkdir();
		File mySubDir2 = new File(mydir, "INTERRUPT");
		mySubDir2.mkdir();*/

		System.out.println("DIRECTORY......."+mydir);

		// TODO Auto-generated method stub
		/*File mydir = new File(Environment.getExternalStorageDirectory()
				+ "/"+MacroConstant.APPLICATION_DATA_FOLDER+"/"+folderName);*/
		if (!mydir.exists()) {
			mydir.mkdirs();
			String path=Environment.getDataDirectory().getPath()+"/data/"+DPUtility.getGlobalApplicationContext().getPackageName()+"/app_DRIVER PROFILE DATA"+"/"+folderName+"/";

			//	String path = "/"+Environment.getDataDirectory()+"/"+MacroConstant.APPLICATION_DATA_FOLDER+"/"+folderName+"/";
			String tripFilePath = MacroConstant.APPLICATION_DATA_FOLDER+"/"+folderName+"/";
			//	File sdcard = Environment.getExternalStorageDirectory();
			//file= new File(mydir, tripFilePath);
			File[] listfiles =mydir.listFiles();
			ArrayList<String> Alfilenames=new ArrayList<String>();
			System.out.println("FileLength"+listfiles.length);
			for(int i=0;i<listfiles.length;i++)
			{
				Alfilenames.add(listfiles[i].getName());
			}
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
			//file = new File(_update_path);
			//file.createNewFile();

		} else {



			String path=Environment.getDataDirectory().getPath()+"/data/"+DPUtility.getGlobalApplicationContext().getPackageName()+"/app_DRIVER PROFILE DATA"+"/"+folderName+"/";

			//	String path = "/"+Environment.getDataDirectory()+"/"+MacroConstant.APPLICATION_DATA_FOLDER+"/"+folderName+"/";
			String tripFilePath = MacroConstant.APPLICATION_DATA_FOLDER+"/"+folderName+"/";
			//	File sdcard = Environment.getExternalStorageDirectory();
			//file= new File(mydir, tripFilePath);
			File[] listfiles =mydir.listFiles();
			ArrayList<String> Alfilenames=new ArrayList<String>();
			System.out.println("FileLength"+listfiles.length);
			for(int i=0;i<listfiles.length;i++)
			{
				Alfilenames.add(listfiles[i].getName());
			}
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
			//file = new File(_update_path);
			//file.createNewFile();
		}
		return _update_path;
		//return file;
	}


	public static File getDirectoryPath(String folderName)
	{
		/*File sdcard = Environment.getExternalStorageDirectory();
		String strPath = MacroConstant.APPLICATION_DATA_FOLDER+"/"+folderName;
		File directoryPath= new File(sdcard,strPath);*/
		File mydirMain =DPUtility.getGlobalApplicationContext().getDir( MacroConstant.APPLICATION_DATA_FOLDER, Context.MODE_PRIVATE);
		System.out.println("DIRECTORY........"+mydirMain);
		File mydir = new File(mydirMain, folderName); 
		return mydir;
	}


	public String getNewInterruptFileName(String folderName,
			String interruptfileName) {
		// TODO Auto-generated method stub
		String _update_path=null;
		String logfileName=null;
		String fileNameEnding=null;
		int fileNameCount = 1;
		File file = null;
		if(folderName.equals(MacroConstant.TRIP_FOLDER))
		{
			logfileName = MacroConstant.TRIP_FOLDER+"-";
		}
		else if(folderName.equals(MacroConstant.INTERRUPT_FOLDER))
		{
			logfileName = MacroConstant.INTERRUPT_FOLDER+"-";
		}
		File mydirMain =DPUtility.getGlobalApplicationContext().getDir( MacroConstant.APPLICATION_DATA_FOLDER, Context.MODE_PRIVATE);
		System.out.println("DIRECTORY........"+mydirMain);
		File mydir = new File(mydirMain, folderName);
		if (!mydir.exists()){
			mydir.mkdir();
			String path=Environment.getDataDirectory().getPath()+"/data/"+DPUtility.getGlobalApplicationContext().getPackageName()+"/app_DRIVER PROFILE DATA"+"/"+folderName+"/";
			String tripFilePath = MacroConstant.APPLICATION_DATA_FOLDER+"/"+folderName+"/";
			logfileName =interruptfileName ;//trip001
			_update_path = path + logfileName;
		}else{
			String path=Environment.getDataDirectory().getPath()+"/data/"+DPUtility.getGlobalApplicationContext().getPackageName()+"/app_DRIVER PROFILE DATA"+"/"+folderName+"/";
			String tripFilePath = MacroConstant.APPLICATION_DATA_FOLDER+"/"+folderName+"/";
			logfileName =interruptfileName ;//trip001
			_update_path = path + logfileName;

		}
		return _update_path;
	}



}
