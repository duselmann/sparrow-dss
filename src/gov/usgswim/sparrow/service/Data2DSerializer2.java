package gov.usgswim.sparrow.service;

import static gov.usgswim.sparrow.service.AbstractSerializer.XMLSCHEMA_NAMESPACE;
import static gov.usgswim.sparrow.service.AbstractSerializer.XMLSCHEMA_PREFIX;
import gov.usgs.webservices.framework.dataaccess.BasicTagEvent;
import gov.usgs.webservices.framework.dataaccess.BasicXMLStreamReader;
import gov.usgswim.sparrow.Int2DImm;

import javax.xml.stream.XMLStreamException;

public class Data2DSerializer2 extends BasicXMLStreamReader {
	
	public static String TARGET_NAMESPACE = "http://www.usgs.gov/sparrow/prediction-response/v0_1";
	public static String TARGET_NAMESPACE_LOCATION = "http://www.usgs.gov/sparrow/prediction-response/v0_1.xsd";
	
	private final Int2DImm result;
	private final IDByPointRequest request;
	
	private int r;
	
	// ===========
	// CONSTRUCTOR
	// ===========
	public Data2DSerializer2(IDByPointRequest req, Int2DImm result) {
		this.request = req;
		this.result = result;
	}


	// ================
	// INSTANCE METHODS
	// ================
	/* Override because there's no resultset
	 * @see gov.usgs.webservices.framework.dataaccess.BasicXMLStreamReader#readNext()
	 */
	@Override
	public void readNext() throws XMLStreamException {
		try {
			if (!isStarted) {
				documentStartAction();
			}
			readRow();
			if (isDataDone && isStarted && !isEnded) {
				// Only output footer if the data is finished, the document was
				// actually started,
				// and the footer has not been output.
				documentEndAction();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new XMLStreamException(e);
		}
	}
	
	
	
	@Override
	protected void documentEndAction() {
		super.documentEndAction();
		addCloseTag("data");
		addCloseTag("response");
		addCloseTag("sparrow-prediction-response");
		events.add(new BasicTagEvent(END_DOCUMENT));
	}

	@Override
	protected BasicTagEvent documentStartAction() {
		super.documentStartAction();
		// add the namespaces
		this.setDefaultNamespace(TARGET_NAMESPACE);
		addNamespace(XMLSCHEMA_NAMESPACE, XMLSCHEMA_PREFIX);
		// opening element
		events.add(new BasicTagEvent(START_DOCUMENT));
		// TODO need to add encoding and version xw.add( evtFact.createStartDocument(ENCODING, XML_VERSION) );
		
		events.add(new BasicTagEvent(START_ELEMENT, "sparrow-prediction-response")
			.addAttribute(XMLSCHEMA_PREFIX, XMLSCHEMA_NAMESPACE, "schemaLocation", TARGET_NAMESPACE + " " + TARGET_NAMESPACE_LOCATION));

		{		
			addOpenTag("response");
			{
				events.add(new BasicTagEvent(START_ELEMENT, "metadata")
				.addAttribute("rowCount", Integer.toString(result.getRowCount()))
				.addAttribute("columnCount", Integer.toString(result.getColCount())));
				{
					addOpenTag("columns");
					{
						{
							events.add(new BasicTagEvent(START_ELEMENT, "group")
							.addAttribute("name", ""));
							{
								for(String head : result.getHeadings()) {
									events.add(makeNonNullBasicTag("col", "")
											.addAttribute("name", head)
											.addAttribute("type", "Number"));
								}
							}
							addCloseTag("group");
						}
					}
					addCloseTag("columns");
				}
				addCloseTag("metadata");
				//
				addOpenTag("data");
				// readRow();
				// addCloseTag("data");
				}
			// addCloseTag("response");
			}
		// addCloseTag("sparrow-prediction-response");
		// events.add(new BasicTagEvent(END_DOCUMENT));
		return null;
	}

	private void readRow() {
		// Only read while we still have rows.
		if (r < result.getRowCount()) {
			events.add(new BasicTagEvent(START_ELEMENT, "r").addAttribute("id", Integer.toString(result.getIdForRow(r))));
			// Get all the cells in the row
			for (int c = 0; c < result.getColCount(); c++)  {
				addNonNullBasicTag("c", Double.toString(result.getDouble(r, c)));
			}
			addCloseTag("r");
			// Add a carriage return so that the xml does not come back in one long line
			events.add(new BasicTagEvent(SPACE));
			r++;
		} else {
			isDataDone = true;
		}
	}
	
	@Override
	public void close() throws XMLStreamException {
		// nothing to do, really.
		// Have to override this as it is abstract.
	}
	

}
