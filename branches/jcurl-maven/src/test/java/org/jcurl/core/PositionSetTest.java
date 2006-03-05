/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.core;

import java.awt.geom.Point2D;

import junit.framework.TestCase;

import org.apache.ugli.ULogger;
import org.jcurl.core.PositionSet;
import org.jcurl.core.RockSet;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.helpers.JCLoggerFactory;

/**
 * JUnit Test
 * 
 * @see org.jcurl.core.PositionSet
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: PositionSetTest.java 238 2006-02-19 16:17:09Z mrohrmoser $
 */
public class PositionSetTest extends TestCase {

    private static final ULogger log = JCLoggerFactory
            .getLogger(PositionSetTest.class);

    public static void main(String[] args) {
        junit.textui.TestRunner.run(PositionSetTest.class);
    }

    public void test010_CountBits() {
        log.info("Dark : " + Integer.toBinaryString(RockSet.DARK_MASK));
        log.info("Light: " + Integer.toBinaryString(RockSet.LIGHT_MASK));
        assertEquals(8, PositionSet.countBits(RockSet.DARK_MASK));
        assertEquals(8, PositionSet.countBits(RockSet.LIGHT_MASK));
    }

    public void test010_getShotRocks() {
        PositionSet a = new PositionSet();
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            a.getRock(i).setLocation(0, i * 0.5);
        assertEquals(1, PositionSet.getShotRocks(a));
    }

    public void test020_findRockAtPos() {
        PositionSet a = PositionSet.allHome();
        a.getRock(1).setLocation(0, 0);
        assertEquals(1, PositionSet.findRockIndexAtPos(a, new Point2D.Float(0,
                0)));
        assertEquals(1, PositionSet.findRockIndexAtPos(a, new Point2D.Float(Dim
                .f2m(0.4), 0)));
    }
}