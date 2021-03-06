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

/**
 * A {@link org.jcurl.core.base.RockSet}&nbsp;with speed semantics.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SpeedSet.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class SpeedSet extends RockSet {

    private static final long serialVersionUID = 7650353293030622027L;

    public SpeedSet() {
        super();
    }

    protected SpeedSet(final boolean fill) {
        super(fill);
    }

    public SpeedSet(final RockSet b) {
        super(b);
    }

    @Override
    public Object clone() {
        return new SpeedSet(this);
    }
}