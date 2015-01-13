package nl.uu.trafficmas.tests.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

import org.junit.Test;


public class CreateAgentTest {

	@Test
	public void createAgent() {
		DataModel dataModel = new DataModelXML(System.getProperty("user.dir")+"/tests/Controller/InstantiateAgents/","MASTest.xml");
		MASData masData = dataModel.getMASData();
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/Controller/CreateAgent/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/Controller/CreateAgent/", "RouteTest.xml");
		LinkedHashMap<Agent, Integer> agentsAndTimes =  new LinkedHashMap<Agent, Integer>();
		assertEquals(0, agentsAndTimes.size());
		
		TrafficMASController.createAgent(routes, agentsAndTimes, masData.agentProfileTypeDistribution, 2, 0.5);
		
		assertEquals(1, agentsAndTimes.size());
		Agent a;
		
		// There only is one agent.
		for(Map.Entry<Agent,Integer> entry : agentsAndTimes.entrySet()){
			a = entry.getKey();			
			assertTrue(a.agentID.startsWith("Agent "));
			assertArrayEquals(routes.get(0).getRoute(),a.getRoute());
			assertNotNull(a.getMaxSpeed());
			assertTrue(Arrays.asList(rn.getNodes()).contains(a.getGoalNode()));
			
		}
		

	}

}
