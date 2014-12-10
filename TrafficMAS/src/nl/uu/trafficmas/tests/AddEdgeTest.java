package nl.uu.trafficmas.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

public class AddEdgeTest {

	@Test
	public void addEdge() {
		RoadNetwork rn = new RoadNetwork();
		Node n = new Node("test",5,5);
		Road r = new Road(5.0,null,1);
		rn.addNode(n);
		Edge edge = new Edge(n,n,r);
		rn.addEdge(edge);
		assertEquals(rn.getEdges().length, 1);
		assertEquals(rn.getEdges()[0], edge);
		
		Node n2 = new Node("test2",5,5);
		rn.addEdge(n, n2, r);	
		assertEquals(rn.getEdges().length, 2);
		assertNotEquals(rn.getEdges()[1], edge);
	}

}
