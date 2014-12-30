package nl.uu.trafficmas.tests.SimulationModelTraaS;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import it.polito.appeal.traci.SumoTraciConnection;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.Pair;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;

import org.junit.Test;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;

public class UpdateAgentsPhysTest {

	@Test
	public void getAgentPhysicalTest() {
		Random random = new Random(1337);
		int simLength = 20;

		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(simLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/", "RouteTest.xml");
		HashMap<AgentProfileType, Double> dist = DataModelXML.getAgentProfileTypeDistribution("tests/", "AgentProfileTypesTest.xml");

		
		double agentSpawnProb = 0.5;
		HashMap<Agent, Integer> agentPairList = DataModelXML.instantiateAgents(random, routes, simLength, agentSpawnProb, dist);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);
		HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);

		Agent a1 = completeAgentMap.get("Agent 0");
		Agent a2 = completeAgentMap.get("Agent 1");
		try {
			int i = 0;
			// Let some time pass so both agents are spawned and moving
			
			// Set speed for a2
			conn.do_job_set(Vehicle.setSpeed(a2.agentID, 10.0));

			while (i < simLength) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
			}
			
			HashMap<String, Agent> agentPhysMap =  SimulationModelTraaS.updateAgents(rn, currentAgentMap, conn);
			AgentPhysical aPhys1 = agentPhysMap.get(a1.agentID);
			AgentPhysical aPhys2 = agentPhysMap.get(a2.agentID);
			
			
			assertEquals(14.0, aPhys1.getVelocity(), 0.1);
			assertEquals(10.0, aPhys2.getVelocity(),0.1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
