package nl.uu.trafficmas.tests.SimulationModelTraaS;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import it.polito.appeal.traci.SumoTraciConnection;
import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.Pair;
import nl.uu.trafficmas.SimulationModelTraaS;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;

public class UpdateAgentsPhys {

	@Test
	public void getAgentPhysical() {
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
		HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);

		try {
			int i = 0;
			// Let some time pass so both agents are spawned and moving
			while (i < 5) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
			}
			
			HashMap<String, AgentPhysical> agentPhysMap =  SimulationModelTraaS.updateAgentsPhys(rn, currentAgentMap, conn);
			AgentPhysical aPhys1 = agentPhysMap.get(a1.agentID);
			AgentPhysical aPhys2 = agentPhysMap.get(a2.agentID);
			
			assertEquals(14.0, aPhys1.getVelocity(), 0.1);
			assertEquals(12.0, aPhys2.getVelocity(),0.1);
			assertEquals(a2.getAgentType(), aPhys2.getAgentType());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
