package nl.uu.trafficmas.tests.SimulationModelTraaS;

import static org.junit.Assert.*;
import it.polito.appeal.traci.SumoTraciConnection;
import nl.uu.trafficmas.SimulationModelTraaS;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.roadnetwork.Node;
import org.junit.Test;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;

public class AddAgentTest {

	@Test
	public void addAgent() {
		SumoTraciConnection conn = SimulationModelTraaS.initialize("sumo", "./tests/ConfigTest.xml");
		Node n = new Node("A28H350", 0.0, 0.0);
		Agent a1 = new NormalAgent("agent1", n, 2000, 70.0);
		SimulationModelTraaS.addAgent(a1, "route0",1000,conn);

		try {
			conn.do_timestep();
			int numCars = (int) conn.do_job_get(Vehicle.getIDCount());
			assertEquals(1,numCars);
			
			SumoStringList agentIDs = (SumoStringList)conn.do_job_get(Vehicle.getIDList());
			assertEquals("agent1",agentIDs.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
