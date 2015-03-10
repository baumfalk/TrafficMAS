package nl.uu.trafficmas.tests.norms;

import static org.junit.Assert.*;
import nl.uu.trafficmas.norm.MergeNormScheme;

import org.junit.Test;

public class MergeNormFindBestTimeTest {


	@Test
	public void case1() {
		double velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars, result;
		velocity						= 0.0;
		acceleration					= 0.0;
		distRemaining					= 0.0;
		lastCarArrivalTimeMergePoint	= 0.0;	
		timeBetweenCars					= 0.0;
		
		result = MergeNormScheme.findBestSpeed(velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars);
		assertEquals(0.0, result, 0.0);
	}

	@Test
	public void case2() {
		double velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars, result;
		velocity						= 0.0;
		acceleration					= 0.0;
		distRemaining					= 0.0;
		lastCarArrivalTimeMergePoint	= 0.0;	
		timeBetweenCars					= 0.0;
		
		result = MergeNormScheme.findBestSpeed(velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars);
		assertEquals(0.0, result, 0.0);
	}

	@Test
	public void case3() {
		double velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars, result;
		velocity						= 0.0;
		acceleration					= 0.0;
		distRemaining					= 0.0;
		lastCarArrivalTimeMergePoint	= 0.0;	
		timeBetweenCars					= 0.0;
		
		result = MergeNormScheme.findBestSpeed(velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars);
		assertEquals(0.0, result, 0.0);
	}
}
