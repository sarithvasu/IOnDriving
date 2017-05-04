package com.ion.iondriving.utilities;



import com.ion.iondriving.macro.MacroConstant;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public  class DPConverter extends Exception 
{
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;


	public static String createUniqueId(String  filePath )
	{
		String folderName=null;
		String tripFileCreationTimeStampInString = null;

		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1); 


		String fff=filePath.substring(0, 58);
		String a_letter = Character.toString(filePath.charAt(58));
		if (a_letter.startsWith("T"))
		{
			folderName="Trip";
		}else{
			folderName="Interrupt";
		}

		File fileWithinMyDir = new File(fff+folderName, fileName);




		//File file = new File(filePath);
		boolean doesExist = fileWithinMyDir.exists();
		if (doesExist) 
		{
			long lastModifiedTime = fileWithinMyDir.lastModified();
			Date date = new Date(lastModifiedTime);

			SimpleDateFormat dateFormatter = new SimpleDateFormat(MacroConstant.UNIQUE_KEY_FORMATTER);
			tripFileCreationTimeStampInString = dateFormatter.format(date);

		}
		else 
		{
			System.out.println(MacroConstant.DP_APPLICATION_ERROR+"Failed To Create Unique tripId For The Path"+filePath);
		}

		// Return 
		return tripFileCreationTimeStampInString;
	}



	public static int StringToInt(String number) 
	{
		int intNumber = 0;
		int length = number.length();
		if (length <= 8)
		{
			intNumber = Integer.parseInt(number);

		}
		else
		{
			long longNumber = Long.parseLong(number);

			if (longNumber > Integer.MAX_VALUE)
			{
				System.out.println(MacroConstant.DP_APPLICATION_ERROR+"The Input string is very Large  can not be converted to integer RangeOverflow......");
				intNumber = 0;
			}
			else
			{
				intNumber = Integer.parseInt(number);
			}
		}


		return intNumber;
	}

	public static long StringToLong(String number) 
	{
		long longNumber = Long.parseLong(number);
		return longNumber;
	}

	public static double StringToDouble(String number) 
	{
		double doubleNumber = Double.parseDouble(number);
		return doubleNumber;
	}


	public static long DateToLong(Date date) 
	{
		return date.getTime();
	}

	public static String LongToDateInString(long time, String formater) 
	{
		Date date = new Date(time);
		SimpleDateFormat formatDate = new SimpleDateFormat(formater);
		String StrDate = formatDate.format(date);
		return StrDate;
	}


	public static String LongToTimeInString(long time, String formater) 
	{
		Date date = new Date(time);
		SimpleDateFormat formatDate = new SimpleDateFormat(formater);
		String StrDate = formatDate.format(date);
		return StrDate;
	}

	public static long DeltaInSeconds(String t1,String t2)
	{
		SimpleDateFormat formate = new SimpleDateFormat(MacroConstant.APPLICATION_TIME_FORMAT_12_HOURS);
		Date diff1,diff2;
		long result = 0;
		try {
			//d = sdf.parse("Mon May 27 11:46:15 IST 2013");
			diff1 = formate.parse(t1);
			diff2 = formate.parse(t2);
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(diff1);
			c2.setTime(diff2);
			long time1 = c1.getTimeInMillis();
			long time2 = c2.getTimeInMillis();
			result = time2 - time1;    //Time difference in milliseconds
			System.out.println(MacroConstant.DP_FLOW+"Method Returns...."+result);



		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return result/1000;

	}

	public static final String LongToString(long longvalue) {
		String value = String.valueOf(longvalue);
		return value;
	}


	public static BigDecimal GetNumberWithPrecesion(double number, int precesion)
	{



		if (number > 0) {
			return new BigDecimal(String.valueOf(number)).setScale(precesion,
					BigDecimal.ROUND_FLOOR);
		} else {
			return new BigDecimal(String.valueOf(number)).setScale(precesion,
					BigDecimal.ROUND_CEILING);
		}

	}



	/*	public static long DeltaTInSeconds(Date t1,Date d2)
	{

	}*/


















	public static String stringRepresentationForSeconds(int lastTimeStamp)
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

		StringBuilder timeStr= new StringBuilder();
		if (hour > 0) {
			timeStr.append(hour+" hour");
		}
		if (minute > 0) {

			timeStr.append(minute+" minute ");
		}

		if (seconds > 0) {


			timeStr.append(seconds+"seconds");
		}

		return timeStr.toString();

	}





	public static double ConvertDoubleWithPreseision(double  number,int  preceision){

		String strDouble = String.format("%."+preceision+"f", number);
		double result= Double.parseDouble(strDouble);
		return result;
	}


	public static String getDay(String date)
	{
		SimpleDateFormat format1=new SimpleDateFormat("yyyy/MM/dd");
		Date tripDate;
		String finalDay = null;
		try {
			tripDate = format1.parse(date);
			SimpleDateFormat format2=new SimpleDateFormat(MacroConstant.APPLICATION_DAY_FORMAT); 
			finalDay=format2.format(tripDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalDay;


	}

	/*public static double RoundUpNumber(double num)
	{
		if (!Double.isInfinite(num))
		{
			double correctedNum = 0.0;
			System.out.println("ddddddd"+Math.round(num));
			correctedNum = Math.round(num);
			return correctedNum;
		}
		else
		{
			return num;
		}

	}*/


	public static double RoundUpNumber(double  number)
	{

		String strDouble = String.format("%."+9+"f", number);
		double result= Double.parseDouble(strDouble);
		return result;
	}

	public static double RoundUpNumberForSevendigit(double  number)
	{

		String strDouble = String.format("%."+5+"f", number);
		double result= Double.parseDouble(strDouble);
		return result;
	}


	public static double RoundUpNumberWithPrecession(double num, int iprecession)
	{
		if (!Double.isInfinite(num))
		{
			double correctedNum = 0.0;
			correctedNum = Math.round(num);
			return correctedNum;
		}
		else
		{
			return num;
		}
	}










}


