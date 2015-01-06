package nl.uu.trafficmas.tests.agents;

import static org.junit.Assert.*;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.roadnetwork.Edge;

import org.junit.Test;

public class AgentUtilityTest {

	@Test
	public void test() {
		AgentProfileType[] agentProfileTypes = AgentProfileType.values();
		Edge[] lst = new Edge[0];
		// goal achieved should be utility 1
		for(AgentProfileType apt : agentProfileTypes) {
			Agent agent = apt.toAgent(Agent.getNextAgentID(),null, lst, 10, 10, 0);
			assertEquals(1.0,agent.specificUtility(agent.getGoalArrivalTime(), null),0);
		}
	}
}
