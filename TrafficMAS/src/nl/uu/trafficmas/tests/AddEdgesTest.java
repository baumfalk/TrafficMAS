package nl.uu.trafficmas.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

public class AddEdgesTest {

	@Test
	public void addEdges() {
		RoadNetwork rn = new RoadNetwork();
		Node n1 = new Node();
		Node n2 = new Node();
		Road r1 = new Road();
		Road r2 = new Road();

		//rn.addNode(n1);
		//rn.addNode(n2);
		Edge edge1 = new Edge(n1,n1,r1);
		Edge edge2 = new Edge(n1,n2,r2);
		Edge edge3 = new Edge(n2, n2, r2);
		ArrayList<Edge> edges = new ArrayList<Edge>();
		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);

		rn.addEdges(edges);
		assertEquals(rn.getEdges().length, 3);
		assertEquals(rn.getEdges()[0], edge1);
		assertEquals(rn.getEdges()[1], edge2);
		assertEquals(rn.getEdges()[2], edge3);
	
	}
}
