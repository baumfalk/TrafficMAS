package nl.uu.trafficmas.tests.controller;

import static org.junit.Assert.*;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.AgentData;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;
import nl.uu.trafficmas.simulationmodel.StateData;

import org.junit.Test;

public class UpdateAgentTest {

	@Test
	public void updateAgent() {
		Random random = new Random(1337);
		
		DataModel dataModel = new DataModelXML("tests/Controller/UpdateAgent/","MASTest.xml");
		MASData masData = dataModel.getMASData(); 
		
		HashMap<AgentProfileType, Double> dicks = dataModel.getAgentProfileTypeDistribution();
		for(Entry<AgentProfileType, Double> entry : dicks.entrySet()){
			System.out.println(entry.getKey());
		}
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/Controller/UpdateAgent/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/Controller/UpdateAgent/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/Controller/UpdateAgent/", "RouteTest.xml");
		
		HashMap<Agent,Integer> agentPairList = TrafficMASController.instantiateAgents(masData, random, routes);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);	
		StateData stateData = SimulationModelTraaS.getStateData(conn, false);
		//HashMap<String, Agent>updatedAgentMap = TrafficMASController.updateAgents(completeAgentMap, rn, stateData);

		// All agents have a velocity of 0.
		Agent a = completeAgentMap.get("Agent 0");
		
		int i = 0;
		while(i++ < masData.simulationLength){
			try {
				stateData = SimulationModelTraaS.getStateData(conn, true);
				if(stateData.agentsData.size() > 2) {
					TrafficMASController.updateAgent(rn, stateData, a.agentID, a);
					assertNotEquals(0,a.getVelocity(),0);
					assertNotNull(a.getRoad());
					assertNotNull(a.getLane());		
					System.out.println("Expected Arrival Time: " + a.getExpectedArrivalTime()*1000);
					System.out.println("Current Time: " + stateData.currentTimeStep);
					assertTrue(a.getExpectedArrivalTime()*1000 > stateData.currentTimeStep);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
}
