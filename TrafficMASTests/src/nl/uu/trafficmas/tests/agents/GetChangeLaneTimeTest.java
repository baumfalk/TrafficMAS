package nl.uu.trafficmas.tests.agents;

import static org.junit.Assert.assertEquals;
import nl.uu.trafficmas.agent.actions.ChangeLaneAction;

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
		
		ChangeLaneAction action = new ChangeLaneAction(0);
		assertEquals(6,action.getTime(5, 0, 2, 1, 2, 2, 1, 0, 0,null),0);
	}
}
