/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.base;

import org.jcurl.core.helpers.EnumBase;

/**
 * North, East, South, West.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Orientation.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class Orientation extends EnumBase {

    private static final long serialVersionUID = -2356484723388215100L;

    /** North */
    public static final Orientation N = new Orientation(0, "north");

    /** East */
    public static final Orientation E = new Orientation(1, "east");

    /** South */
    public static final Orientation S = new Orientation(2, "south");

    /** West */
    public static final Orientation W = new Orientation(3, "west");

    public final double angle;

    /**
     * @param state
     * @param text
     */
    private Orientation(final int state, final String text) {
        super(state, text);
        angle = state * Math.PI / 2;
    }
}