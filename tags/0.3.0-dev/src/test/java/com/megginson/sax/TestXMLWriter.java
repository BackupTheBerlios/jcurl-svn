package com.megginson.sax;
// TestXMLWriter.java - Test harness for the XML writer.
// Usage: java -Dorg.xml.sax.driver="..." TestXMLWriter [files...]

// $Id$

import java.io.FileReader;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * Simple test harness for XMLWriter.
 */
public class TestXMLWriter
{
    public static void main (String args[])
	throws Exception	// yech!
    {
	XMLWriter w = new XMLWriter(XMLReaderFactory.createXMLReader());

	if (args.length == 0) {
	    System.err.println("Usage java -Dorg.xml.sax.driver=<driver> TestXMLWriter file...");
	    System.exit(1);
	}

	for (int i = 0; i < args.length; i++) {
	    w.parse(new InputSource(new FileReader(args[i])));
	}
    }
}

// end of TestXMLWriter.java
