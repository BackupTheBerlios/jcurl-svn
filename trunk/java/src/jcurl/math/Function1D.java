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
package jcurl.math;

/**
 * A "normal" one-dimensional function. Because this is the same as a
 * 1-dimensional curve it extends {@link jcurl.math.CurveBase}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class Function1D extends CurveBase {

    protected Function1D() {
        super(1);
    }

    public abstract double getC(int c, double t);

    public double getC(int dim, int c, double t) {
        if (dim != 0)
            throw new IllegalArgumentException("Dimension must be 0");
        return getC(c, t);
    }
}