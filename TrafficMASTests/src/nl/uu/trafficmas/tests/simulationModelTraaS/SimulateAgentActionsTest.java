package nl.uu.trafficmas.tests.simulationModelTraaS;

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

public class SimulateAgentActionsTest {

	@Test
	public void simulateAgentActions() {
		Random random = new Random(1337);

		DataModel dataModel = new DataModelXML(System.getProperty("user.dir")+"/tests/SimulationModelTraaS/SimulateAgentActions/","MASTest.xml");
		MASData masData = dataModel.getMASData(); 
		
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/SimulationModelTraaS/SimulateAgentActions/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/SimulationModelTraaS/SimulateAgentActions/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/SimulationModelTraaS/SimulateAgentActions/", "RouteTest.xml");
		
		HashMap<Agent,Integer> agentPairList = TrafficMASController.instantiateAgents(masData, random, routes);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);	
		HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);
		HashMap<Agent, AgentAction> actions;

		StateData stateData = SimulationModelTraaS.getStateData(conn, false);

		int i = 0;
		while(i++ < masData.simulationLength){
			try {
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
				stateData = SimulationModelTraaS.getStateData(conn, true);
	
				currentAgentMap = TrafficMASController.updateAgents(completeAgentMap, rn, stateData);
				rn = TrafficMASController.updateRoadNetwork(rn, stateData);
				actions = TrafficMASController.getAgentActions(stateData.currentTimeStep/1000, currentAgentMap);
				SimulationModelTraaS.simulateAgentActions(actions, conn);
				int laneIndexes =0 ;
				if (stateData.currentTimeStep == 12000){
					for(Map.Entry<String, Agent> entry : currentAgentMap.entrySet()){
						laneIndexes += entry.getValue().getLane().laneIndex;
						// If this is true, one of the agents must have switches lanes (i.e. succesfully completed an action.
						assertEquals(1, laneIndexes);
					}
					
				}		
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
}
