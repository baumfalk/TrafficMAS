package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.datamodel.DataModelXML;

import org.junit.Test;

public class XMLAgentTypeDistributionTest {

	@Test
	public void test() {
		HashMap<AgentProfileType, Double> dist = DataModelXML.getAgentProfileTypeDistribution("tests/", "AgentProfileTypesTest.xml");
		
		assertNotNull(dist);
		assertEquals(dist.size(),3);
		assertEquals(dist.get(AgentProfileType.PregnantWoman), 0.05,0);	
		assertEquals(dist.get(AgentProfileType.OldLady), 0.2,0);	
		assertEquals(dist.get(AgentProfileType.Normal), 0.75,0);	
	}
}
