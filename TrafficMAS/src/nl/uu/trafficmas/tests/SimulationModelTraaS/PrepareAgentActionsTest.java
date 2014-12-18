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

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.config.Constants;
import de.tudresden.sumo.util.SumoCommand;

public class PrepareAgentActionsTest {

	@Test
	public void changeLane() {
		double agent1Speed = 10.0;
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", "35");
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/ConfigTest.xml");
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
			
			// Disable automatic SUMO overtaking, but enable automatic right drive changes.
			conn.do_job_set(Vehicle.setLaneChangeMode(a2.agentID, 0b0001000000));
			conn.do_job_set(Vehicle.setLaneChangeMode(a1.agentID, 0b0001000000));
			
			// Reduce velocity of front car, so a2 can execute an overtaking action.
			conn.do_job_set(Vehicle.setSpeed(a1.agentID, agent1Speed));

			int i = 0;
			// Let some time pass so both agents are spawned and moving
			while (i < 5) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
			}
			SimulationModelTraaS.updateAgentsPhys(rn, currentAgentMap, conn);
			HashMap<String, AgentAction> actions = new HashMap<String, AgentAction>();
			
			// Testing action ChangeLane
			AgentAction changeLaneAction = AgentAction.ChangeLane;
			actions.put(a2.agentID, changeLaneAction);
			SimulationModelTraaS.prepareAgentActions(actions, currentAgentMap, conn);
			
			while (i < 37) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
				SimulationModelTraaS.updateAgentsPhys(rn, currentAgentMap, conn);
			}
			
			// Agent 2 is going maxSpeed, this means agent 1 was passed.
			assertEquals(13.9, a2.getVelocity(),0.1);
			assertEquals(agent1Speed, a1.getVelocity(),0.1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void changeVelocity() {
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", "60");
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/ConfigTest.xml");
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
			
			
			conn.do_job_set(Vehicle.setLaneChangeMode(a2.agentID, 0b0001000000));
			conn.do_job_set(Vehicle.setLaneChangeMode(a1.agentID, 0b0001000000));
			conn.do_job_set(Vehicle.setSpeedMode(a1.agentID, 0b00000));
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
			AgentAction changeVelocityAction = AgentAction.ChangeVelocity;
			actions.put(a1.agentID, changeVelocityAction);
			
			SimulationModelTraaS.prepareAgentActions(actions, currentAgentMap, conn);
			while (i < 70) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
				if (currentAgentMap.containsKey(a1.agentID)){
					System.out.println((double) conn.do_job_get(Vehicle.getSpeed(a1.agentID)));
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
	

	

