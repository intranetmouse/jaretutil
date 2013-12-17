/*
 *  File: ConsoleListener.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.ui.console;

/**
 * Listener for listening to entered lines in a <code>ConsoleControl</code>.
 * 
 * @author Peter Kliem
 */
public interface ConsoleListener {
    /**
     * Will be called whenever a line has been entered
     * 
     * @param consoleControl the calling ConsoleControl
     * @param line the entered line
     * @return a String or <code>null</code> acting as a result that will be displayed. If multiple listeners return
     * messages these will be concatenated.
     */
    public String lineEntered(ConsoleControl consoleControl, String line);
}
