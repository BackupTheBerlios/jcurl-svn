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
package org.jcurl.demo.tactics;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.Curler;
import org.jcurl.core.api.IChangeSupport;
import org.jcurl.core.api.IPropertyChangeSupport;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.MessageExecutor.ForkableFixed;
import org.jcurl.core.ui.MessageExecutor.Single;
import org.jcurl.math.MathVec;

class BroomSpeedMediator implements PropertyChangeListener, ChangeListener {
	private static final Log log = JCLoggerFactory
			.getLogger(BroomSpeedMediator.class);

	private static <R extends RockType> void swap(final RockSet<R> r,
			final int a, final int b) {
		final Rock<R> tmp = new RockDouble<R>(r.getRock(a));
		r.getRock(a).setLocation(r.getRock(b));
		r.getRock(b).setLocation(tmp);
	}

	private BroomPromptModel broom;
	private Curler curler;
	private RockSet<Pos> position;
	private RockSet<Vel> speed;

	/**
	 * Initialpos, speed or curler have changed.
	 */
	private final ForkableFixed<Single> updateBroom = new ForkableFixed<Single>() {
		public void run() {
			if (position == null || speed == null || broom == null)
				return;
			// Compute Broom Location
			final Point2D x = position.getRock(broom.getIdx16()).p();
			final Rock<Vel> v = speed.getRock(broom.getIdx16());
			final Point2D b = broom.getBroom();
			final Point2D b2 = new Point2D.Double((b.getY() - x.getY())
					* v.getX() / v.getY(), b.getY());
			log.info(b2);
			broom.setBroom(b2);

			// Compute Split Time
			// broom.getSplitTimeMillis().setValue((int)(1000 *
			// curler.computeIntervalTime(v0));
			// TODO

			// Compute Handle
			broom.setOutTurn(v.getA() >= 0);
		}
	};

	private void add(final IChangeSupport l) {
		if (l != null)
			l.addChangeListener(this);
	}

	private void add(final IPropertyChangeSupport l) {
		if (l != null)
			l.addPropertyChangeListener(this);
	}

	public BroomPromptModel getBroom() {
		return broom;
	}

	public Curler getCurler() {
		return curler;
	}

	public RockSet<Pos> getPosition() {
		return position;
	}

	public RockSet<Vel> getSpeed() {
		return speed;
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		final Object src = evt.getSource();
		if (src instanceof BroomPromptModel) {
			if ("idx16".equals(evt.getPropertyName()))
				updateIndex((Integer) evt.getOldValue(), (Integer) evt
						.getNewValue());
			else
				updateSpeed();
		} else if (src instanceof RockSet)
			updateBroom.fork();
		else if (src instanceof Curler)
			updateSpeed();
		else
			log.warn("unconsumed source: " + src.getClass().getName());
	}

	private void remove(final IChangeSupport l) {
		if (l != null)
			l.removeChangeListener(this);
	}

	private void remove(final IPropertyChangeSupport l) {
		if (l != null)
			l.removePropertyChangeListener(this);
	}

	public void setBroom(final BroomPromptModel broo) {
		if (broo != null && broo.getSplitTimeMillis() != null)
			broo.getSplitTimeMillis().removeChangeListener(this);
		remove(broom);
		add(broom = broo);
		if (broom != null && broom.getSplitTimeMillis() != null)
			broom.getSplitTimeMillis().addChangeListener(this);
		updateSpeed();
	}

	public void setCurler(final Curler curler) {
		// remove(this.curler);
		// add(this.curler = curler);
		this.curler = curler;
		updateSpeed();
	}

	public void setPosition(final RockSet<Pos> position) {
		remove(this.position);
		add(this.position = position);
		updateBroom.fork();
	}

	public void setSpeed(final RockSet<Vel> speed) {
		remove(this.speed);
		add(this.speed = speed);
		updateBroom.fork();
	}

	public void stateChanged(final ChangeEvent e) {
		final Object src = e.getSource();
		if (src instanceof BoundedRangeModel)
			updateSpeed();
		if (src instanceof RockSet)
			updateBroom.fork();
		else
			log.warn("unconsumed source: " + src.getClass().getName());
	}

	/**
	 * Active Rock Number has changed.
	 */
	public void updateIndex(final Integer oldV, final Integer newV) {
		if (oldV == null || newV == null)
			return;
		new ForkableFixed<Single>() {
			public void run() {
				final int a = oldV.intValue();
				final int b = newV.intValue();
				swap(position, a, b);
				swap(speed, a, b);
				// TODO Better call the CurveManager to update it's curves.
				position.fireStateChanged();
				// TODO Better call the CurveManager to update it's curves.
				speed.fireStateChanged();
				// TODO Update the BroomPrompt
			}
		}.fork();
	}

	/**
	 * Broom properties have changed.
	 */
	public void updateSpeed() {
		log.info(broom);
		if (position == null || curler == null || broom == null)
			return;
		new ForkableFixed<Single>() {
			public void run() {
				// set to initial pos
				final Rock start = new RockDouble(0, IceSize.FAR_HACK_2_TEE,
						Math.PI);
				position.getRock(broom.getIdx16()).setLocation(start);
				position.fireStateChanged();
				// Compute direction
				MathVec.sub(broom.getBroom(), start.p(), start.p());
				MathVec.norm(start.p(), start.p());
				// Compute initial Speed
				final double v0 = curler.computeHackSpeed(broom
						.getSplitTimeMillis().getValue() * 1e-3, broom
						.getBroom());
				MathVec.mult(v0, start.p(), start.p());
				// Handle
				start.setA((broom.getOutTurn() ? 1 : -1) * 0.25);
				speed.getRock(broom.getIdx16()).setLocation(start);
				// TODO Better call the CurveManager to update it's curves.
				speed.fireStateChanged();
			}
		}.fork();
	}
}