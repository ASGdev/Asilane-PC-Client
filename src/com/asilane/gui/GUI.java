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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.asilane.core.AsilanePCClient;

/**
 * @author walane
 * 
 */
public class GUI extends Container {
	private static final long serialVersionUID = 4542597583417179196L;

	private static final String[] LANGUAGES = new String[] { "English", "Français" };

	private static final String THEME_IMAGE = "/com/asilane/gui/res/theme.jpg";
	private static final String MICRO_HOVER_IMAGE = "/com/asilane/gui/res/micro-hover.png";
	private static final String VALID_HOVER_IMAGE = "/com/asilane/gui/res/valid-hover.jpg";

	private final AsilanePCClient asilane;
	private JFrame frmAsilane;
	protected JLabel image;
	protected JButton btnRecord;
	protected JTextArea responseAera;
	protected JTextField manualTextField;
	protected JButton validBtn;
	protected JLabel validButtonHover;
	protected JLabel btnRecordHover;
	protected JLabel lblSlogan;
	protected JComboBox<String> localeComboBox;

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
	protected void initialize() {
		final Controller controller = new Controller(this);

		frmAsilane = new JFrame();
		frmAsilane.setTitle("Asilane");
		frmAsilane.setResizable(false);
		frmAsilane.setBounds(100, 100, 900, 625);
		frmAsilane.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAsilane.getContentPane().setLayout(null);

		lblSlogan = new JLabel(asilane.getTranslationFile().getProperty("slogan"));
		lblSlogan.setHorizontalAlignment(SwingConstants.CENTER);
		lblSlogan.setFont(new Font("Arial", Font.BOLD, 26));
		lblSlogan.setForeground(Color.WHITE);
		lblSlogan.setBackground(Color.WHITE);
		lblSlogan.setBounds(0, 67, 886, 66);
		frmAsilane.getContentPane().add(lblSlogan);

		btnRecord = new JButton();
		btnRecord.setBounds(756, 497, 100, 100);
		btnRecord.setForeground(null);
		btnRecord.setContentAreaFilled(false);
		btnRecord.setBorder(null);
		btnRecord.addActionListener(controller);
		frmAsilane.getContentPane().add(btnRecord);

		responseAera = new JTextArea();
		responseAera.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		responseAera.setForeground(SystemColor.text);
		responseAera.setOpaque(false);
		responseAera.setEditable(false);
		responseAera.setBackground(SystemColor.window);
		responseAera.setBounds(40, 179, 822, 306);
		frmAsilane.getContentPane().add(responseAera);

		manualTextField = new JTextField(asilane.getTranslationFile().getProperty("manual_text"));
		manualTextField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		manualTextField.setForeground(SystemColor.text);
		manualTextField.setOpaque(false);
		manualTextField.setBorder(null);
		manualTextField.setBounds(60, 539, 507, 34);
		manualTextField.setColumns(10);
		manualTextField.setOpaque(false);
		manualTextField.addActionListener(controller);
		manualTextField.addMouseListener(controller);
		frmAsilane.getContentPane().add(manualTextField);

		validBtn = new JButton(asilane.getTranslationFile().getProperty("valid_button_text"));
		validBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		validBtn.setForeground(SystemColor.text);
		validBtn.setBorder(null);
		validBtn.setContentAreaFilled(false);
		validBtn.setBounds(601, 542, 117, 25);
		validBtn.addActionListener(controller);
		validBtn.addMouseListener(controller);
		frmAsilane.getContentPane().add(validBtn);

		validButtonHover = new JLabel();
		validButtonHover.setEnabled(false);
		validButtonHover.setForeground(null);
		validButtonHover.setBackground(null);
		validButtonHover.setBorder(null);

		validButtonHover.setIcon(new ImageIcon(GUI.class.getResource(VALID_HOVER_IMAGE)));
		frmAsilane.getContentPane().add(validButtonHover);

		btnRecordHover = new JLabel();
		btnRecordHover.setEnabled(false);
		btnRecordHover.setForeground(null);
		btnRecordHover.setBackground(null);
		btnRecordHover.setIcon(new ImageIcon(GUI.class.getResource(MICRO_HOVER_IMAGE)));
		frmAsilane.getContentPane().add(btnRecordHover);

		localeComboBox = new JComboBox<String>(LANGUAGES);
		localeComboBox.setBounds(746, 0, 140, 36);
		localeComboBox.addActionListener(new ComboBoxController(this));
		frmAsilane.getContentPane().add(localeComboBox);

		image = new JLabel();
		image.setBackground(Color.RED);
		image.setBounds(0, 0, 900, 600);
		image.setIcon(new ImageIcon(GUI.class.getResource(THEME_IMAGE)));
		frmAsilane.getContentPane().add(image);

	}

	/**
	 * @return the asilane
	 */
	public AsilanePCClient getAsilanePCClient() {
		return asilane;
	}

	/**
	 * Switch valid button state
	 */
	public void switchValidButtonState() {
		if (validButtonHover.isEnabled()) {
			validButtonHover.setEnabled(false);
			validButtonHover.setBounds(0, 0, 0, 0);
		} else {
			validButtonHover.setEnabled(true);
			validButtonHover.setBounds(601, 539, 117, 34);
		}
	}

	/**
	 * Switch record button state
	 */
	public void switchRecordButtonState() {
		if (btnRecordHover.isEnabled()) {
			btnRecordHover.setEnabled(false);
			btnRecordHover.setBounds(0, 0, 0, 0);
		} else {
			btnRecordHover.setEnabled(true);
			btnRecordHover.setBounds(756, 497, 117, 100);
		}
	}
}