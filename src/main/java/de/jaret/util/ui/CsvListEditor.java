/*
 *  File: CsvListEditor.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.JaretStyledText;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.jaret.util.misc.MiscUtil;
import de.jaret.util.ui.model.IMutableContentProvider;

/**
 * @author Peter Kliem
 * @version $Id: CsvListEditor.java 242 2007-02-11 21:05:07Z olk $
 */
public class CsvListEditor extends Composite implements FocusListener, VerifyListener, KeyListener, ModifyListener {

    private ILabelProvider _srcLabelProvider;
    private ILabelProvider _destLabelProvider;
    private IMutableContentProvider _destContentProvider;
    private IStructuredContentProvider _srcContentProvider;

    private String _separator = ",";
    private Map _labelMap = new HashMap();
    private List _labelList = new ArrayList();
    private boolean _caseSensitive = false;
    private boolean _contentParsable = true;

    JaretStyledText _text;

    public CsvListEditor(Composite parent, int style) {
        super(parent, style);

        _srcLabelProvider = new DefaultLabelProvider();
        _destLabelProvider = _srcLabelProvider;

        createControls();
    }

    private void updateText() {
        StringBuffer buf = new StringBuffer();
        Object[] dest = _destContentProvider.getElements(null);
        for (int i = 0; i < dest.length; i++) {
            String label = _destLabelProvider.getText(dest[i]);
            buf.append(label);
            if (i < dest.length - 1) {
                buf.append(_separator);
            }
        }
        _text.setText(buf.toString());
        _contentParsable = true;
    }

