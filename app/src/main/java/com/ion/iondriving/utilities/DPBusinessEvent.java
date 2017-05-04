package com.ion.iondriving.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.ion.iondriving.MonitorSpeedActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import static com.ion.iondriving.macro.MacroConstant.NORMAL_VALUE;
import static com.ion.iondriving.macro.MacroConstant.acceleration_value;
import static com.ion.iondriving.macro.MacroConstant.breaking_value;

public class DPBusinessEvent {

    private static final double DPServerConfig = 0;//getAudibleSpeedAlertLimit +5 miles per hour
    long MINIMUM_FREE_SPACE_IN_MB = 500;
    TextToSpeech m_tts;
    MediaPlayer m_player;
    int counter = 0;
    boolean _isSpeakingSynthesizer = false;
    boolean m_isSpeedInitialized, m_isSwervingInitialized, m_isSpeechSynthesizerInitialized;
    double m_previousSpeed=0;

    String AUDIBLE_SPEEDING_MESSAGE = "You are currently exceeding the posted speed limit",
            AUDIBLE_HARD_BREAKING_MESSAGE = "Hard brake detected. Remember to leave sufficient space between you and the car in front of you",
            AUDIBLE_SWERVING_MESSAGE = "Excessive swerving can lead to accidents",
            AUDIBLE_CORNERING_MESSAGE = "Remember to reduce speed when taking turns",
            AUDIBLE_CRASH_MESSAGE = "Crash Detected";
    ArrayList<Date> arrChronologicalEvents;
    int m_swervingCount;
    int CONFIG_SPEED = 0;
  private  AppPreferences appPreferences;
    //TTSManager m_ttsManager=null;


    private static DPBusinessEvent instance_1;


    // ## Single Instance Created
    public static synchronized DPBusinessEvent getSingletonInstance() {
        if (instance_1 == null) {
            instance_1 = new DPBusinessEvent();
        }

        return instance_1;
    }

