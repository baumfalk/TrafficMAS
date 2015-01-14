package nl.uu.trafficmas.tests.XML;

import static org.junit.Assert.*;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;

import org.junit.Test;

public class XMLgetMASDataTest {

	@Test
	public void getMASData() {
		MASData masData = DataModelXML.getMASData(System.getProperty("user.dir")+"/tests/XML/GetMASData/", "MASTest.xml", "ConfigTest.xml", "AgentProfileTypesTest.xml", "RouteTest.xml");
		assertEquals(1000,masData.simulationLength);
		assertEquals(0.01, masData.spawnProbability, 0.0);
		assertEquals("ConfigTest.xml",masData.sumoConfigPath);
		assertEquals(0.05,masData.agentProfileTypeDistribution.get(AgentProfileType.HotShot),0.0);
		assertEquals(0.4, masData.routeIdAndProbability.get("route0"),0);
	}
}
