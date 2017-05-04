package com.ion.iondriving.utilities;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

/**
 Created by Manas Ranjan Pradhan on 1/28/15.
 */
public class TTSManager {

	private TextToSpeech m_tts = null;

	private boolean isLoaded = false;

	public void init(final Context context) {

		if (!isLoaded) {
			try {
				m_tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
					@Override
					public void onInit(int status) {

						if (status == TextToSpeech.SUCCESS) {
							if (status == TextToSpeech.SUCCESS) {
								int result = m_tts.setLanguage(Locale.US);

								/*@SuppressWarnings("deprecation")
								int resulttt = m_tts.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {

									@Override
									public void onUtteranceCompleted(String utteranceId) {
										// TODO Auto-generated method stub
										if (utteranceId.equalsIgnoreCase("done")) {
											System.out.println("Ended..................");
											DPBusinessEvent.getSingletonInstance()._isSpeakingSynthesizer=false;

										}

									}
								});*/
								addQueue("");

								isLoaded = true;

								if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
									System.out.println("error" + "This Language is not supported");
								}
							} else{
								System.out.println("error" + "Initialization Failed!");
							}
						}
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public TextToSpeech getInstance() {
		return m_tts;
	}

	public void shutDown() {
		m_tts.shutdown();
		isLoaded=false;
	}

	public void addQueue(String text) {
			m_tts.speak(text, TextToSpeech.QUEUE_ADD, null);

	}

	public void initQueue(String text) {

		m_tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
}