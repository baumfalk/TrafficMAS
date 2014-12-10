package nl.uu.trafficmas.tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import nl.uu.trafficmas.TrafficModelXML;
import nl.uu.trafficmas.roadnetwork.Node;

import org.junit.Test;

public class XMLNodeTest {

	private HashMap<String, Node> nodes;

	@Test
	public void test() {
		TrafficModelXML model = new TrafficModelXML();
//		<node id="A28H0" x="-350.0" y="0.0" />
//	    <node id="A28H350" x="0.0" y="0.0" /> 
		nodes = model.extractNodes("tests/", "NodeTest.xml");
		
		assertEquals(nodes.size(),2); 
		assertEquals(nodes.get("A28H0"),new Node("A28H0",-350,0));
		assertEquals(nodes.get("A28H350"),new Node("A28H350",0,0));
	}
}
