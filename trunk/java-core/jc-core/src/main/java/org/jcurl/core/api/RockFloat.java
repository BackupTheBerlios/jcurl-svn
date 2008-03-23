/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
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
package org.jcurl.core.api;

import java.io.Serializable;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:RockFloat.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
class RockFloat<T extends RockType> extends Rock<T> implements Serializable {

	private static final long serialVersionUID = 3219049101239057245L;

	private final float[] x = new float[3];

	public RockFloat() {
		this(0, 0, 0);
	}

	public RockFloat(final float x, final float y, final float alpha) {
		this.x[0] = x;
		this.x[1] = y;
		this.x[2] = alpha;
	}

	@Override
	public Object clone() {
		return new RockFloat<T>(x[0], x[1], x[2]);
	}

	@Override
	public double getA() {
		return x[2];
	}

	@Override
	public double getX() {
		return x[0];
	}

	@Override
	public double getY() {
		return x[1];
	}

	@Override
	public int hashCode() {
		// http://www.angelikalanger.com/Articles/JavaSpektrum/03.HashCode/03.HashCode.html
		// hashcode N = hashcode N-1 * multiplikator + feldwert N
		int hash = 17;
		final int fact = 59;
		for (int i = 0; i < 3; i++) {
			hash *= fact;
			hash += x[0] == 0.0 ? 0 : java.lang.Float.floatToIntBits(x[0]);
		}
		return hash;
	}

	@Override
	public boolean isNotZero() {
		return x[0] * x[0] + x[1] * x[1] > 1e-12;
	}

	@Override
	public void setA(final double alpha) {
		if (alpha == x[2])
			return;
		x[2] = (float) alpha;
		fireStateChanged();
	}

	@Override
	public void setLocation(final double x, final double y, final double a) {
		if (x == this.x[0] && y == this.x[1] && a == this.x[2])
			return;
		this.x[0] = (float) x;
		this.x[1] = (float) y;
		this.x[2] = (float) a;
		fireStateChanged();
	}

	@Override
	public void setLocation(final double[] pt) {
		if (pt.length != 3)
			throw new IllegalArgumentException();
		setLocation(pt[0], pt[1], pt[2]);
	}

	@Override
	public void setX(final double x) {
		if (x == this.x[0])
			return;
		this.x[0] = (float) x;
		fireStateChanged();
	}

	@Override
	public void setY(final double y) {
		if (y == x[1])
			return;
		x[1] = (float) y;
		fireStateChanged();
	}
}