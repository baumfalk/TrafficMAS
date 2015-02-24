package nl.uu.trafficmas.tests.norms;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import nl.uu.trafficmas.norm.MergeNormScheme;
import nl.uu.trafficmas.simulationmodel.AgentData;

import org.junit.Test;

public class MergeNormTest {

	@Test
	public void calcTest() {
		
		double lambda 		= 2.0; 
		double c_zero_pos 	= 10;
		double c_one_pos 	= 20;
		double c_zero_v		= (80/3.6);
		double c_one_v		= (100/3.6);
		
		System.out.println("Current speed car zero: "+ c_zero_v);
		System.out.println("Current speed car one: "+ c_one_v);
		
		double c_one_v_new	=	(1/lambda*c_one_pos+c_zero_v)/(1+(1/lambda)*(c_zero_pos/c_zero_v));
		System.out.println("Speed for car one with " +lambda+ " seconds before collission at merge point: "+c_one_v_new+"m/s");
		System.out.println("New speed in km/h: " +c_one_v_new*3.6);
		System.out.println("Difference:" + (c_one_v_new-c_one_v));
		
		fail("Not implemented yet");
	}
	
	@Test
	public void calcTestWithDecel() {
		
		double c_zero_pos 	= 10;
		double c_one_pos 	= 20;
		double c_zero_v		= (80/3.6);
		double c_one_v		= (100/3.6);
		double c_one_v_new	= c_one_v;
		double c_one_a		= -5;
		double lambda_sigma = 0.01;
		double lambda_goal	= 2;
		System.out.println("Current speed car zero: "+ c_zero_v);
		System.out.println("Current speed car one: "+ c_one_v);
		
		
		System.out.println("Starting estimation");
		
		for(int i = (int) c_one_v; i > 0; i--) {
			System.out.println("Speed:"+i+" Lambda="+calcLambda(c_zero_v,c_one_v,i,c_zero_pos,c_one_pos,c_one_a));
		}

		int attempt = 1;
		double lambda = 0;
		do
		{
			lambda = calcLambda(c_zero_v,c_one_v,c_one_v_new,c_zero_pos,c_one_pos,c_one_a);
			System.out.println("Attempt #"+attempt+ " v': " +c_one_v_new);
			System.out.println("Lambda:"+calcLambda(c_zero_v,c_one_v,c_one_v_new,c_zero_pos,c_one_pos,c_one_a) );
			System.out.println("Lambda with infinite decel:"+calcLambda(c_zero_v,c_one_v,c_one_v_new,c_zero_pos,c_one_pos,Double.NEGATIVE_INFINITY) );
			attempt++;
			if(lambda < lambda_goal - lambda_sigma) {
				c_one_v_new = (c_zero_v+c_one_v_new)/2;
			} else if(lambda > lambda_goal + lambda_sigma) {
				c_one_v_new = (c_one_v+c_one_v_new)/2;
			}
		}while(lambda < lambda_goal - lambda_sigma || lambda > lambda_goal + lambda_sigma );
		
		System.out.println("Speed for car one with " +lambda+ " seconds before collission at merge point: "+c_one_v_new+"m/s");
		System.out.println("New speed in km/h: " +c_one_v_new*3.6);
		System.out.println("Difference:" + (c_one_v_new-c_one_v));
		
		fail("Not implemented yet");
	}
	
	
	
	public double calcLambda(double v0, double v1, double vprime, double s0, double s1, double a1 ) {
		
		double decelTime = (vprime - v1)/a1;
		double decelAvrgSpeed = (v1+vprime)/2;
		double decelDist = decelAvrgSpeed * decelTime;
		
		double distAfterDecel = s1-decelDist;
		double c0TravelTime	= s0/v0;
		double timeAfterDecel = c0TravelTime - decelTime;
		double distNewSpeed	= timeAfterDecel * vprime;
		
		double diffDist = distAfterDecel - distNewSpeed;
		double diffVel	= vprime - v0;
		
		double lambda = diffDist/diffVel;
		
		
		return lambda;
	}
	
	@Test
	public void test() {
		List<AgentData>mainList = new ArrayList<AgentData>();
		
		int positionMainCar 	= 9;
		int speedMainCar 		= 8;
		int laneIndexMainCar 	= 0;
		
		mainList.add(new AgentData("main_1", null, positionMainCar, speedMainCar, null, laneIndexMainCar));
		
		positionMainCar 	= 1;
		speedMainCar 		= 8;
		laneIndexMainCar 	= 0;
		
		mainList.add(new AgentData("main_2", null, positionMainCar, speedMainCar, null, laneIndexMainCar));

		
		List<AgentData>rampList = new ArrayList<AgentData>();
		int positionRampCar 	= 6;
		int speedRampCar 		= 5;
		int laneIndexRampCar 	= 0;

		rampList.add(new AgentData("merge_1", null, positionRampCar, speedRampCar, null, laneIndexRampCar));

		positionRampCar 	= 3;
		speedRampCar 		= 55;
		laneIndexRampCar 	= 0;

		rampList.add(new AgentData("merge_2", null, positionRampCar, speedRampCar, null, laneIndexRampCar));
		
		int mainSensorEnd = 10; // mainroad is 20 long before merge
		int rampSensorEnd = 10; // ramp road is 10 long before merge

		List<AgentData> outputList = MergeNormScheme.mergeTrafficStreams(mainList, rampList, mainSensorEnd, rampSensorEnd);
		for(AgentData agentdata : outputList) {
			System.out.println(agentdata.id);
		}
		
		
		
//		
//		double distanceBetween = 2;//at least 2m between cars
//		for(int i=0;i<outputList.size();i++) {
//			if(i!= outputList.size()-1) {
//				AgentData agentFirst 	= outputList.get(i);
//				AgentData agentSecond 	= outputList.get(i+1);
//				
//				double agentFirstTime  = 0;
//				if(agentFirst.id.contains("main")) {
//					agentFirstTime = (mainSensorEnd-agentFirst.position)/agentFirst.velocity;
//				} else {
//					agentFirstTime = (rampSensorEnd-agentFirst.position)/agentFirst.velocity;
//				}
//				double agentSecondPosAtPrevMerge = agentSecond.position+(agentFirstTime*agentSecond.velocity);
//				if(agentSecond.id.contains("main")) {
//					
//				}
//				//speed: at least 2m between agentFirst and agentSecond when agentFirst starts its merge
//			}
//		}
		
		fail("Not yet implemented");
	}

}
