package nl.uu.trafficmas.tests.SimulationModelTraaS;

import static org.junit.Assert.*;

import java.util.ArrayList;

import it.polito.appeal.traci.SumoTraciConnection;
import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.Pair;
import nl.uu.trafficmas.SimulationModelTraaS;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;

public class AddAgentsTest {

	@Test
	public void addAgents() {
		SumoTraciConnection conn = SimulationModelTraaS.initialize("sumo", "./tests/ConfigTest.xml");
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("./tests/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Pair<Agent, Integer>> agentPairList = new ArrayList<Pair<Agent, Integer>>();
		Agent a1 = new NormalAgent("agent1", rn.getNodes()[1], 6000, 70.0);
		Agent a2 = new NormalAgent("agent2", rn.getNodes()[1], 6000, 70.0);
		Pair<Agent, Integer> agentPair1 = new Pair<Agent, Integer>(a1, 1000);
		Pair<Agent, Integer> agentPair2 = new Pair<Agent, Integer>(a2, 3000);
		
		agentPairList.add(agentPair1);
		agentPairList.add(agentPair2);
		
		SimulationModelTraaS.addAgents(agentPairList, conn);
		try {
			//Simtime 1000
			SumoStringList vehicleIDList = (SumoStringList) conn.do_job_get(Vehicle.getIDList());
			assertEquals(0, vehicleIDList.size());
			conn.do_timestep();
			//Simtime 2000 
			// Car can't spawn because the other agent is still in the way.
			vehicleIDList = (SumoStringList) conn.do_job_get(Vehicle.getIDList());
			assertEquals(1, vehicleIDList.size());
			conn.do_timestep();
			//Simtime 3000
			vehicleIDList = (SumoStringList) conn.do_job_get(Vehicle.getIDList());
			assertEquals(1, vehicleIDList.size());
			conn.do_timestep();
			//Simtime 4000
			vehicleIDList = (SumoStringList) conn.do_job_get(Vehicle.getIDList());
			assertEquals(2, vehicleIDList.size());
			conn.do_timestep();
			assertNotEquals(a1.agentID, vehicleIDList.get(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
