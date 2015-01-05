package nl.uu.trafficmas.tests.simulationModelTraaS;

import static org.junit.Assert.*;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.Pair;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;

import org.junit.Test;

public class GetLeadingVehiclesTest {

	@Test
	public void getLeadingVehicles() {
		Random random = new Random(1337);
		int simLength = 7;

		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(simLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/", "RouteTest.xml");
		HashMap<AgentProfileType, Double> dist = DataModelXML.getAgentProfileTypeDistribution("tests/", "AgentProfileTypesTest.xml");
		
		int simulationLength = 20;
		double agentSpawnProb = 0.5;
		HashMap<Agent, Integer> agentPairList = DataModelXML.instantiateAgents(random, routes, simulationLength, agentSpawnProb, dist);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);
		HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);

		try {
			int i = 0;
			// Let some time pass so both agents are spawned and moving
			while (i < 7) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
			}
			HashMap<String, Agent> agents =  SimulationModelTraaS.updateAgents(rn, currentAgentMap, conn);
			HashMap<String, Agent> leadingVehicleMap = SimulationModelTraaS.getLeadingVehicles(agents, conn);
			
			// Agent 1 does not have a leading vehicle
			assertEquals(null, leadingVehicleMap.get("Agent 0"));
			
			// Agent 2 has agent 1 as leading vehicle
			assertEquals(agents.get("Agent 0"), leadingVehicleMap.get("Agent 1"));
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
