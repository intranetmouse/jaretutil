/*
 *  File: MultiLineLabelExample.java 
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.jaret.util.ui.MultiLineLabel;

/**
 * @author Peter Kliem
 */
public class MultiLineLabelExample {

	Shell _shell;

	public MultiLineLabelExample() {
		_shell = new Shell(Display.getCurrent());
		_shell.setSize(400,200);
		createControls();
		_shell.open();
		Display display;
		display = _shell.getDisplay();
		
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
		_shell.setLayout(new FillLayout());

		MultiLineLabel mll = new MultiLineLabel(_shell, SWT.NULL);
		
		String text = "Dies ist die erste Zeile des Testtextes f�r das MultiLineLabel\nABSATZ"+
		"Und da noch mehr Text ben�tigt wird machen wir's einfach l�nger\n Absatz mit Space muss auch dabei sein";
		mll.setText(text);
		
//		Button b = new Button(_shell, SWT.PUSH);
	//	b.setText("test");
/*		b.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				dumpLists();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
*/		
	}


	public static void main(String args[]) {
		MultiLineLabelExample lct = new MultiLineLabelExample();
	}



}
