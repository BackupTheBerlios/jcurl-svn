/*
 * jcurl java curling software framework http://www.jcurl.orgCopyright (C)
 * 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.math;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Helper for convenient approximated Java2D drawing of arbitratry curves with
 * at least 2 dimensions.
 * 
 * @see Shaper
 * @see Shapeable
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class ShaperUtils {

	private static final Log log = JCLoggerFactory.getLogger(ShaperUtils.class);

	/**
	 * Turn a {@link R1RNFunction} (of at least 2 dimensions) into a
	 * {@link Shape} with the given number of samples and straight lines
	 * {@link GeneralPath#lineTo(float, float)} to interpolate between.
	 * 
	 * @param src
	 *            the (2-dimensional) curve. Higher dimensions are ignored.
	 * @param min
	 *            the min input <code>t</code> to
	 *            {@link R1RNFunction#at(int, int, double)}
	 * @param max
	 *            the max input <code>t</code> to
	 *            {@link R1RNFunction#at(int, int, double)}
	 * @param samples
	 *            the number of samples (start + stop + intermediate) - must be
	 *            &gt;= 2 (start + stop + intermediate).
	 * @param zoom
	 *            graphics zoom factor (typically 1)
	 * @param ip
	 *            the {@link Interpolator} to get the intermediate sample
	 *            <code>t</code> values.
	 */
	public static Shape approximateLinear(final R1RNFunction src,
			final double min, final double max, final int samples,
			final float zoom, final Interpolator ip) {
		// setup
		if (samples < 2)
			throw new IllegalArgumentException(
					"Give me at least 2 (start + stop)");
		final float d = (float) (max - min);
		final GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO,
				samples + 1); // +1 just to be sure...
		// start
		final float x = (float) src.at(0, 0, min);
		final float y = (float) src.at(1, 0, min);
		gp.moveTo(zoom * x, zoom * y);

		// intermediate
		final int n = samples - 1;
		for (int i = 1; i < n; i++) {
			final double t = min + d * ip.interpolate((float) i / n);
			lineTo(src, t, gp, zoom);
		}

		// stop
		lineTo(src, max, gp, zoom);
		return gp;
	}

	/**
	 * Turn a {@link R1RNFunction} (of at least 2 dimensions) into a
	 * {@link Shape} with the given number of samples and straight lines
	 * {@link GeneralPath#lineTo(float, float)} to interpolate between.
	 * 
	 * @param src
	 *            the (2-dimensional) curve. Higher dimensions are ignored.
	 * @param min
	 *            the min input <code>t</code> to
	 *            {@link R1RNFunction#at(int, int, double)}
	 * @param max
	 *            the max input <code>t</code> to
	 *            {@link R1RNFunction#at(int, int, double)}
	 * @param samples
	 *            the number of samples (start + stop + intermediate) - must be
	 *            &gt;= 2 (start + stop + intermediate).
	 * @param zoom
	 *            graphics zoom factor (typically 1)
	 * @param ip
	 *            the {@link Interpolator} to get the intermediate sample
	 *            <code>t</code> values.
	 */
	public static Shape approximateQuadratic(final R1RNFunction src,
			final double min, final double max, final int samples,
			final float zoom, final Interpolator ip) {
		// setup
		if (samples < 2)
			throw new IllegalArgumentException(
					"Give me at least 2 (start + stop)");
		final float d = (float) (max - min);
		final GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO,
				2 * (samples + 1)); // +1 just to be sure...
		// start
		final float x = (float) src.at(0, 0, min);
		final float y = (float) src.at(1, 0, min);
		gp.moveTo(zoom * x, zoom * y);

		double told = min;
		// intermediate
		final int n = samples - 1;
		for (int i = 1; i < n; i++) {
			final double t = min + d * ip.interpolate((float) i / n);
			quadTo(src, told, t, gp, zoom);
			told = t;
		}

		// stop
		quadTo(src, told, max, gp, zoom);
		return gp;
	}

	private static float interpolate(final float min, final float max,
			final float t, final Interpolator ip) {
		final float d = max - min;
		return min + d * ip.interpolate((t - min) / d);
	}

	private static final void lineTo(final R1RNFunction f, final double t,
			final GeneralPath gp, final float zoom) {
		final float x = (float) f.at(0, 0, t);
		final float y = (float) f.at(1, 0, t);
		gp.lineTo(zoom * x, zoom * y);
	}

	/**
	 * TODO re-use endpoint location and velocity.
	 * 
	 * Maxima solution:
	 * 
	 * <pre>
	 * x1_0 + k * v1_0 = x3_0 + l * v3_0;
	 * x1_1 + k * v1_1 = x3_1 + l * v3_1;
	 * solve([%o1,%o2],[k,l]);
	 * subst(q, v1_1 * v3_0 - v1_0 * v3_1, %);
	 * subst(dx_0 + x1_0, x3_0, %);
	 * subst(dx_1 + x1_1, x3_1, %);
	 * ratsimp(%);
	 * </pre>
	 * 
	 * @param f
	 * @param tmin
	 * @param tmax
	 * @param gp
	 * @param zoom
	 */
	private static final void quadTo(final R1RNFunction f, final double tmin,
			final double tmax, final GeneralPath gp, final float zoom) {
		final double eps = 1e-6;

		// first control point (startpoint). The same as gp.getCurrentPoint()
		final double x1_0 = f.at(0, 0, tmin);
		final double x1_1 = f.at(1, 0, tmin);
		// startpoint velocity
		double v1_0 = f.at(0, 1, tmin);
		double v1_1 = f.at(1, 1, tmin);
		if (v1_0 * v1_0 + v1_1 * v1_1 < eps) {
			v1_0 = f.at(0, 1, tmin + eps);
			v1_1 = f.at(1, 1, tmin + eps);
		}

		// 3rd control point (endpoint).
		final double x3_0 = f.at(0, 0, tmax);
		final double x3_1 = f.at(1, 0, tmax);
		// endpoint velocity
		double v3_0 = f.at(0, 1, tmax);
		double v3_1 = f.at(1, 1, tmax);
		if (v3_0 * v3_0 + v3_1 * v3_1 < eps) {
			v3_0 = f.at(0, 1, tmax - eps);
			v3_1 = f.at(1, 1, tmax - eps);
		}

		// compute the 2nd control point
		final double dx_0 = x3_0 - x1_0;
		final double dx_1 = x3_1 - x1_1;
		final double q = v1_1 * v3_0 - v1_0 * v3_1;
		final double l = -(dx_0 * v1_1 - dx_1 * v1_0) / q;

		// 2nd control point is
		final float x2_0 = (float) (x3_0 + l * v3_0);
		final float x2_1 = (float) (x3_1 + l * v3_1);

		if (true)
			gp.quadTo(zoom * x2_0, zoom * x2_1, zoom * (float) x3_0, zoom
					* (float) x3_1);
		else {
			gp.lineTo(zoom * x2_0, zoom * x2_1);
			gp.lineTo(zoom * (float) x3_0, zoom * (float) x3_1);
		}
	}

	static String toString(final double[] arr) {
		final StringBuilder w = new StringBuilder();
		if (arr == null)
			w.append("null");
		else {
			boolean start = true;
			w.append("[");
			for (final double element : arr) {
				if (!start)
					w.append(" ");
				w.append(Double.toString(element));
				start = false;
			}
			w.append("]");
		}
		return w.toString();
	}

}