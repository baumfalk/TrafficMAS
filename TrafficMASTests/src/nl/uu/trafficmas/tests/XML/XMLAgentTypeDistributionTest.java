package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.datamodel.DataModelXML;

import org.junit.Test;

public class XMLAgentTypeDistributionTest {

	@Test
	public void xmlAgentTypeDistribution() {
		HashMap<AgentProfileType, Double> dist = DataModelXML.getAgentProfileTypeDistribution(System.getProperty("user.dir")+"/tests/XML/GetAgentProfileType/", "AgentProfileTypesTest.xml");
		
		assertNotNull(dist);
		assertEquals(dist.size(),3);
		assertEquals(dist.get(AgentProfileType.HotShot), 0.05,0);	
		assertEquals(dist.get(AgentProfileType.OldLady), 0.2,0);	
		assertEquals(dist.get(AgentProfileType.Normal), 0.75,0);	
	}
}
