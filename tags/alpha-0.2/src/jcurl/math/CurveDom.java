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

import java.util.Map;
import java.util.TreeMap;

import jcurl.math.dom.DomWalkerEval;
import jcurl.math.dom.MathDom;

/**
 * A n-dimensional, continuous curve (R -&gt; R^n) based on a
 * {@link jcurl.math.dom.MathDom}.Node. Maybe it's better to add a
 * {@link jcurl.math.Function1D}based on {@link jcurl.math.dom.MathDom}and use
 * {@link jcurl.math.CurveFkt}rather than this class.
 * 
 * @see jcurl.math.dom.MathDom
 * @see jcurl.math.CurveTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveDom extends CurveBase {

    /** Internal helper - mutable number implementation */
    private static final class Num extends Number {

        public double v;

        public double doubleValue() {
            return v;
        }

        public float floatValue() {
            return (float) v;
        }

        public int intValue() {
            return (int) v;
        }

        public long longValue() {
            return (long) v;
        }

    }

    private final MathDom.Node [][] c;

    private final DomWalkerEval de;

    private final Map p = new TreeMap();

    private final Num t = new Num();

    public CurveDom(final MathDom.Node [] c0, final MathDom.Node [] c1,
            final String param) {
        super(c0.length);
        p.put(param, t);
        de = new DomWalkerEval(p);
        this.c = new MathDom.Node[2][dim];
        for (int i = dim - 1; i >= 0; i--) {
            this.c[0][i] = c0[i];
            this.c[1][i] = c1[i];
        }
    }

    public CurveDom(final MathDom.Node [] x, final String param) {
        this(x, null, param); // TODO compute and store the derivatives
    }

    public double getC(int dim, int c, double t) {
        if (c == 0) {
            this.t.v = t;
            de.reset();
            de.walk(this.c[0][dim]);
            return de.doubleValue();
        }
        throw new UnsupportedOperationException("Not supported.");
    }
}