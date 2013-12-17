/*
 *  File: ColorSpot.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Custom widget showing a colored spot.
 * 
 * @author Peter Kliem
 * @version $Id: SmileyWidget.java 242 2007-02-11 21:05:07Z olk $
 */
public class ColorSpot extends Canvas {
    /** default prefreed size. */
    private static final int PREFSIZE_DEFAULT = 10;
    /** default color. */
    private static final RGB COLOR_DEFAULT = new RGB(255, 0, 0);

    /** the preferred size. */
    private int _preferredSize = PREFSIZE_DEFAULT;
    /** the color (allocated). */
    private Color _spotColor;
    /** the rgb value for the color. */
    private RGB _spotRGB = COLOR_DEFAULT;

    /**
     * Construct a spot.
     * 
     * @param parent parent composite
     * @param style style bits
     */
    public ColorSpot(Composite parent, int style) {
        super(parent, style);
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent event) {
                onPaint(event);
            }
        });
        setBackground(parent.getBackground());
    }

    /**
     * The paint method simply paints a spot on the whole area.
     * 
     * @param event paint event
     */
    private void onPaint(PaintEvent event) {
        GC gc = event.gc;
        int width = getClientArea().width;
        int height = getClientArea().height;

        Color bg = gc.getBackground();

        gc.setBackground(getColorIntern());
        gc.fillOval(0, 0, width - 1, height - 1);

        gc.setBackground(bg);
        gc.drawOval(0, 0, width - 1, height - 1);

    }

    /**
     * Get the allocated color. Allocates the color from the rgb value if not already done.
     * 
     * @return the color
     */
    private Color getColorIntern() {
        if (_spotColor != null) {
            return _spotColor;
        }
        _spotColor = new Color(Display.getCurrent(), _spotRGB);
        return _spotColor;
    }

    /**
     * Get the color of the spot.
     * 
     * @return RGB
     */
    public RGB getSpotRGB() {
        return _spotRGB;
    }

    /**
     * Set the color of the spot as RGB. Freeing of the color will be accomplished by the widget.
     * 
     * @param spotRGB color as RGB
     */
    public void setSpotRGB(RGB spotRGB) {
        _spotRGB = spotRGB;
        if (_spotColor != null) {
            _spotColor.dispose();
            _spotColor = null;
        }
    }

    /**
     * {@inheritDoc} Dispose the color.
     */
    public void dispose() {
        super.dispose();
        if (_spotColor != null) {
            _spotColor.dispose();
        }
    }

    /**
     * {@inheritDoc} Uses the hint if set and the preferred size.
     */
    public Point computeSize(int whint, int hhint) {
        if (whint != SWT.DEFAULT && hhint != SWT.DEFAULT) {
            return new Point(whint, hhint);
        }
        if (whint != SWT.DEFAULT) {
            return new Point(whint, _preferredSize);
        }
        if (hhint != SWT.DEFAULT) {
            return new Point(_preferredSize, hhint);
        }
        return new Point(_preferredSize, _preferredSize);

    }

    /**
     * {@inheritDoc} Simply delegates to computeSize(int, int).
     */
    public Point computeSize(int hint, int hint2, boolean changed) {
        return computeSize(hint, hint2);
    }

    /**
     * Get the preferred size.
     * 
     * @return preferred size
     */
    public int getPreferredSize() {
        return _preferredSize;
    }

    /**
     * Set the preferred size.
     * 
     * @param preferredSize preferred size (width and height)
     */
    public void setPreferredSize(int preferredSize) {
        _preferredSize = preferredSize;
    }

}
