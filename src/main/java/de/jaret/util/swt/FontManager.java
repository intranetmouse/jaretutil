/*
 *  File: FontManager.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * Simple class to collect allocated fonts and to relese them.
 * 
 * @author Peter Kliem
 * @version $Id: FontManager.java 242 2007-02-11 21:05:07Z olk $
 */
public class FontManager {
    protected static Map<Device, FontManager> _instances = new HashMap<Device, FontManager>();

    protected Map<FontData, Font> _fontTable = new HashMap<FontData, Font>(10);
    protected Device _device;

    public FontManager(Device device) {
        if (device != null) {
            _device = device;
        } else {
            _device = Display.getCurrent();
        }
    }

    public static FontManager getFontManager(Device device) {
        FontManager cm = _instances.get(device);
        if (cm == null) {
            cm = new FontManager(device);
            _instances.put(device, cm);
        }
        return cm;
    }

    public static void disposeAll() {
        for (Device device : _instances.keySet()) {
            FontManager cm = getFontManager(device);
            cm.dispose();
        }
        _instances.clear();
    }

    public void dispose() {
        for (Font Font : _fontTable.values()) {
            Font.dispose();
        }
    }

    public Font getFont(FontData FontData) {
        Font Font = _fontTable.get(FontData);
        if (Font == null) {
            Font = new Font(_device, FontData);
            _fontTable.put(FontData, Font);
        }
        return Font;
    }
}
