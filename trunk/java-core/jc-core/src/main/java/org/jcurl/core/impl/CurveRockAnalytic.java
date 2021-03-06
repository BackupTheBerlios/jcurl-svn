/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
package org.jcurl.core.impl;

import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.RockType;
import org.jcurl.math.CurveFkt;
import org.jcurl.math.R1R1Function;
import org.jcurl.math.R1RNFunction;

/**
 * Wrapper for rock trajectory segments wrapping 3 dimensional
 * {@link R1RNFunction}s.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:CurveRockAnalytic.java 780 2008-03-18 11:06:30Z mrohrmoser $
 */
public class CurveRockAnalytic<T extends RockType> extends CurveRock<T> {

	private static final long serialVersionUID = 6879559446969142018L;

	private final R1RNFunction curve;

	public CurveRockAnalytic(final R1R1Function x, final R1R1Function y,
			final R1R1Function a) {
		this(new R1R1Function[] { x, y, a });
	}

	public CurveRockAnalytic(final R1R1Function[] x) {
		this(new CurveFkt(x));
	}

	public CurveRockAnalytic(final R1RNFunction f) {
		if (f.dim() != 3)
			throw new IllegalArgumentException("Function must be 3-dimensional");
		curve = f;
	}

	@Override
	public double at(final double t, final int c, final int dim) {
		return curve.at(t, c, dim);
	}

	public R1RNFunction getCurve() {
		return curve;
	}

	@Override
	public String toString() {
		return curve.toString();
	}

}
