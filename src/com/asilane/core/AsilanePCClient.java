/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core;

import java.io.IOException;
import java.net.UnknownServiceException;
import java.util.Locale;
import java.util.Properties;

import javax.sound.sampled.AudioFileFormat;

import com.asilane.core.facade.Response;
import com.asilane.gui.GUI;
import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.microphone.Microphone.CaptureState;
import com.darkprograms.speech.recognizer.Recognizer;

/**
 * Record voice, play and return the IA response
 * 
 * @author walane
 */
public class AsilanePCClient {
	private static final String SAVED_WAV = "asilane_voice.wav";
	private Microphone microphone;
	private final Asilane asilane;
	protected Properties translationFile;

	/**
	 * Create a new Asilane instance
	 */
	public AsilanePCClient() {
		asilane = new Asilane(new PCEnvironmentTools());
		translationFile = new Properties();
		loadTranslations();
	}

	/**
	 * Start the capture of the microphone
	 */
	public void beginRecord() {
		// Record what is saying
		try {
			microphone = new Microphone(AudioFileFormat.Type.WAVE);
			microphone.captureAudioToFile(SAVED_WAV);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Stop the capture of the microphone and if the sentence is understood, a response will be played and returned
	 * 
	 * @return The IA response if the sentence has been understood<br>
	 *         null if not
	 */
	public Response closeRecordAndHandleSentence() {
		Response iaResponse;

		try {
			// Stop microphone capture
			microphone.close();
		} catch (final Exception e) {
			return null;
		}

		// Transform voice into text
		final String textSpeeched = speechToText(SAVED_WAV);
		if (textSpeeched != null) {
			// Understand what means the sentence
			try {
				iaResponse = asilane.handleSentence(textSpeeched);
			} catch (final UnknownServiceException e) {
				iaResponse = new Response(translationFile.get("error_understanding").toString());
				iaResponse.setError(true);
			}

			return iaResponse;
		}

		// If nothing has been heard
		iaResponse = new Response(translationFile.getProperty("error_nothing_heard"));
		iaResponse.setError(true);

		return iaResponse;
	}

	/**
	 * Transform a WAV File into a text
	 * 
	 * @return The text corresponding to the WAV File if is understood<br>
	 *         null if not
	 */
	private String speechToText(final String waveFile) {
		final Recognizer recognizer = new Recognizer();
		recognizer.setLocale(asilane.getLocale().toString().substring(0, 2));

		try {
			return recognizer.getRecognizedDataForWave(waveFile).getResponse();
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Speech a text in argument
	 * 
	 * @param textToSpeech
	 */
	public void textToSpeech(final String textToSpeech) {
		TextToSpeechThread.getInstance().textToSpeech(textToSpeech, asilane.getLocale());
		new Thread(TextToSpeechThread.getInstance()).start();
	}

	/**
	 * Gets the current state of Microphone
	 * 
	 * @return PROCESSING_AUDIO is returned when the Thread is recording Audio and/or saving it to a file<br>
	 *         STARTING_CAPTURE is returned if the Thread is setting variables<br>
	 *         CLOSED is returned if the Thread is not doing anything/not capturing audio
	 */
	public CaptureState getRecordingState() {
		return (microphone == null) ? null : microphone.getState();
	}

	/**
	 * Load translation corresponding to the Asilane language
	 */
	public void loadTranslations() {
		try {
			translationFile.load(getClass().getResourceAsStream("/i18n_client/" + asilane.getLocale().toLanguageTag() + ".properties"));
		} catch (final IOException e) {
			new RuntimeException(e);
		}
	}

	/**
	 * @return the asilane
	 */
	public Asilane getAsilane() {
		return asilane;
	}

	/**
	 * @param lang
	 *            the lang to set
	 */
	public void setLocale(final Locale lang) {
		asilane.setLocale(lang);
	}

	/**
	 * @return the translationFile
	 */
	public Properties getTranslationFile() {
		return translationFile;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// Handle args
		if (args.length > 0) {
			// Help menu
			if (args.length > 2) {
				System.out.println("Usage :\nNo parameter -> GUI Interface");
				System.out
						.println("sentence Locale -> handle sentence in the specified Locale without GUI Interface (not recommended for performances)");
				return;
			}

			// No GUI
			Locale lang = null;
			if (args.length == 1 || args[1].toLowerCase().trim().equals("english")) {
				lang = Locale.ENGLISH;
			} else if (args[1].toLowerCase().trim().equals("french")) {
				lang = Locale.FRENCH;
			}

			// Handle sentence from args
			if (lang == null) {
				System.out.println("Unknow Locale : \"" + args[1] + "\"");
			} else {
				final AsilanePCClient asilane = new AsilanePCClient();
				asilane.setLocale(lang);
				try {
					System.out.println(asilane.getAsilane().handleSentence(args[0]).getSpeechedResponse());
				} catch (final UnknownServiceException e) {
					System.err.println(asilane.translationFile.get("error_understanding"));
				}
			}
		} else {
			new GUI(new AsilanePCClient());
		}
	}
}