package nl.uu.trafficmas.tests;

import static org.junit.Assert.*;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

public class RemoveNodeTest {

	@Test
	public void test() {
		RoadNetwork rn = new RoadNetwork();
		Node n = new Node("test");
		rn.addNode(n);
		rn.removeNode(n);
		assertEquals(rn.getNodes().length, 0);
	}

}
