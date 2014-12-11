package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.KeyValue;
import nl.uu.trafficmas.agent.AgentType;

import org.junit.Test;

public class XMLAgentTypeDistributionTest {

	@Test
	public void test() {
		ArrayList<KeyValue<AgentType, Double>> dist = DataModelXML.getAgentTypeDistribution("tests/", "AgentTypesTest.xml");
		
		assertNotNull(dist);
		assertEquals(dist.size(),3);
		assertEquals(dist.get(0).key,AgentType.PregnantWoman);
		assertEquals(dist.get(1).key,AgentType.OldLady);
		assertEquals(dist.get(2).key,AgentType.Normal);

		assertEquals(dist.get(0).value,0.05,0);
		assertEquals(dist.get(1).value,0.20,0);
		assertEquals(dist.get(2).value,0.75,0);
	}

}
