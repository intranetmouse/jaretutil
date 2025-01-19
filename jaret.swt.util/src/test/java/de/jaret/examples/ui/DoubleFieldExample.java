/*
 *  File: DoubleFieldExample.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.examples.ui;

import java.text.ParseException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.jaret.util.ui.DoubleField;

/**
 * Simple example and test for the ListComposer.
 * @author Peter Kliem
 * @version $Id: DoubleFieldExample.java 175 2007-01-05 00:01:18Z olk $
 */
public class DoubleFieldExample {

    Shell _shell;
    List _src;
    List _dest;

    public DoubleFieldExample() {
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
    }
    
    protected void createControls() {
        _shell.setLayout(new RowLayout());
        
        Text text = new Text(_shell, SWT.BORDER | SWT.RIGHT);
        final DoubleField df = new DoubleField();
        df.setText(text);
        
        Button b1 = new Button(_shell, SWT.PUSH);
        b1.setText("out");
        b1.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                try {
                    System.out.println("Value:"+df.getValue());
                } catch (ParseException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });
        Button b2 = new Button(_shell, SWT.PUSH);
        b2.setText("set");
        b2.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                df.setValue(123.4);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });
        
     }

    public static void main(String args[]) {
        DoubleFieldExample dfe = new DoubleFieldExample();
    }

}
