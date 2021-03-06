/*
 * jcurl curling simulation framework 
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
package org.jcurl.core.math;

import junit.framework.TestCase;

import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class FunctionBridgeTest extends TestCase {

    /**
     * Enable usage of {@link UnivariateRealFunction}s without implanting the
     * dependency into the jcurl core.
     */
    public static abstract class FunctionBridge implements R1R1Function,
            DifferentiableUnivariateRealFunction {
        private static class Curl2Jakarta extends FunctionBridge {
            private final int diff;

            private final R1R1Function root;

            public Curl2Jakarta(R1R1Function f, int diff) {
                this.root = f;
                this.diff = diff;
            }

            public double at(double x) throws MathException {
                return root.at(x, diff);
            }

            public double at(double x, int derivative) throws MathException {
                return root.at(x, diff + derivative);
            }

            public UnivariateRealFunction derivative() {
                return new Curl2Jakarta(root, diff + 1);
            }

            public double value(double x)
                    throws org.apache.commons.math.FunctionEvaluationException {
                try {
                    return root.at(x, diff);
                } catch (MathException e) {
                    final org.apache.commons.math.FunctionEvaluationException fe = new org.apache.commons.math.FunctionEvaluationException(
                            x, e.getMessage());
                    fe.initCause(e);
                    throw fe;
                }
            }
        }

        private static class Jakarta2Curl extends FunctionBridge {
            private final UnivariateRealFunction base;

            private Jakarta2Curl deriv = null;

            public Jakarta2Curl(UnivariateRealFunction f) {
                this.base = f;
            }

            public double at(double x) throws MathException {
                try {
                    return base.value(x);
                } catch (org.apache.commons.math.MathException e) {
                    final MathException fe = new org.jcurl.core.math.FunctionEvaluationException();
                    fe.initCause(e);
                    throw fe;
                }
            }

            public double at(double x, int derivative) throws MathException {
                if (derivative < 0)
                    throw new DifferentiationException();
                if (derivative == 0)
                    return at(x);
                if (deriv == null)
                    deriv = (Jakarta2Curl) derivative();
                if (deriv == null)
                    throw new DifferentiationException();
                return deriv.at(x, derivative - 1);
            }

            public UnivariateRealFunction derivative() {
                if (deriv == null
                        && base instanceof DifferentiableUnivariateRealFunction)
                    deriv = new Jakarta2Curl(
                            ((DifferentiableUnivariateRealFunction) base)
                                    .derivative());
                return deriv;
            }

            public double value(double x)
                    throws org.apache.commons.math.FunctionEvaluationException {
                return base.value(x);
            }
        }

        public static FunctionBridge newInstance(Object o) {
            if (o instanceof FunctionBridge)
                return (FunctionBridge) o;
            if (o instanceof R1R1Function)
                return new Curl2Jakarta((R1R1Function) o, 0);
            if (o instanceof UnivariateRealFunction)
                return new Jakarta2Curl((UnivariateRealFunction) o);
            return null;
        }
    }

    public void testPoly()
            throws org.apache.commons.math.FunctionEvaluationException,
            MathException {
        final PolynomialFunction p0 = new PolynomialFunction(new double[] { 1,
                0.1, 0.01, 0.001 });
        final Polynome p1 = new PolynomeImpl(p0.getCoefficients());
        final FunctionBridge b0 = FunctionBridge.newInstance(p0);
        final FunctionBridge b1 = FunctionBridge.newInstance(p1);
        for (double x = -100; x < 100; x += 0.9) {
            assertEquals(b0.at(x), b0.value(x), 1e-9);
            assertEquals(b1.at(x), b1.value(x), 1e-9);
            assertEquals(b0.at(x), b1.at(x), 1e-9);
            for (int i = 0; i < 100; i++) {
                assertEquals(b0.at(x, i), b1.at(x, i), 1e-9);
            }
        }
    }
}
