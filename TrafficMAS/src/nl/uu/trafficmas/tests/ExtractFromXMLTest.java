package nl.uu.trafficmas.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.uu.trafficmas.KeyValue;
import nl.uu.trafficmas.SimpleXMLReader;
import nl.uu.trafficmas.TrafficModelXML;

import org.junit.Test;

public class ExtractFromXMLTest {

	@Test
	public void test() {

		ArrayList<ArrayList<KeyValue<String, String>>> result = SimpleXMLReader.extractFromXML("", "ExtractFromXMLTest.xml", "test1");
		assertNotNull("Result shouldn't be null", result);
		assertEquals(result.size(),1);
		fail("Not yet implemented");
	}

}
