package nl.uu.trafficmas.tests.norms;

import static org.junit.Assert.assertEquals;
import nl.uu.trafficmas.exception.InvalidParameterCombination;
import nl.uu.trafficmas.norm.MergeNormScheme;

import org.junit.Test;

public class MergeNormGetArrivalTimeTest {

	@Test(expected=InvalidParameterCombination.class)
	public void positiveSpeedDeltaNegativeAccel() throws InvalidParameterCombination {
		double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= 1.0;
		acceleration 	= -1.0;
		distRemaining 	= 10.0;
		vprime 			= 2.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
	}
	
	@Test(expected=InvalidParameterCombination.class)
	public void negativeVelocity() throws InvalidParameterCombination {
		double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= -1.0;
		acceleration 	= 1.0;
		distRemaining 	= 10.0;
		vprime 			= 2.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
	}
	
	@Test(expected=InvalidParameterCombination.class)
	public void negativeDist() throws InvalidParameterCombination {
		double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= 1.0;
		acceleration 	= 1.0;
		distRemaining 	= -1.0;
		vprime 			= 2.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
	}
	
	@Test(expected=InvalidParameterCombination.class)
	public void negativeVPrime() throws InvalidParameterCombination {
		double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= 1.0;
		acceleration 	= -1.0;
		distRemaining 	= 10.0;
		vprime 			= -1.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
	}
	
	@Test(expected=InvalidParameterCombination.class)
	public void negativeSpeedDeltaPositiveAccel() throws InvalidParameterCombination {
		double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= 2.0;
		acceleration 	= 1.0;
		distRemaining 	= 10.0;
		vprime 			= 1.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
	}
	
	@Test(expected=InvalidParameterCombination.class)
	public void impossibleToAchieve() throws InvalidParameterCombination {
		double velocity, acceleration, distRemaining, vprime, result;
		velocity 		= 2.0;
		acceleration 	= 1.0;
		distRemaining 	= 1.0;
		vprime 			= 10.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
	}
	
	
	@Test
	public void infinitePosAcceleration() throws InvalidParameterCombination {
		double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= 1.0;
		acceleration 	= Double.POSITIVE_INFINITY;
		distRemaining 	= 10.0;
		vprime 			= 2.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
		assertEquals(5,result,0.0);
	}
	
	@Test
	public void infiniteNegAcceleration() throws InvalidParameterCombination {
		double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= 2.0;
		acceleration 	= Double.NEGATIVE_INFINITY;
		distRemaining 	= 10.0;
		vprime 			= 1.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
		assertEquals(10,result,0.0);
	}
	
	@Test
	public void normalAccelExample() throws InvalidParameterCombination {
		double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= 1.0;
		acceleration 	= 1;
		distRemaining 	= 10.0;
		vprime 			= 3.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
		assertEquals(4,result,0.0);
	}
	
	@Test
	public void normalDecelExample() throws InvalidParameterCombination {
		double velocity, acceleration, distRemaining, vprime, result;
		
		velocity 		= 3.0;
		acceleration 	= -1;
		distRemaining 	= 10.0;
		vprime 			= 1.0;
		result = MergeNormScheme.getArrivalTime(velocity, acceleration, distRemaining, vprime);
		assertEquals(8,result,0.0);
	}

}
