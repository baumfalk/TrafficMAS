package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.KeyValue;
import nl.uu.trafficmas.agent.AgentProfileType;

import org.junit.Test;

public class XMLAgentTypeDistributionTest {

	@Test
	public void test() {
		ArrayList<KeyValue<AgentProfileType, Double>> dist = DataModelXML.getAgentProfileTypeDistribution("tests/", "AgentProfileTypesTest.xml");
		
		assertNotNull(dist);
		assertEquals(dist.size(),3);
			
		assertEquals(dist.get(0).key,AgentProfileType.PregnantWoman);
		assertEquals(dist.get(1).key,AgentProfileType.OldLady);
		assertEquals(dist.get(2).key,AgentProfileType.Normal);

		assertEquals(dist.get(0).value,0.05,0);
		assertEquals(dist.get(1).value,0.20,0);
		assertEquals(dist.get(2).value,0.75,0);
	}
}
