package nl.uu.trafficmas.tests.norms;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import nl.uu.trafficmas.norm.MergeNormScheme;
import nl.uu.trafficmas.simulationmodel.AgentData;

import org.junit.Test;

public class MergeNormTest {

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
		
		int mainSensorEnd = 20; // mainroad is 20 long before merge
		int rampSensorEnd = 10; // ramp road is 10 long before merge

		List<AgentData> outputList = MergeNormScheme.mergeTrafficStreams(mainList, rampList, mainSensorEnd, rampSensorEnd);
		for(AgentData agentdata : outputList) {
			System.out.println(agentdata.id);
		}
		
		double distanceBetween = 2;//at least 2m between cars
		for(int i=0;i<outputList.size();i++) {
			if(i!= outputList.size()-1) {
				AgentData agentFirst 	= outputList.get(i);
				AgentData agentSecond 	= outputList.get(i+1);
				
				double agentFirstTime  = 0;
				if(agentFirst.id.contains("main")) {
					agentFirstTime = (mainSensorEnd-agentFirst.position)/agentFirst.velocity;
				} else {
					agentFirstTime = (rampSensorEnd-agentFirst.position)/agentFirst.velocity;
				}
				double agentSecondPosAtPrevMerge = agentSecond.position+(agentFirstTime*agentSecond.velocity);
				if(agentSecond.id.contains("main")) {
					
				}

				
				
				
				//speed: at least 2m between agentFirst and agentSecond when agentFirst starts its merge
				
				
				
			}
		}
		
		fail("Not yet implemented");
	}

}
