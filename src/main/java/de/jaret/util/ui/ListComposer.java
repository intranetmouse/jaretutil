/*
 *  File: ListComposer.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui;

import java.util.Iterator;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import de.jaret.util.ui.model.IMutableContentProvider;

/**
 * The ListComposer is a widget that is used for copying parts of another list in a new list. The visualization are the
 * (classical) two lists with buttons for moving the entries. source is supplied as an IContentProvider, destination is
 * adressed as an IMutableContentProvider(see package model).
 * 
 * @author Peter Kliem
 * @version $Id: ListComposer.java 242 2007-02-11 21:05:07Z olk $
 */
public class ListComposer extends Composite {

    protected String _srcLabelText = "Source";
    protected String _destLabelText = "Destination";
    protected String _headingLabelText = "Heading";

    protected IMutableContentProvider _destMutableCP;
    protected IContentProvider _srcContentProvider;

    protected ILabelProvider _srcLabelProvider;
    protected ILabelProvider _destLabelProvider;

    private ListViewer _srcViewer;
    private ListViewer _destViewer;
    private Button _remButton;
    private Button _addButton;
    private Button _addAllButton;
    private Button _remAllButton;
    private static final int VIEWERWIDTHHINT = 50;
    private Label _headingLabelWidget;
    private Label _srcLabelWidget;
    private Label _destLabelWidget;

    public ListComposer(Composite parent, int style) {
        super(parent, style);
        createControls();
        DefaultLabelProvider dlp = new DefaultLabelProvider();
        _srcViewer.setLabelProvider(dlp);
        _destViewer.setLabelProvider(dlp);

        // setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
    }

    @Override
    public void setBackground(Color bgColor) {
        super.setBackground(bgColor);
        Control children[] = getChildren();
        for (int i = 0; i < children.length; i++) {
            Control control = children[i];
            control.setBackground(bgColor);
        }
    }

    private void createControls() {
        int width = 30;

        // create the listener for the bunch of buttons
        SelectionListener buttonListener = new ButtonListener();
        // create Doubleclicklistener
        IDoubleClickListener doubleClickListener = new DoubleClickListener();

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        this.setLayout(gridLayout);

        GridData gd = new GridData();
        gd.horizontalSpan = 3;
        _headingLabelWidget = new Label(this, SWT.LEFT);
        _headingLabelWidget.setLayoutData(gd);
        _headingLabelWidget.setText(_headingLabelText);

        gd = new GridData();
        gd.horizontalSpan = 2;
        _srcLabelWidget = new Label(this, SWT.LEFT);
        _srcLabelWidget.setLayoutData(gd);
        _srcLabelWidget.setText(_srcLabelText);

        gd = new GridData();
        _destLabelWidget = new Label(this, SWT.LEFT);
        _destLabelWidget.setLayoutData(gd);
        _destLabelWidget.setText(_destLabelText);

        gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = VIEWERWIDTHHINT;
        _srcViewer = new ListViewer(this);
        _srcViewer.getList().setLayoutData(gd);
        _srcViewer.addDoubleClickListener(doubleClickListener);

        Composite buttons = new Composite(this, SWT.NULL);
        buttons.setLayout(new RowLayout(SWT.VERTICAL));

        _remButton = new Button(buttons, SWT.PUSH);
        _remButton.setText("<");
        Point size = _remButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        RowData rd = new RowData(width, size.y);
        _remButton.setLayoutData(rd);
        _remButton.addSelectionListener(buttonListener);

        _addButton = new Button(buttons, SWT.PUSH);
        _addButton.setText(">");
        size = _addButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        rd = new RowData(width, size.y);
        _addButton.setLayoutData(rd);
        _addButton.addSelectionListener(buttonListener);

        _addAllButton = new Button(buttons, SWT.PUSH);
        _addAllButton.setText(">>");
        size = _addAllButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        rd = new RowData(width, size.y);
        _addAllButton.setLayoutData(rd);
        _addAllButton.addSelectionListener(buttonListener);

        _remAllButton = new Button(buttons, SWT.PUSH);
        _remAllButton.setText("<<");
        size = _remAllButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        rd = new RowData(width, size.y);
        _remAllButton.setLayoutData(rd);
        _remAllButton.addSelectionListener(buttonListener);

        gd = new GridData(GridData.FILL_BOTH);
        _destViewer = new ListViewer(this);
        _destViewer.getList().setLayoutData(gd);
        gd.widthHint = VIEWERWIDTHHINT;
        _destViewer.addDoubleClickListener(doubleClickListener);
    }

