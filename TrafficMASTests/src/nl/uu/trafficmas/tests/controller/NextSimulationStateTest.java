package nl.uu.trafficmas.tests.controller;

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

import de.tudresden.sumo.cmd.Simulation;

public class NextSimulationStateTest {

	@Test
	public void nextSimulationState() {
Random random = new Random(1337);
		
		DataModel dataModel = new DataModelXML(System.getProperty("user.dir")+"/tests/SimulationModelTraaS/NextSimulationState/","MASTest.xml");
		MASData masData = dataModel.getMASData(); 
		
		HashMap<String, String> options = new LinkedHashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/SimulationModelTraaS/NextSimulationState/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/SimulationModelTraaS/NextSimulationState/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/SimulationModelTraaS/NextSimulationState/", "RouteTest.xml");
		
		HashMap<Agent,Integer> agentPairList = TrafficMASController.instantiateAgents(masData, random, routes);
		SimulationModelTraaS.addAgents(agentPairList, conn);
		
		StateData stateData = SimulationModelTraaS.getStateData(conn, false);
		int i = 0;
		while(i++ < masData.simulationLength){
			try {
				stateData = SimulationModelTraaS.getStateData(conn, true);
				if(i == 4){
					assertEquals(5000,conn.do_job_get(Simulation.getCurrentTime()));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
}
