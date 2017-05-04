package com.ion.iondriving.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;

import com.ion.iondriving.core_logic.ABCCalculator;
import com.ion.iondriving.dpconverter.VectorBase;
import com.ion.iondriving.macro.MacroConstant;
import com.ion.iondriving.utilities.DPAutomaticTripFileManager;
import com.ion.iondriving.utilities.DPBusinessEvent;
import com.ion.iondriving.utilities.DPCSVDataHelper;
import com.ion.iondriving.utilities.DPConverter;
import com.ion.iondriving.utilities.DPUtility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.ion.iondriving.macro.MacroConstant.NORMAL_VALUE;

/**
 * Created by sarith.vasu on 29-12-2016.
 */

public class DPMotionManager implements SensorEventListener {
    private static DPMotionManager instance_1;
    private boolean m_isMonitoring;
    private SensorManager m_CMotionManager;
    private Sensor m_sensor, m_sensor_Gyro;
    private boolean m_hasCrossedMinSpeed;
    private Timer m_timer;
    double m_previouaccelerationValue;
    SharedPreferences m_sharedpreference;
    String conversionUnit;
    private boolean m_IsAccelerometerAndGyroscopeInitialised = false;
    private VectorBase m_rawAccelerometerData = null;
    private VectorBase m_rawGyroscopeData = null;
    TimerTask m_timerTask;
    private double m_currentSpeed;
    private ABCCalculator m_calculator;
    private Location m_currentLocation;
    private int m_tempCount;
    TextView lblSpeed, lblspeedUnit;


    private Context mContext;

    private DPMotionManager() {

    }

    // ## Single Instance Created
    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public static DPMotionManager getSingletonInstance() {
        if (instance_1 == null) {
            instance_1 = new DPMotionManager();

        }

        return instance_1;
    }

    public void init(Context m_Context) {
        m_CMotionManager = (SensorManager) m_Context.getSystemService(m_Context.SENSOR_SERVICE);
        m_sensor = m_CMotionManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m_sensor_Gyro = m_CMotionManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //Notification Need  to be  Done  Once the  App is  shut Down  And Save The Trip

        m_isMonitoring = false;
    }

