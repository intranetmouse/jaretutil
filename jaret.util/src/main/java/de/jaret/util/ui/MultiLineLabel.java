/*
 *  File: MultiLineLabel.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import de.jaret.util.swt.TextRenderer;

/**
 * Mostly for testing.
 * 
 * @author Peter Kliem
 * @version $Id: MultiLineLabel.java 242 2007-02-11 21:05:07Z olk $
 */
public class MultiLineLabel extends Canvas {
    String _text;

    public MultiLineLabel(Composite parent, int style) {
        super(parent, style);
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent event) {
                onPaint(event);
            }

        });
    }

    private void onPaint(PaintEvent event) {
        GC gc = event.gc;
        TextRenderer.renderText(gc, getClientArea(), true, false, _text);
    }

    /**
     * @return Returns the text.
     */
    public String getText() {
        return _text;
    }

    /**
     * @param text The text to set.
     */
    public void setText(String text) {
        _text = text;
    }
}
