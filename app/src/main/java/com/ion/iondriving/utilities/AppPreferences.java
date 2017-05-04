package com.ion.iondriving.utilities;

import android.content.Context;
import android.content.SharedPreferences;




/**
 * Created by sarith.vasu on 29-02-2016.
 */
public class AppPreferences {
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private String APP_SHARED_PREFS	= AppPreferences.class.getSimpleName();
    private String AUDIO_ENABLE="setting_audio_enable";
    private String ACCELERATION_THRESHOLD="acceleration_threshold";
    private String VOICE_ALERT="voice_alert";
    private String BRAKING_THRESHOLD="braking_threshold";
    private String ACC_THRESHOLD="acc_threshold";

    public AppPreferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    }

    public boolean getAUDIO_ENABLE() {
        return sharedPrefs.getBoolean(AUDIO_ENABLE, false);
    }
    public void saveAUDIO_ENABLE(boolean allow ) {
        prefsEditor.putBoolean(AUDIO_ENABLE, allow);
        prefsEditor.commit();
    }
    public boolean getACCELERATION_THRESHOLD () {
        return sharedPrefs.getBoolean(ACCELERATION_THRESHOLD, false);
    }
    public void saveACCELERATION_THRESHOLD(boolean allow ) {
        prefsEditor.putBoolean(ACCELERATION_THRESHOLD, allow);
        prefsEditor.commit();
    }
    public boolean getVOICE_ALERT() {
        return sharedPrefs.getBoolean(VOICE_ALERT, false);
    }
    public void saveVOICE_ALERT(boolean allow ) {
        prefsEditor.putBoolean(VOICE_ALERT, allow);
        prefsEditor.commit();
    }
    public int getBRAKING_THRESHOLD() {
        return sharedPrefs.getInt(BRAKING_THRESHOLD, 0);
    }
    public void saveBRAKING_THRESHOLD(int value ) {
        prefsEditor.putInt(BRAKING_THRESHOLD, value);
        prefsEditor.commit();
    }
    public int getACC_THRESHOLD() {
        return sharedPrefs.getInt(ACC_THRESHOLD, 0);
    }
    public void saveAcc_THRESHOLD(int value ) {
        prefsEditor.putInt(ACC_THRESHOLD, value);
        prefsEditor.commit();
    }




}
