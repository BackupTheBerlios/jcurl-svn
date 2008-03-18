/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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

import org.jcurl.math.R1RNFunction;

/**
 * {@link R1RNFunction} based set of {@link RockSet#ROCKS_PER_SET} trajectories.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:ComputedTrajectorySet.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public interface ComputedTrajectorySet extends TrajectorySet {

    public abstract Collider getCollider();

    public abstract CollissionDetector getCollissionDetector();

    public abstract Curler getCurler();

    public abstract PositionSet getInitialPos();

    public abstract SpeedSet getInitialSpeed();

    public abstract void setCollider(final Collider collider);

    public abstract void setCollissionDetector(
            final CollissionDetector collissionDetector);

    public abstract void setCurler(final Curler curler);

    /**
     * Currently the internal reference MUST be final, so this updates via
     * {@link PositionSet#setLocation(RockSet)}. This is the case to simplify
     * event controllers.
     * 
     * @param initialPos
     */
    public abstract void setInitialPos(final PositionSet initialPos);

    /**
     * Currently the internal reference MUST be final, so this updates via
     * {@link SpeedSet#setLocation(RockSet)}. This is the case to simplify
     * event controllers.
     * 
     * @param initialSpeed
     */
    public abstract void setInitialSpeed(final SpeedSet initialSpeed);

}