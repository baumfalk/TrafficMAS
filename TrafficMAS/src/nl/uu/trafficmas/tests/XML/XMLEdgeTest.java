package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.LaneType;
import nl.uu.trafficmas.roadnetwork.Node;

import org.junit.Test;

public class XMLEdgeTest {

	@Test
	public void test() {
		//    <edge from="A28H0" id="A28Tot350" to="A28H350" numLanes="2" priority="2" />

		HashMap<String, Node> nodes = DataModelXML.extractNodes("tests/", "NodeTest.xml");
		ArrayList<Edge> edges = DataModelXML.extractEdges("tests/", "EdgeTest.xml", nodes);
		assertNotNull(edges);
		assertEquals(edges.size(),1);
		assertEquals(edges.get(0).getFromNode().name,"A28H0");
		assertEquals(edges.get(0).getToNode().name,"A28H350");
		assertEquals(edges.get(0).getRoad().priority,2);
		assertEquals(edges.get(0).getRoad().getLanes().length,2);

		assertEquals(edges.get(0).getRoad().getLanes()[0].laneType,LaneType.Normal);
		assertEquals(edges.get(0).getRoad().getLanes()[1].laneType,LaneType.Normal);
		double l = Node.nodeDistance(edges.get(0).getFromNode(), edges.get(0).getToNode());
		assertEquals(edges.get(0).getRoad().length, l,0);
		
		
	}

}
