/*
 *  File: HolidayEnumeratorTest.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.date.holidayenumerator.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import junit.framework.TestCase;
import de.jaret.util.date.holidayenumerator.HolidayEnumerator;
import de.jaret.util.date.holidayenumerator.HolidayEnumeratorBase;
import de.jaret.util.date.holidayenumerator.HolidayEnumeratorFactory;
import de.jaret.util.date.holidayenumerator.HolidayEnumeratorBase.EasyDate;
/**
 * 
 * @author Peter Kliem
 * @version $Id: HolidayEnumeratorTest.java 175 2007-01-05 00:01:18Z olk $
 */
public class HolidayEnumeratorTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(HolidayEnumeratorTest.class);
	}

/*	
	public void testList() {
		HolidayEnumerator en = HolidayEnumeratorFactory.getHolidayEnumeratorInstance(Locale.GERMANY, null);
		List<NamedDate> l = en.getNamedDays(2005, true);
		for (NamedDate date : l) {
			System.out.println(date);
		}
	}
*/
	/**
	 * Basic function test
	 */
	@Test
	public void testDays() {
		HolidayEnumerator en = HolidayEnumeratorFactory.getHolidayEnumeratorInstance(Locale.GERMANY, null);
		Date date = getDate(2005, 1, 1);
		assertTrue(en.isHoliday(date));
	}
	/**
	 * Test the methods for calculating dates
	 *
	 */
	@Test
	public void testCalcFunctions() {
		// does not matter what locale is used
		HolidayEnumeratorBase en = (HolidayEnumeratorBase) HolidayEnumeratorFactory.getHolidayEnumeratorInstance(Locale.GERMANY, null);
		EasyDate ed = en.calcEaster(2005);
		assertTrue(ed.equals(2005, 2, 27));
		
		ed = en.nThWeekdayInMonth(2005, 1, Calendar.WEDNESDAY, 1);
		assertTrue(ed.equals(2005, 1, 2));
		ed = en.nThWeekdayInMonth(2005, 1, Calendar.WEDNESDAY, 2);
		assertTrue(ed.equals(2005, 1, 9));
		ed = en.nThWeekdayInMonth(2005, 1, Calendar.WEDNESDAY, 3);
		assertTrue(ed.equals(2005, 1, 16));
		ed = en.nThWeekdayInMonth(2005, 1, Calendar.WEDNESDAY, 4);
		assertTrue(ed.equals(2005, 1, 23));
		ed = en.nThWeekdayInMonth(2005, 2, Calendar.SATURDAY, 1);
		assertTrue(ed.equals(2005, 2, 5));
		ed = en.nThWeekdayInMonth(2005, 4, Calendar.SUNDAY, 1);
		assertTrue(ed.equals(2005, 4, 1));
		
		
		ed = en.lastWeekdayInMonth(2005, 2, Calendar.SATURDAY);
		assertTrue(ed.equals(2005, 2, 26));
		ed = en.lastWeekdayInMonth(2005, 11, Calendar.MONDAY);
		assertTrue(ed.equals(2005, 11, 26));

		ed = en.fridayOrMonday(en.getEasyDate(2005, 4, 21));
		assertTrue(ed.equals(2005, 4, 20));
		ed = en.fridayOrMonday(en.getEasyDate(2005, 4, 22));
		assertTrue(ed.equals(2005, 4, 23));
		

	}
	
	@Test
	public void testEnumeration() {
		List<Locale> locales = HolidayEnumeratorFactory.getAvailableHolidayEnumeratorLocales();
		if (locales.size()<4) {
			fail();
		}
		for (Locale locale : locales) {
			System.out.println("Available: "+locale);
		}
	}
	
	private Date getDate(int year, int month, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}	
	
	
}
