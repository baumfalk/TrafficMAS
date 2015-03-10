package nl.uu.trafficmas.norm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nl.uu.trafficmas.exception.InvalidParameterCombination;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class MergeNormScheme extends NormScheme {

	private Sensor mainSensor;
	private Sensor rampSensor;
	private Sensor mergeSensor;
	private Set<String> tickedAgents;
	private static int count = 0;
	private static List<Sensor> sensors;
	

	/**
	 * first sensor: main road
	 * second sensor: ramp
	 * third sensor: after merge
	 * @param sensorList the list of the sensors, used to determine how to merge.
	 */
	public MergeNormScheme(String id, SanctionType sanctionType, List<Sensor> sensorList) {
		super(id,sanctionType,sensorList);
		sensors		= sensorList;
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
		
		removeTickedAgents(mainList);
		removeTickedAgents(rampList);
		
		
		List<AgentData> outputList = mergeTrafficStreams(mainList, rampList, mainSensor.getEndPosition(), rampSensor.getEndPosition());
		for (int i = 0; i < outputList.size(); i++) {
			tickedAgents.add(outputList.get(i).id);
		}
		
		currentOrgKnowledge.isEmpty(); 
		AgentData firstCar = outputList.get(0);
		double normInstSpeed;
		MergeNormInstantiation ni = new MergeNormInstantiation(this, firstCar.id);
		if(mergeSensor.getLastStepMeanSpeed() == 0.0){
			normInstSpeed = vmax;
		} else{
			normInstSpeed = Math.min(vmax, mergeSensor.getLastStepMeanSpeed());
		}
		ni.setSpeed(normInstSpeed);
		List<NormInstantiation> normInstList = new ArrayList<NormInstantiation>();
		
		normInstList.add(ni);
		
		double lastSpeed = ni.getSpeed();
		double lastCarMergePoint= rn.getEdge(firstCar.roadID).getRoad().length;
		double distRemaining	= lastCarMergePoint-firstCar.position;
		double acceleration 	= (firstCar.velocity < lastSpeed) ? firstCar.acceleration : firstCar.deceleration;
		double accelerationTime	= (Math.abs(lastSpeed-firstCar.velocity)/acceleration);
		double accelAvgSpeed	= (ni.getSpeed()+firstCar.velocity)/2;
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

	public static double getArrivalTime(double velocity, double acceleration,
			double distRemaining, double vprime) throws InvalidParameterCombination {
		
		double accelerationTime = Math.abs((vprime - velocity)/acceleration);
		double accelerationDist = accelerationTime*(vprime+velocity)/2;
		
		boolean positiveSpeedDeltaNegativeAccel = velocity < vprime && acceleration <= 0;
		boolean negativeSpeedDeltaPositiveAccel = velocity > vprime && acceleration >= 0;
		boolean notEnoughDistRemaining = (accelerationDist > distRemaining);
		if(positiveSpeedDeltaNegativeAccel || negativeSpeedDeltaPositiveAccel || notEnoughDistRemaining)
			throw new InvalidParameterCombination();
		double remainingTime = (distRemaining-accelerationDist)/vprime;
		return accelerationTime+ remainingTime;
	}

	private void removeTickedAgents(List<AgentData> mainList) {
		Iterator<AgentData> iter = mainList.iterator();
		while (iter.hasNext()) {
		    AgentData ad = iter.next();
		    if (tickedAgents.contains(ad.id))
		        iter.remove();
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
		for(Entry<String, AgentData> entry : currentOrgKnowledge.entrySet()){
			// TODO: replace arbitratry hardcoded 90.
			String str = rampSensor.lane.getRoadID();
			if(entry.getValue().roadID.equals(rampSensor.lane.getRoadID()) && entry.getValue().position > 90 && !tickedAgents.contains(entry.getKey()))
				return true;
		} 
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
	
	public static List<Sensor> getSensors(){
		return sensors;
	}

	@Override
	public AgentData goal() {
		return null;
	}

}
