package nl.uu.trafficmas.tests.agents;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Route;

import org.junit.Test;

public class AgentUtilityTest {

	@Test
	public void agentUtility() {
		AgentProfileType[] agentProfileTypes 	= AgentProfileType.values();
		ArrayList<Edge> lst 					= new ArrayList<Edge>();
		Route route = new Route("route0", lst);
		// goal achieved should be utility 1
		for(AgentProfileType apt : agentProfileTypes) {
			Agent agent = apt.toAgent(Agent.getNextAgentID(),null, route, 10, 10);
			assertEquals(1.0,agent.specificUtility(agent.getGoalArrivalTime(), null),0);
		}
	}
}
