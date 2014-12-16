package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.Pair;
import nl.uu.trafficmas.agent.AgentProfileType;

import org.junit.Test;

public class XMLSelectAgentProfileType {

	@Test
	public void test() {
		ArrayList<Pair<AgentProfileType, Double>> dist = DataModelXML.getAgentProfileTypeDistribution("tests/", "AgentProfileTypesTest.xml");
		double coinFlip = 0.04;
		AgentProfileType agentType = DataModelXML.selectAgentProfileType(coinFlip, dist);
		assertEquals(agentType, AgentProfileType.PregnantWoman);
		
		coinFlip = 0.05;
		agentType = DataModelXML.selectAgentProfileType(coinFlip, dist);
		assertNotEquals(agentType, AgentProfileType.PregnantWoman);
		
		agentType = DataModelXML.selectAgentProfileType(coinFlip, dist);
		assertEquals(agentType, AgentProfileType.OldLady);
		
		coinFlip = 0.95;
		agentType = DataModelXML.selectAgentProfileType(coinFlip, dist);
		assertEquals(agentType, AgentProfileType.Normal);
	}

}
