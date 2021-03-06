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
package jcurl.core.gui;

import java.awt.Graphics2D;

import org.jcurl.core.PositionSet;

/**
 * A simple display for rock locations. Delegates rock- and ice painting.
 * 
 * @see jcurl.core.gui.RockPainter
 * @see jcurl.core.gui.IcePainter
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockLocationDisplay extends RockLocationDisplayBase {

    private static final long serialVersionUID = 146935083360365782L;

//    private static final ULogger log = JCLoggerFactory
//            .getLogger(RockLocationDisplay.class);

    private final IcePainter iceP;

    private final RockPainter rockP;

    public RockLocationDisplay(final PositionSet rocks, final Zoomer zoom,
            final IcePainter iceP, final RockPainter rockP) {
        super(rocks, zoom);
        this.iceP = iceP == null ? new IcePainter() : iceP;
        this.rockP = rockP == null ? new RockPainter() : rockP;
    }

    public RockLocationDisplay(final Zoomer zoom) {
        this(null, zoom, null, null);
    }

    protected void paintIceDC(final Graphics2D g2) {
        // background
        g2.setPaint(iceP.color.backGround);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.transform(wc_mat);
        // Ice
        iceP.paintIceWC(g2);
    }

    protected void paintRockRC(final Graphics2D g, final boolean isDark,
            final int idx) {
        rockP.paintRockRC(g, isDark, idx);
    }
}