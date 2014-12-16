package nl.uu.trafficmas.tests.SimulationModelTraaS;

import static org.junit.Assert.*;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.Pair;
import nl.uu.trafficmas.SimulationModelTraaS;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

public class UpdateCurrentAgentMapTest {

	@Test
	public void test() {
		SumoTraciConnection conn = SimulationModelTraaS.initialize("sumo", "./tests/ConfigTest.xml");
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("./tests/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Pair<Agent, Integer>> agentPairList = new ArrayList<Pair<Agent, Integer>>();
		Agent a1 = new NormalAgent("agent1", rn.getNodes()[1], 6000, 70.0);
		Agent a2 = new NormalAgent("agent2", rn.getNodes()[1], 6000, 70.0);
		Pair<Agent, Integer> agentPair1 = new Pair<Agent, Integer>(a1, 1000);
		Pair<Agent, Integer> agentPair2 = new Pair<Agent, Integer>(a2, 3000);
		
		agentPairList.add(agentPair1);
		agentPairList.add(agentPair2);
		
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);
		
		// Since no time has passed yet, SUMO has not initialized the cars, and this should be empty.
		HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);
		assertTrue(currentAgentMap.isEmpty());
	
		try {
			int i = 0;
			// Let some time pass so both agents are spawned and moving
			while (i < 20) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);

			}
			assertEquals(2, currentAgentMap.size());
			
			// Now let some more time pass so the vehicles will have completed their routes.
			while (i < 35) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
			}
			assertEquals(0, currentAgentMap.size());
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
