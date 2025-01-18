/*
 *  File: SmileyWidget.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Custom (fun) widget visualizing an integer as a smiley.
 * 
 * @author Peter Kliem
 * @version $Id: SmileyWidget.java 242 2007-02-11 21:05:07Z olk $
 */
public class SmileyWidget extends Canvas implements ChangeListener {
    private double _currentValue = 0;
    private BoundedRangeModel _brModel = new DefaultBoundedRangeModel(50, 0, 0, 100);
    private boolean _eyeBrows = true;
    private boolean _colorChange = true;

    private Color _neutral = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
    private Color _positive = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
    private Color _negative = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
    private Color _currentColor;

    public SmileyWidget(Composite parent, int style) {
        super(parent, style);
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent event) {
                onPaint(event);
            }
        });
        calcSmileFactor();
        _brModel.addChangeListener(this);
    }

    private void onPaint(PaintEvent event) {
        GC gc = event.gc;
        int width = getClientArea().width;
        int height = getClientArea().height;

        int lineWidth = height / 40;
        if (!_colorChange) {
            gc.setBackground(_neutral);
        } else {
            if (_currentValue >= 0) { // positive
                gc.setBackground(calcColor(_positive));
            } else { // negative
                gc.setBackground(calcColor(_negative));
            }
        }

        Path p = new Path(Display.getCurrent());
        p.addArc(0 + lineWidth / 2, 0 + lineWidth / 2, width - 1 - lineWidth, height - 1 - lineWidth, 0, 360);
        gc.fillPath(p);
        gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
        gc.setLineWidth(lineWidth);
        gc.drawPath(p);
        p.dispose();
        // eyes
        int y = height / 3;
        int x1 = width / 3;
        int x2 = width - width / 3;
        int r = width / 30;
        // eyes have a minimal size
        if (r == 0) {
            r = 1;
        }
        gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
        gc.fillOval(x1 - r, y - r, 2 * r, 2 * r);
        gc.fillOval(x2 - r, y - r, 2 * r, 2 * r);
        // eye brows
        if (_eyeBrows) {
            gc.setLineWidth(lineWidth / 2);
            int ebWidth = width / 10;
            int yDist = height / 13;
            int yOff = (int) (_currentValue * (double) height / 30);
            int xShift = (int) (_currentValue * (double) width / 90);
            p = new Path(Display.getCurrent());
            p.moveTo(x1 - ebWidth / 2 + xShift, y - yDist + yOff);
            p.lineTo(x1 + ebWidth / 2 - xShift, y - yDist - yOff);
            gc.drawPath(p);
            p.dispose();

            p = new Path(Display.getCurrent());
            p.moveTo(x2 - ebWidth / 2 + xShift, y - yDist - yOff);
            p.lineTo(x2 + ebWidth / 2 - xShift, y - yDist + yOff);
            gc.drawPath(p);
            p.dispose();
        }
        // mouth
        gc.setLineWidth(lineWidth);
        x1 = (int) (width / 4.5);
        x2 = width - x1;
        y = height - height / 3;
        int midX = width / 2;
        int offset = (int) (_currentValue * (double) height / 3);
        p = new Path(Display.getCurrent());
        p.moveTo(x1, y);
        p.quadTo(midX, y + offset, x2, y);
        gc.drawPath(p);
        p.dispose();
    }

    /**
     * Scales the BoundedRangeModel to [-1, 1]
     */
    private void calcSmileFactor() {
        int range = _brModel.getMaximum() - _brModel.getMinimum();
        int mid = _brModel.getMinimum() + range / 2;
        int value = _brModel.getValue();
        _currentValue = (double) (value - mid) / (double) (range / 2);
        // due to rounding errors the smileFactor may be over 1
        if (_currentValue > 1) {
            _currentValue = 1;
        } else if (_currentValue < -1) {
            _currentValue = -1;
        }
    }

    public BoundedRangeModel getModel() {
        return _brModel;
    }

    public void setModel(BoundedRangeModel model) {
        _brModel.removeChangeListener(this);
        _brModel = model;
        _brModel.addChangeListener(this);
        calcSmileFactor();
        redraw();
    }

    /**
     * Calculates the color beetween _neutral and the specified color
     * 
     * @param destColor
     * @return the mixed color
     */
    private Color calcColor(Color destColor) {
        int rDiff = destColor.getRed() - _neutral.getRed();
        int gDiff = destColor.getGreen() - _neutral.getGreen();
        int bDiff = destColor.getBlue() - _neutral.getBlue();
        double factor = Math.abs(_currentValue);
        int r = (int) ((double) rDiff * factor);
        int g = (int) ((double) gDiff * factor);
        int b = (int) ((double) bDiff * factor);

        if (_currentColor != null) {
            _currentColor.dispose();
        }
        _currentColor = new Color(Display.getCurrent(), _neutral.getRed() + r, _neutral.getGreen() + g, _neutral
                .getBlue()
                - b);
        return _currentColor;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (_currentColor != null) {
            _currentColor.dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        calcSmileFactor();
        redraw();
    }

    /**
     * @return Returns the colorChange.
     */
    public boolean getColorChange() {
        return _colorChange;
    }

    /**
     * @param colorChange The colorChange to set.
     */
    public void setColorChange(boolean colorChange) {
        _colorChange = colorChange;
        redraw();
    }

    /**
     * @return Returns the eyeBrows.
     */
    public boolean getEyeBrows() {
        return _eyeBrows;
    }

    /**
     * @param eyeBrows The eyeBrows to set.
     */
    public void setEyeBrows(boolean eyeBrows) {
        _eyeBrows = eyeBrows;
        redraw();
    }

    /**
     * shortcut for getModel().setValue()
     * 
     * @see javax.swing.BoundedRangeModel
     * @param value
     */
    public void setValue(int value) {
        _brModel.setValue(value);
    }

    /**
     * @return Returns the negative.
     */
    public Color getNegative() {
        return _negative;
    }

    /**
     * @param negative The negative to set.
     */
    public void setNegative(Color negative) {
        _negative = negative;
        redraw();
    }

    /**
     * @return Returns the neutral.
     */
    public Color getNeutral() {
        return _neutral;
    }

    /**
     * Set the neutral color. If color change is not actiovated zhis color will be used to paint the smiley. default is
     * classic YELLOW
     * 
     * @param neutral The neutral to set.
     */
    public void setNeutral(Color neutral) {
        _neutral = neutral;
        redraw();
    }

    /**
     * @return Returns the positive.
     */
    public Color getPositive() {
        return _positive;
    }

    /**
     * @param positive The positive to set.
     */
    public void setPositive(Color positive) {
        _positive = positive;
        redraw();
    }

}
