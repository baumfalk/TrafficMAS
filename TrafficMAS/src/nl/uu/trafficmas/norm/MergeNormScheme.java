package nl.uu.trafficmas.norm;

import java.util.ArrayList;
import java.util.List;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class MergeNormScheme extends NormScheme {

	private List<AgentData> mainList;
	private List<AgentData> rampList;
	private Sensor mainSensor;
	private Sensor rampSensor;
	private Sensor mergeSensor;
	private List<AgentData> outputList;

	/**
	 * first sensor: main road
	 * second sensor: ramp
	 * third sensor: after merge
	 * @param sensorList the list of the sensors, used to determine how to merge.
	 */
	public MergeNormScheme(List<Sensor> sensorList) {
		super(sensorList);
		mainSensor 	= sensorList.get(0);
		rampSensor 	= sensorList.get(1);
		mergeSensor	= sensorList.get(2);
	}

	@Override
	public List<NormInstantiation> instantiateNorms(List<Agent> agents) {
		// TODO Auto-generated method stub
		this.runAlgorithm();
		
		// determine based on the order the speed for the other cars in front of 
		
		
		return null;
	}

	@Override
	public boolean checkCondition() {
	
		return true;
	}


	/**
	 * Based on the algorithm in the paper 
	 * 		Proactive Traffic Merging Strategies for Sensor-Enabled Cars
	 * by Wang et al
	 */
	@Override
	protected void runAlgorithm() {
		mainList = mainSensor.readSensor();
		rampList = rampSensor.readSensor();
		outputList = mergeTrafficStreams(mainList, rampList, mainSensor.getEndPosition(), rampSensor.getEndPosition());
		// TODO Auto-generated method stub
		
		// read cars from first and second sensor
		// make sure lists are ordered based on position of cars on road
		// while there are still cars on the ramp that are unassigned
		// get the first unassigned car
		// 
		// if the car has passed the decision point (assumption: we only have sensors
		// for the area of the decision point, so this if is nt needed)
		
	}

	
	
	public static List<AgentData> mergeTrafficStreams(List<AgentData> mainList, List <AgentData> rampList, double mainSensorEnd, double rampSensorEnd) {
		
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
						double distanceUntilEndOfSensorMainRoad = mainSensorEnd - m.position;
						t_m = distanceUntilEndOfSensorMainRoad/m.velocity;
						cond = false;
					}
					double distanceUntilEndOfSensorRamp = rampSensorEnd - r.position;
					t_r = distanceUntilEndOfSensorRamp / r.velocity;
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
}
