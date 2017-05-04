package com.ion.iondriving.utilities;

import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.ion.iondriving.macro.MacroConstant;
import com.ion.iondriving.model.DPMotionManager;
import com.ion.iondriving.model.DPServerConfig;

import java.util.Date;

/**
 * Created by sarith.vasu on 29-12-2016.
 */

public class DPAutomaticTripFileManager {
    private static DPAutomaticTripFileManager instance_1;
    public boolean m_IsPreviusLocationLatitudeInitialized;
    double  m_PreviusLocationLatitude;
    String m_vehicleStopTimeStamp;
    Date currentTimeInDateFormat,previousTimeInDateFormat;
    public static DPAutomaticTripFileManager getSingletonInstance(){

        if (instance_1 == null) {

            instance_1 = new DPAutomaticTripFileManager();
        }
        return instance_1;

    }
    public boolean isVehicleStationaryNew(Location currentLocation , double currentSpeed , boolean hasCrossedMinSpeed)
    {
        if (currentLocation==null) {
            return false;
        }
        boolean result = false;
        //Manas Done  in 31st Dec
        //currentSpeed=0.0;


        if(currentSpeed < DPServerConfig.CONFIGUE_SPEED_LIMIT_IN_MILES)
        {
            if (!m_IsPreviusLocationLatitudeInitialized)
            {
                m_PreviusLocationLatitude = currentLocation.getLatitude();
                m_IsPreviusLocationLatitudeInitialized = true;
                result = true;
            }
            else
            {
                this.m_PreviusLocationLatitude = currentLocation.getLatitude();
                if (currentSpeed <= MacroConstant.MINIMUM_STATIONARY_SPEED_IN_MILES)//.2
                {
                    result = true;
                }
                else
                {
                    // Clean up member variable
                    this.m_vehicleStopTimeStamp = null;
                }
            }
        }
        else
        {
            result = false;
        }

        return result;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void manageAutomaticTripFile(boolean hasCrossedMinSpeed , boolean isCollectionEmpty)
    {
        if(hasCrossedMinSpeed == true)
        {
            // Vehicle is stationary so lock the time in self.m_vehicleStopTimeStamp
            if (m_vehicleStopTimeStamp == null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    m_vehicleStopTimeStamp=new SimpleDateFormat("HH:mm:ss").format(new Date());
                }
            }
            else
            {
                // If the vehicle is stationary for 5 minutes then create an automatic trip file.
                checkAndCreateAutomaticTripFile(hasCrossedMinSpeed ,isCollectionEmpty);
            }
        }
        else
        {
            //	        NSString *str2 = [DP_FLOW stringByAppendingString:@"Vehicle has Not Crossed Min Speed.."];
            //	        [DPLogger log:str2];
        }
    }


    // If the vehicle is stationary for 5 minutes then create an automatic trip file.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void checkAndCreateAutomaticTripFile(boolean hasCrossedMinSpeed , boolean isCollectionEmpty)
    {
        String currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        }
        SimpleDateFormat format1= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format1 = new SimpleDateFormat("HH:mm:ss");
        }

        try{
            currentTimeInDateFormat = format1.parse(currentTime);
            previousTimeInDateFormat=format1.parse(m_vehicleStopTimeStamp);
        }catch(Exception e){

        }
        long currentTimeLong=currentTimeInDateFormat.getTime();
        long previouTimeLong=previousTimeInDateFormat.getTime();
        long timeDiffInMillisec=currentTimeLong-previouTimeLong;
        long timeDiffInsec=timeDiffInMillisec/1000;
        System.out.println("current time is"+currentTime);
        System.out.println("previoustime is"+m_vehicleStopTimeStamp);
        System.out.println("previous time in long"+previouTimeLong);
        System.out.println("current time in long"+currentTimeLong);
        System.out.println("time Difference in milliseconds"+timeDiffInMillisec);
        System.out.println("time difference in sec"+timeDiffInsec);

        if (timeDiffInsec>=90) // 5 minutes
        {
            System.out.println("Entered  in  to the loop  of time Difference");
            if(!isCollectionEmpty)
            {
                System.out.println("Collection is not null");
                DPMotionManager.getSingletonInstance().saveDataAutomatic();


                //Automatic WIFI Upload
                //[[DPWIFIUploader sharedInstance]uploadTripFile];

                // Clean up member variable
                hasCrossedMinSpeed = false;
                CleanUpStationaryMemberVariable();
            }
            else
            {
                return;
            }
        }
    }

    public void CleanUpStationaryMemberVariable()
    {
        m_vehicleStopTimeStamp = null;
        m_PreviusLocationLatitude = 0.0000;
        m_IsPreviusLocationLatitudeInitialized = false;
    }
}
