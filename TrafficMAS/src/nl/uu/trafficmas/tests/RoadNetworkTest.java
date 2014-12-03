package nl.uu.trafficmas.tests;

import static org.junit.Assert.*;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

public class RoadNetworkTest {

	@Test
	public void addNode() {
		RoadNetwork rn = new RoadNetwork();
		Node n = new Node();
		rn.addNode(n);
		
		assertEquals(rn.getNodes().length, 1);
		assertEquals(rn.getNodes()[0], n);
	}
	
	@Test
	public void addEdge() {
		RoadNetwork rn = new RoadNetwork();
		Node n = new Node();
		Road r = new Road();
		rn.addNode(n);
		Edge edge = new Edge(n,n,r);
		rn.addEdge(edge);
		assertEquals(rn.getEdges().length, 1);
		assertEquals(rn.getEdges()[0], edge);
		
		Node n2 = new Node();
		rn.addEdge(n, n2, r);	
		assertEquals(rn.getEdges().length, 2);
		assertNotEquals(rn.getEdges()[1], edge);
	}

}
