/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core;

import java.util.Locale;

import javax.sound.sampled.AudioFileFormat;

import com.asilane.core.facade.Response;
import com.asilane.gui.GUI;
import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.microphone.Microphone.CaptureState;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.darkprograms.speech.recognizer.Recognizer;

/**
 * Record voice, play and return the IA response
 * 
 * @author walane
 */
public class AsilanePCClient {
	private static final String SAVED_WAV = "asilane-voice.wav";
	private Microphone microphone;
	private final Asilane asilane;

	/**
	 * Create a new Asilane instance
	 */
	public AsilanePCClient() {
		asilane = new Asilane(null);
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
			e.printStackTrace();
		}
	}

	/**
	 * Stop the capture of the microphone and if the sentence is understood, a response will be played and returned
	 * 
	 * @return The IA response if the sentence has been understood<br>
	 *         null if not
	 */
	public String closeRecordAndHandleSentence() {
		try {
			// Stop microphone capture
			microphone.close();
		} catch (final Exception e) {
			return null;
		}

		// Transform voice into text
		final String textSpeeched = speechToText(SAVED_WAV);
		if (textSpeeched != null) {
			Response iaResponse;
			// Understand what means the sentence
			iaResponse = asilane.handleSentence(textSpeeched);

			// Say the response
			textToSpeech(iaResponse.getSpeechedResponse());
			// Return the response;
			return iaResponse.getDisplayedResponse();
		}

		// If nothing has been heard
		if (asilane.getLocale() == Locale.FRANCE) {
			return "Rien n'a été entendu.";
		}
		return "Nothing has been head.";
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
			final GoogleResponse response = recognizer.getRecognizedDataForWave(waveFile);
			System.out.println(response.getOtherPossibleResponses());
			return response.getResponse();
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Speech a text in argument
	 * 
	 * @param textToSpeech
	 */
	private void textToSpeech(final String textToSpeech) {
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
				lang = Locale.FRANCE;
			}

			// Handle sentence from args
			if (lang == null) {
				System.out.println("Unknow Locale : \"" + args[1] + "\"");
			} else {
				final AsilanePCClient asilane = new AsilanePCClient();
				asilane.setLocale(lang);
				System.out.println(asilane.getAsilane().handleSentence(args[0]).getSpeechedResponse());
			}
		} else {
			new GUI(new AsilanePCClient());
		}
	}
}