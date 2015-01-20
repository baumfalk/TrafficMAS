package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLRoadNetworkTest {

	@Test
	public void xmlRoadNetwork() throws ParserConfigurationException, SAXException, IOException {
		String dir = System.getProperty("user.dir")+"/tests/XML/InstantiateRoadNetwork/";
		String nodeXML 		= "NodeTest.xml";
		String edgeXML 		= "EdgeTest.xml";
		
		Document nodeDoc 	= DataModelXML.loadDocument(dir, nodeXML);
		Document edgeDoc 	= DataModelXML.loadDocument(dir, edgeXML);
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(nodeDoc, edgeDoc);
		
		assertEquals(rn.getNodes().length,2);
		assertEquals(rn.getEdges().length,1);
	}
}
