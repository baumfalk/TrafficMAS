package nl.uu.trafficmas.tests.SimulationModelTraaS;

import static org.junit.Assert.*;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.Pair;
import nl.uu.trafficmas.SimulationModelTraaS;
import nl.uu.trafficmas.TrafficMAS;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

public class PrepareAgentActionsTest {

	@Test
	public void changeLane() {
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
			HashMap<String, AgentPhysical> leadingVehicleMap = SimulationModelTraaS.getLeadingVehicles(agentPhysMap, conn);
			HashMap<String, AgentAction> actions = new HashMap<String, AgentAction>();
			
			// Check for every action
			// ChangeLane
			AgentAction changeLaneAction = AgentAction.ChangeLane;
			actions.put(a2.agentID, changeLaneAction);
			
			SimulationModelTraaS.prepareAgentActions(actions, currentAgentMap, conn);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void changeVelocity() {
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
			// Let some time pass so both agents have spawned and are moving
			while (i < 5) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
			}
			HashMap<String, AgentPhysical> agentPhysMap =  SimulationModelTraaS.updateAgentsPhys(rn, currentAgentMap, conn);
			HashMap<String, AgentPhysical> leadingVehicleMap = SimulationModelTraaS.getLeadingVehicles(agentPhysMap, conn);
			HashMap<String, AgentAction> actions = new HashMap<String, AgentAction>();
			
			// Check for every action
			// ChangeLane
			AgentAction changeVelocityAction = AgentAction.ChangeVelocity;
			actions.put(a1.agentID, changeVelocityAction);
			
			SimulationModelTraaS.prepareAgentActions(actions, currentAgentMap, conn);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		fail("Not yet implemented");
	}
}
	

	

