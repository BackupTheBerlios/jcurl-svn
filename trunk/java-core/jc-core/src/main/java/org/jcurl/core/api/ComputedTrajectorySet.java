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
package org.jcurl.core.api;

import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.math.R1RNFunction;

/**
 * {@link R1RNFunction} based set of {@link RockSet#ROCKS_PER_SET} trajectories.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:ComputedTrajectorySet.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public interface ComputedTrajectorySet extends TrajectorySet {

	Collider getCollider();

	/** expect this property to be transient */
	CollissionDetector getCollissionDetector();

	Curler getCurler();

	/** expect this property to be transient */
	CurveStore getCurveStore();

	RockSet<Pos> getInitialPos();

	RockSet<Vel> getInitialVel();

	/**
	 * expect this property to be transient
	 * 
	 * @see #setSuspended(boolean)
	 */
	public boolean getSuspended();

	void setCollider(final Collider collider);

	void setCollissionDetector(final CollissionDetector collissionDetector);

	void setCurler(final Curler curler);

	void setCurveStore(final CurveStore curveStore);

	/**
	 * Currently the internal reference MUST be final, so this updates via
	 * {@link RockSet#setLocation(RockSet)}. This is the case to simplify event
	 * controllers.
	 * 
	 * @param initialPos
	 */
	void setInitialPos(final RockSet<Pos> initialPos);

	/**
	 * Currently the internal reference MUST be final, so this updates via
	 * {@link RockSet#setLocation(RockSet)}. This is the case to simplify event
	 * controllers.
	 * 
	 * @param initialVel
	 */
	void setInitialVel(final RockSet<Vel> initialVel);

	/**
	 * @param suspend
	 *            <ul>
	 *            <li><code>true</code>: don't immediately recompute, but
	 *            collect requested recomputations until set <code>false</code>
	 *            again</li>
	 *            <li><code>false</code>: process collected
	 *            recompute-requests and listen again.</li>
	 *            </ul>
	 */
	void setSuspended(boolean suspend);
}