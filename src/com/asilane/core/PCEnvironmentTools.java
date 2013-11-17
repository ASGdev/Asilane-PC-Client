/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.net.URI;

/**
 * PC environment tools
 * 
 * @author walane
 * 
 */
public class PCEnvironmentTools implements EnvironmentTools {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.core.EnvironmentTools#setClipboardContents(java.lang.String)
	 */
	@Override
	public void setClipboardContents(final String s) {
		final StringSelection stringSelection = new StringSelection(s);
		final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		clipboard.setContents(stringSelection, new ClipboardOwner() {
			@Override
			public void lostOwnership(final Clipboard arg0, final Transferable arg1) {
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.core.EnvironmentTools#browse(java.net.URI)
	 */
	@Override
	public boolean browse(final URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
				return true;
			} catch (final IOException e) {
				return false;
			}
		}

		return false;
	}
}