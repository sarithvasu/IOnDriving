
package com.ion.iondriving;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.ion.iondriving.utilities.AppPreferences;

public class DPSettingsActivity extends AppCompatActivity {
    private ImageView mIvBack;
    private Switch mSwAudio;

    private Switch mSwVoiceAlert;
    private SeekBar mSbBrakingTher,mSwAccThers;
    private AppPreferences mAppPreferences;
    private TextView mTvBreakingValue,mTvAccValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpsettings);
        mAppPreferences=new AppPreferences(this);
        mIvBack=(ImageView)findViewById(R.id.iv_back);
        mSwAudio=(Switch) findViewById(R.id.audio_sw);
        mSwAccThers=(SeekBar)findViewById(R.id.acc_thers);
        mSwVoiceAlert=(Switch)findViewById(R.id.voice_alert);
        mTvBreakingValue=(TextView)findViewById(R.id.tv_break_value);
        mSbBrakingTher=(SeekBar)findViewById(R.id.seekBar);
        mTvAccValue=(TextView) findViewById(R.id.tv_breaking);
        int values=mAppPreferences.getBRAKING_THRESHOLD();
        mTvBreakingValue.setText("- "+values);
        mTvAccValue.setText("  "+mAppPreferences.getACC_THRESHOLD());

            //  mTvBreakingValue.setText(mSbBrakingTher.getProgress());
        setSettingValues();
        setListnersTOSettings();
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListnersTOSettings() {
        mSwAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAppPreferences.saveAUDIO_ENABLE(isChecked);
            }
        });
        mSwVoiceAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAppPreferences.saveVOICE_ALERT(isChecked);
            }
        });
       /* mSwAccThers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAppPreferences.saveACCELERATION_THRESHOLD(isChecked);
            }
        });*/
        mSbBrakingTher.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAppPreferences.saveBRAKING_THRESHOLD(progress);
                mTvBreakingValue.setText("- "+progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSwAccThers.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAppPreferences.saveAcc_THRESHOLD(progress);
                mTvAccValue.setText("  "+mAppPreferences.getACC_THRESHOLD());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setSettingValues() {
        if(mAppPreferences.getAUDIO_ENABLE())
            mSwAudio.setChecked(true);
        else
            mSwAudio.setChecked(false);
       if(mAppPreferences.getACC_THRESHOLD()>0)
            mSwAccThers.setProgress(mAppPreferences.getACC_THRESHOLD());

        if(mAppPreferences.getVOICE_ALERT())
            mSwVoiceAlert.setChecked(true);
        else
            mSwVoiceAlert.setChecked(false);
        if(mAppPreferences.getBRAKING_THRESHOLD()>0)
            mSbBrakingTher.setProgress(mAppPreferences.getBRAKING_THRESHOLD());
    }
}
