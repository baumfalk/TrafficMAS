package nl.uu.trafficmas.tests;

import static org.junit.Assert.*;
import nl.uu.trafficmas.roadnetwork.*;

import org.junit.Test;

public class ClearTest {

	@Test
	public void clear() {
		RoadNetwork rn = new RoadNetwork();
		Node n1 = new Node();
		Node n2 = new Node();
		Road r1 = new Road();
		Edge e1 = new Edge(n1,n2,r1);
		
		rn.addNode(n1);
		rn.addNode(n2);
		rn.addEdge(e1);
		rn.clear();
		assertEquals(0, rn.getNodes().length);
		assertEquals(0, rn.getEdges().length);
	}

}
