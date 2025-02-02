package gov.usgswim.sparrow.service.model;

import gov.usgs.webservices.framework.utils.ResourceLoaderUtils;
import gov.usgswim.ThreadSafe;
import gov.usgswim.service.HttpService;
import gov.usgswim.service.pipeline.PipelineRequest;
import gov.usgswim.sparrow.domain.SparrowModel;
import gov.usgswim.sparrow.service.ModelSerializer;
import gov.usgswim.sparrow.service.SharedApplication;
import gov.usgswim.sparrow.util.SparrowResourceUtils;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

//TODO:  No caching is done of model data
@ThreadSafe
public class ModelService implements HttpService<ModelRequest> {
	public static final String MODEL_SERVICE_ERROR_RESPONSE = "modelServiceError.xml";

	protected static Logger log =
		Logger.getLogger(ModelService.class); //logging for this class

	protected static String RESPONSE_MIME_TYPE = "application/xml";

	//They promise these factories are threadsafe
	@SuppressWarnings("unused")
	private static Object factoryLock = new Object();
	//protected static XMLInputFactory xinFact;
	protected static XMLOutputFactory xoFact;


	public ModelService() {}

	protected Connection getConnection() throws NamingException, SQLException {
		return SharedApplication.getInstance().getROConnection();
	}

	public void shutDown() {
		xoFact = null;
	}


	public XMLStreamReader getXMLStreamReader(PipelineRequest o, boolean needsCompleteFirstRow) throws Exception{
		return getXMLStreamReader((ModelRequest) o, needsCompleteFirstRow);
	}

	public XMLStreamReader getXMLStreamReader(ModelRequest o, boolean needsCompleteFirstRow) throws Exception{
		
		List<SparrowModel> models = null;
		
		try {
			models = SharedApplication.getInstance().getModelMetadata(o.toCacheKey());
		} catch (Exception e) {
			System.err.println(this.getClass().getSimpleName() + " unable to get a connection");
			e.printStackTrace();
			String errorResponsePath = SparrowResourceUtils.getResourceFilePath(MODEL_SERVICE_ERROR_RESPONSE);
			String errorResponseTemplate = ResourceLoaderUtils.loadResourceAsString(errorResponsePath);

			XMLInputFactory inFact = XMLInputFactory.newInstance();
			XMLStreamReader reader = inFact.createXMLStreamReader(new StringReader(String.format(errorResponseTemplate, e.getMessage())));
			return reader;
		}

		ModelSerializer serializer = new ModelSerializer(models);
		if (needsCompleteFirstRow) {
			serializer.setOutputCompleteFirstRow();
		}
		return serializer;
	}
}
