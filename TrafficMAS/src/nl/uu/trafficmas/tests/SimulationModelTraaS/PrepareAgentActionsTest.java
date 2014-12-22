package nl.uu.trafficmas.tests.SimulationModelTraaS;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.Pair;
import nl.uu.trafficmas.SimulationModelTraaS;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

import org.junit.Test;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoColor;

public class PrepareAgentActionsTest {

	@Test
	public void changeLane() {
		double agent1Speed = 10.0;
		Random random = new Random(1337);
		int simLength = 50;

		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(simLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo-gui", "./tests/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/", "RouteTest.xml");
		ArrayList<Pair<AgentProfileType, Double>> dist = DataModelXML.getAgentProfileTypeDistribution("tests/", "AgentProfileTypesTest.xml");
		SumoColor colorHotShot = new SumoColor(255,0,0,255);
		SumoColor colorOldLady = new SumoColor(0,255,0,255);
		
		
		double agentSpawnProb = 0.5;
		ArrayList<Pair<Agent, Integer>> agentPairList = DataModelXML.instantiateAgents(random, routes, simLength, agentSpawnProb, dist);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);
		HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);

		Agent a1 = completeAgentMap.get("Agent 0");
		Agent a2 = completeAgentMap.get("Agent 3");
		try {
			
			// Disable automatic SUMO overtaking, but enable automatic right drive changes.
			//conn.do_job_set(Vehicle.setLaneChangeMode(a1.agentID, 0b0001000000));
			//conn.do_job_set(Vehicle.setLaneChangeMode(a2.agentID, 0b0001000000));
			
			// Reduce velocity of front car, so a2 can execute an overtaking action.
			conn.do_job_set(Vehicle.setSpeed(a1.agentID, agent1Speed));
			conn.do_job_set(Vehicle.setColor(a2.agentID, colorHotShot));
			conn.do_job_set(Vehicle.setColor(a1.agentID, colorOldLady ));

			int i = 0;
			// Let some time pass so both agents are spawned and moving
			while (i < 7) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
			}
			SimulationModelTraaS.updateAgents(rn, currentAgentMap, conn);
			HashMap<String, AgentAction> actions = new HashMap<String, AgentAction>();
			
			// Testing action ChangeLane
			AgentAction changeLaneAction = AgentAction.ChangeLane;
			
			actions.put(a2.agentID, changeLaneAction);
			SimulationModelTraaS.prepareAgentActions(actions, currentAgentMap, conn);
			
			while (i < simLength) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
				SimulationModelTraaS.updateAgents(rn, currentAgentMap, conn);
			}
			// Agent 2 is going maxSpeed, this means agent 1 was passed.
			// TODO get maxSpeed from Lane object. 
			assertEquals(14.0, a2.getVelocity(),0.1);
			assertEquals(agent1Speed, a1.getVelocity(),0.1);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void changeVelocity() {
		fail("Not implemented");
		Random random = new Random(1337);
		int simLength = 30;

		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(simLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/", "RouteTest.xml");
		ArrayList<Pair<AgentProfileType, Double>> dist = DataModelXML.getAgentProfileTypeDistribution("tests/", "AgentProfileTypesTest.xml");

		
		int simulationLength = 20;
		double agentSpawnProb = 0.5;
		ArrayList<Pair<Agent, Integer>> agentPairList = DataModelXML.instantiateAgents(random, routes, simulationLength, agentSpawnProb, dist);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);
		HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);
		HashMap<String, Agent> agentMap = new HashMap<String, Agent>();
		Agent a1 = completeAgentMap.get("Agent 0");
		Agent a2 = completeAgentMap.get("Agent 1");
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
			SimulationModelTraaS.updateAgents(rn, currentAgentMap, conn);
			HashMap<String, AgentAction> actions = new HashMap<String, AgentAction>();
			
			// Check for every action
			AgentAction changeVelocityAction = AgentAction.ChangeVelocity5;
			actions.put(a1.agentID, changeVelocityAction);
			
			SimulationModelTraaS.prepareAgentActions(actions, currentAgentMap, conn);
			while (i < 30) {
				conn.do_timestep();
				i++;
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
				agentMap = SimulationModelTraaS.updateAgents(rn, currentAgentMap, conn);
			}
			// TODO Change AgentAction data structure and change this. 
			assertEquals(17.0,agentMap.get(a1.agentID).getVelocity(),0.5);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void changeRoad() {
		fail("Not implemented");
	}

}

	
	

	

