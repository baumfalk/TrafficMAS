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

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;

public class AddAgentsTest {

	@Test
	public void addAgents() {
		Random random = new Random(1337);
		 
		DataModel dataModel = new DataModelXML(System.getProperty("user.dir")+"/tests/SimulationModelTraaS/AddAgents/","MASTest.xml");
		MASData masData = dataModel.getMASData();
		
		HashMap<String, String> options = new LinkedHashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", System.getProperty("user.dir")+"/tests/SimulationModelTraaS/AddAgents/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/SimulationModelTraaS/AddAgents/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/SimulationModelTraaS/AddAgents/", "RouteTest.xml");

		HashMap<Agent,Integer> agentPairList = TrafficMASController.instantiateAgents(masData, random, routes);
		
		SimulationModelTraaS.addAgents(agentPairList, conn);

		try {
			int i = 0;
			while(i++ < masData.simulationLength){
				StateData stateData = SimulationModelTraaS.getStateData(conn, true);				
				if(i == 8){
					assertEquals(3,stateData.agentsData.size());
				}
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
