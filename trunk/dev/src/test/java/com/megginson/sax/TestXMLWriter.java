package com.megginson.sax;
// TestXMLWriter.java - Test harness for the XML writer.
// Usage: java -Dorg.xml.sax.driver="..." TestXMLWriter [files...]

// $Id: TestXMLWriter.java,v 1.1.1.1 2000/09/16 14:15:33 david Exp $

import java.io.FileReader;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.ParserAdapter;
import org.xml.sax.helpers.XMLReaderFactory;

import com.megginson.sax.XMLWriter;


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