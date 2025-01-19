/*
 *  File: ResourceImageDescriptor.java 
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

/**
 * @author Peter Kliem
 * @version $Id: ResourceImageDescriptor.java 297 2007-03-12 21:38:00Z olk $
 */
public class ResourceImageDescriptor extends ImageDescriptor {
    String _rscString;
    Class<?> _loadingClass;

    /**
     * 
     */
    public ResourceImageDescriptor(String rscString, Class<?> loadingClass) {
        this._rscString = rscString;
        _loadingClass = loadingClass;
    }

    public ResourceImageDescriptor(String rscString) {
        this(rscString, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.resource.ImageDescriptor#getImageData()
     */
    public ImageData getImageData() {
        Class<?> clazz = _loadingClass != null ? _loadingClass : this.getClass();
        Image img = new Image(Display.getCurrent(), clazz.getResourceAsStream(_rscString));
        return img.getImageData();
    }
}
