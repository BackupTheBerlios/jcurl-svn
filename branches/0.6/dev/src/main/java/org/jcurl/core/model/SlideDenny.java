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
package org.jcurl.core.model;


import org.jcurl.core.base.Ice;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.base.SlideCurves;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.math.CurveBase;
import org.jcurl.math.CurveFkt;
import org.jcurl.math.Polynome;

/**
 * Mark Denny's curl-model. Motion of a curling rock acc. to "Curling rock
 * dynamics", Mark Denny, Canadian Journal of Physics, 1988, P. 295-304.
 * <p>
 * 
 * @see org.jcurl.core.model.SlideStraight
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideDenny extends SlideCurves {

    private static final double _R = 6.5e-2;

    private static final double eps = RockProps.DEFAULT.getInertia()
            / (RockProps.DEFAULT.getMass() * sqr(_R));

    private static final double g = 9.81;

    private double _mu;

    private double b;

    private double draw_curl;

    private double draw_time;

    public SlideDenny() {
        super();
    }

    protected CurveBase createCurve(final double t0, final Rock pos,
            final Rock speed) {
        throw new NotImplementedYetException();
    }

    protected CurveBase createCurve(final Rock speed) {
        final Polynome[] x = new Polynome[3];
        x[0] = new Polynome(new double[] { 0, 1, 2, 3 });
        x[1] = new Polynome(new double[] { 0, 1, 2, 3 });
        x[2] = new Polynome(new double[] { 0, 1, 2, 3 });
        return new CurveFkt(x);
    }

    public String description() {
        return "Mark Denny's curl model";
    }

    /**
     * Guess the initial speed.
     * 
     * @param y
     *            Start (distance to tee-line)
     * @param t
     *            from Hog to Hog. see ./doc/eiszeit.tex for details.
     * @return [meter/sec]
     */
    public double getInitialSpeed(final double y, final double t) {
        double tmp;

        final double Y = Ice.FAR_HOG_2_TEE - y;
        tmp = _mu * g * t + 2.0 * Ice.HOG_2_HOG / t;
        tmp *= tmp;
        // tmp += 4.0 * _mu * g * Ice.HOG_2_HOG;

        assert Y <= tmp / (8.0 * g * _mu); // ensure a positive sqrt-arg.

        tmp -= 8.0 * g * _mu * Y;
        tmp = Math.sqrt(tmp) / 2.0;

        return tmp;
    }

    public double getMu() {
        return _mu;
    }

    public void setDraw2Tee(final double T, final double X) {
        super.setDraw2Tee(T, X);
        draw_time = T;
        _mu = 2.0 * Ice.FAR_HOG_2_TEE / (sqr(draw_time) * g);
        draw_curl = X;
        b = -draw_curl * 12.0 * eps * _R / sqr(Ice.FAR_HOG_2_TEE);
    }
}