package nl.uu.trafficmas.tests.roadNetwork;

import static org.junit.Assert.*;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

import org.junit.Test;

public class GetRoadFromIDTest {

	@Test
	public void getRoadFromID() {
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork("./tests/RoadNetwork/", "NodeTest.xml", "EdgeTest.xml");
		Road r = rn.getRoadFromID("A28Tot350");
		assertEquals("A28Tot350", r.id);
		assertEquals(2, r.laneList.size());
	}
}
