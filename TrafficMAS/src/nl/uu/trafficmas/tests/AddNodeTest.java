package nl.uu.trafficmas.tests;

import static org.junit.Assert.*;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

public class AddNodeTest {

	@Test
	public void addNode() {
		RoadNetwork rn = new RoadNetwork();
		Node n = new Node("test");
		rn.addNode(n);
		
		assertEquals(rn.getNodes().length, 1);
		assertEquals(rn.getNodes()[0], n);
	}
}
