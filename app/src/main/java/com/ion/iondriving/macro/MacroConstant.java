package com.ion.iondriving.macro;

/**
 * Created by sarith.vasu on 29-12-2016.
 */

public class MacroConstant {
    public static final String APPLICATION_TIME_FORMAT_SECOND="ss";
    public static final String UNIQUE_KEY_FORMATTER ="yyyyMMddhhmmss";
    public static final double MINIMUM_STATIONARY_SPEED_IN_MILES=.2;
    public static final String APPLICATION_DATE_FORMAT_NEW="dd/MM/yyyyy";
    public static final String APPLICATION_DATE_FORMAT="yyyy/MM/dd";
    public static final String APPLICATION_TIME_FORMAT_24_HOURS  ="HH:mm:ss";
    public static final String APPLICATION_TIME_FORMAT_12_HOURS ="hh:mm:ss a";
    public static final String APPLICATION_DAY_FORMAT="EEEE";
    public static final String APPLICATION_DATA_FOLDER="DRIVER PROFILE DATA";
    public static final String TRIP_FOLDER ="Trip";
    public static final String INTERRUPT_FOLDER  ="Interrupt";
    public static final String DP_CONSOLE_LOG ="Console log :-----------------";
    public static final String DP_APPLICATION_ERROR="DP Application Error :-";
    public static final String  DP_SYSTEM_ERROR="System Error :-";
    public static final String DP_FLOW="Flow :- ++++++++++++++++++++++++++++++++++++++++ ";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS= 1000;
    public static final long DISTANCE_FILTER_IN_METERS= 10;
    public static final int STALE_LOCATION_TIME_INTERVAL_IN_MILLISECONDS= 5000;
    public static final String DATE ="Date";
    public static final String START_TIME ="Start Time";
    public static final String END_TIME ="End Time";
    public static final String  DURATION ="Duration";
    public static final String INTERRUPT_TYPE ="Interrupt Type";
    public static final String INTERRUPT_STATUS ="Interrupt Status";
    public static final double KM_TO_MILE_CONVERSION_FACTOR =0.621371;
    public static final double KM_TO_METER_PER_SECOND_CONVERSION_FACTOR =0.27778;
    public static final double MILE_TO_KM_CONVERSION_FACTOR  =1.60934;
    public static final double MILE_TO_METER_PER_SECOND_CONVERSION_FACTOR =0.44704;
    public static final double METER_PER_SECOND_TO_KPH_CONVERSION_FACTOR =3.6;
    public static final double METER_PER_SECOND_TO_MPH_CONVERSION_FACTOR  =2.23694;
    public static final String  SCORE_RATING_TRIP_ID_KEY ="score_rating_trip_id";
    public static final String  SCORE_RATING_AVERAGE_ACCELERATION_KEY="score_rating_average_acceleration";
    public static final String  SCORE_RATING_AVERAGE_BREAKING_KEY ="score_rating_average_breaking";
    public static final String  SCORE_RATING_AVERAGE_CORNERING_KEY ="score_rating_average_cornering";
    public static final String  SCORE_RATING_AVERAGE_SCORE_KEY ="score_rating_average_score";
    public static final String  SCORE_RATING_TRIP_DATE_KEY ="score_rating_trip_date";
    public static final String  SCORE_RATING_TRIP_DURATION_KEY ="score_rating_trip_duration";
    public static final String  SCORE_RATING_TRIP_DISTANCE_KEY ="score_rating_trip_distance";
    public static final String  SCORE_RATING_FIRST_RECORDED_TIMESTAMP_KEY ="score_rating_first_recorded_timeStamp";
    public static final String  SCORE_RATING_LAST_RECORDED_TIMESTAMP_KEY ="score_rating_last_recorded_timeStamp";

    public static final String SCORE_RATING_ORIGINAL_FILENAME_KEY="score_rating_original_filename";
    public static final String SCORE_RATING_UPDATE_FILENAME_KEY="score_rating_update_filename";
    public static final String SCORE_RATING_ISINTERRUPT_KEY="score_rating_isinterrupt";


    public static final String KEY_CALCULATED_DISTANCE ="calculated_distance";
    public static final String KEY_CSV_COLLECTION ="csv_collection";

    public static final String POST_URL="http://api.net.v3.cloudsites.gearhost.com/api/Device/PostUpload";
    public static final String LOG_FILE_POST_URL ="http://api.net.v3.cloudsites.gearhost.com/Api/Device/LogPostUpload";

    public static final String  INTERRUPT_ID_KEY ="interrupt_id";
    public static final String  INTERRUPT_FOREIGN_KEY_ID ="trip_id_ForeignKey";
    public static final String  INTERRUPT_ACTUAL_FILE_NAME_KEY ="actuaFilename";
    public static final String  INTERRUPT_UPDATED_FILE_NAME_KEY="updatedFilename";
    public static final String INTERRUPT_LAST_RECORDED_TIME_KEY="lastRecordedTimeStamp";
    public static final String INTERRUPT_DATE_KEY="inetrruptDate";


    public static final String TABLE_NAME ="ScoreRatings_Table";

    public static final String INTERRUPT_TABLE_NAME="Interrupt_Table";

    public static final int LATITUDE_INDEX = 1;
    public static final int LONGITUDE_INDEX = 2;
    public static final int SPEED_INDEX = 3;
    public static final int ACCELERATION_INDEX=4;
    public static final int BREAKING_INDEX=5;
    public static final int CORNERING_INDEX=6;

    public static final String LATITUDE_KEY ="Latitude";
    public static final String  LONGITUDE_KEY ="Longitude";
    public static final String  SPEED_KEY ="Speed";
    public static final String  ACCELERATION_KEY= "Acceleration";
    public static final String  BREAKING_KEY ="Breaking";
    public static final String  CORNERING_KEY= "Cornering";


    public static  int NORMAL_VALUE=0;
    public static int  acceleration_value=1;
    public static int  breaking_value=2;

}
