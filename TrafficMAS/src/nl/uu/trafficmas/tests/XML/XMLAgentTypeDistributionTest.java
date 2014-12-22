package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.Pair;

import org.junit.Test;

public class XMLAgentTypeDistributionTest {

	@Test
	public void test() {
		ArrayList<Pair<AgentProfileType, Double>> dist = DataModelXML.getAgentProfileTypeDistribution("tests/", "AgentProfileTypesTest.xml");
		
		assertNotNull(dist);
		assertEquals(dist.size(),3);
			
		assertEquals(dist.get(0).first,AgentProfileType.PregnantWoman);
		assertEquals(dist.get(1).first,AgentProfileType.OldLady);
		assertEquals(dist.get(2).first,AgentProfileType.Normal);

		assertEquals(dist.get(0).second,0.05,0);
		assertEquals(dist.get(1).second,0.20,0);
		assertEquals(dist.get(2).second,0.75,0);
	}
}
