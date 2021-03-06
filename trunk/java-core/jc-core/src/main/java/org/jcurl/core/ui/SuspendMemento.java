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

import org.jcurl.core.api.ComputedTrajectorySet;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class SuspendMemento extends Memento<ComputedTrajectorySet> {

	private static final long serialVersionUID = -4168117544962338554L;
	private final boolean suspend;

	public SuspendMemento(final ComputedTrajectorySet context,
			final boolean suspend) {
		super(context);
		this.suspend = suspend;
	}

	@Override
	public ComputedTrajectorySet apply(final ComputedTrajectorySet dst) {
		dst.setSuspended(suspend);
		return dst;
	}

	@Override
	protected Object clone() {
		return new SuspendMemento(getContext(), suspend);
	}

}
