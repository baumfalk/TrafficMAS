package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import nl.uu.trafficmas.datamodel.DataModelXML;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GetAgentSpawnInformation {

	@Test
	public void test() throws ParserConfigurationException, SAXException, IOException {
		String dir 					= System.getProperty("user.dir")+"/tests/Misc/MultipleRoutes/";
		String xmlTest 		= "XMLTest.xml";
		Document agentProfileDoc 	= DataModelXML.loadDocument(dir, xmlTest);
		NodeList routeList= agentProfileDoc.getElementsByTagName("route");

		for(int i=0;i<routeList.getLength(); i++){
			org.w3c.dom.Node n 		= routeList.item(i);
			NamedNodeMap attributes = n.getAttributes();
			String id 				= attributes.getNamedItem("id").getTextContent();	
		}
		
	}
}
