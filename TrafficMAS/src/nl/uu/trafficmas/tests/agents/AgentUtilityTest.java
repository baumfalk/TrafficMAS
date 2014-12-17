package nl.uu.trafficmas.tests.agents;

import static org.junit.Assert.*;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;

import org.junit.Test;

public class AgentUtilityTest {

	@Test
	public void test() {
		AgentProfileType[] agentProfileTypes = AgentProfileType.values();
		
		// goal achieved should be utility 1
		for(AgentProfileType apt : agentProfileTypes) {
			Agent agent = apt.toAgent(Agent.getNextAgentID(), null, 10, 10);
			assertEquals(agent.specificUtility(agent.getGoalArrivalTime(), null),1,0);
		}
	}

}
