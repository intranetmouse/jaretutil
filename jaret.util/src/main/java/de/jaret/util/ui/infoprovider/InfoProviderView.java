/*
 *  File: InfoProviderView.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui.infoprovider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import de.jaret.util.infoprovider.JaretInfoProvider;
import de.jaret.util.infoprovider.SysInfoEntry;
import de.jaret.util.ui.TableExtension;

/**
 * View for JaretSystemInfoProviders. Instantiates a shell to be displayed within.
 * 
 * @author Peter Kliem
 * @version $Id: InfoProviderView.java 242 2007-02-11 21:05:07Z olk $
 */
public class InfoProviderView implements ISelectionChangedListener, SelectionListener {
    static final int BUTTON_WIDTH = 50;
    Shell _shell;

    private TableViewer _tviewer;

    private Button _closeButton;
    private Button _refreshButton;

    private TreeViewer _treeViewer;
    private JaretInfoProvider _rootProvider;
    private int _accessLevel;

    /**
     * Create an InfoProviderView.
     * 
     * @param rootInfoProvider root
     * @param title title for the shell window
     * @param accessLevel entries that claim an access level > this parameter will not be displayed
     */
    public InfoProviderView(JaretInfoProvider rootInfoProvider, String title, int accessLevel) {
        _accessLevel = accessLevel;
        _shell = new Shell(Display.getCurrent());
        _rootProvider = rootInfoProvider;
        _shell.setText(title != null ? title : "Info");
        // Size of the shell
        _shell.setSize(700, 400);

        createControls();
        _shell.open();
    }

    public Shell getShell() {
        return _shell;
    }

    protected void createControls() {
        Composite composite = _shell;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        composite.setLayout(gridLayout);

        GridData gd = new GridData(GridData.FILL_BOTH);

        SashForm sash = new SashForm(composite, SWT.HORIZONTAL);
        sash.setLayoutData(gd);

        _treeViewer = new TreeViewer(sash);
        _treeViewer.setContentProvider(new IPTreeContentProvider(_rootProvider));
        _treeViewer.setInput("");
        _treeViewer.setLabelProvider(new IPTreeLabelProvider());
        _treeViewer.addSelectionChangedListener(this);

        _tviewer = new TableViewer(sash, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
        _tviewer.setContentProvider(new IPTableContentProvider());
        _tviewer.setLabelProvider(new IPTableLabelProvider());

        TableColumn column = new TableColumn(_tviewer.getTable(), SWT.LEFT);
        column.setText("Name");
        column.setWidth(100);
        column = new TableColumn(_tviewer.getTable(), SWT.LEFT);
        column.setText("Value");
        column.setWidth(100);

        _tviewer.getTable().setHeaderVisible(true);

        TableExtension te = new TableExtension(_tviewer);

        // buttons

        Composite buttonbar = new Composite(composite, SWT.NULL);
        gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
        buttonbar.setLayoutData(gd);

        buttonbar.setLayout(new RowLayout());

        _refreshButton = new Button(buttonbar, SWT.PUSH);
        _refreshButton.setText("Refresh");
        Point size = _refreshButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        RowData rd = new RowData(BUTTON_WIDTH, size.y);
        _refreshButton.setLayoutData(rd);
        _refreshButton.addSelectionListener(this);

        _closeButton = new Button(buttonbar, SWT.PUSH);
        _closeButton.setText("Close");
        size = _closeButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        rd = new RowData(BUTTON_WIDTH, size.y);
        _closeButton.setLayoutData(rd);
        _shell.setDefaultButton(_closeButton);
        _closeButton.addSelectionListener(this);

    }

    public class IPTreeContentProvider implements ITreeContentProvider {
        Map _parentMap = new HashMap();
        JaretInfoProvider _root;

        public IPTreeContentProvider(JaretInfoProvider root) {
            _root = root;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
         */
        public Object[] getChildren(Object arg0) {
            java.util.List l = ((JaretInfoProvider) arg0).getSubInfoProviders();
            if (l == null) {
                return new Object[0];
            }
            return l.toArray();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
         */
        public Object getParent(Object arg0) {
            Object parent = _parentMap.get(arg0);
            if (parent == null) {
                parent = findParent(_root, (JaretInfoProvider) arg0);
                _parentMap.put(arg0, parent);
            }
            System.out.println("PARENT " + parent);
            return parent;
        }

        /**
         * @param provider
         * @return Object
         */
        private JaretInfoProvider findParent(JaretInfoProvider base, JaretInfoProvider child) {
            java.util.List l = base.getSubInfoProviders();
            if (l == null) {
                return null;
            }

            for (int i = 0; i < l.size(); i++) {
                JaretInfoProvider ip = (JaretInfoProvider) l.get(i);
                if (ip.equals(child)) {
                    return base;
                } else {
                    JaretInfoProvider ipNext = findParent(ip, child);
                    if (ipNext != null) {
                        return ipNext;
                    }
                }
            }
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
         */
        public boolean hasChildren(Object arg0) {
            if (arg0 instanceof String)
                return true;
            return getChildren(arg0).length > 0 ? true : false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object arg0) {
            // return new Object[] {arg0};
            if (arg0 instanceof String)
                return new Object[] {_root};
            return getChildren(arg0);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
         * java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        }
    }

    class IPTreeLabelProvider extends LabelProvider {
        public String getText(Object o) {
            JaretInfoProvider ip = (JaretInfoProvider) o;
            String text = ip.getInfoProviderName();
            return text;
        }
    }

    class IPTableContentProvider implements IStructuredContentProvider {
        public Object[] getElements(Object element) {
            JaretInfoProvider ip = (JaretInfoProvider) element;
            Object[] kids = null;
            java.util.List<SysInfoEntry> l = ip.getSysInfoEntries();
            // filter for acces level
            java.util.List<SysInfoEntry> lout = new ArrayList<SysInfoEntry>();
            for (SysInfoEntry entry : l) {
                if (entry.access <= _accessLevel) {
                    lout.add(entry);
                }
            }
            kids = lout.toArray();
            return kids;
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object old_object, Object new_object) {
        }
    }

    class IPTableLabelProvider implements ITableLabelProvider {
        public String getColumnText(Object obj, int i) {
            String result;
            SysInfoEntry entry = (SysInfoEntry) obj;
            switch (i) {
            case 0:
                result = entry.name;
                break;
            case 1:
                result = entry.value;
                break;
            default:
                result = "error - unknow column";
                break;
            }
            return result;
        }

        public void addListener(ILabelProviderListener ilabelproviderlistener) {
        }

        public void dispose() {
        }

        public boolean isLabelProperty(Object obj, String s) {
            return false;
        }

        public void removeListener(ILabelProviderListener ilabelproviderlistener) {
        }

        public Image getColumnImage(Object arg0, int arg1) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        Object selected = selection.getFirstElement();
        _tviewer.setInput(selected);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent event) {
        if (event.getSource().equals(_closeButton)) {
            _shell.close();
        } else if (event.getSource().equals(_refreshButton)) {
            _treeViewer.refresh();
            _tviewer.refresh();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent event) {
    }

}
