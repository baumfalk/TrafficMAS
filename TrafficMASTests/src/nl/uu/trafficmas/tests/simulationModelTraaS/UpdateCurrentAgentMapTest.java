package nl.uu.trafficmas.tests.simulationModelTraaS;

import static org.junit.Assert.assertEquals;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;
import nl.uu.trafficmas.simulationmodel.StateData;

import org.junit.Test;

public class UpdateCurrentAgentMapTest {

	@Test
	public void updateCurrentAgentMap() {
		Random random = new Random(1337);
		
		DataModel dataModel = new DataModelXML(System.getProperty("user.dir")+"/tests/SimulationModelTraaS/UpdateCurrentAgentMap/","MASTest.xml");
		MASData masData = dataModel.getMASData();
		
		HashMap<String, String> options = new LinkedHashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", System.getProperty("user.dir")+"/tests/SimulationModelTraaS/UpdateCurrentAgentMap/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/SimulationModelTraaS/UpdateCurrentAgentMap/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/SimulationModelTraaS/UpdateCurrentAgentMap/", "RouteTest.xml");
		
		HashMap<Agent,Integer> agentPairList = TrafficMASController.instantiateAgents(masData, random, routes);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);
		HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new LinkedHashMap<String, Agent>(), conn);
	
		try {
			int i = 0;
			while (i++ < masData.simulationLength) {
				SimulationModelTraaS.getStateData(conn, true);
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
			}
			assertEquals(4, currentAgentMap.size());
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
