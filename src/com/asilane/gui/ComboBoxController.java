/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.Properties;

public class ComboBoxController implements ActionListener {
	private final GUI gui;

	/**
	 * @param gui
	 */
	public ComboBoxController(final GUI gui) {
		this.gui = gui;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		final String textLang = gui.localeComboBox.getModel().getSelectedItem().toString();
		if (textLang.equals("English")) {
			gui.getAsilanePCClient().setLocale(Locale.ENGLISH);
		} else if (textLang.equals("Français")) {
			gui.getAsilanePCClient().setLocale(Locale.FRENCH);
		}

		gui.getAsilanePCClient().loadTranslations();

		// Edit each component
		final Properties translationFile = gui.getAsilanePCClient().getTranslationFile();

		gui.lblSlogan.setText(translationFile.getProperty("slogan"));
		gui.manualTextField.setText(translationFile.getProperty("manual_text"));
		gui.validBtn.setText(translationFile.getProperty("valid_button_text"));
	}
}