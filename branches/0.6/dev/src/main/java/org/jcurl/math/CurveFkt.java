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
package org.jcurl.math;

/**
 * A n-dimensional, continuous curve <code>R -&gt; R^n</code> based on
 * {@link org.jcurl.math.Function1D}s.
 * 
 * @see org.jcurl.math.Function1D
 * @see org.jcurl.math.CurveTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveFkt extends CurveBase {

    private final Function1D[] fkt;

    public CurveFkt(final Function1D[] fkt) {
        super(fkt.length);
        this.fkt = new Function1D[dim];
        for (int i = dim - 1; i >= 0; i--) {
            this.fkt[i] = fkt[i];
        }
    }

    public double getC(int dim, int c, double t) {
        return fkt[dim].getC(0, c, t);
    }
}