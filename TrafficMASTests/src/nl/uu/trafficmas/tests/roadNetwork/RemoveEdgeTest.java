package nl.uu.trafficmas.tests.roadNetwork;

import static org.junit.Assert.*;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

public class RemoveEdgeTest {

	@Test
	public void removeEdge() {
		RoadNetwork rn = new RoadNetwork();
		
		Node n = new Node("test",0,0);
		Road r = new Road("r",5,null,1);
		rn.addNode(n);
		
		Edge edge = new Edge(n,n,r);
		rn.addEdge(edge);
		assertEquals(rn.getEdges().length, 1);
		rn.removeEdge(edge);
		assertEquals(rn.getEdges().length, 0);
	}

}
