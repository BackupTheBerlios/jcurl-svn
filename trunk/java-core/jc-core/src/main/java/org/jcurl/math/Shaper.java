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

package org.jcurl.math;

import java.awt.Shape;

/**
 * Turn a {@link R1RNFunction} into a {@link Shape}.
 * 
 * @see Shape
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public interface Shaper {
	/**
	 * Convert a given interval of a {@link R1RNFunction} into a {@link Shape}.
	 * 
	 * @param f
	 *            the input curve - usually the first 2 dimensions are used.
	 * @param tmin
	 *            lower interval end
	 * @param tmax
	 *            upper interval end
	 * @return the resulting shape, usually a {@link java.awt.geom.GeneralPath}
	 *         or <code>null</code> if no sensible curve exists.
	 */
	Shape toShape(R1RNFunction f, double tmin, double tmax);
}
