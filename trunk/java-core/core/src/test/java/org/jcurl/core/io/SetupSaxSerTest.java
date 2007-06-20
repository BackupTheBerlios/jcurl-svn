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
package org.jcurl.core.io;

import java.io.StringWriter;
import java.util.Map;

import junit.framework.TestCase;

import org.jcurl.core.base.PropModel;
import org.jcurl.core.base.PropModelHelper;
import org.jcurl.core.base.PropModelImpl;
import org.jcurl.core.helpers.Unit;
import org.jcurl.core.helpers.Measure;
import org.jcurl.core.model.CollissionSimple;
import org.jcurl.core.model.CurlerNoCurl;
import org.xml.sax.SAXException;

public class SetupSaxSerTest extends TestCase {

    public void testSlider() throws SAXException {
        final StringWriter w = new StringWriter();
        SetupSaxSer s = new SetupSaxSer(w);
        s.internal(new CurlerNoCurl(23, 0));
        w.flush();
        assertEquals(
                "<model engine=\"org.jcurl.core.model.CurlerNoCurl\"><param name=\"drawToTeeTime\" val=\"23.0\" dim=\"s\"></param><param name=\"drawToTeeCurl\" val=\"0.0\" dim=\"m\"></param></model>",
                w.getBuffer().toString());
    }

    public void testModel() throws SAXException {
        Map<CharSequence, Measure> p = PropModelHelper.create();
        p.put("none", new Measure(1, Unit.NONE));
        p.put("meter", new Measure(1, Unit.METER));
        p.put("second", new Measure(1, Unit.SECOND));
        PropModel m = new PropModelImpl() {

            public void init(Map<CharSequence, Measure> params) {
                internalInit(params);
            }
        };
        m.init(p);
        final StringWriter w = new StringWriter();
        SetupSaxSer s = new SetupSaxSer(w);
        s.internal(m);
        w.flush();
        assertEquals(
                "<model engine=\"org.jcurl.core.io.SetupSaxSerTest$1\"><param name=\"none\" val=\"1.0\" dim=\"\"></param><param name=\"meter\" val=\"1.0\" dim=\"m\"></param><param name=\"second\" val=\"1.0\" dim=\"s\"></param></model>",
                w.getBuffer().toString());
    }

    public void testCollissionSimple() throws SAXException {
        final StringWriter w = new StringWriter();
        SetupSaxSer s = new SetupSaxSer(w);
        s.internal(new CollissionSimple(null));
        w.flush();
        assertEquals(
                "<model engine=\"org.jcurl.core.model.CollissionSimple\"></model>",
                w.getBuffer().toString());
    }
}