    public boolean startMonitoringDPMotionManagerWithTimer() {
        // Clean up member variable
        m_hasCrossedMinSpeed = false;
        //m_hasCrossedMinSpeed = true;
        if (m_timer != null) {
            System.out.println(MacroConstant.DP_FLOW + "timer is not nil before timerCallback..");
            m_timer.cancel();
            m_timer = null;
        }


        m_timer = new Timer();
        m_timerTask = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                /*PowerManager mgr = (PowerManager)m_context.getSystemService(Context.POWER_SERVICE);
				WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
				wakeLock.acquire();*/
                timerCallback();
                System.out.println(MacroConstant.DP_FLOW + "Testing Timer....Now");

            }
        };
        m_timer.scheduleAtFixedRate(m_timerTask, 0, DPServerConfig.DATA_CAPTURE_INTERVAL_IN_SECOND * 1000);
        System.out.println(MacroConstant.DP_CONSOLE_LOG + "timerCallback started.. | timer is valid :" + m_timer);

        // Start Accelerometer and Gyroscope
        m_CMotionManager.registerListener(this, m_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        m_CMotionManager.registerListener(this, m_sensor_Gyro, SensorManager.SENSOR_DELAY_NORMAL);

        return true;
    }

    public boolean isMonitoring() {
        return m_isMonitoring;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void timerCallback() {

        if (!this.HasSensorInitialized()) {
            System.out.println(MacroConstant.DP_APPLICATION_ERROR + " Control Returned From TimerCallBack Method Due to Null Calculator Object");
            return;
        }


        SimpleDateFormat formater = new SimpleDateFormat(MacroConstant.APPLICATION_TIME_FORMAT_12_HOURS);
        String time = formater.format(new java.util.Date());
        System.out.println(" ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^befoure time in timer call back == %@ and nsadte  === %@" + time + Calendar.getInstance().getTime());

        m_calculator = ABCCalculator.getSingletonInstance().Caculate(m_rawAccelerometerData, m_currentSpeed, 17.0);


        boolean isSuccessFullyAddedToCollection = false;
        DPAutomaticTripFileManager tripFileManagerSharedInstance = DPAutomaticTripFileManager.getSingletonInstance();
        m_hasCrossedMinSpeed = true;
        //Check if Vehicle is Stationary

        if (tripFileManagerSharedInstance.isVehicleStationaryNew(m_currentLocation, m_currentSpeed, m_hasCrossedMinSpeed)) {
            if (m_currentLocation == null) {
                return;
            }
            // We confirm here vehicle is Stationary
            System.out.println(MacroConstant.DP_FLOW + "Vehicle is Stationary..............................................");
            /* soumen blocked*/
            tripFileManagerSharedInstance.manageAutomaticTripFile(m_hasCrossedMinSpeed, isSuccessFullyAddedToCollection);
        }
        if (this.m_hasCrossedMinSpeed == true) {
            if (this.m_currentSpeed >= MacroConstant.MINIMUM_STATIONARY_SPEED_IN_MILES) {
                isSuccessFullyAddedToCollection = DPCSVDataHelper.getSingletonInstance().buildCSVDataAndAddToCollection(this.m_calculator, this.m_currentLocation, this.m_rawAccelerometerData, this.m_rawGyroscopeData, this.m_currentSpeed,NORMAL_VALUE);
                Log.e("DPMotionManager",""+NORMAL_VALUE);

                System.out.println(MacroConstant.DP_FLOW + "Vechile is Moving--------------------------------------------");

                // Save the trip data if the battery level fall bellow 0 percent
                // soumen blocked
                //this.forcefullySaveTripDataInLowBattery();
            }
        }

        SimpleDateFormat formater1 = new SimpleDateFormat(MacroConstant.APPLICATION_TIME_FORMAT_12_HOURS);
        String time1 = formater1.format(new java.util.Date());
        System.out.println(" *******************************************************after time in timer call back == %@ and nsadte  === %@" + time1 + Calendar.getInstance().getTime());

      
		/*//Code For Testing On Desk
        if (!m_isReadyToWriteInterrupt)
            {
                DPInterruptHandler sharedInstance]initializeInterruptHandler];
                m_isReadyToWriteInterrupt = true;
                this.m_hasCrossedMinSpeed=true;
            }*/

		/*
        if(!m_isReadyToWriteInterrupt)
        {

            runOnUiThread(new Runnable() {
                public void run() {
                    DPCallandSMSInterruptHandler.getSingletonInstance();

                }
            });

            m_isReadyToWriteInterrupt = true;
            this.m_hasCrossedMinSpeed=true;
        }*/
		/*if(m_hasCrossedMinSpeed)
		{
			if (!m_isReadyToWriteInterrupt)
			{
				runOnUiThread(new Runnable() {
					public void run() {
						DPCallandSMSInterruptHandler.getSingletonInstance();

					}
				});

				//need to  do  Interrupt Handler with
				//    DPCallandSMSInterruptHandler sharedInstance=new DPCallandSMSInterruptHandler();
				//[[DPBatteryStatus sharedInstance]getBatteryNotification];//initilize the battery notification
				m_isReadyToWriteInterrupt = true;
			}
		}*/
        m_tempCount = m_tempCount + 1;

    }

    private boolean HasSensorInitialized() {

        return m_IsAccelerometerAndGyroscopeInitialised;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //  CalibrationInitializer utilityManager = CalibrationInitializer.getSingletonInstance();

        Sensor m_deviceSensor = event.sensor;

        if (m_deviceSensor.getType() == Sensor.TYPE_GYROSCOPE) {
            m_rawGyroscopeData = new VectorBase();
            m_rawGyroscopeData.setX(event.values[0]);
            m_rawGyroscopeData.setY(event.values[1]);
            m_rawGyroscopeData.setZ(event.values[2]);
        } else if ((m_deviceSensor.getType() == Sensor.TYPE_ACCELEROMETER)) {
            if (m_previouaccelerationValue == event.values[0]) {
                System.out.println("matching.......");
            } else {
                System.out.println("Not................. matching");
            }
            m_previouaccelerationValue = event.values[0];

            m_rawAccelerometerData = new VectorBase();
            m_rawAccelerometerData.setX(event.values[0]);
            m_rawAccelerometerData.setY(event.values[1]);
            m_rawAccelerometerData.setZ(event.values[2]);
        }
        if (m_rawAccelerometerData != null && m_rawGyroscopeData != null) {
            //System.out.println(MacroConstant.DP_FLOW+"accelerometer data in x-axis...."+m_rawAccelerometerData.getX()+m_rawAccelerometerData.getY()+m_rawAccelerometerData.getZ() );
            //System.out.println(MacroConstant.DP_FLOW+"rawGyroscope data in x-axis...."+m_rawGyroscopeData.getX()+m_rawGyroscopeData.getY()+m_rawGyroscopeData.getZ() );

            //System.out.println(MacroConstant.DP_FLOW+"Current Speed in onSensorChanged is ..."+m_currentSpeed);
            //	this.m_calculator=utilityManager.GetResult(m_rawAccelerometerData,m_rawGyroscopeData,m_currentSpeed, DPServerConfig.CONFIGUE_SPEED_LIMIT_IN_MILES);
            //System.out.println(MacroConstant.DP_FLOW+"calculator is...."+m_calculator);
			/*try {
				VectorBase  data=CalibrationInitializer.getSingletonInstance().GetResult(m_rawAccelerometerData, m_rawGyroscopeData, 10, 5, 0.0123, 0.3455);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
            m_IsAccelerometerAndGyroscopeInitialised = true;
        }


        if (this.m_hasCrossedMinSpeed && (this.m_currentSpeed > MacroConstant.MINIMUM_STATIONARY_SPEED_IN_MILES)) {
            //sushil comments
            double x_axisMagnitude = 0;//this.m_calculator.getX_AxisMagnitudeDifference();
            double y_axisMagnitude = 0;//this.m_calculator.getY_AxisMagnitudeDifference();
            double z_axisMagnitude = 0;//this.m_calculator.getZ_AxisMagnitudeDifference();

            this.enable_Disable_Timer_WithAccelerometreXYZ(x_axisMagnitude, y_axisMagnitude, z_axisMagnitude);
        }
    }

    public static double lastSpeed = 15;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }



    public void waypointWithLocation(Location waypoint, double calculatedSpeed, TextView lblspeed, TextView lblspeedUnit) {

            this.lblSpeed = lblspeed;
            this.lblspeedUnit = lblspeedUnit;

            System.out.println(MacroConstant.DP_FLOW + "Received Location Update....");
            this.m_currentLocation = waypoint;

            if (calculatedSpeed >= 0) {
                System.out.println(MacroConstant.DP_FLOW + "calculatre  sp....");
                m_currentSpeed = DPConverter.ConvertDoubleWithPreseision(calculatedSpeed /*MacroConstant.METER_PER_SECOND_TO_MPH_CONVERSION_FACTOR*/, 1); // meter per second to mph


                System.out.println(MacroConstant.DP_FLOW + "current  speed  in  mph...." + m_currentSpeed);

                if (!m_hasCrossedMinSpeed) {
                    if (m_currentSpeed > DPServerConfig.CONFIGUE_SPEED_LIMIT_IN_MILES) {
                        m_hasCrossedMinSpeed = true;


                    }
                }

                //lblSpeed.setText(String.valueOf(DPConverter.ConvertDoubleWithPreseision(checkForSpeedUnit(m_currentSpeed), 1)));


                lblSpeed.setText(m_currentSpeed + "");

                //i am Simply trying to check whether it will work or not

         /*       if (lastSpeed < m_currentSpeed) {

                    dpBusinessEvent.AccelerationBreakingAudioAlert(m_currentSpeed, 1.f);
                    dpBusinessEvent.checkForSwervingEvents();
                }
                if (lastSpeed > m_currentSpeed) {

                    dpBusinessEvent.AccelerationBreakingAudioAlert(m_currentSpeed, 1.f);
                    dpBusinessEvent.checkForSwervingEvents();
                }

                lastSpeed = m_currentSpeed;
            }
*/


                //dps.updateSpeed(m_currentSpeed);
                //Need  to  do later  to pass in monitor screen
                //[[NSNotificationCenter defaultCenter] postNotificationName:@"UPDATE_SPEED" object:[NSNumber numberWithFloat:_currentSpeed]];
            }

    }


    private double checkForSpeedUnit(double speedInMiles) {
        double convertedSpeed = 0;

        if (speedInMiles != 0) {
            //i am chaning the sc
            System.out.println(MacroConstant.DP_FLOW + "speed  is  in km/h");
            convertedSpeed = speedInMiles * 2.f;/* MacroConstant.MILE_TO_METER_PER_SECOND_CONVERSION_FACTOR;*/
        }// Return
        return convertedSpeed;


    }


    private double checkForSpeedUnit_NotinUse(double speedInMiles) {

        double convertedSpeed = 0;
        if (speedInMiles != 0) {
            // Receive require speed unit from preference screen
            //            String conversionUnit=DPBusinessEvent.getSingletonInstance().getDPPreferenceObject().toString();
            m_sharedpreference = PreferenceManager.getDefaultSharedPreferences(DPUtility.getGlobalApplicationContext());
            conversionUnit = m_sharedpreference.getString("SPEED_VALUE", null);

            switch (conversionUnit) {
                case "km/h":

                    System.out.println(MacroConstant.DP_FLOW + "speed  is  in km/h");
                    convertedSpeed = speedInMiles * MacroConstant.MILE_TO_KM_CONVERSION_FACTOR;
                    break;
                case "m/s":

                    System.out.println(MacroConstant.DP_FLOW + "speed  is  in m/s");
                    convertedSpeed = speedInMiles * MacroConstant.MILE_TO_METER_PER_SECOND_CONVERSION_FACTOR;
                    break;
                case "mph":

                    System.out.println(MacroConstant.DP_FLOW + "speed  is  in mph");
                    convertedSpeed = speedInMiles;
                    break;
                default:

                    System.out.println(MacroConstant.DP_FLOW + "speed  is  in mph");
                    convertedSpeed = speedInMiles;
                    break;
            }

        }

        return convertedSpeed;


    }

    public boolean hasCrossedMinimumSpeed() {
        return m_hasCrossedMinSpeed;
    }

    public void stopMonitoringDPMotionManager() {
        //Soumen this.m_hasCrossedMinSpeed=false;
        System.out.println(MacroConstant.DP_CONSOLE_LOG + "invalidate............");

        if (m_timer != null) {
            m_timer.cancel();
            System.out.println(MacroConstant.DP_FLOW + "timer call back cancelled...........");
        }
        if (m_CMotionManager != null) {
            //Stop Both The Sensors(Accelerometer and Gyroscope)

            m_CMotionManager.unregisterListener(this);
        }
        // Soumen Blocked  m_isMonitoring=false;
        System.out.println(MacroConstant.DP_FLOW + "is Monitoring Value at stop...........");
        m_timer = null;
        // Soumen Blocked  m_isReadyToWriteInterrupt=false;
    }

    public void resetMemberVariables() {
        this.m_hasCrossedMinSpeed = false;
        m_isMonitoring = false;
        //m_isReadyToWriteInterrupt=false;

        // Only this variable need to be removed
        m_tempCount = 0;
    }


    public void saveDataInMotionManagerManual() {
        if (this.m_hasCrossedMinSpeed == true) {
            DPCSVDataHelper.getSingletonInstance().saveDataInDPCSVDataHelper();
            System.out.println(MacroConstant.DP_FLOW + "...........Manual Data Save");

            // delete the Log file and create new log file with the same name because trip file is genrated successfully
            //this.refreshLogFile();
        }
    }


    public void saveDataAutomatic() {
        DPCSVDataHelper.getSingletonInstance().saveDataInDPCSVDataHelper();

        DPLocationManager.getSingletonInstance().stopMonitoringLocationManager();
        this.stopMonitoringDPMotionManager();
        if (DPBusinessEvent.getSingletonInstance().isSufficientMemoryAvailable()) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DPLocationManager.getSingletonInstance().startMonitoringLocationManager(mContext, lblSpeed, lblspeedUnit);
                }
            });
            this.startMonitoringDPMotionManager(mContext);
        }
    }

    public void highSpeedtimerCallbackWithAccelerometer() {
        boolean isSuccessFullyAddedToCollection = false;
        System.out.println(MacroConstant.DP_FLOW + "has crossed minimum speed---------" + m_hasCrossedMinSpeed);
        if (m_hasCrossedMinSpeed == true) {
            System.out.println(MacroConstant.DP_FLOW + "current speed is-----------" + m_currentSpeed + "and  minimum speed is..........." + MacroConstant.MINIMUM_STATIONARY_SPEED_IN_MILES);
            if (m_currentSpeed > MacroConstant.MINIMUM_STATIONARY_SPEED_IN_MILES) {
                isSuccessFullyAddedToCollection = DPCSVDataHelper.getSingletonInstance().buildCSVDataAndAddToCollection(this.m_calculator, this.m_currentLocation, this.m_rawAccelerometerData, this.m_rawGyroscopeData, this.m_currentSpeed,NORMAL_VALUE);
                //System.out.println(MacroConstant.DP_FLOW+"Started Capturing Data Without Timer Due to High Z Magnitude Difference------------------------------------------");

            }
        }
    }

    public void enable_Disable_Timer_WithAccelerometreXYZ(double X_MagnitudeDifference, double Y_MagnitudeDifference, double Z_MagnitudeDifference) {
        //System.out.println("Entered into  enable_Disable_Timer_WithAccelerometreXYZ");
        boolean x_Difference = ((X_MagnitudeDifference >= 1.1f && X_MagnitudeDifference <= 2.0f) || (X_MagnitudeDifference <= -1.1f && X_MagnitudeDifference >= -2.0f)) ? true : false;
        boolean y_Difference = ((Y_MagnitudeDifference >= 1.1f && Y_MagnitudeDifference <= 2.0f) || (Y_MagnitudeDifference <= -1.1f && Y_MagnitudeDifference >= -2.0f)) ? true : false;
        boolean z_Difference = ((Z_MagnitudeDifference >= 1.81f && Z_MagnitudeDifference <= 3.0f) || (Z_MagnitudeDifference <= -1.81f && Z_MagnitudeDifference >= -3.0f)) ? true : false;

        //System.out.println(MacroConstant.DP_FLOW+"Magnitude Difference X+"+X_MagnitudeDifference+"Y="+Y_MagnitudeDifference+"Z="+Z_MagnitudeDifference+"and"+"current speed="+m_currentSpeed);
        //System.out.println(MacroConstant.DP_FLOW+"X Difference+"+x_Difference+"Y Difference"+y_Difference+"Z Difference"+z_Difference+"and"+"current speed="+m_currentSpeed);

        if (x_Difference || y_Difference || z_Difference) {
            this.stopTimerCallBack();
            this.highSpeedtimerCallbackWithAccelerometer();
        } else {
            this.startTimerCallBack();
        }
    }

    public void startTimerCallBack() {
        if (m_timer == null) {
            m_timer = new Timer();
            m_timerTask = new TimerTask() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    timerCallback();
                }
            };
            m_timer.scheduleAtFixedRate(m_timerTask, 0, DPServerConfig.DATA_CAPTURE_INTERVAL_IN_SECOND * 1000);
            //System.out.println(MacroConstant.DP_FLOW+"timer started Successfully.........................");
        }
    }

    public boolean startMonitoringDPMotionManager(Context m_Context) {
        this.mContext = m_Context;
        this.init(m_Context);
        if (m_CMotionManager == null) {
            System.out.println("cmMotionManager is nil..");
            return false;
        }

        if (!m_isMonitoring) {
            m_isMonitoring = this.startMonitoringDPMotionManagerWithTimer();
        }
        System.out.println("is Monitoring value at Start =" + m_isMonitoring);

        return m_isMonitoring;
    }

    public void stopTimerCallBack() {
        if (m_timer != null) {
            m_timer.cancel();
            m_timer = null;
            System.out.println(MacroConstant.DP_FLOW + "stopped Timer call Back due to High Z Magnitude Difference..");

            DPAutomaticTripFileManager.getSingletonInstance().CleanUpStationaryMemberVariable();
        }
    }


}
