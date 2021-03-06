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
package org.jcurl.math;

import org.jcurl.core.api.CollissionDetector;
import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.TestBase;
import org.jcurl.core.impl.CurveRockAnalytic;

public class Distance2DSqTest extends TestBase {

	static CurveRock createPoint2D(final double x, final double y) {
		return new CurveRock() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6310106969704482207L;

			@Override
			public double at(final double t, final int derivative,
					final int component) {
				if (derivative == 0) {
					if (component == 0)
						return x;
					if (component == 1)
						return y;
				}
				return 0;
			}
		};
	}

	static CurveRock createPoly(final double[] c) {
		return new CurveRock() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4095623981968751102L;
			private final Polynome p = new Polynome(c);

			@Override
			public double at(final double t, final int derivative,
					final int component) {
				if (component == 0 && derivative == 0)
					return t;
				if (component == 1)
					return p.at(t, derivative);
				return 0;
			}
		};
	}

	public void testPointStraightLine() {
		final Distance2DSq d = new Distance2DSq(createPoint2D(2, 1),
				createPoly(new double[] { -1, 0.5 }), CollissionDetector.RR2);
		assertEquals(2.811956073217295, Math.sqrt(d.at(0)));
		assertEquals(1.7768221513994102, Math.sqrt(d.at(1)));
		assertEquals(0.9524163783260075, Math.sqrt(d.at(2)));
		assertEquals(1.075684413619361, Math.sqrt(d.at(3)));
		assertEquals(Double.NaN, NewtonSimpleSolver.computeNewtonZero(d, 0, 0,
				0, 5));

		assertEquals(Double.NaN, NewtonSimpleSolver.computeNewtonZero(
				new Distance2DSq(createPoint2D(2, 2), createPoly(new double[] {
						0, 1 }), CollissionDetector.RR2), 0, 0, 0, 5));
	}

	public void testPolyHit() {
		final CurveRock c0 = createPoint2D(2, 1);
		final CurveRock c1 = new CurveRockAnalytic(new Polynome[] {
				new Polynome(new double[] { 0, 1 }),
				new Polynome(new double[] { 1 }),
				new Polynome(new double[] { 0, 0.5 }) });
		{
			final R1R1Function d = new Distance2DSq(c0, c1, 0);
			for (int i = 0; i < 3000; i++) {
				final double t = i * 1e-3;
				final Rock x0 = c0.at(t, 0, new RockDouble());
				final Rock x1 = c1.at(t, 0, new RockDouble());
				assertEquals(Double.toString(t), x0.p().distanceSq(x1.p()), d
						.at(t, 0), 1e-9);
			}
			assertEquals("", 2, NewtonSimpleSolver.computeNewtonZero(d, 0, 0,
					0, 5), 1e-9);
		}
		{
			final R1R1Function d = new Distance2DSq(c0, c1, 1);
			for (int i = 0; i < 3000; i++) {
				final double t = i * 1e-3;
				final Rock x0 = c0.at(t, 0, new RockDouble());
				final Rock x1 = c1.at(t, 0, new RockDouble());
				assertEquals(Double.toString(t), x0.p().distanceSq(x1.p()) - 1,
						d.at(t, 0), 1e-9);
			}
			assertEquals("", 1, NewtonSimpleSolver.computeNewtonZero(d, 0, 0,
					0, 5), 1e-9);
		}
		{
			final double RR2 = MathVec.sqr(RockProps.DEFAULT.getRadius() * 2);
			final R1R1Function d = new Distance2DSq(c0, c1, RR2);
			for (int i = 0; i < 3000; i++) {
				final double t = i * 1e-3;
				final Rock x0 = c0.at(t, 0, new RockDouble());
				final Rock x1 = c1.at(t, 0, new RockDouble());
				assertEquals(Double.toString(t), x0.p().distanceSq(x1.p())
						- RR2, d.at(t, 0), 1e-9);
			}
			assertEquals("", 1.6951999962329865, NewtonSimpleSolver
					.computeNewtonZero(d, 0, 0, 0, 5), 1e-9);
		}
	}

	public void testPolyMiss() {
		final CurveRock c0 = createPoint2D(2, 0.5);
		final CurveRock c1 = new CurveRockAnalytic(new Polynome[] {
				new Polynome(new double[] { 0, 1 }),
				new Polynome(new double[] { 1 }),
				new Polynome(new double[] { 0, 0.5 }) });
		{
			final R1R1Function d = new Distance2DSq(c0, c1, 0);
			for (int i = 0; i < 3000; i++) {
				final double t = i * 1e-3;
				final Rock x0 = c0.at(t, 0, new RockDouble());
				final Rock x1 = c1.at(t, 0, new RockDouble());
				assertEquals(Double.toString(t), x0.p().distanceSq(x1.p()), d
						.at(t, 0), 1e-9);
			}
			assertEquals("", Double.NaN, NewtonSimpleSolver.computeNewtonZero(
					d, 0, 0, 0, 5), 1e-9);
		}
		{
			final double RR2 = MathVec.sqr(RockProps.DEFAULT.getRadius() * 2);
			final R1R1Function d = new Distance2DSq(c0, c1, RR2);
			for (int i = 0; i < 3000; i++) {
				final double t = i * 1e-3;
				final Rock x0 = c0.at(t, 0, new RockDouble());
				final Rock x1 = c1.at(t, 0, new RockDouble());
				assertEquals(Double.toString(t), x0.p().distanceSq(x1.p())
						- RR2, d.at(t, 0), 1e-9);
			}
			assertEquals("", Double.NaN, NewtonSimpleSolver.computeNewtonZero(
					d, 0, 0, 0, 5), 1e-9);
		}
	}
}
