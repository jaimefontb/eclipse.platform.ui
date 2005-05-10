/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.browser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.PlatformUI;

/**
 * Preferences for the Web browser.
 */
public class WebBrowserPreference {
	protected static final String PREF_BROWSER_HISTORY = "webBrowserHistory"; //$NON-NLS-1$

	protected static final String PREF_INTERNAL_WEB_BROWSER_HISTORY = "internalWebBrowserHistory"; //$NON-NLS-1$

	protected static final String PREF_BROWSER_CHOICE = "browser-choice"; //$NON-NLS-1$

	private static final String INTERNAL_BROWSER_ID = "org.eclipse.ui.browser.editor"; //$NON-NLS-1$

	private static final String BROWSER_SUPPORT_ID = "org.eclipse.ui.browser.editorSupport"; //$NON-NLS-1$

	public static final int INTERNAL = 0;

	public static final int SYSTEM = 1;

	public static final int EXTERNAL = 2;

	/**
	 * WebBrowserPreference constructor comment.
	 */
	private WebBrowserPreference() {
		super();
	}

	/**
	 * Returns the URL to the homepage.
	 * 
	 * @return java.lang.String
	 */
	public static String getHomePageURL() {
		try {
			// get the default home page
			URL url = WebBrowserUIPlugin.getInstance().getBundle().getEntry(
					"home/home.html"); //$NON-NLS-1$
			url = Platform.resolve(url);
			return url.toExternalForm();
		} catch (Exception e) {
			return "http://www.eclipse.org"; //$NON-NLS-1$
		}
	}

	/**
	 * Returns the preference store.
	 * 
	 * @return the preference store
	 */
	protected static IPreferenceStore getPreferenceStore() {
		return WebBrowserUIPlugin.getInstance().getPreferenceStore();
	}

	/**
	 * Returns the Web browser history list.
	 * 
	 * @return java.util.List
	 */
	public static List getInternalWebBrowserHistory() {
		String temp = getPreferenceStore().getString(
				PREF_INTERNAL_WEB_BROWSER_HISTORY);
		StringTokenizer st = new StringTokenizer(temp, "|*|"); //$NON-NLS-1$
		List l = new ArrayList();
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			l.add(s);
		}
		return l;
	}

	/**
	 * Sets the Web browser history.
	 * 
	 * @param list
	 *            the history
	 */
	public static void setInternalWebBrowserHistory(List list) {
		StringBuffer sb = new StringBuffer();
		if (list != null) {
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				sb.append(s);
				sb.append("|*|"); //$NON-NLS-1$
			}
		}
		getPreferenceStore().setValue(PREF_INTERNAL_WEB_BROWSER_HISTORY,
				sb.toString());
		WebBrowserUIPlugin.getInstance().savePluginPreferences();
	}

	/**
	 * Returns whether the internal browser is used by default
	 * 
	 * @return true if the internal browser is used by default
	 */
	public static boolean isDefaultUseInternalBrowser() {
		return WebBrowserUtil.canUseInternalWebBrowser();
	}

	/**
	 * Returns whether the system browser is used by default
	 * 
	 * @return true if the system browser is used by default
	 */
	public static boolean isDefaultUseSystemBrowser() {
		return WebBrowserUtil.canUseSystemBrowser();
	}

	/**
	 * Returns whether the internal, system or external browser is being used
	 * 
	 * @return one of <code>INTERNAL</code>, <code>EXTERNAL</code> and
	 *         <code>SYSTEM</code>.
	 */
	public static int getBrowserChoice() {
		int choice = getPreferenceStore().getInt(PREF_BROWSER_CHOICE);
		if (choice == INTERNAL && !WebBrowserUtil.canUseInternalWebBrowser())
			choice = SYSTEM;
		if (choice == SYSTEM && !WebBrowserUtil.canUseSystemBrowser())
			choice = EXTERNAL;
		return choice;
	}

	/**
	 * Sets whether the internal, system and external browser is used
	 * 
	 * @param choice
	 *            </code>INTERNAL</code>, <code>SYSTEM</code> and <code>EXTERNAL</code>
	 */
	public static void setBrowserChoice(int choice) {
		getPreferenceStore().setValue(PREF_BROWSER_CHOICE, choice);
		WebBrowserUIPlugin.getInstance().savePluginPreferences();
		updateDefaultEditor(choice);
	}

	private static void updateDefaultEditor(int choice) {
		// Toggle from internal editor to browser support
		// to avoid confusion between default editors and
		// Web browser preference page
		IEditorRegistry registry = PlatformUI.getWorkbench()
				.getEditorRegistry();
		String oldId = choice == INTERNAL ? BROWSER_SUPPORT_ID
				: INTERNAL_BROWSER_ID;
		String newId = choice == INTERNAL ? INTERNAL_BROWSER_ID
				: BROWSER_SUPPORT_ID;

		String[][] extensions = { { "a.html", "*.html" }, { "a.htm", "*.htm" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				{ "a.shtml", "*.shtml" } };  //$NON-NLS-1$//$NON-NLS-2$

		// For each default editor that matches the oldId, change
		// the default editor to the newId
		for (int i = 0; i < extensions.length; i++) {
			String[] ext = extensions[i];
			IEditorDescriptor ddesc = registry.getDefaultEditor(ext[0]);
			if (ddesc != null && ddesc.getId().equals(oldId)) {
				registry.setDefaultEditor(ext[1], newId);
			}
		}
	}
}