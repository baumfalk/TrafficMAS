package nl.uu.trafficmas.tests.SimulationModelTraaS;

import static org.junit.Assert.*;

import java.util.ArrayList;

import it.polito.appeal.traci.SumoTraciConnection;
import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.SimulationModelTraaS;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;

public class GetAgentPhysicalTest {

	@Test
	public void getAgentPhysical() {
		SumoTraciConnection conn = SimulationModelTraaS.initialize("sumo", "./tests/ConfigTest.xml");
		ArrayList<AgentPhysical> agentPhysList = new ArrayList<AgentPhysical>();
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("./tests/", "NodeTest.xml", "EdgeTest.xml");
		Agent a1 = new NormalAgent("agent1", rn.getNodes()[0], 2000, 70.0);

		// TODO: More testing.
		try {
			SimulationModelTraaS.addAgent(a1, "route0",1000,conn);
			conn.do_timestep();
			agentPhysList = SimulationModelTraaS.getAgentsPhysical(rn, conn);
			AgentPhysical aPhys = agentPhysList.get(0);
			assertEquals("A28Tot350", aPhys.getRoad().id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
