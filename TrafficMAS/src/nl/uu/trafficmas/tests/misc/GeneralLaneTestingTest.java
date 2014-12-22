package nl.uu.trafficmas.tests.misc;

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
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

import de.tudresden.sumo.cmd.Lane;
import de.tudresden.sumo.cmd.Vehicle;

public class GeneralLaneTestingTest {

	//@Test
	public void getMeanSpeedNextLane() {
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

		
		double agentSpawnProb = 0.5;
		ArrayList<Pair<Agent, Integer>> agentPairList = DataModelXML.instantiateAgents(random, routes, simLength, agentSpawnProb, dist);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);
		HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);
		HashMap<String, Agent> aPhysMap = new HashMap<String, Agent>();
		Edge[] lst = new Edge[0];
		
		Agent a1 = new NormalAgent("Extra 0", rn.getNodes()[1], lst, 1000, 70.0,70);
		
		completeAgentMap.put(a1.agentID, a1);
		try {
			int i=0;
			
			// Add another agent on lane 1 for testing, disable going right for this agent.
			conn.do_job_set(Vehicle.add(a1.agentID, "Car", "route0", 7000, 0, 10.0, (byte) 1));
			conn.do_job_set(Vehicle.setLaneChangeMode(a1.agentID, 0b0000000000));
			conn.do_job_set(Vehicle.setSpeed(a1.agentID, 10.0));

			
			
			while(i++ < simLength){
				conn.do_timestep();
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
				aPhysMap = SimulationModelTraaS.updateAgents(rn, currentAgentMap, conn);
			}
			double meanSpeedLane0 = (double) conn.do_job_get(Lane.getLastStepMeanSpeed("A28Tot350_0"));
 			double meanSpeedLane1 = (double) conn.do_job_get(Lane.getLastStepMeanSpeed("A28Tot350_1"));
 			System.out.println("Actual mSpeed_0: " + meanSpeedLane0);
 			System.out.println("Actual mSpeed_1: " + meanSpeedLane1);

			
		} catch(Exception e){
			e.printStackTrace();
		}		
	}
}
