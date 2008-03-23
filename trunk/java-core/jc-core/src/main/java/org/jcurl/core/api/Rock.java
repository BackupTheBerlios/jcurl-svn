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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

import javax.swing.event.ChangeListener;

import org.jcurl.core.helpers.Service;

/**
 * Base class for rock information (either location or speed). The 3rd component (<code>a</code>)
 * is the handle angle in radians.
 * <h3>Why is this type not generic, e.g. Rock<T extends Number></h3>
 * <p>
 * Yes, this would be preferable, especially for a neat integration into <a
 * href="http://www.jscience.org">JScience</a> calculus.
 * </p>
 * <p>
 * But as {@link #setLocation(double, double)} &amp; co must be provided to
 * satisfy the {@link Point2D} (settable via mouse interaction on a panel) there
 * MUST be a way to convert {@link java.lang.Double}s to <code>T</code>s.
 * {@link Service} factories (@link Double}-&gt;<code>T</code>) could be a
 * solution.
 * </p>
 * <p>
 * Looks like this absolutely central class is subject to major refactoring.
 * </p>
 * <h2>Possible ways out</h2>
 * <ul>
 * <li>don't extend {@link Point2D} but use a wrapper to communicate with
 * displays.</li>
 * </ul>
 * 
 * @see org.jcurl.core.api.RockSet
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Rock.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class Rock extends Point2D implements IChangeSupport,
		Cloneable, Serializable {
	/** Utility field used by bound properties. */
	protected final transient ChangeSupport change = new ChangeSupport(this);

	private transient volatile boolean dirty = true;

	private transient volatile AffineTransform trafo;

	public void addChangeListener(final ChangeListener l) {
		change.addChangeListener(l);
	}

	@Override
	public abstract Object clone();

	@Override
	public final boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof Rock))
			return false;
		return equals((Rock) obj);
	}

	private boolean equals(final Rock b) {
		if (this == b)
			return true;
		if (b == null)
			return false;
		return getX() == b.getX() && getY() == b.getY() && getA() == b.getA();
	}

	protected void fireStateChanged() {
		dirty = true;
		change.fireStateChanged();
	}

	public abstract double getA();

	public AffineTransform getAffineTransform() {
		if (trafo != null && !dirty)
			return trafo;
		synchronized (this) {
			if (trafo == null)
				trafo = new AffineTransform();
			else
				trafo.setToIdentity();
			trafo.translate(getX(), getY());
			// TUNE jdk 1.6 brings a non-trigonometric rotate!
			trafo.rotate(getA());
			dirty = false;
		}
		return trafo;
	}

	public ChangeListener[] getChangeListeners() {
		return change.getChangeListeners();
	}

	/**
	 * Convenience method to check if zero or not.
	 * 
	 * @return whether x or y are non-zero
	 */
	public abstract boolean isNotZero();

	public void removeChangeListener(final ChangeListener l) {
		change.removeChangeListener(l);
	}

	public abstract void setA(double a);

	public abstract void setLocation(double x, double y, double a);

	public abstract void setLocation(double[] l);

	public void setLocation(final Rock r) {
		this.setLocation(r.getX(), r.getY(), r.getA());
	}

	public abstract void setX(double x);

	public abstract void setY(double y);

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append('[');
		buf.append(getX());
		buf.append(", ");
		buf.append(getY());
		buf.append(", ");
		buf.append(getA());
		buf.append(']');
		return buf.toString();
	}
}