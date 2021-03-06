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
package org.jcurl.core.api;

import java.io.Serializable;

import org.jcurl.math.R1RNFunction;

/**
 * When drops the "speed" (first derivative) of a function to zero?
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:StopDetector.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public interface StopDetector extends Strategy, Serializable {

	/**
	 * When drops the "speed" (first derivative) of a function to zero?
	 * 
	 * @param f
	 * @param t0
	 * @param tmax
	 * @return {@link Double#NaN} if not stopping within [tmin, tmax]
	 */
	public abstract double compute(R1RNFunction f, double t0, double tmax);
}