/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.asilane.core.AsilanePCClient;

/**
 * @author walane
 * 
 */
/**
 * @author walane
 * 
 */
public class GUI extends Container {
	private static final long serialVersionUID = 4542597583417179196L;

	private final AsilanePCClient asilane;
	private JFrame frmAsilane;
	private JLabel image;
	private JButton btnRecord;
	private JTextArea textArea;
	private JTextField manualTextField;
	private JButton btnGo;

	/**
	 * Create the application.
	 */
	public GUI(final AsilanePCClient asilane) {
		this.asilane = asilane;
		initialize();
		frmAsilane.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAsilane = new JFrame();
		frmAsilane.setTitle("Asilane");
		frmAsilane.setResizable(false);
		frmAsilane.setBounds(100, 100, 900, 625);
		frmAsilane.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAsilane.getContentPane().setLayout(null);

		final JLabel lblAsilaneVotre = new JLabel("Asilane  : votre assistant intelligent");
		lblAsilaneVotre.setFont(new Font("Bitstream Charter", Font.BOLD, 20));
		lblAsilaneVotre.setForeground(Color.WHITE);
		lblAsilaneVotre.setBackground(Color.WHITE);
		lblAsilaneVotre.setBounds(280, 77, 388, 66);
		frmAsilane.getContentPane().add(lblAsilaneVotre);

		btnRecord = new JButton("New button");
		btnRecord.setBounds(756, 497, 117, 103);
		btnRecord.setContentAreaFilled(false);
		btnRecord.addActionListener(new Controller(this));
		frmAsilane.getContentPane().add(btnRecord);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(SystemColor.window);
		textArea.setBounds(44, 177, 818, 308);
		frmAsilane.getContentPane().add(textArea);

		manualTextField = new JTextField();
		manualTextField.setBounds(54, 539, 518, 34);
		frmAsilane.getContentPane().add(manualTextField);
		manualTextField.setColumns(10);
		manualTextField.setOpaque(false);

		image = new JLabel();
		image.setBackground(Color.RED);
		image.setBounds(0, 0, 900, 600);
		image.setIcon(new ImageIcon(GUI.class.getResource("/com/asilane/gui/res/theme.jpg")));
		frmAsilane.getContentPane().add(image);

		btnGo = new JButton("Go");
		btnGo.setBounds(604, 543, 117, 25);
		frmAsilane.getContentPane().add(btnGo);
		btnGo.addActionListener(new Controller(this));

	}

	/**
	 * @return the lblResponse
	 */
	public JTextArea getTextFieldResponse() {
		return textArea;
	}

	//
	/**
	 * @return the asilane
	 */
	public AsilanePCClient getAsilane() {
		return asilane;
	}

	//
	// /**
	// * @return the LocaleComboBox
	// */
	// public JComboBox<String> getLocaleComboBox() {
	// return LocaleComboBox;
	// }

	/**
	 * @return the manualTextField
	 */
	public JTextField getManualTextField() {
		return manualTextField;
	}

	/**
	 * @return the btnRecord
	 */
	public JButton getBtnRecord() {
		return btnRecord;
	}
}