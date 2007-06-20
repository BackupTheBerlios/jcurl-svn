/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

/**
 * Read and write JCurl data.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public interface JCurlSerializer {

    public static interface Payload {

        public Map<String, Object> getAnnotations();

        public TrajectorySet[] getTrajectories();

    };

    public Payload read(InputStream src, Payload dst);

    public Payload read(InputStream src, String name, Payload dst)
            throws IOException;

    public Payload read(Reader src, Payload dst);

    public Payload read(String s);

    public Payload read(URL src, Payload dst) throws IOException;

    /**
     * {@link Payload} factory.
     * 
     * @param annotations
     * @param trajectory
     * @return the payload
     */
    public Payload wrap(Map<String, Object> annotations,
            TrajectorySet trajectory);

    /**
     * {@link Payload} factory.
     * 
     * @param annotations
     * @param trajectories
     * @return the payload
     */
    public Payload wrap(Map<String, Object> annotations,
            TrajectorySet[] trajectories);

    public String write(Payload src);

    public void write(Payload src, File dst) throws IOException;

    public void write(Payload src, OutputStream dst);

    public void write(Payload src, Writer dst);

}