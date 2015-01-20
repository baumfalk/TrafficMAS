package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import nl.uu.trafficmas.datamodel.DataModelXML;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class GetRouteSpawnProbabilityTest {

	@Test
	public void GetRouteSpawnProbability() throws ParserConfigurationException, SAXException, IOException {
		String dir = System.getProperty("user.dir")+"/tests/XML/GetRouteSpawnProbability/";
		String routeXML = "RouteTest.xml";
		Document routeDoc = DataModelXML.loadDocument(dir, routeXML);
		HashMap<String, Double> routeIdAndProbability = DataModelXML.getRouteSpawnProbability(routeDoc);
		for(Map.Entry<String,Double> entry : routeIdAndProbability.entrySet()){
			assertEquals("route0", entry.getKey());
			assertEquals(0.4, entry.getValue(), 0);
			break;
		}		
	}
}
