/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
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
package jcurl.sim.core;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.TreeMap;

import jcurl.core.Source;

import org.apache.commons.logging.Log;
import org.apache.commons.math.FunctionEvaluationException;
import org.jcurl.core.dto.Ice;
import org.jcurl.core.dto.PositionSet;
import org.jcurl.core.dto.Rock;
import org.jcurl.core.dto.RockProps;
import org.jcurl.core.dto.RockSet;
import org.jcurl.core.dto.RockSetProps;
import org.jcurl.core.dto.SpeedSet;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.math.helpers.MathVec;
import org.jcurl.model.CollissionModel;
import org.jcurl.model.CollissionSpin;

/**
 * Abstract base class for propagation/friction models.
 * 
 * @see jcurl.sim.core.SlideStrategyTest
 * @see org.jcurl.model.CollissionModel
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class SlideStrategy extends ModelBase implements Source {

    public static final String D2T_CURL = "curl";

    public static final String D2T_TIME = "drawtotee";

    private static final Log log = JCLoggerFactory
            .getLogger(SlideStrategy.class);

    private static final float outY = -(Ice.BACK_2_TEE + RockProps.DEFAULT
            .getRadius());

    protected static double hypot(final double a, final double b) {
        return Math.sqrt(a * a + b * b);
    }

    public static SlideStrategy newInstance(final Class clz) {
        final Class parent = SlideStrategy.class;
        if (!parent.isAssignableFrom(clz))
            throw new IllegalArgumentException("Class [" + clz.getName()
                    + "] is no descendant of [" + parent.getName() + "]");
        try {
            return (SlideStrategy) clz.newInstance();
        } catch (InstantiationException e) {
            final IllegalArgumentException ex = new IllegalArgumentException();
            ex.initCause(e);
            throw ex;
        } catch (IllegalAccessException e) {
            final IllegalArgumentException ex = new IllegalArgumentException();
            ex.initCause(e);
            throw ex;
        } catch (SecurityException e) {
            final IllegalArgumentException ex = new IllegalArgumentException();
            ex.initCause(e);
            throw ex;
        }
    }

    protected static byte sgn(final double a) {
        if (a > 0)
            return 1;
        if (a < 0)
            return -1;
        return 0;
    }

    protected static double sqr(final double a) {
        return (a * a);
    }

    /**
     * Compute the time until two positions hit. Return {@link Long#MAX_VALUE}/1000
     * if never. This implementation assumes straight movement with constant
     * speed. As the real time is almost always bigger, this approach is ok only
     * for iterative methods.
     * 
     * @param a
     *            index of first rock [0-15]
     * @param ap
     *            location of first rock
     * @param av
     *            speed of first rock
     * @param b
     *            index of second rock [0-15]
     * @param bp
     *            location of second rock
     * @param bv
     *            speed of second rock
     * @return [sec] 0 if the positions "touch", see
     *         {@link CollissionModel#MaxDistSq}
     */
    protected static double timetilhit(final int a, final Rock ap,
            final Rock av, final int b, final Rock bp, final Rock bv) {
        final double RR = sqr(RockProps.DEFAULT.getRadius()
                + RockProps.DEFAULT.getRadius());
        final Point2D x = MathVec.sub(bp, ap, new Point2D.Double());
        final Point2D v = MathVec.sub(bv, av, new Point2D.Double());
        final double vx = MathVec.scal(x, v);
        if (vx >= 0)
            // the positions don't move or separate:
            return Long.MAX_VALUE / 1000;
        if (CollissionModel.MaxDistSq >= ap.distanceSq(bp)) {
            // the positions already touch
            if (log.isDebugEnabled())
                log.debug("Max=" + CollissionModel.MaxDistSq + " current="
                        + ap.distanceSq(bp));
            return 0;
        }
        final double vv = MathVec.scal(v, v);
        final double xx = MathVec.scal(x, x);
        return -(vx + Math.sqrt(sqr(vx) - vv * (xx - RR))) / vv;
    }

    static double tst_timetilhit(final int a, final Rock ap, final Rock av,
            final int b, final Rock bp, final Rock bv) {
        return timetilhit(a, ap, av, b, bp, bv);
    }

    private CollissionModel coll;

    protected double dt = 0.050;

    protected PositionSet maxPos = new PositionSet();

    protected SpeedSet maxSpeed = new SpeedSet();

    protected final PositionSet rocks = PositionSet.allHome();

    protected int rocksInMotion = 0;

    protected final SpeedSet speed = new SpeedSet();

    private double t;

    protected final double T0 = -1;

    protected double tmax = -1;

    protected double tmin = -1;

    public SlideStrategy() {
        setColl(new CollissionSpin());
        final Map tmp = new TreeMap();
        tmp.put(D2T_TIME, new DimVal(25, Dim.SEC_HOG_TEE));
        tmp.put(D2T_CURL, new DimVal(1, Dim.METER));
        init(tmp);
    }

    /**
     * Walk on until the given time. Requires
     * {@link #timetilhit(int, Rock, Rock, int, Rock, Rock)}and
     * {@link #estimateNextHit(PositionSet, SpeedSet)}&nbsp;to return 0 for
     * touching positions in motion towards each others.
     * 
     * @param time
     * @param dt
     *            intervals
     * @throws FunctionEvaluationException
     */
    protected void computeUntil(final double time, final double dt)
            throws FunctionEvaluationException {
        if (log.isDebugEnabled())
            log.debug("t=" + time + " dt=" + dt);
        // convert seconds to milliseconds and slowly approach hits
        final double fact = 0.95;
        final double dtHit = 1e-3; // time of the hit itself
        // check: is the time known already?
        while (time > tmax) {
            checkHit: for (;;) {
                // slowly approach the hit
                approach: for (;;) {
                    // check the next hit
                    double dtNextHit = this.estimateNextHit(maxPos, maxSpeed);
                    if (log.isDebugEnabled())
                        log.debug("tmax=" + tmax + " dtNextHit=" + dtNextHit);
                    if (dtNextHit == 0)
                        break approach;
                    if (dtNextHit < 0)
                        throw new IllegalStateException("dtNextHit="
                                + dtNextHit + " is in the past!");
                    dtNextHit *= fact; // slowly approach
                    if (dt < dtNextHit)
                        break checkHit;
                    // move til hit
                    int mov = move(tmax, tmax + dtNextHit, maxPos, maxSpeed);
                    if (mov != rocksInMotion)
                        set(tmax + dtNextHit, maxPos, maxSpeed, mov
                                ^ rocksInMotion);
                    else
                        tmax += dtNextHit;
                    rocksInMotion = mov;
                }
                // compute the hit
                final int hit = coll.compute(maxPos, maxSpeed);
                set(tmax + dtHit, maxPos, maxSpeed, hit);
                rocksInMotion |= hit;
            }
            // move on
            final int mov = move(tmax, tmax + dt, maxPos, maxSpeed);
            if (mov != rocksInMotion)
                set(tmax + dt, maxPos, maxSpeed, mov ^ rocksInMotion);
            else
                tmax += dt;
            rocksInMotion = mov;
        }
    }

    /**
     * Test all combinations of the given positions for upcoming collissions.
     * <p>
     * There is a little conceptual gap between the space-distance (used by
     * {@link CollissionModel#compute(PositionSet, SpeedSet)}) and the time
     * distance used here and in {@link #computeUntil(double, double)}. This is
     * because the collission engine should not need to know about rock
     * propagation and therefore cannot compute the exact time until the hit -
     * the slider on the other hand needs to know the time to propagate the
     * positions until the hit.
     * </p>
     * <p>
     * This is solved by additionally checking the distance in
     * {@link #timetilhit(int, Rock, Rock, int, Rock, Rock)}&nsbp;and returning
     * 0 if two positions are touching in the sense of
     * {@link CollissionModel#MaxDistSq}.
     * </p>
     * <p>
     * So the propagation should continue until 0 is returned here.
     * </p>
     * 
     * @see #timetilhit(int, Rock, Rock, int, Rock, Rock)
     * @param pos
     * @param speed
     * @return seconds until the next hit or 0 if two positions "touch" (see above
     *         and {@link #timetilhit(int, Rock, Rock, int, Rock, Rock)}).
     * @see #timetilhit(int, Rock, Rock, int, Rock, Rock)
     */
    public double estimateNextHit(final PositionSet pos, final SpeedSet speed) {
        if (log.isDebugEnabled())
            log.debug("estimateNextHit");
        double t = Long.MAX_VALUE / 1000;
        // test combination of all positions:
        for (int a = 0; a < PositionSet.ROCKS_PER_SET; a++) {
            final Rock ap = pos.getRock(a);
            final Rock av = speed.getRock(a);
            for (int b = 0; b < a; b++) {
                final Rock bp = pos.getRock(b);
                final Rock bv = speed.getRock(b);
                final double t1 = timetilhit(a, ap, av, b, bp, bv);
                if (t1 >= 0 && t1 < t)
                    t = t1;
            }
        }
        return t;
    }

    protected abstract RockSet getC(int c, double time, RockSet rocks)
            throws FunctionEvaluationException;

    public CollissionModel getColl() {
        return coll;
    }

    /**
     * Guess the initial speed.
     * 
     * @param y0
     *            start position (y) [meter]
     * @param Trun
     *            from Hog to Hog. [seconds] see ./doc/eiszeit.tex for details.
     * @return [meter/sec]
     */
    public abstract double getInitialSpeed(final double y0, final double Trun);

    /**
     * Not supported.
     * 
     * @return not supported
     */
    public double getMaxT() {
        throw new UnsupportedOperationException("Not supported");
    }

    public double getMinT() {
        return tmin;
    }

    /**
     * Query the friction.
     * 
     * @return friction coefficient
     */
    public abstract double getMu();

    public final PositionSet getPos() {
        return rocks;
    }

    public final SpeedSet getSpeed() {
        return speed;
    }

    public double getT() {
        return t;
    }

    public void init(final Map props) {
        props.clear();
        props.putAll(props);
        DimVal time = (DimVal) getProp(D2T_TIME);
        if (time == null)
            time = new DimVal(25, Dim.SEC_HOG_TEE);
        if (!Dim.SEC_HOG_TEE.equals(time.dim))
            throw new IllegalArgumentException(
                    "Draw to tee time must be seconds hog/tee");
        DimVal curl = (DimVal) getProp(D2T_CURL);
        if (curl == null)
            curl = new DimVal(1, Dim.METER);
        if (!curl.dim.isLength())
            throw new IllegalArgumentException(
                    "Draw to tee curl must be a length (meter/feet/...)");
        setDraw2Tee(time.val, curl.val);
    }

    /**
     * Checks if the rock is out of play.
     * 
     * @param x
     *            rock location
     * @param v
     *            rock speed
     * @return <code>true</code> the rock is out
     */
    protected boolean isOut(final Rock x, final Rock v) {
        if (x.getX() > Ice.SIDE_2_CENTER || x.getX() < -Ice.SIDE_2_CENTER)
            return true;
        if (x.getY() < outY)
            return true;
        return false;
    }

    /**
     * Move one single rock without any checks.
     * 
     * @param t0
     * @param t1
     * @param idx
     *            rock index
     * @param pos
     * @param speed
     * @return <code>true</code> the rock moves at t1
     */
    protected abstract boolean move(final double t0, final double t1, int idx,
            final Rock pos, final Rock speed)
            throws FunctionEvaluationException;

    /**
     * Generic mover. calls
     * {@link SlideStrategy#move(double, double, int, Rock, Rock)}for each rock
     * and checks if the rock is still in play afterwards.
     * 
     * @param t0
     *            [sec] start time
     * @param t1
     *            [sec] end time
     * @param pos
     *            positions
     * @param speed
     *            velocities
     * @return bitmask of the positions in motion at t1
     */
    protected int move(final double t0, final double t1, final PositionSet pos,
            final SpeedSet speed) throws FunctionEvaluationException {
        if (log.isDebugEnabled())
            log.debug("t0=" + t0 + " t1=" + t1);
        int ret = 0;
        for (int i = PositionSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            final Rock x = pos.getRock(i);
            final Rock v = speed.getRock(i);
            if (move(t0, t1, i, x, v)) {
                // only check moving positions.
                if (isOut(x, v)) {
                    Ice.setOut(x, (i % 2) == 0, i / 2);
                    v.setLocation(0, 0, 0);
                } else
                    ret |= 1 << i;
            }
        }
        this.rocks.notifyChange();
        this.speed.notifyChange();
        return ret;
    }

    public void reset(PositionSet startPos, SpeedSet startSpeed,
            RockSetProps props) throws FunctionEvaluationException {
        tmin = tmax = T0;
        RockSet.copy(startPos, maxPos);
        RockSet.copy(startSpeed, maxSpeed);
        set(0, startPos, startSpeed, PositionSet.ALL_MASK);
        rocksInMotion = 0;
        for (int i = PositionSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            final Rock v = startSpeed.getRock(i);
            if (v.getX() != 0 || v.getY() != 0)
                rocksInMotion |= 1 << i;
        }
    }

    /**
     * Add a discontinuity.
     * 
     * @param t0
     *            [msec]
     * @param pos
     * @param speed
     * @param discontinuous
     *            bitmask of the discontinuous positions
     */
    protected abstract void set(final double t0, final PositionSet pos,
            final SpeedSet speed, final int discontinuous)
            throws FunctionEvaluationException;

    public void setColl(CollissionModel coll) {
        if (coll == null) {
            if (this.coll == null)
                throw new IllegalArgumentException(
                        "Collission strategy undefined.");
        } else
            this.coll = coll;
    }

    /**
     * Set the draw-to-T-time and curl.
     * 
     * @param time
     *            [seconds]
     * @param curl
     *            [meter]
     */
    public void setDraw2Tee(final double time, final double curl) {
        props.put(D2T_TIME, new DimVal(time, Dim.SEC_HOG_TEE));
        props.put(D2T_CURL, new DimVal(curl, Dim.METER));
    }

    public void setT(double t) throws FunctionEvaluationException {
        computeUntil(getT(), dt);
        getC(0, getT(), rocks);
        getC(1, getT(), speed);
    }
}