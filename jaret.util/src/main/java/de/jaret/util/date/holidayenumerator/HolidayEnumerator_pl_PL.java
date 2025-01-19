/*
 *  File: HolidayEnumerator_pl_PL.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.date.holidayenumerator;

import java.util.Locale;

/**
 * HolidayEnumerator for Poland. Includes public holidays according to <a
 * href="https://en.wikipedia.org/wiki/Public_holidays_in_Poland">https://en.wikipedia.org/wiki/Public_holidays_in_Poland</a>. <b>The
 * calculations are without any warranty!</b>
 * 
 * @author Peter Kliem
 * @version $Id: HolidayEnumerator_pl_PL.java 293 2007-03-11 17:50:57Z olk $
 */
public class HolidayEnumerator_pl_PL extends HolidayEnumeratorBase {
    /**
     * Constructs the he for poland.
     * 
     * @param regionId region id (currently no region support)
     */
    public HolidayEnumerator_pl_PL(String regionId) {
        _regionId = regionId;
        _locale = new Locale("pl", "PL");
    }

    /*
     * 7th Sunday after Easter Pentecost Sunday pierwszy dzie? Zielonych ?wi?tek (Zielone ?wi?tki) As this holiday
     * always falls on a Sunday it is not widely known. 9th Thursday after Easter Corpus Christi dzie? Bo?ego Cia?a
     * (Bo?e Cia?o)
     */
    /**
     * {@inheritDoc}
     */
    protected void fillMap(int year) {
        // general holidays
        addNamedDate(year, 0, 1, true, "Nowy Rok");
        addNamedDate(year, 4, 1, true, "Święto Państwowe");
        addNamedDate(year, 4, 3, true, "Święto Narodowe Trzeciego Maja");

        addNamedDate(year, 7, 15, true, "Wniebowzięcie Najświętszej Maryi Panny");
        addNamedDate(year, 10, 1, true, "Wszystkich Świętych");
        addNamedDate(year, 10, 11, true, "Narodowe Święto Niepodległości ");
        // addNamedDate(year, 11, 24, false, "Heiligabend");
        addNamedDate(year, 11, 25, true, "pierwszy dzień Bożego Narodzenia");
        addNamedDate(year, 11, 26, true, "drugi dzień Bożego Narodzenia");
        // addNamedDate(year, 11, 31, false, "Sylvester");

        // easter days
        EasyDate ed = calcEaster(year);
        addNamedDate(year, ed.month, ed.day, true, "pierwszy dzień Wielkiej Nocy");
        addNamedDate(year, ed.month, ed.day, 1, true, "drugi dzień Wielkiej Nocy");
        addNamedDate(year, ed.month, ed.day, 7, true, "pierwszy dzień Zielonych Świętek");

        // 9th Thursday after Easter
        addNamedDate(year, ed.month, ed.day, 4 + 9 * 7, true, "dzień Bożego Ciała");

    }

}
