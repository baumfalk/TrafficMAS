package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;
import nl.uu.trafficmas.datamodel.DataModelXML;

import org.junit.Test;

public class XMLSpawnProbabilityTest {

	@Test
	public void xmlSpawnProbability() {
		double prob = DataModelXML.getAgentSpawnProbability(System.getProperty("user.dir")+"/tests/XML/AgentSpawnProbability/", "AgentProfileTypesTest.xml");
		assertEquals(prob,0.01,0);
	}

}
