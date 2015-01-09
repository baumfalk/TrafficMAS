package nl.uu.trafficmas.tests.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModel;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;

import org.junit.Test;

public class SetupSimulationTest {

	@Test
	public void setupSimulation() {
		Random rng = new Random(1337);
		DataModel dataModel = new DataModelXML("tests/Controller/SetupSimulation/","MASTest.xml");
		SimulationModel simModel 		= new SimulationModelTraaS("sumo",dataModel.getSumoConfigPath());
		
		MASData masData = dataModel.getMASData();
		
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/Controller/SetupSimulation/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/Controller/SetupSimulation/", "RouteTest.xml");

		HashMap<Agent,Integer> agentsAndTime = TrafficMASController.instantiateAgents(masData, rng, routes);

		HashMap<String, Agent> completeAgentMap = TrafficMASController.setupSimulation(masData, simModel, agentsAndTime);
		
		assertEquals(9, completeAgentMap.size());

	}

}
