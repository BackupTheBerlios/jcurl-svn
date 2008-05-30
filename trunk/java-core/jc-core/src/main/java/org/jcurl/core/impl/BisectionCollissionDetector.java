/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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

package org.jcurl.core.impl;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.CollissionDetector;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.BisectionSolver;
import org.jcurl.math.Distance2DSq;
import org.jcurl.math.R1R1Function;
import org.jcurl.math.R1RNFunction;

/**
 * Collission detector based on {@link Distance2DSq} and {@link BisectionSolver}.
 * <p>
 * As the {@link NewtonCollissionDetector} got stuck in an endless loop under
 * certain circumstances (see the "HngrrrrTest JUnit test) let's try bisection.
 * </p>
 * <p>
 * As crossing curves have a hit-distance 2 times we use a trick and use the
 * sign of the first derivative to flip one half of the distance function
 * upside-down. Now bisection can find the hit.
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class BisectionCollissionDetector extends CollissionDetectorBase {

	private static final Log log = JCLoggerFactory
			.getLogger(BisectionCollissionDetector.class);

	public double compute(final double t0, final double tstop,
			final R1RNFunction fa, final R1RNFunction fb, final double distSq) {
		final R1R1Function dist = new Distance2DSq(fa, fb, 0);
		final double trend = Math.signum(dist.at(1, 0));
		final R1R1Function f = new R1R1Function() {
			private static final long serialVersionUID = 7051701140539614770L;

			@Override
			public double at(final int c, final double x) {
				if (c != 0)
					throw new IllegalArgumentException();
				return dist.at(0, x) * Math.signum(dist.at(1, x)) * trend;
			}
		};
		final double r = BisectionSolver.findRoot(f, CollissionDetector.RR2,
				t0, tstop);
		if (log.isDebugEnabled())
			log.debug(dist.at(r) - CollissionDetector.RR2);
		if (Double.isNaN(r)
				|| Math.abs(dist.at(r) - CollissionDetector.RR2) > 1e-6)
			return Double.NaN;
		return r;
	}
}
