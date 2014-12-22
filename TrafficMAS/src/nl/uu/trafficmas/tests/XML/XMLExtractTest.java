package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.Pair;
import nl.uu.trafficmas.datamodel.SimpleXMLReader;

import org.junit.Test;

public class XMLExtractTest {

	@Test
	public void test() {

		ArrayList<ArrayList<Pair<String, String>>> result = SimpleXMLReader.extractFromXML("tests/", "ExtractFromXMLTest.xml", "test1");
		assertNotNull("Result shouldn't be null", result);
		assertEquals(result.size(),1);
		assertEquals(result.get(0).size(),2);
		int i =0;
		for (Pair<String, String> attribute : result.get(0)) {
			if(i==0) {
				assertEquals(attribute.first,"aap");
				assertEquals(attribute.second,"noot");
			}
			else if(i==1) {
				assertEquals(attribute.first,"mies");
				assertEquals(attribute.second,"schaap");
			}
			i++;
		}
	}
}
