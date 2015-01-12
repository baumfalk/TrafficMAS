package nl.uu.trafficmas.tests.agents;

import static org.junit.Assert.*;
import nl.uu.trafficmas.agent.actions.AgentAction;

import org.junit.Test;

public class GetChangeLaneTimeTest {

	@Test
	public void getChangeLaneTime() {
		/*
		 * change lane time (no knowledge about RN)
		 * 
		 * lane_remainder       rest_route_length
		 * --------------   +   ----------------- + current_time
		 * next_lane_speed      max_comfy_speed
		 */
		// change lane time:
		
		
		AgentAction action = AgentAction.ChangeLane;
		assertEquals(6,action.getChangeLaneTime(5, 2, 1, 2, 2, 1),0);	
	}
}
