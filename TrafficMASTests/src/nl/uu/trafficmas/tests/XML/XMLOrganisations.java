package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.norm.NormScheme;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.Sensor;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLOrganisations {

	
	@Test
	public void loadSensorTest() throws ParserConfigurationException, SAXException, IOException {
		fail("Not yet implemented");
		String dir 			= System.getProperty("user.dir")+"/tests/XML/Organisations/";
		String sensorsXML	= "test.sensors.xml";
		Document sensorsDoc	= DataModelXML.loadDocument(dir, sensorsXML);
		
		Map<String,Sensor> sensorList = DataModelXML.getSensors(sensorsDoc);
		
		assertNotNull(sensorList);
		assertEquals(false, sensorList.isEmpty());
		
		for(Sensor sensor : sensorList.values()) {
			assert(sensor.readSensor().isEmpty());
		}
		
	}
	
	@Test
	public void loadNormsTest() throws ParserConfigurationException, SAXException, IOException {
		fail("Not yet implemented");
		String dir 			= System.getProperty("user.dir")+"/tests/XML/Organisations/";
		String normsXML		= "test.norm.xml";
		Document normsDoc	= DataModelXML.loadDocument(dir, normsXML);
		
		Map<String,NormScheme> normList = DataModelXML.getNorms(normsDoc);
		
		assertNotNull(normList);
		assertEquals(false, normList.isEmpty());
	}
	
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

		Map<String,NormScheme> normList = DataModelXML.getNorms(normsDoc);

		Map<String,Organisation> organisations = DataModelXML.instantiateOrganisations(orgsDoc,normsDoc, sensorsDoc); 
		
		assertNotNull(organisations);
		assertEquals(false, organisations.isEmpty());
		
		Set<NormScheme> normSet = new HashSet<NormScheme>(normList.values());
		for(Organisation org : organisations.values()) {
			Set<NormScheme> orgNorms = new HashSet<NormScheme>(org.getNorms().values());
			assertEquals(true,normSet.containsAll(orgNorms));
		}
	}

}
