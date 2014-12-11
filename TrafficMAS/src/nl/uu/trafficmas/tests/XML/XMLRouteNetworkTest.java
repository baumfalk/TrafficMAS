package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;
import nl.uu.trafficmas.DataModelXML;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

public class XMLRouteNetworkTest {

	@Test
	public void test() {
		
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/", "NodeTest.xml", "EdgeTest.xml");
		
		assertEquals(rn.getNodes().length,2);
		assertEquals(rn.getEdges().length,1);
	}
}
