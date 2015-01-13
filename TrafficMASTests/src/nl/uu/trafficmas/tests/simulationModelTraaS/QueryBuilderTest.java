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

public class QueryBuilderTest {

	@Test
	public void queryBuilder() {
		Random random = new Random(1337);
		
		DataModel dataModel = new DataModelXML(System.getProperty("user.dir")+"/tests/SimulationModelTraaS/QueryBuilder/","MASTest.xml");
		MASData masData = dataModel.getMASData(); 
		
		HashMap<String, String> options = new LinkedHashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/SimulationModelTraaS/QueryBuilder/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/SimulationModelTraaS/QueryBuilder/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/SimulationModelTraaS/QueryBuilder/", "RouteTest.xml");
		
		HashMap<Agent,Integer> agentPairList = TrafficMASController.instantiateAgents(masData, random, routes);
		SimulationModelTraaS.addAgents(agentPairList, conn);
		
		StateData stateData = SimulationModelTraaS.getStateData(conn, false);
		assertNotNull(stateData);
		int i = 0;
		while(i++ < masData.simulationLength){
			try {
				stateData = SimulationModelTraaS.getStateData(conn, true);
				if(!stateData.agentsData.isEmpty()) {
					for(Entry<String, AgentData> val : stateData.agentsData.entrySet()) {
						assertNotNull(val.getValue().laneIndex);
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
