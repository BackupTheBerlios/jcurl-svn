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
package org.jcurl.core.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import jcurl.core.Source;
import jcurl.core.TargetDiscrete;

import org.apache.commons.math.FunctionEvaluationException;

/**
 * A first, simple keyboard input class. Uses a
 * {@link org.jcurl.core.gui.RealTimePlayer}to play.
 * 
 * @see org.jcurl.demo.viewer.ViewerApp
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SimpleKeys implements KeyListener {

    private final Source src;

    private final TargetDiscrete dst;

    private final RealTimePlayer player;

    private Thread worker = null;

    public SimpleKeys(final Source src, final TargetDiscrete dst)
            throws FunctionEvaluationException {
        this.src = src;
        this.dst = dst;
        final double t0 = src.getMinT();
        player = new RealTimePlayer(t0, 1.0, src, dst);
        // push the initial state from src to dst
        dst.setPos(t0, src.getPos());
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_SPACE:
            if (worker == null || !worker.isAlive()) {
                worker = new Thread(player, player.getClass().getName());
                worker.start();
            } else {
                worker.interrupt();
                worker = null;
            }
            break;
        case KeyEvent.VK_LEFT:
            player.setTimeScale(-1);
            break;
        case KeyEvent.VK_RIGHT:
            player.setTimeScale(1);
            break;
        }
    }

    public void keyReleased(KeyEvent e) {
        ; // nop
    }

    public void keyTyped(KeyEvent e) {
        ; // nop
    }
}