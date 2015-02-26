package nl.uu.trafficmas.norm;

import java.util.ArrayList;
import java.util.List;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class MergeNormScheme extends NormScheme {

	private List<AgentData> mainList;
	private List<AgentData> rampList;
	private Sensor mainSensor;
	private Sensor rampSensor;
	private Sensor mergeSensor;
	private List<AgentData> outputList;
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
	}

	@Override
	public List<NormInstantiation> instantiateNorms(List<Agent> agents,RoadNetwork rn) {
		// TODO Auto-generated method stub
		this.runAlgorithm(rn);
		
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
	protected void runAlgorithm(RoadNetwork rn) {
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
	
	public static List<Double> calculateNewSpeeds(List<AgentData> outputList, RoadNetwork rn) {
		List<Double> newSpeeds = new ArrayList<Double>(outputList.size());
		newSpeeds.add(outputList.get(0).velocity);
		for(int i=0;i<outputList.size()-1;i++) {			
			AgentData first = outputList.get(i);
			AgentData second = outputList.get(i+1);
			double firstDistRemaining = rn.getEdge(first.roadID).getRoad().length - first.position;
			double secondDistRemaining = rn.getEdge(second.roadID).getRoad().length - second.position;

			double secondSpeed = findBestSpeed(first,second,firstDistRemaining,secondDistRemaining);
			System.out.println("Speed that is being added:"+secondSpeed);
			newSpeeds.add(secondSpeed);
		}
		
		return newSpeeds;
	}

	public static double findBestSpeed(AgentData first, AgentData second, double firstDistRemaining, double secondDistRemaining) {
		double lambda;
		double c_zero_pos 	= firstDistRemaining;
		double c_one_pos 	= secondDistRemaining;
		double c_zero_v		= first.velocity;
		double c_one_v		= second.velocity;
		
		double c_one_a		= (c_zero_v < c_one_v) ? -5 : 2;// TODO: use first.deceleration for this;
		double new_v_high 	= c_one_v;
		double new_v_low	= c_zero_v;
		double c_one_v_new	= new_v_high;
		double lambda_sigma = 0.1;
		double lambda_goal	= 4;
		int numberOfAttempts= 20;
		int attempt = 1;
		do
		{
			lambda = calcLambda(c_zero_v,c_one_v,c_one_v_new,c_zero_pos,c_one_pos,c_one_a);
			System.out.println("Attempt #"+attempt++ + "lambda:"+lambda+" newspeed:"+c_one_v_new);
			double lambdaInf = calcLambda(c_zero_v,c_one_v,c_one_v_new,c_zero_pos,c_one_pos,Double.NEGATIVE_INFINITY);
			System.out.println("\t With decel = infinity, lambda = "+lambdaInf);
			if(lambda < lambda_goal - lambda_sigma) {
				new_v_high = c_one_v_new;
				c_one_v_new = (new_v_low+new_v_high)/2;
			} else if(lambda > lambda_goal + lambda_sigma) {
				new_v_low = c_one_v_new;
				c_one_v_new = (new_v_low+new_v_high)/2;
			}
		}while((lambda < lambda_goal - lambda_sigma || lambda > lambda_goal + lambda_sigma) && attempt < numberOfAttempts);
		
		//unrealistic results, keep the original speed
		if(lambda < 0) {
			c_one_v_new = c_one_v;
		}
		System.out.println("final speed:"+ c_one_v_new);
		return c_one_v_new;
	}
	
	public static double calcLambda(double v0, double v1, double vprime, double s0, double s1, double a1 ) {
		System.out.println("CalcLambdaFunction\nv0:"+v0+"\nv1:"+v1+"\nvprime:"+vprime+"\ns0:"+s0+"\ns1:"+s1+"\na1:"+a1);
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
		
		System.out.println("\t deceltime:"+decelTime);
		System.out.println("\t decelAvrgSpeed:"+ decelAvrgSpeed);
		System.out.println("\t decelDist:"+ decelDist);
		System.out.println("\t distAfterDecel:"+ distAfterDecel);
		System.out.println("\t c0TravelTime:"+ c0TravelTime);
		System.out.println("\t timeAfterDecel:"+ timeAfterDecel);
		System.out.println("\t distNewSpeed:"+ distNewSpeed);
		System.out.println("\t diffDist:"+ diffDist);
		System.out.println("\t diffVel:"+ diffVel);
		System.out.println("\t lambda:"+lambda);
		System.out.println();
		return lambda;
	}
}
