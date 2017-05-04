package com.ion.iondriving;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ion.iondriving.model.DPLocationManager;
import com.ion.iondriving.model.DPMotionManager;
import com.ion.iondriving.utilities.DPBusinessEvent;
import com.ion.iondriving.utilities.DPUtility;
import com.ion.iondriving.utilities.TTSManager;

import static com.ion.iondriving.macro.MacroConstant.NORMAL_VALUE;
import static com.ion.iondriving.macro.MacroConstant.breaking_value;

public class MonitorSpeedActivity extends AppCompatActivity implements View.OnClickListener,SensorEventListener {

    private Button mBtnStartStop;
    private TextView mTvspeed,mTvkpm;
    private ImageView mIvSettings,mIvArche;
    private String TAG="MonitorSpeedActivity";
    private  LocationManager  locationManager;
    public static TTSManager m_ttsManager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_speed);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        m_ttsManager=new TTSManager();
        m_ttsManager.init(getApplicationContext());
    //        validateConnections();
     if( checkPermission() ) {
         validateConnections();
         getView();
     }else
         askPermission();

    }
    public void getView()
    {

        mBtnStartStop=(Button)findViewById(R.id.btn_start_stop);
        mTvspeed=(TextView) findViewById(R.id.tv_speed);
        mIvSettings=(ImageView)findViewById(R.id.iv_settings);
        mTvkpm=(TextView)findViewById(R.id.tv_kpm);
        mIvArche=(ImageView) findViewById(R.id.iv_arch_list);
        mIvArche.setOnClickListener(this);
        DPUtility.SetGlobalApplicationContext(this);
        mBtnStartStop.setOnClickListener(this);
        mIvSettings.setOnClickListener(this);
    }

    private final int REQ_PERMISSION = 999;
    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    validateConnections();
                    getView();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void validateConnections() {

         if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        } else {
            try {
                getView();
            } catch (Exception e) {

            }
        }
    }

    private void showGPSDisabledAlertToUser() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please Trun on the Gps")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);
                    }
                });
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.iv_arch_list){
            startActivity(new Intent(this,DPMonitoringArchiveList.class));
        }
        else if(v.getId()==R.id.btn_start_stop) {
            if (((Button) v).getText().toString().equals(getString(R.string.start))) {
                mBtnStartStop.setText(getString(R.string.stop));
                startMonitoring(mTvspeed, mTvkpm);
            } else if (((Button) v).getText().toString().equals(getString(R.string.stop))) {
                mBtnStartStop.setText(getString(R.string.start));
                stopMonitoring();
            }
        }
        else if(v.getId()==R.id.iv_settings){
            startActivity(new Intent(this,DPSettingsActivity.class));
        }

    }
    DPBusinessEvent dpBusinessEvent=new DPBusinessEvent();
    public void startMonitoring(TextView lblSpeed, TextView lblspeedUnit)
    {
       // DPMotionManager.getSingletonInstance();


        if (!DPMotionManager.getSingletonInstance().isMonitoring())
        {
			/*if (m_ttsManager==null) {
				m_ttsManager = new TTSManager();
				m_ttsManager.init(DPUtility.getGlobalApplicationContext());
			}*/

            DPLocationManager.getSingletonInstance().startMonitoringLocationManager(getApplicationContext(),lblSpeed,lblspeedUnit);
            DPMotionManager.getSingletonInstance().init(this);
            DPMotionManager.getSingletonInstance().startMonitoringDPMotionManagerWithTimer();
            /*soumen blocked*/
         /*   if (isMyServiceRunning(TestService.class))
            {
                System.out.println("Hii"+isMyServiceRunning(TestService.class));
            }
            else
            {
                startService(new Intent(this, TestService.class));
                System.out.println("service started");
            }*/


        }
    }
    //Definition of stopMonitoring Method
    public void stopMonitoring()
    {
        //calling the stopMonitoringDPMotionManager method
        DPMotionManager.getSingletonInstance().stopMonitoringDPMotionManager();

        //calling the stopMonitoringLocationManager method
        DPLocationManager.getSingletonInstance().stopMonitoringLocationManager();

        //calling the saveDataInMonitorManual method
        saveDataInMonitorManual();

        //calling the createTripFile method To create Interrupt


        //Calling the Method resetMemberVariables to resetting  the Member Variable
        DPMotionManager.getSingletonInstance().resetMemberVariables();

        //Stopping the service which started during startMonitoring
        mTvspeed.setText("0.00");
        NORMAL_VALUE=0;

    }
    public void saveDataInMonitorManual()
    {
        //calling the saveDataInMotionManagerManual method
        DPMotionManager.getSingletonInstance().saveDataInMotionManagerManual();

        //Calling the intent which one is called by this


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
