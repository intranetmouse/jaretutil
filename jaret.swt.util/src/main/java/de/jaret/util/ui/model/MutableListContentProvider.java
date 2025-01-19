/*
 *  File: MutableListContentProvider.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui.model;

import java.util.List;

import org.eclipse.jface.viewers.Viewer;

/**
 * An implementation of the IMutableContentProvider interface working on a java.util.List.
 * 
 * @author Peter Kliem
 * @version $Id: MutableListContentProvider.java 242 2007-02-11 21:05:07Z olk $
 */
public class MutableListContentProvider implements IMutableContentProvider {
    private List _content;

    /**
     * Constructor
     * 
     * @param content List that holds the content and that is modified.
     */
    public MutableListContentProvider(List content) {
        _content = content;
    }

    public Object[] getElements(Object element) {
        return _content.toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.jaret.util.ui.model.IMutableContentProvider#addToDest(java.lang.Object)
     */
    public void addToDest(Object o) {
        if (!_content.contains(o)) {
            _content.add(o);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.jaret.util.ui.model.IMutableContentProvider#remFromDest(java.lang.Object)
     */
    public void remFromDest(Object o) {
        _content.remove(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.jaret.util.ui.model.IMutableContentProvider#clear()
     */
    public void clear() {
        _content.clear();
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
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.jaret.util.ui.model.IMutableContentProvider#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        return _content.contains(o);
    }

}
