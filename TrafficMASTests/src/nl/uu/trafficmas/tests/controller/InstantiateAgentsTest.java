package nl.uu.trafficmas.tests.controller;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;

import org.junit.Test;

public class InstantiateAgentsTest {

	//TODO: Make test for new instantiateAgent functionality (Spawning agents on every route.)

	@Test
	public void instantiateAgents() {
		Random random = new Random(1337);
	
		DataModel dataModel = new DataModelXML(System.getProperty("user.dir")+"/tests/Controller/InstantiateAgents/","MASTest.xml");
		MASData masData = dataModel.getMASData();
		
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/Controller/InstantiateAgents/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/Controller/InstantiateAgents/", "RouteTest.xml");

		HashMap<Agent,Integer> agentPairList = TrafficMASController.instantiateAgents(masData, random, routes);
		
		assertNotEquals(agentPairList.size(), 0);
		
		for(Entry<Agent, Integer> pair : agentPairList.entrySet()) {
			assertNotNull(pair.getKey());
			assertNotEquals(0,(int)pair.getValue());
		}
		
	}
}
