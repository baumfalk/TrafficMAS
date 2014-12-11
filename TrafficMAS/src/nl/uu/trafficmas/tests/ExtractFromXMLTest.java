package nl.uu.trafficmas.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.uu.trafficmas.KeyValue;
import nl.uu.trafficmas.SimpleXMLReader;
import nl.uu.trafficmas.DataModelXML;

import org.junit.Test;

public class ExtractFromXMLTest {

	@Test
	public void test() {

		ArrayList<ArrayList<KeyValue<String, String>>> result = SimpleXMLReader.extractFromXML("tests/", "ExtractFromXMLTest.xml", "test1");
		assertNotNull("Result shouldn't be null", result);
		assertEquals(result.size(),1);
		assertEquals(result.get(0).size(),2);
		int i =0;
		for (KeyValue<String, String> attribute : result.get(0)) {
			if(i==0) {
				assertEquals(attribute.key,"aap");
				assertEquals(attribute.value,"noot");
			}
			else if(i==1) {
				assertEquals(attribute.key,"mies");
				assertEquals(attribute.value,"schaap");
			}
			i++;
		}
	}
}
