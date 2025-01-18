/*
 *  File: TimeHelperTest.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.date;

import java.text.ParseException;

import org.junit.Test;

import junit.framework.TestCase;

public class TimeHelperTest extends TestCase {

	@Test
	public void testConversion() {
		int sec = 0;
		try {
			sec = TimeHelper.timeStringToSeconds("8:00");
		} catch (ParseException e) {
			fail();
		}
		assertEquals(sec, 8*60*60);
		
		try {
			sec = TimeHelper.timeStringToSeconds("8:00:30");
		} catch (ParseException e) {
			fail();
		}
		assertEquals(sec, 8*60*60+30);
		
		try {
			sec = TimeHelper.timeStringToSeconds("16:00");
		} catch (ParseException e) {
			fail();
		}
		assertEquals(sec, 16*60*60);

		boolean caught = false;
		try {
			sec = TimeHelper.timeStringToSeconds("16:a00");
		} catch (ParseException e) {
			// ok
			caught = true;
		}
		assertTrue(caught);
		
	}
	
}
