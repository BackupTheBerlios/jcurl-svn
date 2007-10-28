/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.core.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.CurveRockAnalytic;
import org.jcurl.core.base.CurveStill;
import org.jcurl.core.base.CurveStore;
import org.jcurl.core.base.StopDetector;
import org.jcurl.core.helpers.PropertyChangeSupport;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.CurveCombined;
import org.jcurl.math.R1RNFunction;

/**
 * Manage rock trajectory segments for a complete set of rocks over time.
 * <p>
 * Supports smart stop detection.
 * </p>
 * 
 * @see CurveCombined
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CurveStoreImpl.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class CurveStoreImpl implements Serializable, CurveStore {

    private static final byte DIM = 3;
    private static final Log log = JCLoggerFactory
            .getLogger(CurveStoreImpl.class);
    private static final long serialVersionUID = -1485170570756670720L;
    private final CurveCombined[] curve;
    private final transient PropertyChangeSupport pcs = new PropertyChangeSupport(
            this);
    private final StopDetector stopper;

    /**
     * 
     * @param stopper
     *            May be <code>null</code> for no stop detection.
     * @param capacity
     */
    public CurveStoreImpl(final StopDetector stopper, final int capacity) {
        this.stopper = stopper;
        curve = new CurveCombined[capacity];
        for (int i = curve.length - 1; i >= 0; i--)
            curve[i] = new CurveCombined(DIM);
    }

    public void add(final int i, final double t, final R1RNFunction f,
            final double tstop) {
        if (log.isDebugEnabled())
            log.debug(i + "  t=" + t + " " + f);
        // unwrap if possible:
        if (f instanceof CurveRockAnalytic)
            curve[i].add(t, ((CurveRockAnalytic) f).getCurve(), true);
        else
            curve[i].add(t, f, true);
        // Stop detection. Either here or in each slider?
        if (stopper != null) {
            final double stop = stopper.compute(f, 0, tstop - t);
            if (stop > 0 && !Double.isNaN(stop)) {
                // TUNE save one instanciation?
                final double[] tmp = { 0, 0, 0 };
                curve[i].add(stop, CurveStill.newInstance(f.at(0, stop, tmp)),
                        true);
                if (log.isDebugEnabled())
                    log.debug(i + "  t=" + t + " Still");
            }
        }
        fireIndexedPropertyChange("curve", i, null, curve[i]);
    }

    public void addPropertyChangeListener(final PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void addPropertyChangeListener(final String property,
            final PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(property, pcl);
    }

    public void fireIndexedPropertyChange(final String propertyName,
            final int index, final boolean oldValue, final boolean newValue) {
        pcs.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    public void fireIndexedPropertyChange(final String propertyName,
            final int index, final int oldValue, final int newValue) {
        pcs.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    public void fireIndexedPropertyChange(final String property,
            final int index, final Object old, final Object neo) {
        if (log.isDebugEnabled())
            log.debug(property + " " + pcs.hasListeners(property));
        pcs.fireIndexedPropertyChange(property, index, old, neo);
    }

    public void firePropertyChange(final PropertyChangeEvent event) {
        pcs.firePropertyChange(event);
    }

    public void firePropertyChange(final String property, final boolean old,
            final boolean neo) {
        pcs.firePropertyChange(property, old, neo);
    }

    public void firePropertyChange(final String property, final double old,
            final double neo) {
        pcs.firePropertyChange(property, old, neo);
    }

    public void firePropertyChange(final String property, final int old,
            final int neo) {
        pcs.firePropertyChange(property, old, neo);
    }

    public void firePropertyChange(final String property, final Object old,
            final Object neo) {
        pcs.firePropertyChange(property, old, neo);
    }

    public R1RNFunction getCurve(final int i) {
        return curve[i];
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners();
    }

    public PropertyChangeListener[] getPropertyChangeListeners(
            final String property) {
        return pcs.getPropertyChangeListeners(property);
    }

    public boolean hasListeners(final String property) {
        return pcs.hasListeners(property);
    }

    public Iterator<Iterable<Entry<Double, R1RNFunction>>> iterator() {
        return new Iterator<Iterable<Entry<Double, R1RNFunction>>>() {
            int current = 0;

            public boolean hasNext() {
                return current < curve.length;
            }

            public Iterable<Entry<Double, R1RNFunction>> next() {
                return curve[current++];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public Iterator<Entry<Double, R1RNFunction>> iterator(final int i) {
        return curve[i].iterator();
    }

    public void removePropertyChangeListener(final PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(final String property,
            final PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(property, pcl);
    }

    public void reset(final int i) {
        curve[i].clear();
        fireIndexedPropertyChange("curve", i, curve[i], curve[i]);
    }
}
