/*
 *  File: ConsoleControl.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui.console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Simple widget that acts like a shell window. Entered lines are reported to attched <code>ConsoleListeners</code>.
 * A simple history allows reusing previously entered lines. Technically the widget wraps a <code>StyledText</code>.
 * 
 * @author Peter Kliem
 * @version $Id: ConsoleControl.java 242 2007-02-11 21:05:07Z olk $ ddd
 */
public class ConsoleControl extends Composite implements VerifyKeyListener {
    private List _enteredLines = new ArrayList();
    private int _currentEnteredLineIdx = -1;
    private List _listeners;
    private String _prompt;
    private int _promptEndIdx;
    private int _promptEndOffset;
    private StyledText _styledText;

    /**
     * Contructor
     * 
     * @param parent parent composite
     * @param style style
     * @param prompt prompt to be displayed
     * @param message startup message (<code>null</code> for no message)
     */
    public ConsoleControl(Composite parent, int style, String prompt, String message) {
        super(parent, style);
        _prompt = prompt;
        createControls();
        if (message != null && message.length() > 0) {
            _styledText.append(message);
            _styledText.setCaretOffset(_styledText.getCharCount());
            writePrompt(true);
        } else {
            writePrompt(false);
        }
    }

    /**
     * Create the controls
     */
    private void createControls() {
        setLayout(new FillLayout());
        _styledText = new StyledText(this, SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
        _styledText.addVerifyKeyListener(this);
    }

    /**
     * Write the prompt and store the end index of the prompt.
     * 
     * @param newLine if <code>true</code> prepend a newline
     */
    private void writePrompt(boolean newLine) {
        if (newLine) {
            _styledText.append(_styledText.getLineDelimiter());
        }
        _styledText.append(_prompt + " ");
        _promptEndIdx = _prompt.length() + 1;
        _promptEndOffset = _styledText.getCaretOffset() + _promptEndIdx + _styledText.getLineDelimiter().length();
        _styledText.setCaretOffset(_promptEndOffset);
        _styledText.showSelection();
    }

    /**
     * Retrieve the caret offset in the line it is in.
     * 
     * @return caret ofsfet in the current line
     */
    private int offsetInLine() {
        int caretOffset = _styledText.getCaretOffset();
        int lineidx = _styledText.getLineAtOffset(caretOffset);
        int lineoffset = _styledText.getOffsetAtLine(lineidx);
        int posInLine = caretOffset - lineoffset;
        return posInLine;
    }

    /**
     * Get the last line without the prompt.
     * 
     * @return last line without prompt or <code>null</code> if the line is empty.
     */
    private String getEnteredLine() {
        if (_promptEndOffset <= _styledText.getCharCount() - 1) {
            return _styledText.getText(_promptEndOffset, _styledText.getCharCount() - 1);
        } else {
            return null;
        }
    }

    /**
     * Set the prompt to be used.
     * 
     * @param prompt the new prompt
     */
    public void setPrompt(String prompt) {
        _prompt = prompt;
    }

    /**
     * Get the prompt
     * 
     * @return currently set prompt
     */
    public String getPrompt() {
        return _prompt;
    }

    /**
     * Add a <code>ConsoleListener</code> to be informed about entered lines.
     * 
     * @param listener listener to be added
     */
    public void addConsoleListener(ConsoleListener listener) {
        if (_listeners == null) {
            _listeners = new ArrayList();
        }
        _listeners.add(listener);
    }

    /**
     * Remove a <code>ConsoleListener</code>.
     * 
     * @param listener listener to be removed
     */
    public void remConsoleListener(ConsoleListener listener) {
        if (_listeners != null) {
            _listeners.remove(listener);
        }
    }

    /**
     * Inform all registered listeners about an entered line. The resulting Strings are concatenated to form the output
     * of the listeners.
     * 
     * @param line line entered
     * @return concatenated results from the listeners or <code>null/code> if no listener did respond with a String
     */
    protected String fireLineEntered(String line) {
        if (_listeners != null) {
            StringBuffer result = new StringBuffer();
            Iterator it = _listeners.iterator();
            while (it.hasNext()) {
                ConsoleListener listener = (ConsoleListener) it.next();
                String r = listener.lineEntered(this, line);
                if (r != null) {
                    result.append(r);
                }
            }
            return result.toString();
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.custom.VerifyKeyListener#verifyKey(org.eclipse.swt.events.VerifyEvent)
     */
    public void verifyKey(VerifyEvent verifyEvent) {
        switch (verifyEvent.keyCode) {
        case SWT.ARROW_LEFT:
        case SWT.BS:
            System.out.println("left " + _promptEndIdx + " " + offsetInLine());
            if (offsetInLine() <= _promptEndIdx) {
                System.out.println("ignore");
                verifyEvent.doit = false;
            }
            break;
        case SWT.ARROW_UP:
            int idx = _currentEnteredLineIdx - 1;
            if (idx >= 0) {
                replaceEnteredLineWith((String) _enteredLines.get(idx));
                _currentEnteredLineIdx = idx;
            }
            verifyEvent.doit = false;
            break;
        case SWT.ARROW_DOWN:
            int idx2 = _currentEnteredLineIdx + 1;
            if (idx2 <= _enteredLines.size() - 1) {
                replaceEnteredLineWith((String) _enteredLines.get(idx2));
                _currentEnteredLineIdx = idx2;
            }
            verifyEvent.doit = false;
            break;
        case SWT.CR:
            String line = getEnteredLine();
            _styledText.setCaretOffset(_styledText.getCharCount());
            if (line != null) {
                _enteredLines.add(line);
                _currentEnteredLineIdx = _enteredLines.size();
                String result = fireLineEntered(line);
                if (result != null && result.length() > 0) {
                    _styledText.append(_styledText.getLineDelimiter());
                    _styledText.append(result);
                    _styledText.setCaretOffset(_styledText.getCharCount());
                }
            } else {
                // entered line was empty
            }
            writePrompt(true);
            verifyEvent.doit = false;
            break;

        default:
            break;
        }
    }

    /**
     * Append the given text to the console output
     * 
     * @param text text to be appended
     */
    public void output(String text) {
        _styledText.append(text);
        _styledText.setCaretOffset(_styledText.getCaretOffset() + text.length());
        _styledText.showSelection();
    }

    /**
     * Get a <code>PrintStream</code> that prints to the console.
     * 
     * @return a PrintStream printing to the console
     */
    public PrintStream getPrintStream() {
        return new PrintStream(new ConsoleOutStream(this));
    }

    /**
     * Replace the currently entered line with the given string
     * 
     * @param string text to be set in the current line
     */
    private void replaceEnteredLineWith(String string) {
        _styledText.replaceTextRange(_promptEndOffset, _styledText.getCharCount() - _promptEndOffset, string);
        _styledText.setCaretOffset(_styledText.getCharCount());
    }

    /**
     * An implemenation of an <code>OutputStream</code> writing to the console.
     * 
     * @author Peter Kliem
     */
    public class ConsoleOutStream extends OutputStream {
        ConsoleControl _consoleControl;

        /**
         * @param control ConsoleControl to write to
         */
        public ConsoleOutStream(ConsoleControl control) {
            super();
            _consoleControl = control;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.io.OutputStream#write(int)
         */
        public void write(int b) throws IOException {
            _consoleControl.output("" + (char) b);
        }
    }

}
