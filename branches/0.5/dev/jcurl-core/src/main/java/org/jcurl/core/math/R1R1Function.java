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
package org.jcurl.core.math;

import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;

/**
 * Functions <code>f : R -&gt; R</code>.
 * 
 * TODO extends DifferentiableUnivariateRealFunction
 * 
 * @see DifferentiableUnivariateRealFunction
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:R1R1Function.java 350 2006-08-16 18:41:58Z mrohrmoser $
 */
public interface R1R1Function {

    public abstract double at(double x) throws MathException;

    public abstract double at(double x, int derivative) throws MathException;
}
