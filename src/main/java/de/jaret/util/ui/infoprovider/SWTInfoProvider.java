/*
 *  File: SWTInfoProvider.java 
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
import java.util.List;

import org.eclipse.swt.SWT;

import de.jaret.util.infoprovider.JaretInfoProvider;
import de.jaret.util.infoprovider.SysInfoEntry;

/**
 * Info provider for swt
 * 
 * @author Peter Kliem
 * @version $Id: SWTInfoProvider.java 242 2007-02-11 21:05:07Z olk $
 */
public class SWTInfoProvider implements JaretInfoProvider {
    /*
     * (non-Javadoc)
     * 
     * @see de.jaret.app.swt.InfoProvider.JaretSystemInfoProvider#getInfoProviderName()
     */
    public String getInfoProviderName() {
        return "SWT";
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.jaret.app.swt.InfoProvider.JaretSystemInfoProvider#getAccess()
     */
    public int getAccess() {
        return SysInfoEntry.ACCESS_PUBLIC;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.jaret.app.swt.InfoProvider.JaretSystemInfoProvider#getSysInfoEntries()
     */
    public List<SysInfoEntry> getSysInfoEntries() {
        List<SysInfoEntry> entries = new ArrayList<SysInfoEntry>();
        entries.add(new SysInfoEntry("Platform", SWT.getPlatform()));
        entries.add(new SysInfoEntry("Version", Integer.toString(SWT.getVersion())));
        return entries;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.jaret.app.swt.InfoProvider.JaretSystemInfoProvider#getSubInfoProviders()
     */
    public List<JaretInfoProvider> getSubInfoProviders() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.jaret.app.swt.InfoProvider.JaretSystemInfoProvider#addSubInfoProvider(de.jaret.app.swt.InfoProvider.JaretSystemInfoProvider)
     */
    public void addSubInfoProvider(JaretInfoProvider infoProvider) {
        throw new RuntimeException("Not implemented");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.jaret.app.swt.InfoProvider.JaretSystemInfoProvider#remSubInfoProvider(de.jaret.app.swt.InfoProvider.JaretSystemInfoProvider)
     */
    public void remSubInfoProvider(JaretInfoProvider infoProvider) {
    }
}
