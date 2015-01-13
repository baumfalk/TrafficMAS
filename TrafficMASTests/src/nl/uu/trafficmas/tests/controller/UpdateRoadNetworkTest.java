package nl.uu.trafficmas.tests.controller;

import static org.junit.Assert.*;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;
import nl.uu.trafficmas.simulationmodel.StateData;

import org.junit.Test;

public class UpdateRoadNetworkTest {

	@Test
	public void updateRoadNetwork() {
		Random random = new Random(1337);

		DataModel dataModel = new DataModelXML(System.getProperty("user.dir")+"/tests/Controller/UpdateRoadNetwork/","MASTest.xml");
		MASData masData = dataModel.getMASData(); 
		
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", System.getProperty("user.dir")+"/tests/Controller/UpdateRoadNetwork/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/Controller/UpdateRoadNetwork/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/Controller/UpdateRoadNetwork/", "RouteTest.xml");
		
		HashMap<Agent,Integer> agentPairList = TrafficMASController.instantiateAgents(masData, random, routes);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);	
		//HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);

		StateData stateData = SimulationModelTraaS.getStateData(conn, false);
		
		int i = 0;
		while(i++ < masData.simulationLength){
			try {
				stateData = SimulationModelTraaS.getStateData(conn, true);
				rn = TrafficMASController.updateRoadNetwork(rn, stateData);
				Road r  = rn.getRoadFromID(rn.getEdges()[0].getID());
				
				if( stateData.currentTimeStep == 8000){
					// 13.9 is MaxSpeed. Since cars are only just arriving, the meanSpeed will be less if this is correctly updated.
					assertTrue(r.getMeanSpeedEdge() < 13.9);
					
					// Agents should also take longer on average.
					assertTrue(r.getMeanTravelTime() > r.length/13.9);

					// The left lane has no agents on it so it's meanSpeed should be max (13.9)
					assertEquals(13.9, r.laneList.get(1).getLaneMeanSpeed(),0 );
					
					// And thus meanTravelTime on that same lane should be 0 
					assertEquals(0, r.laneList.get(1).getMeanTravelTime(),0);

				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
}
