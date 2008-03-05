/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.helpers;

import java.util.Iterator;

/**
 * Peek iterator with 1-step lookahead.
 * 
 * Very similar to {@link FilterIterator}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:EnumBase.java 682 2007-08-12 21:25:04Z mrohrmoser $
 * @param <E>
 *            Iterator Element Type.
 */
public class PeekIterator<E> implements Iterator<E> {

    private final Iterator<E> base;

    private E next = null;

    private boolean start = true;

    public PeekIterator(final Iterator<E> base) {
        this.base = base;
    }

    private void doLookAhead() {
        start = false;
        if (base.hasNext())
            next = base.next();
        else
            next = null;
    }

    public boolean hasNext() {
        if (start)
            doLookAhead();
        return next != null || base.hasNext();
    }

    public E next() {
        if (start)
            doLookAhead();
        try {
            return next;
        } finally {
            doLookAhead();
        }
    }

    public E peek() {
        if (start)
            doLookAhead();
        return next;
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
}