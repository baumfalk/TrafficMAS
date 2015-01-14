package nl.uu.trafficmas.tests.agents;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.NormalAgent;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

import org.junit.Test;

public class SetRoadTest {

	@Test
	public void setRoad() {
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/Agent/SetRoad/", "takeOverScenario.nod.xml", "takeOverScenario.edg.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/Agent/SetRoad/", "takeOverScenario.rou.xml");		
		Road r = rn.getRoadFromID("edge1");
		
		Agent a = new NormalAgent("Agent1", rn.getNodes()[2], routes.get(0), 6000, 70.0, 70.0);
		// Route consists of 3 roads now.
		assertEquals(3,a.getRoute().length);
		
		Edge secondToLast = a.getRoute()[1];
		Edge lastEdge = a.getRoute()[2];
		
		// Move one road ahead
		a.setRoad(r);
		
		//Route should be length 2 now
		assertEquals(2,a.getRoute().length);
		
		// And the last two edges should still be in order.
		assertEquals(a.getRoute()[0],secondToLast);
		assertEquals(a.getRoute()[1],lastEdge);
	}
}
