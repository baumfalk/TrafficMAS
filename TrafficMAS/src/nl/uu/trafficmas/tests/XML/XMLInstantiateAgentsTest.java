package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.Pair;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

import org.junit.Test;

public class XMLInstantiateAgentsTest {

	@Test
	public void test() {
		Random random = new Random(1337);
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/XML/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/XML/", "RouteTest.xml");
		HashMap<AgentProfileType, Double> dist = DataModelXML.getAgentProfileTypeDistribution("tests/XML/", "AgentProfileTypesTest.xml");

		int simulationLength = 10;
		double agentSpawnProb = 0;
		HashMap<Agent, Integer> lst = DataModelXML.instantiateAgents(random, routes, simulationLength, agentSpawnProb, dist);
		assertEquals(lst.size(),0);
		
		simulationLength = 20;
		agentSpawnProb = 1;
		lst = DataModelXML.instantiateAgents(random, routes, simulationLength, agentSpawnProb, dist);
		assertEquals(lst.size(),simulationLength);
		
		simulationLength = 200;
		agentSpawnProb = 0.5;
		lst = DataModelXML.instantiateAgents(random, routes, simulationLength, agentSpawnProb, dist);
		assertEquals(lst.size(),98); //always 98 with this seed
	}
}
