/*
 *  File: InfoProviderExample.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.examples.ui;

import org.eclipse.swt.widgets.Display;

import de.jaret.util.infoprovider.JaretInfoProvider;
import de.jaret.util.infoprovider.JavaSystemInfoProvider;
import de.jaret.util.infoprovider.SysInfoEntry;
import de.jaret.util.ui.infoprovider.InfoProviderView;

public class InfoProviderExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JaretInfoProvider ip = new JavaSystemInfoProvider();
		InfoProviderView ipv = new InfoProviderView(ip, "Test", SysInfoEntry.ACCESS_DEBUG);
		
		while (ipv.getShell() != null && !ipv.getShell().isDisposed()) {
			Display.getCurrent().readAndDispatch();
		}
		
	}

}
