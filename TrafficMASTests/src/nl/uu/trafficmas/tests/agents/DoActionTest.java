package nl.uu.trafficmas.tests.agents;

import static org.junit.Assert.assertEquals;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
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

public class DoActionTest {

	@Test
	public void doAction() {
		Random random = new Random(1337);

		DataModel dataModel = new DataModelXML(System.getProperty("user.dir")+"/tests/Agent/DoAction/","MASTest.xml");
		MASData masData = dataModel.getMASData(); 
		
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/Agent/DoAction/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/Agent/DoAction/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/Agent/DoAction/", "RouteTest.xml");
		
		HashMap<Agent,Integer> agentPairList = TrafficMASController.instantiateAgents(masData, random, routes);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);	
		HashMap<String, Agent> currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);
		StateData stateData = SimulationModelTraaS.getStateData(conn, false);

		// All agents have a velocity of 0.
		// The first agent is a granny
		Agent granny = completeAgentMap.get("Agent 0");
		
		// The fourth is a HotShot
		Agent hotshot = completeAgentMap.get("Agent 3");
		
		
		int i = 0;
		while(i++ < masData.simulationLength){
			currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
			stateData = SimulationModelTraaS.getStateData(conn, true);
			currentAgentMap = TrafficMASController.updateAgents(completeAgentMap, rn, stateData);
			rn = TrafficMASController.updateRoadNetwork(rn, stateData);
			try {
				if(currentAgentMap.containsValue(granny) && currentAgentMap.containsValue(hotshot)){
					AgentAction grannyAction = granny.doAction(stateData.currentTimeStep/1000);
					AgentAction hotShotAction = hotshot.doAction(stateData.currentTimeStep/1000);
					assertEquals(null, grannyAction);
					assertEquals(AgentAction.ChangeLane, hotShotAction);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
}
