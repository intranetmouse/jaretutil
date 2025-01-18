/*
 *  File: ListComposerExample.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.examples.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.jaret.util.ui.ListComposer;
import de.jaret.util.ui.model.IMutableContentProvider;
import de.jaret.util.ui.model.MutableListContentProvider;

/**
 * Simple example and test for the ListComposer.
 * @author Peter Kliem
 * @version $Id: ListComposerExample.java 175 2007-01-05 00:01:18Z olk $
 */
public class ListComposerExample {

	Shell _shell;
	List _src;
	List _dest;

	public ListComposerExample() {
		_shell = new Shell(Display.getCurrent());
		_shell.setSize(400,200);
		initLists();
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
		dumpLists();
	}

	/**
	 * 
	 */
	private void dumpLists() {
		Iterator it = _src.iterator();
		while (it.hasNext()) {
			System.out.println("src:"+it.next().toString());
		}
		it = _dest.iterator();
		while (it.hasNext()) {
			System.out.println("dest:"+it.next().toString());
		}
		
	}

	private void initLists() {
		_src = new ArrayList();
		_dest = new ArrayList();
		
		_src.add("Saturn");
		_src.add("Mars");
		_src.add("Mercury");
		_src.add("Uranus");
		_src.add("Neptune");
		_src.add("Jupiter");
		_src.add("Earth");
		_src.add("Pluto");
		_src.add("Venus");
	}

	protected void createControls() {
		_shell.setLayout(new FillLayout());

		ListComposer listComposer = new ListComposer(_shell, SWT.NULL);
		
		IContentProvider srcCp = new MutableListContentProvider(_src);
		IMutableContentProvider destCp = new MutableListContentProvider(_dest);
		
		listComposer.setSrcContentProvider(srcCp);
		listComposer.setDestContentProvider(destCp);
		
		listComposer.setInput("GO"); // there is no real base input object
	}

	public static void main(String args[]) {
		ListComposerExample lct = new ListComposerExample();
	}

}
