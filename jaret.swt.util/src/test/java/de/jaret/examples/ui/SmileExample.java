/*
 *  File: SmileExample.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.examples.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import de.jaret.util.ui.SmileyWidget;

/**
 * Simple test window showing a smiley widget and a scale to control the smile.
 * 
 * @author Peter Kliem
 * @version $Id: SmileExample.java 175 2007-01-05 00:01:18Z olk $
 */
public class SmileExample {
	Shell _shell;
	SmileyWidget _sw;
	
	public SmileExample() {
		_shell = new Shell(Display.getCurrent());
		createControls();
		_shell.open();
		Display display = _shell.getDisplay();
		_shell.pack();
		_shell.setSize(400, 430);
		
		/*
		 * do the event loop until the shell is closed to block
		 * the call
		 */
		while (_shell != null && ! _shell.isDisposed()) {
			try {
				if (!display.readAndDispatch())
					display.sleep();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		display.update();
	}

	protected void createControls() {
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		_shell.setLayout(gl);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		
		_sw = new SmileyWidget(_shell, SWT.NULL);
		_sw.setLayoutData(gd);
		
		// scale to control the smile
		final Scale smileScale = new Scale(_shell, SWT.HORIZONTAL);
		smileScale.setMaximum(_sw.getModel().getMaximum());
		smileScale.setMinimum(_sw.getModel().getMinimum());
		smileScale.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent ev) {
				int val = smileScale.getSelection();
				_sw.getModel().setValue(val);
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		smileScale.setLayoutData(gd);
	}

	public static void main(String args[]) {
		SmileExample se = new SmileExample();
	}
}
