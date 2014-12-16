package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.Pair;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

import org.junit.Test;

public class XMLInstantiateAgentsTest {

	@Test
	public void test() {
		Random random = new Random(1337);
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/", "RouteTest.xml");
		ArrayList<Pair<AgentProfileType, Double>> dist = DataModelXML.getAgentProfileTypeDistribution("tests/", "AgentProfileTypesTest.xml");

		int simulationLength = 10;
		double agentSpawnProb = 0;
		ArrayList<Pair<Integer, Agent>> lst = DataModelXML.instantiateAgents(random, routes, simulationLength, agentSpawnProb, dist);
		assertEquals(lst.size(),0);
		
		simulationLength = 20;
		agentSpawnProb = 1;
		lst = DataModelXML.instantiateAgents(random, routes, simulationLength, agentSpawnProb, dist);
		assertEquals(lst.size(),simulationLength);
		
		simulationLength = 200;
		agentSpawnProb = 0.5;
		lst = DataModelXML.instantiateAgents(random, routes, simulationLength, agentSpawnProb, dist);
		assertEquals(lst.size(),98); //always 12 with this seed
		 
		for(Pair<Integer, Agent> item : lst) {
			System.out.println(item.first);
			System.out.println(item.second.getClass().getName());
		}
		
		fail("Not yet implemented");
	}

}