    /**
     * parses the text and updates the destination List
     * 
     * @return
     */
    private boolean updateDest() {
        String input = _text.getText().trim();
        // if there is an ending separator -> remove
        if (input.endsWith(_separator)) {
            input = input.substring(0, input.length() - 1);
        }
        StringTokenizer tokenizer = new StringTokenizer(input, _separator, true);
        boolean success = true;
        List objects = new ArrayList();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!token.equals(_separator)) {
                token = token.trim();
                if (!_caseSensitive) {
                    token = token.toLowerCase();
                }
                Object o = _labelMap.get(token);
                if (o == null) {
                    success = false;
                    break;
                } else {
                    objects.add(o);
                }
            }
        }
        if (success) {
            // TODO this is rude way
            _destContentProvider.clear();
            for (int i = 0; i < objects.size(); i++) {
                _destContentProvider.addToDest(objects.get(i));
            }
        }
        _contentParsable = success;
        return success;
    }

    private void createControls() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        this.setLayout(gridLayout);

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        _text = new JaretStyledText(this, SWT.SINGLE | SWT.BORDER);
        _text.setLayoutData(gd);
        _text.addFocusListener(this);
        // _text.addKeyListener(this);
        _text.addModifyListener(this);
    }

    /**
     * check wether the labels of the elements for a unique key
     * 
     * @param cp
     * @param lp
     * @return
     */
    private boolean checkLabelUniqueness(IStructuredContentProvider cp, ILabelProvider lp) {
        boolean result = true;
        Map labels = new HashMap();
        Object[] elements = cp.getElements(null);
        for (int i = 0; i < elements.length; i++) {
            String label = lp.getText(elements[i]).trim();
            if (!_caseSensitive) {
                label = label.toLowerCase();
            }
            if (labels.get(label) != null) {
                result = false;
                break;
            } else {
                labels.put(label, "x");
            }
        }
        return result;
    }

    private void fillLabelMap() {
        Object[] elements = _srcContentProvider.getElements(null);
        for (int i = 0; i < elements.length; i++) {
            String label = _srcLabelProvider.getText(elements[i]).trim();
            if (!_caseSensitive) {
                label = label.toLowerCase();
            }
            _labelMap.put(label, elements[i]);
            _labelList.add(label);
        }
    }

    public void setSrcLabelProvider(ILabelProvider labelProvider) {
        _srcLabelProvider = labelProvider;
    }

    public void setDestLabelProvider(ILabelProvider labelProvider) {
        _destLabelProvider = labelProvider;
    }

    /**
     * @return Returns the destContentProvider.
     */
    public IMutableContentProvider getDestContentProvider() {
        return _destContentProvider;
    }

    /**
     * @param destContentProvider The destContentProvider to set.
     */
    public void setDestContentProvider(IMutableContentProvider destContentProvider) {
        _destContentProvider = destContentProvider;
        updateText();
    }

    public void updateX() {
        updateText();
        updateDest2(false);
    }

    /**
     * @return Returns the srcContentProvider.
     */
    public IContentProvider getSrcContentProvider() {
        return _srcContentProvider;
    }

    /**
     * @param srcContentProvider The srcContentProvider to set.
     */
    public void setSrcContentProvider(IStructuredContentProvider srcContentProvider) {
        _srcContentProvider = srcContentProvider;
        if (!checkLabelUniqueness(_srcContentProvider, _srcLabelProvider)) {
            throw new RuntimeException("SrcLabels must form a unique key");
        }
        fillLabelMap();
    }

    /**
     * @return Returns the destLabelProvider.
     */
    public ILabelProvider getDestLabelProvider() {
        return _destLabelProvider;
    }

    /**
     * @return Returns the srcLabelProvider.
     */
    public ILabelProvider getSrcLabelProvider() {
        return _srcLabelProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events.FocusEvent)
     */
    public void focusGained(FocusEvent arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
     */
    public void focusLost(FocusEvent arg0) {
        boolean success = updateDest();
        if (!success) {
            _text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        } else {
            _text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            _text.removeModifyListener(this);
            updateText();
            updateDest2(false);
            _text.addModifyListener(this);
        }
    }

    private void resetStyle() {
        _text.setStyleRanges(new StyleRange[] {});
    }

    private boolean updateDest2(boolean updateDest) {
        String input = MiscUtil.rightTrim(_text.getText());
        // if there is an ending separator -> remove
        if (input.endsWith(_separator)) {
            input = input.substring(0, input.length() - 1);
        }
        boolean success = true;
        char separator = _separator.charAt(0);
        List objects = new ArrayList();
        List styleRanges = new ArrayList();
        int pos = 0;

        while (pos < input.length()) {
            int beginToken = -1;
            int endToken = -1;

            // skip leading whitespaces
            while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
                pos++;
            }
            beginToken = pos;
            // search ending position
            while (pos < input.length() && input.charAt(pos) != separator) {
                pos++;
            }
            if (beginToken == endToken) {
                // no input
                break;
            }
            endToken = pos;
            String token = input.substring(beginToken, endToken);
            // System.out.println("TOKEN:"+token+"|");
            pos++; // skip separator

            // check token
            token = token.trim();
            if (!_caseSensitive) {
                token = token.toLowerCase();
            }
            Object o = _labelMap.get(token);
            if (o == null) {
                success = false;
            } else {
                objects.add(o);
                // System.out.println("TOKENSUCCESS:"+token+"|"+beginToken+","+endToken);
                styleRanges.add(new StyleRange(beginToken, endToken - beginToken, _text.getForeground(), _text
                        .getBackground(), SWT.BOLD));
            }
        }
        /*
         * if (success) { // TODO this is rude way _destContentProvider.clear(); for(int i=0;i<objects.size();i++){
         * _destContentProvider.addToDest(objects.get(i)); } }
         */_contentParsable = success;
        StyleRange srs[] = new StyleRange[styleRanges.size()];
        for (int i = 0; i < styleRanges.size(); i++) {
            StyleRange sr = (StyleRange) styleRanges.get(i);
            srs[i] = sr;
        }
        _text.setStyleRanges(srs);
        return success;
    }

    /**
     * @return Returns the caseSensitive.
     */
    public boolean isCaseSensitive() {
        return _caseSensitive;
    }

    /**
     * @param caseSensitive The caseSensitive to set.
     */
    public void setCaseSensitive(boolean caseSensitive) {
        _caseSensitive = caseSensitive;
    }

    /**
     * @return Returns the separator.
     */
    public String getSeparator() {
        return _separator;
    }

    /**
     * @param separator The separator to set.
     */
    public void setSeparator(String separator) {
        _separator = separator;
    }

    /**
     * @return Returns the contentParsable.
     */
    public boolean isContentParsable() {
        return _contentParsable;
    }

    private String possibleLabel(String prefix) {
        if (!_caseSensitive) {
            prefix = prefix.toLowerCase();
        }
        if (prefix.length() == 0) {
            return "";
        }
        // exact match?
        Object o = _labelMap.get(prefix);
        if (o != null) {
            return prefix;
        }
        for (int i = 0; i < _labelList.size(); i++) {
            String label = (String) _labelList.get(i);
            if (label.startsWith(prefix)) {
                return label;
            }
        }
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.VerifyEvent)
     */
    public void verifyText(VerifyEvent ve) {
        /*
         * System.out.println("ve "+ve.text+" start:"+ve.start+" end:"+ve.end); // search for the prefix String text =
         * _text.getText(); String prefix = getPrefix(text, ve.start);
         */}

    /**
     * @param text
     * @return
     */
    private String getPrefix(String text, int pos) {
        if (text.length() < 1) {
            return "";
        }
        int end = pos;
        while (pos > 0 && !text.substring(pos - 1, pos).equals(_separator)) {
            pos--;
        }
        String prefix = text.substring(pos, end);
        return MiscUtil.leftTrim(prefix);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {
        // System.out.println("keytpressed "+e.toString());
        // if (e.character=='a') e.doit=false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
     */
    public void keyReleased(KeyEvent arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
     */
    private boolean ignoreNext = false;

    public void modifyText(ModifyEvent arg0) {
        updateDest2(false);
        // System.out.println("Pos "+_text.getCaretOffset()+" lpos "+lastPos+" ignoreNext "+ignoreNext);
        int pos = _text.getCaretOffset();
        if (ignoreNext) {
            ignoreNext = false;
            return;
        }
        System.out.println("modify " + _text.getText());
        String prefix = getPrefix(_text.getText(), pos);
        // System.out.println("Prefix:"+prefix);
        String label = possibleLabel(prefix);
        // System.out.println("possible:"+label);
        if (label.length() > 0) {
            String rest = label.substring(prefix.length());
            System.out.println("Rest:" + rest);
            _text.removeModifyListener(this);
            ignoreNext = true;
            _text.insert(rest);
            _text.setSelection(pos, pos + rest.length());
            _text.setCaretOffsetJaret(pos); // hacked version of setCaretOffset without clearing of the selection
            _text.addModifyListener(this);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setToolTipText(java.lang.String)
     */
    public void setToolTipText(String toolTip) {
        super.setToolTipText(toolTip);
        _text.setToolTipText(toolTip);
    }
}
