package nl.uu.trafficmas.tests.simulationModelTraaS;

import static org.junit.Assert.*;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.AgentData;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;
import nl.uu.trafficmas.simulationmodel.StateData;

import org.junit.Test;

public class QueryBuilderTest {

	@Test
	public void test() {
		Random random = new Random(1337);
		int simLength = 20;
		//fail("Not yet implemented");
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(simLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/", "RouteTest.xml");
		HashMap<AgentProfileType, Double> dist = DataModelXML.getAgentProfileTypeDistribution("tests/", "AgentProfileTypesTest.xml");
		
		double agentSpawnProb = 0.5;
		HashMap<Agent,Integer> agentPairList = DataModelXML.instantiateAgents(random, routes, simLength, agentSpawnProb, dist);
		SimulationModelTraaS.addAgents(agentPairList, conn);
		
		StateData stateData = SimulationModelTraaS.getStateData(conn);
		assertNotNull(stateData);
		int i = 0;
		while(i++ < simLength){
			try {
				conn.do_timestep();
				stateData = SimulationModelTraaS.getStateData(conn);
				if(!stateData.agentsData.isEmpty()) {
					stateData = SimulationModelTraaS.getStateData(conn);
					for(Entry<String, AgentData> val : stateData.agentsData.entrySet()) {
						assertNotNull(val.getValue().laneID);
						assertNotNull(val.getValue().position);
						assertNotNull(val.getValue().speed);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		}
		
	}

}
