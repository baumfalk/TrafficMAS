package nl.uu.trafficmas.norm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class MergeNormScheme extends NormScheme {

	private Sensor mainSensor;
	private Sensor rampSensor;
	private Sensor mergeSensor;
	private Set<String> tickedAgents;
	private static int count = 0;
	/**
	 * first sensor: main road
	 * second sensor: ramp
	 * third sensor: after merge
	 * @param sensorList the list of the sensors, used to determine how to merge.
	 */
	public MergeNormScheme(String id, SanctionType sanctionType, List<Sensor> sensorList) {
		super(id,sanctionType,sensorList);
		//super(id, sanctionType, sensorList);
		mainSensor 	= sensorList.get(0);
		rampSensor 	= sensorList.get(1);
		mergeSensor	= sensorList.get(2);
		tickedAgents= new HashSet<String>();
	}

	@Override
	public List<NormInstantiation> instantiateNorms(RoadNetwork rn, Map<String, AgentData> currentOrgKnowledge) {
		
		double vmax = (80/3.6);
		List<AgentData> mainList = mainSensor.readSensor();
		List<AgentData> rampList = rampSensor.readSensor();
		
		removeTicketAgents(mainList);
		removeTicketAgents(rampList);
		
		
		List<AgentData> outputList = mergeTrafficStreams(mainList, rampList, mainSensor.getEndPosition(), rampSensor.getEndPosition());
		for (int i = 0; i < outputList.size(); i++) {
			tickedAgents.add(outputList.get(i).id);
		}
		
		AgentData firstCar = outputList.get(0);
		
		MergeNormInstantiation ni = new MergeNormInstantiation(this, firstCar.id);
		ni.setSpeed(Math.min(vmax, mergeSensor.getLastStepMeanSpeed()));
		List<NormInstantiation> normInstList = new ArrayList<NormInstantiation>();
		
		normInstList.add(ni);
		
		double lastSpeed = ni.getSpeed();
		double lastCarMergePoint= rn.getEdge(firstCar.roadID).getRoad().length;
		double distRemaining	= lastCarMergePoint-firstCar.position;
		double acceleration 	= (firstCar.velocity < lastSpeed) ? firstCar.acceleration : firstCar.deceleration;
		double accelerationTime	= (Math.abs(lastSpeed-firstCar.velocity)/acceleration);
		double accelAvgSpeed	= (ni.getSpeed()-firstCar.velocity)/2;
		double accelerationDist	= accelerationTime*accelAvgSpeed;
		distRemaining			-= accelerationDist;
		double lastCarArrivalTimeMergePoint = accelerationTime + distRemaining/lastSpeed;
		
		double timeBetweenCars	= 2.0;
		
		for (int i = 1; i < outputList.size(); i++) {
			
			AgentData currentCar 	= outputList.get(i);
			lastCarMergePoint 		= rn.getEdge(currentCar.roadID).getRoad().length;
			distRemaining			= lastCarMergePoint - currentCar.position;
			double posAtArrivalTime = currentCar.velocity* (timeBetweenCars + lastCarArrivalTimeMergePoint);
			acceleration 			= (posAtArrivalTime < lastCarMergePoint) ? currentCar.acceleration : currentCar.deceleration;
			lastSpeed 				= findBestSpeed(currentCar.velocity, acceleration, distRemaining, lastCarArrivalTimeMergePoint, timeBetweenCars );
			lastSpeed				= Math.min(lastSpeed, vmax);
			lastCarArrivalTimeMergePoint = getArrivalTime(currentCar.velocity, acceleration, distRemaining, lastSpeed);
			ni = new MergeNormInstantiation(this, currentCar.id);
			ni.setSpeed(lastSpeed);
			normInstList.add(ni);
		}
		
		// determine based on the order the speed for the other cars in front of 
		return normInstList;
	}

	public static double findBestSpeed(double velocity, double acceleration,
			double distRemaining, double lastCarArrivalTimeMergePoint,
			double timeBetweenCars) {
		// TODO Auto-generated method stub
		
		// boostrap depending on if we need to brake or not
		// between 0m/s or 200m/s
		double vprime 	= (acceleration >0) ? 200 : 0;
		double vhigh 	= (acceleration >0) ? 200 : velocity;
		double vlow 	= (acceleration >0) ? velocity : 0;
		double sigma 	= 0.1;
		int steps 		= 20;
		int currentStep = 0;
		double arrivalTime = 0;
		do
		{
			arrivalTime = getArrivalTime(velocity, acceleration, distRemaining, vprime);
			
			double newvprime = (vhigh + vlow)/2;
			if(newvprime < vprime) {
				vhigh = vprime;
			} else{
				vlow = vprime;
			}
			vprime = newvprime;
			
		}while(currentStep < steps 
				&& (arrivalTime < (lastCarArrivalTimeMergePoint + timeBetweenCars)-sigma)
				&& (arrivalTime > (lastCarArrivalTimeMergePoint + timeBetweenCars)+sigma));
		
		
		return vprime;
	}

	private static double getArrivalTime(double velocity, double acceleration,
			double distRemaining, double vprime) {
		return Math.abs((vprime - velocity)/acceleration)+ (distRemaining-
				Math.abs((vprime - velocity)/acceleration)*(vprime+velocity)/2
				)/vprime;
	}

	private void removeTicketAgents(List<AgentData> mainList) {
		for(AgentData ad : mainList) {
			if(tickedAgents.contains(ad.id)) {
				mainList.remove(ad);
			}
		}
	}

	public static List<AgentData> mergeTrafficStreams(List<AgentData> mainList, List <AgentData> rampList, double mainRoadEnd, double rampRoadEnd) {
		List<AgentData> tempMainList = new ArrayList<AgentData>(mainList);
		List<AgentData> tempRampList = new ArrayList<AgentData>(rampList);
		List<AgentData> outputList = new ArrayList<AgentData>();
		boolean cond = true;
		AgentData m = null;
		AgentData r = null;
		double t_r = Double.POSITIVE_INFINITY;
		double t_m = Double.POSITIVE_INFINITY;
		while(!(tempRampList.isEmpty() && r== null)) {
			if(r==null) {
				r = tempRampList.remove(0); 
			}
			
			if(tempMainList.isEmpty() && m== null) {
				outputList.add(r);
				r = null;
			} else {
				do {
					
					if(cond) {
						m = tempMainList.remove(0);
						double distanceUntilEndOfMainRoad = mainRoadEnd - m.position;
						t_m = distanceUntilEndOfMainRoad/m.velocity;
						cond = false;
					}
					double distanceUntilEndOfRamp = rampRoadEnd - r.position;
					t_r = distanceUntilEndOfRamp / r.velocity;
					if(t_m > t_r) {
						outputList.add(r);
						r = null;
					} else {
						outputList.add(m);
						cond = true;
						m = null;
					}
				}while(!tempMainList.isEmpty() && cond);
			}
		}
		
		if(m != null) {
			outputList.add(m);
		}
		outputList.addAll(tempMainList);
	
		return outputList;
	}
	
	@Override
	public boolean checkCondition(Map<String, AgentData> currentOrgKnowledge) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean violated(AgentData ad) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deadline(Map<String, AgentData> currentOrgKnowledge,
			int currentTime) {
		// TODO Auto-generated method stub
		return false;
	}
}
