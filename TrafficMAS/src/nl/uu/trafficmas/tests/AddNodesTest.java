package nl.uu.trafficmas.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

public class AddNodesTest {

	@Test
	public void test() {
		RoadNetwork rn = new RoadNetwork();
		Node n1 = new Node();
		Node n2 = new Node();
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(n1);
		nodes.add(n2);
		
		rn.addNodes(nodes);
		
		assertEquals(rn.getNodes().length, 2);
		assertEquals(rn.getNodes()[0], n1);
		assertEquals(rn.getNodes()[1], n2);
	}
}
