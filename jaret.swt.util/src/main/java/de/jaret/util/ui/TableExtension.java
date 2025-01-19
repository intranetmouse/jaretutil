/*
 *  File: TableExtension.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TableColumn;

/**
 * TableExtension is a buddy for a TableViewer allowing sorting on click on the column header and a width optimization
 * of the column on doubleclick on the column header.
 * 
 * The double click is handled manually by a selectionListener since the addListener(MouseDoubleClick ) on the cols does
 * not work.
 * 
 * @author Peter Kliem
 * @version $Id: TableExtension.java 242 2007-02-11 21:05:07Z olk $
 */
public class TableExtension implements SelectionListener, DisposeListener {// , Listener {

    boolean _doWidthControl = true;
    long _lastClick = -1;
    int _lastClickColIdx = -1;
    int _sortingcol = -1;
    int _invert = 1;
    TableViewer _tviewer;
    TableExtensionViewerSorter _sorter;

    private ImageRegistry _imageRegistry;

    /**
     * Constructor
     * 
     * @param tableViewer table viewer that is extended.
     * @param doWidthOptimization true for enabling of widht optimization (double click)
     */
    public TableExtension(TableViewer tableViewer, boolean doWidthOptimization) {
        _tviewer = tableViewer;
        TableColumn cols[] = tableViewer.getTable().getColumns();
        for (int i = 0; i < cols.length; i++) {
            cols[i].addSelectionListener(this);
            // cols[i].addListener(SWT.MouseDoubleClick, this);
        }
        _sorter = new TableExtensionViewerSorter();
        setDoWidthOptimization(doWidthOptimization);
        _tviewer.getTable().addDisposeListener(this);
    }

    /**
     * Constructor. Width optimization defaults to true.
     * 
     * @param tableViewer tableViewer table viewer that is extended.
     */
    public TableExtension(TableViewer tableViewer) {
        this(tableViewer, true);
    }

    /**
     * If set to true the extension will react on double click doing a width change of the column.
     * 
     * @param doWidthControl true for enabling widht changes
     */
    public void setDoWidthOptimization(boolean doWidthControl) {
        _doWidthControl = doWidthControl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent event) {
        TableColumn tc = (TableColumn) event.getSource();
        int idx = _tviewer.getTable().indexOf(tc);

        if (_doWidthControl) {
            if (_lastClickColIdx != idx) {
                _lastClickColIdx = idx;
            } else {
                if (System.currentTimeMillis() - _lastClick < 800) {
                    doWidthOpt(idx, tc);
                }
            }
            _lastClick = System.currentTimeMillis();
        }

        if (_sortingcol != -1) {
            _tviewer.getTable().getColumn(_sortingcol).setImage(null);
        }
        if (_sortingcol == idx && _invert == -1) {
            _sortingcol = -1;
            _invert = 1;
            _tviewer.setSorter(null);
        } else {
            if (_sortingcol == idx) {
                _invert = _invert * -1;
            } else {
                _invert = 1;
            }
            _sortingcol = idx;
            if (_invert == 1) {
                tc.setImage(getImageRegistry().get("down"));
            } else {
                tc.setImage(getImageRegistry().get("up"));
            }
            _tviewer.getTable().setRedraw(false);
            _tviewer.setComparator(null);
            _tviewer.setComparator(_sorter);
            _tviewer.getTable().setRedraw(true);
        }
    }

    /**
     * optimize the width of th column to the max wdth of the cuurently available using the gc of the table (maybe this
     * should be changed to computeSize of the tabeitem)
     * 
     * @param tc
     */
    private void doWidthOpt(int idx, TableColumn tc) {
        IStructuredContentProvider contentProvider = (IStructuredContentProvider) _tviewer.getContentProvider();
        ITableLabelProvider lprovider = (ITableLabelProvider) _tviewer.getLabelProvider();
        GC gc = new GC(_tviewer.getTable());

        Object items[] = contentProvider.getElements(_tviewer.getInput());
        int max = 0;

        for (int i = 0; i < items.length; i++) {
            String str = lprovider.getColumnText(items[i], idx);
            Point p = gc.textExtent(str);
            if (p.x > max) {
                max = p.x;
            }
        }
        tc.setWidth(max + 20);
        gc.dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent arg0) {
    }

    private ImageRegistry getImageRegistry() {
        if (_imageRegistry == null) {
            _imageRegistry = new ImageRegistry();
            ImageDescriptor imgDesc = new ResourceImageDescriptor("/de/jaret/util/rsc/smallarrow_up.gif");
            _imageRegistry.put("up", imgDesc);
            imgDesc = new ResourceImageDescriptor("/de/jaret/util/rsc/smallarrow_down.gif");
            _imageRegistry.put("down", imgDesc);
        }
        return _imageRegistry;
    }

    private class TableExtensionViewerSorter extends ViewerComparator {
        public int compare(Viewer viewer, Object e1, Object e2) {
            ITableLabelProvider lprovider = (ITableLabelProvider) _tviewer.getLabelProvider();
            String str1 = lprovider.getColumnText(e1, _sortingcol);
            String str2 = lprovider.getColumnText(e2, _sortingcol);
            return getComparator().compare(str1, str2) * _invert;
        }
    }

    /**
     * On dispose of the table viewer dispose imgae registry
     * 
     * @param arg0
     */
    public void widgetDisposed(DisposeEvent arg0) {
        if (_imageRegistry != null) {
            _imageRegistry.dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    /*
     * public void handleEvent(Event arg0) { System.out.println("mouse double click " + arg0.button); }
     */
}