    //Memory check
    public boolean isSufficientMemoryAvailable() {
        // TODO Auto-generated method stub
        long minFreeSpaceInBytes = MINIMUM_FREE_SPACE_IN_MB;//500mb
        boolean result = false;
        try {

            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
            System.out.println("Total Memory Available in Byte is" + bytesAvailable + "Free Space  is" + minFreeSpaceInBytes);

            if (bytesAvailable > minFreeSpaceInBytes) {
                result = true;
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("isSufficientMemoryAvailable Exception is" + e.getMessage());
        }
        System.out.println("Total Memory Available is" + result);
        return result;
    }


    public void showAlert(String message) {
        // TODO Auto-generated method stub
        try {

            new AlertDialog.Builder(DPUtility.getGlobalApplicationContext()).setTitle("Alert!").setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void speakText(final String toBeSpoken, Context context) {
        // TODO Auto-generated method stub
        try {
            m_tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {

                    // TODO Auto-generated method stub

                    if (status == TextToSpeech.SUCCESS) {
                        if (status == TextToSpeech.SUCCESS) {
                            int result = m_tts.setLanguage(Locale.US);
                            if (result == TextToSpeech.LANG_MISSING_DATA ||
                                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                System.out.println("error" + "This Language is not supported");
                            } else {
                                ConvertTextToSpeech(toBeSpoken);
                            }
                        } else
                            System.out.println("error" + "Initilization Failed!");
                    }
                }

                private void ConvertTextToSpeech(String toBeSpoken) {
                    // TODO Auto-generated method stub
                    m_tts.speak(toBeSpoken, TextToSpeech.QUEUE_ADD, null);

                }


            }

            );

			/*	//m_tts.setLanguage(Locale.UK);
            //m_tts.speak(toBeSpoken, TextToSpeech.QUEUE_FLUSH, null);
				m_tts = new TextToSpeech(this,(OnInitListener) this);
			new TextToSpeech.OnInitListener() {

				@Override
				public void onInit(int status) {
					// TODO Auto-generated method stub

					if (status == TextToSpeech.SUCCESS) {

						int result = m_tts.setLanguage(Locale.US);
						if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

							if (m_player!=null) {
								m_player.pause();
							}
							m_tts.setPitch(2);
						}
					}
				}
			};
			//m_tts.speak(toBeSpoken, TextToSpeech.QUEUE_ADD, null);
			 */

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public void runVoiceAlertOnSeparateThread(final String toBeSpoken) {
        // TODO Auto-generated method stub


        try {
            if (!_isSpeakingSynthesizer) {
                MonitorSpeedActivity.m_ttsManager.
                        addQueue(toBeSpoken);





            }


        } catch (Exception e) {

        }
    }

    public void speechSynthesizer() {
        // TODO Auto-generated method stub

        _isSpeakingSynthesizer = false;


    }

    // A Generic Method to throw voice alert for over speed, bad cornering, hard breaking and swerving
    public void throwVoiceAlert(double xAxisCorrectedAccelerometerReading, double xAxisBaseAccelerometerReading, double currentSpeed, float accuracy,Context context) {
        // TODO Auto-generated method stub
        if (_isSpeakingSynthesizer) {
            //m_ttsManager = new TTSManager();
            //m_ttsManager.init(DPUtility.getGlobalApplicationContext());
        }

     //   AccelerationBreakingAudioAlert(currentSpeed, accuracy);
        checkForSwervingEvents();

        if (currentSpeed > CONFIG_SPEED) {

          // AccelerationBreakingAudioAlert(currentSpeed, accuracy);
            CorneringAudioAlert(xAxisCorrectedAccelerometerReading, xAxisBaseAccelerometerReading);
            checkForSwervingEvents();
        }

    }

    // Start :- Acceleration Breaking Audio Alert
    public int  AccelerationBreakingAudioAlert(double currentSpeed, float accuracy,Context context) {
        // TODO Auto-generated method stu
        appPreferences =new AppPreferences(context);
        try {

            if (!m_isSpeedInitialized) {
                m_previousSpeed = currentSpeed;
                m_isSpeedInitialized = true;
            }
            double speedDiff = currentSpeed - m_previousSpeed;
            if (speedDiff < -appPreferences.getBRAKING_THRESHOLD()) {
                //if (speedDiff > DPServerConfig) {
                NORMAL_VALUE=breaking_value;
                if(appPreferences.getVOICE_ALERT()) {
                    runVoiceAlertOnSeparateThread(AUDIBLE_SPEEDING_MESSAGE);
                    // addswervingEvent();
                }
                Log.e("DPBusinessEvent",""+speedDiff);
                Log.e("DPBusinessEvent",""+NORMAL_VALUE);
                //}
            } else if (speedDiff > appPreferences.getACC_THRESHOLD()) {
                NORMAL_VALUE=acceleration_value;
                //if (speedDiff < DPServerConfig) {
                if(appPreferences.getVOICE_ALERT()) {
                    runVoiceAlertOnSeparateThread(AUDIBLE_HARD_BREAKING_MESSAGE);
                    //addswervingEvent();
                }
                //}
                Log.e("DPBusinessEvent",""+NORMAL_VALUE);

                Log.e("DPBusinessEvent",""+speedDiff);
            } else {
                NORMAL_VALUE=0;
            }

            m_previousSpeed = currentSpeed;
        } catch (Exception e) {
            // TODO: handle exception
        }
        Log.e("DPBusinessEvent",""+NORMAL_VALUE);
        return NORMAL_VALUE;
    }

    // Start :- Cornering Audio Alert
    public void CorneringAudioAlert(double xAxisCorrectedAccelerometerReading, double xAxisBaseAccelerometerReading) {
        // TODO Auto-generated method stub

        try {
            // If both the numbers are of same sing then Subtract them, otherwise for different sing Add them
            double magnitudeDifference = GetMagnitudeDifference(xAxisCorrectedAccelerometerReading, xAxisBaseAccelerometerReading);
            // First Check whether the g-force difference is within total allowable limit or not => +/- 0.0 to 2.0g
            if ((magnitudeDifference >= (0.0f) && magnitudeDifference <= (2.0f)) || (magnitudeDifference < 0.0f && magnitudeDifference >= -2.0f)) {
                if ((magnitudeDifference >= 1.1f && magnitudeDifference <= 2.0f) || (magnitudeDifference <= -1.0f && magnitudeDifference >= -2.0f)) {
                    // Throw voice message
                  //  runVoiceAlertOnSeparateThread(AUDIBLE_CORNERING_MESSAGE);
                    // Store this event (swervingEvent) in array in chronological order. (Only store the time stamp fot this event)
                    addswervingEvent();
                }
            } else {

            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    // Check and throw an audible alert if two or more successive swerves occurs within a 10 second period
    public double GetMagnitudeDifference(double streemA, double baseB) {
        // TODO Auto-generated method stub

        //try {
        double result;
        boolean IsBoth_the_numbers_are_of_same_sing = IsSameSignNumber(streemA, baseB);
        if (IsBoth_the_numbers_are_of_same_sing == true) {
            result = Math.abs(streemA - baseB);
            // Get the sing of stream data (streemA) and then set the sing of the result to sing of stream data
            String sign = CheckNegativeOrpositiveNumber(streemA);
            if (sign.equalsIgnoreCase("POSITIVE NUMBER")) {
                // Change the sing of the result to Positive Number using Math.Abs method
                result = Math.abs(result);
            } else {
                // Change the sing of the result to negative by multiplying with (-1).
                result = result * (-1);
            }

        } else {
            // If both the numbers are of different sing then Add them with their absolute values and keep the sing of stream data,
            result = Math.abs(streemA) + Math.abs(baseB);
            // Get the sing of stream data (streemA) and then set the sing of the result to sing of stream data
            String sign = CheckNegativeOrpositiveNumber(streemA);
            if (sign.equalsIgnoreCase("POSITIVE NUMBER")) {

                // Change the sing of the result to Positive Number using Math.Abs method
                result = Math.abs(result);
            } else {
                // Change the sing of the result to negative by multiplying with (-1).
                result = result * (-1);
            }

        }

		/*} catch (Exception e) {
			// TODO: handle exception
		}*/
        return result;

    }

    public String CheckNegativeOrpositiveNumber(double number) {
        // TODO Auto-generated method stub
        String result;
        if (number < 0) {

            result = "NEGATIVE NUMBER";
        } else {
            result = "POSITIVE NUMBER";
        }
        return result;
    }

    public boolean IsSameSignNumber(double numberA, double numberB) {
        // TODO Auto-generated method stub

        boolean result = false;

        String outputA = CheckNegativeOrpositiveNumber(numberA);
        String outputB = CheckNegativeOrpositiveNumber(numberB);

        if (outputA.equals(outputB)) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    public void checkForSwervingEvents() {
        // TODO Auto-generated method stub
        Date t2;
        try {

            // Check for empty array
            if (arrChronologicalEvents != null) {
                // Check for at least two or more successive swerves events
                if (arrChronologicalEvents.size() >= 2) {
                    // Get the total no. of swearing events
                    int count = arrChronologicalEvents.size();
                    // Get the last swearing event time stamp ( t2 = current )
                    t2 = arrChronologicalEvents.get(count - 1);
                    // Get the prior to last (last - 1) swearing event time stamp ( t1 = previous )
                    Date t1 = arrChronologicalEvents.get(count - 2);

                    // Get the time Difference in seconds
                    long timeDiff = t2.getTime() - t1.getTime();
                    if (timeDiff > 0 & timeDiff <= DPServerConfig) {
                        // No delay (waiting), immediately throw the voice alert
                        // Throw voice message
                    //    runVoiceAlertOnSeparateThread(AUDIBLE_SWERVING_MESSAGE);
                        resetEvents();
                    }

                }
            } else {
                //do nothing
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    // Just keep the last event and remove all other events from the array
    public void resetEvents() {
        // TODO Auto-generated method stub

        try {
            if (arrChronologicalEvents != null) {

                int lastEvent = arrChronologicalEvents.lastIndexOf(arrChronologicalEvents);

                ArrayList<Integer> arrChronologicalEvents = new ArrayList<Integer>();

                arrChronologicalEvents.lastIndexOf(lastEvent);

                m_swervingCount = 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // To check any abnormal successive driving events => two or more successive swerves within a 10 second period needs to be audible alert
    public void addswervingEvent() {
        // TODO Auto-generated method stub

        try {
            if (!m_isSwervingInitialized) {

                //ArrayList<Integer>arrChronologicalEvents = new ArrayList<>();
                //ArrayList arrChronologicalEvents;
                arrChronologicalEvents = new ArrayList<Date>();
                m_isSwervingInitialized = true;
            }

            Date date = null;
            arrChronologicalEvents.add(date);
            m_swervingCount = m_swervingCount + 1;

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


}
