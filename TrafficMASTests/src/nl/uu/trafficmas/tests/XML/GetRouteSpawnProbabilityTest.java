package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import nl.uu.trafficmas.datamodel.DataModelXML;

import org.junit.Test;

public class GetRouteSpawnProbabilityTest {

	@Test
	public void GetRouteSpawnProbability() {
		LinkedHashMap<String, Double> routeIdAndProbability = DataModelXML.getRouteSpawnProbability(System.getProperty("user.dir")+"/tests/XML/GetRouteSpawnProbability/", "RouteTest.xml");
		for(Map.Entry<String,Double> entry : routeIdAndProbability.entrySet()){
			assertEquals("route0", entry.getKey());
			assertEquals(0.4, entry.getValue(), 0);
			break;
		}		
	}
}
