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
package org.jcurl.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.jcurl.core.io.JCurlSerializer.Engine;

public class JDKSerializer implements Engine {

	public IONode read(final InputStream src) throws IOException {
		try {
			return (IONode) new ObjectInputStream(src).readObject();
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	public void write(final IONode src, final OutputStream dst)
			throws IOException {
		src.annotations().put(IONode.CreatedByUser,
				System.getProperty("user.name"));
		new ObjectOutputStream(dst).writeObject(src);
		dst.flush();
	}
}