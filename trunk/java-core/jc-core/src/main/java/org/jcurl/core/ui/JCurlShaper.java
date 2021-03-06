/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
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

package org.jcurl.core.ui;

import java.awt.Shape;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.Interpolator;
import org.jcurl.math.Interpolators;
import org.jcurl.math.NaturalShaper;
import org.jcurl.math.R1RNFunction;
import org.jcurl.math.Shaper;
import org.jcurl.math.ShaperUtils;

/**
 * The default jcurl {@link Shaper}. Details see
 * {@link #toShape(R1RNFunction, double, double)}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlShaper extends NaturalShaper {

	private static final boolean DROP_STOP = true;
	private static final Interpolator ip = Interpolators.getLinearInstance();
	private static final Log log = JCLoggerFactory.getLogger(JCurlShaper.class);
	private static final double MIN_LEN = 1e-6;
	private final double meters_per_curve;
	private final float zoom;

	/** By default: 7 meters per curve segment */
	public JCurlShaper() {
		this(7);
	}

	protected JCurlShaper(final double meters_per_curve) {
		this(meters_per_curve, 1.0F);
	}

	/**
	 * Sample the input interval into <code>N</code> segments and use
	 * {@link Interpolators#getQuadraticInstance()} and
	 * {@link ShaperUtils#interpolateLinear(R1RNFunction, double, double, int, float, Interpolator)}
	 * to get the resulting shape.
	 * 
	 * @param meters_per_curve
	 *            must be &gt; 0
	 * @param zoom
	 *            an optional graphics zoom factor - usually 1.0F
	 */
	private JCurlShaper(final double meters_per_curve, final float zoom) {
		this.meters_per_curve = meters_per_curve;
		this.zoom = zoom;
	}

	/**
	 * Extension point to change curve rendering. Default curve rendering is
	 * {@link ShaperUtils#interpolateCubic(R1RNFunction, double, double, int, float, Interpolator)}.
	 */
	protected Shape doRender(final R1RNFunction f, final double tmin,
			final double tmax, final int segments, final float zoom,
			final Interpolator ip) {
		if (log.isDebugEnabled())
			log.debug("segments: " + segments);
		return ShaperUtils.interpolateCubic(f, (float) tmin, (float) tmax,
				segments, zoom, ip);
	}

	/**
	 * Does an "adaptive sampling for the poor": (distance start-stop) /
	 * meters_per_shape. But no less than 1 and no more than 7 curves. Delegates
	 * to
	 * {@link #doRender(R1RNFunction, double, double, int, float, Interpolator)}.
	 * <p>
	 * If the time distance or start-stop distance is next to zero no shape is
	 * created at all.
	 * </p>
	 * 
	 * @see #doRender(R1RNFunction, double, double, int, float, Interpolator)
	 */
	@Override
	public Shape toShape(final R1RNFunction f, final double tmin,
			final double tmax) {
		{
			final Shape s = super.toShape(f, tmin, tmax);
			if (s != null)
				return s;
		}
		final double len_sq;
		{
			final double x = f.at(tmin, 0, 0) - f.at(tmax, 0, 0);
			final double y = f.at(tmin, 0, 1) - f.at(tmax, 0, 1);
			len_sq = x * x + y * y;
		}
		if (DROP_STOP) {
			// treat some special cases:
			if (f == null || tmin + MIN_LEN >= tmax)
				return null;
			if (len_sq <= MIN_LEN * MIN_LEN)
				return null;
		}
		int segments = (int) (Math.sqrt(len_sq) / meters_per_curve);
		if (segments < 1)
			segments = 1;
		if (segments > 7)
			segments = 7;
		return doRender(f, tmin, tmax, segments, zoom, ip);
	}
}
