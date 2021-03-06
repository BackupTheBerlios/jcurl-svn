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
package jcurl.core.dto;

import jcurl.model.PositionSet;

/**
 * Properties of a unique set of rocks (size, mass, etc.)
 * 
 * @see jcurl.core.dto.RockProps
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockSetProps {

    public static final RockSetProps DEFAULT;
    static {
        DEFAULT = new RockSetProps();
        for (int i = DEFAULT.dark.length - 1; i >= 0; i--) {
            DEFAULT.dark[i] = RockProps.DEFAULT;
            DEFAULT.light[i] = RockProps.DEFAULT;
        }
    }

    private final RockProps[] dark = new RockProps[PositionSet.ROCKS_PER_COLOR];

    private final RockProps[] light = new RockProps[PositionSet.ROCKS_PER_COLOR];

    public RockSetProps() {
        for (int i = dark.length - 1; i >= 0; i--) {
            dark[i] = new RockProps();
            light[i] = new RockProps();
        }
    }

    public RockProps getDark(int i) {
        return dark[i];
    }

    public RockProps getLight(int i) {
        return light[i];
    }
}