package nl.uu.trafficmas.tests.controller;

import static org.junit.Assert.*;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;

import org.junit.Test;

public class SelectAgentProfileTypeTest {

	@Test
	public void selectAgentProfileTypeTest() {
		DataModel dataModel = new DataModelXML("tests/Controller/SelectAgentProfileType/","MASTest.xml");
		MASData masData = dataModel.getMASData();
		double coinFlip = 0.04;
		
		AgentProfileType agentType = TrafficMASController.selectAgentProfileType(coinFlip, masData.agentProfileTypeDistribution);
		assertEquals(AgentProfileType.HotShot,agentType);
		
		coinFlip = 0.05;
		agentType = TrafficMASController.selectAgentProfileType(coinFlip, masData.agentProfileTypeDistribution);
		assertEquals(AgentProfileType.OldLady,agentType);
		
		coinFlip = 0.95;
		agentType = TrafficMASController.selectAgentProfileType(coinFlip, masData.agentProfileTypeDistribution);
		assertEquals(AgentProfileType.Normal,agentType);
	}
}
