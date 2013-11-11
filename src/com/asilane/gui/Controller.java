package com.asilane.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.asilane.core.TextToSpeechThread;
import com.darkprograms.speech.microphone.Microphone.CaptureState;

/**
 * @author walane
 * 
 */
public class Controller implements ActionListener {
	private final GUI gui;

	/**
	 * @param gui
	 */
	public Controller(final GUI gui) {
		this.gui = gui;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		// Stop speech
		TextToSpeechThread.getInstance().stopSpeech();

		// Manual call
		if (!gui.getManualTextField().getText().isEmpty() && !e.getActionCommand().contains("Record")) {
			gui.getTextFieldResponse().setText(
					gui.getAsilane().getAsilane().handleSentence(gui.getManualTextField().getText()).getDisplayedResponse());
			return;
		}

		// Voice call
		if (CaptureState.PROCESSING_AUDIO.equals(gui.getAsilane().getRecordingState())) {
			final String iaResponse = gui.getAsilane().closeRecordAndHandleSentence();
			gui.getBtnRecord().setText("Record");
			gui.getTextFieldResponse().setText(iaResponse);
		} else {
			gui.getBtnRecord().setText("Recording...");
			gui.getAsilane().beginRecord();
		}
	}
}