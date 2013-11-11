/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.UnknownServiceException;
import java.util.Properties;

import com.asilane.core.TextToSpeechThread;
import com.asilane.core.facade.Response;
import com.darkprograms.speech.microphone.Microphone.CaptureState;

/**
 * @author walane
 * 
 */
public class Controller extends MouseAdapter implements ActionListener {
	private final GUI gui;
	private final Properties translationFile;

	/**
	 * @param gui
	 */
	public Controller(final GUI gui) {
		this.gui = gui;
		translationFile = gui.getAsilanePCClient().getTranslationFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		Response iaResponse = null;

		// Stop speech
		TextToSpeechThread.getInstance().stopSpeech();

		try {
			// Manual call
			if ((e.getActionCommand().equals(gui.getAsilanePCClient().getTranslationFile().get("valid_button_text")) || e.getSource()
					.toString().contains("JTextField"))) {
				if (gui.manualTextField.getText().isEmpty()
						|| gui.manualTextField.getText().equals(gui.getAsilanePCClient().getTranslationFile().get("manual_text"))) {
					return;
				}
				iaResponse = gui.getAsilanePCClient().getAsilane().handleSentence(gui.manualTextField.getText());
			}
			// Voice call
			else {
				gui.switchRecordButtonState();

				if (CaptureState.PROCESSING_AUDIO.equals(gui.getAsilanePCClient().getRecordingState())) {
					iaResponse = gui.getAsilanePCClient().closeRecordAndHandleSentence();
				} else {
					gui.getAsilanePCClient().beginRecord();
				}
			}
		} catch (final UnknownServiceException e1) {
			iaResponse = new Response(translationFile.get("error_understanding").toString());
			iaResponse.setError(true);
		}

		if (iaResponse != null) {
			// Set color of response corresponding to the success, or not
			if (iaResponse.isError()) {
				gui.responseAera.setForeground(Color.ORANGE);
			} else {
				gui.responseAera.setForeground(Color.WHITE);
			}

			// Display response and speech it
			gui.responseAera.setText(iaResponse.getDisplayedResponse());
			gui.getAsilanePCClient().textToSpeech(iaResponse.getSpeechedResponse());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(final MouseEvent e) {
		if (e.getComponent().equals(gui.manualTextField)) {
			if (gui.manualTextField.getText().equals(translationFile.get("manual_text"))) {
				gui.manualTextField.setText(null);
			}
		} else {
			gui.switchValidButtonState();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(final MouseEvent e) {
		if (!e.getComponent().equals(gui.manualTextField)) {
			gui.switchValidButtonState();
		}
	}
}