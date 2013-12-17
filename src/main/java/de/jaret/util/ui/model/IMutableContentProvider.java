/*
 *  File: IMutableContentProvider.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui.model;

import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * Extension of the org.eclipse.jface.viewers.IStructuredContentProvider interface allowing modification of the provided
 * content.
 * 
 * @author Peter Kliem
 * @version $Id: IMutableContentProvider.java 242 2007-02-11 21:05:07Z olk $
 */
public interface IMutableContentProvider extends IStructuredContentProvider {
    /**
     * Add an object to the content supplied by this ContentProvider
     * 
     * @param o object to be added
     */
    public void addToDest(Object o);

    /**
     * Remove an object from the content of the ContentProvider.
     * 
     * @param o the object to be removed from the Content
     */
    public void remFromDest(Object o);

    /**
     * Return true when the object is in the content.
     * 
     * @param o Object to check
     * @return boolean true if o is part of the content of the ContentProvider
     */
    public boolean contains(Object o);

    /**
     * removes all elements from the ContentProvider
     */
    public void clear();
}
