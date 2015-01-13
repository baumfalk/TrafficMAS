package nl.uu.trafficmas.tests.controller;

import static org.junit.Assert.*;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;
import nl.uu.trafficmas.simulationmodel.StateData;

import org.junit.Test;

public class GetAgentActionsTest {

	@Test
	public void test() {
		Random random = new Random(1337);

		DataModel dataModel = new DataModelXML(System.getProperty("user.dir")+"/tests/Controller/GetAgentActions/","MASTest.xml");
		MASData masData = dataModel.getMASData(); 
		
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", System.getProperty("user.dir")+"/tests/Controller/GetAgentActions/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/Controller/GetAgentActions/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/Controller/GetAgentActions/", "RouteTest.xml");
		
		HashMap<Agent,Integer> agentPairList = TrafficMASController.instantiateAgents(masData, random, routes);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);	
		HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);
		HashMap<Agent, AgentAction> agentActionMap;

		StateData stateData = SimulationModelTraaS.getStateData(conn, false);
		
		int i = 0;
		while(i++ < masData.simulationLength){
			currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
			stateData = SimulationModelTraaS.getStateData(conn, true);

			currentAgentMap = TrafficMASController.updateAgents(completeAgentMap, rn, stateData);
			rn = TrafficMASController.updateRoadNetwork(rn, stateData);
			agentActionMap = TrafficMASController.getAgentActions(stateData.currentTimeStep/1000, currentAgentMap);
			
			try {
				// The fourth agent will ChangeLane, the rest does not need to take action.
				if(stateData.currentTimeStep == 12000){
					for(Map.Entry<Agent, AgentAction> entry : agentActionMap.entrySet()){
						if(entry.getKey().getClass().getSimpleName().equals("HotShotAgent")){
							assertEquals(AgentAction.ChangeLane,entry.getValue());
						} else{
							assertEquals(null,entry.getValue());
						}
					}
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
}
