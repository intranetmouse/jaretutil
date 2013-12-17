/*
 *  File: JaretStyledText.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package org.eclipse.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Extension (HACK!) of the styled text widget to add a method for setting the caret offset without clearing the
 * selection.
 * 
 * @author Peter Kliem
 * @version $Id: JaretStyledText.java 242 2007-02-11 21:05:07Z olk $
 */
public class JaretStyledText extends StyledText {

    public JaretStyledText(Composite arg0, int arg1) {
        super(arg0, arg1);
    }

    public void setCaretOffsetJaret(int offset) {
        checkWidget();
        int length = getCharCount();

        if (length > 0 && offset != caretOffset) {
            if (offset < 0) {
                caretOffset = 0;
            } else if (offset > length) {
                caretOffset = length;
            } else {
                if (isLineDelimiter(offset)) {
                    SWT.error(SWT.ERROR_INVALID_ARGUMENT);
                }
                caretOffset = offset;
            }
        }
        setCaretLocation();
    }
}
