/*
 *  File: DefaultLabelProvider.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * Simple "toString"-LabelProvider.
 * 
 * @author Peter Kliem
 * @version $Id: DefaultLabelProvider.java 242 2007-02-11 21:05:07Z olk $
 */
public class DefaultLabelProvider implements ILabelProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
     */
    public Image getImage(Object arg0) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
     */
    public String getText(Object o) {
        return o.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void addListener(ILabelProviderListener arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    public void dispose() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
     */
    public boolean isLabelProperty(Object arg0, String arg1) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void removeListener(ILabelProviderListener arg0) {
    }

}