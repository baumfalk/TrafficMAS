package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

import org.junit.Test;

public class XMLGetRoutesTest {

	@Test
	public void test() {
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("tests/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, "tests/", "RouteTest.xml");
		assertEquals(routes.size(),1);
		
		rn = DataModelXML.instantiateRoadNetwork("tests/", "NodeTest2.xml", "EdgeTest2.xml");
		routes = DataModelXML.getRoutes(rn, "tests/", "RouteTest2.xml");
		assertEquals(routes.size(),2);
		assertEquals(routes.get(1).routeID,"route1");
	}
}
