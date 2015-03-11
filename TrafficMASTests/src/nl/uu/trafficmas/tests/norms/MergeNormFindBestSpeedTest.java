package nl.uu.trafficmas.tests.norms;

import static org.junit.Assert.*;
import nl.uu.trafficmas.exception.InvalidParameterCombination;
import nl.uu.trafficmas.exception.InvalidDistanceParameter;
import nl.uu.trafficmas.norm.MergeNormScheme;

import org.junit.Test;

public class MergeNormFindBestSpeedTest {


	@Test(expected=InvalidDistanceParameter.class)
	public void noDistRemaining() throws Exception {
		double velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars, result;
		velocity						= 10;
		acceleration					= 1;
		distRemaining					= 0.0;
		lastCarArrivalTimeMergePoint	= 2.0;	
		timeBetweenCars					= 2.0;
		
		MergeNormScheme.findBestSpeed(velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars);
	}
	
	@Test(expected=InvalidDistanceParameter.class)
	public void negDistance() throws Exception {
		double velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars, result;
		velocity						= 10;
		acceleration					= 1;
		distRemaining					= -100.0;
		lastCarArrivalTimeMergePoint	= 2.0;	
		timeBetweenCars					= 2.0;
		
		MergeNormScheme.findBestSpeed(velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars);
	}

	@Test(expected=InvalidDistanceParameter.class)
	public void negVelocity() throws Exception {
		double velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars, result;
		velocity						= -22;
		acceleration					= 1;
		distRemaining					= 200.0;
		lastCarArrivalTimeMergePoint	= 10.0;	
		timeBetweenCars					= 2.0;
		
		result = MergeNormScheme.findBestSpeed(velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars);
		assertEquals(0.0, result, 0.0);
	}
	
	@Test
	public void normalSituation() throws Exception {
		double velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars, result;
		velocity						= (80/3.6);
		acceleration					= 1;
		distRemaining					= 300.0;
		lastCarArrivalTimeMergePoint	= 10.0;	
		timeBetweenCars					= 2.0;
		
		result = MergeNormScheme.findBestSpeed(velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars);
		assertEquals((100/3.6), result, 0.0);
	}
	
	
	@Test(expected=InvalidParameterCombination.class)
	public void noTimeBetweenCars() throws Exception {
		double velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars, result;
		velocity						= 80.0;
		acceleration					= 1.0;
		distRemaining					= 200.0;
		lastCarArrivalTimeMergePoint	= 0.0;	
		timeBetweenCars					= 0.0;
		
		result = MergeNormScheme.findBestSpeed(velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars);
	}

	//@Test
	public void case3() throws Exception {
		double velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars, result;
		velocity						= 0.0;
		acceleration					= 0.0;
		distRemaining					= 0.0;
		lastCarArrivalTimeMergePoint	= 0.0;	
		timeBetweenCars					= 0.0;
		
		result = MergeNormScheme.findBestSpeed(velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars);
		assertEquals(3.0, result, 0.0);
	}
}