    public void setSrcLabelText(String srcLabelText) {
        _srcLabelText = srcLabelText;
        _srcLabelWidget.setText(srcLabelText);
    }

    public void setDestLabelText(String destLabeltext) {
        _destLabelText = destLabeltext;
        _destLabelWidget.setText(destLabeltext);
    }

    public void setHeadingLabelText(String heading) {
        _headingLabelText = heading;
        _headingLabelWidget.setText(heading);
    }

    public void setSrcContentProvider(IContentProvider contentProvider) {
        _srcContentProvider = contentProvider;
        _srcViewer.setContentProvider(contentProvider);
        _srcViewer.addFilter(new Filter());
    }

    public IContentProvider getSrcContentProvider() {
        return _srcContentProvider;
    }

    public void setDestContentProvider(IMutableContentProvider contentProvider) {
        _destMutableCP = contentProvider;
        _destViewer.setContentProvider(contentProvider);
    }

    public IMutableContentProvider getDestContentProvider() {
        return _destMutableCP;
    }

    /**
     * Sets the input base object for the content providers.
     * 
     * @param input the base object for the content providers
     */
    public void setInput(Object input) {
        // first set the input on the destination
        // this makes the filter for the source effective
        _destViewer.setInput(input);
        _srcViewer.setInput(input);
    }

    public void setSrcLabelProvider(ILabelProvider labelProvider) {
        _srcViewer.setLabelProvider(labelProvider);
    }

    public void setDestLabelProvider(ILabelProvider labelProvider) {
        _destViewer.setLabelProvider(labelProvider);
    }

    public ListViewer getSrcListViewer() {
        return _srcViewer;
    }

    public ListViewer getDestListViewer() {
        return _destViewer;
    }

    private void addAll() {
        Object[] all = ((IStructuredContentProvider) _srcViewer.getContentProvider()).getElements(null);
        for (int i = 0; i < all.length; i++) {
            _destMutableCP.addToDest(all[i]);
        }
        _destViewer.refresh();
        _srcViewer.refresh();
    }

    private void remAll() {
        Object[] all = _destMutableCP.getElements(null);
        for (int i = 0; i < all.length; i++) {
            _destMutableCP.remFromDest(all[i]);
        }
        _destViewer.refresh();
        _srcViewer.refresh();
    }

    private void rem() {
        IStructuredSelection selection = (IStructuredSelection) _destViewer.getSelection();
        if (!selection.isEmpty()) {
            Iterator it = selection.iterator();
            while (it.hasNext()) {
                Object item = it.next();
                _destMutableCP.remFromDest(item);
            }
            _destViewer.refresh();
            _srcViewer.refresh();
        }
    }

    private void add() {
        IStructuredSelection selection = (IStructuredSelection) _srcViewer.getSelection();
        if (!selection.isEmpty()) {
            Iterator it = selection.iterator();
            while (it.hasNext()) {
                Object item = it.next();
                _destMutableCP.addToDest(item);
            }
            _destViewer.refresh();
            _srcViewer.refresh();
        }
    }

    private class ButtonListener implements SelectionListener {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
         */
        public void widgetSelected(SelectionEvent selEvent) {
            if (selEvent.getSource() == _remAllButton) {
                remAll();
            } else if (selEvent.getSource() == _addAllButton) {
                addAll();
            } else if (selEvent.getSource() == _remButton) {
                rem();
            } else if (selEvent.getSource() == _addButton) {
                add();
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
         */
        public void widgetDefaultSelected(SelectionEvent arg0) {
        }

    }

    class DoubleClickListener implements IDoubleClickListener {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
         */
        public void doubleClick(DoubleClickEvent dcEvent) {
            Object source = dcEvent.getSource();
            if (source == _srcViewer) {
                add();
            } else if (source == _destViewer) {
                rem();
            }
        }

    }

    /**
     * Filter for filtering src removing items already in dest
     */
    class Filter extends ViewerFilter {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
         * java.lang.Object)
         */
        public boolean select(Viewer viewer, Object parent, Object item) {
            if (_destMutableCP != null) {
                return !_destMutableCP.contains(item);
            } else {
                return true;
            }
        }
    }

}
