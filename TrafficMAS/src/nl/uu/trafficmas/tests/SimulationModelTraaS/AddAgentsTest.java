package nl.uu.trafficmas.tests.SimulationModelTraaS;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import it.polito.appeal.traci.SumoTraciConnection;
import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.Pair;
import nl.uu.trafficmas.SimulationModelTraaS;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

import org.junit.Test;

import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;

public class AddAgentsTest {

	@Test
	public void addAgents() {
		Random random = new Random(1337);
		int simLength = 20;

		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(simLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", "./tests/ConfigTest.xml");				
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/", "RouteTest.xml");
		ArrayList<Pair<AgentProfileType, Double>> dist = DataModelXML.getAgentProfileTypeDistribution("tests/", "AgentProfileTypesTest.xml");
		
		double agentSpawnProb = 0.5;
		ArrayList<Pair<Agent, Integer>> agentPairList = DataModelXML.instantiateAgents(random, routes, simLength, agentSpawnProb, dist);
		SimulationModelTraaS.addAgents(agentPairList, conn);
		try {
			int i = 0;
			while(i++ < simLength){
				conn.do_timestep();			
				if(i == 4){
					SumoStringList vehicleIDList = (SumoStringList) conn.do_job_get(Vehicle.getIDList());
					System.out.println(vehicleIDList.size());
					assertEquals(1,vehicleIDList.size()); // Length is always 1 at this time with this seed.
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
