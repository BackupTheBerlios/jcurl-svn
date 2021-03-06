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
package org.jcurl.model;

import org.apache.commons.math.FunctionEvaluationException;
import org.jcurl.core.dto.PositionSet;
import org.jcurl.core.dto.SpeedSet;
import org.jcurl.core.helpers.MutableObject;
import org.jcurl.core.helpers.NotImplementedYetException;

public abstract class RockSetPaths extends MutableObject {

    public abstract PositionSet getCurrentPos()
            throws FunctionEvaluationException;

    public abstract SpeedSet getCurrentSpeed();

    public abstract double getCurrentT();

    public abstract double getMaxT();

    public void setCurrentPos(PositionSet currentPos) {
        throw new NotImplementedYetException();
    }

    public void setCurrentSpeed(SpeedSet currentSpeed) {
        throw new NotImplementedYetException();
    }

    public abstract void setCurrentT(double currentT)
            throws FunctionEvaluationException;

}
