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
package org.jcurl.math;

/**
 * Polynomes of n-th grade.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Polynome extends R1R1Function {
    /**
     * Compute <code>a!</code>
     * 
     * @param a
     * @return a!
     */
    static long fak(final int a) {
        return fak(a, 1);
    }

    /**
     * Compute <code>high! / low!</code>
     * 
     * @param high
     * @param low
     * @return high! / low!
     */
    static long fak(final int high, final int low) {
        if (high < 2)
            return 1;
        long ret = 1;
        for (int i = low < 2 ? 1 : low; i < high; ret *= ++i)
            ;
        return ret;
    }

    /**
     * Convenience method to get the "bewegungsgleichung" for a given initial
     * state.
     * 
     * @param t0
     *            initial time
     * @param x0
     *            initial location
     * @param v0
     *            initial speed
     * @param a0
     *            constant acceleration
     * @return the resulting polynome
     * @see #getPolyParams(double, double, double, double)
     */
    public static final Polynome getPoly(final double t0, final double x0,
            final double v0, final double a0) {
        return new Polynome(getPolyParams(t0, x0, v0, a0));
    }

    /**
     * Convenience method to get the "bewegungsgleichung" for a given initial
     * state.
     * 
     * @param t0
     *            initial time
     * @param x0
     *            initial location
     * @param v0
     *            initial speed
     * @param a0
     *            constant acceleration
     * @return the resulting polynome's parameters
     */
    public static final double[] getPolyParams(final double t0,
            final double x0, final double v0, final double a0) {
        final double[] p = { x0 - v0 * t0 + 0.5 * a0 * t0 * t0, v0 - a0 * t0,
                0.5 * a0 };
        return p;
    }

    /**
     * Compute the polynome p at x.
     * 
     * @param x
     * @param p
     *            polynome coefficients
     * @return <code>p(x)</code>
     * @see #poly(int, double, double[])
     */
    public static double poly(final double x, final double[] p) {
        return poly(0, x, p);
    }

    /**
     * Compute the c-th derivative of the polynome p at x.
     * 
     * @param c
     *            derivative
     * @param x
     * @param p
     *            polynome coefficients
     * @return <code>d/dx^c p(x)</code>
     */
    public static double poly(final int c, final double x, final double[] p) {
        double ret = 0;
        for (int i = p.length - 1; i >= c; i--) {
            ret *= x;
            ret += fak(i, i - c) * p[i];
        }
        return ret;
    }

    public static String toString(final double[] poly) {
        final StringBuffer ret = new StringBuffer();
        ret.append("p(x) = ");
        for (int i = 0; i < poly.length; i++) {
            ret.append(poly[i]);
            ret.append("*x**");
            ret.append(i);
            ret.append(" + ");
        }
        ret.setLength(ret.length() - 3);
        return ret.toString();
    }

    private final double[] params;

    public Polynome(final double[] params) {
        this.params = params;
    }

    /**
     * @param c
     * @param x
     * @return the value
     * @see #poly(int, double, double[])
     */
    @Override
    public double at(final int c, final double x) {
        return poly(c, x, params);
    }

    @Override
    public String toString() {
        return toString(params);
    }
}