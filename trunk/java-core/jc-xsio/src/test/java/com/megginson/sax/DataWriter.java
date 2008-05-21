// DataWriter.java - XML writer for data-oriented files.

package com.megginson.sax;

import java.io.Writer;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Write data- or field-oriented XML.
 * 
 * <p>
 * This filter pretty-prints field-oriented XML without mixed content. all added
 * indentation and newlines will be passed on down the filter chain (if any).
 * </p>
 * 
 * <p>
 * In general, all whitespace in an XML document is potentially significant, so
 * a general-purpose XML writing tool like the
 * {@link com.megginson.sax.XMLWriter XMLWriter} class cannot add newlines or
 * indentation.
 * </p>
 * 
 * <p>
 * There is, however, a large class of XML documents where information is
 * strictly fielded: each element contains either character data or other
 * elements, but not both. For this special case, it is possible for a writing
 * tool to provide automatic indentation and newlines without requiring extra
 * work from the user. Note that this class will likely not yield appropriate
 * results for document-oriented XML like XHTML pages, which mix character data
 * and elements together.
 * </p>
 * 
 * <p>
 * This writer will automatically place each start tag on a new line, optionally
 * indented if an indent step is provided (by default, there is no indentation).
 * If an element contains other elements, the end tag will also appear on a new
 * line with leading indentation. Consider, for example, the following code:
 * </p>
 * 
 * <pre>
 * DataWriter w = new DataWriter();
 * 
 * w.setIndentStep(2);
 * w.startDocument();
 * w.startElement(&quot;Person&quot;);
 * w.dataElement(&quot;name&quot;, &quot;Jane Smith&quot;);
 * w.dataElement(&quot;date-of-birth&quot;, &quot;1965-05-23&quot;);
 * w.dataElement(&quot;citizenship&quot;, &quot;US&quot;);
 * w.endElement(&quot;Person&quot;);
 * w.endDocument();
 * </pre>
 * 
 * <p>
 * This code will produce the following document:
 * </p>
 * 
 * <pre>
 *                &lt;?xml version=&quot;1.0&quot; standalone=&quot;yes&quot;?&gt;
 *               
 *                &lt;Person&gt;
 *                  &lt;name&gt;Jane Smith&lt;/name&gt;
 *                  &lt;date-of-birth&gt;1965-05-23&lt;/date-of-birth&gt;
 *                  &lt;citizenship&gt;US&lt;/citizenship&gt;
 *                &lt;/Person&gt;
 * </pre>
 * 
 * <p>
 * This class inherits from {@link com.megginson.sax.XMLWriter XMLWriter}, and
 * provides all of the same support for Namespaces.
 * </p>
 * 
 * @author David Megginson, david@megginson.com
 * @version 0.2
 * @see com.megginson.sax.XMLWriter
 */
class DataWriter extends XMLWriter {

	// //////////////////////////////////////////////////////////////////
	// Constructors.
	// //////////////////////////////////////////////////////////////////

	private final static Object SEEN_DATA = new Object();

	private final static Object SEEN_ELEMENT = new Object();

	private final static Object SEEN_NOTHING = new Object();

	private int depth = 0;

	// //////////////////////////////////////////////////////////////////
	// Accessors and setters.
	// //////////////////////////////////////////////////////////////////

	private int indentStep = 0;

	private Object state = SEEN_NOTHING;

	// //////////////////////////////////////////////////////////////////
	// Override methods from XMLWriter.
	// //////////////////////////////////////////////////////////////////

	private Stack stateStack = new Stack();

	/**
	 * Create a new data writer for standard output.
	 */
	public DataWriter() {
		super();
	}

	/**
	 * Create a new data writer for the specified output.
	 * 
	 * @param writer
	 *            The character stream where the XML document will be written.
	 */
	public DataWriter(final Writer writer) {
		super(writer);
	}

	/**
	 * Create a new data writer for standard output.
	 * 
	 * <p>
	 * Use the XMLReader provided as the source of events.
	 * </p>
	 * 
	 * @param xmlreader
	 *            The parent in the filter chain.
	 */
	public DataWriter(final XMLReader xmlreader) {
		super(xmlreader);
	}

	/**
	 * Create a new data writer for the specified output.
	 * <p>
	 * Use the XMLReader provided as the source of events.
	 * </p>
	 * 
	 * @param xmlreader
	 *            The parent in the filter chain.
	 * @param writer
	 *            The character stream where the XML document will be written.
	 */
	public DataWriter(final XMLReader xmlreader, final Writer writer) {
		super(xmlreader, writer);
	}

	// //////////////////////////////////////////////////////////////////
	// Internal methods.
	// //////////////////////////////////////////////////////////////////

	/**
	 * Write a sequence of characters.
	 * 
	 * @param ch
	 *            The characters to write.
	 * @param start
	 *            The starting position in the array.
	 * @param length
	 *            The number of characters to use.
	 * @exception org.xml.sax.SAXException
	 *                If there is an error writing the characters, or if a
	 *                filter further down the chain raises an exception.
	 * @see XMLWriter#characters(char[], int, int)
	 */
	@Override
	public void characters(final char ch[], final int start, final int length)
			throws SAXException {
		state = SEEN_DATA;
		super.characters(ch, start, length);
	}

