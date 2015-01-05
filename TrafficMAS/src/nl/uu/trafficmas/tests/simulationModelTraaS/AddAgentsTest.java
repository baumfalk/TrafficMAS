package nl.uu.trafficmas.tests.simulationModelTraaS;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import it.polito.appeal.traci.SumoTraciConnection;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.datamodel.Pair;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;

import org.junit.Test;

import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;

public class AddAgentsTest {

	@Test
	public void addAgents() {
		Random random = new Random(1337);
		 
		DataModel dataModel = new DataModelXML("tests/SimulationModelTraaS/AddAgents/","MASTest.xml");
		MASData masData = dataModel.getMASData();
		
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/SimulationModelTraaS/AddAgents/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/SimulationModelTraaS/AddAgents/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/SimulationModelTraaS/AddAgents/", "RouteTest.xml");

		HashMap<Agent,Integer> agentPairList = TrafficMASController.instantiateAgents(masData, random, routes);
		
		SimulationModelTraaS.addAgents(agentPairList, conn);

		try {
			int i = 0;
			while(i++ < masData.simulationLength){
				conn.do_timestep();
				
				if(i == 110){
					SumoStringList vehicleIDList = (SumoStringList) conn.do_job_get(Vehicle.getIDList());
					assertEquals(1,vehicleIDList.size()); // Length is always 1 at this time with this seed.
				}
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
