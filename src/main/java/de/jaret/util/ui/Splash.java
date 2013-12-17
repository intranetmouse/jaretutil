/*
 *  File: Splash.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * A splash screen implementation for swt. Displays an image and a textmessage (i.e. version information). The image is
 * retrieved by <code>ClassLoader.getSystemResourceAsStream</code> so it has to be made sure that the resource is in
 * the classpath. Usage is fairly simple: construction will show the splash, dispose if you're done with the splash.
 * 
 * @author Peter Kliem
 * @version $Id: Splash.java 242 2007-02-11 21:05:07Z olk $
 */
public class Splash {
    Image _image;
    Shell _splashShell;
    Label _label;
    Label _msgLabel;
    Display _display;

    /**
     * Constructor for the splash screen
     * 
     * @param rscName resource identifier for the image to be displayed
     * @param messageString message to be displayed underneath the image
     */
    public Splash(String rscName, String messageString) {
        Display display = new Display();
        _display = display;
        InputStream inStream = ClassLoader.getSystemResourceAsStream(rscName);
        _image = new Image(display, inStream);
        _splashShell = new Shell(SWT.ON_TOP);

        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 1;
        gridLayout.marginHeight = 1;
        gridLayout.numColumns = 1;
        _splashShell.setLayout(gridLayout);

        // FillLayout layout = new FillLayout();
        // _splashShell.setLayout(layout);

        GridData gd = new GridData(GridData.FILL_BOTH);
        // gd.horizontalAlignment = GridData.FILL_BOTH;

        _label = new Label(_splashShell, SWT.NONE);
        _label.setImage(_image);
        _label.setLayoutData(gd);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        _msgLabel = new Label(_splashShell, SWT.NONE);
        _msgLabel.setLayoutData(gd);
        _msgLabel.setBackground(new Color(_display, 255, 255, 255));
        _msgLabel.setText(messageString);

        _splashShell.pack();
        Rectangle splashRect = _splashShell.getBounds();
        Rectangle displayRect = display.getBounds();
        int x = (displayRect.width - splashRect.width) / 2;
        int y = (displayRect.height - splashRect.height) / 2;
        _splashShell.setLocation(x, y);
        _splashShell.open();
    }

    public void dispose() {
        _splashShell.close();
        _image.dispose();
        _label.dispose();
    }
}
