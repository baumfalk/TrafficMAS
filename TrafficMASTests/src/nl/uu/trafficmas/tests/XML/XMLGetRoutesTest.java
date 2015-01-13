package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

import org.junit.Test;

public class XMLGetRoutesTest {

	@Test
	public void xmlGetRoutes() {
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/XML/GetRoutes/", "NodeTest.xml", "EdgeTest.xml");
		ArrayList<Route> routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/XML/GetRoutes/", "RouteTest.xml");
		assertEquals(routes.size(),1);
		
		rn = DataModelXML.instantiateRoadNetwork(System.getProperty("user.dir")+"/tests/XML/GetRoutes/", "NodeTest2.xml", "EdgeTest2.xml");
		routes = DataModelXML.getRoutes(rn, System.getProperty("user.dir")+"/tests/XML/GetRoutes/", "RouteTest2.xml");
		assertEquals(routes.size(),2);
		assertEquals(routes.get(1).routeID,"route1");
	}
}
