package nl.uu.trafficmas.tests.norms;

import static org.junit.Assert.assertEquals;
import nl.uu.trafficmas.exception.InvalidParameterCombination;
import nl.uu.trafficmas.norm.MergeNormScheme;

import org.junit.Test;

public class MergeNormGetArrivalTimeTest {

	@Test(expected=InvalidParameterCombination.class)
	public void case1() {
		Double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= 1.0;
		acceleration 	= -1.0;
		distRemaining 	= 0.0;
		vprime 			= 2.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
	}
	
	@Test
	public void case2() {
		Double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= 0.0;
		acceleration 	= 0.0;
		distRemaining 	= 0.0;
		vprime 			= 0.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
		assertEquals(0,result,0.0);
	}
	
	@Test
	public void case3() {
		Double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= 0.0;
		acceleration 	= 0.0;
		distRemaining 	= 0.0;
		vprime 			= 0.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
		assertEquals(0,result,0.0);
	}

}
