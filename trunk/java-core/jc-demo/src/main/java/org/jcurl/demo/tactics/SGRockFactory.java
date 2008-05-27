/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.jcurl.core.api.Factory;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.ui.IceShapes;
import org.jcurl.zui.piccolo.PRockNode;

import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGShape;
import com.sun.scenario.scenegraph.SGText;
import com.sun.scenario.scenegraph.SGTransform;
import com.sun.scenario.scenegraph.SGAbstractShape.Mode;
import com.sun.scenario.scenegraph.SGTransform.Affine;

/**
 * Creates a pickable node displaying one rock, assuming a <b>RIGHT HANDED</b>
 * parent coordinate system.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PRockFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public abstract class SGRockFactory implements Factory {

	public static class Fancy extends SGRockFactory {

		protected static final IceShapes.RockColors colors = new IceShapes.RockColors();
		private final int alpha;

		public Fancy(final int alpha) {
			this.alpha = alpha;
		}

		@Override
		public Affine newInstance(final int i8, final boolean isDark,
				final Rock<Pos> rock) {
			final SGGroup r = new SGGroup();
			r.add(node(IceShapes.ROCK_OUTER, IceShapes.alpha(colors.granite,
					alpha), null, null));
			r.add(node(IceShapes.ROCK_INNER, IceShapes.alpha(
					isDark ? colors.dark : colors.light, alpha), null, null));
			{
				final SGText t = new SGText();
				t.setAntialiasingHint(RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				t.setText(IceShapes.ROCK_LABELS[i8]);
				t.setFont(IceShapes.ROCK_LABEL);
				t.setDrawPaint(IceShapes.alpha(colors.label, alpha));

				// Make coord-sys left-handed again, as the ice is assumed to be
				// right-handed:
				final AffineTransform tr = AffineTransform.getScaleInstance(1,
						-1);
				final Rectangle2D tb = t.getBounds();
				tr.translate(-0.015 * tb.getWidth(), 0.015 * tb.getHeight());
				tr.scale(1.0 / 5.0, 1.0 / 5.0); // implicit FontSize
				// t.setPickable(false);
				r.add(SGTransform.createAffine(tr, t));
			}
			// r.setChildrenPickable(false);
			// r.setPickable(true);
			// r.getChild(0).setPickable(true);
			return SGTransform.createAffine(new AffineTransform(), r);
		}
	}

	public static class Simple extends Fancy {
		private static final Stroke stroke = new BasicStroke(0.010F);

		public Simple() {
			super(128);
		}

		@Override
		public Affine newInstance(final int i8, final boolean isDark,
				final Rock<Pos> rock) {
			// final PRockNode r = new PRockNode(i8, isDark, rock);
			// // fill to also make the body, not only the edge
			// // pickable:
			// r.addChild(node(outer, isDark ? colors.dark : colors.light,
			// stroke,
			// Color.BLACK));
			// r.getChild(0).setPickable(true);
			// return r;
			throw new NotImplementedYetException();
		}
	}

	static final double EPSILON = 1e-11;

	protected static SGShape node(final Shape s, final Paint p,
			final Stroke str, final Paint sp) {
		final SGShape n = new SGShape();
		n.setAntialiasingHint(RenderingHints.VALUE_ANTIALIAS_ON);
		n.setShape(s);
		if (sp != null)
			n.setDrawPaint(sp);
		if (p != null)
			n.setFillPaint(p);
		// n.setPickable(false);
		n.setMode(Mode.FILL);
		return n;
	}

	protected static boolean sync(final Rock<Pos> src, final PRockNode dst) {
		// check if it's changed either location or angle:
		if (src.p().distanceSq(dst.getOffset()) < EPSILON
				&& Math.abs(src.getA() - dst.getRotation()) < EPSILON)
			return false;
		if (true)
			dst.setTransform(src.getAffineTransform());
		else {
			dst.setRotation(src.getA());
			dst.setOffset(src.getX(), src.getY());
		}
		dst.invalidatePaint();
		// Why is this necessary?
		dst.repaint();
		return true;
	}

	public abstract Affine newInstance(final int i8, final boolean isDark,
			Rock<Pos> rock);

	public Affine newInstance(final int i16, final Rock<Pos> rock) {
		return newInstance(RockSet.toIdx8(i16), RockSet.isDark(i16), rock);
	}
}
