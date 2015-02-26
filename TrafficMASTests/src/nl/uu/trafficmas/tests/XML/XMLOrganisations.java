package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.norm.NormScheme;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLOrganisations {

	public Map<String,Sensor> sensorMap;
	
	@Test
	public void loadSensorTest() throws ParserConfigurationException, SAXException, IOException {
		String dir 			= System.getProperty("user.dir")+"/tests/XML/GetSensors/";
		
		String sensorsXML	= "SensorTest.xml";
		String nodeXML		= "NodeTest.xml";
		String edgeXML		= "EdgeTest.xml";
		
		Document sensorsDoc	= DataModelXML.loadDocument(dir, sensorsXML);
		Document nodeDoc	= DataModelXML.loadDocument(dir, nodeXML);
		Document edgeDoc	= DataModelXML.loadDocument(dir, edgeXML);

		RoadNetwork	rn		= DataModelXML.instantiateRoadNetwork(nodeDoc, edgeDoc);
		
		Map<String,Sensor> sensorList = DataModelXML.getSensors(rn, sensorsDoc);
		
		this.sensorMap = sensorList;
		
		assertNotNull(sensorList);
		assertEquals(false, sensorList.isEmpty());
		
		for(Sensor sensor : sensorList.values()) {
			assert(sensor.readSensor().isEmpty());
		}
		
	}
	
	@Test
	public void loadNormsTest() throws ParserConfigurationException, SAXException, IOException {
		String dir 			= System.getProperty("user.dir")+"/tests/XML/GetNormSchemes/";
		
		String sensorsXML	= "SensorTest.xml";
		String nodeXML		= "NodeTest.xml";
		String edgeXML		= "EdgeTest.xml";
		String normXML		= "NormSchemeTest.xml";
		
		Document sensorsDoc	= DataModelXML.loadDocument(dir, sensorsXML);
		Document nodeDoc	= DataModelXML.loadDocument(dir, nodeXML);
		Document edgeDoc	= DataModelXML.loadDocument(dir, edgeXML);
		Document normsDoc	= DataModelXML.loadDocument(dir, normXML);

		RoadNetwork	rn		= DataModelXML.instantiateRoadNetwork(nodeDoc, edgeDoc);
		
		Map<String,Sensor> sensorList = DataModelXML.getSensors(rn, sensorsDoc);
		
		Map<String,NormScheme> normList = DataModelXML.getNormSchemes(sensorList, normsDoc);
		
		assertNotNull(normList);
		assertEquals(false, normList.isEmpty());
	}
	
	/*
	@Test
	public void loadOrganisationsTest() throws ParserConfigurationException, SAXException, IOException {
		fail("Not yet implemented");
		String dir 			= System.getProperty("user.dir")+"/tests/XML/Organisations/";
		String orgsXML 		= "test.orgs.xml";
		String normsXML		= "test.norms.xml";
		String sensorsXML	= "test.sensors.xml";

		Document orgsDoc 	= DataModelXML.loadDocument(dir, orgsXML);
		Document normsDoc 	= DataModelXML.loadDocument(dir, normsXML);
		Document sensorsDoc	= DataModelXML.loadDocument(dir, sensorsXML);				

		Map<String,NormScheme> normList = DataModelXML.getNormSchemes(normsDoc);

		Map<String,Organisation> organisations = DataModelXML.instantiateOrganisations(orgsDoc,normsDoc, sensorsDoc); 
		
		assertNotNull(organisations);
		assertEquals(false, organisations.isEmpty());
		
		Set<NormScheme> normSet = new HashSet<NormScheme>(normList.values());
		for(Organisation org : organisations.values()) {
			Set<NormScheme> orgNorms = new HashSet<NormScheme>(org.getNormSchemes());
			assertEquals(true,normSet.containsAll(orgNorms));
		}
	}
	*/

}
