package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;
import nl.uu.trafficmas.datamodel.DataModelXML;

import org.junit.Test;

public class XMLSimulationLengthTest {

	@Test
	public void testSimulationLengthStringString() {
		int simulationLength = DataModelXML.simulationLength("tests/", "MASTest.xml");
		assertEquals(simulationLength,1000);
	}

}
