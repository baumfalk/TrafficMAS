package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.Pair;
import nl.uu.trafficmas.datamodel.SimpleXMLReader;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;

import org.junit.Test;

public class XMLExtractEdgeTest {

	@Test
	public void extractEdge() {
		HashMap<String,Node> nodes = DataModelXML.extractNodes(System.getProperty("user.dir")+"/tests/XML/ExtractEdge/","NodeTest2.xml");
		ArrayList<ArrayList<Pair<String,String>>> edgesAttributes = SimpleXMLReader.extractFromXML(System.getProperty("user.dir")+"/tests/XML/ExtractEdge/", "EdgeTest2.xml","edge");
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		DataModelXML.extractEdge(edges, edgesAttributes.get(0), nodes);
		assertEquals("A28Tot350", edges.get(0).getID());
		assertEquals("A28H0" ,edges.get(0).getFromNode().name);
		assertEquals("A28H350" ,edges.get(0).getToNode().name);
		
		Road r = edges.get(0).getRoad();
		assertEquals(2 ,r.priority);
		assertEquals(2,r.laneList.size());
		assertEquals(350,r.length,0.0);	
	}
}