/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.core.math;

import java.awt.geom.Point2D;

/**
 * Helper class that brings some (2D-)vector artihmetics.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public final class MathVec {

    public static double abs(final Point2D a) {
        return Math.sqrt(scal(a, a));
    }

    public static Point2D add(final Point2D a, final Point2D b, final Point2D c) {
        final Point2D ret = ensureInstance(a, c);
        ret.setLocation(a.getX() + b.getX(), a.getY() + b.getY());
        return ret;
    }

    public static double distSq(final double[] a, final double[] b,
            final double[] tmp) {
        final double[] c = MathVec.sub(a, b, tmp);
        return MathVec.scal(c, c);
    }

    /**
     * Ensure c isn't <code>null</code>, if so create a new instance of the
     * type of <code>template</code>.
     * 
     * @param template
     * @param c
     * @return <code>c</code> or
     *         <code>template.getClass().newInstance()</code>
     */
    public static Point2D ensureInstance(final Point2D template, final Point2D c) {
        if (c != null)
            return c;
        // try {
        // return (Point2D) template.getClass().newInstance();
        return (Point2D) template.clone();
        // } catch (InstantiationException e) {
        // throw new RuntimeException("Couldn't create a new instance.", e);
        // } catch (IllegalAccessException e) {
        // throw new RuntimeException("Couldn't create a new instance.", e);
        // }
    }

    /**
     * Solve a linear equation of the form a*x=b.
     * <p>
     * See Numerische Mathematik, H. R. Schwarz, B. G. Teubner Verlag, 1998,
     * S.22f.
     * </p>
     * 
     * @see MathVecTest#test010_gauss()
     * @param a
     *            coefficient matrix. Attention! All fields a[i][j] are
     *            overwritten!
     * @param b
     *            constant vector. Attention! All fields b[i] are overwritten!
     * @param x
     *            solution (max be null)
     * @return x solution of a*x=b
     */
    public static double[] gauss(final double[][] a, final double[] b,
            double[] x) {
        final int n = a.length;
        // check parameters
        if (n != a[0].length)
            throw new IllegalArgumentException(
                    "Matrix a must be quadratic but is " + n + "x"
                            + a[0].length);
        if (n != b.length)
            throw new IllegalArgumentException(
                    "Dimension of vector b must match Matrix a but is "
                            + b.length);
        if (x == null)
            x = new double[n];
        if (n != x.length)
            throw new IllegalArgumentException(
                    "Dimension of vector x must match Matrix a but is "
                            + x.length);
        // start to work
        double det = 1;
        final int p[] = new int[n];
        outer: for (int k = 0; k < n - 1; k++) {
            double max = 0;
            p[k] = 0;
            for (int i = k; i < n; i++) {
                double s = 0;
                for (int j = k; j < n; j++)
                    s += Math.abs(a[i][j]);
                double q = Math.abs(a[i][k]) / s;
                if (q > max) {
                    max = q;
                    p[k] = i;
                }
            }
            if (max == 0)
                break outer;
            if (p[k] != k) {
                det = -det;
                for (int j = 0; j < n; j++) {
                    final double h = a[k][j];
                    a[k][j] = a[p[k]][j];
                    a[p[k]][j] = h;
                }
            }
            det *= a[k][k];
            for (int i = k + 1; i < n; i++) {
                a[i][k] /= a[k][k];
                for (int j = k + 1; j < n; j++) {
                    a[i][j] -= a[i][k] * a[k][j];
                }
            }
        }
        det *= a[n - 1][n - 1];
        // Vorwärtseinsetzen
        for (int k = 0; k < n - 1; k++) {
            if (p[k] != k) {
                final double h = b[k];
                b[k] = b[p[k]];
                b[p[k]] = h;
            }
        }
        final double[] c = new double[n];
        for (int i = 0; i < n; i++) {
            c[i] = b[i];
            for (int j = 0; j < i - 1; j++) {
                c[i] -= a[i][j] * c[j];
            }
        }
        // Rückwärtseinsetzen
        for (int i = n - 1; i >= 0; i--) {
            double s = c[i];
            for (int k = i + 1; k < n; k++)
                s += a[i][k] * x[k];
            x[i] = -s / a[i][i];
        }
        return x;
    }

    public static double[] mult(final double fact, final double[] a, double[] b) {
        if (b == null)
            b = new double[a.length];
        for (int i = a.length - 1; i >= 0; i--)
            b[i] = a[i] * fact;
        return b;
    }

    public static Point2D mult(final double fact, final Point2D a, Point2D b) {
        b = ensureInstance(a, b);
        b.setLocation(a.getX() * fact, a.getY() * fact);
        return b;
    }

    public static double scal(final double[] a, final double[] b) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Dimension mismatch: "
                    + a.length + "!=" + b.length);
        double c = 0;
        for (int i = a.length - 1; i >= 0; i--)
            c += a[i] * b[i];
        return c;
    }

    public static double scal(final Point2D a, final Point2D b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

    public static double[] sub(final double[] a, final double[] b, double[] c) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Dimension mismatch: "
                    + a.length + "!=" + b.length);
        if (c == null)
            c = new double[a.length];
        else if (a.length != c.length)
            throw new IllegalArgumentException("Dimension mismatch: "
                    + a.length + "!=" + c.length);
        for (int i = a.length - 1; i >= 0; i--)
            c[i] = a[i] - b[i];
        return c;
    }

    public static Point2D sub(final Point2D a, final Point2D b, final Point2D c) {
        final Point2D ret = ensureInstance(a, c);
        ret.setLocation(a.getX() - b.getX(), a.getY() - b.getY());
        return ret;
    }

    public static String toString(double[] v) {
        if (v == null)
            return "null";
        final StringBuffer buf = new StringBuffer();
        final int n = v.length;
        buf.append('[');
        for (int i = 0; i < n; i++) {
            buf.append(v[i]);
            buf.append(", ");
        }
        if (n > 0)
            buf.setLength(buf.length() - 2);
        buf.append(']');
        return buf.toString();
    }
}