	// //////////////////////////////////////////////////////////////////
	// Constants.
	// //////////////////////////////////////////////////////////////////

	/**
	 * Print indentation for the current level.
	 * 
	 * @exception org.xml.sax.SAXException
	 *                If there is an error writing the indentation characters,
	 *                or if a filter further down the chain raises an exception.
	 */
	private void doIndent() throws SAXException {
		if (indentStep > 0 && depth > 0) {
			final int n = indentStep * depth;
			final char ch[] = new char[n];
			for (int i = 0; i < n; i++)
				ch[i] = ' ';
			this.characters(ch, 0, n);
		}
	}

	/**
	 * Write a empty element tag.
	 * 
	 * <p>
	 * Each tag will appear on a new line, and will be indented by the current
	 * indent step times the number of ancestors that the element has.
	 * </p>
	 * 
	 * <p>
	 * The newline and indentation will be passed on down the filter chain
	 * through regular characters events.
	 * </p>
	 * 
	 * @param uri
	 *            The element's Namespace URI.
	 * @param localName
	 *            The element's local name.
	 * @param qName
	 *            The element's qualified (prefixed) name.
	 * @param atts
	 *            The element's attribute list.
	 * @exception org.xml.sax.SAXException
	 *                If there is an error writing the empty tag, or if a filter
	 *                further down the chain raises an exception.
	 * @see XMLWriter#emptyElement(String, String, String, Attributes)
	 */
	@Override
	public void emptyElement(final String uri, final String localName,
			final String qName, final Attributes atts) throws SAXException {
		state = SEEN_ELEMENT;
		if (depth > 0)
			super.characters("\n");
		doIndent();
		super.emptyElement(uri, localName, qName, atts);
	}

	/**
	 * Write an end tag.
	 * 
	 * <p>
	 * If the element has contained other elements, the tag will appear indented
	 * on a new line; otherwise, it will appear immediately following whatever
	 * came before.
	 * </p>
	 * 
	 * <p>
	 * The newline and indentation will be passed on down the filter chain
	 * through regular characters events.
	 * </p>
	 * 
	 * @param uri
	 *            The element's Namespace URI.
	 * @param localName
	 *            The element's local name.
	 * @param qName
	 *            The element's qualified (prefixed) name.
	 * @exception org.xml.sax.SAXException
	 *                If there is an error writing the end tag, or if a filter
	 *                further down the chain raises an exception.
	 * @see XMLWriter#endElement(String, String, String)
	 */
	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {
		depth--;
		if (state == SEEN_ELEMENT) {
			super.characters("\n");
			doIndent();
		}
		super.endElement(uri, localName, qName);
		state = stateStack.pop();
	}

	// //////////////////////////////////////////////////////////////////
	// Internal state.
	// //////////////////////////////////////////////////////////////////

	/**
	 * Return the current indent step.
	 * 
	 * <p>
	 * Return the current indent step: each start tag will be indented by this
	 * number of spaces times the number of ancestors that the element has.
	 * </p>
	 * 
	 * @return The number of spaces in each indentation step, or 0 or less for
	 *         no indentation.
	 * @see #setIndentStep
	 */
	public int getIndentStep() {
		return indentStep;
	}

	/**
	 * Reset the writer so that it can be reused.
	 * 
	 * <p>
	 * This method is especially useful if the writer failed with an exception
	 * the last time through.
	 * </p>
	 * 
	 * @see com.megginson.sax.XMLWriter#reset
	 */
	@Override
	public void reset() {
		depth = 0;
		state = SEEN_NOTHING;
		stateStack = new Stack();
		super.reset();
	}

	/**
	 * Set the current indent step.
	 * 
	 * @param indentStep
	 *            The new indent step (0 or less for no indentation).
	 * @see #getIndentStep
	 */
	public void setIndentStep(final int indentStep) {
		this.indentStep = indentStep;
	}

	/**
	 * Write a start tag.
	 * 
	 * <p>
	 * Each tag will begin on a new line, and will be indented by the current
	 * indent step times the number of ancestors that the element has.
	 * </p>
	 * 
	 * <p>
	 * The newline and indentation will be passed on down the filter chain
	 * through regular characters events.
	 * </p>
	 * 
	 * @param uri
	 *            The element's Namespace URI.
	 * @param localName
	 *            The element's local name.
	 * @param qName
	 *            The element's qualified (prefixed) name.
	 * @param atts
	 *            The element's attribute list.
	 * @exception org.xml.sax.SAXException
	 *                If there is an error writing the start tag, or if a filter
	 *                further down the chain raises an exception.
	 * @see XMLWriter#startElement(String, String, String, Attributes)
	 */
	@Override
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes atts) throws SAXException {
		stateStack.push(SEEN_ELEMENT);
		state = SEEN_NOTHING;
		if (depth > 0)
			super.characters("\n");
		doIndent();
		super.startElement(uri, localName, qName, atts);
		depth++;
	}

}

// end of DataWriter.java
