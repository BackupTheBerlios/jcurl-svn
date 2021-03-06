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

package org.jcurl.zui.scenario;

import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.sun.scenario.scenegraph.JSGPanel;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGTransform;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class IceDemo {
	public static void main(final String[] args) {
		final JFrame f = new JFrame("Demo");
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		final JSGPanel panel = new JSGPanel();

		final AffineTransform at = new AffineTransform();
		at.translate(200, 600);
		at.scale(50, 50);

		final SGNode ice = new SGIceFactory.Fancy().newInstance(null);
		panel.setScene(SGTransform.createAffine(at, ice));

		f.add(panel);
		f.setSize(400, 800);
		f.setVisible(true);
	}
}
