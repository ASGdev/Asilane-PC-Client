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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Date;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.core.EnvironmentTools#mail(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean mail(final String dest, final String subject, final String message) {
		if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
			return false;
		}

		try {
			Desktop.getDesktop().mail(
					URI.create("mailto:" + dest + "?subject=" + AsilaneUtils.encode(subject) + "&body=" + AsilaneUtils.encode(message)));
		} catch (final UnsupportedEncodingException e) {
			return false;
		} catch (final IOException e) {
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.core.EnvironmentTools#addCalendarEvent(java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public void addCalendarEvent(final String title, final Date beginDate, final Date endDate) {
		throw new UnsupportedOperationException("You can do this only if you are on a Android device");
	}
}