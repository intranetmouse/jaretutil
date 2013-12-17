/*
 *  File: DoubleField.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * Helper wrapping a Text to allow only editing of double values.
 * 
 * @author Peter Kliem
 * @version $Id: DoubleField.java 242 2007-02-11 21:05:07Z olk $
 */
public class DoubleField implements VerifyListener, KeyListener {
    protected int _digits = 1;
    protected double _min = -Double.MAX_VALUE;
    protected double _max = Double.MAX_VALUE;
    protected Text _text;
    protected NumberFormat _numberFormat;
    protected double _increment = 1.0;

    public DoubleField(int digits, double min, double max) {
        _digits = digits;
        _min = min;
        _max = max;
        updateNumberFormat();
    }

    public DoubleField() {
        this(1, -Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public double getValue() throws ParseException {
        double value = ((Number) _numberFormat.parse(_text.getText())).doubleValue();
        return value;
    }

    public void setValue(double value) {
        if (_text != null) {
            _text.setText(_numberFormat.format(value));
        }
    }

    private void updateNumberFormat() {
        if (_numberFormat == null) {
            _numberFormat = DecimalFormat.getInstance();
        }
        _numberFormat.setMaximumFractionDigits(_digits);
        _numberFormat.setGroupingUsed(false);
    }

    /**
     * @return the digits
     */
    public int getDigits() {
        return _digits;
    }

    /**
     * @param digits the digits to set
     */
    public void setDigits(int digits) {
        _digits = digits;
        updateNumberFormat();
    }

    /**
     * @return the max
     */
    public double getMax() {
        return _max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(double max) {
        _max = max;
    }

    /**
     * @return the min
     */
    public double getMin() {
        return _min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(double min) {
        _min = min;
    }

    /**
     * @return the text
     */
    public Text getText() {
        return _text;
    }

    /**
     * @param text the text to set
     */
    public void setText(Text text) {
        if (_text != null) {
            _text.removeVerifyListener(this);
            _text.removeKeyListener(this);
        }
        _text = text;
        if (_text != null) {
            _text.addVerifyListener(this);
            _text.addKeyListener(this);
        }
    }

    public void verifyText(VerifyEvent e) {
        if (e.text.length() > 1) {
            try {
                _numberFormat.parse(e.text);
                return;
            } catch (Exception ex) {
                // ignore, text will no t be inserted
            }
        }
        // System.out.println("characteer "+(int)e.character);
        if (Character.isDigit(e.character) || e.character < 32 || e.character == 127) {
            return;
        }
        if (e.character == '.' || e.character == ',') {
            if (_text.getText().indexOf('.') == -1 && _text.getText().indexOf(',') == -1) {
                return;
            }
        }
        if (e.character == '-') {
            if (_text.getText().indexOf('-') == -1 && _text.getCaretPosition() == 0) {
                return;
            }
        }
        e.doit = false;
    }

    /**
     * @return the increment
     */
    public double getIncrement() {
        return _increment;
    }

    /**
     * @param increment the increment to set
     */
    public void setIncrement(double increment) {
        _increment = increment;
    }

    public void keyPressed(KeyEvent e) {
        if (e.keyCode == SWT.ARROW_DOWN) {
            try {
                double value = getValue();
                value -= _increment;
                if (value < _min) {
                    value = _min;
                }
                setValue(value);
            } catch (ParseException ex) {
                // ignore
            }
            e.doit = false;
        } else if (e.keyCode == SWT.ARROW_UP) {
            try {
                double value = getValue();
                value += _increment;
                if (value > _max) {
                    value = _max;
                }
                setValue(value);
            } catch (ParseException ex) {
                // ignore
            }
            e.doit = false;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

}
